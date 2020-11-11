//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 4350145457376879396L;
    private int id;
    private String name;

    public Player(int id) {
        this.id = id;
    }

    public Player(Player player) {
        this.id = player.getId();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            Player p = (Player)obj;
            if (p.getId() == this.getId()) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        return hash;
    }

    public String toString() {
        String result = "Player[" + this.getId();
        if (this.name != null) {
            result = result + ", " + this.name;
        }

        result = result + "]";
        return result;
    }
}
