import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by sven on 18-Aug-17.
 *
 * scherm waar de gebruiker de namen van de spelers kan invoeren
 *
 */
public class SpelerNaamScherm extends Application {
    VBox window;
    VBox spelerInvoer;
    Scene scene;
    int width = 400;
    int height = 500;
    int players = 4;
    TextField[] textFields;
    Button startButton = new Button("start");
    Button terugButton = new Button("terug");
    Button afsluitButton = new Button("afsluiten");
    CheckBox uniekCheck = new CheckBox("Alleen unkieke woorden");
    VBox uniekBox = new VBox();

    /**
     * maakt het scherm
     *
     * @param mainStage Stage die standaard wordt meegegeven
     */
    @Override
    public void start(Stage mainStage){
        this.startProcedure();
        mainStage.setScene(this.scene);
        mainStage.show();
    }

    /**
     * overload van de start functie hierbij wordt het aantal spelers opgeslagen
     *
     * @param mainStage Stage die standaard wordt meegegeven
     * @param players int het aantal spelers
     */
    public void start(Stage mainStage, int players){
        this.players = players;
        this.startProcedure();
        mainStage.setScene(this.scene);
        mainStage.show();
    }

    /**
     * maakt heen HBox met de voorgedefineerde buttons met de juiste spacing en alignment
     *
     * @return HBox met alle buttons erin
     */
    private HBox makeButtonBox(){
        HBox buttonBox = UtilLib.makeHBox(Pos.CENTER, 50);
        buttonBox.getChildren().addAll(terugButton, afsluitButton, startButton);
        return buttonBox;
    }

    /**
     * maakt alle boxen en zet ze in de juiste volgorde met de juiste spacing en alignment
     */
    public void startProcedure(){
        Label uitleg = new Label("Voer de namen in van de spelers:");
        uniekCheck.setSelected(false);
        this.uniekBox.setAlignment(Pos.CENTER);
        this.spelerInvoer = new VBox();
        this.spelerInvoer.setSpacing(50);
        this.window = new VBox(uitleg, this.spelerInvoer, this.uniekBox, this.makeButtonBox());
        this.spelerInvoer.setMinHeight(270);
        this.window.setAlignment(Pos.CENTER);
        this.window.setSpacing(50);
        this.scene = new Scene(this.window, this.width, this.height);
    }

    /**
     * vult het scherm op bisis van een aantal spelers
     *
     * dez funcite wordt pas later in het programma aangeroepen omdat het aantal spelers nog niet van tevoren is te
     * bepalen, het kijkt eerst of er meer dan een speler is, als dit het geval is dan wordt er een checkbox
     * toegevoegd die ervoor zorgt dat een woord maar door een speler kan worden geraden, hierna loopt het door het
     * aantal spelers heen en maakt voor elke speler een textbox waar een naam kan worden ingevoerd, deze textbox krijgt
     * een event mee waardoor het alleen de letters a-z accepteerd en er maar 10 characters kan worden ingevoerd
     *
     * @param players int aantal spelers
     */
    public void vulScherm(int players){
        this.uniekBox.getChildren().clear();
        this.players = players;
        if (this.players > 1){
            this.uniekBox.getChildren().add(this.uniekCheck);
        }
        this.textFields = new TextField[this.players];
        for (int i = 0; i < this.players; i++) {
            Label player = new Label(String.format("Speler %d:", i + 1));
            TextField text = new TextField(){ @Override public void paste() { }};
            text.addEventFilter(KeyEvent.KEY_TYPED, UtilLib.lowercaseValidation(10));
            HBox temp = new HBox(player, text);
            textFields[i] = text;
            temp.setSpacing(50);
            temp.setAlignment(Pos.CENTER);
            this.spelerInvoer.getChildren().add(temp);
        }
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
