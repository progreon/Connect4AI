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
public final class TransferFunctions {

    public static enum Type {

        GAUSSIAN, LINEAR, SIGMOID, TANH
    }

    public static final Type DEFAULT = Type.SIGMOID;
    public static final Type GAUSSIAN = Type.GAUSSIAN;
    public static final Type LINEAR = Type.LINEAR;
    public static final Type SIGMOID = Type.SIGMOID;
    public static final Type TANH = Type.TANH;

    public static Matrix calc(Matrix m, boolean deriv) {
        return calc(m, deriv, DEFAULT);
    }

    public static Matrix calc(Matrix m, boolean deriv, Type tf) {
        switch (tf) {
            case GAUSSIAN:
                return gaussian(m, deriv);
            case LINEAR:
                return linear(m, deriv);
            case TANH:
                return tanh(m, deriv);
            case SIGMOID:
            default:
                return sigmoid(m, deriv);
        }
    }

    public static double calc(double d, boolean deriv) {
        return calc(d, deriv, DEFAULT);
    }

    public static double calc(double d, boolean deriv, Type tf) {
        switch (tf) {
            case GAUSSIAN:
                return gaussian(d, deriv);
            case LINEAR:
                return linear(d, deriv);
            case TANH:
                return tanh(d, deriv);
            case SIGMOID:
            default:
                return sigmoid(d, deriv);
        }
    }

    private static Matrix gaussian(Matrix m, boolean deriv) {
        Matrix mat = new Matrix(m.h, m.w);
        for (int r = 0; r < m.h; r++) {
            for (int c = 0; c < m.w; c++) {
                mat.set(r, c, gaussian(m.get(r, c), deriv));
            }
        }
        return mat;
    }

    private static double gaussian(double d, boolean deriv) {
        if (deriv) {
            return -2 * d * Math.exp(-d * d);
        } else {
            return Math.exp(-d * d);
        }
    }

    private static Matrix linear(Matrix m, boolean deriv) {
        Matrix mat = new Matrix(m.h, m.w);
        for (int r = 0; r < m.h; r++) {
            for (int c = 0; c < m.w; c++) {
                mat.set(r, c, linear(m.get(r, c), deriv));
            }
        }
        return mat;
    }

    private static double linear(double d, boolean deriv) {
        if (deriv) {
            return 1;
        } else {
            return d;
        }
    }

    private static Matrix sigmoid(Matrix m, boolean deriv) {
        Matrix mat = new Matrix(m.h, m.w);
        for (int r = 0; r < m.h; r++) {
            for (int c = 0; c < m.w; c++) {
                mat.set(r, c, sigmoid(m.get(r, c), deriv));
            }
        }
        return mat;
    }

    private static double sigmoid(double d, boolean deriv) {
        if (deriv) {
            d = sigmoid(d, false);
            return d * (1 - d);
        } else {
            return 1 / (1 + Math.exp(-d));
        }
    }

    private static Matrix tanh(Matrix m, boolean deriv) {
        Matrix mat = new Matrix(m.h, m.w);
        for (int r = 0; r < m.h; r++) {
            for (int c = 0; c < m.w; c++) {
                mat.set(r, c, tanh(m.get(r, c), deriv));
            }
        }
        return mat;
    }

    private static double tanh(double d, boolean deriv) {
        if (deriv) {
            return 1 - Math.tanh(d) * Math.tanh(d);
        } else {
            return Math.tanh(d);
        }
    }

}
