//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package impl;

import api.MoveListener;
import api.Player;
import api.Prisoner;

import java.util.ArrayList;
import java.util.Iterator;

public class Game {
    public static final int NUM_GUARDS = 9;
    public static final int PRISONERS_PER_PLAYER = 4;
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;
    public static final int ROW = 0;
    public static final int COL = 1;
    public static final int WIN = 2;
    public static final int ROWS = 9;
    public static final int COLS = 9;
    private Guard[] guards;
    private Prisoner[] prisoners;
    private ArrayList<Move> moveHistory = new ArrayList();
    private int noPlayers;
    private Player currPlayer;
    private Player myPlayer;
    private Player[] players;
    private ArrayList<MoveListener> listeners = new ArrayList();
    private boolean initialized = false;

    public Game() {
    }

    public Game(Game game, boolean withListeners) {
        if (game.isInitialized()) {
            this.guards = new Guard[9];
            this.prisoners = new Prisoner[4 * game.getNoPlayers()];
            this.players = new Player[game.getNoPlayers()];

            int i;
            for(i = 1; i < 9; ++i) {
                this.guards[i] = new Guard(game.guards[i]);
            }

            for(i = 0; i < 4 * game.getNoPlayers(); ++i) {
                this.prisoners[i] = new Prisoner(game.prisoners[i]);
            }

            for(i = 0; i < game.getNoPlayers(); ++i) {
                this.players[i] = new Player(game.players[i]);
            }

            this.currPlayer = game.currPlayer;
            this.myPlayer = game.myPlayer;
            if (withListeners) {
                this.listeners = (ArrayList)game.listeners.clone();
            }

        }
    }

    public void initialize(int noPlayers, int myPlayerId) throws IllegalArgumentException {
        if (noPlayers < 2) {
            throw new IllegalArgumentException("Too few players. At least two players required.");
        } else if (noPlayers > 4) {
            throw new IllegalArgumentException("Too many players. At most four players allowed.");
        } else if (myPlayerId >= 0 && myPlayerId < noPlayers) {
            this.setNoPlayers(noPlayers);
            this.guards = new Guard[9];
            this.prisoners = new Prisoner[4 * this.getNoPlayers()];
            this.players = new Player[this.getNoPlayers()];
            this.guards[1] = new Guard(1, 0);
            this.guards[2] = new Guard(2, 8);
            this.guards[3] = new Guard(3, 1);
            this.guards[4] = new Guard(4, 7);
            this.guards[5] = new Guard(5, 2);
            this.guards[6] = new Guard(6, 6);
            this.guards[7] = new Guard(7, 3);
            this.guards[8] = new Guard(8, 4);

            int i;
            for(i = 0; i < 4 * this.getNoPlayers(); ++i) {
                this.prisoners[i] = new Prisoner(i, -1, i % 4);
            }

            for(i = 0; i < this.getNoPlayers(); ++i) {
                this.players[i] = new Player(i);
            }

            this.setCurrPlayer(this.players[0]);
            this.setMyPlayer(this.players[myPlayerId]);
            this.moveHistory.clear();
            this.initialized = true;
        } else {
            throw new IllegalArgumentException("Player number is out of range.");
        }
    }

    public void reset() {
        if (this.isInitialized()) {
            this.initialize(this.getNoPlayers(), this.getMyPlayer().getId());
        }
    }

    public Prisoner getPrisoner(int row, int col) {
        if (row >= 9) {
            return null;
        } else {
            for(int i = 0; i < 4 * this.getNoPlayers(); ++i) {
                if (this.prisoners[i].getRow() == row && this.prisoners[i].getCol() == col) {
                    if (row != -1) {
                        return this.prisoners[i];
                    }

                    if (this.isOwnPrisoner(this.getCurrPlayer(), this.prisoners[i])) {
                        return this.prisoners[i];
                    }
                }
            }

            return null;
        }
    }

    public boolean isOwnPrisoner(Player player, Prisoner prisoner) {
        return prisoner.getId() / 4 == player.getId();
    }

    public boolean hasWon(Player player) {
        for(int i = player.getId() * 4; i < (player.getId() + 1) * 4; ++i) {
            if (this.prisoners[i].getRow() != 9) {
                return false;
            }
        }

        return true;
    }

