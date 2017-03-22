/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.players;

import be.marcowillems.connect4ai.Settings;
import be.marcowillems.connect4ai.game.Board;
import java.util.PriorityQueue;

/**
 *
 * @author Marco Willems
 */
public class SimpleAIPlayer extends Player {

    @Override
    public boolean _doMove(Board b, boolean isP1) {
        // TODO
        // Strategy (with priorities):
        //  0) if 3 in a row, try making it 4 in a row in 1 move
        //  1) if opponent has 3 in a row, try blocking it in 1 move
        //  2) if 2 in a row, try making it 3 in a row in 1 move
        //  3) if opponent has 2 in a row, try blocking it in 1 move (opt)
        //  4) if 1 in a row, try making it 2 in a row in 1 move (opt)
        //  9) else place at random

        PriorityQueue<NextMove> nextQueue = new PriorityQueue<>();
        int pID = isP1 ? 1 : 2;

        // Check colums
        for (int col = 0; col < b.getWidth(); col++) {
            int startMe = 0;
            int startOther = 0;
            int row = 0;
            while (row < b.getHeight() && b.get(row, col) != 0) {
                int val = b.get(row, col);
                row++;
                if (val == pID) {
                    startOther = row;
                    if (row < b.getHeight() && b.get(row, col) == 0) {
                        switch (row - startMe) {
                            case 1:
                                nextQueue.add(new NextMove(col, 4));
                                break;
                            case 2:
                                nextQueue.add(new NextMove(col, 2));
                                break;
                            case 3:
                                nextQueue.add(new NextMove(col, 0));
                                break;
                        }
                    }
                } else if (val == 0) {
                    startMe = row;
                    startOther = row;
                } else { // other player
                    startMe = row;
                    if (row < b.getHeight() && b.get(row, col) == 0) {
                        switch (row - startOther) {
//                            case 1:
//                                nextQueue.add(new NextMove(col, 5));
//                                break;
                            case 2:
                                nextQueue.add(new NextMove(col, 3));
                                break;
                            case 3:
                                nextQueue.add(new NextMove(col, 1));
                                break;
                        }
                    }
                }
            }
        }

        // Check rows
        for (int row = 0; row < b.getHeight(); row++) {
            int startMe = 0;
            int startOther = 0;
            int col = 0;
            while (col < b.getWidth()) {
                int val = b.get(row, col);
                col++;
                if (val == pID) {
                    startOther = col;
                    switch (col - startMe) {
                        case 1:
                            addMoveIfPossible(nextQueue, 4, b, row, col);
                            addMoveIfPossible(nextQueue, 4, b, row, startMe - 1);
                            break;
                        case 2:
                            addMoveIfPossible(nextQueue, 2, b, row, col);
                            addMoveIfPossible(nextQueue, 2, b, row, startMe - 1);
                            break;
                        case 3:
                            addMoveIfPossible(nextQueue, 0, b, row, col);
                            addMoveIfPossible(nextQueue, 0, b, row, startMe - 1);
                            break;
                    }
                } else if (val == 0) {
                    startMe = col;
                    startOther = col;
                } else { // other player
                    startMe = col;
                    switch (col - startOther) {
//                        case 1:
//                            addMoveIfPossible(nextQueue, 5, b, row, col);
//                            addMoveIfPossible(nextQueue, 5, b, row, startOther - 1);
//                            break;
                        case 2:
                            addMoveIfPossible(nextQueue, 3, b, row, col);
                            addMoveIfPossible(nextQueue, 3, b, row, startOther - 1);
                            break;
                        case 3:
                            addMoveIfPossible(nextQueue, 1, b, row, col);
                            addMoveIfPossible(nextQueue, 1, b, row, startOther - 1);
                            break;
                    }
                }
            }
        }

        // Check diagonals
        // top-left to bottom-right
        int[][] startpos = new int[b.getHeight() + b.getWidth() - 1][2];
        for (int i = 0; i < b.getHeight(); i++) {
            startpos[i][0] = i;
            startpos[i][1] = 0;
        }
        for (int i = 1; i < b.getWidth(); i++) {
            startpos[b.getHeight() + i - 1][0] = 0;
            startpos[b.getHeight() + i - 1][1] = i;
        }

        for (int[] startpo : startpos) {
            int row = startpo[0];
            int col = startpo[1];
//        }
//        for (int row = 0; row < b.getHeight(); row++) {
            int startMe = col;
            int startOther = col;
//            int col = 0;
            while (row < b.getHeight() && col < b.getWidth()) {
                int val = b.get(row, col);
                col++;
                row++;
                if (val == pID) {
                    startOther = col;
                    switch (col - startMe) {
                        case 1:
                            addMoveIfPossible(nextQueue, 4, b, row, col);
                            addMoveIfPossible(nextQueue, 4, b, row, startMe - 1);
                            break;
                        case 2:
                            addMoveIfPossible(nextQueue, 2, b, row, col);
                            addMoveIfPossible(nextQueue, 2, b, row, startMe - 1);
                            break;
                        case 3:
                            addMoveIfPossible(nextQueue, 0, b, row, col);
                            addMoveIfPossible(nextQueue, 0, b, row, startMe - 1);
                            break;
                    }
                } else if (val == 0) {
                    startMe = col;
                    startOther = col;
                } else { // other player
                    startMe = col;
                    switch (col - startOther) {
//                        case 1:
//                            addMoveIfPossible(nextQueue, 5, b, row, col);
//                            addMoveIfPossible(nextQueue, 5, b, row, startOther - 1);
//                            break;
                        case 2:
                            addMoveIfPossible(nextQueue, 3, b, row, col);
                            addMoveIfPossible(nextQueue, 3, b, row, startOther - 1);
                            break;
                        case 3:
                            addMoveIfPossible(nextQueue, 1, b, row, col);
                            addMoveIfPossible(nextQueue, 1, b, row, startOther - 1);
                            break;
                    }
                }
            }
        }

        // top-right to bottom-left
        startpos = new int[b.getHeight() + b.getWidth() - 1][2];
        for (int i = 0; i < b.getHeight(); i++) {
            startpos[i][0] = i;
            startpos[i][1] = b.getWidth() - 1;
        }
        for (int i = 0; i < b.getWidth() - 1; i++) {
            startpos[b.getHeight() + i][0] = 0;
            startpos[b.getHeight() + i][1] = i;
        }

        for (int[] startpo : startpos) {
            int row = startpo[0];
            int col = startpo[1];
//        }
//        for (int row = 0; row < b.getHeight(); row++) {
            int startMe = col;
            int startOther = col;
//            int col = 0;
            while (row < b.getHeight() && col >= 0) {
                int val = b.get(row, col);
                col--;
                row++;
                if (val == pID) {
                    startOther = col;
                    switch (startMe - col) {
                        case 1:
                            addMoveIfPossible(nextQueue, 4, b, row, col);
                            addMoveIfPossible(nextQueue, 4, b, row, startMe + 1);
                            break;
                        case 2:
                            addMoveIfPossible(nextQueue, 2, b, row, col);
                            addMoveIfPossible(nextQueue, 2, b, row, startMe + 1);
                            break;
                        case 3:
                            addMoveIfPossible(nextQueue, 0, b, row, col);
                            addMoveIfPossible(nextQueue, 0, b, row, startMe + 1);
                            break;
                    }
                } else if (val == 0) {
                    startMe = col;
                    startOther = col;
                } else { // other player
                    startMe = col;
                    switch (col - startOther) {
//                        case 1:
//                            addMoveIfPossible(nextQueue, 5, b, row, col);
//                            addMoveIfPossible(nextQueue, 5, b, row, startOther + 1);
//                            break;
                        case 2:
                            addMoveIfPossible(nextQueue, 3, b, row, col);
                            addMoveIfPossible(nextQueue, 3, b, row, startOther + 1);
                            break;
                        case 3:
                            addMoveIfPossible(nextQueue, 1, b, row, col);
                            addMoveIfPossible(nextQueue, 1, b, row, startOther + 1);
                            break;
                    }
                }
            }
        }

        // Random move
        int move = Settings.rg.nextInt(b.getWidth());
        int result = b.pieceWillFallInRow(move);
        while (result == -1) {
            move = Settings.rg.nextInt(b.getWidth());
            result = b.pieceWillFallInRow(move);
        }
        nextQueue.add(new NextMove(move, 9));

        // Print possible moves
//        System.out.println(nextQueue);
        // Do a move
        return nextQueue.peek().apply(b, isP1);
    }

    private void addMoveIfPossible(PriorityQueue<NextMove> nextQueue, int priority, Board b, int row, int col) {
        if (b.pieceWillFallInRow(row, col)) {
            nextQueue.add(new NextMove(col, priority));
        }
    }

    private class NextMove implements Comparable<NextMove> {

        final int priority;
        final int action;

        public NextMove(int action, int priority) {
            this.action = action;
            this.priority = priority;
        }

        public boolean apply(Board b, boolean isP1) {
            return b.put(action, isP1);
        }

        @Override
        public int compareTo(NextMove o) {
            return priority - o.priority;
        }

        @Override
        public String toString() {
            return "{r:" + action + "|" + priority + "}";
        }
    }

}
