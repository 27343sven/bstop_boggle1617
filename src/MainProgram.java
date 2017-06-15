import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Time;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by sven on 15-May-17.
 */
public class MainProgram extends Application {
    private BoggleScherm boggleScherm = new BoggleScherm();
    private int boardSize = 5;
    private int buttonSize = 50;
    private int buttonSpacing = 30;
    private int totalSeconds = 30;
    private int seconds = totalSeconds;
    private Timeline time = new Timeline();
    private ArrayList<Point2D> lines = new ArrayList<>();
    private ArrayList<Character> currentWord = new ArrayList<>();
    private boolean play = false;

    public void start(Stage PrimaryStage){
        boggleScherm.start(PrimaryStage, this.boardSize, this.buttonSize, this.buttonSpacing, 500, 500);
        this.bindBoggleScherm();
        PrimaryStage.show();
    }

    private void bindBoggleScherm(){
        for (BoggleButton[] buttonList: this.boggleScherm.BoggleArray) {
            for (BoggleButton x: buttonList) {
                x.setOnMouseDragReleased(e -> this.onRelease());
                x.setOnMouseDragged(this.getMouseEvent());
                x.setOnMouseDragOver(this.getMouseDragEvent());
            }
        }
        this.boggleScherm.scene.setOnMouseDragReleased(e -> this.onRelease());
        this.boggleScherm.startTimer.setOnAction(e -> this.doTime());
        this.setTimerLabel();
    }

    private void doTime(){
        this.time.stop();
        this.time = new Timeline();
        this.seconds = this.totalSeconds;
        this.time.setCycleCount(this.seconds + 1);

        if (this.time == null){
            this.time.stop();
        }

        this.seconds = this.totalSeconds;
        KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTimerLabel();
                if (seconds == 0) {
                    play = false;
                    boggleScherm.startTimer.setDisable(false);
                    lines.clear();
                    boggleScherm.lineGroup.getChildren().clear();
                } else {
                    seconds--;
                }
            }
        });

        this.time.getKeyFrames().add(frame);
        this.time.playFromStart();
        this.play = true;
        this.boggleScherm.startTimer.setDisable(true);
    }

    private void setTimerLabel(){
        int min = this.seconds / 60;
        String sec = String.format("%02d", this.seconds % 60);

        this.boggleScherm.timer.setText(min + ":" + sec);
    }

    private EventHandler<MouseDragEvent> getMouseDragEvent(){
        EventHandler<MouseDragEvent> test = new EventHandler<MouseDragEvent>(){
            @Override
            public void handle(MouseDragEvent event) {
                if (play) {
                    BoggleButton currentButton = ((BoggleButton) event.getSource());
                    currentButton.setDisabled();
                    Point2D tempPoint = new Point2D(
                            buttonSize * currentButton.getLocation().getX() +
                                    buttonSpacing * currentButton.getLocation().getX() + buttonSize / 2,
                            buttonSize * currentButton.getLocation().getY() +
                                    buttonSpacing * currentButton.getLocation().getY() + buttonSize / 2
                    );
                    if (!inLines(tempPoint)) {
                        lines.add(tempPoint);
                        currentWord.add(currentButton.getLetter());
                        drawLines();
                    }
                }
            }
        };
        return test;
    }

    private EventHandler<MouseEvent> getMouseEvent(){
        EventHandler<MouseEvent> test = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if (play) {
                    BoggleButton currentButton = ((BoggleButton) event.getSource());
                    currentButton.setDisabled();
                    Point2D tempPoint = new Point2D(
                            buttonSize * currentButton.getLocation().getX() +
                                    buttonSpacing * currentButton.getLocation().getX() + buttonSize / 2,
                            buttonSize * currentButton.getLocation().getY() +
                                    buttonSpacing * currentButton.getLocation().getY() + buttonSize / 2
                    );
                    if (!inLines(tempPoint)) {
                        lines.add(tempPoint);
                        currentWord.add(currentButton.getLetter());
                        drawLines();
                    }
                }
            }
        };
        return test;
    }

    private void onRelease(){
        if (play) {
            this.lines.clear();
            this.boggleScherm.lineGroup.getChildren().clear();
            String woord = "";
            for (int i = 0; i < this.currentWord.size(); i++) {
                woord += this.currentWord.get(i);
            }
            System.out.println(woord);
            this.currentWord.clear();
            for (BoggleButton[] i : this.boggleScherm.BoggleArray) {
                for (BoggleButton j : i) {
                    try {
                        j.setDisabled();
                    } catch (NullPointerException e) {
                        System.out.println("Kan geen buttons vinden!");
                    }
                }
            }
        }
    }

    private boolean inLines(Point2D test){
        for (int i = 0; i < this.lines.size(); i++) {
            if (this.lines.get(i).getX() == test.getX() && this.lines.get(i).getY() == test.getY()){
                return true;
            }
        }
        double maxLineLength = 10 + sqrt(2*pow(this.buttonSize + this.buttonSpacing, 2));
        if (this.lines.size() != 0 && this.lines.get(this.lines.size() - 1).subtract(test).magnitude() > maxLineLength){
            return true;
        }
        return false;
    }

    private void drawLines(){
        if (this.lines.size() > 1) {
            int index = this.lines.size() - 1;
            Line tempLine = new Line(
                    this.lines.get(index).getX(), this.lines.get(index).getY(),
                    this.lines.get(index - 1).getX(), this.lines.get(index - 1).getY()
            );
            this.boggleScherm.lineGroup.getChildren().add(tempLine);
        }

    }

    public static void main(String[] args) {
        launch();
    }

}
