import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by sven on 03-Jul-17.
 */
public class OptieScherm extends Application{

    Scene scene;
    int players = 1;
    int boardSize = 5;
    int seconds = 180;
    ToggleGroup playerSelection = new ToggleGroup();
    ToggleGroup boardSelection = new ToggleGroup();
    ToggleGroup timeSelection = new ToggleGroup();
    RadioButton[] playerButtons;
    RadioButton[] boardButtons;
    RadioButton[] timeButtons;
    Button volgendeButton = new Button("volgende");
    Button menuButton = new Button("menu");
    Button afsluitButton = new Button("afsluiten");
    Button helpButton = new Button("?");

    /**
     * start functie die wordt aangeroepen op het scherm te starten, maakt het scherm
     *
     * @param mainStage Stage die standaard wordt meegegeven
     */
    @Override
    public void start(Stage mainStage) {
        VBox window = this.setToggleBoxes();
        this.bindToggleGroups();
        window.setSpacing(40);
        window.setAlignment(Pos.BOTTOM_CENTER);
        this.scene = new Scene(window, 400, 400);
        mainStage.setScene(this.scene);
        mainStage.show();
    }

    /**
     * maakt toggleboxen voor alle opties
     *
     * er wordt een togglebox aangemaakt voor de volgende opties (aantal spelers, bordgrote, tijd) voor elk van deze
     * boxen wordt er een standaard waarde megegeven en er wordt oook metadata meegegeven met de getallen die het
     * programma gebruikt (bijv. voor bord grote 4x4 wordt er aan het programma 4 meegegeven). ook wordt er een hulp
     * knop aangemaakt die later uitleg gaat geven over de opties in het programma
     *
     * @return VBox met alle toggleboxen
     */
    public VBox setToggleBoxes(){
        this.helpButton.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-style: dotted");
        VBox playerBox = this.makeToggleGroup(
                new String[] {"1", "2", "3", "4"}, this.playerSelection,
                this.playerButtons, 0, "Spelers:", new String[] {"1", "2", "3", "4"});
        VBox boardBox = this.makeToggleGroup(
                new String[] {"4x4", "5x5"}, this.boardSelection,
                this.boardButtons, 1, "Bord Grote:", new String[] {"4", "5"});
        VBox timeBox = this.makeToggleGroup(
                new String[] {"2:00", "2:30", "3:00", "3:30", "4:00"}, this.timeSelection,
                this.timeButtons, 2, "Tijd:", new String[] {"120", "150", "180", "210", "240"});
        return new VBox(playerBox, boardBox, timeBox,  this.helpButton, makeButtonBox());
    }

    /**
     * maakt een box met alle buttons
     *
     * maakt een HBox met de juiste spacing en alignment en zet hier alle voorafgedefineerde buttons in, ook weizigt het
     * de stijl van de hulpbutton
     *
     * @return HBox met alle buttons
     */
    private HBox makeButtonBox(){
        HBox buttonBox = UtilLib.makeHBox(Pos.CENTER, 50);
        this.helpButton.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-style: dotted");
        HBox helpBox = new HBox(this.helpButton);
        helpBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(menuButton, afsluitButton, volgendeButton);
        return buttonBox;
    }

    /**
     *
     * @param names String array namen voor in de togglegroep (deze worden aan de user getoond)
     * @param group Togglegroep waar de togglebuttons bij horen
     * @param toggleArray array van radio buttons voor latere referentie
     * @param selectionIndex index van de default waarde
     * @param title String titel van de radioboxen
     * @param userData = String array met de waarden die het programma ontvangt als het de data opvraagt
     * @return
     */
    private VBox makeToggleGroup(String[] names, ToggleGroup group, RadioButton[] toggleArray, int selectionIndex,
    String title, String[] userData){
        toggleArray = new RadioButton[names.length];
        HBox toggleBox = UtilLib.makeHBox(Pos.CENTER, 10);
        for (int i = 0; i < names.length; i++) {
            RadioButton tempButton = new RadioButton(names[i]);
            tempButton.setToggleGroup(group);
            tempButton.setUserData(userData[i]);
            toggleArray[i] = tempButton;
            toggleBox.getChildren().add(tempButton);
        }
        toggleArray[selectionIndex].setSelected(true);
        VBox mainBox = new VBox(new Label(title), toggleBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);
        return mainBox;
    }

    /**
     * bind alle toggle groeps aan een methode
     *
     * maakt een event aan voor alle togglegroeps dat als de toggle verandert het de informatie gelijk opslaat
     */
    private void bindToggleGroups(){
        this.playerSelection.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (playerSelection.getSelectedToggle() != null){
                    setSelectionVariables();
                }
            }
        });
        this.boardSelection.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (boardSelection.getSelectedToggle() != null){
                    setSelectionVariables();
                }
            }
        });
        this.timeSelection.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (timeSelection.getSelectedToggle() != null){
                    setSelectionVariables();
                }
            }
        });
    }

    /**
     * slaat alle togglegroep data op in de klasse attributen
     */
    private void setSelectionVariables(){
        this.players = Integer.parseInt(this.playerSelection.getSelectedToggle().getUserData().toString());
        this.boardSize = Integer.parseInt(this.boardSelection.getSelectedToggle().getUserData().toString());
        this.seconds = Integer.parseInt(this.timeSelection.getSelectedToggle().getUserData().toString());
    }

    /**
     * start de applicatie
     *
     * @param args string array systeem argumenten
     */
    public static void main(String[] args) {
        launch(args);
    }

}
