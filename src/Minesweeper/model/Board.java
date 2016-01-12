package Minesweeper.model;

import Minesweeper.view.messaging.GameMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private final int numberOfMines;
    private GameMediator mediator;
    private final Dimension dimension;
    private final ArrayList<Position> minesPositions = new ArrayList<>();
    private final HashMap<Position,Flag> closedCells = new HashMap<>();
    private GameTimer gameTimer;

    public Board(Dimension dimension, int numberOfMines) throws BoardError {
        this.dimension = dimension;
        this.numberOfMines = numberOfMines;
        if(numberOfMines > dimension.getArea()-1)
            throw new BoardError("Demasiadas minas");
        if(dimension.cols() < 3 || dimension.rows() < 3)
            throw new BoardError("Dimensiones demasiado pequeñas");
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Board)) return false;
        Board board = (Board) obj;
        boolean haveTheSameMines = board.getNumberOfMines() == this.getNumberOfMines();
        boolean haveTheSameDimension  = this.dim().equals(board.dim());

        return  haveTheSameMines && haveTheSameDimension;
    }

    public void setMediator(GameMediator mediator) {
        this.mediator = mediator;
        mediator.registerBoard(this);
        this.gameTimer = new GameTimer();
        this.gameTimer.setMediator(mediator);
    }

    public Cell getCell(Position position) {
        return new Cell() {
            @Override
            public void open() throws MineExplosion, WinGame {
                if(minesPositions.size() == 0) startGame(position);
                if(minesPositions.contains(position)) explodeMine();
                else if(closedCells.containsKey(position)) openCell();
                if(minesPositions.size() == closedCells.size()) throw new WinGame();
            }

            private void explodeMine() throws MineExplosion {
                closedCells.remove(position);
                throw new MineExplosion();
            }

            private void openCell() throws MineExplosion, WinGame {
                closedCells.remove(position);
                if(nearMines() == 0) {
                    for(Cell adjacentCell : adjacentCells())
                        adjacentCell.open();
                }
            }

            @Override
            public boolean isOpen() {
                return !closedCells.containsKey(position);
            }

            @Override
            public boolean isMine() {
                return minesPositions.contains(position);
            }

            @Override
            public int nearMines() {
                int mineCount = 0;
                for( Cell cell: adjacentCells())
                    if(cell.isMine()) mineCount++;

                return mineCount;
            }

            private ArrayList<Cell> adjacentCells(){
                ArrayList<Cell> adjCells = new ArrayList<>();
                for(int i = -1; i <= 1; i++)
                    for(int j = -1; j <= 1; j++)
                        addCell(new Position(position.x() + i, position.y() + j), adjCells);
                return adjCells;
            }

            private void addCell(Position adjacentPos, ArrayList<Cell> adjacentCells) {
                if(!position.equals(adjacentPos) && position.isInside(dimension))
                    adjacentCells.add(getCell(adjacentPos));
            }

            @Override
            public void setFlag(Flag flag) {
                closedCells.replace(position, flag);
                notifyFlagsChanged();
            }

            @Override
            public Flag getFlag() {
                return closedCells.get(position);
            }
        };
    }

    public void prepareForGame(){
        minesPositions.clear();
        fillCoverCells();
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }

    private void notifyFlagsChanged() {
        int count = (int) closedCells.values().stream().filter(f-> f == Flag.MineFlag).count();

        mediator.setRemainingMines(numberOfMines - count);
    }

    private void fillCoverCells() {
        for(int i = 0; i < dimension.cols(); i++)
            for (int j = 0; j < dimension.rows(); j++)
                closedCells.put(new Position(i,j),Flag.None);
    }

    public Dimension dim() {
        return dimension;
    }

    private void startGame(Position initPos){
        fillMines(initPos);
        gameTimer.start();
    }

    private void fillMines(Position initPos) {
        for (int i = 0; i < numberOfMines; i++) {
            Position position;
            do
                position = getRandomPosition();
            while(minesPositions.contains(position) || position.equals(initPos));
            minesPositions.add(position);
        }
    }

    private Position getRandomPosition() {
        Random random = new Random();
        int posCols = (dimension.cols()>1)? random.nextInt(dimension.cols()-1) : 1;
        int posRows = (dimension.rows()>1)? random.nextInt(dimension.rows()-1) : 1;
        return new Position(posCols,posRows);
    }
}
