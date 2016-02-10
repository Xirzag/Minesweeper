package Minesweeper.model;

import java.time.Duration;
import java.util.*;

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
        Collections.sort(ranking, compareDuration());
        return ranking;
    }

    private Comparator compareDuration() {
        return new Comparator<Result>(){

            @Override
            public int compare(Result result1, Result result2) {
                return result1.getSolveTime().compareTo(result2.getSolveTime());
            }
        };
    }

    public Board getBoard() {
        return board;
    }
}
