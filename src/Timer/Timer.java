package Timer;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

import static Timer.Position.MIDDLE;
import static Timer.Position.getPosition;

public class Timer {

    private double startTime;
    private double stopTime;
    private boolean running;
    private List<Lap> movementLaps = new ArrayList<>();
    private List<Lap> groomingLaps = new ArrayList<>();
    private List<Lap> frozenLaps = new ArrayList<>();
    private boolean isGrooming;
    private boolean isFrozen;
    private boolean discardResults;

    public Timer() {
        isGrooming = false;
        isFrozen = false;
        discardResults = false;
    }

    public boolean isRunning () { return running; }
    public boolean isGrooming () { return isGrooming; }
    public boolean isFrozen () { return isFrozen; }
    public boolean isDiscardResults () { return discardResults; }

    public List<Lap> getMovementLaps () { return movementLaps; }
    public List<Lap> getGroomingLaps () { return groomingLaps; }
    public List<Lap> getFrozenLaps () { return frozenLaps; }

    public Position getCurrentPosition () {
        return movementLaps.get(movementLaps.size()-1).getCurrentPosition();
    }

    public String getCurrentPositionString () {
        return getCurrentPosition().getName();
    }

    private double getElapsedTime () {
        double elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }

    public double getElapsedTimeInSeconds () {
        return getElapsedTime() / 1000;
    }

    public boolean startStop () {
        if (running) {
            stopTime = System.currentTimeMillis();
            running = false;
        } else {
            startTime = System.currentTimeMillis();
            movementLaps.add(new Lap(0.0, 0.0, MIDDLE));
            running = true;
        }
        return running;
    }

    public void discard (KeyCode input) {
        discardResults = (input == KeyCode.N);
    }

    public void reset () {
        running = false;
        isGrooming = false;
        isFrozen = false;
        discardResults = false;
        movementLaps.clear();
        groomingLaps.clear();
        frozenLaps.clear();
    }

    public void lap (KeyCode input) {
        double now = System.currentTimeMillis();
        double start = startTime + now;
        double lapLength = getCurrentTime();
        Position prevPos = movementLaps.get(movementLaps.size()-1).getCurrentPosition();
        Position newPos = getPosition(input);
        if (prevPos != newPos) {
            if (prevPos != MIDDLE && newPos != MIDDLE) {
                Lap middleLap = new Lap(start, lapLength, MIDDLE);
                movementLaps.add(middleLap);
                System.out.println("New automatic movement to " + MIDDLE.getName() + " after " + lapLength + " ms");
            }
            Lap lap = new Lap(start, lapLength, newPos);
            movementLaps.add(lap);
            System.out.println("New movement to " + newPos.getName() + " after " + lapLength + " ms");
        }
    }

    public void groom () {
        if (isGrooming) {
            groomingLaps.get(groomingLaps.size()-1).stopLap(getCurrentTime());
            isGrooming = false;
            System.out.println("Rat has stopped groomingLaps.");
        } else {
            groomingLaps.add(new Lap(getCurrentTime(), getCurrentPosition()));
            isGrooming = true;
            System.out.println("Rat has started groomingLaps.");
        }
    }

    public void freeze () {
        if (isFrozen) {
            frozenLaps.get(frozenLaps.size()-1).stopLap(getCurrentTime());
            isFrozen = false;
            System.out.println("Rat is unfrozen.");
        } else {
            frozenLaps.add(new Lap(getCurrentTime(), getCurrentPosition()));
            isFrozen = true;
            System.out.println("Rat is frozenLaps.");
        }
    }

    private double getCurrentTime () {
        return System.currentTimeMillis() - startTime;
    }
}
