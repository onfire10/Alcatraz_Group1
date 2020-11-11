//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api;

import impl.GUIFrame;
import impl.GUIPanel;
import impl.Game;

import javax.swing.*;

public class Alcatraz {
    public static final int ROW = 0;
    public static final int COL = 1;
    public static final int BOAT = 2;
    private GUIFrame frame;
    private GUIPanel boardPanel;
    private Game game = new Game();

    public Alcatraz() {
        this.frame = new GUIFrame(this.game);
        this.boardPanel = this.frame.getGamePanel();
    }

    private void refreshBoardPanel() {
        this.boardPanel.validate();
        this.boardPanel.repaint();
    }

    public void init(int noPlayers, int myPlayerId) {
        this.game.initialize(noPlayers, myPlayerId);
        this.refreshBoardPanel();
    }

    public void start() {
        if (this.game.getCurrPlayer().equals(this.game.getMyPlayer())) {
            this.boardPanel.setState(0);
        } else {
            this.boardPanel.setState(2);
        }

    }

    public void showWindow() {
        this.frame.setVisible(true);
    }

    public void closeWindow() {
        this.frame.setVisible(false);
    }

    public JFrame getWindow() {
        return this.frame;
    }

    public JPanel getGameBoard() {
        return this.boardPanel;
    }

    public void disposeWindow() {
        this.frame.dispose();
        this.frame = null;
    }

    public void addMoveListener(MoveListener listener) {
        this.game.addMoveListener(listener);
    }

    public void removeMoveListener(MoveListener listener) {
        this.game.removeMoveListener(listener);
    }

    public void doMove(Player player, Prisoner prisoner, int rowOrCol, int row, int col) throws IllegalMoveException {
        player = this.getPlayer(player.getId());
        prisoner = this.getPrisoner(prisoner.getId());
        if (!player.equals(this.game.getCurrPlayer())) {
            throw new IllegalMoveException("Move tried by wrong player: " + player);
        } else {
            this.game.doMove(player, prisoner, rowOrCol, row, col, false);
            if (this.game.hasWon(player)) {
                this.boardPanel.setState(4);
            } else if (this.game.getCurrPlayer().equals(this.game.getMyPlayer())) {
                this.boardPanel.setState(0);
            } else {
                this.boardPanel.setState(2);
            }

            this.refreshBoardPanel();
        }
    }

    public Player getPlayer(int playerId) {
        return this.game.getPlayers()[playerId];
    }

    public Prisoner getPrisoner(int prisonerId) {
        return this.game.getPrisoners()[prisonerId];
    }
}
