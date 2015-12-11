package Minesweeper.view;

import Minesweeper.model.Board;
import Minesweeper.model.GameTimer;
import Minesweeper.view.ui.GameDisplay;

import java.time.Duration;

public interface GameMediator {

    void registerDisplay(GameDisplay display);
    void registerBoard(Board board);
    void registerTimer(GameTimer timer);

    void setTime(Duration time);
    void setRemainingMines(int mines);
    void stopTimer();

}
