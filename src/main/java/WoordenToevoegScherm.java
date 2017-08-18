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
 */
public class WoordenToevoegScherm extends Application {
    Button toevoegButton = new Button("Toevoegen");
    Button terugButton = new Button("terug");
    Button afsluitButton = new Button("afsluiten");
    Label statusLabel = new Label();
    TextField text = new TextField();
    double height = 300;
    double width = 300;
    VBox window;
    Scene scene;

    @Override
    public void start(Stage mainStage){
        this.window = new VBox();
        this.fillWindow();
        this.scene = new Scene(this.window, this.width, this.height);
        mainStage.setScene(this.scene);
        mainStage.show();
    }

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

    public static void main(String[] args) {
        launch(args);
    }

}
