package Minesweeper.view.ui;

import Minesweeper.model.Ranking;

public interface RankingDisplay {
    void display();

    void showResult(Ranking ranking, Ranking.Result result);
}
