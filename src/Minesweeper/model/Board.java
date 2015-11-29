package Minesweeper.model;

public class Board {
    private Dimension dimension;
    private int mineAmount;

    public Board(Dimension dimension, int mineAmount) {
        this.dimension = dimension;
        this.mineAmount = mineAmount;
    }

    public Dimension dimension() {
        return dimension;
    }

    public int getMineAmount() {
        return mineAmount;
    }
}
