import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by sven on 18-Aug-17.
 *
 * klasse met handig methoden die static gebruikt kunne woorden
 */
public class UtilLib {

    /**
     * maakt een HBox met een bepaalde alignment en spacing
     *
     * @param alignment Pos
     * @param spacing int ruimte tussen de Nodes
     * @return HBox met de juiste spacing en alignment
     */
    public static HBox makeHBox(Pos alignment, int spacing){
        HBox box = new HBox();
        box.setSpacing(spacing);
        box.setAlignment(alignment);
        return box;
    }

    /**
     * maakt een VBox met een bepaalde alignment en spacing
     *
     * @param alignment Pos
     * @param spacing int ruimte tussen de Nodes
     * @return VBox met de juiste spacing en alignment
     */
    public static VBox makeVBox(Pos alignment, int spacing){
        VBox box = new VBox();
        box.setSpacing(spacing);
        box.setAlignment(alignment);
        return box;
    }

    /**
     * maakt een gecenterde HBox
     *
     * het stopt een node in een VBox en dan een HBox en centreerd deze, hierdoor zit de node in het midden
     *
     * @param node die gecentreerd moet worden
     * @return HBox met de gecentreerde node
     */
    public static HBox getCentered(Node node){
        VBox vb = new VBox(node);
        vb.setAlignment(Pos.CENTER);
        HBox hb = new HBox(vb);
        hb.setAlignment(Pos.CENTER);
        return hb;
    }

    /**
     * maakt een eventhandler voor een textfield
     *
     * dit event staat allen toe om kleine letters in te typen en maar tot een bepaalde lengte
     *
     * @param maxLength int maximale lengte die in de textbox mag worden ingetypt
     * @return Eventhandlers
     */
    public static EventHandler<KeyEvent> lowercaseValidation(int maxLength){
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                TextField text = (TextField) event.getSource();
                if (text.getText().length() >= maxLength){
                    event.consume();
                }
                if (!event.getCharacter().matches("[a-z]")){
                    event.consume();
                }
            }
        };
    }

}
