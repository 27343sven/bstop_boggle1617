import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by sven on 16-Aug-17.
 *
 * beginscherm van het spel
 *
 */
public class BeginScherm extends Application{

    Button afsluitButton = new Button("afsluiten");
    Button spelenButton = new Button("spelen");
    Button woordToevoegButton = new Button("woord toevoegen");
    VBox window;
    Scene scene;
    int width = 300;
    int height = 300;

    /**
     * standaard functie die wordt aangeroepen als het scherm moet worden getoond
     *
     * roept de startprocedure aan en laat de mainStage zien
     *
     * @param mainStage Stage die wordt meegegeven
     */
    @Override
    public void start(Stage mainStage){
        this.startProcedure();
        mainStage.setScene(this.scene);
        mainStage.show();
    }

    /**
     * voegt alle nodes toe aan het beginscherm
     *
     * zet alle buttons in een Vertical box en zet deze in de scene met de correcte alignment en spacing
     */
    public void startProcedure(){
        this.window = new VBox();
        this.window.setAlignment(Pos.CENTER);
        this.window.setSpacing(50);
        this.scene = new Scene(this.window, this.width, this.height);
        Label welkomBericht = new Label("Welkom bij boggle!");
        this.window.getChildren().addAll(welkomBericht, this.spelenButton, this.woordToevoegButton, this.afsluitButton);
    }

    /**
     * start de Applicatie(om dit scherm te testen)
     *
     * deze dient alleen voor het testen
     *
     * @param args String Array met de systeem argumenten
     */
    public static void main(String[] args) {
        launch(args);
    }

}
