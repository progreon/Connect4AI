/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.nn;

import be.marcowillems.connect4ai.util.Matrix;
import java.util.Random;

/**
 *
 * @author Marco Willems
 */
public class FeedForwardNN {

    private final Random rg = new Random(System.currentTimeMillis());
    private static final double minRand = -1.0;
    private static final double maxRand = +1.0;

    public final int layerCount;
    public final int[] shape;
    public final Matrix[] weights;
    public final TransferFunctions.Type tfType;

    public FeedForwardNN(int[] layerSize) {
        this(layerSize, TransferFunctions.DEFAULT);
    }

    public FeedForwardNN(int[] layerSize, TransferFunctions.Type tfType) {
        // What transfer function are we using
        this.tfType = tfType;

        // Layer info
        this.layerCount = layerSize.length - 1;
        this.shape = layerSize;

        // Create the weight arrays
        weights = new Matrix[layerCount];
        for (int i = 0; i < layerCount; i++) {
            weights[i] = Matrix.rand(layerSize[i] + 1, layerSize[i + 1], minRand, maxRand); // Adding 1 for the bias nodes
        }
    }

    public FeedForwardNN(FeedForwardNN[] parents) {
        this(parents, 0.0);
    }

    public FeedForwardNN(FeedForwardNN[] parents, double mutation) {
        this(parents[0].shape, parents[0].tfType);
        // TODO: combine
        double range = maxRand - minRand;
        for (int w = 0; w < weights.length; w++) { // for each layer int he neural net
            for (int r = 0; r < weights[w].h; r++) {
                for (int c = 0; c < weights[w].w; c++) {
                    int p = rg.nextInt(parents.length);
                    if (rg.nextDouble() < mutation) {
                        weights[w].set(r, c, range * rg.nextDouble() + minRand);
                    } else {
                        weights[w].set(r, c, parents[p].weights[w].get(r, c));
                    }
                }
            }
        }
    }

    public Matrix run(Matrix inputs) {
        Matrix lastOutputs = inputs;
        for (int index = 0; index < layerCount; index++) {
//            // adding bias
            Matrix biased = lastOutputs.addCol(1.0);

            // Determine layer input
//            Matrix layerInput = lastOutputs.mul(weights[index]);
            Matrix layerInput = biased.mul(weights[index]);
            lastOutputs = TransferFunctions.calc(layerInput, false, tfType);
        }
        return lastOutputs;
    }

}
