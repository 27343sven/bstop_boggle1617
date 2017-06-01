import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

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
    private ArrayList<Point2D> lines = new ArrayList<>();
    private ArrayList<Character> currentWord = new ArrayList<>();

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
    }

    private EventHandler<MouseDragEvent> getMouseDragEvent(){
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

    private EventHandler<MouseEvent> getMouseEvent(){
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

    private void onRelease(){
        this.lines.clear();
        this.boggleScherm.lineGroup.getChildren().clear();
        String woord = "";
        for (int i = 0; i < this.currentWord.size(); i++) {
            woord += this.currentWord.get(i);
        }
        System.out.println(woord);
        this.currentWord.clear();
        for (BoggleButton[] i : this.boggleScherm.BoggleArray){
            for (BoggleButton j : i){
                try {
                    j.setDisabled();
                } catch (NullPointerException e){
                    System.out.println("Kan geen buttons vinden!");
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
