package Minesweeper.model;

public class CoveredCell implements CellCoverState {
    private Flag flag = Flag.None;

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }
}
