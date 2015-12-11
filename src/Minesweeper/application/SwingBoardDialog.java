package Minesweeper.application;

import Minesweeper.control.RunGameCommand;
import Minesweeper.model.Board;
import Minesweeper.view.ui.BoardDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SwingBoardDialog extends JFrame implements BoardDialog{

    private final SwingGameDisplay gameDisplay;
    private final Application frameFather;

    public SwingBoardDialog(SwingGameDisplay gameDisplay, Application application) {
        this.gameDisplay = gameDisplay;
        this.frameFather = application;
    }

    @Override
    public void display() {
        boardOptions();
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        this.add(boardOptions());
        this.pack();
        this.setVisible(true);
    }

    private JPanel boardOptions(){
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.setBorder(new EmptyBorder(10,10,10,10));

        panel.add(new JLabel("Rows"));
        JSpinner rowsInput = new JSpinner(new SpinnerNumberModel(8, 1, 400, 1));
        panel.add(rowsInput);
        panel.add(new JLabel("Cols"));
        JSpinner colsInput = new JSpinner(new SpinnerNumberModel(8, 1, 400, 1));
        panel.add(colsInput);
        panel.add(new JLabel("Mines"));
        JSpinner minesInput = new JSpinner(new SpinnerNumberModel(10, 1, 16000, 1));
        panel.add(minesInput);
        JButton button = new JButton("Play");

        button.addActionListener(e -> {

            Board board = new Board(
                    new Minesweeper.model.Dimension((Integer) rowsInput.getValue(), (Integer) colsInput.getValue()),
                    (Integer) minesInput.getValue());

            new RunGameCommand(gameDisplay, board).execute();
            frameFather.pack();
            this.dispose();
        });

        panel.add(button);

        return panel;
    }

}
