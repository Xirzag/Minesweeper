package Minesweeper.process;

import Minesweeper.model.*;

public class MinefieldUpdater {
    public static GameState update(GameInProgress gameState, Position position) {
        Cell updateCell = gameState.getMinefield().getCell(position);
        if(updateCell instanceof UncoveredCell) return gameState;

        uncoverCell(position, gameState.getMinefield());

        if(updateCell.getContent() instanceof CellWithMine) {
            return new FinishedGame(gameState.getMinefield(), GameResult.lose);
        };
        updateCells(gameState.getMinefield(), position);

        return gameState;
    }

    private static void updateCells(Minefield minefield, Position position){
        if(minefield.getCell(position).getContent() instanceof CellWithMine) return;
        CellWithoutMine updateCell = (CellWithoutMine) minefield.getCell(position).getContent();
        if(updateCell.nearMines() != 0) return;
        for(int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++) {
                Position adjacentPosition = new Position(position.x()+i,position.y()+j);
                if(!minefield.getBoard().dimension().isInside(adjacentPosition) ||
                        minefield.getCell(adjacentPosition) instanceof UncoveredCell) continue;
                uncoverCell(adjacentPosition, minefield);
                updateCells(minefield, adjacentPosition);
            }
        }
    }

    public static void uncoverCell(Position position, Minefield minefield){
        if(minefield.getCell(position) instanceof UncoveredCell) return;

        CellContent content = minefield.getCell(position).getContent();
        minefield.setCell(position, new UncoveredCell(content));
    }

}
