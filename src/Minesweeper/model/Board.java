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
    private boolean gameFinish = false;

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

    @Override
    public String toString() {
        return dimension.toString() + " " + numberOfMines;
    }

    public void setMediator(GameMediator mediator) {
        this.mediator = mediator;
        mediator.registerBoard(this);
    }

    public Cell getCell(Position position) {
        return new Cell() {
            @Override
            public void open() {
                startGameIfNotWithInitialSelectedCell(position);
                openCellIfIsNotOpenedYet();
                mediator.openCell(this);
            }

            private void openCellIfIsNotOpenedYet() {
                if(isAMineInPosition(position)) explodeMine();
                else if(isCellClosedInPosition(position)) {
                    openCell();
                    checkIfThePlayerWinTheGame();
                }
            }

            private void explodeMine() {
                closedCells.remove(position);
                mediator.loseGame();
            }

            private void openCell() {
                closedCells.remove(position);
                if(nearMines() == 0)
                    openAdjacentCells();

            }

            private void openAdjacentCells() {
                for(Cell adjacentCell : adjacentCells())
                    if(adjacentCell.getPosition().isInside(dimension))
                        adjacentCell.open();
            }

            @Override
            public boolean isOpen() {
                return !isCellClosedInPosition(position);
            }

            @Override
            public boolean isMine() {
                return isAMineInPosition(position);
            }

            @Override
            public int nearMines() {
                return (int) adjacentCells().stream().
                        filter(cell -> cell.isMine()).count();

            }

            private ArrayList<Cell> adjacentCells(){
                ArrayList<Cell> adjCells = new ArrayList<>();
                for(int i = -1; i <= 1; i++)
                    for(int j = -1; j <= 1; j++)
                        addCellInPositionTo(new Position(position.x() + i, position.y() + j), adjCells);
                return adjCells;
            }

            private void addCellInPositionTo(Position adjacentPos, ArrayList<Cell> adjacentCells) {
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

            @Override
            public Position getPosition() {
                return position;
            }
        };
    }

    private void checkIfThePlayerWinTheGame() {
        if(allCellsWithoutMineAreOpened() && !gameFinish) {
            mediator.winGame();
            gameFinish = true;
        }
    }

    private boolean allCellsWithoutMineAreOpened() {
        return minesPositions.size() == closedCells.size();
    }

    private void startGameIfNotWithInitialSelectedCell(Position initPosition) {
        if(this.minesPositions.size() == 0)
            startGame(initPosition);

    }

    private boolean isCellClosedInPosition(Position position) {
        return closedCells.containsKey(position);
    }

    public void prepareForGame(){
        minesPositions.clear();
        fillCoverCells();
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
        mediator.startGame();
        gameFinish = false;
    }

    private void fillMines(Position initPos) {
        for (int i = 0; i < numberOfMines; i++) {
            addMineExceptIn(initPos);
        }
    }

    private void addMineExceptIn(Position initPos) {
        Position position = getRandomNotUsedPosition(initPos);
        minesPositions.add(position);
    }

    private Position getRandomNotUsedPosition(Position initPos) {
        Position position;
        do
            position = getRandomPosition();
        while(isAMineInPosition(position) || position.equals(initPos));
        return position;
    }

    private boolean isAMineInPosition(Position position) {
        return minesPositions.contains(position);
    }

    private Position getRandomPosition() {
        Random random = new Random();
        int posCols = random.nextInt(dimension.cols());
        int posRows = random.nextInt(dimension.rows());
        return new Position(posCols,posRows);
    }
}
