package Minesweeper.application;

import Minesweeper.control.RunGameCommand;
import Minesweeper.model.*;
import Minesweeper.ui.BoardDialog;
import Minesweeper.view.GameMediator;

import javax.swing.*;
import java.awt.*;

public class SwingBoardDialog extends JPanel implements BoardDialog{

    JFrame frame;
    private Container previousPane;

    public SwingBoardDialog(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void display() {
        boardOptions();
    }

    private void boardOptions(){
        this.setLayout(new GridLayout(4,2));

        this.add(new JLabel("Filas"));
        JSpinner rowsInput = new JSpinner(new SpinnerNumberModel(8, 1, 400, 1));
        this.add(rowsInput);
        this.add(new JLabel("Columnas"));
        JSpinner colsInput = new JSpinner(new SpinnerNumberModel(8, 1, 400, 1));
        this.add(colsInput);
        this.add(new JLabel("Minas"));
        JSpinner minesInput = new JSpinner(new SpinnerNumberModel(10, 1, 16000, 1));
        this.add(minesInput);
        JButton button = new JButton("Jugar");

        button.addActionListener(e -> {
            GameMediator mediator = new GameConcreteMediator();
            Board board = new Board(
                    new Minesweeper.model.Dimension((Integer) rowsInput.getValue(), (Integer) colsInput.getValue()),
                    (Integer) minesInput.getValue(), mediator);

            new RunGameCommand(new SwingGameDisplay(frame, board, mediator)).execute();
        });

        this.add(button);

        frame.setContentPane(this);
    }

}
