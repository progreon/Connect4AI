/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.util;

import java.util.Arrays;
import java.util.Random;

/**
 * TODO: OpenGL subclass
 *
 * @author Marco Willems
 */
public final class Matrix {

    private static Random rg = new Random(System.currentTimeMillis());

    private double[][] vals;
    public final int h, w;

    public Matrix(int height, int width) {
        this.h = height;
        this.w = width;
        vals = new double[h][w];
    }

    public Matrix(double[] values) {
        this(new double[][]{values});
    }

    public Matrix(double[][] values) {
        if (!isMatrix(values)) {
            throw new RuntimeException("The values don't represent a matrix!");
        }
        this.h = values.length;
        this.w = values[0].length;
        this.vals = new double[h][];
        for (int r = 0; r < h; r++) {
            this.vals[r] = Arrays.copyOf(values[r], w);
        }
    }

    public Matrix add(double d) {
        Matrix mat = new Matrix(vals);

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                mat.vals[r][c] += d;
            }
        }

        return mat;
    }

    public Matrix add(Matrix m) {
        if (h == m.h && w == m.w) {
            Matrix mat = new Matrix(vals);
            for (int r = 0; r < h; r++) {
                for (int c = 0; c < w; c++) {
                    mat.vals[r][c] += m.vals[r][c];
                }
            }
            return mat;
        } else {
            throw new RuntimeException("Dimensions do not match!");
        }
    }

    public Matrix addCol(double d) {
        Matrix mat = new Matrix(h, w + 1);

        for (int r = 0; r < h; r++) {
            mat.vals[r] = Arrays.copyOf(vals[r], w + 1);
            mat.vals[r][w] = d;
        }

        return mat;
    }

    public Matrix addRow(double d) {
        Matrix mat = new Matrix(h + 1, w);

        for (int r = 0; r < h; r++) {
            mat.vals[r] = Arrays.copyOf(vals[r], w);
        }
        Arrays.fill(mat.vals[h], d);

        return mat;
    }

    public Matrix delLastCol() {
        Matrix mat = new Matrix(h, w - 1);

        for (int r = 0; r < h; r++) {
            mat.vals[r] = Arrays.copyOf(vals[r], w - 1);
        }

        return mat;
    }

    public Matrix delLastRow() {
        Matrix mat = new Matrix(h - 1, w);

        for (int r = 0; r < h - 1; r++) {
            mat.vals[r] = Arrays.copyOf(vals[r], w);
        }

        return mat;
    }

    public Matrix dotProd(double d) {
        Matrix mat = new Matrix(vals);

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                mat.vals[r][c] *= d;
            }
        }

        return mat;
    }

    public Matrix dotProd(Matrix m) {
        if (h == m.h && w == m.w) {
            Matrix mat = new Matrix(vals);

            for (int r = 0; r < h; r++) {
                for (int c = 0; c < w; c++) {
                    mat.vals[r][c] *= m.vals[r][c];
                }
            }

            return mat;
        } else {
            throw new RuntimeException("Dimensions do not match!");
        }
    }

    public double get(int row, int col) {
        return vals[row][col];
    }

    private boolean isMatrix(double[][] x) {
        if (x.length > 0) {
            int len = x[0].length;
            for (double[] row : x) {
                if (row.length != len) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public Matrix mul(Matrix m) {
        if (w == m.h) {
            Matrix mat = new Matrix(h, m.w);

            int d = w;
            for (int r = 0; r < mat.h; r++) { // loop rows
                for (int c = 0; c < mat.w; c++) { // loop cols
                    double sum = 0.0;
                    for (int i = 0; i < d; i++) {
                        sum += vals[r][i] * m.vals[i][c];
                    }
                    mat.vals[r][c] = sum;
                }
            }

            return mat;
        } else {
            throw new RuntimeException("Dimensions do not match!");
        }
    }

    public static Matrix rand(int rows, int cols, double min, double max) {
        Matrix mat = new Matrix(rows, cols);

        double range = max - min;
        for (int r = 0; r < mat.h; r++) {
            for (int c = 0; c < mat.w; c++) {
                mat.vals[r][c] = range * rg.nextDouble() + min;
            }
        }

        return mat;
    }

    public void set(int row, int col, double val) {
        vals[row][col] = val;
    }

    public Matrix sub(double d) {
        return add(-d);
    }

    public Matrix sub(Matrix m) {
        if (h == m.h && w == m.w) {
            Matrix mat = new Matrix(vals);
            for (int r = 0; r < h; r++) {
                for (int c = 0; c < w; c++) {
                    mat.vals[r][c] -= m.vals[r][c];
                }
            }
            return mat;
        } else {
            throw new RuntimeException("Dimensions do not match!");
        }
    }

    public double sum() {
        double sum = 0.0;

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                sum += vals[r][c];
            }
        }

        return sum;
    }

    public Matrix T() {
        Matrix mat = new Matrix(w, h);

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                mat.vals[c][r] = vals[r][c];
            }
        }

        return mat;
    }

    @Override
    public String toString() {
        String str = "[";
        for (double[] row : vals) {
            str += "[";
            for (double val : row) {
                str += String.format("%f, ", val);
            }
            str = str.substring(0, str.length() - 2);
            str += "],\n";
        }
        str = str.substring(0, str.length() - 2);
        str += "]";
        return str;
    }

}
