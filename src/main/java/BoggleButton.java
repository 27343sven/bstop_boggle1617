import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;


/**
 * Created by sven on 12-May-17.
 *
 * een button die zelf bijhoud of deze is gebruikt en op welke locatie het zich bevind
 *
 */
public class BoggleButton extends Button {
    public char letter;
    public boolean used;
    public Point2D locatie;

    /**
     * initieert de knop
     *
     * zet de meegegeven character op de button en slaat deze met de locatie op en zorgt ervoor dat het niks doet
     * als er op wordt geclickt
     *
     * @param letter char de letter die getoond moet worden op de button
     * @param locatie Point2D met de locatie van de knop
     */
    public BoggleButton(char letter, Point2D locatie){
        super(Character.toString(letter));
        this.locatie = locatie;
        this.letter = letter;
        this.setFont(new Font(20));
        this.setOnMouseDragged(e -> this.setDisabled(true));
        this.setOnMouseDragOver(e -> this.setDisabled(true));
        this.setOnDragDetected(e -> this.startFullDrag());
    }

    /**
     * geeft de locatie van de knop
     *
     * returned de locatie van de knop
     *
     * @return Point2D locatie van de knop
     */
    public Point2D getLocation(){
        return this.locatie;
    }

    /**
     * zorgt ervoor dat de knop niet meer gebruikt kan worden
     *
     * ze de knop op disabled
     */
    public void setDisabled(){
        this.setDisabled(false);
    }

    /**
     * returned de letter van de knop
     *
     * geeft de letter van de knop
     *
     * @return char leeter van de butotn
     */
    public char getLetter(){
        return this.letter;
    }

}