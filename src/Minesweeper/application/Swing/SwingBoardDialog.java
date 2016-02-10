package Minesweeper.application.Swing;

import Minesweeper.control.RunGameCommand;
import Minesweeper.model.Board;
import Minesweeper.model.BoardError;
import Minesweeper.model.Dimension;
import Minesweeper.view.messaging.GameMediator;
import Minesweeper.view.ui.BoardDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SwingBoardDialog extends JFrame implements BoardDialog{

    private SwingGameDisplay gameDisplay;
    private final JFrame frameFather;
    private GameMediator mediator;
    private JSpinner rowsInput, colsInput, minesInput;

    public SwingBoardDialog(SwingGameDisplay gameDisplay, JFrame application) {
        this.gameDisplay = gameDisplay;
        this.frameFather = application;
    }

    public void setGameMediator(GameMediator mediator){
        this.mediator = mediator;
    }

    @Override
    public void display() {
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        this.add(boardOptions());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel boardOptions(){
        JPanel panel = createOptionsDisplay();

        addInputsTo(panel);
        addPlayButtonTo(panel);

        return panel;
    }

    private void addInputsTo(JPanel panel) {
        addRowInputTo(panel);
        addColInputTo(panel);
        addMineInputTo(panel);
    }

    private JPanel createOptionsDisplay() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        return panel;
    }

    private void addPlayButtonTo(JPanel panel) {
        JButton button = new JButton("Play");

        button.addActionListener(playGameWithSelectedConfiguration()
        );

        panel.add(button);
    }

    private void addMineInputTo(JPanel panel) {
        panel.add(new JLabel("Mines"));
        minesInput = new JSpinner(new SpinnerNumberModel(10, 1, 16000, 1));
        panel.add(minesInput);
    }

    private void addColInputTo(JPanel panel) {
        panel.add(new JLabel("Cols"));
        colsInput = new JSpinner(new SpinnerNumberModel(8, 1, 400, 1));
        panel.add(colsInput);
    }

    private void addRowInputTo(JPanel panel) {
        panel.add(new JLabel("Rows"));
        rowsInput = new JSpinner(new SpinnerNumberModel(8, 1, 400, 1));
        panel.add(rowsInput);
    }

    private ActionListener playGameWithSelectedConfiguration() {
        return e -> {
            try {
                runGameWithSelectedConfiguration();
            } catch (BoardError boardError) {
                showErrorMessage(boardError.getMessage());
            }
        };
    }

    private void runGameWithSelectedConfiguration() throws BoardError {
        Board board = createBoardFromInputsValues();
        new RunGameCommand(gameDisplay, board, this.mediator).execute();
        closeBoardDialog();
    }

    private Board createBoardFromInputsValues() throws BoardError {
        return new Board(
                new Dimension((Integer) rowsInput.getValue(), (Integer) colsInput.getValue()),
                (Integer) minesInput.getValue());
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void closeBoardDialog() {
        frameFather.pack();
        this.dispose();
    }


}
