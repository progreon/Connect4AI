/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.players;

import be.marcowillems.connect4ai.Settings;
import be.marcowillems.connect4ai.game.Board;

/**
 *
 * @author Marco Willems
 */
public abstract class Player {

    private int moves = 0;
    private int draws = 0;
    private int errors = 0;
    private int losses = 0;
    private int wins = 0;

    public void addDraw() {
        draws++;
    }

    public void addError() {
        errors++;
    }

    public void addLoss() {
        losses++;
    }

    public void addWin() {
        wins++;
    }

    public final boolean doMove(Board b, boolean isP1) {
        moves++;
        return _doMove(b, isP1);
    }

    protected abstract boolean _doMove(Board b, boolean isP1);

    public final double getAvgMovecount() {
        return moves * 1.0 / getPlaycount();
    }

    public final double getDrawRate() {
        return draws * 1.0 / getPlaycount();
    }

    public final int getDraws() {
        return draws;
    }

    public final double getErrorRate() {
        return errors * 1.0 / getPlaycount();
    }

    public final int getErrors() {
        return errors;
    }

    public final double getLossRate() {
        return losses * 1.0 / getPlaycount();
    }

    public final int getLosses() {
        return losses;
    }

    protected final int getPlaycount() {
        return draws + errors + losses + wins;
    }

    /**
     * Fitness function
     *
     * @return
     */
    public final double getScore() {
        return ((wins * 1.0 + (draws * Settings.DRAW_WEIGHT)) / getPlaycount()) + (getAvgMovecount() * Settings.MOVES_WEIGHT) + (getErrorRate() * Settings.ERROR_WEIGHT);
    }

    public final double getWinRate() {
        return wins * 1.0 / getPlaycount();
    }

    public final int getWins() {
        return wins;
    }

}
