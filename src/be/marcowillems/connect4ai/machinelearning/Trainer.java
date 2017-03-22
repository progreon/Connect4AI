/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.machinelearning;

import be.marcowillems.connect4ai.Settings;
import be.marcowillems.connect4ai.players.Player;
import be.marcowillems.connect4ai.players.RandomPlayer;
import be.marcowillems.connect4ai.players.SimpleAIPlayer;

/**
 *
 * @author Marco Willems
 */
public class Trainer {

    private final Population[] generations;
    private final Settings settings;

    public Trainer(Settings settings) {
        this.settings = settings;
        this.generations = new Population[settings.GENERATION_COUNT];
    }

    public void train(Player opponent) {
        System.out.println("Training...");
        generations[0] = new Population(settings.POPULATION_SIZE);
        generations[0].play(settings.GAME_COUNT, opponent);
        System.out.println(getStats(generations[0]));
        for (int i = 1; i < generations.length; i++) {
            generations[i] = new Population(settings.POPULATION_SIZE, generations[i - 1].selection(settings.SELECTION_SIZE, settings.BAD_RATIO));
            generations[i].play(settings.GAME_COUNT, opponent);
            System.out.println(getStats(generations[i]));
        }
    }

    public Stats[] getStats() {
        Stats[] stats = new Stats[generations.length];

        for (int i = 0; i < stats.length; i++) {
            stats[i] = getStats(generations[i]);
        }

        return stats;
    }

    private Stats getStats(Population population) {
        if (population == null) {
            return null;
        } else {
            return new Stats(population.getAverageScore(), population.getWinRate());
        }
    }

    public class Stats {

        public final double score;
        public final double winRate;

        public Stats(double score, double winRate) {
            this.score = score;
            this.winRate = winRate;
        }

        @Override
        public String toString() {
            return String.format("Score: %f, win rate: %f", score, winRate);
        }

    }

    public static void main(String[] args) {
        Trainer trainer = new Trainer(new Settings());
        trainer.train(new SimpleAIPlayer());
//        trainer.train(new RandomPlayer());
    }

}
