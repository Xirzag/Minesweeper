package Minesweeper.application;

import Minesweeper.model.Cell;
import Minesweeper.model.Flag;
import Minesweeper.ui.CellDisplay;

import javax.swing.*;
import java.awt.*;

public class CellPanel extends JButton implements CellDisplay {

    private final Cell cell;
    private boolean gameFinish = false;

    public CellPanel(Cell cell) {
        this.cell = cell;
    }

    final static Color[] numberColor = {Color.blue,Color.green, Color.red,
            Color.MAGENTA, Color.CYAN, Color.ORANGE,Color.PINK, Color.YELLOW };

    public void showMines() {
        this.gameFinish = true;
    }

    @Override
    public void display() {
        boolean a = cell.isOpen();
        if (cell.isOpen()) {
            this.setBackground(Color.lightGray);
            openCellDisplay();
        } else {
            this.setBackground(Color.darkGray);
            closedCellDisplay();
        }
    }

    private void closedCellDisplay() {
        if(cell.getFlag() == Flag.MineFlag){
            this.setText("P");
            if(gameFinish && !cell.isMine())
                this.setForeground(Color.MAGENTA);
            else
                this.setForeground(Color.RED);
        }else if(cell.getFlag() == Flag.QuestionMarkFlag) {
            if(gameFinish && cell.isMine()) {
                this.setForeground(Color.RED);
                this.setText("*");
            }
            else{
                this.setText("?");
                this.setForeground(Color.BLUE);
            }
        }else{
            if(gameFinish && cell.isMine()) {
                this.setForeground(Color.RED);
                this.setText("*");
            }
            else this.setText("");
        }
    }

    private void openCellDisplay() {
        if(cell.isMine()) {
            this.setText("*");
        }else{
            int nearMines = cell.nearMines();
            if(nearMines!=0){
                this.setText(Integer.toString(nearMines));
                this.setForeground(numberColor[nearMines-1]);
            }else{
                this.setText("");
            }
        }
    }
}
