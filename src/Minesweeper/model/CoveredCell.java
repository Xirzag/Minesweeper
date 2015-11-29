package Minesweeper.model;

public class CoveredCell implements Cell {
    private Flag flag = Flag.None;

    private CellContent content;

    public CoveredCell(CellContent content) {
        this.content = content;
    }

    public CellContent getContent() {
        return content;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }
}
