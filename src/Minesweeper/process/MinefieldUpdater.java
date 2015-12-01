package Minesweeper.process;

import Minesweeper.model.*;

public class MinefieldUpdater {
    public static GameState update(GameInProgress gameState, Position position) {
        Cell updateCell = gameState.getMinefield().getCell(position);
        if(updateCell.getCoverState() instanceof UncoveredCell) return gameState;

        uncoverCell(updateCell);

        if(updateCell instanceof CellWithMine) {
            return new FinishedGame(gameState.getMinefield(), GameResult.lose);
        };
        updateCells(updateCell);

        return gameState;
    }

    private static void updateCells(Cell cell){
        if(cell instanceof CellWithMine) return;

        CellWithoutMine updateCell = (CellWithoutMine) cell;
        if(updateCell.nearMines() != 0) return;

        for( Cell adjacentCell :updateCell.getAdjacentCells()) {
            if(adjacentCell.getCoverState() instanceof CoveredCell) {
                uncoverCell(adjacentCell);
                updateCells(adjacentCell);
            }
        }
    }

    public static void uncoverCell(Cell cell){
        if(cell.getCoverState() instanceof CoveredCell)
            cell.setCoverState(new UncoveredCell());

    }


}
