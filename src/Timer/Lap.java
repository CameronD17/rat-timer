package Timer;

import static Timer.Lap.LapType.BINARY;
import static Timer.Lap.LapType.MOVEMENT;

public class Lap {
    private LapType lapType;
    private double startTime;
    private double endTime;
    private double elapsedTime;
    private final Position current;

    Lap (double startTime, Position current) {
        this.startTime = startTime;
        this.current = current;
        this.lapType = BINARY;
    }

    Lap (double startTime, double elapsedTime, Position current) {
        this.startTime = startTime;
        this.elapsedTime = elapsedTime;
        this.current = current;
        this.lapType = MOVEMENT;
    }

    Position getCurrentPosition () {
        return current;
    }

    void stopLap (double endTime) {
        this.endTime = endTime;
        elapsedTime = endTime - startTime;
    }

    private String toSeconds (double input) {
        return Double.toString(input / 1000);
    }

    public String getPrintString () {
        if (lapType == LapType.MOVEMENT) {
            return current.getName() + "," + toSeconds(elapsedTime) + ",";
        }
        return current.getName() + "," + toSeconds(startTime) + "," + toSeconds(endTime) +",";
    }

    public enum LapType {
        MOVEMENT,
        BINARY
    }
}
