package Minesweeper.control;

import Minesweeper.ui.BoardDialog;

public class CreateBoardCommand implements Command {
        private final BoardDialog dialog;
    
    public CreateBoardCommand(BoardDialog dialog) {
        this.dialog = dialog;
    }

    public void execute(){
        dialog.display();
    }
}
