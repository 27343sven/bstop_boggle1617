import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by sven on 15-May-17.
 */
public class MainProgram extends Application {
    private double woordAnimationDelay = 0.2;
    private BeginScherm beginScherm = new BeginScherm();
    private BoggleScherm boggleScherm = new BoggleScherm();
    private OptieScherm optieScherm = new OptieScherm();
    private SpelerNaamScherm spelerNaamScherm = new SpelerNaamScherm();
    private WoordenToevoegScherm woordenToevoegScherm = new WoordenToevoegScherm();
    private int boardSize = 5;
    private int buttonSize = 50;
    private int buttonSpacing = 30;
    private int totalSeconds = 200;
    private int seconds = totalSeconds;
    private Timeline time = new Timeline();
    private ArrayList<Point2D> lines = new ArrayList<>();
    private ArrayList<Character> currentWord = new ArrayList<>();
    private boolean play = false;
    private BoggleGame game = new BoggleGame();
    private Stage PrimaryStage;

    public void start(Stage PrimaryStage){
        this.PrimaryStage = PrimaryStage;
        beginScherm.start(this.PrimaryStage);
        this.bindBeginScherm();
        this.bindOptieScherm();
        this.bindSpelerNaamScherm();
        this.bindWoordenToevoegScherm();
        this.game.connectDB("boggleopdracht", "bstop", "opdracht");
        this.game.removePlayer(3);
        PrimaryStage.show();
    }

    private void bindBeginScherm(){
        this.beginScherm.spelenButton.setOnAction(e -> this.optieScherm.start(this.PrimaryStage));
        this.beginScherm.woordToevoegButton.setOnAction(e -> this.woordenToevoegScherm.start(this.PrimaryStage));
        this.beginScherm.afsluitButton.setOnAction(e -> Platform.exit());
    }

    private void bindWoordenToevoegScherm(){
        this.woordenToevoegScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.woordenToevoegScherm.terugButton.setOnAction(e -> this.beginScherm.start(this.PrimaryStage));
        this.woordenToevoegScherm.toevoegButton.setOnAction(e -> this.onToevoegButton());
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
        this.updateSpeler();
    }

    private void bindOptieScherm(){
        this.optieScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.optieScherm.volgendeButton.setOnAction(e -> this.onVolgendeButton());
        this.optieScherm.menuButton.setOnAction(e -> this.beginScherm.start(this.PrimaryStage));
    }

    private void bindSpelerNaamScherm(){
        this.spelerNaamScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.spelerNaamScherm.startButton.setOnAction(e -> this.onStartButton());
        this.spelerNaamScherm.terugButton.setOnAction(e -> this.optieScherm.start(this.PrimaryStage));
    }

    private void onToevoegButton(){
        this.woordenToevoegScherm.statusLabel.setText(this.game.addWoord(this.woordenToevoegScherm.text.getText()));
    }


    private void onStartButton(){
        String[] names = new String[this.optieScherm.players];
        for (int i = 0; i < this.spelerNaamScherm.textFields.length; i++) {
            names[i] = this.spelerNaamScherm.textFields[i].getText();
        }
        this.game.addNewPlayers(names);
        this.boardSize = this.optieScherm.boardSize;
        this.totalSeconds = this.optieScherm.seconds;
        this.seconds = this.optieScherm.seconds;
        boggleScherm.start(this.PrimaryStage, this.boardSize, this.buttonSize, this.buttonSpacing, 900, 600);
        this.bindBoggleScherm();
        boggleScherm.setLettersHidden(true);
    }

    private void onVolgendeButton(){
        this.spelerNaamScherm.start(this.PrimaryStage);
        this.spelerNaamScherm.vulScherm(this.optieScherm.players);
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
                    resetGameBoard();
                } else {
                    seconds--;
                }
            }
        });

        this.time.getKeyFrames().add(frame);
        this.time.playFromStart();
        this.play = true;
        this.boggleScherm.setLettersHidden(false);
        this.boggleScherm.startTimer.setDisable(true);
    }

    private void resetGameBoard(){
        play = false;
        boggleScherm.startTimer.setDisable(false);
        lines.clear();
        boggleScherm.setLettersHidden(true);
        boggleScherm.lineGroup.getChildren().clear();
        resetWoordLabel();
        seconds = totalSeconds;
        setTimerLabel();
        this.game.nextPlayer();
        this.updateGeradenWoorden();
        this.updateSpeler();
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
                BoggleButton currentButton = ((BoggleButton) event.getSource());
                mouseOverLetterEvent(currentButton);
            }
        };
        return test;
    }

    private EventHandler<MouseEvent> getMouseEvent(){
        EventHandler<MouseEvent> test = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                BoggleButton currentButton = ((BoggleButton) event.getSource());
                mouseOverLetterEvent(currentButton);
            }
        };
        return test;
    }

    private void mouseOverLetterEvent(BoggleButton currentButton){
        if (this.play) {
            currentButton.setDisabled();
            Point2D tempPoint = new Point2D(
                    this.buttonSize * currentButton.getLocation().getX() +
                            this.buttonSpacing * currentButton.getLocation().getX() + this.buttonSize / 2,
                    this.buttonSize * currentButton.getLocation().getY() +
                            this.buttonSpacing * currentButton.getLocation().getY() + this.buttonSize / 2
            );
            if (!this.inLines(tempPoint)) {
                this.lines.add(tempPoint);
                this.currentWord.add(currentButton.getLetter());
                this.updateWoordLabel();
                this.drawLines();
            }
        }
    }

    private void updateWoordLabel(){
        String woord = "";
        for (int i = 0; i < this.currentWord.size(); i++) {
            woord += this.currentWord.get(i);
        }
        this.boggleScherm.woordLabel.setText(woord);
    }

    private void updateSpeler(){
        String speler = this.game.getPlayerName();
        this.boggleScherm.speler.setText(String.format("Speler: %s", speler));
    }

    private void updateGeradenWoorden(){
        int score = 0;
        StringBuffer woordenText = new StringBuffer();
        ArrayList<String[]> geradenWoorden = this.game.getGeradenWoorden();
        for (int i = 0; i < geradenWoorden.size() ; i++) {
            woordenText.append(String.format("%-24s %s\n", geradenWoorden.get(i)[0], geradenWoorden.get(i)[1]));
            score += Integer.parseInt(geradenWoorden.get(i)[1]);
        }
        Text text = new Text(woordenText.toString());
        text.setStyle("-fx-font-family: monospace");
        this.boggleScherm.geradenWoordenPane.setContent(text);
        this.boggleScherm.totalScore.setText(String.format("Score: %d", score));
    }

    private void onRelease(){
        if (play) {
            this.lines.clear();
            this.boggleScherm.lineGroup.getChildren().clear();
            String woord = "";
            for (int i = 0; i < this.currentWord.size(); i++) {
                woord += this.currentWord.get(i);
            }
            this.animateOnWoordRelease(woord);
            this.updateGeradenWoorden();
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

    private void resetWoordLabel(){
        boggleScherm.woordLabel.setTextFill(Color.BLACK);
        boggleScherm.woordLabel.setText("");
    }

    private void animateOnWoordRelease(String woord){
        if (this.game.guessWoord(woord)){
            this.boggleScherm.woordLabel.setTextFill(Color.LIGHTGREEN);
        } else {
            this.boggleScherm.woordLabel.setTextFill(Color.RED);
        }
        Timeline time = new Timeline(new KeyFrame(Duration.seconds(woordAnimationDelay), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetWoordLabel();
            }
        }));
        time.setCycleCount(1);
        time.play();
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
