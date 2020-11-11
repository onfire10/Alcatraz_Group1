//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package impl;

import api.Player;
import api.Prisoner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class GUIPanel extends JPanel implements MouseListener, KeyListener, AIListener {
    private static final long serialVersionUID = 8211415836117264254L;
    public static final int MY_TURN = 0;
    public static final int MY_TURN_SELECTED = 1;
    public static final int OTHERS_TURN = 2;
    public static final int IDLE = 3;
    public static final int WON = 4;
    private Game game;
    private Image[] defFieldImage;
    private Image[] defFieldHighlightImage;
    private Image[] defRedFieldImage;
    private Image guardImage;
    private Image[] prisonerImage;
    private Image prisonerSelectImage;
    private Image startImage;
    private Image boatImage;
    private Image boatHighlightImage;
    int[] won;
    boolean running;
    boolean calcing;
    private int state;
    private boolean highlight = true;
    private Prisoner selPrisoner;

    public GUIPanel() {
        this.initialize();
    }

    public GUIPanel(Game game) {
        this.game = game;
        this.initialize();
    }

    private void initialize() {
        this.setLayout((LayoutManager)null);
        this.setSize(540, 660);
        this.setBackground(Color.BLACK);
        this.addMouseListener(this);
        this.addKeyListener(this);

        try {
            this.defFieldImage = new Image[2];
            this.defFieldImage[0] = ImageIO.read(this.getClass().getResource("../field1.png"));
            this.defFieldImage[1] = ImageIO.read(this.getClass().getResource("../field2.png"));
            this.defRedFieldImage = new Image[2];
            this.defRedFieldImage[0] = ImageIO.read(this.getClass().getResource("../field_red1.png"));
            this.defRedFieldImage[1] = ImageIO.read(this.getClass().getResource("../field_red2.png"));
            this.defFieldHighlightImage = new Image[2];
            this.defFieldHighlightImage[0] = ImageIO.read(this.getClass().getResource("../field_highlight1.png"));
            this.defFieldHighlightImage[1] = ImageIO.read(this.getClass().getResource("../field_highlight2.png"));
            this.guardImage = ImageIO.read(this.getClass().getResource("../guard.png"));
            this.prisonerImage = new Image[4];
            this.prisonerImage[0] = ImageIO.read(this.getClass().getResource("../prisoner1.png"));
            this.prisonerImage[1] = ImageIO.read(this.getClass().getResource("../prisoner2.png"));
            this.prisonerImage[2] = ImageIO.read(this.getClass().getResource("../prisoner3.png"));
            this.prisonerImage[3] = ImageIO.read(this.getClass().getResource("../prisoner4.png"));
            this.prisonerSelectImage = ImageIO.read(this.getClass().getResource("../prisoner_select.png"));
            this.startImage = ImageIO.read(this.getClass().getResource("../start.png"));
            this.boatImage = ImageIO.read(this.getClass().getResource("../boat.png"));
            this.boatHighlightImage = ImageIO.read(this.getClass().getResource("../boat_highlight.png"));
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        this.initState();
    }

    private void initState() {
        this.won = null;
        this.running = false;
        this.calcing = false;
        this.state = 0;
    }

    public void paint(Graphics g) {
        g.drawImage(this.boatImage, 0, 0, 540, 60, this);

        int i;
        int j;
        for(i = 0; i < 9; ++i) {
            for(j = 0; j < 9; ++j) {
                this.drawImage(g, this.defFieldImage[(i + j % 2) % 2], i, j);
            }
        }

        for(i = 0; i < 3; ++i) {
            for(j = -i; j <= i; ++j) {
                this.drawImage(g, this.defRedFieldImage[(Math.abs(j) + i % 2) % 2], 6 + i, 4 + j);
            }
        }

        g.drawImage(this.startImage, 0, 600, 540, 60, this);
        if (this.game.isInitialized()) {
            Guard[] guards = this.game.getGuards();

            for(i = 1; i < 9; ++i) {
                this.drawImage(g, this.guardImage, guards[i].getRow(), guards[i].getCol());
            }

            Prisoner[] prisoners = this.game.getPrisoners();

            int oldCol;
            for(oldCol = 0; oldCol < 4 * this.game.getNoPlayers(); ++oldCol) {
                this.drawImage(g, this.prisonerImage[oldCol / 4], prisoners[oldCol].getRow(), prisoners[oldCol].getCol(), prisoners[oldCol]);
                if (prisoners[oldCol] == this.selPrisoner) {
                    this.drawImage(g, this.prisonerSelectImage, prisoners[oldCol].getRow(), prisoners[oldCol].getCol());
                }
            }

            if (this.highlight && this.state == 1) {
                if (this.selPrisoner.getRow() != -1) {
                    this.drawHighlightFields(g);
                } else {
                    oldCol = this.selPrisoner.getCol();

                    for(i = 0; i < 9; ++i) {
                        this.selPrisoner.setCol(i);
                        this.drawHighlightFields(g);
                    }

                    this.selPrisoner.setCol(oldCol);
                }
            }

            if (this.state == 2) {
                this.drawMessage(g, "Please wait for player " + (this.game.getCurrPlayer().getName() != null ? this.game.getCurrPlayer().getName() : Integer.toString(this.game.getCurrPlayer().getId())) + " to move.");
            } else if (this.state == 4) {
                this.drawMessage(g, "Player " + (this.game.getCurrPlayer().getName() != null ? this.game.getCurrPlayer().getName() : Integer.toString(this.game.getCurrPlayer().getId())) + " wins the game!!!");
            }
        }

    }

    protected void drawHighlightFields(Graphics g) {
        int[][] moves = this.game.getValidMoves(this.selPrisoner);

        int i;
        for(i = 0; i < 9; ++i) {
            if (moves[1][i] > 0) {
                this.drawImage(g, this.defFieldHighlightImage[(i + this.selPrisoner.getRow() % 2) % 2], this.selPrisoner.getRow(), i);
            }
        }

        for(i = 0; i < 9; ++i) {
            if (moves[0][i] > 0) {
                this.drawImage(g, this.defFieldHighlightImage[(this.selPrisoner.getCol() + i % 2) % 2], i, this.selPrisoner.getCol());
            }
        }

        if (moves[2][0] > 0) {
            g.drawImage(this.boatHighlightImage, 0, 0, 540, 60, this);
        }

    }

    protected void drawImage(Graphics g, Image image, int row, int col) {
        this.drawImage(g, image, row, col, (Prisoner)null);
    }

    protected void drawImage(Graphics g, Image image, int row, int col, Prisoner prisoner) {
        if (prisoner != null && (row == -1 || row == 9)) {
            col = prisoner.getId() % 4;
            if (this.game.isOwnPrisoner(this.game.getMyPlayer(), prisoner)) {
                g.drawImage(image, col * 60, (9 - row) * 60, 60, 60, this);
            } else {
                g.drawImage(image, (col + 4) * 60 + prisoner.getId() / 4 * 10, (9 - row) * 60, 60, 60, this);
            }
        } else {
            g.drawImage(image, col * 60, (9 - row) * 60, 60, 60, this);
        }

    }

    protected void drawMessage(Graphics g, String msg) {
        g.setColor(new Color(255, 224, 145));
        g.fill3DRect(30, 315, 480, 30, true);
        g.setColor(Color.BLACK);
        Font fold = g.getFont();
        Font f = new Font(fold.getFontName(), 1, fold.getSize());
        g.setFont(f);
        Rectangle2D rect = f.getStringBounds(msg, ((Graphics2D)g).getFontRenderContext());
        g.drawString(msg, 30 + (480 - (int)rect.getWidth()) / 2, 327 + g.getFontMetrics().getAscent() / 2);
        g.setFont(fold);
    }

    protected void processMove(int row, int col) {
        boolean moveDone = false;
        if (row >= 0 && row <= 9 && col >= 0 && col <= 9) {
            Player currentPlayer = this.game.getCurrPlayer();
            if (this.selPrisoner.getRow() == -1) {
                int oldCol = this.selPrisoner.getCol();
                this.selPrisoner.setCol(col);
                int[][] moves = this.game.getValidMoves(this.selPrisoner);
                if (moves[0][row] > 0) {
                    this.game.doMove(currentPlayer, this.selPrisoner, 0, row, this.selPrisoner.getCol(), true);
                    moveDone = true;
                } else {
                    this.selPrisoner.setCol(oldCol);
                }
            } else {
                int[][] moves;
                if (row == 9) {
                    moves = this.game.getValidMoves(this.selPrisoner);
                    if (moves[2][0] > 0) {
                        this.game.doMove(currentPlayer, this.selPrisoner, 0, row, this.selPrisoner.getCol(), true);
                        moveDone = true;
                    }
                } else if (row == this.selPrisoner.getRow()) {
                    moves = this.game.getValidMoves(this.selPrisoner);
                    if (moves[1][col] > 0) {
                        this.game.doMove(currentPlayer, this.selPrisoner, 1, this.selPrisoner.getRow(), col, true);
                        moveDone = true;
                    }
                } else if (col == this.selPrisoner.getCol()) {
                    moves = this.game.getValidMoves(this.selPrisoner);
                    if (moves[0][row] > 0) {
                        this.game.doMove(currentPlayer, this.selPrisoner, 0, row, this.selPrisoner.getCol(), true);
                        moveDone = true;
                    }
                }
            }

            if (moveDone) {
                this.selPrisoner = null;
                if (this.game.hasWon(currentPlayer)) {
                    this.state = 4;
                } else {
                    this.state = 2;
                }

                this.repaint();
            }

        }
    }

    public void calcFinished(AI ai) {
        int[] bestMove = ai.getBestMove();
        if (bestMove != null) {
            int prisonerId = this.game.getCurrPlayer().getId() * 4 + bestMove[2];
            if (this.game.getPrisoners()[prisonerId].getRow() == -1) {
                this.game.getPrisoners()[prisonerId].setCol(bestMove[3]);
            }

            this.game.doMove(bestMove[0], bestMove[1], this.game.getPrisoners()[prisonerId]);
        } else {
            System.out.println("Cannot move!!!");
        }

        this.repaint();
        this.state = 0;
    }

    public void mouseClicked(MouseEvent me) {
        int col = me.getX() / 60;
        int row = 9 - me.getY() / 60;
        Prisoner prisoner = this.game.getPrisoner(row, col);
        switch(this.state) {
            case 0:
                if (prisoner != null && this.game.isOwnPrisoner(this.game.getCurrPlayer(), prisoner)) {
                    this.selPrisoner = prisoner;
                    this.state = 1;
                    this.repaint();
                }
                break;
            case 1:
                if (prisoner != null) {
                    if (prisoner != this.selPrisoner && this.game.isOwnPrisoner(this.game.getCurrPlayer(), prisoner)) {
                        this.selPrisoner = prisoner;
                        this.repaint();
                    } else {
                        this.selPrisoner = null;
                        this.state = 0;
                        this.repaint();
                    }
                } else {
                    this.processMove(row, col);
                }
            case 2:
            case 3:
            case 4:
        }

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'h') {
            this.highlight = !this.highlight;
            if (this.state == 1) {
                this.repaint();
            }
        }

    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
