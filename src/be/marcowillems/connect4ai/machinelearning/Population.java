/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.machinelearning;

import be.marcowillems.connect4ai.Settings;
import be.marcowillems.connect4ai.game.Game;
import be.marcowillems.connect4ai.nn.FeedForwardNN;
import be.marcowillems.connect4ai.nn.TransferFunctions;
import be.marcowillems.connect4ai.players.FFNNAIPlayer;
import be.marcowillems.connect4ai.players.Player;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Marco Willems
 */
public class Population {

    public static final int[] ffnnShape
            = new int[]{Settings.BOARD_HEIGHT * Settings.BOARD_WIDTH,
                (Settings.BOARD_HEIGHT / 3) * Settings.BOARD_WIDTH,
//                Settings.BOARD_WIDTH,
                Settings.BOARD_WIDTH};
//                1};
    public static final TransferFunctions.Type functionType = TransferFunctions.DEFAULT;
    public static final int PARENTS_PER_CHILD = 2;
    public static final double MUTATION_RATE = 0.1;

    private final Random rg = new Random(System.currentTimeMillis());
    private final FFNNAIPlayer[] population;

    public Population(int size) {
        this(size, null);
    }

    public Population(int size, FFNNAIPlayer[] ancestors) {
        population = new FFNNAIPlayer[size];
        for (int i = 0; i < size; i++) {
            if (ancestors == null) {
                population[i] = new FFNNAIPlayer(new FeedForwardNN(ffnnShape, functionType));
            } else {
                Set<Integer> parentIDs = new TreeSet<>();
                parentIDs.add(rg.nextInt(ancestors.length));
                while (ancestors.length >= PARENTS_PER_CHILD && parentIDs.size() < PARENTS_PER_CHILD) {
                    parentIDs.add(rg.nextInt(ancestors.length));
                }
                FeedForwardNN[] parents = new FeedForwardNN[PARENTS_PER_CHILD];
                int p = 0;
                for (int pid : parentIDs) {
                    parents[p] = ancestors[pid].ffnn;
                    p++;
                }
                population[i] = new FFNNAIPlayer(new FeedForwardNN(parents, MUTATION_RATE));
            }
        }
    }

    public double getAverageScore() {
        double sum = 0.0;

        for (FFNNAIPlayer player : population) {
            sum += player.getScore(FFNNAIPlayer.DRAW_WEIGHT, FFNNAIPlayer.MOVES_WEIGHT);
        }

        return sum / population.length;
    }

    public int getSize() {
        return population.length;
    }

    public double getWinRate() {
        double sum = 0.0;

        for (FFNNAIPlayer player : population) {
            sum += player.getWinRate();
        }

        return sum / population.length;
    }
    
    public void play(int count, Player opponent) {
        for (FFNNAIPlayer player : population) {
            for (int i = 0; i < count; i++) {
                Game g = new Game(opponent, player);
                g.play();
            }
        }
    }

    public FFNNAIPlayer[] selection(int size) {
        return selection(size, 0.0);
    }

    public FFNNAIPlayer[] selection(int size, double badRatio) {
        int bestCount = Math.max(2, (int) (size * (1 - badRatio)));
        int badCount = size - bestCount;

        PriorityQueue<FFNNAIPlayer> sortedPlayers = new PriorityQueue<>();
        sortedPlayers.addAll(Arrays.asList(population));
        ArrayDeque<FFNNAIPlayer> sortedPlayers2 = new ArrayDeque<>(sortedPlayers);

        FFNNAIPlayer[] sel = new FFNNAIPlayer[size];
        for (int i = 0; i < bestCount; i++) {
            sel[i] = sortedPlayers2.removeLast();
        }
        for (int i = 0; i < badCount; i++) {
            sel[i] = sortedPlayers2.removeFirst();
        }

        return sel;
    }

}
