//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package impl;

import api.Player;

import java.util.ArrayList;
import java.util.List;

public class AI extends Thread {
    private int[] bestMove;
    private int nummoves;
    private Player[] players;
    private Player me;
    private Player other;
    private int maxdepth;
    private Game game;
    private AIListener listener;

    public AI(Game game, AIListener listener) {
        super("AIThread");
        this.game = new Game(game, false);
        this.listener = listener;
        this.players = game.getPlayers();
        this.setPlayer(game.getCurrPlayer());
        this.maxdepth = 5;
    }

    public void setPlayer(Player player) {
        this.me = player;
        if (this.me.getId() == 0) {
            this.other = this.players[1];
        } else {
            this.other = this.players[0];
        }

    }

    public int[] getBestMove() {
        return this.bestMove;
    }

    public void run() {
        this.calc();
        this.listener.calcFinished(this);
        this.listener = null;
    }

    protected void calc() {
        this.bestMove = null;
        this.nummoves = 0;
        int result = this.alphaBetaSearch(this.game, this.me, this.me, this.other, this.maxdepth, 0, -100000000, 100000000);
        System.out.println("Anzahl Zï¿½ge: " + this.nummoves + " / Bewertung: " + result);
    }

    protected int alphaBetaSearch(Game sf_ai, Player current, Player me, Player other, int maxdepth, int depth, int alpha, int beta) {
        ++this.nummoves;
        Player enemy = current == me ? other : me;
        if (depth >= maxdepth) {
            return -sf_ai.getScore(me);
        } else {
            int num_pris = 4;
            List validMoves = new ArrayList(num_pris);
            boolean hasValidMoves = false;

            int k;
            int prisonerId;
            int oldCol;
            for(k = current.getId() * num_pris; k < (current.getId() + 1) * num_pris; ++k) {
                List subMoves = new ArrayList();
                if (sf_ai.getPrisoners()[k].getRow() != -1) {
                    int[][] moves = sf_ai.getValidMoves(sf_ai.getPrisoners()[k]);
                    subMoves.add(moves);
                    if (sf_ai.hasValidMove(moves)) {
                        hasValidMoves = true;
                    }
                } else {
                    prisonerId = sf_ai.getPrisoners()[k].getCol();

                    for(oldCol = 0; oldCol < 9; ++oldCol) {
                        sf_ai.getPrisoners()[k].setCol(oldCol);
                        int[][] moves = sf_ai.getValidMoves(sf_ai.getPrisoners()[k]);
                        subMoves.add(moves);
                    }

                    hasValidMoves = true;
                    sf_ai.getPrisoners()[k].setCol(prisonerId);
                }

                validMoves.add(subMoves);
            }

            if (!hasValidMoves) {
                if (depth == 0) {
                    this.bestMove = null;
                }

                return 0;
            } else {
                for(k = 0; k < validMoves.size(); ++k) {
                    List subMoves = (List)validMoves.get(k);
                    prisonerId = current.getId() * num_pris + k;
                    oldCol = sf_ai.getPrisoners()[prisonerId].getCol();

                    for(int l = 0; l < subMoves.size(); ++l) {
                        if (subMoves.size() > 1) {
                            sf_ai.getPrisoners()[prisonerId].setCol(l);
                        }

                        int[][] prisonerMoves = (int[][])((int[][])subMoves.get(l));

                        for(int i = 2; i >= 0; --i) {
                            for(int j = 0; j < 9; ++j) {
                                if (prisonerMoves[i][j] != 0) {
                                    sf_ai.doMove(i, j, sf_ai.getPrisoners()[prisonerId]);
                                    if (sf_ai.hasWon(current)) {
                                        sf_ai.undoMove(false);
                                        if (depth == 0) {
                                            this.bestMove = new int[]{i, j, k, l};
                                        }

                                        return current == me ? 100000 : -100000;
                                    }

                                    int res = -this.alphaBetaSearch(sf_ai, enemy, me, other, maxdepth, depth + 1, -beta, -alpha);
                                    sf_ai.undoMove(false);
                                    if (i == 2 && j == 0 && depth == 0 && prisonerMoves[i][j] > 0) {
                                        System.out.println("" + current.getId() + "/" + me.getId() + " ins boot! after rec depth " + depth + ": " + res + " " + alpha + "/" + beta);
                                    }

                                    if (res >= beta) {
                                        return beta;
                                    }

                                    if (res > alpha) {
                                        alpha = res;
                                        if (depth == 0) {
                                            this.bestMove = new int[]{i, j, k, l};
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (subMoves.size() > 1) {
                        sf_ai.getPrisoners()[prisonerId].setCol(oldCol);
                    }
                }

                return alpha;
            }
        }
    }
}
