package Output;

import Timer.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsWriter {

    private String FILENAME = ".." + File.separator + "results" + File.separator + "plusMaze";
    //private String FILENAME = "results" + File.separator + "plusMaze";
    private int INITIAL_FILE_NUMBER = 0;
    private String EXTENSION = ".csv";

    public void writeResultsToFile (Timer timer, int ratCount, boolean needsNewFile) {
        File file = getCurrentFile(needsNewFile);
        try (FileWriter fileWriter = new FileWriter(file, true))
        {
            fileWriter.write(makeHeader(ratCount));
            List<String> results = concatenateResults(timer.getMovementLaps(), timer.getGroomingLaps(), timer.getFrozenLaps());
            for (String result: results) {
                fileWriter.write(result);
            }
            fileWriter.write(makeEnd(timer.getStopTimeInSeconds()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getCurrentFile (boolean needsNewFile) {
        File currentFile = new File(FILENAME + INITIAL_FILE_NUMBER + EXTENSION);
        int nextAvailableNumber = INITIAL_FILE_NUMBER + 1;
        File nextFile = new File(FILENAME + nextAvailableNumber + EXTENSION);

        while (nextFile.exists()){
            currentFile = nextFile;
            nextAvailableNumber++;
            nextFile = new File(FILENAME + nextAvailableNumber + EXTENSION);
        }

        return needsNewFile ? nextFile : currentFile;
    }

    private String makeHeader (int ratNumber) {
        return "Rat " + ratNumber + ",Movement Time," +
                "Grooming Position,Grooming Start,Grooming End," +
                "Frozen Position,Frozen Start,Frozen End\n";
    }

    private String makeEnd (String stopTime) {
        return "END," + stopTime + "\n";
    }

    private List<String> concatenateResults (List<Lap> movementLaps,
                                                    List<Lap> groomingLaps,
                                                    List<Lap> frozenLaps) {
        List<String> results = new ArrayList<>();
        int numberOfRows = getLongestLength(movementLaps, groomingLaps, frozenLaps);

        for (int i = 0; i < numberOfRows; i++) {
            String resultLine = "";
            if (movementLaps.size() > i) {
                resultLine = resultLine + movementLaps.get(i).getPrintString();
            } else {
                resultLine = resultLine + ",,";
            }
            if (groomingLaps.size() > i) {
                resultLine = resultLine + groomingLaps.get(i).getPrintString();
            } else {
                resultLine = resultLine + ",,,";
            }
            if (frozenLaps.size() > i) {
                resultLine = resultLine + frozenLaps.get(i).getPrintString();
            } else {
                resultLine = resultLine + ",,,";
            }
            resultLine += "\n";
            results.add(resultLine);
        }

        return results;
    }

    private int getLongestLength (List<?>... lists) {
        int longestLength = 0;
        for (List list : lists) {
            if (list.size() > longestLength) {
                longestLength = list.size();
            }
        }
        return longestLength;
    }
}
