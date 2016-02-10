package Minesweeper.application.FileRanking;

import Minesweeper.model.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class RankingFileParser {

    private static final String boardDelimiter = "-";
    private static final String resultDelimiter = "-";
    private static final char boardStartMark = ':';
    private static enum boardFields {width, height, mines};
    private static enum resultFields {date, solveTime};
    private static final SimpleDateFormat dateFormat =new SimpleDateFormat("\"yyyy.MM.dd.HH:mm:ss\"");
    private BufferedReader bufferedReader;

    public RankingFileParser() {

    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }


    public Board parseBoard(String line) throws RankingLoaderException {
        if(!isABoardDefinition(line)) throw new RankingLoaderException();
        String[] parts = line.substring(1).split(boardDelimiter);
        Dimension boardDimension = parseDimension(parts);
        int mines = parseMines(parts);
        try {
            return new Board(boardDimension, mines);
        } catch (BoardError boardError) {
            throw new RankingLoaderException();
        }
    }

    public boolean isABoardDefinition(String line) {
        return line.charAt(0) == boardStartMark;
    }

    private Dimension parseDimension(String[] parts) throws RankingLoaderException {
        try {
            int width = Integer.parseInt(parts[boardFields.width.ordinal()]);
            int height = Integer.parseInt(parts[boardFields.height.ordinal()]);
            return new Dimension(width, height);
        }catch (Exception e){
            throw new RankingLoaderException();
        }
    }

    public Stream<Board> getBoardStream() throws RankingLoaderException {
        try {
            return extractBoards();
        }catch (IllegalStateException e){
            throw new RankingLoaderException();
        }
    }

    private Stream<Board> extractBoards() {
        return bufferedReader.lines()
                .filter(line -> isABoardDefinition(line)).
                        map(line -> {
                            return parseBoardOrReturnNull(line);
                        }).
                        filter(board -> board != null);
    }

    private Board parseBoardOrReturnNull(String line) {
        try {
            return parseBoard(line);
        } catch (Exception e) {
            return null;
        }
    }

    public Stream<Ranking> getRankingStream() throws RankingLoaderException {
        List<Ranking> rankings = new ArrayList<>();
        try {
            bufferedReader.lines().forEach(
                    line -> {
                        processLine(rankings, line);
                    });
        }catch (IllegalStateException e){
            throw new RankingLoaderException();
        }
        return  rankings.stream();
    }

    private void processLine(List<Ranking> rankings, String line) {
        try {
            if (isABoardDefinition(line))
                addNewRanking(rankings, line);
            else
                addResultToCurrentRanking(rankings, line);
        } catch (RankingLoaderException e) {

        }
    }

    private void addResultToCurrentRanking(List<Ranking> rankings, String line) throws RankingLoaderException {
        rankings.get(rankings.size() - 1).addResult(parseResult(line));
    }

    private void addNewRanking(List<Ranking> rankings, String line) throws RankingLoaderException {
        rankings.add(new Ranking(parseBoard(line)));
    }

    private Ranking.Result parseResult(String result) throws RankingLoaderException {
        String[] parts = result.split(resultDelimiter);

        Date date = parseRankingDate(parts);
        int seconds = Integer.parseInt(parts[resultFields.solveTime.ordinal()]);
        return new Ranking.Result(date, Duration.ofSeconds(seconds));
    }

    private Date parseRankingDate(String[] parts) throws RankingLoaderException {
        try {
            return dateFormat.parse(parts[resultFields.date.ordinal()]);
        } catch (ParseException e) {
            throw new RankingLoaderException();
        }
    }

    private int parseMines(String[] parts) {
        return Integer.parseInt(  parts[boardFields.mines.ordinal()]  );
    }

    public String getFileFormatFrom(Ranking ranking) {

        String output = getFileFormatFromBoardHeader(ranking) + "\n";
        for (Ranking.Result result : ranking.getRanking())
            output += getFileFormatFrom(result) + "\n";

        return output;
    }

    private String getFileFormatFromBoardHeader(Ranking ranking) {
        String output = String.valueOf(boardStartMark);
        output += ranking.getBoard().dim().rows();

        output += boardDelimiter;
        output += ranking.getBoard().dim().cols();

        output += boardDelimiter;
        output += ranking.getBoard().getNumberOfMines();
        return output;
    }

    private String getFileFormatFrom(Ranking.Result result) {
        String output = dateFormat.format(result.getDate());
        output += resultDelimiter;
        output += result.getSolveTime().getSeconds();
        return output;
    }

}
