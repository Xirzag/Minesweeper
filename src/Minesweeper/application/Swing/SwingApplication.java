package Minesweeper.application.Swing;

import Minesweeper.application.FileRanking.RankingFileReader;
import Minesweeper.application.GameConcreteMediator;
import Minesweeper.control.Command;
import Minesweeper.control.CreateBoardCommand;
import Minesweeper.control.RunGameCommand;
import Minesweeper.model.Board;
import Minesweeper.model.BoardError;
import Minesweeper.model.Dimension;
import Minesweeper.model.RankingLoaderException;
import Minesweeper.view.messaging.GameMediator;
import Minesweeper.view.ui.BoardDialog;
import Minesweeper.view.ui.BoardListDisplay;
import Minesweeper.view.ui.RankingDisplay;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SwingApplication extends JFrame implements BoardListDisplay {
    
    Map<String, Command> commands = new HashMap<>();
    private SwingGameDisplay gameDisplay;
    private BoardDialog boardFrame;
    private JMenu userBoardMenu;
    private final Board[]  defaultBoards = new Board[3];
    private final GameMediator mediator = new GameConcreteMediator();

    private enum BoardName {Easy, Intermediate, Expert};

    public static void main(String[] args){
        new SwingApplication();
    }

    public SwingApplication(){
        initMediator();
        createDefaultBoards();

        initDisplays();
        createCommands();
        deployUi();

        execute("RunEasyGame");
    }

    private void initMediator() {
        mediator.registerBoardListDisplay(this);
    }

    private void createDefaultBoards() {
        try {
            defaultBoards[BoardName.Easy.ordinal()] = new Board(new Dimension(8, 8), 10);
            defaultBoards[BoardName.Intermediate.ordinal()] = new Board(new Dimension(16, 16), 40);
            defaultBoards[BoardName.Expert.ordinal()] = new Board(new Dimension(16, 30), 99);
        }catch (BoardError error) {
            error.printStackTrace();
        }
    }

    private void createCommands() {
        commands.put("CreateBoard", new CreateBoardCommand(boardFrame));


        commands.put("RunEasyGame", new RunGameCommand(gameDisplay,
                defaultBoards[BoardName.Easy.ordinal()], this.mediator));
        commands.put("RunIntermediateGame", new RunGameCommand(gameDisplay,
                defaultBoards[BoardName.Intermediate.ordinal()], this.mediator));
        commands.put("RunExpertGame", new RunGameCommand(gameDisplay,
                defaultBoards[BoardName.Expert.ordinal()], this.mediator));

    }

    private void initDisplays() {
        gameDisplay = new SwingGameDisplay(this.mediator);
        SwingBoardDialog boardDialog = new SwingBoardDialog(gameDisplay, this);
        boardDialog.setGameMediator(this.mediator);
        this.boardFrame = boardDialog;
    }

    private void deployUi(){
        this.setTitle("Mineswepeer");
        this.setJMenuBar(menuBar());
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.add(gameDisplay);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JMenuBar menuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(gameMenu());
        return menuBar;
    }

    private JMenu gameMenu() {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        addMenuItemsTo(gameMenu);
        gameMenu.addSeparator();
        gameMenu.add(itemExit());
        return gameMenu;
    }

    private void addMenuItemsTo(JMenu gameMenu) {
        addGameModeItemsTo(gameMenu);
        addOtherMenuItemsTo(gameMenu);
    }

    private void addOtherMenuItemsTo(JMenu gameMenu) {
        gameMenu.add(itemCustomBoard());
        gameMenu.add(userBoardMenu());
        gameMenu.add(itemRankingDisplay());
    }

    private void addGameModeItemsTo(JMenu gameMenu) {
        gameMenu.add(itemEasyMode());
        gameMenu.add(itemIntermediateMode());
        gameMenu.add(itemExpertMode());
    }

    private JMenuItem itemRankingDisplay() {
        JMenuItem menuItem = new JMenuItem("Rankings...", KeyEvent.VK_X);
        RankingDisplay rankingDisplay = new SwingRankingDisplay(this.mediator);
        menuItem.addActionListener(e -> rankingDisplay.display() );
        return menuItem;
    }

    private JMenu userBoardMenu() {
        userBoardMenu = new JMenu("User Boards");
        updateUserBoards();

        return userBoardMenu;
    }

    private void exitWithFailure(Object errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        this.dispose();
        System.exit(1);
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
        JMenuItem menuItem = new JMenuItem("Custom Board...", KeyEvent.VK_C);
        menuItem.addActionListener(e -> execute("CreateBoard"));
        return menuItem;
    }

    private void execute(String command){
        commands.get(command).execute();
        this.pack();
    }

    @Override
    public void refreshBoardsDisplay() {
        userBoardMenu.removeAll();
        updateUserBoards();
        this.userBoardMenu.repaint();
    }

    private void updateUserBoards() {
        try {
            RankingFileReader rankingFileReader = new RankingFileReader(new File("rankings.data"));
            ArrayList<Board> boards = rankingFileReader.getBoards();
            addBoardsToUserBoardsMenu(boards);
        } catch (RankingLoaderException e) {
            exitWithFailure("Error loading los rankings");
        }
    }

    private void addBoardsToUserBoardsMenu(ArrayList<Board> boards) {
        for(Board board: boards) {
            if(isDefaultBoards(board)) continue;
            addBoardToUserBoardMenu(board);
        }
    }

    private void addBoardToUserBoardMenu(Board board) {
        JMenuItem item = createUserBoardItem(board);
        item.addActionListener(e -> new RunGameCommand(gameDisplay, board, this.mediator).execute());
        userBoardMenu.add(item);
    }

    private JMenuItem createUserBoardItem(Board board) {
        String menuItemTitle = board.toString()+" mines";
        return new JMenuItem(menuItemTitle);
    }

    private boolean isDefaultBoards(Board board) {
        return board.equals(defaultBoards[BoardName.Easy.ordinal()]) ||
                board.equals(defaultBoards[BoardName.Intermediate.ordinal()]) ||
                board.equals(defaultBoards[BoardName.Expert.ordinal()]);
    }
}
