//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package impl;

import api.Prisoner;

import java.util.ArrayList;

public class Move {
    private Guard guard = null;
    private ArrayList prisoners = new ArrayList();

    public Move() {
    }

    public void saveGuard(Guard guard) {
        if (guard != null) {
            this.guard = new Guard(guard);
        }

    }

    public void restoreGuard(Guard[] guards) {
        if (this.guard != null) {
            guards[this.guard.getRow()].setCol(this.guard.getCol());
        }

    }

    public void addPrisoner(Prisoner prisoner) {
        this.prisoners.add(new Prisoner(prisoner));
    }

    public void restorePrisoners(Prisoner[] prisoners) {
        for(int i = 0; i < this.prisoners.size(); ++i) {
            Prisoner prisoner = (Prisoner)this.prisoners.get(i);
            prisoners[prisoner.getId()].setCol(prisoner.getCol());
            prisoners[prisoner.getId()].setRow(prisoner.getRow());
        }

    }
}