    public int getScore(Player player) {
        int[] scores = new int[this.getNoPlayers()];
        int[] left = new int[this.getNoPlayers()];
        int[] right = new int[this.getNoPlayers()];

        int i;
        int playerId;
        for(i = 0; i < 4 * this.getNoPlayers(); ++i) {
            playerId = i / 4;
            scores[playerId] += this.prisoners[i].getRow() * 3;
            if (this.prisoners[i].getRow() == 9) {
                scores[playerId] += 5000;
            }

            if (this.prisoners[i].getRow() == -1) {
                scores[playerId] -= 50;
            }

            if (this.prisoners[i].getRow() >= 6 && this.prisoners[i].getCol() > 9 - this.prisoners[i].getRow() && this.prisoners[i].getCol() < 9 - (9 - this.prisoners[i].getRow() + 1)) {
                scores[playerId] += 500;
            }

            if (this.prisoners[i].getRow() >= 0 && this.prisoners[i].getRow() < 9) {
                int var10002;
                if (this.prisoners[i].getCol() < 4) {
                    var10002 = left[playerId]++;
                } else {
                    var10002 = right[playerId]++;
                }
            }
        }

        i = 0;

        for(playerId = 0; playerId < this.getNoPlayers(); ++playerId) {
            if (Math.abs(left[playerId] - right[playerId]) < 2) {
                scores[playerId] += 100;
            }

            if (playerId == player.getId()) {
                i += scores[playerId];
            } else {
                i -= scores[playerId] / (this.getNoPlayers() - 1);
            }
        }

        return i;
    }

    public int[][] getValidMoves(Prisoner prisoner) {
        int[][] moves = new int[3][9];
        int guardInCol;
        if (prisoner.getRow() >= 9) {
            for(guardInCol = 1; guardInCol < 9; ++guardInCol) {
                moves[0][guardInCol] = 0;
            }

            for(guardInCol = 1; guardInCol < 9; ++guardInCol) {
                moves[1][guardInCol] = 0;
            }

            moves[2][0] = 0;
            return moves;
        } else {
            moves[0][0] = 1;
            guardInCol = 0;

            int i;
            for(i = 1; i < 9; ++i) {
                if (this.guards[i].getCol() - prisoner.getCol() == 0) {
                    guardInCol = i;
                }

                moves[0][i] = Math.abs(this.guards[i].getCol() - prisoner.getCol()) > Math.abs(i - prisoner.getRow()) ? 1 : 0;
            }

            if (guardInCol > 0) {
                if (this.guards[guardInCol].getRow() - prisoner.getRow() > 0) {
                    for(i = this.guards[guardInCol].getRow(); i < 9; ++i) {
                        moves[0][i] = 0;
                    }
                } else {
                    for(i = 0; i <= this.guards[guardInCol].getRow(); ++i) {
                        moves[0][i] = 0;
                    }
                }
            }

            if (prisoner.getRow() >= 0) {
                moves[0][prisoner.getRow()] = 0;
            }

            if (prisoner.getRow() > 0) {
                i = this.guards[prisoner.getRow()].getCol();
                int stopCol = i + (prisoner.getCol() - i) / 2;
                // TODO: Check int i
                //int i;
                if (i < prisoner.getCol()) {
                    for(i = 0; i <= stopCol; ++i) {
                        moves[1][i] = 0;
                    }

                    for(i = stopCol + 1; i < 9; ++i) {
                        moves[1][i] = 1;
                    }
                } else {
                    for(i = 0; i < stopCol; ++i) {
                        moves[1][i] = 1;
                    }

                    for(i = stopCol; i < 9; ++i) {
                        moves[1][i] = 0;
                    }
                }

                moves[1][prisoner.getCol()] = 0;
            } else if (prisoner.getRow() == 0) {
                for(i = 0; i < 9; ++i) {
                    moves[1][i] = 1;
                }
            } else {
                for(i = 0; i < 9; ++i) {
                    moves[1][i] = 0;
                }
            }

            for(i = 0; i < 4 * this.getNoPlayers(); ++i) {
                if (this.prisoners[i].getRow() >= 0 && this.prisoners[i].getRow() < 9) {
                    if (this.prisoners[i].getCol() == prisoner.getCol()) {
                        moves[0][this.prisoners[i].getRow()] = 0;
                    }

                    if (this.prisoners[i].getRow() == prisoner.getRow()) {
                        moves[1][this.prisoners[i].getCol()] = 0;
                    }
                }
            }

            moves[2][0] = 0;
            if (prisoner.getRow() >= 6 && prisoner.getCol() > 9 - prisoner.getRow() && prisoner.getCol() < 9 - (9 - prisoner.getRow() + 1)) {
                moves[2][0] = 1;

                for(i = prisoner.getRow() + 1; i < 9; ++i) {
                    if (this.guards[i].getCol() == prisoner.getCol()) {
                        moves[2][0] = 0;
                        break;
                    }
                }
            }

            return moves;
        }
    }

