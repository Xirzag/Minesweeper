package Minesweeper.control;

import Minesweeper.application.GameConcreteMediator;
import Minesweeper.model.Board;
import Minesweeper.view.ui.GameDisplay;
import Minesweeper.view.GameMediator;

public class RunGameCommand implements Command {

    private final GameDisplay display;
    private final Board board;

    public RunGameCommand(GameDisplay display, Board board) {
        this.display = display;
        this.board = board;
    }

    public void execute(){
        GameMediator mediator = new GameConcreteMediator();
        display.setMediator(mediator);
        board.setMediator(mediator);
        board.prepareForGame();

        display.setBoard(board);
        display.display();
    }

}
