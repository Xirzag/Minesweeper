package Minesweeper.control;

import Minesweeper.model.Board;
import Minesweeper.model.GameTimer;
import Minesweeper.view.messaging.GameMediator;
import Minesweeper.view.ui.GameDisplay;

public class RunGameCommand implements Command {

    private final GameDisplay display;
    private final Board board;
    private final GameMediator mediator;

    public RunGameCommand(GameDisplay display, Board board, GameMediator mediator) {
        this.display = display;
        this.board = board;
        this.mediator = mediator;
    }

    public void execute(){
        configureTimer();
        configureBoard();
        configureDisplay();
    }

    private void configureTimer() {
        GameTimer timer = new GameTimer();
        timer.setMediator(mediator);
    }

    private void configureBoard() {
        board.setMediator(mediator);
        board.prepareForGame();
    }

    private void configureDisplay() {
        display.initGame(board);
        display.display();
    }

}
