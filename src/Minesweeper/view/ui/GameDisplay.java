package Minesweeper.view.ui;

import Minesweeper.model.Board;
import Minesweeper.view.messaging.GameMediator;

import java.time.Duration;

public interface GameDisplay {

    void display();
    public void setRemainingMines(int mines);
    public void setTimer(Duration time);

    void setRankingBoardsDisplay(RankingBoardsDisplay rankingBoardsDisplay);

    void initGame(Board board, GameMediator mediator);

}
