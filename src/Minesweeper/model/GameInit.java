package Minesweeper.model;

public class GameInit implements GameState {

    private Board board;

    public GameInit(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
