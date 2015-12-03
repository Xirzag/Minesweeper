package Minesweeper.model;

public interface Cell {

    void open() throws MineExplosion, WinGame;
    boolean isOpen();
    boolean isMine();
    int nearMines();
    void setFlag(Flag flag);
    Flag getFlag();

}
