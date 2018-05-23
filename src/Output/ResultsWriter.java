package Output;

import Timer.Lap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsWriter {

    private String FILENAME = "results/plusMaze";
    private int CURRENT_FILE_NUMBER = 1;
    private String EXTENSION = ".csv";

    public void writeResultsToFile (List<Lap> movementLaps, List<Lap> groomingLaps, List<Lap> frozenLaps, int ratCount, boolean needsNewFile) {
        CURRENT_FILE_NUMBER = getFileNumber(needsNewFile);
        try (FileWriter fileWriter = new FileWriter(FILENAME + CURRENT_FILE_NUMBER + EXTENSION, true))
        {
            fileWriter.write(makeHeader(ratCount));
            List<String> results = concatenateResults(movementLaps, groomingLaps, frozenLaps);
            for (String result: results) {
                fileWriter.write(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getFileNumber (boolean needsNewFile) {
        int nextAvailableNumber = CURRENT_FILE_NUMBER;
        File file = new File(FILENAME + nextAvailableNumber + EXTENSION);

        while (file.exists()){
            nextAvailableNumber++;
            file = new File(FILENAME + nextAvailableNumber + EXTENSION);
        }
        return needsNewFile ? nextAvailableNumber : nextAvailableNumber - 1;
    }

    private String makeHeader (int ratNumber) {
        return "Rat " + ratNumber + ",Movement Time," +
                "Grooming Position,Grooming Start,Grooming End," +
                "Frozen Position,Frozen Start,Frozen End\n";
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
