package Minesweeper.control;

import Minesweeper.application.GameConcreteMediator;
import Minesweeper.model.Board;
import Minesweeper.view.messaging.GameMediator;
import Minesweeper.view.ui.GameDisplay;
import Minesweeper.view.ui.RankingBoardsDisplay;

public class RunGameCommand implements Command {

    private final GameDisplay display;
    private final Board board;
    private final RankingBoardsDisplay rankingBoardsDisplay;

    public RunGameCommand(GameDisplay display, Board board, RankingBoardsDisplay rankingBoardsDisplay) {
        this.display = display;
        this.board = board;
        this.rankingBoardsDisplay = rankingBoardsDisplay;
    }

    public void execute(){
        GameMediator mediator= new GameConcreteMediator();
        mediator.registerRankingBoardDisplay(rankingBoardsDisplay);
        board.setMediator(mediator);
        board.prepareForGame();

        display.initGame(board, mediator);
        display.display();
    }

}
