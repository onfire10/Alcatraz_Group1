//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api;

import impl.Figure;

public class Prisoner extends Figure {
    private static final long serialVersionUID = -6603387237232801473L;
    private int id;

    public Prisoner(int id, int row, int col) {
        super(row, col);
        this.id = id;
    }

    public Prisoner(Prisoner prisoner) {
        super(prisoner);
        this.id = prisoner.getId();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Prisoner) {
            Prisoner p = (Prisoner)obj;
            if (p.getId() == this.getId()) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return this.id;
    }

    public String toString() {
        return "Prisoner[" + this.getId() + "]";
    }
}
