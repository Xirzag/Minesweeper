package Minesweeper.model;

public class CellWithMine implements Cell {

    private CellCoverState coverState;

    public CellWithMine(CellCoverState coverState) {
        this.coverState = coverState;
    }

    @Override
    public CellCoverState getCoverState() {
        return coverState;
    }

    public void setCoverState(CellCoverState coverState) {
        this.coverState = coverState;
    }
}
