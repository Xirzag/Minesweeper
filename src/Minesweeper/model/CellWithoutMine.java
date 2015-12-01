package Minesweeper.model;

import java.util.ArrayList;
import java.util.List;

public class CellWithoutMine implements Cell {
    private List<Cell> adjacentCells;
    private CellCoverState coverState;

    public List<Cell> getAdjacentCells() {
        return adjacentCells;
    }

    public CellWithoutMine(CellCoverState coverState) {
        adjacentCells = new ArrayList<>();
        this.coverState = coverState;
    }

    public void addAdjacentCells(Cell cell){
        adjacentCells.add(cell);
    };

    public int nearMines(){
        return (int) adjacentCells.stream().filter(c -> c instanceof CellWithMine).count();
    }

    @Override
    public CellCoverState getCoverState() {
        return coverState;
    }

    public void setCoverState(CellCoverState coverState) {
        this.coverState = coverState;
    }
}
