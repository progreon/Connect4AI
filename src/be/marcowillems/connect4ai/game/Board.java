/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.game;

/**
 * NOTE: board is upside down
 *
 * @author Marco Willems
 */
public class Board {

    private final int[][] b; // 0 when no piece, 1 and 2 for player piece, stored upside down

    public Board(int height, int width) {
        b = new int[height][width];
    }

    public int get(int row, int col) {
        return b[row][col];
    }

    public int getHeight() {
        return b.length;
    }

    public int getWidth() {
        return b[0].length;
    }

    public boolean hasSpaceLeft() {
        boolean spaceLeft = false;
        for (int i = 0; i < getWidth(); i++) {
            if (get(getHeight() - 1, i) == 0) {
                spaceLeft = true;
            }
        }
        return spaceLeft;
    }

    public boolean inBounds(int row, int col) {
        return 0 <= row && row < getHeight() && 0 <= col && col < getWidth();
    }

    public int pieceWillFallInRow(int col) {
        if (inBounds(0, col)) {
            int r = 0; // row it falls in
            while (r < getHeight() && get(r, col) != 0) {
                r++;
            }
            return r == getHeight() ? -1 : r;
        } else {
            return -1;
        }
    }

    public boolean pieceWillFallInRow(int row, int col) {
        if (inBounds(row, col) && get(row, col) == 0) {
            return pieceWillFallInRow(col) == row;
        } else {
            return false;
        }
    }

    public boolean put(int col, boolean isP1) {
        if (inBounds(0, col)) {
            // check row it will be placed in
            int row = 0;
            while (row < getHeight() && get(row, col) != 0) {
                row++;
            }
            if (row == getHeight()) {
                return false;
            } else {
                b[row][col] = isP1 ? 1 : 2;
                return true;
            }
        } else {
            return false;
        }
    }

    public double[] toArray() {
        double[] array = new double[getHeight() * getWidth()];

        for (int r = 0; r < getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                array[r * getWidth() + c] = get(r, c) / 2.0;
            }
        }

        return array;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < getWidth() + 1; i++) {
            str += "--";
        }
        str += "-\n";
        for (int r = getHeight() - 1; r >= 0; r--) {
            str += "|";
            for (int c = 0; c < getWidth(); c++) {
                str += " " + (b[r][c] == 0 ? "." : b[r][c]);
            }
            str += " |\n";
        }
        for (int i = 0; i < getWidth() + 1; i++) {
            str += "--";
        }
        str += "-";

        return str;
    }

}
