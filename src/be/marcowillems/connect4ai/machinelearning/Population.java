/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.machinelearning;

import be.marcowillems.connect4ai.Settings;
import be.marcowillems.connect4ai.game.Game;
import be.marcowillems.connect4ai.nn.FeedForwardNN;
import be.marcowillems.connect4ai.players.FFNNAIPlayer;
import be.marcowillems.connect4ai.players.Player;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Marco Willems
 */
public class Population {

    final FFNNAIPlayer[] population;

    public Population(int size) {
        this(size, null);
    }

    public Population(int size, FFNNAIPlayer[] ancestors) {
        population = new FFNNAIPlayer[size];
        for (int i = 0; i < size; i++) {
            if (ancestors == null) {
                population[i] = new FFNNAIPlayer(new FeedForwardNN(Settings.ffnnShape, Settings.functionType));
            } else {
                Set<Integer> parentIDs = new TreeSet<>();
                parentIDs.add(Settings.rg.nextInt(ancestors.length));
                while (ancestors.length >= Settings.PARENTS_PER_CHILD && parentIDs.size() < Settings.PARENTS_PER_CHILD) {
                    parentIDs.add(Settings.rg.nextInt(ancestors.length));
                }
                FeedForwardNN[] parents = new FeedForwardNN[Settings.PARENTS_PER_CHILD];
                int p = 0;
                for (int pid : parentIDs) {
                    parents[p] = ancestors[pid].ffnn;
                    p++;
                }
                population[i] = new FFNNAIPlayer(new FeedForwardNN(parents, Settings.MUTATION_RATIO));
            }
        }
    }
    
    public double getAverageMoves() {
        double sum = 0.0;

        for (FFNNAIPlayer player : population) {
            sum += player.getAvgMovecount();
        }

        return sum / population.length;
    }

    public double getAverageScore() {
        double sum = 0.0;

        for (FFNNAIPlayer player : population) {
            sum += player.getScore();
        }

        return sum / population.length;
    }

    public double getDrawRate() {
        double sum = 0.0;

        for (FFNNAIPlayer player : population) {
            sum += player.getDrawRate();
        }

        return sum / population.length;
    }

    public double getErrorRate() {
        double sum = 0.0;

        for (FFNNAIPlayer player : population) {
            sum += player.getErrorRate();
        }

        return sum / population.length;
    }

    public double getLossRate() {
        double sum = 0.0;

        for (FFNNAIPlayer player : population) {
            sum += player.getLossRate();
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

    public void play(Player[] opponents) {
        for (FFNNAIPlayer player : population) {
            for (int i = 0; i < opponents.length; i++) {
                Game g = new Game(opponents[i], player);
                g.play(i % 2 == 0);
//                g.play(false);
            }
        }
    }

    public FFNNAIPlayer[] selection(int size) {
        int bestCount = Math.max(2, (int) (size * (1 - Settings.BAD_RATIO)));
        int badCount = Math.max(0, size - bestCount);

        PriorityQueue<FFNNAIPlayer> sortedPlayers = new PriorityQueue<>();
        sortedPlayers.addAll(Arrays.asList(population));
        ArrayDeque<FFNNAIPlayer> sortedPlayers2 = new ArrayDeque<>(sortedPlayers);

        FFNNAIPlayer[] sel = new FFNNAIPlayer[bestCount + badCount];
        for (int i = 0; i < bestCount; i++) {
            sel[i] = sortedPlayers2.removeFirst();
        }
        for (int i = bestCount; i < badCount + bestCount; i++) {
            sel[i] = sortedPlayers2.removeLast();
        }

        return sel;
    }

}
