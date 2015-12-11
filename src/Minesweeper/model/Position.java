package Minesweeper.model;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Position)? this.x == ((Position) obj).x && this.y == ((Position) obj).y: false;
    }

    @Override
    public int hashCode(){
        return x * y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isInside(Dimension dimension){
        return x >= 0 && x < dimension.cols() && y >= 0 && y < dimension.rows();
    }
}
