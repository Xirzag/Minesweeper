package Minesweeper.view.persistence;

import Minesweeper.model.RankingLoaderException;
import Minesweeper.model.Ranking;

public interface RankingWriter {
    public void save(Ranking ranking) throws RankingLoaderException;
}
