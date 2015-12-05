package Minesweeper.view;

import Minesweeper.model.Board;
import Minesweeper.ui.GameDisplay;

import java.util.Timer;

public interface GameMediator {

    void registerDisplay(GameDisplay display);
    void registerBoard(Board board);
    void registerTimer(Timer timer);

    void setTime(String time);
    void setRemainingMines(int mines);

}
