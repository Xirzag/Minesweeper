package Minesweeper.application;

import Minesweeper.control.Command;
import Minesweeper.control.CreateBoardCommand;
import Minesweeper.control.RunGameCommand;
import Minesweeper.model.Board;
import Minesweeper.model.Dimension;
import Minesweeper.view.ui.BoardDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Application extends JFrame {
    
    Map<String, Command> commands = new HashMap<>();
    private SwingGameDisplay gameDisplay;
    private BoardDialog boardFrame;

    public static void main(String[] args){
        new Application();
    }

    public Application(){

        createCommands();
        deployUi();

    }

    private void createCommands(){
        gameDisplay = new SwingGameDisplay();
        boardFrame = new SwingBoardDialog(gameDisplay, this);
        commands.put("CreateBoard", new CreateBoardCommand(boardFrame));
        commands.put("RunEasyGame", new RunGameCommand(gameDisplay, new Board(new Dimension(8,8),10)));
        commands.put("RunIntermediateGame", new RunGameCommand(gameDisplay, new Board(new Dimension(16,16),40)));
        commands.put("RunExpertGame", new RunGameCommand(gameDisplay, new Board(new Dimension(16,30),99)));
    }

    private void deployUi(){
        this.setTitle("Mineswepeer");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setJMenuBar(menuBar());
        this.add(gameDisplay);
        this.pack();
        this.setVisible(true);

        execute("RunEasyGame");
    }

    private JMenuBar menuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(gameMenu());
        return menuBar;
    }

    private JMenu gameMenu() {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        gameMenu.add(itemEasyMode());
        gameMenu.add(itemIntermediateMode());
        gameMenu.add(itemExpertMode());
        gameMenu.add(itemCustomBoard());
        gameMenu.addSeparator();
        gameMenu.add(itemExit());
        return gameMenu;
    }

    private JMenuItem itemExit() {
        JMenuItem menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(e -> dispose() );
        return menuItem;
    }

    private JMenuItem itemEasyMode() {
        JMenuItem menuItem = new JMenuItem("Easy mode", KeyEvent.VK_E);
        menuItem.addActionListener(e -> execute("RunEasyGame"));
        return menuItem;
    }

    private JMenuItem itemIntermediateMode() {
        JMenuItem menuItem = new JMenuItem("Intermediate mode", KeyEvent.VK_I);
        menuItem.addActionListener(e -> execute("RunIntermediateGame"));
        return menuItem;
    }

    private JMenuItem itemExpertMode() {
        JMenuItem menuItem = new JMenuItem("Expert mode", KeyEvent.VK_P);
        menuItem.addActionListener(e -> execute("RunExpertGame"));
        return menuItem;
    }

    private JMenuItem itemCustomBoard() {
        JMenuItem menuItem = new JMenuItem("Custom Board", KeyEvent.VK_C);
        menuItem.addActionListener(e -> execute("CreateBoard"));
        return menuItem;
    }

    void execute(String command){
        commands.get(command).execute();
        this.pack();
    }

}
