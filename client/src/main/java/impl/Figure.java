//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package impl;

import java.io.Serializable;

public abstract class Figure implements Serializable {
    private int row;
    private int col;

    public Figure(int row, int col) {
        this.setRow(row);
        this.setCol(col);
    }

    public Figure(Figure player) {
        this.setRow(player.getRow());
        this.setCol(player.getCol());
    }

    public int getCol() {
        return this.col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
