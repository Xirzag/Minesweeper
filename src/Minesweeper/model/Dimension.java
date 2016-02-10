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

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Dimension)? this.cols == ((Dimension) obj).cols && this.rows == ((Dimension) obj).rows: false;
    }

    @Override
    public String toString() {
        return rows + "x" + cols;
    }
}
