package Minesweeper.model;

public class GameInProgress implements GameState {

    private Minefield minefield;

    public GameInProgress(Minefield minefield) {
        this.minefield = minefield;
    }

    public Minefield getMinefield() {
        return minefield;
    }

    public void setMinefield(Minefield minefield) {
        this.minefield = minefield;
    }


    @Override
    public Board getBoard() {
        return minefield.getBoard();
    }
}
