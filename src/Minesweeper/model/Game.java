package Minesweeper.model;

import Minesweeper.process.MinefieldFiller;
import Minesweeper.process.MinefieldUpdater;

public class Game {

    private GameState currentGameState;

    public Game(Board board) {
        currentGameState = new GameInit(board);
    }

    public void updateGame(Position position){
        if(currentGameState instanceof GameInit) {
            Minefield minefield = new Minefield(currentGameState.getBoard());
            MinefieldFiller.fill(minefield, position);
            currentGameState = MinefieldUpdater.update(new GameInProgress(minefield), position);
        }else if(currentGameState instanceof GameInProgress) {
            currentGameState = MinefieldUpdater.update((GameInProgress) currentGameState, position);
        }

    }

    public Board getBoard() {
        return currentGameState.getBoard();
    }
    public Minefield getMineField() {
        if(currentGameState instanceof GameInit) return null;
        if(currentGameState instanceof GameInProgress)
            return ((GameInProgress) currentGameState).getMinefield();
        return ((FinishedGame) currentGameState).getMinefield();
    }

    public boolean isFinished() {
        return currentGameState instanceof FinishedGame;
    }
}
