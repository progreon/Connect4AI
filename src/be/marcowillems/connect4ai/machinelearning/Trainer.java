/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.machinelearning;

import be.marcowillems.connect4ai.Settings;
import be.marcowillems.connect4ai.game.Game;
import be.marcowillems.connect4ai.players.FFNNAIPlayer;
import be.marcowillems.connect4ai.players.Player;
import be.marcowillems.connect4ai.players.RandomPlayer;
import be.marcowillems.connect4ai.players.SimpleAIPlayer;

/**
 *
 * @author Marco Willems
 */
public class Trainer {

    private Population bestPopulation;
    public int bestIt;
//    private final Population[] generations;
    private FFNNAIPlayer bestSoFar;
    public int bestFromIt;
//
//    public Trainer() {
//        this.generations = new Population[Settings.GENERATION_COUNT];
//    }

    public void train(Player[] initialOpponents) {
        System.out.println("Training...");
        Population currentPop = new Population(Settings.POPULATION_SIZE);
//        generations[0] = new Population(Settings.POPULATION_SIZE);
////        generations[0].play(Settings.GAME_COUNT, initialOpponent);
//        generations[0].play(initialOpponents);
        currentPop.play(initialOpponents);
        bestPopulation = currentPop;
        bestIt = 0;
        bestSoFar = currentPop.selection(1)[0];
        bestFromIt = 0;
        System.out.printf("%0,2d: %s (best: s=%f, w=%.2f, m=%.2f)\n", 0, getStats(currentPop), bestSoFar.getScore(), bestSoFar.getWinRate(), bestSoFar.getAvgMovecount());
        for (int i = 1; i < Settings.GENERATION_COUNT; i++) {
            currentPop = new Population(Settings.POPULATION_SIZE, currentPop.selection(Settings.SELECTION_SIZE));
//            generations[i] = new Population(Settings.POPULATION_SIZE, generations[i - 1].selection(Settings.SELECTION_SIZE));
//            generations[i].play(Settings.GAME_COUNT, bestSoFar);
//            generations[i].play(generations[i - 1].population);
//            generations[i].play(initialOpponents);
            currentPop.play(initialOpponents);
            FFNNAIPlayer best = currentPop.selection(1)[0];
            if (best.getWinRate() > bestSoFar.getWinRate()) {
                bestSoFar = best;
                bestFromIt = i;
            }
            if (currentPop.getAverageScore() > bestPopulation.getAverageScore()) {
                bestPopulation = currentPop;
                bestIt = i;
            }
//            System.out.println(i + ": " + getStats(currentPop) + " (best: s=" + best.getScore() + " w=" + best.getWinRate() + ")");
            System.out.printf("%0,2d: %s (best: s=%f, w=%.2f, m=%.2f)\n", i, getStats(currentPop), best.getScore(), best.getWinRate(), best.getAvgMovecount());
        }
    }

    public FFNNAIPlayer getBest() {
        return bestSoFar;
    }
//
//    public Stats[] getStats() {
//        Stats[] stats = new Stats[generations.length];
//
//        for (int i = 0; i < stats.length; i++) {
//            stats[i] = getStats(generations[i]);
//        }
//
//        return stats;
//    }

    private Stats getStats(Population population) {
        if (population == null) {
            return null;
        } else {
            return new Stats(population.getAverageScore(), population.getWinRate(), population.getDrawRate(), population.getLossRate(), population.getAverageMoves());
        }
    }

    public class Stats {

        public final double score;
        public final double winRate;
        public final double drawRate;
        public final double lossRate;
        public final double avgMoves;

        public Stats(double score, double winRate, double drawRate, double lossRate, double avgMoves) {
            this.score = score;
            this.winRate = winRate;
            this.drawRate = drawRate;
            this.lossRate = lossRate;
            this.avgMoves = avgMoves;
        }

        @Override
        public String toString() {
            return String.format("Score: %f, wins: %f, draws: %f, losses: %f, avgMoves: %f", score, winRate, drawRate, lossRate, avgMoves);
        }

    }

    public static void main(String[] args) {
        Trainer trainer = new Trainer();
//        trainer.train(new SimpleAIPlayer());
//        trainer.train(new RandomPlayer());
        Player[] initialPlayers = new Player[Settings.POPULATION_SIZE];
        for (int i = 0; i < initialPlayers.length; i++) {
            initialPlayers[i] = new SimpleAIPlayer();
        }
//        for (int i = 0; i < initialPlayers.length; i++) {
//            initialPlayers[i] = new FFNNAIPlayer(new FeedForwardNN(Settings.ffnnShape, Settings.functionType));
//        }
//        trainer.train(new FFNNAIPlayer(new FeedForwardNN(Settings.ffnnShape, Settings.functionType)));
        trainer.train(initialPlayers);
        System.out.printf("Best it: %d, best AI from: %d\n", trainer.bestIt, trainer.bestFromIt);
        Game g1 = new Game(new SimpleAIPlayer(), new FFNNAIPlayer(trainer.getBest().ffnn));
//        Game g = new Game(new FFNNAIPlayer(new FeedForwardNN(Settings.ffnnShape, Settings.functionType)), trainer.getBest());
//        Game g = new Game(new FFNNAIPlayer(trainer.getBest().ffnn), new FFNNAIPlayer(trainer.getBest().ffnn));
        g1.play(true, true);
        System.out.println("==========================");
        Game g2 = new Game(new SimpleAIPlayer(), new FFNNAIPlayer(trainer.getBest().ffnn));
        g2.play(false, true);
//        System.out.println("==========================");
//        Game g3 = new Game(new SimpleAIPlayer(), new FFNNAIPlayer(trainer.getBest().ffnn));
//        g3.play(false, true);
//        System.out.println("==========================");
//        Game g4 = new Game(new RandomPlayer(), new FFNNAIPlayer(trainer.getBest().ffnn));
//        g4.play(false, true);
    }

}
