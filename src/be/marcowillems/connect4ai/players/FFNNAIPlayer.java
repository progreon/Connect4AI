/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.players;

import be.marcowillems.connect4ai.game.Board;
import be.marcowillems.connect4ai.nn.FeedForwardNN;
import be.marcowillems.connect4ai.util.Matrix;
import java.util.PriorityQueue;

/**
 *
 * @author Marco Willems
 */
public class FFNNAIPlayer extends Player implements Comparable<FFNNAIPlayer> {

    public final FeedForwardNN ffnn;

    public FFNNAIPlayer(FeedForwardNN ffnn) {
        this.ffnn = ffnn;
    }

    @Override
    public boolean _doMove(Board b, boolean isP1) {
        Matrix output = ffnn.run(new Matrix(b.toArray()));
        PriorityQueue<NextMove> queue = new PriorityQueue<>();
        for (int i = 0; i < output.w; i++) {
            queue.add(new NextMove(i, 1 / output.get(0, i)));
        }
        boolean result = b.put(queue.remove().action, isP1);
        while (!result) {
            result = b.put(queue.remove().action, isP1);
        }
        return result;
//        double output = ffnn.run(new Matrix(b.toArray())).get(0, 0) * (b.getWidth() - 1);
//        System.out.println("RNN output: " + output);
//        int move = (int) output;
//        return b.put(move, isP1);
    }

    @Override
    public int compareTo(FFNNAIPlayer o) {
        return (int) (100000 * (o.getScore() - getScore()));
    }

    private class NextMove implements Comparable<NextMove> {

        final double priority;
        final int action;

        public NextMove(int action, double priority) {
            this.action = action;
            this.priority = priority;
        }

        public boolean apply(Board b, boolean isP1) {
            return b.put(action, isP1);
        }

        @Override
        public int compareTo(NextMove o) {
            return (int) (1000 * (priority - o.priority));
        }

        @Override
        public String toString() {
            return "{r:" + action + "|" + priority + "}";
        }
    }

}
