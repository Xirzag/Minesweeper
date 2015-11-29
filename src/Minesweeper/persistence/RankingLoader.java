package Minesweeper.persistence;

import Minesweeper.model.Board;
import Minesweeper.model.Ranking;

import java.util.ArrayList;

public interface RankingLoader {
    public Ranking load(Board board);
    public void save(Ranking ranking);
    public ArrayList<Board> getBoards();
}
