package Minesweeper.model;

public class Dimension {
    public int rows;
    public int cols;

    public Dimension(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getArea(){
        return rows * cols;
    }

    public boolean isInside(Position position){
        return position.x() >= 0 && position.x() < cols && position.y() >= 0 && position.y() < rows;
    }
}
