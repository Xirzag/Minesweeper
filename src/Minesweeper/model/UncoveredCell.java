package Minesweeper.model;

public class UncoveredCell implements Cell {

    private CellContent content;

    public UncoveredCell(CellContent content) {
        this.content = content;
    }

    public CellContent getContent() {
        return content;
    }
}
