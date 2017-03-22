/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai;

import be.marcowillems.connect4ai.nn.TransferFunctions;
import java.util.Random;

/**
 *
 * @author Marco Willems
 */
public abstract class Settings {

    // Random
    public static final Random rg = new Random(System.currentTimeMillis());

    // Game
    public static final int BOARD_HEIGHT = 6;
    public static final int BOARD_WIDTH = 7;
    // Populations
    public static final int GENERATION_COUNT = 10 * 4;
    public static final int POPULATION_SIZE = 100 * 1;
//    public static final int GAME_COUNT = 100;
    // AI
    public static final int[] ffnnShape
            = new int[]{BOARD_HEIGHT * BOARD_WIDTH,
                //                BOARD_HEIGHT * BOARD_WIDTH,
                (BOARD_HEIGHT + BOARD_WIDTH) * (BOARD_HEIGHT + BOARD_WIDTH),
                //                (BOARD_HEIGHT + 1) * (BOARD_WIDTH + 1),
                //                BOARD_HEIGHT * BOARD_WIDTH,
                BOARD_HEIGHT * BOARD_WIDTH / 4,
                //                2 * (BOARD_HEIGHT + BOARD_WIDTH),
                //                BOARD_WIDTH,
                BOARD_WIDTH};
//    public static final TransferFunctions.Type functionType = TransferFunctions.SIGMOID;
    public static final TransferFunctions.Type functionType = TransferFunctions.GAUSSIAN;
//    public static final TransferFunctions.Type functionType = TransferFunctions.TANH;
    // Training
//    public static final int SELECTION_SIZE = POPULATION_SIZE / 4;
    public static final int SELECTION_SIZE = 10;
    public static final int PARENTS_PER_CHILD = 2;
    public static final double BAD_RATIO = 0.05;
    public static final double MUTATION_RATIO = 0.02;
    public static final double DRAW_WEIGHT = 0.2;
    public static final double ERROR_WEIGHT = -10.0;
    public static final double MOVES_WEIGHT = 0.005;

}
