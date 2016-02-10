package Minesweeper.view.persistence;

import Minesweeper.model.RankingLoaderException;
import Minesweeper.model.Board;
import Minesweeper.model.Ranking;

import java.util.ArrayList;

public interface RankingReader {
    public Ranking load(Board board) throws RankingLoaderException;
    public ArrayList<Board> getBoards() throws RankingLoaderException;
}
