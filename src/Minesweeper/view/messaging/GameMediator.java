package Minesweeper.view.messaging;

import Minesweeper.model.Board;
import Minesweeper.model.Cell;
import Minesweeper.model.GameTimer;
import Minesweeper.model.Ranking;
import Minesweeper.view.ui.BoardListDisplay;
import Minesweeper.view.ui.GameDisplay;
import Minesweeper.view.ui.RankingDisplay;

import java.time.Duration;

public interface GameMediator {

    void registerDisplay(GameDisplay display);
    void registerBoard(Board board);
    void registerTimer(GameTimer timer);
    void registerBoardListDisplay(BoardListDisplay boardListDisplay);
    void registerRankingDisplay(RankingDisplay rankingDisplay);

    void setTime(Duration time);
    void setRemainingMines(int mines);
    void openCell(Cell cell);

    void loseGame();
    void winGame();

    void showRankingFrameWith(Ranking ranking, Ranking.Result result);

    void startGame();

    Duration getTime();
}
