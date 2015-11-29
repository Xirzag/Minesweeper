package Minesweeper.application;

import Minesweeper.model.*;
import Minesweeper.ui.GameDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingGameDisplay implements GameDisplay {

    private final JFrame frame;
    private Container previousPane;
    private JPanel gameContainer;
    private Game game;
    private JButton[][] board;

    public SwingGameDisplay(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void show(Game game) {
        this.game = game;
        createBoard();
    }

    public void createBoard(){
        savePreviousData();

        gameContainer = new JPanel(new GridLayout(
                game.getBoard().dimension().rows, game.getBoard().dimension().cols));
        frame.setContentPane(gameContainer);
        board = new JButton[game.getBoard().dimension().cols][game.getBoard().dimension().rows];

        for(int j = 0; j < game.getBoard().dimension().rows; j++){
            for(int i = 0; i < game.getBoard().dimension().cols; i++){
                board[i][j] = new JButton();
                board[i][j].setBackground(Color.darkGray);
                Position cellPosition = new Position(i, j);
                board[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            game.updateGame(cellPosition);
                            refreshDisplay();
                        }else if(e.getButton() == MouseEvent.BUTTON3){
                            Flag cellFlag = game.getMineField().getFlag(cellPosition);
                            if(cellFlag == Flag.None)
                                game.getMineField().setFlag(cellPosition, Flag.MineFlag);
                            else if(cellFlag == Flag.MineFlag)
                                game.getMineField().setFlag(cellPosition, Flag.QuestionMarkFlag);
                            else if(cellFlag == Flag.QuestionMarkFlag)
                                game.getMineField().setFlag(cellPosition, Flag.None);
                            refreshDisplay();
                        }
                    }

                });
                gameContainer.add(board[i][j]);
            }
        }

        frame.setContentPane(gameContainer);
        frame.validate();
        frame.repaint();
    }

    final static Color[] numberColor = {Color.blue,Color.green, Color.red,
            Color.MAGENTA, Color.CYAN, Color.ORANGE,Color.PINK, Color.YELLOW };

    public void refreshDisplay(){
        for(int i = 0; i < game.getBoard().dimension().cols; i++) {
            for (int j = 0; j < game.getBoard().dimension().rows; j++) {
                Cell cell = game.getMineField().getCell(new Position(i, j));
                refreshCell(board[i][j], cell);

            }
        }
    }

    private void refreshCell(JButton uiCell, Cell cell) {
        if(cell instanceof CoveredCell) {
            uiCell.setBackground(Color.darkGray);
            CoveredCell coveredCell = (CoveredCell) cell;
            if(((CoveredCell) cell).getFlag() == Flag.MineFlag){
                uiCell.setText("P");
                uiCell.setForeground(Color.RED);
            }else if(((CoveredCell) cell).getFlag() == Flag.QuestionMarkFlag) {
                if(game.isFinished() && cell.getContent() instanceof CellWithMine) {
                    uiCell.setForeground(Color.RED);
                    uiCell.setText("*");
                }
                else{
                    uiCell.setText("?");
                    uiCell.setForeground(Color.BLUE);
                }
            }else{
                if(game.isFinished() && cell.getContent() instanceof CellWithMine) {
                    uiCell.setForeground(Color.RED);
                    uiCell.setText("*");
                }
                else uiCell.setText("");
            }
        }else{
            uiCell.setBackground(Color.lightGray);
            CellContent content = cell.getContent();
            if(content instanceof CellWithMine) {
                uiCell.setText("*");
            }else{
                int nearMines = ((CellWithoutMine) content).nearMines();
                if(nearMines!=0){
                    uiCell.setText(Integer.toString(nearMines));
                    uiCell.setForeground(numberColor[nearMines-1]);
                }
            }
        }
    }

    private void savePreviousData(){
        previousPane = frame.getContentPane();
    }

    private void restorePreviousData(){
        frame.setContentPane(previousPane);
    }
}
