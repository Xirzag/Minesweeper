package Minesweeper.application;

import Minesweeper.model.*;
import Minesweeper.ui.CellDisplay;
import Minesweeper.ui.GameDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;

public class SwingGameDisplay extends JPanel implements GameDisplay {

    private final JFrame frame;
    private Container previousPane;
    private Board board;
    private ArrayList<CellPanel> boardCells = new ArrayList<>();
    private int cellSize = 50;

    public SwingGameDisplay(JFrame frame, Board board) {
        this.frame = frame;
        this.board = board;
    }

    private enum GameResult{
        win, lose;
    }

    @Override
    public void display() {
        createBoard();
    }

    private Position indexToPosition(int index){
        return new Position(index/board.dim().rows, index % board.dim().rows);
    }

    private void createBoard() {
        savePreviousData();

        this.setLayout(new GridLayout(board.dim().rows, board.dim().cols));
        frame.setContentPane(this);
        frame.setSize(new Dimension(this.board.dim().cols*cellSize, this.board.dim().rows*cellSize));

        for (int i = 0; i < this.board.dim().getArea(); i++)
            addCellIndex(i);

        frame.setContentPane(this);
        frame.validate();
        frame.repaint();
    }

    private void addCellIndex(int i) {
        Cell cell = board.getCell(indexToPosition(i));
        CellPanel cellPanel = new CellPanel(cell);
        boardCells.add(i, cellPanel);
        this.add(cellPanel);
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

    public void refreshDisplay()  {
        for (CellDisplay cellPanel : boardCells)
            cellPanel.display();
    }

    private void savePreviousData(){
        previousPane = frame.getContentPane();
    }

    private void restorePreviousData(){
        frame.setContentPane(previousPane);
    }
}
