/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.nn;

import be.marcowillems.connect4ai.util.Matrix;

/**
 *
 * @author Marco Willems
 */
public class BackPropagationNetwork {

    public final int layerCount;
    public final int[] shape;
    public final Matrix[] weights;
    public final TransferFunctions.Type tfType;

    // Filled in during run, used for training
    private Matrix[] layerInputs; // Before transfer function
    private Matrix[] layerOutputs; // After transfer function
    private Matrix[] previousWeightDelta; // For momentum

    public BackPropagationNetwork(int[] layerSize) {
        this(layerSize, TransferFunctions.DEFAULT);
    }

    public BackPropagationNetwork(int[] layerSize, TransferFunctions.Type tfType) {
        this.tfType = tfType;

        // Initialize the network
        // Layer info
        this.layerCount = layerSize.length - 1;
        this.shape = layerSize;

        // Data from last Run
        layerInputs = new Matrix[layerCount];
        layerOutputs = new Matrix[layerCount];
        previousWeightDelta = new Matrix[layerCount];

        // Create the weight arrays
        weights = new Matrix[layerCount];
        for (int i = 0; i < layerCount; i++) {
            weights[i] = Matrix.rand(layerSize[i] + 1, layerSize[i + 1], -1, 1);
            previousWeightDelta[i] = new Matrix(layerSize[i] + 1, layerSize[i + 1]);
        }
    }

    public Matrix run(Matrix inputs) {
        layerInputs = new Matrix[layerCount];
        layerOutputs = new Matrix[layerCount];

        Matrix lastOutputs = inputs;
        for (int index = 0; index < layerCount; index++) {
            // adding bias
//            Matrix biased = lastOutputs;
            Matrix biased = lastOutputs.addCol(1.0);

            // Determine layer input
            Matrix layerInput = biased.mul(weights[index]);
            layerInputs[index] = layerInput;
//            lastOutputs = sigmoid(layerInput, false);
            lastOutputs = TransferFunctions.calc(layerInput, false, tfType);
            layerOutputs[index] = lastOutputs;
        }
        return lastOutputs;
    }

    public double trainEpoch(Matrix inputs, Matrix targets, double trainingRate, double momentum) {
        double error = 0.0;

        Matrix[] delta = new Matrix[layerCount];

        // First run the network
        run(inputs);

        // Calculate our deltas
        for (int index = layerCount - 1; index >= 0; index--) {
            if (index == layerCount - 1) {
                // Compare to the target values
                Matrix outputError = layerOutputs[index].sub(targets);
//                delta[index] = outputError.dotProd(TransferFunctions.sigmoid(layerOutputs[index], true));
                delta[index] = outputError.dotProd(TransferFunctions.calc(layerInputs[index], true, tfType));
                error = outputError.dotProd(outputError).sum();
            } else {
                // Compare to the following layer's delta
                Matrix outputError = delta[index + 1].mul(weights[index + 1].T()); // index + 1??
//                delta[index] = outputError.dotProd(sigmoid(layerOutputs[index], true));
//                delta[index] = outputError.delLastCol().dotProd(TransferFunctions.sigmoid(layerOutputs[index], true));
                delta[index] = outputError.delLastCol().dotProd(TransferFunctions.calc(layerInputs[index], true, tfType));
            }
        }

        // Compute weight deltas
        for (int index = 0; index < layerCount; index++) {
            Matrix layerOutput;
            if (index == 0) {
//                layerOutput = inputs;
                layerOutput = inputs.addCol(1);
            } else {
//                layerOutput = layerOutputs[index - 1];
                layerOutput = layerOutputs[index - 1].addCol(1);
            }

//            Matrix weightDelta = layerOutput.T().mul(delta[index]);
            Matrix currWeightDelta = layerOutput.T().mul(delta[index]);
            Matrix weightDelta = currWeightDelta.dotProd(trainingRate).add(previousWeightDelta[index].dotProd(momentum));

//            weights[index] = weights[index].sub(weightDelta.dotProd(trainingRate));
            weights[index] = weights[index].sub(weightDelta);

            previousWeightDelta[index] = weightDelta;
        }

        return error;
    }

}
