package Minesweeper.control;

import Minesweeper.model.Board;
import Minesweeper.ui.GameDisplay;

public class RunGameCommand implements Command {

    private final GameDisplay display;

    public RunGameCommand(GameDisplay display) {
        this.display = display;
    }

    public void execute(){
        display.display();
    }

}
