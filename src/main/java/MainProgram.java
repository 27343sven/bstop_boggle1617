import com.sun.xml.internal.bind.v2.TODO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private ScoreScherm scoreScherm = new ScoreScherm();
    private DatabaseExeptionScreen databaseExeptionScreen = new DatabaseExeptionScreen();
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
    private String DBName = "boggleopdracht";
    private String DBUser = "bstop";
    private String DBPass = "opdrachts";
    private TextField[] exeptionText = new TextField[3];
    private Stage dialog;

    public void start(Stage PrimaryStage){
        this.PrimaryStage = PrimaryStage;
        beginScherm.start(this.PrimaryStage);
        this.bindBeginScherm();
        this.bindOptieScherm();
        this.bindSpelerNaamScherm();
        this.bindWoordenToevoegScherm();
        this.bindScoreScherm();
        this.bindDatabaseExeptionScreen();
        this.tryDatabaseConnection();
        PrimaryStage.show();
    }

    private void tryDatabaseConnection(){
        try {
            this.game.connectDB(this.DBName, this.DBUser, this.DBPass);
        } catch (SQLException e) {
            this.databaseExeptionScreen.start(this.PrimaryStage, this.DBName, this.DBUser, this.DBPass);
        }
    }

    private void bindScoreScherm(){
        this.scoreScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.scoreScherm.menuButton.setOnAction(e -> this.onEndMenuButton());
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
        this.boggleScherm.opgeefButton.setOnAction(e -> this.resetGameBoard());
        this.boggleScherm.opgeefButton.setDisable(true);
        this.setTimerLabel();
        this.updateSpeler();
    }

    private void bindOptieScherm(){
        this.optieScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.optieScherm.volgendeButton.setOnAction(e -> this.onVolgendeButton());
        this.optieScherm.menuButton.setOnAction(e -> this.beginScherm.start(this.PrimaryStage));
        this.optieScherm.helpButton.setOnAction(e -> this.onHelpButton());
    }

    private void bindSpelerNaamScherm(){
        this.spelerNaamScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.spelerNaamScherm.startButton.setOnAction(e -> this.onStartButton());
        this.spelerNaamScherm.terugButton.setOnAction(e -> this.optieScherm.start(this.PrimaryStage));
    }

    private void bindDatabaseExeptionScreen(){
        this.databaseExeptionScreen.afsluitButton.setOnAction(e -> Platform.exit());
        this.databaseExeptionScreen.connectButton.setOnAction(e -> this.onConnectAgainButton());
    }

    private void onConnectAgainButton(){
        this.DBName = this.databaseExeptionScreen.databaseData[0].getText();
        this.DBUser = this.databaseExeptionScreen.databaseData[1].getText();
        this.DBPass = this.databaseExeptionScreen.databaseData[2].getText();
        this.beginScherm.start(this.PrimaryStage);
        this.tryDatabaseConnection();
    }

    private void showDialog(Parent window, double width, double height){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(this.PrimaryStage);
        Scene dialogScene = new Scene(window, width, height);
        dialog.setScene(dialogScene);
        this.dialog = dialog;
        dialog.showAndWait();
    }

    private void onToevoegButton(){
        this.woordenToevoegScherm.statusLabel.setText(this.game.addWoord(this.woordenToevoegScherm.text.getText()));
    }

    private void onEndMenuButton(){
        this.game.reset();
        this.beginScherm.start(this.PrimaryStage);
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
        this.game.setUniekeWoorden(this.spelerNaamScherm.uniekCheck.isSelected());
        boggleScherm.setLettersHidden(true);
        this.updateSpeler();
        this.updateGeradenWoorden();
    }

    private void onVolgendeButton(){
        this.spelerNaamScherm.start(this.PrimaryStage);
        this.spelerNaamScherm.vulScherm(this.optieScherm.players);
    }

    private void onHelpButton(){
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(this.optieDialogText());
        this.showDialog(dialogVbox, 700, 200);
    }

    private Text optieDialogText(){
        return new Text(
                "Spelers: \t\tselecteer het aantal spelers dat mee speelt, als er 2 of meer spelers\n" + "" +
                        "\t\t\tmeedoen is er de optie om bij het invoeren van de namen te selecteeren\n" +
                        "\t\t\tof een woord door meerdere spelers kan worden geraden of door een.\n\n"+
                        "Bord grote: \tde grote van het spelbord, 4 bij 4 (16) tegels of 5 bij 5 (25) tegels.\n\n" +
                        "Tijd: \t\t\tde tijd die iedere speler krijgt om woorden te maken."
        );
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
        this.boggleScherm.opgeefButton.setDisable(false);
    }

    private void resetGameBoard(){
        play = false;
        boggleScherm.startTimer.setDisable(false);
        boggleScherm.opgeefButton.setDisable(true);
        lines.clear();
        boggleScherm.setLettersHidden(true);
        boggleScherm.lineGroup.getChildren().clear();
        resetWoordLabel();
        seconds = totalSeconds;
        setTimerLabel();
        if (!this.game.nextPlayer()){
            this.endGame();
        }
        this.updateGeradenWoorden();
        this.updateSpeler();
    }

    private void endGame(){
        this.scoreScherm.start(this.PrimaryStage);
        if (this.game.getSpelers().length > 1) {
            this.endMultiPlayer();
        } else {
            this.endSinglePlayer();
        }
    }

    private void endMultiPlayer(){
        ArrayList<String[]> scores = this.game.getTotalScore(true);
        List<Integer> idQueue = IntStream.of(this.game.getSpelers()).boxed().collect(Collectors.toList());
        StringBuilder text = new StringBuilder();
        text.append(String.format("%-10s %s\n", "naam", "score"));
        for (String[] spelerScore : scores) {
            text.append(String.format("%-10s %s\n", this.game.getPlayerName(
                    Integer.parseInt(spelerScore[0])),
                    spelerScore[1]));
            idQueue.remove(new Integer(Integer.parseInt(spelerScore[0])));
        }
        for (Integer id : idQueue) {
            text.append(String.format("%-10s 0\n", this.game.getPlayerName(id)));
        }
        this.scoreScherm.textlabel.setText(text.toString());
    }

    private void endSinglePlayer(){
        String player = this.game.getPlayerName(this.game.getSpelers()[0]);
        int score = 0;
        if (this.game.playerScoreExists(this.game.getSpelers()[0])){
            ArrayList<String[]> besteWoorden = this.game.getBesteWoorden(this.game.getSpelers()[0]);
            StringBuilder text = new StringBuilder();
            text.append(String.format("%-25s %s\n", "woord", "punten"));
            for (String[] woordScore : besteWoorden) {
                text.append(String.format("%-25s %s\n", woordScore[0], woordScore[1]));
            }
            this.scoreScherm.textlabel.setText(text.toString());
            score = Integer.parseInt(this.game.getTotalScore(true).get(0)[1]);
        } else {
            this.scoreScherm.textlabel.setText("<geen woorden>");
        }
        this.scoreScherm.mainLabel.setText(String.format("Score %s: %d\n",player, score));
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
