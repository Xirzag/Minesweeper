package Minesweeper.view.ui;

import Minesweeper.model.Board;
import Minesweeper.model.Cell;

import java.time.Duration;

public interface GameDisplay {

    void display();
    public void setRemainingMines(int mines);
    public void setTimerLabel(Duration time);

    void initGame(Board board);

    void loseGame();

    void winGame();

    void openCell(Cell cell);

}
