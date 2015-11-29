package Minesweeper.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ranking {
    public class Result{
        Date date;
        Time solveTime;

        public Result(Date date, Time solveTime) {
            this.date = date;
            this.solveTime = solveTime;
        }
    }

    private List<Result> ranking = new ArrayList<>();


}
