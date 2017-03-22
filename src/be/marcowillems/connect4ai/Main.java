/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai;

import be.marcowillems.connect4ai.util.Matrix;
import be.marcowillems.connect4ai.nn.BackPropagationNetwork;
import java.util.Arrays;

/**
 *
 * @author Marco Willems
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        BackPropagationNetwork bpn = new BackPropagationNetwork(new int[]{3, 4, 1}, TransferFunctions.GAUSSIAN);
        BackPropagationNetwork bpn = new BackPropagationNetwork(new int[]{3, 4, 1});
        System.out.println(Arrays.toString(bpn.shape));
        System.out.println(Arrays.toString(bpn.weights));

        Matrix lvInput = new Matrix(new double[][]{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {0, 1, 1}, {1, 0, 0}, {1, 0, 1}, {1, 1, 0}, {1, 1, 1}});
//        Matrix lvTarget = new Matrix(new double[]{0.0005, 0.9995, 0.9995, 0.0005, 0.9995, 0.0005, 0.0005, 0.9995}).T();
        Matrix lvTarget = new Matrix(new double[]{0, 1, 2, 3, 4, 5, 6, 7}).dotProd(1.0/8).T();

        // Train
        int lnMax = 60000;
        double lnErr = 1e-5;
        for (int i = 0; i < lnMax; i++) {
            double err = bpn.trainEpoch(lvInput, lvTarget, 1, 0);
            if (i % 10000 == 0) {
                System.out.printf("Error at %d: %f\n", i, err);
            }
            if (err < lnErr) {
                System.out.printf("Error small enough after %d iterations!\n", i);
                break;
            }
        }

        // Display output
        Matrix lvOutput = bpn.run(lvInput);
        System.out.println("Input: \n" + lvInput);
        System.out.println("Output: \n" + lvOutput.dotProd(8));
    }

}
