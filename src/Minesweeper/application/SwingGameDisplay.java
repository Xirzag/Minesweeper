package Minesweeper.application;

import Minesweeper.model.*;
import Minesweeper.ui.CellDisplay;
import Minesweeper.ui.GameDisplay;
import Minesweeper.view.GameMediator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;

public class SwingGameDisplay extends JPanel implements GameDisplay {

    private final JFrame frame;
    private Container previousPane;
    private Board board;
    private GameMediator mediator;
    private JLabel timer;
    private JLabel remainingMines;
    private ArrayList<CellPanel> boardCells = new ArrayList<>();
    private int cellSize = 50;

    public SwingGameDisplay(JFrame frame, Board board, GameMediator mediator) {
        this.frame = frame;
        this.board = board;
        this.mediator = mediator;
        mediator.registerDisplay(this);
    }

    private enum GameResult{
        win, lose;
    }

    @Override
    public void display() {
        createPanels();
    }

    private Position indexToPosition(int index){
        return new Position(index/board.dim().rows, index % board.dim().rows);
    }

    private int positionToIndex(Position position){
        return position.y() * board.dim().cols + position.x();
    }


    private void createPanels(){
        savePreviousData();
        this.setLayout(new BorderLayout());
        this.add(createBoard(), BorderLayout.CENTER);
        this.add(createStatsPanel(), BorderLayout.NORTH);
        frame.setContentPane(this);
        frame.setSize(new Dimension(this.board.dim().cols*cellSize, this.board.dim().rows*cellSize+cellSize));
        frame.setContentPane(this);
        frame.validate();
        frame.repaint();
    }

    private JPanel createStatsPanel() {
        JPanel statsBar = new JPanel(new BorderLayout());
        remainingMines = new JLabel(Integer.toString(board.getMineAmount()));
        timer = new JLabel("0");
        statsBar.add(remainingMines, BorderLayout.WEST);
        statsBar.add(timer, BorderLayout.EAST);
        statsBar.setBorder(new EmptyBorder(10,20,10,20));
        return statsBar;
    }

    private JPanel createBoard() {
        JPanel boardPanel = new JPanel(new GridLayout(board.dim().rows, board.dim().cols));
        for (int j = 0; j < this.board.dim().rows; j++)
            for (int i = 0; i < this.board.dim().cols; i++)
                addCellIndex(new Position(i,j), boardPanel);
        return boardPanel;
    }

    private void addCellIndex(Position position, JPanel boardPanel) {
        Cell cell = board.getCell(position);
        CellPanel cellPanel = new CellPanel(cell);
        boardCells.add(positionToIndex(position), cellPanel);
        boardPanel.add(cellPanel, positionToIndex(position));
        cellPanel.display();
        cellPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try{
                        cell.open();
                    }catch (WinGame winGame){
                        finishGame(GameResult.win);
                    }catch (MineExplosion explosion){
                        finishGame(GameResult.lose);
                    }
                    refreshDisplay();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    Flag cellFlag = cell.getFlag();
                    if (cellFlag == Flag.None)
                        cell.setFlag(Flag.MineFlag);
                    else if (cellFlag == Flag.MineFlag)
                        cell.setFlag(Flag.QuestionMarkFlag);
                    else if (cellFlag == Flag.QuestionMarkFlag)
                        cell.setFlag(Flag.None);
                    refreshDisplay();
                }
            };
        });
    }

    private void finishGame(GameResult result) {
        if(result == GameResult.win)System.out.println("Has ganado");
        else System.out.println("Has perdido");
        for (CellPanel cellPanel : boardCells)
            cellPanel.showMines();
    }

    protected void refreshDisplay()  {
        for (CellDisplay cellPanel : boardCells)
            cellPanel.display();
    }

    public void setRemainingMines(int mines) {
        remainingMines.setText(Integer.toString(mines));
    }

    public void setTimer(String time){
        timer.setText(time);
    }

    private void savePreviousData(){
        previousPane = frame.getContentPane();
    }

    private void restorePreviousData(){
        frame.setContentPane(previousPane);
    }
}
