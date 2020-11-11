//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package impl;

public class Guard extends Figure {
    private static final long serialVersionUID = -2893243985978967034L;

    public Guard(int row, int col) {
        super(row, col);
    }

    public Guard(Guard guard) {
        super(guard);
    }

    public String toString() {
        return "Guard" + this.getRow();
    }
}
