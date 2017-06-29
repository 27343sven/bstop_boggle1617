import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;


/**
 * Created by sven on 12-May-17.
 */
public class BoggleButton extends Button {
    public char letter;
    public boolean used;
    public Point2D locatie;

    public BoggleButton(char letter, Point2D locatie){
        super(Character.toString(letter));
        this.locatie = locatie;
        this.letter = letter;
        this.setFont(new Font(20));
        this.setOnMouseDragged(e -> this.setDisabled(true));
        this.setOnMouseDragOver(e -> this.setDisabled(true));
        this.setOnDragDetected(e -> this.startFullDrag());
    }

    public Point2D getLocation(){
        return this.locatie;
    }

    public void setUsed(boolean used) {
        this.used = used;
        if (this.used == true) {
            this.setDisabled(true);
        } else {
            this.setDisabled(false);
        }
    }

    public boolean getUsed(){
        return this.used;
    }

    public void setLetter(char letter){
        if (Character.isLowerCase(letter)){
            this.letter = letter;
        }
    }

    public void setDisabled(){
        this.setDisabled(false);
    }

    public char getLetter(){
        return this.letter;
    }

}
