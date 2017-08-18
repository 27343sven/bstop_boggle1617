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
 * Created by sven on 18-Aug-17.
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

    @Override
    public void start(Stage mainStage){
        this.startProcedure();
        mainStage.setScene(this.scene);
        mainStage.show();
    }

    public void start(Stage mainStage, int players){
        this.players = players;
        this.startProcedure();
        mainStage.setScene(this.scene);
        mainStage.show();
    }

    private HBox makeButtonBox(){
        HBox buttonBox = UtilLib.makeHBox(Pos.CENTER, 50);
        buttonBox.getChildren().addAll(terugButton, afsluitButton, startButton);
        return buttonBox;
    }

    public void startProcedure(){
        Label uitleg = new Label("Voer de namen in van de spelers:");
        this.spelerInvoer = new VBox();
        this.spelerInvoer.setSpacing(50);
        this.window = new VBox(uitleg, this.spelerInvoer, this.makeButtonBox());
        this.spelerInvoer.setMinHeight(335);
        this.window.setAlignment(Pos.CENTER);
        this.window.setSpacing(50);
        this.scene = new Scene(this.window, this.width, this.height);
    }

    public void vulScherm(int players){
        System.out.println("yay");
        System.out.println(players);
        this.players = players;
        this.textFields = new TextField[this.players];
        for (int i = 0; i < this.players; i++) {
            Label player = new Label(String.format("Speler %d:", i + 1));
            TextField text = new TextField();
            text.addEventFilter(KeyEvent.KEY_TYPED, UtilLib.lowercaseValidation(10));
            HBox temp = new HBox(player, text);
            textFields[i] = text;
            temp.setSpacing(50);
            temp.setAlignment(Pos.CENTER);
            this.spelerInvoer.getChildren().add(temp);
        }
    }




    public static void main(String[] args) {
        launch(args);
    }

}
