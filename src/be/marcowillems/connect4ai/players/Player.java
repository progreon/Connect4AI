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
    private int losses = 0;
    private int wins = 0;

    public void addDraw() {
        draws++;
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

    public final int getDraws() {
        return draws;
    }

    public final int getLosses() {
        return losses;
    }

    protected final int getPlaycount() {
        return draws + losses + wins;
    }

    public final double getWinRate() {
        return wins * 1.0 / getPlaycount();
    }

    public final int getWins() {
        return wins;
    }

    public final double getScore() {
        return getScore(0.5);
    }

    public final double getScore(double drawWeight) {
        return ((wins * 1.0 + (draws * drawWeight)) / getPlaycount());
    }

    public final double getScore(double drawWeight, double movesWeight) {
        return getScore(drawWeight) - ((getAvgMovecount() * movesWeight) / (Settings.BOARD_WIDTH * Settings.BOARD_HEIGHT));
    }

}
