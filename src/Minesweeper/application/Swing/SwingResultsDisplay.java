package Minesweeper.application.Swing;

import Minesweeper.model.Board;
import Minesweeper.model.Ranking;
import Minesweeper.model.RankingLoaderException;
import Minesweeper.view.persistence.RankingReader;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.Duration;
import java.util.List;

public class SwingResultsDisplay extends JPanel {
    private final RankingReader reader;
    private final Board board;
    private JTable table;

    public SwingResultsDisplay(RankingReader reader, Board board) {
        super(false);
        this.reader = reader;
        this.board = board;
        this.setPreferredSize(new Dimension(410, 150));
        display();
    }

    public void display(){
        try {
            createResultDisplay();
        } catch (RankingLoaderException e) {
            e.printStackTrace();
        }
    }

    private void createResultDisplay() throws RankingLoaderException {
        final List<Ranking.Result> results = loadResults();
        JTable table = createTableFor(results);
        addScrollTo(table);
    }

    private List<Ranking.Result> loadResults() throws RankingLoaderException {
        Ranking ranking = reader.load(board);
        return ranking.getRanking();
    }

    private JTable createTableFor(final List<Ranking.Result> results) {
        table = new JTable(new AbstractTableModel() {

            String[] columnNames = {
                    "º",
                    "Points",
                    "Date",
            };

            @Override
            public int getRowCount() {
                return results.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return new Integer(rowIndex + 1).toString();
                    case 1:
                        return durationToString(results.get(rowIndex).getSolveTime());
                    case 2:
                        return results.get(rowIndex).getDate();
                    default:
                        return "";
                }
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }
        });
        configureTable(table);
        return table;
    }

    private void addScrollTo(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(this.getPreferredSize());
        add(scrollPane);
    }

    private void configureTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(this.getPreferredSize());
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setMaxWidth(80);
    }


    private String durationToString(Duration duration) {
        int seconds = (int) (duration.getSeconds() % 60);
        return duration.toMinutes()+":"+((seconds<10)? "0" + seconds : seconds);
    }

    public void select(Ranking.Result result) {
        for (int i = 0; i < table.getRowCount(); i++) {
            if(rowContains(i, result))
                selectRowNumber(i);
        }

    }

    private void selectRowNumber(int i) {
        table.setRowSelectionInterval(i, i);
    }

    private boolean rowContains(int row, Ranking.Result result) {
        return solveTimeMatch(row, result) && dateMatch(row, result);
    }

    private boolean solveTimeMatch(int row, Ranking.Result result) {
        String resultSolveTime = durationToString(result.getSolveTime());
        String rowSolveTime = (String) table.getValueAt(row, 1);
        return resultSolveTime.equals(rowSolveTime);
    }

    private boolean dateMatch(int row, Ranking.Result result) {
        String rowDate = table.getValueAt(row, 2).toString();
        String resultDate = result.getDate().toString();
        return resultDate.equals(rowDate);
    }
}
