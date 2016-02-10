package Minesweeper.application.Swing;

import Minesweeper.application.FileRanking.RankingFileReader;
import Minesweeper.model.Board;
import Minesweeper.model.Ranking;
import Minesweeper.model.RankingLoaderException;
import Minesweeper.view.messaging.GameMediator;
import Minesweeper.view.persistence.RankingReader;
import Minesweeper.view.ui.RankingDisplay;

import javax.swing.*;
import java.io.File;

public class SwingRankingDisplay extends JFrame implements RankingDisplay {

    private final GameMediator mediator;
    private final JTabbedPane tabbedPane = new JTabbedPane();

    public SwingRankingDisplay(GameMediator mediator) {
        this.mediator = mediator;
        mediator.registerRankingDisplay(this);
    }

    @Override
    public void display() {
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        this.add(rankingTabs());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JTabbedPane rankingTabs() {
        try {
            RankingReader reader = new RankingFileReader(new File("rankings.data"));
            createTabsWithTheBoardsIn(reader);
        } catch (RankingLoaderException e) {
            showErrorMessage("No se ha podido abrir los rankings");
            this.dispose();
        }
        return tabbedPane;
    }

    private void createTabsWithTheBoardsIn(RankingReader reader) throws RankingLoaderException {
        for(Board board : reader.getBoards()) {
            JPanel resultDisplay = new SwingResultsDisplay(reader, board);
            tabbedPane.addTab(board.toString(), null, resultDisplay);
        }
    }

    @Override
    public void showResult(Ranking ranking, Ranking.Result result){
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String tabTitle = tabbedPane.getTitleAt(i);
            if(tabTitle.equals(ranking.getBoard().toString())){
                tabbedPane.setSelectedIndex(i);
                SwingResultsDisplay resultDisplay = (SwingResultsDisplay) tabbedPane.getComponentAt(i);
                resultDisplay.select(result);
            }

        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

}
