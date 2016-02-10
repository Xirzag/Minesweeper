package Minesweeper.model;

public interface Cell {

    void open();
    boolean isOpen();
    boolean isMine();
    int nearMines();
    void setFlag(Flag flag);
    Flag getFlag();
    Position getPosition();

}
