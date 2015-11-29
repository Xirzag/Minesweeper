package Minesweeper.application;

import Minesweeper.control.Command;
import Minesweeper.control.RunGameCommand;
import Minesweeper.model.Board;
import Minesweeper.model.Dimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Application {
    
    static JFrame frame;
    Map<String, Command> commands = new HashMap<>();
    
    public static void main(String[] args){
        deployUi();
    }
    
    private void createCommands(){
        //NoCommands
    }

    private static void deployUi(){
        frame = new JFrame("Minesweeper");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        boardOptions();
        //frame.pack();
        frame.setVisible(true);
    }

    private static void boardOptions(){

        JPanel panel = new JPanel(new GridLayout(4,2));

        panel.add(new JLabel("Filas"));
        JSpinner rowsInput = new JSpinner(new SpinnerNumberModel(8, 1, 400, 1));
        panel.add(rowsInput);
        panel.add(new JLabel("Columnas"));
        JSpinner colsInput = new JSpinner(new SpinnerNumberModel(8, 1, 400, 1));
        panel.add(colsInput);
        panel.add(new JLabel("Minas"));
        JSpinner minesInput = new JSpinner(new SpinnerNumberModel(10, 1, 16000, 1));
        panel.add(minesInput);
        JButton button = new JButton("Jugar");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Board board = new Board(
                        new Dimension((Integer) rowsInput.getValue(), (Integer) colsInput.getValue()),
                        (Integer) minesInput.getValue());

                new RunGameCommand(new SwingGameDisplay(frame), board).execute();
            }
        });
        panel.add(button);
        frame.getContentPane().add(panel, BorderLayout.WEST);

        /*frame.add(numberInputWithLabel("Columnas", new SpinnerNumberModel(20, 1, 400, 1)));
        frame.add(numberInputWithLabel("Filas", new SpinnerNumberModel(20, 1, 400, 1)));
        frame.add(numberInputWithLabel("Minas", new SpinnerNumberModel(10, 1, 15999, 1)));*/

    }

}
