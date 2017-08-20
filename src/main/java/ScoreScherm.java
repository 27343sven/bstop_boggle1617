import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by sven on 19-Aug-17.
 *
 * scherm die de resultaten van het spel laat zien
 */
public class ScoreScherm extends Application {

    VBox window;
    Scene scene;
    Button menuButton = new Button("menu");
    Button afsluitButton = new Button("afsluiten");
    Label mainLabel = new Label("Winnaar: UND");
    Label textlabel = new Label("Scores:");
    int width = 400;
    int height = 500;

    /**
     * maakt het scherm
     *
     * @param primaryStage Stage die standaard wordt meegegeven
     */
    @Override
    public void start(Stage primaryStage){
        this.startProcedure();
        primaryStage.setScene(this.scene);
        primaryStage.show();
    }

    /**
     * maakt het scherm
     *
     * er wordt een label gamaakt on de uiteindelijke winnaar te laten, en nog een label om de scores te laten zien
     * van dit label wordt de style op monospaced gezet zodat alles mooi aligned en het overzichtelijk is.
     */
    public void startProcedure(){
        HBox buttonBox = new HBox(this.menuButton, this.afsluitButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setSpacing(50);
        buttonBox.setMaxHeight(50);
        VBox labelBox = new VBox(this.mainLabel, this.textlabel);
        labelBox.setAlignment(Pos.CENTER);
        setBoxStyle();
        textlabel.setStyle("-fx-font-family: monospace");
        this.window = new VBox(labelBox, buttonBox);
        this.window.setAlignment(Pos.CENTER);
        this.scene = new Scene(this.window, this.width, this.height);
    }

    /**
     * maakt alle boxen netjes
     *
     * ze de aignement en spacing van alle boxen
     */
    private void setBoxStyle(){
        this.mainLabel.setMaxHeight(50);
        this.mainLabel.setFont(new Font(23));
        this.mainLabel.setMinHeight(50);
        this.textlabel.setMaxHeight(300);
        this.textlabel.setMinHeight(300);
        this.textlabel.setFont(new Font(18));
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
