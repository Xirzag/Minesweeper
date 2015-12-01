package Minesweeper.model;

import java.util.ArrayList;

public class Minefield {
    private Board board;
    private ArrayList<Cell> field;

    public Minefield(Board board) {
        this.board = board;
        this.field = new ArrayList<>();
        this.field.ensureCapacity(board.dimension().cols * board.dimension().rows);
    }

    private int positionToIndex(Position position){
        return board.dimension().cols * position.y() + position.x();
    }

    private Position indexToPosition(int index){
        return new Position(index/board.dimension().rows,index%board.dimension().rows);
    }

    public Cell getCell(Position position) {
        return field.get(positionToIndex(position));
    }

    public void setFlag(Position cellPosition, Flag flag) {
        Cell cell = getCell(cellPosition);
        if( cell.getCoverState() instanceof CoveredCell)
            ((CoveredCell) cell.getCoverState()).setFlag(flag);
    }

    public Flag getFlag(Position cellPosition) {
        Cell cell = getCell(cellPosition);
        if( cell.getCoverState() instanceof CoveredCell)
            return ((CoveredCell) cell.getCoverState()).getFlag();
        return Flag.None;
    }

    public void setCell(Position position, Cell cell){
        field.set(positionToIndex(position),cell);
    }

    public void addCell(Cell cell){
        field.add(cell);
    }

    public Board getBoard() {
        return board;
    }
}
