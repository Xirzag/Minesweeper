package Minesweeper.model;

import java.util.ArrayList;
import java.util.List;

public class CellWithoutMine implements CellContent {
    private List<CellContent> adjacentCells;

    public List<CellContent> getAdjacentCells() {
        return adjacentCells;
    }

    public CellWithoutMine() {
        adjacentCells = new ArrayList<>();
    }

    public void addAdjacentCells(CellContent cell){
        adjacentCells.add(cell);
    };

    public int nearMines(){
        return (int) adjacentCells.stream().filter(c -> c instanceof CellWithMine).count();
    }
}
