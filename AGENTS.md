# CheatBreaker 1.8.9 工作约定

## 完成流程

- 每轮先完成修改、构建、部署及报告更新，再将全部变更一次性提交，说明修改内容并保持工作区干净。每轮只提交一次；不自动推送，由用户手动推送。
- 执行 `mvnw.cmd clean package` 全量构建，编译、Java 测试、recovery audit 和成品 JAR 构造器链接检查须通过；不复用旧 JAR 或增量打包。
- 成功后依次运行 `python tools/package_client.py`、`python tools/deploy_neo.py`，以后统一部署到 `C:\Users\hp\AppData\Roaming\.minecraft\versions\CheatBreaker 1.8.9`。
- 更新 JAR、JSON、natives，保留脚本备份及用户配置、存档等数据。核对 JAR SHA-256、全部 natives；JSON 的 id/jar 为 `CheatBreaker 1.8.9`，clientVersion 为 `1.8.9`。
- 最终说明提交号、构建/测试结果、部署目录；未验证游戏内效果须如实说明。

## 范围与命名

- 编译源码：`src/main/java`；反编译参考：`recovery/decompiled-complete`。不修改参考项目 `F:\Work\CheatBreakerZ`。
- 构建、日志、备份放在 `.target/`，不提交。正式构建及打包部署只用 `.target/maven-source/`，与 IDE 的 `.target/maven/` 隔离。
- 繁体中文使用 `zh-Hant`（文字体系）命名，不用地区代码；README 语言入口使用居中的“🌐 README available in:”文本链接。
