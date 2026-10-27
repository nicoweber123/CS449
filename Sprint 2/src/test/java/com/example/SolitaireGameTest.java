package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SolitaireGameTest {

    @Test
    void testSelectPieceAndMakeMove() {
        SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);

        int initialPegCount = game.getBoard().getPegCount();

        // Select a peg.
        assertTrue(game.select(1, 3));
        assertEquals(new Position(1, 3), game.getSelected());

        // Move the peg over another peg.
        assertTrue(game.moveSelectedTo(3, 3));

        // Check that the board was updated.
        assertFalse(game.getBoard().isOccupied(1, 3));
        assertFalse(game.getBoard().isOccupied(2, 3));
        assertTrue(game.getBoard().isOccupied(3, 3));
        assertEquals(initialPegCount - 1,
                     game.getBoard().getPegCount());
    }

    @Test
    void testGameOverWhenNoMovesRemain() {
        SolitaireGame game = new SolitaireGame(7, BoardType.ENGLISH);
        Board board = game.getBoard();

        // Remove all pegs from valid positions.
        for (Position position : board.getValidPositions()) {
            board.setOccupied(
                position.row(), position.col(), false
            );
        }

        // Leave one peg on the board.
        board.setOccupied(0, 3, true);

        // No legal moves should remain.
        assertTrue(game.isGameOver());

        // A jump without a peg to jump over must fail.
        assertFalse(game.makeMove(0, 3, 2, 3));
    }
}