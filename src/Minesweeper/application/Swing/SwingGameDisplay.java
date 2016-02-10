package Minesweeper.application.Swing;

import Minesweeper.application.FileRanking.RankingFileReader;
import Minesweeper.application.FileRanking.RankingFileWriter;
import Minesweeper.control.RunGameCommand;
import Minesweeper.model.*;
import Minesweeper.view.messaging.GameMediator;
import Minesweeper.view.ui.CellDisplay;
import Minesweeper.view.ui.GameDisplay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class SwingGameDisplay extends JPanel implements GameDisplay {

    private Board board;
    private GameMediator mediator;
    private JLabel timer;
    private JLabel remainingMines;

    private ArrayList<CellPanel> boardCells = new ArrayList<>();

    public SwingGameDisplay(GameMediator mediator) {
        this.mediator = mediator;
        mediator.registerDisplay(this);
    }

    public void initGame(Board board) {
        this.board = board;
    };

    @Override
    public void display() {
        createPanels();
    }

    private void createPanels(){
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(createBoard(), BorderLayout.CENTER);
        this.add(createStatsPanel(), BorderLayout.NORTH);
        this.validate();
        this.repaint();
    }

    private JPanel createStatsPanel() {
        JPanel statsBar = new JPanel(new GridLayout(1,3));
        addStatBarItemsTo(statsBar);
        setStatBarConfiguration(statsBar);

        return statsBar;
    }

    private void addStatBarItemsTo(JPanel statsBar) {
        addRemainingMinesLabelTo(statsBar);
        addNewGameButtonTo(statsBar);
        addTimerTo(statsBar);
    }

    private void setStatBarConfiguration(JPanel statsBar) {
        statsBar.setBorder(new EmptyBorder(10,20,10,20));
        statsBar.setPreferredSize(new Dimension(this.getWidth(),40));
    }

    private void addNewGameButtonTo(JPanel statsBar) {
        Button newGameButton = new Button("New game");
        SwingGameDisplay thisDisplay = this;
        newGameButton.addActionListener(e -> new RunGameCommand(thisDisplay, thisDisplay.board, this.mediator).execute());
        statsBar.add(newGameButton);
    }

    private void addRemainingMinesLabelTo(JPanel statsBar) {
        remainingMines = new JLabel(Integer.toString(board.getNumberOfMines()));
        statsBar.add(remainingMines);
    }

    private void addTimerTo(JPanel statsBar) {
        timer = new JLabel("0:00", SwingConstants.RIGHT);
        statsBar.add(timer);
    }

    private JPanel createBoard() {
        JPanel boardPanel = new JPanel(new GridLayout(board.dim().rows(), board.dim().cols()));
        for (int j = 0; j < this.board.dim().rows(); j++)
            for (int i = 0; i < this.board.dim().cols(); i++)
                addCellFromPositionTo(new Position(i, j), boardPanel);
        return boardPanel;
    }

    private void addCellFromPositionTo(Position position, JPanel boardPanel) {
        Cell cell = board.getCell(position);
        CellPanel cellPanel = new CellPanel(cell);

        saveCellPanelIn(position, boardPanel, cellPanel);
        configureCell(cell, cellPanel);
    }

    private void saveCellPanelIn(Position position, JPanel boardPanel, CellPanel cellPanel) {
        boardCells.add(positionToIndex(position), cellPanel);
        boardPanel.add(cellPanel, positionToIndex(position));
    }

    private void configureCell(Cell cell, CellPanel cellPanel) {
        cellPanel.display();
        cellPanel.addMouseListener(cellEvent(cell));
    }

    private MouseAdapter cellEvent(final Cell cell) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    onRightClick();
                else if (e.getButton() == MouseEvent.BUTTON3)
                    onLeftClick();
            }

            private void onLeftClick() {
                Flag cellFlag = cell.getFlag();
                if (cellFlag == Flag.None)
                    cell.setFlag(Flag.MineFlag);
                else if (cellFlag == Flag.MineFlag)
                    cell.setFlag(Flag.QuestionMarkFlag);
                else if (cellFlag == Flag.QuestionMarkFlag)
                    cell.setFlag(Flag.None);

                refreshTheDisplayOf(cell);
            }

            private void onRightClick() {
                cell.open();
            }

        };
    }

    private void refreshTheDisplayOf(Cell cell) {
        getCellPanelOf(cell).display();
    }

    @Override
    public void loseGame(){
        finishGame();
        warnThatTheUserLose();
    }

    @Override
    public void winGame(){
        finishGame();
        congratulateUser();
        updateRankings();
    }

    private void finishGame() {
        disableCellListeners();
        refreshDisplay();
    }

    private void warnThatTheUserLose() {
        JOptionPane.showMessageDialog(this, "Game Over");
    }

    private void updateRankings() {
        File file = new File("rankings.data");
        Ranking loadedRankings = loadRankings(file);
        Ranking.Result result = new Ranking.Result(new Date(), mediator.getTime());
        loadedRankings.addResult(result);
        writeRankingTo(file, loadedRankings);
        mediator.showRankingFrameWith(loadedRankings, result);
    }

    private void writeRankingTo(File file, Ranking loadedRankings) {
        try {
            RankingFileWriter writer = new RankingFileWriter(file);
            writer.save(loadedRankings);
        } catch (RankingLoaderException e) {
            e.printStackTrace();
        }
    }

    private Ranking loadRankings(File file) {
        try {
            return getRankingFrom(file);
        }catch (RankingLoaderException e){
            return new Ranking(this.board);
        }
    }

    private Ranking getRankingFrom(File file) throws RankingLoaderException {
        RankingFileReader reader = new RankingFileReader(file);
        return reader.load(this.board);
    }

    private void congratulateUser() {
        JOptionPane.showMessageDialog(this,
                "You resolved minesweeper in " + durationToString(mediator.getTime()) + " seconds!");
    }

    private void disableCellListeners() {
        for (CellPanel cellPanel : boardCells) {
            cellPanel.showMines();
            removeMouseListenersFrom(cellPanel);

        }
    }

    private void removeMouseListenersFrom(CellPanel cellPanel) {
        for( MouseListener listener : cellPanel.getMouseListeners() )
            cellPanel.removeMouseListener( listener );
    }

    @Override
    public void openCell(Cell cell) {
        refreshTheDisplayOf(cell);
    }

    private CellPanel getCellPanelOf(Cell cell) {
        return boardCells.get(positionToIndex(cell.getPosition()));
    }

    protected void refreshDisplay()  {
        for (CellDisplay cellPanel : boardCells)
            cellPanel.display();
    }

    public void setRemainingMines(int mines) {
        remainingMines.setText(Integer.toString(mines));
    }

    public void setTimerLabel(Duration time){
        timer.setText(durationToString(time));
    }


    private String durationToString(Duration duration) {
        int seconds = (int) (duration.getSeconds() % 60);
        return duration.toMinutes()+":"+((seconds<10)? "0" + seconds : seconds);
    }

    private Position indexToPosition(int index){
        return new Position(index/board.dim().rows(), index % board.dim().rows());
    }

    private int positionToIndex(Position position){
        int index = position.y() * board.dim().cols() + position.x();
        return index;
    }
}
