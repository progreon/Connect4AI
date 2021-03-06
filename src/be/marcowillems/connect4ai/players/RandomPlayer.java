/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.marcowillems.connect4ai.players;

import be.marcowillems.connect4ai.Settings;
import be.marcowillems.connect4ai.game.Board;

/**
 *
 * @author Marco Willems
 */
public class RandomPlayer extends Player {

    @Override
    public boolean _doMove(Board b, boolean isP1) {
        if (b.hasSpaceLeft()) {
            boolean success = false;
            while (!success) {
                int move = Settings.rg.nextInt(b.getWidth());
                success = b.put(move, isP1);
            }
            return true;
        } else {
            return false;
        }
    }

}
