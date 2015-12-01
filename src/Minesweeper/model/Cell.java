package Minesweeper.model;

public interface Cell {

    public CellCoverState getCoverState();
    public void setCoverState(CellCoverState coverState);
}
