package Minesweeper.application.Swing;

import Minesweeper.model.Cell;
import Minesweeper.model.Flag;
import Minesweeper.view.ui.CellDisplay;

import javax.swing.*;
import java.awt.*;

public class CellPanel extends JButton implements CellDisplay {

    private final Cell cell;
    private boolean gameFinish = false;

    public CellPanel(Cell cell) {
        int fontSize = this.getFontMetrics(this.getFont()).getHeight();
        this.setPreferredSize(new Dimension(fontSize+20, fontSize+20));
        this.setMargin(new Insets(2,2,2,2));
        this.cell = cell;
    }

    final static Color[] numberColor = {Color.blue,Color.green, Color.red,
            Color.MAGENTA, Color.CYAN, Color.ORANGE,Color.PINK, Color.YELLOW };

    public void showMines() {
        this.gameFinish = true;
    }

    @Override
    public void display() {
        if (cell.isOpen()) {
            this.setBackground(Color.lightGray);
            openCellDisplay();
        } else {
            this.setBackground(Color.darkGray);
            closedCellDisplay();
        }
    }

    private void closedCellDisplay() {
        if(cell.getFlag() == Flag.MineFlag)
            drawFlag();

        else if(cell.getFlag() == Flag.QuestionMarkFlag)
            drawQuestionMark();

        else
            drawMine();
    }

    private void drawMine() {
        if(gameFinish && cell.isMine()) {
            this.setForeground(Color.RED);
            this.setText("*");
        }
        else this.setText("");
    }

    private void drawQuestionMark() {
        if(gameFinish && cell.isMine()) {
            this.setForeground(Color.RED);
            this.setText("*");
        }
        else{
            this.setText("?");
            this.setForeground(Color.BLUE);
        }
    }

    private void drawFlag() {
        this.setText("P");
        if(gameFinish && !cell.isMine())
            this.setForeground(Color.MAGENTA);
        else
            this.setForeground(Color.RED);
    }

    private void openCellDisplay() {
        if(cell.isMine()) {
            drawMine();
        }else{
            drawNumber();
        }
    }

    private void drawNumber() {
        int nearMines = cell.nearMines();
        if(nearMines!=0){
            this.setText(Integer.toString(nearMines));
            this.setForeground(numberColor[nearMines-1]);
        }else{
            this.setText("");
        }
    }
}
