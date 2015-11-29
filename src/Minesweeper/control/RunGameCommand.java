package Minesweeper.control;

import Minesweeper.model.Board;
import Minesweeper.model.Game;
import Minesweeper.ui.GameDisplay;

public class RunGameCommand implements Command {

    private final GameDisplay display;
    private final Board board;

    public RunGameCommand(GameDisplay display, Board board) {
        this.board = board;
        this.display = display;
    }

    public void execute(){
        Game game = new Game(board);
        display.show(game);
    }

}
