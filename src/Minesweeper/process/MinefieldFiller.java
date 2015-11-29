package Minesweeper.process;

import Minesweeper.model.*;

import java.util.Random;

public class MinefieldFiller {

    private static class Probability {
        private int remainingMines;
        private int remainingCells;

        public Probability(int remainingMines, int remainingCells) {
            this.remainingMines = remainingMines;
            this.remainingCells = remainingCells;
        }
        
        public double get() {
            return remainingMines / (double) remainingCells;
        }

        public void reduceMines(){
            if(remainingMines>0) remainingMines--;
        }

        public void reduceCells(){
            if(remainingCells>0) remainingCells--;
        }

    }

    public static void fill(Minefield field, Position initPos) {
        Probability probability = new Probability(field.getBoard().getMineAmount(),
                field.getBoard().dimension().getArea() - 1);

        Random randomGenerator = new Random();
        for (int i = 0; i < field.getBoard().dimension().rows; i++) {
            for (int j = 0; j < field.getBoard().dimension().cols; j++) {
                Position position = new Position(j,i);
                if(initPos.equals(position)) addCellWithoutMine(field, position);
                else addCell(field, position, probability, randomGenerator.nextDouble());
            }
        }
    }


    private static void addCell(Minefield field, Position position, Probability probability, double random) {
        if(probability.get() != 0 && probability.get() - random >= 0){
            probability.reduceMines();
            addCellWithMine(field, position);
        }else{
            addCellWithoutMine(field, position);
        }
        probability.reduceCells();
    }

    private static void addCellWithoutMine(Minefield field, Position position) {
        CellWithoutMine cell = new CellWithoutMine();
        field.addCell(new CoveredCell(cell));
        configureCell(cell, position, field);
    }

    private static void addCellWithMine(Minefield field, Position position) {
        CellWithMine mine = new CellWithMine();
        field.addCell(new CoveredCell(mine));
        configureCell(mine, position, field);
    }


    private static void configureCell(CellContent cell, Position position, Minefield field) {
        if(position.y() > 0){

            linkCells(cell, field.getCell(new Position(position.x(), position.y() - 1)).getContent());

            if(position.x() != 0){
                linkCells(cell, field.getCell(new Position(position.x()-1, position.y()-1)).getContent());
            }
            if(position.x() < (field.getBoard().dimension().cols -1)){
                linkCells(cell, field.getCell(new Position(position.x()+1, position.y()-1)).getContent());
            }
        }
        if(position.x() > 0){
            linkCells(cell, field.getCell(new Position(position.x()-1, position.y())).getContent());
        }

    }

    private static void linkCells(CellContent cell, CellContent otherCell){
        if(cell instanceof CellWithoutMine)
            ((CellWithoutMine)cell).addAdjacentCells(otherCell);

        if(otherCell instanceof CellWithoutMine)
            ((CellWithoutMine)otherCell).addAdjacentCells(cell);
    }
}
