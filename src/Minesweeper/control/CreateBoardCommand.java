package Minesweeper.control;

import Minesweeper.model.Board;
import Minesweeper.model.Game;
import Minesweeper.ui.BoardDialog;

public class CreateBoardCommand implements Command {
        private final BoardDialog dialog;
    
    public CreateBoardCommand(BoardDialog dialog) {
        this.dialog = dialog;
    }

    public void execute(){
        dialog.show();
    }
}
