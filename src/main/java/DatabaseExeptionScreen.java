import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by sven on 20-Aug-17.
 */
public class DatabaseExeptionScreen extends Application{
    Button afsluitButton = new Button("afsluiten");
    Button connectButton = new Button("verbind opnieuw");
    String DBName;
    String DBUser;
    String DBPass;
    TextField[] databaseData = new TextField[3];



    @Override
    public void start(Stage primaryStage){
        this.startProcedure(primaryStage);
    }

    public void start(Stage primaryStage, String DBName, String DBUser, String DBPass){
        this.DBName = DBName;
        this.DBPass = DBPass;
        this.DBUser = DBUser;
        this.startProcedure(primaryStage);
    }

    private void startProcedure(Stage primaryStage){
        Text warning = new Text(
                "er kon geen connectie worden gemaakt met de database\nvoer de correcte gegevens hier in:"
        );
        VBox dbExeption = new VBox(warning, this.databaseExeptionTextfields(), this.databaseExeptionButtonBox());
        dbExeption.setAlignment(Pos.CENTER);
        dbExeption.setSpacing(30);
        Scene scene = new Scene(dbExeption, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox databaseExeptionButtonBox(){
        HBox buttonBox = new HBox(this.connectButton, this.afsluitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(30);
        return buttonBox;
    }

    private VBox databaseExeptionTextfields(){
        VBox texts = UtilLib.makeVBox(Pos.CENTER, 30);
        String[] fields = new String[]{"Database", "User       ", "Password"};
        String[] varField = new String[]{this.DBName, this.DBUser, this.DBPass};
        TextField test;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].matches("Password")) {
                test = new PasswordField();
            } else {
                test = new TextField(varField[i]);
            }
            HBox temp = UtilLib.makeHBox(Pos.CENTER, 20);
            temp.getChildren().addAll(new Text(fields[i]), test);
            texts.getChildren().add(temp);
            this.databaseData[i] = test;
            test.setText(varField[i]);
        }
        return texts;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
