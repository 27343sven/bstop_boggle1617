import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by sven on 15-May-17.
 */
public class BoggleScherm extends Application{
    private int boardSize = 5;
    private int buttonSpacing = 20;
    private int buttonSize = 50;
    private int width = 900;
    private int height = 500;
    private int geradenWoordenWidth = 250;
    Scene scene;
    BoggleButton[][] BoggleArray;
    private VBox gameBox;
    private HBox window;
    private  HBox boggleButtons;
    private char[] characters;
    Group lineGroup = new Group();
    Label timer = new Label("UND");
    Button startTimer = new Button("Start");
    Label woordLabel = new Label("");
    ScrollPane geradenWoordenPane = new ScrollPane();
    HBox timerBox = new HBox(timer, startTimer);
    Label totalScore = new Label("Score: 0");
    Label speler = new Label("Speler: UND");

    @Override
    public void start(Stage PrimaryStage){
        this.startProcedure();
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
    }

    public void start(Stage PrimaryStage, int boardSize, int buttonSize, int buttonSpacing, int width, int height){
        this.boardSize = boardSize;
        this.buttonSize = buttonSize;
        this.buttonSpacing = buttonSpacing;
        this.width = width;
        this.height = height;
        this.startProcedure();
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
    }

    private void startProcedure(){
        this.woordLabel.setFont(new Font(25));
        this.BoggleArray = new BoggleButton[boardSize][boardSize];
        this.characters = new char[boardSize * boardSize];
        this.generateCharacters();
        this.boggleButtons = this.makeBoggleScherm();
        Pane boggleScherm = new Pane(boggleButtons, lineGroup);
        this.gameBox = new VBox(this.timerBox, this.woordLabel, boggleScherm);
        this.window = new HBox(gameBox, new VBox(geradenWoordenPane, this.totalScore, this.speler));
        this.timerBox.setSpacing((this.boardSize - 2) * this.buttonSpacing + (this.boardSize - 2) * this.buttonSize);
        this.timerBox.setAlignment(Pos.CENTER);
        this.gameBox.setAlignment(Pos.CENTER);
        this.gameBox.setSpacing(this.buttonSpacing);
        this.window.setSpacing(this.width * 0.20);
        this.geradenWoordenPane.setMinWidth(this.geradenWoordenWidth);
        this.geradenWoordenPane.setMinHeight(this.height - 50);
        this.scene = new Scene(UtilLib.getCentered(window), this.width, this.height);
    }

    private HBox makeBoggleScherm(){
        HBox boggleButtons = new HBox();
        boggleButtons.setSpacing(this.buttonSpacing);
        BoggleButton[][] BoggleArray = new BoggleButton[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++){
            VBox temp = new VBox();
            temp.setSpacing(this.buttonSpacing);
            temp.setMinSize(this.buttonSize, this.buttonSize);
            for (int j = 0; j < boardSize; j++){
                BoggleButton tempButton = new BoggleButton(this.characters[i + j * this.boardSize], new Point2D(i, j));
                this.BoggleArray[i][j] = tempButton;
                tempButton.setMinSize(this.buttonSize, this.buttonSize);
                temp.getChildren().add(tempButton);
            }
            boggleButtons.getChildren().add( temp);
        }
        boggleButtons.setAlignment(Pos.CENTER);
        return(boggleButtons);
    }

    public void setLettersHidden(boolean isHidden){
        for (int i = 0; i < this.BoggleArray.length; i++) {
            for (int j = 0; j < this.BoggleArray[i].length; j++) {
                if (isHidden){
                    this.BoggleArray[i][j].setText("");
                } else {
                    this.BoggleArray[i][j].setText(Character.toString(this.characters[i + j * this.boardSize]));
                }
            }
        }
    }

    private void generateCharacters(){
        ArrayList<Character> tempCharacters = new ArrayList<Character>();
        char[] vowel = { 'a', 'e', 'i', 'o', 'u' };
        char[] all = {
                'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z',
                'a', 'e', 'i', 'o', 'u'
        };
        Random random = new Random();
        for (int i = 0; i < this.boardSize * this.boardSize; i++) {
            if (i < this.boardSize){
                tempCharacters.add(vowel[random.nextInt(vowel.length)]);
            } else {
                tempCharacters.add(all[random.nextInt(all.length)]);
            }
        }
        Collections.shuffle(tempCharacters);
        for (int i = 0; i < tempCharacters.size(); i++) {
            this.characters[i] = tempCharacters.get(i);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
