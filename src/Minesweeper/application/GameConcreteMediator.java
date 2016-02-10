package Minesweeper.application;

import Minesweeper.model.Board;
import Minesweeper.model.Cell;
import Minesweeper.model.GameTimer;
import Minesweeper.model.Ranking;
import Minesweeper.view.ui.GameDisplay;
import Minesweeper.view.messaging.GameMediator;
import Minesweeper.view.ui.BoardListDisplay;
import Minesweeper.view.ui.RankingDisplay;

import java.time.Duration;

public class GameConcreteMediator implements GameMediator
{

    private GameDisplay display;
    private GameTimer timer;
    private Board board;
    private BoardListDisplay boardListDisplay;
    private RankingDisplay rankingDisplay;


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
    public void registerBoardListDisplay(BoardListDisplay boardListDisplay) {
        this.boardListDisplay = boardListDisplay;
    }

    @Override
    public void registerRankingDisplay(RankingDisplay rankingDisplay) {
        this.rankingDisplay = rankingDisplay;
    }

    @Override
    public void setTime(Duration time) {
        display.setTimerLabel(time);
    }

    @Override
    public void setRemainingMines(int mines) {
        display.setRemainingMines(mines);
    }

    @Override
    public void openCell(Cell cell) {
        display.openCell(cell);
    }

    @Override
    public void loseGame() {
        finishGame();
        display.loseGame();
    }

    @Override
    public void winGame() {
        finishGame();
        display.winGame();
    }

    @Override
    public void showRankingFrameWith(Ranking ranking, Ranking.Result result){
        rankingDisplay.display();
        rankingDisplay.showResult(ranking, result);
    }

    @Override
    public void startGame(){
        timer.start();
    }

    @Override
    public Duration getTime() {
        return timer.getTime();
    }

    protected void finishGame(){
        stopTimer();
        updateRankings();
    }

    protected void updateRankings() {
        boardListDisplay.refreshBoardsDisplay();
    }

    protected void stopTimer() {
        timer.stop();
    }
}
