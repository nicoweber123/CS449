package com.example;

import java.util.ArrayList;
import java.util.List;

public class SolitaireGame {
    private Board board;
    private Position selected;

    public SolitaireGame(int size, BoardType type) {
        board = new Board(size, type);
    }

    public void newGame(int size, BoardType type) {
        board = new Board(size, type);
        selected = null;
    }

    public Board getBoard() {
        return board;
    }

    public Position getSelected() {
        return selected;
    }

    public void clearSelection() {
        selected = null;
    }

    public boolean select(int row, int col) {
        if (!board.isOccupied(row, col)) {
            return false;
        }
        selected = new Position(row, col);
        return true;
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        int midRow = (fromRow + toRow) / 2;
        int midCol = (fromCol + toCol) / 2;

        board.setOccupied(fromRow, fromCol, false);
        board.setOccupied(midRow, midCol, false);
        board.setOccupied(toRow, toCol, true);
        selected = null;
        return true;
    }

    public boolean moveSelectedTo(int toRow, int toCol) {
        if (selected == null) {
            return false;
        }

        boolean moved = makeMove(selected.row(), selected.col(), toRow, toCol);
        if (!moved) {
            return false;
        }
        return true;
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!board.isValid(fromRow, fromCol)
                || !board.isValid(toRow, toCol)
                || !board.isOccupied(fromRow, fromCol)
                || board.isOccupied(toRow, toCol)) {
            return false;
        }

        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        if (board.getType() == BoardType.HEXAGON) {
            boolean adjacent = isHexDirection(rowDiff, colDiff);
            if (!adjacent) {
                return false;
            }
        } else {
            boolean straight = (rowDiff == 0 && Math.abs(colDiff) == 2)
                    || (colDiff == 0 && Math.abs(rowDiff) == 2);
            if (!straight) {
                return false;
            }
        }

        int midRow = (fromRow + toRow) / 2;
        int midCol = (fromCol + toCol) / 2;

        return board.isValid(midRow, midCol) && board.isOccupied(midRow, midCol);
    }

    private boolean isHexDirection(int rowDiff, int colDiff) {
        return (rowDiff == 0 && Math.abs(colDiff) == 2)
                || (colDiff == 0 && Math.abs(rowDiff) == 2)
                || (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2
                    && Integer.signum(rowDiff) == Integer.signum(colDiff));
    }

    public boolean isGameOver() {
        return getPossibleMoves().isEmpty();
    }

    public List<String> getPossibleMoves() {
        List<String> moves = new ArrayList<>();

        for (Position from : board.getValidPositions()) {
            int[][] directions;

            if (board.getType() == BoardType.HEXAGON) {
                directions = new int[][] {
                    {-2, 0}, {2, 0}, {0, -2}, {0, 2},
                    {-2, -2}, {2, 2}
                };
            } else {
                directions = new int[][] {
                    {-2, 0}, {2, 0}, {0, -2}, {0, 2}
                };
            }

            for (int[] direction : directions) {
                int toRow = from.row() + direction[0];
                int toCol = from.col() + direction[1];

                if (isValidMove(from.row(), from.col(), toRow, toCol)) {
                    moves.add(from + " -> (" + toRow + "," + toCol + ")");
                }
            }
        }

        return moves;
    }
}
