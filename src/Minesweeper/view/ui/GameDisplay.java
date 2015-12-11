package Minesweeper.view.ui;

import Minesweeper.model.Board;
import Minesweeper.view.GameMediator;

import java.time.Duration;

public interface GameDisplay {

    void display();
    public void setRemainingMines(int mines);
    public void setTimer(Duration time);
    void setBoard(Board board);
    void setMediator(GameMediator mediator);

}
