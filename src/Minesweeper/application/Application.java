package Minesweeper.application;

import Minesweeper.control.Command;
import Minesweeper.control.CreateBoardCommand;
import Minesweeper.control.RunGameCommand;
import Minesweeper.model.Board;
import Minesweeper.model.Dimension;
import Minesweeper.view.GameMediator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Application extends JFrame {
    
    Map<String, Command> commands = new HashMap<>();
    private SwingGameDisplay gameDisplay;
    private SwingBoardDialog boardDialog;

    public static void main(String[] args){
        new Application();
    }

    public Application(){

        createCommands();
        deployUi();

    }

    private void createCommands(){
        //gameDisplay = new SwingGameDisplay(this);
        boardDialog = new SwingBoardDialog(this);
        commands.put("Create Board", new CreateBoardCommand(this.boardDialog));
        //commands.put("Run Game", new RunGameCommand(this.gameDisplay,¿?));
    }

    private void deployUi(){
        this.setTitle("Mineswepeer");
        this.setSize(400, 400);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        //execute("Create Board");
        this.pack();
        this.setVisible(true);

        GameMediator mediator = new GameConcreteMediator();
        new RunGameCommand(new SwingGameDisplay(this, new Board(new Dimension(8,8),10, mediator), mediator)).execute();
    }

    void execute(String command){
        commands.get(command).execute();
    }

}
