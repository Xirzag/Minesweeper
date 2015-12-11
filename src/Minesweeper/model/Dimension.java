package Minesweeper.model;

public class Dimension {
    private final int rows;
    private final int cols;

    public Dimension(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getArea(){
        return rows * cols;
    }

    public int rows() {
        return rows;
    }

    public int cols(){
        return cols;
    }
}
