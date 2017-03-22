/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.players;

import be.marcowillems.connect4ai.game.Board;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Marco Willems
 */
public class HumanPlayer extends Player {

    @Override
    public boolean _doMove(Board b, boolean isP1) {
//        System.out.println(b);
        System.out.println("Choose a column to move your piece in:");
        Scanner scanner = new Scanner(System.in);

        boolean success = false;
        while (!success) {
            try {
                String line = scanner.nextLine();
                int input = Integer.parseInt(line);

                if (input >= 0 && input < b.getWidth()) {
                    success = b.put(input, isP1);
                }
            } catch (InputMismatchException | NumberFormatException ex) {
            }
            if (!success) {
                System.out.println("Invalid input, please give a column number between 0 and " + (b.getWidth() - 1));
            }
        }

        return success;
    }

}
