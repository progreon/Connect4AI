/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.game;

import be.marcowillems.connect4ai.Settings;
import be.marcowillems.connect4ai.players.Player;
import be.marcowillems.connect4ai.nn.FeedForwardNN;
import be.marcowillems.connect4ai.players.FFNNAIPlayer;

/**
 *
 * @author Marco Willems
 */
public class Game {

    public static enum State {

        PLAYING, DRAW, P1WON, P2WON, ERROR;
    }

    public final Board board;

    private final Player p1, p2;
    private boolean isTurnP1;
    private State state;

    public Game(Player p1, Player p2) {
        board = new Board(Settings.BOARD_HEIGHT, Settings.BOARD_WIDTH);
        this.p1 = p1;
        this.p2 = p2;
        isTurnP1 = true;
        state = State.PLAYING;
    }

    public State play(boolean p1Starts) {
        return play(p1Starts, false);
    }

    public State play(boolean p1Starts, boolean withPrints) {
        isTurnP1 = p1Starts;
        while (state == State.PLAYING) {
            if (withPrints) {
                System.out.println(board);
            }
            doTurn();
            isTurnP1 = !isTurnP1;
        }
        switch (state) {
            case P1WON:
                p1.addWin();
                p2.addLoss();
                break;
            case P2WON:
                p1.addLoss();
                p2.addWin();
                break;
            case ERROR:
                break;
            default:
                p1.addDraw();
                p2.addDraw();
                break;
        }
        if (withPrints) {
            System.out.println(board);
            System.out.println("Game result: " + state);
        }
        return state;
    }

    private State doTurn() {
        if (isTurnP1) {
            if (!p1.doMove(board, true)) {
                state = State.ERROR;
                p1.addError();
                p2.addDraw();
                return state;
            }
        } else {
            if (!p2.doMove(board, false)) {
                state = State.ERROR;
                p1.addDraw();
                p2.addError();
                return state;
            }
        }
        return updateGameState();
    }

    private int colWinner() {
        int p1Count = 0;
        int p2Count = 0;

        for (int c = 0; c < board.getWidth(); c++) {
            for (int r = 0; r < board.getHeight(); r++) {
                double val = board.get(r, c);
                switch ((int) val) {
                    case 1:
                        p2Count = 0;
                        p1Count++;
                        break;
                    case 2:
                        p1Count = 0;
                        p2Count++;
                        break;
                    default:
                        p1Count = 0;
                        p2Count = 0;
                        break;
                }
                if (p1Count == 4) {
                    return 1;
                } else if (p2Count == 4) {
                    return 2;
                }
            }
            p1Count = 0;
            p2Count = 0;
        }

        return 0;
    }

    private int diagWinner() {

        // top-left to bottom-right
        int[][] startpos = new int[board.getHeight() + board.getWidth() - 1][2];
        for (int i = 0; i < board.getHeight(); i++) {
            startpos[i][0] = i;
            startpos[i][1] = 0;
        }
        for (int i = 1; i < board.getWidth(); i++) {
            startpos[board.getHeight() + i - 1][0] = 0;
            startpos[board.getHeight() + i - 1][1] = i;
        }

        for (int[] startpo : startpos) {
            int r = startpo[0];
            int c = startpo[1];
            int p1Count = 0;
            int p2Count = 0;
            while (r < board.getHeight() && c < board.getWidth()) {
                int val = board.get(r, c);
                switch (val) {
                    case 1:
                        p2Count = 0;
                        p1Count++;
                        break;
                    case 2:
                        p1Count = 0;
                        p2Count++;
                        break;
                    default:
                        p1Count = 0;
                        p2Count = 0;
                        break;
                }
                if (p1Count == 4) {
                    return 1;
                } else if (p2Count == 4) {
                    return 2;
                }
                r++;
                c++;
            }
        }

        // top-right to bottom-left
        startpos = new int[board.getHeight() + board.getWidth() - 1][2];
        for (int i = 0; i < board.getHeight(); i++) {
            startpos[i][0] = i;
            startpos[i][1] = board.getWidth() - 1;
        }
        for (int i = 0; i < board.getWidth() - 1; i++) {
            startpos[board.getHeight() + i][0] = 0;
            startpos[board.getHeight() + i][1] = i;
        }

        for (int[] startpo : startpos) {
            int r = startpo[0];
            int c = startpo[1];
            int p1Count = 0;
            int p2Count = 0;
            while (r < board.getHeight() && c >= 0) {
                int val = board.get(r, c);
                switch (val) {
                    case 1:
                        p2Count = 0;
                        p1Count++;
                        break;
                    case 2:
                        p1Count = 0;
                        p2Count++;
                        break;
                    default:
                        p1Count = 0;
                        p2Count = 0;
                        break;
                }
                if (p1Count == 4) {
                    return 1;
                } else if (p2Count == 4) {
                    return 2;
                }
                r++;
                c--;
            }
        }

        return 0;
    }

    private int rowWinner() {
        int p1Count = 0;
        int p2Count = 0;

        for (int r = 0; r < board.getHeight(); r++) {
            for (int c = 0; c < board.getWidth(); c++) {
                double val = board.get(r, c);
                switch ((int) val) {
                    case 1:
                        p2Count = 0;
                        p1Count++;
                        break;
                    case 2:
                        p1Count = 0;
                        p2Count++;
                        break;
                    default:
                        p1Count = 0;
                        p2Count = 0;
                        break;
                }
                if (p1Count == 4) {
                    return 1;
                } else if (p2Count == 4) {
                    return 2;
                }
            }
            p1Count = 0;
            p2Count = 0;
        }

        return 0;
    }

    private State updateGameState() {
        int winner = 0;
        if (winner == 0) {
            winner = colWinner();
        }
        if (winner == 0) {
            winner = rowWinner();
        }
        if (winner == 0) {
            winner = diagWinner();
        }

        switch (winner) {
            case 1:
                state = State.P1WON;
                break;
            case 2:
                state = State.P2WON;
                break;
            default:
                if (board.hasSpaceLeft()) {
                    state = State.PLAYING;
                } else {
                    state = State.DRAW;
                }
                break;
        }
        return state;
    }

    public static void main(String[] args) {
        FeedForwardNN ffnn = new FeedForwardNN(Settings.ffnnShape);
//        Game game = new Game(new RandomPlayer(), new HumanPlayer());
//        Game game = new Game(new SimpleAIPlayer(), new HumanPlayer());
        Game game = new Game(new FFNNAIPlayer(new FeedForwardNN(Settings.ffnnShape)), new FFNNAIPlayer(new FeedForwardNN(Settings.ffnnShape)));
//        Game game = new Game(new SimpleAIPlayer(), new FFNNAIPlayer(ffnn));
//        Game game = new Game(new FFNNAIPlayer(ffnn), new SimpleAIPlayer());
        State s = game.play(true);
    }

}
