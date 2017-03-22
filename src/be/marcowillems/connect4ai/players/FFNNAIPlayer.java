/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.players;

import be.marcowillems.connect4ai.game.Board;
import be.marcowillems.connect4ai.nn.FeedForwardNN;
import be.marcowillems.connect4ai.util.Matrix;

/**
 *
 * @author Marco Willems
 */
public class FFNNAIPlayer extends Player implements Comparable<FFNNAIPlayer> {

    public static final double DRAW_WEIGHT = 0.5;
    public static final double MOVES_WEIGHT = 0.0;

    public final FeedForwardNN ffnn;

    public FFNNAIPlayer(FeedForwardNN bpn) {
        this.ffnn = bpn;
    }

    @Override
    public boolean _doMove(Board b, boolean isP1) {
        Matrix output = ffnn.run(new Matrix(b.toArray()));
        int move = 0;
        for (int i = 1; i < output.w; i++) {
            if (output.get(0, move) < output.get(0, i)) {
                move = i;
            }
        }
//        double output = ffnn.run(new Matrix(b.toArray())).get(0, 0) * (b.getWidth() - 1);
//        System.out.println("RNN output: " + output);
//        int move = (int) output;
        return b.put(move, isP1);
    }

    @Override
    public int compareTo(FFNNAIPlayer o) {
        return (int) (getPlaycount() * (getScore(DRAW_WEIGHT, DRAW_WEIGHT) - o.getScore(DRAW_WEIGHT, DRAW_WEIGHT)));
    }

}
