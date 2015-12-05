package Minesweeper.application;

import Minesweeper.model.Board;
import Minesweeper.ui.GameDisplay;
import Minesweeper.view.GameMediator;

import java.util.Timer;

public class GameConcreteMediator implements GameMediator
{

    GameDisplay display;
    Timer timer;
    Board board;


    @Override
    public void registerDisplay(GameDisplay display) {
        this.display = display;
    }

    @Override
    public void registerBoard(Board board) {
        this.board = board;
    }

    @Override
    public void registerTimer(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void setTime(String time) {
        display.setTimer(time);
    }

    @Override
    public void setRemainingMines(int mines) {
        display.setRemainingMines(mines);
    }
}
