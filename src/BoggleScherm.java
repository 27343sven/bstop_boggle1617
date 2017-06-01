import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    private int width = 500;
    private int height = 500;
    Scene scene;
    BoggleButton[][] BoggleArray = new BoggleButton[boardSize][boardSize];
    private Pane window;
    private  HBox boggleButtons;
    private char[] characters;
    Group lineGroup = new Group();

    @Override
    public void start(Stage PrimaryStage){

        this.characters = new char[boardSize * boardSize];
        this.generateCharacters();
        this.boggleButtons = this.makeBoggleScherm();
        Pane boggleScherm = new Pane(boggleButtons, lineGroup);
        this.window = new Pane(boggleScherm);
        this.scene = new Scene(this.getCentered(window), this.width, this.height);
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
    }

    public void start(Stage PrimaryStage, int boardSize, int buttonSize, int buttonSpacing, int width, int height){
        this.boardSize = boardSize;
        this.buttonSize = buttonSize;
        this.buttonSpacing = buttonSpacing;
        this.width = width;
        this.height = height;
        this.characters = new char[boardSize * boardSize];
        this.generateCharacters();
        this.boggleButtons = this.makeBoggleScherm();
        Pane boggleScherm = new Pane(boggleButtons, lineGroup);
        this.window = new Pane(boggleScherm);
        this.scene = new Scene(this.getCentered(window), this.width, this.height);
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
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

    private HBox getCentered(Pane pane){
        VBox vb = new VBox(pane);
        vb.setAlignment(Pos.CENTER);
        HBox hb = new HBox(vb);
        hb.setAlignment(Pos.CENTER);
        return hb;
    }

    private void generateCharacters(){
        ArrayList<Character> tempCharacters = new ArrayList<Character>();
        char[] vowel = { 'a', 'e', 'i', 'o', 'u' };
        char[] constant = {
                'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'
        };
        Random random = new Random();
        for (int i = 0; i < this.boardSize * this.boardSize; i++) {
            if (i < this.boardSize){
                tempCharacters.add(vowel[random.nextInt(vowel.length)]);
            } else {
                tempCharacters.add(constant[random.nextInt(constant.length)]);
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
