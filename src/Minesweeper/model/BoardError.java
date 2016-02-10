package Minesweeper.model;

public class BoardError extends Throwable {
    private final String errorMessage;

    public BoardError(String errorMessage) {

        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
