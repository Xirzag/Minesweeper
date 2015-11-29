package Minesweeper.model;

public class FinishedGame implements GameState {

    private Minefield minefield;
    private GameResult result;

    public FinishedGame(Minefield minefield, GameResult result) {
        this.minefield = minefield;
        this.result = result;
    }

    public Minefield getMinefield() {
        return minefield;
    }

    public GameResult getResult() {
        return result;
    }

    @Override
    public Board getBoard() {
        return minefield.getBoard();
    }
}
