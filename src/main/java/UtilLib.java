import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by sven on 18-Aug-17.
 */
public class UtilLib {

    public static HBox makeHBox(Pos alignment, int spacing){
        HBox box = new HBox();
        box.setSpacing(spacing);
        box.setAlignment(alignment);
        return box;
    }

    public static VBox makeVBox(Pos alignment, int spacing){
        VBox box = new VBox();
        box.setSpacing(spacing);
        box.setAlignment(alignment);
        return box;
    }

    public static HBox getCentered(Node node){
        VBox vb = new VBox(node);
        vb.setAlignment(Pos.CENTER);
        HBox hb = new HBox(vb);
        hb.setAlignment(Pos.CENTER);
        return hb;
    }

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
