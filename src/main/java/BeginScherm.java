import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by sven on 16-Aug-17.
 */
public class BeginScherm extends Application{

    Button afsluitButton = new Button("afsluiten");
    Button spelenButton = new Button("spelen");
    Button woordToevoegButton = new Button("woord toevoegen");
    VBox window;
    Scene scene;
    int width = 300;
    int height = 300;

    @Override
    public void start(Stage mainStage){
        this.startProcedure();
        mainStage.setScene(this.scene);
        mainStage.show();
    }

    public void startProcedure(){
        this.window = new VBox();
        this.window.setAlignment(Pos.CENTER);
        this.window.setSpacing(50);
        this.scene = new Scene(this.window, this.width, this.height);
        Label welkomBericht = new Label("Welkom bij boggle!");
        this.window.getChildren().addAll(welkomBericht, this.spelenButton, this.woordToevoegButton, this.afsluitButton);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
