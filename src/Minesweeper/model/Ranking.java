package Minesweeper.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ranking {
    public static class Result{
        private final Date date;
        private final Duration solveTime;

        public Result(Date date, Duration solveTime) {
            this.date = date;
            this.solveTime = solveTime;
        }

        public Date getDate() {
            return date;
        }

        public Duration getSolveTime() {
            return solveTime;
        }
    }

    private List<Result> ranking = new ArrayList<>();
    Board board;

    public Ranking(Board board) {
        this.board = board;
    }

    public void addResult(Result result){
        ranking.add(result);
    }

    public List<Result> getRanking() {
        return ranking;
    }

    public Board getBoard() {
        return board;
    }
}
