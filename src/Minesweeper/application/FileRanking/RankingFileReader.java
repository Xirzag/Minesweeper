package Minesweeper.application.FileRanking;

import Minesweeper.model.Board;
import Minesweeper.model.Ranking;
import Minesweeper.model.RankingLoaderException;
import Minesweeper.view.persistence.RankingReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class RankingFileReader implements RankingReader {

    final RankingFileParser parser;
    private final File file;

    public RankingFileReader(File file) throws RankingLoaderException {
        this.file = file;
        this.parser = new RankingFileParser();
    }

    public Ranking load(Board board) throws RankingLoaderException {
        BufferedReader bufferedReader = getBufferedReader();
        parser.setBufferedReader(bufferedReader);
        Optional<Ranking> first = findBoard(board);

        closeBufferedReader(bufferedReader);
        return first.orElseThrow(() -> new RankingLoaderException());
    }

    private Optional<Ranking> findBoard(Board board) throws RankingLoaderException {
        return parser.getRankingStream().filter(
                ranking -> {
                    boolean b = ranking.getBoard().equals(board);
                    return b;
                }).findFirst();
    }

    public boolean containsBoardRanking(Board board) throws RankingLoaderException {
        BufferedReader bufferedReader = getBufferedReader();
        parser.setBufferedReader(bufferedReader);
        boolean isContained = parser.getBoardStream().anyMatch(readBoard -> board.equals(readBoard));
        closeBufferedReader(bufferedReader);
        return isContained;
    }

    @Override
    public ArrayList<Board> getBoards() throws RankingLoaderException {
        ArrayList<Board> boardsUsedInRankings = new ArrayList<>();
        BufferedReader bufferedReader = getBufferedReader();
        parser.setBufferedReader(bufferedReader);
        parser.getBoardStream().forEach(board -> boardsUsedInRankings.add(board));
        return boardsUsedInRankings;
    }

    public BufferedReader getBufferedReader() throws RankingLoaderException {
        try {
            return new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            return RetryCreatingFile();
        }
    }

    private BufferedReader RetryCreatingFile() throws RankingLoaderException {
        try {
            file.createNewFile();
            return new BufferedReader(new FileReader(file));
        } catch (IOException e1) {
            throw new RankingLoaderException();
        }
    }

    public void closeBufferedReader(BufferedReader bufferedReader){
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
