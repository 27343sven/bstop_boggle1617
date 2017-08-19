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

    @Override
    public void start(Stage primaryStage){
        this.startProcedure();
        primaryStage.setScene(this.scene);
        primaryStage.show();
    }

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

    private void setBoxStyle(){
        this.mainLabel.setMaxHeight(50);
        this.mainLabel.setFont(new Font(23));
        this.mainLabel.setMinHeight(50);
        this.textlabel.setMaxHeight(300);
        this.textlabel.setMinHeight(300);
        this.textlabel.setFont(new Font(18));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
