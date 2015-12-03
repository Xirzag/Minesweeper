package Minesweeper.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private final int mineAmount;
    private final Dimension dimension;
    private final ArrayList<Position> mines = new ArrayList<>();
    private final HashMap<Position,Flag> closedCells = new HashMap<>();

    public Board(Dimension dimension, int mineAmount) {
        this.dimension = dimension;
        this.mineAmount = mineAmount;
    }

    public Cell getCell(Position position) {
        if(mines.size() == 0) initBoard();
        return new Cell() {
            @Override
            public void open() throws MineExplosion, WinGame {
                if(mines.contains(position)) throw new MineExplosion();
                else if(closedCells.containsKey(position)) openCell();
                if(mines.size() == closedCells.size()) throw new WinGame();
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
                return mines.contains(position);
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
                        addCell(i,j, adjCells);
                return adjCells;
            }

            private void addCell(int i, int j, ArrayList<Cell> adjCells) {
                Position adjPosition = new Position(position.x() + i, position.y() + j);
                if(!position.equals(adjPosition))
                    adjCells.add(getCell(adjPosition));
            }

            @Override
            public void setFlag(Flag flag) {
                closedCells.replace(position, flag);
            }

            @Override
            public Flag getFlag() {
                return closedCells.get(position);
            }
        };
    }

    private void initBoard() {
        fillMines();
        fillCoverCells();
    }

    private void fillCoverCells() {
        for(int i = 0; i < dimension.cols; i++)
            for (int j = 0; j < dimension.rows; j++) {
                closedCells.put(new Position(i,j),Flag.None);
            }
    }

    public Dimension dim() {
        return dimension;
    }

    public int getMineAmount() {
        return mineAmount;
    }

    private void fillMines() {
        for (int i = 0; i < mineAmount; i++) {
            Position position;
            do{
                position = getRandomPosition();
            }while(mines.contains(position));
            mines.add(position);
        }
    }

    public Position getRandomPosition() {
        Random random = new Random();
        return new Position(random.nextInt(dimension.cols-1),random.nextInt(dimension.rows-1));
    }
}
