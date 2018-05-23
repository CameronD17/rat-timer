import Output.ResultsWriter;
import Timer.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import static Timer.Position.MIDDLE;

public class RatTimer extends Application {

    private boolean needsNewFile = true;
    private int ratCount = 1;

    private Timer timer = new Timer();
    private Label timerLabel = new Label();
    private DoubleProperty timeSeconds = new SimpleDoubleProperty();

    private ResultsWriter resultsWriter = new ResultsWriter();

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.RED);
        timerLabel.setStyle("-fx-font-size: 4em;");

        final Label loadingLabel = new Label();
        loadingLabel.setText("Results will not be saved!");
        loadingLabel.setFont(Font.font("Arial", 16));
        loadingLabel.setVisible(false);

        final Label currentPositionLabel = new Label();
        currentPositionLabel.setText(MIDDLE.getName());
        currentPositionLabel.setFont(Font.font("Arial", 16));

        final Label groomingLabel = new Label();
        groomingLabel.setText("Grooming");
        groomingLabel.setVisible(false);

        final Label frozenLabel = new Label();
        frozenLabel.setText("Frozen");
        frozenLabel.setVisible(false);

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 300, 300);
        VBox vb = new VBox(20);
        vb.setAlignment(Pos.CENTER);
        vb.setPrefWidth(scene.getWidth());
        vb.getChildren().addAll(loadingLabel, timerLabel, currentPositionLabel, groomingLabel, frozenLabel);
        root.getChildren().add(vb);

        stage.setTitle("Rat Timer");
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                    case A:
                    case S:
                    case D:
                    case M:
                        timer.lap(event.getCode());
                        currentPositionLabel.textProperty().setValue(timer.getCurrentPositionString());
                        break;
                    case G:
                        timer.groom();
                        groomingLabel.setVisible(timer.isGrooming());
                        break;
                    case F:
                        timer.freeze();
                        frozenLabel.setVisible(timer.isFrozen());
                        break;
                    case Y:
                    case N:
                        timer.discard(event.getCode());
                        loadingLabel.setVisible(timer.isDiscardResults());
                        break;
                    case SPACE:
                        timer.startStop();
                        if (!timer.isRunning()) {
                            if (timer.isDiscardResults()) {
                                timer.reset();
                            } else {
                                writeResultsAndResetTimer();
                            }
                            groomingLabel.setVisible(timer.isGrooming());
                            frozenLabel.setVisible(timer.isFrozen());
                            currentPositionLabel.textProperty().setValue(MIDDLE.getName());
                        }
                        startDisplayTimer();
                        break;
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private void startDisplayTimer () {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        if (timer.isRunning()) {
                            timeSeconds.set(timer.getElapsedTimeInSeconds());
                        }
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void writeResultsAndResetTimer () {
        resultsWriter.writeResultsToFile(timer.getMovementLaps(),
                timer.getGroomingLaps(),
                timer.getFrozenLaps(),
                ratCount,
                needsNewFile);
        timer.reset();
        needsNewFile = false;
        ratCount++;
    }
}
