package Minesweeper.ui;

import Minesweeper.model.Board;

public interface GameDisplay {

    void display();
    public void setRemainingMines(int mines);
    public void setTimer(String time);

}
