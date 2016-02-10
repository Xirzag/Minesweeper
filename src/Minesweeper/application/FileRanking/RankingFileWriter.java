package Minesweeper.application.FileRanking;

import Minesweeper.model.Ranking;
import Minesweeper.model.RankingLoaderException;
import Minesweeper.view.persistence.RankingWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RankingFileWriter implements RankingWriter {

    private final File rankingFile;
    private final RankingFileReader fileReader;
    private final RankingFileParser parser;

    public RankingFileWriter(File rankingFile) throws RankingLoaderException {
        this.rankingFile = rankingFile;
        fileReader = new RankingFileReader(rankingFile);
        parser = new RankingFileParser();
    }

    @Override
    public void save(Ranking ranking) throws RankingLoaderException {
        List<String> fileLines = getFileLinesWithout(ranking);
        BufferedWriter bufferedWriter = getBufferedWriterForFile();
        PrintWriter file = new PrintWriter(bufferedWriter, true);

        writeRankings(ranking, fileLines, file);
        close(bufferedWriter, file);
    }

    private void writeRankings(Ranking ranking, List<String> fileLines, PrintWriter file) {
        writeRankingIntoFile(ranking, file);
        writeLinesToFile(fileLines, file);
    }

    private void close(BufferedWriter bufferedWriter, PrintWriter file) {
        closeBufferedWriter(bufferedWriter);
        closePrintWriter(file);
    }

    private void closePrintWriter(PrintWriter file) {
        file.flush();
        file.close();
    }

    private void writeLinesToFile(List<String> fileLines, PrintWriter file) {
        for (String fileLine : fileLines)
            file.println(fileLine);
    }


    private void writeRankingIntoFile(Ranking ranking, PrintWriter file) {
        String[] lines = parser.getFileFormatFrom(ranking).split("\n");
        for (String fileLine : lines)
            file.println(fileLine);
    }

    public List<String> getFileLinesWithout(Ranking ranking) throws RankingLoaderException {
        BufferedReader bufferedReader = fileReader.getBufferedReader();
        List<String> lines = getLinesFromBufferedReader(bufferedReader);
        getFileLinesWithout(ranking, lines);

        fileReader.closeBufferedReader(bufferedReader);
        return lines;
    }

    private void getFileLinesWithout(Ranking ranking, List<String> lines) throws RankingLoaderException {
        boolean isInTheRankingsBlock = false;
        for(int i = 0; i < lines.size(); i++) {
            if (isInTheRankingsBlock && isARankingBlock(lines, i)) break;
            if (isTheRankingBlock(ranking, lines, i))
                isInTheRankingsBlock = true;
            if (isInTheRankingsBlock){
                lines.remove(i);
                i--;
            }
        }
    }

    private boolean isARankingBlock(List<String> lines, int i) {
        return parser.isABoardDefinition(lines.get(i));
    }

    private boolean isTheRankingBlock(Ranking ranking, List<String> lines, int i) throws RankingLoaderException {
        return isARankingBlock(lines, i) && parser.parseBoard(lines.get(i)).equals(ranking.getBoard());
    }

    public BufferedWriter getBufferedWriterForFile() throws RankingLoaderException {
        try {
            return new BufferedWriter(new FileWriter(rankingFile));
        } catch (IOException e) {
            return retryCreatingTheFile();
        }
    }

    private BufferedWriter retryCreatingTheFile() throws RankingLoaderException {
        try {
            rankingFile.createNewFile();
            return new BufferedWriter(new FileWriter(rankingFile));
        } catch (IOException e1) {
            throw new RankingLoaderException();
        }
    }

    private void closeBufferedWriter(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getLinesFromBufferedReader(BufferedReader bufferedReader){
        List<String> records = new ArrayList<>();
        bufferedReader.lines().forEach(lineString -> records.add(lineString));

        return records;
    };

}
