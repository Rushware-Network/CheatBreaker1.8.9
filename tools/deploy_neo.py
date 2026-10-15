"""Deploy the recovered source build, backing up existing version artifacts."""
import argparse
import datetime
import hashlib
import json
import pathlib
import shutil
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('--version', default='CheatBreakerNeo-1.8.9', help='Launcher version directory name')
VERSION = parser.parse_args().version
if not VERSION or VERSION in ('.', '..') or any(c in VERSION for c in '<>:"/\\|?*') or VERSION.endswith((' ', '.')):
    parser.error('--version must be a single Windows directory name')
DEST = pathlib.Path(r'C:\Users\hp\AppData\Roaming\.minecraft\versions') / VERSION
JAR = ROOT / '.target/maven-source/CheatBreaker1.8.9.jar'
assert JAR.is_file(), 'Build the source JAR first'
with zipfile.ZipFile(JAR) as archive:
    names = set(archive.namelist())
    assert 'net/minecraft/client/main/Main.class' in names
    assert 'com/cheatbreaker/client/CheatBreaker.class' in names
    assert not any(n.startswith(('OfflineSmokeLaunch', 'RecoverySmokeProbe')) for n in names)
DEST.mkdir(parents=True, exist_ok=True)
backup = ROOT / '.target/deployment-backups' / datetime.datetime.now().strftime('%Y%m%d-%H%M%S')
targets = [DEST / (VERSION + '.jar'), DEST / (VERSION + '.json'), DEST / (VERSION + '-natives')]
for target in targets:
    if target.exists():
        backup.mkdir(parents=True, exist_ok=True)
        if target.is_dir(): shutil.copytree(target, backup / target.name)
        else: shutil.copy2(target, backup / target.name)
shutil.copy2(JAR, targets[0])
manifest = json.loads((ROOT / 'build/original-version.json').read_text('utf-8'))
manifest['id'] = VERSION
manifest['jar'] = VERSION
manifest['clientVersion'] = '1.8.9'
manifest['type'] = 'custom'
manifest.pop('downloads', None)
targets[1].write_text(json.dumps(manifest, indent=2), 'utf-8')
def copy_native_if_changed(source, destination):
    target = pathlib.Path(destination)
    if target.is_file() and pathlib.Path(source).read_bytes() == target.read_bytes():
        return str(target)  # Loaded DLLs cannot be overwritten on Windows.
    return shutil.copy2(source, destination)

shutil.copytree(ROOT / 'natives', targets[2], dirs_exist_ok=True, copy_function=copy_native_if_changed)
digest = lambda p: hashlib.sha256(p.read_bytes()).hexdigest()
assert digest(targets[0]) == digest(JAR)
deployed_manifest = json.loads(targets[1].read_text('utf-8'))
assert deployed_manifest['id'] == deployed_manifest['jar'] == VERSION
assert deployed_manifest['clientVersion'] == '1.8.9'
natives = list((ROOT / 'natives').rglob('*'))
for native in natives:
    if native.is_file(): assert digest(native) == digest(targets[2] / native.relative_to(ROOT / 'natives'))
report = {'version': VERSION, 'directory': str(DEST), 'source_jar': str(JAR),
          'jar_sha256': digest(JAR), 'jar_bytes': JAR.stat().st_size,
          'compiled_classes': len([n for n in names if n.endswith('.class')]),
          'native_files_verified': sum(p.is_file() for p in natives),
          'backup': str(backup) if backup.exists() else None,
          'deployed_at': datetime.datetime.now().astimezone().isoformat()}
report_name = 'neo-deployment.json' if VERSION == 'CheatBreakerNeo-1.8.9' else 'client-deployment.json'
(ROOT / 'recovery' / report_name).write_text(json.dumps(report, indent=2), 'utf-8')
print(json.dumps(report, indent=2))
