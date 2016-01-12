package Minesweeper.application;

import Minesweeper.model.Board;
import Minesweeper.model.GameTimer;
import Minesweeper.view.ui.GameDisplay;
import Minesweeper.view.messaging.GameMediator;
import Minesweeper.view.ui.RankingBoardsDisplay;

import java.time.Duration;

public class GameConcreteMediator implements GameMediator
{

    GameDisplay display;
    GameTimer timer;
    Board board;
    private RankingBoardsDisplay rankingBoardsDisplay;


    @Override
    public void registerDisplay(GameDisplay display) {
        this.display = display;
    }

    @Override
    public void registerBoard(Board board) {
        this.board = board;
    }

    @Override
    public void registerTimer(GameTimer timer) {
        this.timer = timer;
    }

    @Override
    public void registerRankingBoardDisplay(RankingBoardsDisplay rankingBoardsDisplay) {
        this.rankingBoardsDisplay = rankingBoardsDisplay;
    }

    @Override
    public void setTime(Duration time) {
        display.setTimer(time);
    }

    @Override
    public void setRemainingMines(int mines) {
        display.setRemainingMines(mines);
    }

    @Override
    public void updateRankings() {
        rankingBoardsDisplay.refreshBoardsDisplay();
    }

    @Override
    public void stopTimer() {
        timer.stop();
    }
}
