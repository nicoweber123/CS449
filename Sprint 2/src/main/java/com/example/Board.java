package com.example;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private final BoardType type;
    private final boolean[][] valid;
    private final boolean[][] occupied;

    public Board(int size, BoardType type) {
        if (size < 3 || size % 2 == 0) {
            throw new IllegalArgumentException("Board size must be an odd number of at least 3.");
        }

        this.size = size;
        this.type = type;
        this.valid = new boolean[size][size];
        this.occupied = new boolean[size][size];

        createShape();
        reset();
    }

    private void createShape() {
        int center = size / 2;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (type == BoardType.ENGLISH) {
                    int cut = Math.max(1, size / 3);
                    boolean inCorner = (row < cut || row >= size - cut)
                            && (col < cut || col >= size - cut);
                    valid[row][col] = !inCorner;
                } else if (type == BoardType.DIAMOND) {
                    valid[row][col] = Math.abs(row - center) + Math.abs(col - center) <= center;
                } else {
                    valid[row][col] = Math.abs(row - center) <= center
                            && Math.abs(col - center) <= center
                              && Math.abs((row - center) + (col - center)) <= center;
                }
            }
        }
    }

    public void reset() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                occupied[row][col] = valid[row][col];
            }
        }
        occupied[size / 2][size / 2] = false;
    }

    public int getSize() {
        return size;
    }

    public BoardType getType() {
        return type;
    }

    public boolean isValid(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size && valid[row][col];
    }

    public boolean isOccupied(int row, int col) {
        return isValid(row, col) && occupied[row][col];
    }

    public void setOccupied(int row, int col, boolean value) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid board position.");
        }
        occupied[row][col] = value;
    }

    public int getPegCount() {
        int count = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (occupied[row][col]) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<Position> getValidPositions() {
        List<Position> positions = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (valid[row][col]) {
                    positions.add(new Position(row, col));
                }
            }
        }
        return positions;
    }
}
