package com.cheatbreaker.client.module.type;

/** Local-space geometry shared by live and HUD-editor player lists. */
public final class TabListLayout {
   public final int rows;
   public final int columns;
   public final int columnWidth;
   public final int width;

   public TabListLayout(int playerCount, int contentWidth, int availableWidth) {
      int count = Math.max(0, Math.min(80, playerCount));
      this.columns = Math.max(1, (count + 19) / 20);
      this.rows = (count + this.columns - 1) / this.columns;
      int gaps = (this.columns - 1) * 5;
      this.columnWidth = Math.max(1, Math.min(contentWidth, (Math.max(1, availableWidth) - gaps) / this.columns));
      this.width = this.columns * this.columnWidth + gaps;
   }

   public int columnX(int index) {
      return this.rows == 0 ? 0 : index / this.rows * (this.columnWidth + 5);
   }

   public int rowY(int index) {
      return this.rows == 0 ? 0 : index % this.rows * 9;
   }
}
