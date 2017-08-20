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

    /**
     * maakt het scherm
     *
     * wordt automatisch aangeroepen als de applicaite wordt gestart, maakt het scherm
     *
     * @param primaryStage Stage die alles toont
     */
    @Override
    public void start(Stage primaryStage){
        this.startProcedure(primaryStage);
    }

    /**
     * maakt het scherm en slaat database gegevens op
     *
     * overload van de andere startfunctie, hierbij wordt de naam van de database en de user met zijn wachtwoord
     * meegegeven en opgeslagen
     *
     * @param primaryStage Stage primaryStage
     * @param DBName String naam van de database
     * @param DBUser String username op de database
     * @param DBPass String wachtwoord van de user
     */
    public void start(Stage primaryStage, String DBName, String DBUser, String DBPass){
        this.DBName = DBName;
        this.DBPass = DBPass;
        this.DBUser = DBUser;
        this.startProcedure(primaryStage);
    }

    /**
     * maakt het scherm
     *
     * maakt het scherm dat wordt getoond als er geen connectie kan worden gemaakt met de database, dit scherm heeft
     * een veld voor de naam van de database een user en het wachtwoord van de user.
     *
     * @param primaryStage Stage die wordt getoond
     */
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

    /**
     * maakt een HBox met alle buttons erin
     *
     * neemt de voorgedeclareerde buttons en zet ze in een Box met de juiste alignment en spacing
     *
     * @return HBox met alle buttons
     */
    private HBox databaseExeptionButtonBox(){
        HBox buttonBox = new HBox(this.connectButton, this.afsluitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(30);
        return buttonBox;
    }

    /**
     * maakt de textinvoer voor de database gegevens
     *
     * loopt door alle database gegevens heen en maakt een textvak vool elk van deze, voor het wachtwoord wordt er een
     * speciaal wachtwoordtextvak aangemaakt, in deze textvakken worden de oude waarden ingevuld. deze worden in een
     * Box gestop met de juise alignment en spacing en gereturned
     *
     * @return VBox met alle textvelden en label vooer het invoeren van de database gegevens
     */
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

    /**
     * start de applicatie
     *
     * @param args String Array systeem argumenten
     */
    public static void main(String[] args) {
        launch(args);
    }

}
