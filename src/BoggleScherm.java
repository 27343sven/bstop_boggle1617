import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by sven on 15-May-17.
 */
public class BoggleScherm extends Application{
    int boardSize = 5;
    int buttonSpacing = 20;
    int buttonSize = 50;
    BoggleButton[][] BoggleArray = new BoggleButton[boardSize][boardSize];
    ArrayList<Point2D> lines = new ArrayList<Point2D>();
    Pane window;
    HBox boggleButtons;
    ArrayList<Character> currentWord = new ArrayList<Character>();
    char[] characters;
    Group lineGroup = new Group();

    public void start(Stage PrimaryStage){

        this.characters = new char[boardSize * boardSize];
        this.generateCharacters();
        this.boggleButtons = this.makeBoggleScherm();
        Pane boggleScherm = new Pane(boggleButtons, lineGroup);
        this.window = new Pane(boggleScherm);
        Scene scene = new Scene(this.getCentered(window), 500, 500);
        scene.setOnMouseDragReleased(e -> this.onRelease());
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
    }

    public HBox makeBoggleScherm(){
        HBox boggleButtons = new HBox();
        boggleButtons.setSpacing(this.buttonSpacing);
        BoggleButton[][] BoggleArray = new BoggleButton[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++){
            VBox temp = new VBox();
            temp.setSpacing(20);
            temp.setMinSize(this.buttonSize, this.buttonSize);
            for (int j = 0; j < boardSize; j++){
                BoggleButton tempButton = new BoggleButton(this.characters[i + j * this.boardSize], new Point2D(i, j));
                this.BoggleArray[i][j] = tempButton;
                tempButton.setOnMouseDragReleased(e -> this.onRelease());
                tempButton.setOnMouseDragged(this.getMouseEvent());
                tempButton.setOnMouseDragOver(this.getMouseDragEvent());
                tempButton.setMinSize(this.buttonSize, this.buttonSize);
                temp.getChildren().add(tempButton);
            }
            boggleButtons.getChildren().add( temp);
        }
        boggleButtons.setAlignment(Pos.CENTER);
        return(boggleButtons);
    }

    public EventHandler<MouseDragEvent> getMouseDragEvent(){
        EventHandler<MouseDragEvent> test = new EventHandler<MouseDragEvent>(){
            @Override
            public void handle(MouseDragEvent event) {
                BoggleButton currentButton = ((BoggleButton)event.getSource());
                currentButton.setDisabled();
                Point2D tempPoint = new Point2D(
                        buttonSize * currentButton.getLocation().getX() +
                                buttonSpacing * currentButton.getLocation().getX() + buttonSize/2,
                        buttonSize * currentButton.getLocation().getY() +
                                buttonSpacing * currentButton.getLocation().getY() + buttonSize/2
                );
                if (!inLines(tempPoint)){
                    lines.add(tempPoint);
                    currentWord.add(currentButton.getLetter());
                    drawLines();
                }
            }
        };
        return test;
    }

    public EventHandler<MouseEvent> getMouseEvent(){
        EventHandler<MouseEvent> test = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                BoggleButton currentButton = ((BoggleButton)event.getSource());
                currentButton.setDisabled();
                Point2D tempPoint = new Point2D(
                        buttonSize * currentButton.getLocation().getX() +
                                buttonSpacing * currentButton.getLocation().getX() + buttonSize/2,
                        buttonSize * currentButton.getLocation().getY() +
                                buttonSpacing * currentButton.getLocation().getY() + buttonSize/2
                );
                if (!inLines(tempPoint)){
                    lines.add(tempPoint);
                    currentWord.add(currentButton.getLetter());
                    drawLines();
                }
            }
        };
        return test;
    }

    public HBox getCentered(Pane pane){
        VBox vb = new VBox(pane);
        vb.setAlignment(Pos.CENTER);
        HBox hb = new HBox(vb);
        hb.setAlignment(Pos.CENTER);
        return hb;
    }

    public void drawLines(){
        for (int i = 0; i < this.lines.size(); i++) {
            if (i > 0){
                Line tempLine = new Line(
                        this.lines.get(i - 1).getX(), this.lines.get(i - 1).getY(),
                        this.lines.get(i).getX(), this.lines.get(i).getY());
                //System.out.println(tempLine);
                this.lineGroup.getChildren().add(tempLine);
            }
        }
    }

    public boolean inLines(Point2D test){
        for (int i = 0; i < this.lines.size(); i++) {
            if (this.lines.get(i).getX() == test.getX() && this.lines.get(i).getY() == test.getY()){
                return true;
            }
        }
        if (this.lines.size() != 0 && this.lines.get(this.lines.size() - 1).subtract(test).magnitude() > 100){
            return true;
        }
        return false;
    }

    public void generateCharacters(){
        ArrayList<Character> tempCharacters = new ArrayList<Character>();
        char[] vowel = { 'a', 'e', 'i', 'o', 'u' };
        char[] constant = {
                'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w',
                'x', 'y', 'z'
        };
        Random random = new Random();
        for (int i = 0; i < this.boardSize * (this.boardSize-1); i++) {
            tempCharacters.add(constant[random.nextInt(constant.length)]);
        }
        for (int i = (this.boardSize * (this.boardSize-1)); i < this.boardSize * this.boardSize; i++) {
            tempCharacters.add(vowel[random.nextInt(vowel.length)]);
        }
        Collections.shuffle(tempCharacters);
        for (int i = 0; i < tempCharacters.size(); i++) {
            this.characters[i] = tempCharacters.get(i);
        }
    }

    public void onRelease(){
        this.lines.clear();
        this.lineGroup.getChildren().clear();
        //this.window.getChildren().add(this.boggleButtons);
        String woord = "";
        for (int i = 0; i < this.currentWord.size(); i++) {
            woord += this.currentWord.get(i);
        }
        System.out.println(woord);
        this.currentWord.clear();
        for (BoggleButton[] i : this.BoggleArray){
            for (BoggleButton j : i){
                try {
                    j.setDisabled();
                } catch (NullPointerException e){

                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