    public boolean hasValidMove(int[][] moves) {
        int i;
        for(i = 0; i < 9; ++i) {
            if (moves[0][i] > 0) {
                return true;
            }
        }

        for(i = 0; i < 9; ++i) {
            if (moves[1][i] > 0) {
                return true;
            }
        }

        if (moves[2][0] > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void doAIMove(AIListener listener) {
        AI ai = new AI(this, listener);
        ai.start();
    }

    public void doMove(Player player, Prisoner prisoner, int rowOrCol, int row, int col, boolean notifyMoveListeners) {
        if (prisoner.getRow() == -1) {
            prisoner.setCol(col);
        }

        this.doMove(rowOrCol, rowOrCol == 0 ? row : col, prisoner);
        Iterator var7;
        MoveListener ml;
        if (notifyMoveListeners) {
            var7 = this.listeners.iterator();

            while(var7.hasNext()) {
                ml = (MoveListener)var7.next();
                ml.moveDone(player, prisoner, rowOrCol, row, col);
            }
        }

        if (this.hasWon(player)) {
            var7 = this.listeners.iterator();

            while(var7.hasNext()) {
                ml = (MoveListener)var7.next();
                ml.gameWon(player);
            }
        }

    }

    public void doMove(int rowOrCol, int pos, Prisoner prisoner) {
        int diff = 0;
        Move move = new Move();
        move.addPrisoner(prisoner);
        int guardRow = 0;
        switch(rowOrCol) {
            case 1:
                diff = pos - prisoner.getCol();
                prisoner.setCol(pos);
                guardRow = prisoner.getRow();
                break;
            case 2:
                pos = 9;
            case 0:
                diff = pos - prisoner.getRow();
                prisoner.setRow(pos);
                if (pos == 9) {
                    prisoner.setCol(prisoner.getId());
                }

                guardRow = pos;
        }

        if (guardRow > 0 && guardRow < 9) {
            move.saveGuard(this.guards[guardRow]);
            int i;
            if (prisoner.getCol() > this.guards[guardRow].getCol()) {
                for(i = 0; i < 4 * this.getNoPlayers(); ++i) {
                    if (this.prisoners[i].getRow() == guardRow && this.prisoners[i].getCol() > this.guards[guardRow].getCol() && this.prisoners[i].getCol() <= this.guards[guardRow].getCol() + Math.abs(diff)) {
                        move.addPrisoner(this.prisoners[i]);
                        this.prisoners[i].setCol(i % 4);
                        this.prisoners[i].setRow(-1);
                    }
                }

                this.guards[guardRow].setCol(this.guards[guardRow].getCol() + Math.abs(diff));
            } else {
                for(i = 0; i < 4 * this.getNoPlayers(); ++i) {
                    if (this.prisoners[i].getRow() == guardRow && this.prisoners[i].getCol() < this.guards[guardRow].getCol() && this.prisoners[i].getCol() >= this.guards[guardRow].getCol() - Math.abs(diff)) {
                        move.addPrisoner(this.prisoners[i]);
                        this.prisoners[i].setCol(i % 4);
                        this.prisoners[i].setRow(-1);
                    }
                }

                this.guards[guardRow].setCol(this.guards[guardRow].getCol() - Math.abs(diff));
            }
        }

        this.moveHistory.add(move);
        if (!this.hasWon(this.getCurrPlayer())) {
            this.setCurrPlayer(this.getPlayers()[(this.getCurrPlayer().getId() + 1) % this.getNoPlayers()]);
        }

    }

    public void undoMove(boolean notifyMoveListeners) {
        Move move = (Move)this.moveHistory.get(this.moveHistory.size() - 1);
        this.moveHistory.remove(this.moveHistory.size() - 1);
        move.restoreGuard(this.guards);
        move.restorePrisoners(this.prisoners);
    }

    public Guard[] getGuards() {
        return this.guards;
    }

    public Prisoner[] getPrisoners() {
        return this.prisoners;
    }

    public Player getCurrPlayer() {
        return this.currPlayer;
    }

    protected void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Player getMyPlayer() {
        return this.myPlayer;
    }

    public int getNoPlayers() {
        return this.noPlayers;
    }

    private void setNoPlayers(int noPlayers) {
        this.noPlayers = noPlayers;
    }

    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public void addMoveListener(MoveListener listener) {
        this.listeners.add(listener);
    }

    public void removeMoveListener(MoveListener listener) {
        this.listeners.remove(listener);
    }

    public boolean isInitialized() {
        return this.initialized;
    }
}
