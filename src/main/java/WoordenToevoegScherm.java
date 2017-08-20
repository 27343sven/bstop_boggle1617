import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by sven on 16-Aug-17.
 *
 * in dit scherm kan er een nieuw wordt worden toegevoegd
 */
public class WoordenToevoegScherm extends Application {
    Button toevoegButton = new Button("Toevoegen");
    Button terugButton = new Button("terug");
    Button afsluitButton = new Button("afsluiten");
    Label statusLabel = new Label();
    TextField text = new TextField(){@Override public void paste() { }};
    double height = 300;
    double width = 300;
    VBox window;
    Scene scene;

    /**
     * maakt het scherm en sized deze naar een bepaalde lengte en breedte
     *
     * @param mainStage Stage die standaard wordt meegegeven
     */
    @Override
    public void start(Stage mainStage){
        this.window = new VBox();
        this.fillWindow();
        this.scene = new Scene(this.window, this.width, this.height);
        mainStage.setScene(this.scene);
        mainStage.show();
    }

    /**
     * vult het scherm
     *
     * deze methode vult daadwerkelijk het scherm, in het scherm zit een status label, een textfield waar een woord
     * kan worden meegegeven van maximaal 25 characters en een knop om het woord op te slaan, deze worden in boxen
     * gestopt met de juiste alignment en spacing.
     */
    public void fillWindow(){
        this.window.setSpacing(20);
        this.window.setAlignment(Pos.CENTER);
        this.window.setMaxWidth(this.width*0.66);
        this.text.setMaxWidth(this.width * 0.5);
        this.text.addEventFilter(KeyEvent.KEY_TYPED, UtilLib.lowercaseValidation(26));
        VBox screen = new VBox(this.statusLabel, this.text, this.toevoegButton);
        HBox buttonBox = new HBox(this.terugButton, this.afsluitButton);
        screen.setMinHeight(230);
        screen.setSpacing(20);
        screen.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(50);
        this.window.getChildren().addAll(screen, buttonBox);
    }

    /**
     * start de applicatie
     *
     * @param args String array met systeem argumenten
     */
    public static void main(String[] args) {
        launch(args);
    }

}
