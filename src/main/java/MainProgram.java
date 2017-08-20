import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by sven on 15-May-17.
 *
 * dit is de hoofdklasse die alle andere applicaties aanroept
 *
 * @link https://github.com/27343sven/bstop_boggle1617
 */
public class MainProgram extends Application {
    private double woordAnimationDelay = 0.2;
    private BeginScherm beginScherm = new BeginScherm();
    private BoggleScherm boggleScherm = new BoggleScherm();
    private OptieScherm optieScherm = new OptieScherm();
    private SpelerNaamScherm spelerNaamScherm = new SpelerNaamScherm();
    private WoordenToevoegScherm woordenToevoegScherm = new WoordenToevoegScherm();
    private ScoreScherm scoreScherm = new ScoreScherm();
    private DatabaseExeptionScreen databaseExeptionScreen = new DatabaseExeptionScreen();
    private int boardSize = 5;
    private int buttonSize = 50;
    private int buttonSpacing = 30;
    private int totalSeconds = 200;
    private int seconds = totalSeconds;
    private Timeline time = new Timeline();
    private ArrayList<Point2D> lines = new ArrayList<>();
    private ArrayList<Character> currentWord = new ArrayList<>();
    private boolean play = false;
    private BoggleGame game = new BoggleGame();
    private Stage PrimaryStage;
    private String DBName = "boggleopdracht";
    private String DBUser = "bstop";
    private String DBPass = "opdracht";

    /**
     * maakt de applicatie klaar om gebruikt te worden.
     * de Stage PrimaryStage wordt globaal bekend gemaakt en Applicatie beginScherm wordt opgestart. Voor elk scherm
     * wordt een bind methode aangeroepen die alle events maakt voor de schermen.
     *
     * @param PrimaryStage Stage die standaard wordt meegegeven als de applicatie opgestart.
     */

    @Override
    public void start(Stage PrimaryStage){
        this.PrimaryStage = PrimaryStage;
        PrimaryStage.resizableProperty().setValue(false);
        PrimaryStage.setOnCloseRequest(evt -> {evt.consume();});
        beginScherm.start(this.PrimaryStage);
        this.bindBeginScherm();
        this.bindOptieScherm();
        this.bindSpelerNaamScherm();
        this.bindWoordenToevoegScherm();
        this.bindScoreScherm();
        this.bindDatabaseExeptionScreen();
        this.tryDatabaseConnection();
        PrimaryStage.show();
    }

    /**
     * probeert een connectie te maken met de database.
     *
     * als dit niet lukt wordt databaseExeptionScherm geopend zodat de gebruiker de gegevens voor de database kan
     * controleren/aanpassen
     *
     * @see DatabaseExeptionScreen
     */
    private void tryDatabaseConnection(){
        try {
            this.game.connectDB(this.DBName, this.DBUser, this.DBPass);
        } catch (SQLException e) {
            this.databaseExeptionScreen.start(this.PrimaryStage, this.DBName, this.DBUser, this.DBPass);
        }
    }

    /**
     * bind alle events voor scoreScherm.
     *
     * deze methode wordt alleen aan het begin van het programma aangeroepen. afsluitButton sluit nu de Applicatie
     * af en menuButton laad het optieScherm
     *
     * @see ScoreScherm
     */
    private void bindScoreScherm(){
        this.scoreScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.scoreScherm.menuButton.setOnAction(e -> this.onEndMenuButton());
    }

    /**
     * bind alle events voor beginScherm
     *
     * bind alle buttons op het beginscherm zodat ze de correcte Applicaties aanroepen, met uitzondering van de afsluit
     * knop die de applicatie afsluit
     *
     * @see BeginScherm
     */
    private void bindBeginScherm(){
        this.beginScherm.spelenButton.setOnAction(e -> this.optieScherm.start(this.PrimaryStage));
        this.beginScherm.woordToevoegButton.setOnAction(e -> this.woordenToevoegScherm.start(this.PrimaryStage));
        this.beginScherm.afsluitButton.setOnAction(e -> Platform.exit());
    }

    /**
     * bind alle event van het woordenToeveoeg scherm
     *
     * zorgt ervoor dat de juiste methoden worden aangeroepen voor elk van de buttons op het woordenToevoegScherm, met
     * uitzondering van de afsluitButton welke de applicatie afsluit
     *
     * @see WoordenToevoegScherm
     */
    private void bindWoordenToevoegScherm(){
        this.woordenToevoegScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.woordenToevoegScherm.terugButton.setOnAction(e -> this.beginScherm.start(this.PrimaryStage));
        this.woordenToevoegScherm.toevoegButton.setOnAction(e -> this.onToevoegButton());
    }

    /**
     * bind alle events van het boggleScherm
     *
     * alle BoggleButtons die op het bogglescherm staan krijgen events mee als er op wordt geclickt, als de muis is
     * ingedrukt en als de muis wordt losgelaten. Deze events zijn voor het maken van woorden in het spel. daarnaast
     * wordt er ook een event voor het loslaten van de muis gebind aan de scene zodat de juist methode ook wordt
     * aangeroepen als de muis zich niet bevind op een button. de start- en opgeef knoppen worden worden naar de juiste
     * methoden verwezen
     *
     * @see BoggleButton
     * @see BoggleScherm
     */
    private void bindBoggleScherm(){
        for (BoggleButton[] buttonList: this.boggleScherm.BoggleArray) {
            for (BoggleButton x: buttonList) {
                x.setOnMouseDragReleased(e -> this.onRelease());
                x.setOnMouseDragged(this.getMouseEvent());
                x.setOnMouseDragOver(this.getMouseDragEvent());
            }
        }
        this.boggleScherm.scene.setOnMouseDragReleased(e -> this.onRelease());
        this.boggleScherm.startTimer.setOnAction(e -> this.doTime());
        this.boggleScherm.opgeefButton.setOnAction(e -> this.resetGameBoard());
        this.boggleScherm.opgeefButton.setDisable(true);
        this.setTimerLabel();
        this.updateSpeler();
    }

    /**
     * bind alle events van optieScherm
     *
     * alle buttons worden naar de juiste methoden verwezen met uitzondering van de afsluit knop welke de Applicatie
     * afsluit
     *
     * @see OptieScherm
     */
    private void bindOptieScherm(){
        this.optieScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.optieScherm.volgendeButton.setOnAction(e -> this.onVolgendeButton());
        this.optieScherm.menuButton.setOnAction(e -> this.beginScherm.start(this.PrimaryStage));
        this.optieScherm.helpButton.setOnAction(e -> this.onHelpButton());
    }

    /**
     * bind alle events van spelerNaamScherm
     *
     * bind alle knoppen van spelerNaamScherm aan de juiste methode met uitzondering van de afsluitknop welke
     * de applicatie afsluit
     *
     * @see SpelerNaamScherm
     */
    private void bindSpelerNaamScherm(){
        this.spelerNaamScherm.afsluitButton.setOnAction(e -> Platform.exit());
        this.spelerNaamScherm.startButton.setOnAction(e -> this.onStartButton());
        this.spelerNaamScherm.terugButton.setOnAction(e -> this.optieScherm.start(this.PrimaryStage));
    }

    /**
     * bind alle events van databaseexeptionScreen
     *
     * bind alle knoppen van databaseExeptionScreen aan de juiste methoden met uitzondering van de afsluitknop welke
     * de applicatie afsluit
     *
     * @see DatabaseExeptionScreen
     */
    private void bindDatabaseExeptionScreen(){
        this.databaseExeptionScreen.afsluitButton.setOnAction(e -> Platform.exit());
        this.databaseExeptionScreen.connectButton.setOnAction(e -> this.onConnectAgainButton());
    }

    /**
     * slaat de aangepaste gegevens voor het verbinden met de database op probeert het opnieuw
     *
     * dit is een event van de databaseExeptionScreen, deze slaat de gegevens die zijn ingevuld op dit scherm op
     * in de attributen van deze klasse en probeert opnieuw een verbinding te maken met de database
     *
     * @see DatabaseExeptionScreen
     */
    private void onConnectAgainButton(){
        this.DBName = this.databaseExeptionScreen.databaseData[0].getText();
        this.DBUser = this.databaseExeptionScreen.databaseData[1].getText();
        this.DBPass = this.databaseExeptionScreen.databaseData[2].getText();
        this.beginScherm.start(this.PrimaryStage);
        this.tryDatabaseConnection();
    }

    /**
     * laat een popup zien
     *
     * maakt een nieuwe Stage met darin een meegegeven container met Nodes, door de methode showAndWait() moet deze
     * Stage eerst worden afgesloten voordat de gebruiker verder kan met de normale Applicatie.
     *
     * @param window een container van Nodes(bijv. VBox, HBox enz.) welke getoond wordt in de popup
     * @param width een double met de breete van de popup
     * @param height een souble met de hoogte van de popup
     */
    private void showDialog(Parent window, double width, double height){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(this.PrimaryStage);
        Scene dialogScene = new Scene(window, width, height);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * probeert een woord toe te voegen aan de database
     *
     * het opgegeven woord wordt opgehaald van het woordenToevoegScherm en meegegeven aan boggleGame, deze geeft een
     * string mee welke aangeeft of het woord is toegevoegd
     *
     * @see WoordenToevoegScherm
     * @see BoggleGame#addWoord(String)
     */
    private void onToevoegButton(){
        this.woordenToevoegScherm.statusLabel.setText(this.game.addWoord(this.woordenToevoegScherm.text.getText()));
        this.woordenToevoegScherm.text.setText("");
    }

    /**
     * reset het spel na het einde
     *
     * alle waarden van het spel worden naar de beginwaarden teruggezet en het beginscherm wordt getoond
     *
     * @see BeginScherm
     * @see BoggleGame#reset()
     */
    private void onEndMenuButton(){
        this.game.reset();
        this.beginScherm.start(this.PrimaryStage);
    }

    /**
     * maakt het spelscherm naar de ingevoerde opties en toont deze
     *
     * in de database worden er nieuwe spelers aangemaakt met de ingevoerde namen, hierdoor zijn de spelers ook gelijk
     * bekend. het bord wordt naar de juiste grote gemaakt (4x4 of 5x5), en het correcte aantal seconden wordt getoond.
     * het boggle scherm wordt getoond en hierna worden pas de events gebonden. dit is omdat de grote van het spelbord
     * verschilt. aan het spel wordt er doorgegeven of een woord door meerdere spelers kan worden geraden. alle letters
     * op het speelscherm worden geraden en de juiste speler en geraden worden worden op het scherm getoond
     *
     * @see BoggleScherm
     * @see OptieScherm
     * @see BoggleScherm#setLettersHidden(boolean)
     * @see BoggleGame#addNewPlayers(String[])
     * @see BoggleGame#setUniekeWoorden(boolean)
     */
    private void onStartButton(){
        String[] names = new String[this.optieScherm.players];
        for (int i = 0; i < this.spelerNaamScherm.textFields.length; i++) {
            names[i] = this.spelerNaamScherm.textFields[i].getText();
        }
        this.game.addNewPlayers(names);
        this.boardSize = this.optieScherm.boardSize;
        this.totalSeconds = this.optieScherm.seconds;
        this.seconds = this.optieScherm.seconds;
        boggleScherm.start(this.PrimaryStage, this.boardSize, this.buttonSize, this.buttonSpacing, 900, 600);
        this.bindBoggleScherm();
        this.game.setUniekeWoorden(this.spelerNaamScherm.uniekCheck.isSelected());
        boggleScherm.setLettersHidden(true);
        this.updateSpeler();
        this.updateGeradenWoorden();
    }

    /**
     * opent het spelernaamScherm
     *
     * opent het spelerNaamScherm en laat de juiste hoeveelheid textboxen zien afhankelijk van het aantal spelers. deze
     * methode wordt aangeroepen nadat de gebruiker op start heeft gedrukt in het optieScherm
     *
     * @see SpelerNaamScherm
     */
    private void onVolgendeButton(){
        this.spelerNaamScherm.start(this.PrimaryStage);
        this.spelerNaamScherm.vulScherm(this.optieScherm.players);
    }

    /**
     * laat een popup zien met informatie over alle opties
     *
     * het verkrijgt de text van een methode en laat deze zien in een popup
     *
     * @see MainProgram#optieDialogText()
     * @see MainProgram#showDialog(Parent, double, double)
     */
    private void onHelpButton(){
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(this.optieDialogText());
        this.showDialog(dialogVbox, 700, 200);
    }

    /**
     * geef de uitleg van de opties van het optiescherm terug
     *
     * geeft de uitleg als een Text mee
     *
     * @return Text die uitleg geeft over de opties van het optieScherm
     */
    private Text optieDialogText(){
        return new Text(
                "Spelers: \t\tselecteer het aantal spelers dat mee speelt, als er 2 of meer spelers\n" + "" +
                        "\t\t\tmeedoen is er de optie om bij het invoeren van de namen te selecteeren\n" +
                        "\t\t\tof een woord door meerdere spelers kan worden geraden of door een.\n\n"+
                        "Bord grote: \tde grote van het spelbord, 4 bij 4 (16) tegels of 5 bij 5 (25) tegels.\n\n" +
                        "Tijd: \t\t\tde tijd die iedere speler krijgt om woorden te maken."
        );
    }

    /**
     * start de timer van het spel
     *
     * start een Timeline die elke seconde de tijd op het speelscherm aanpast, en kijkt of de tijd al voorbij is
     * als dit het geval is reset het het spelbord. hiernaast worden de letter op het spelbord getoond en de opgeef
     * knop beschikbaar gemaakt
     *
     * @see MainProgram#resetGameBoard()
     * @see BoggleScherm#setLettersHidden(boolean)
     */
    private void doTime(){
        this.time.stop();
        this.time = new Timeline();
        this.seconds = this.totalSeconds;
        this.time.setCycleCount(this.seconds + 1);
        if (this.time == null){
            this.time.stop();
        }
        this.seconds = this.totalSeconds;
        KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTimerLabel();
                if (seconds == 0) {
                    resetGameBoard();
                } else {
                    seconds--;
                }
            }
        });
        this.time.getKeyFrames().add(frame);
        this.time.playFromStart();
        this.play = true;
        this.boggleScherm.setLettersHidden(false);
        this.boggleScherm.startTimer.setDisable(true);
        this.boggleScherm.opgeefButton.setDisable(false);
    }

    /**
     * reset het spelbord nadat een speler zijn beurt heeft afgemaakt
     *
     * zorgt ervoor dat de startbutton gebruikt kan worden en de opgeefbutton niet meer, verder haalt het alle lijnen
     * van het scherm, verbergt de letters, en update de labels voor de speler, score, geraden woorden en current woord.
     * als de laatste speler geweest is wordt het scorescherm aangeroepen
     *
     * @see BoggleScherm
     * @see BoggleGame#nextPlayer()
     */
    private void resetGameBoard(){
        play = false;
        boggleScherm.startTimer.setDisable(false);
        boggleScherm.opgeefButton.setDisable(true);
        lines.clear();
        boggleScherm.setLettersHidden(true);
        boggleScherm.lineGroup.getChildren().clear();
        resetWoordLabel();
        seconds = totalSeconds;
        setTimerLabel();
        if (!this.game.nextPlayer()){
            this.endGame();
        } else {
            this.updateGeradenWoorden();
            this.updateSpeler();
        }
        this.currentWord.clear();
    }

    /**
     * laat het een score Scherm zien afhankelijk van het aantal spelers
     *
     * start het scoreScherm en vult dit scherm in, als er maar een speler meedoet worden de beste woorden getoond,
     * anders worden de scores van alle spelers weergeven
     *
     * @see MainProgram#endSinglePlayer()
     * @see MainProgram#endMultiPlayer()
     */
    private void endGame(){
        this.scoreScherm.start(this.PrimaryStage);
        if (this.game.getSpelers().length > 1) {
            this.endMultiPlayer();
        } else {
            this.endSinglePlayer();
        }
    }

    /**
     * vult het scoreScherm met informatie van meerder spelers
     *
     * deze methode wordt alleen aangeroepen als er meer dan een speler het spel speelt. als eerst wordt er een score
     * lijst opgehaald van alle spelers die ten minste een woord hebben ingevuld en een score, dan moet er speciaal
     * worden gekeken welke spelers er geen woord hebben ingevuld (deze kan de database niet zien) hiervan wordt de
     * naam opgehaald en een score van 0 ingevuld, de winnaar is de speler met het meeste punten.
     *
     * @see BoggleGame#getTotalScore(boolean)
     * @see BoggleGame#getSpelers()
     * @see ScoreScherm
     */
    private void endMultiPlayer(){
        ArrayList<String[]> scores = this.game.getTotalScore(true);
        List<Integer> idQueue = IntStream.of(this.game.getSpelers()).boxed().collect(Collectors.toList());
        StringBuilder text = new StringBuilder();
        text.append(String.format("%-10s %s\n", "naam", "score"));
        for (String[] spelerScore : scores) {
            text.append(String.format("%-10s %s\n", this.game.getPlayerName(
                    Integer.parseInt(spelerScore[0])),
                    spelerScore[1]));
            idQueue.remove(new Integer(Integer.parseInt(spelerScore[0])));
        }
        for (Integer id : idQueue) {
            text.append(String.format("%-10s 0\n", this.game.getPlayerName(id)));
        }
        this.scoreScherm.textlabel.setText(text.toString());
    }

    /**
     * vult het scoreScherm met informatie van een speler
     *
     * vult de score in van de speler en toon de 5 beste woorden, als de speler niks heeft ingevuld komt er simpelweg
     * "geen woorden" te staan
     *
     * @see ScoreScherm
     * @see BoggleGame#playerScoreExists(int)
     * @see BoggleGame#getPlayerName()
     * @see BoggleGame#getTotalScore(boolean)
     */
    private void endSinglePlayer(){
        String player = this.game.getPlayerName(this.game.getSpelers()[0]);
        int score = 0;
        if (this.game.playerScoreExists(this.game.getSpelers()[0])){
            ArrayList<String[]> besteWoorden = this.game.getBesteWoorden(this.game.getSpelers()[0]);
            StringBuilder text = new StringBuilder();
            text.append(String.format("%-25s %s\n", "woord", "punten"));
            for (String[] woordScore : besteWoorden) {
                text.append(String.format("%-25s %s\n", woordScore[0], woordScore[1]));
            }
            this.scoreScherm.textlabel.setText(text.toString());
            score = Integer.parseInt(this.game.getTotalScore(true).get(0)[1]);
        } else {
            this.scoreScherm.textlabel.setText("<geen woorden>");
        }
        this.scoreScherm.mainLabel.setText(String.format("Score %s: %d\n",player, score));
    }

    /**
     * update het tijdslabel met de tijd geformatterd naar m:ss
     *
     * eerst woordt het aantal minuten en seconden berekend en ingevoerd in het tijdslabel
     *
     * @see BoggleScherm
     */
    private void setTimerLabel(){
        int min = this.seconds / 60;
        String sec = String.format("%02d", this.seconds % 60);
        this.boggleScherm.timer.setText(min + ":" + sec);
    }

    /**
     * maakt een MouseDragEvent
     *
     * geeft een mouseDragEvent voor een Bogglebutton, deze geeft de gebruikte button mee aan een methode
     *
     * @return een MouseDragEvent die een methode aanroept
     * @see BoggleButton
     */
    private EventHandler<MouseDragEvent> getMouseDragEvent(){
        return new EventHandler<MouseDragEvent>(){
            @Override
            public void handle(MouseDragEvent event) {
                BoggleButton currentButton = ((BoggleButton) event.getSource());
                mouseOverLetterEvent(currentButton);
            }
        };
    }

    /**
     * maakt een MouseEvent
     *
     * geeft een mouseEvent voor een Bogglebutton, deze geeft de gebruikte button mee aan een methode
     *
     * @return een MouseEvent die een methode aanroept
     * @see BoggleButton
     */
    private EventHandler<MouseEvent> getMouseEvent(){
        EventHandler<MouseEvent> test = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                BoggleButton currentButton = ((BoggleButton) event.getSource());
                mouseOverLetterEvent(currentButton);
            }
        };
        return test;
    }

    /**
     * slaat de letter op die op de knop staat
     *
     * als het spel bezig is wordt er voor de knop gekeken of die knop een legale zet is, dit houdt in dat het alleen
     * een knop mag zijn die naast de vorige knop ligt en die nog niet is gebruikt, als dit zo is wordt er een lijn
     * retekend van de vorige knop naar de nieuwe knop en de letter van die knop wordt toegevoegd aan currentwoord.
     *
     * @param currentButton een BoggleButton waarvan moet worden gekeken of het een legale zet is.
     * @see MainProgram#inLines(Point2D)
     * @see BoggleScherm
     */
    private void mouseOverLetterEvent(BoggleButton currentButton){
        if (this.play) {
            currentButton.setDisabled();
            Point2D tempPoint = new Point2D(
                    this.buttonSize * currentButton.getLocation().getX() +
                            this.buttonSpacing * currentButton.getLocation().getX() + this.buttonSize / 2,
                    this.buttonSize * currentButton.getLocation().getY() +
                            this.buttonSpacing * currentButton.getLocation().getY() + this.buttonSize / 2
            );
            if (!this.inLines(tempPoint)) {
                this.lines.add(tempPoint);
                this.currentWord.add(currentButton.getLetter());
                this.updateWoordLabel();
                this.drawLines();
            }
        }
    }

    /**
     * update het het label waar het gemaakte woord wordt getoond
     *
     * haalt alle karacters op die zijn ingevoerd door er met de muis overheen te gaan en toont deze in het woordlabel
     * op het spelbord
     *
     * @see BoggleScherm
     */
    private void updateWoordLabel(){
        String woord = "";
        for (int i = 0; i < this.currentWord.size(); i++) {
            woord += this.currentWord.get(i);
        }
        this.boggleScherm.woordLabel.setText(woord);
    }

    /**
     * update het label dat angeft welke speler er bezig is
     *
     * vraagt aan het spel welke speler er bezig is en zet dit in een label van het speelScherm
     *
     * @see BoggleScherm
     * @see BoggleGame#getPlayerName()
     */
    private void updateSpeler(){
        String speler = this.game.getPlayerName();
        this.boggleScherm.speler.setText(String.format("Speler: %s", speler));
    }

    /**
     * update het scherm met alle geraden woorden
     *
     * als eerst vraagt het aan het spel welke worden er al zijn geraden door de speler die nu bezig is, deze worden
     * onder eelkaar gezet met de score door middel van een StringBuffer, de style van dez box wordt op monospace gezet
     * zodat alle scores en letters netjes onder elkaar staan
     *
     * @see BoggleGame#getGeradenWoorden()
     * @see BoggleScherm
     */
    private void updateGeradenWoorden(){
        int score = 0;
        StringBuffer woordenText = new StringBuffer();
        ArrayList<String[]> geradenWoorden = this.game.getGeradenWoorden();
        for (int i = 0; i < geradenWoorden.size() ; i++) {
            woordenText.append(String.format("%-24s %s\n", geradenWoorden.get(i)[0], geradenWoorden.get(i)[1]));
            score += Integer.parseInt(geradenWoorden.get(i)[1]);
        }
        Text text = new Text(woordenText.toString());
        text.setStyle("-fx-font-family: monospace");
        this.boggleScherm.geradenWoordenPane.setContent(text);
        this.boggleScherm.totalScore.setText(String.format("Score: %d", score));
    }

    /**
     * kijkt of het ingevoerde woord punten oplevert
     *
     * deze method wordt aangeroepen als de gebruiker de muis loslaat. als het spel bezig is haalt het alle lijntjes
     * van het scherm en haalt alle letters op die tot nutoe zijn geraden, een methode wordt aangeroepen om de kleur
     * can het wordt tijdelijk te veranderen naar rood(fout woord) of groen(goed woord) en alle buttons worden weer
     * beschikbaar gesteld
     *
     * @see MainProgram#animateOnWoordRelease(String)
     * @see MainProgram#updateGeradenWoorden()
     */
    private void onRelease(){
        if (play) {
            this.lines.clear();
            this.boggleScherm.lineGroup.getChildren().clear();
            String woord = "";
            for (int i = 0; i < this.currentWord.size(); i++) {
                woord += this.currentWord.get(i);
            }
            this.animateOnWoordRelease(woord);
            this.updateGeradenWoorden();
            this.currentWord.clear();
            for (BoggleButton[] i : this.boggleScherm.BoggleArray) {
                for (BoggleButton j : i) {
                    try {
                        j.setDisabled();
                    } catch (NullPointerException e) {
                        System.out.println("Kan geen buttons vinden!");
                    }
                }
            }
        }
    }

    /**
     * reset het label met het geraden woord
     *
     * maakt de kleur van het label zwart en zet er een lege string in
     */
    private void resetWoordLabel(){
        boggleScherm.woordLabel.setTextFill(Color.BLACK);
        boggleScherm.woordLabel.setText("");
    }

    /**
     * kijkt of het woord goed is en laat daar een bijbehorend kleurtje bij zien
     *
     * als eerste wordt er gekeken of het woord goed is, dit voegt hem ook gelijk toe aan de geraden woorden in de
     * database als dit het geval is. de kleur van het geraden woordLabel wordt gekleurd naar de uitkomst en een
     * er wordt een Timeline gemaakt die het label weer reset na een bepaalde tijd
     *
     * @param woord String die gecontroleerd moet worden
     * @see BoggleGame#guessWoord(String)
     */
    private void animateOnWoordRelease(String woord){
        if (this.game.guessWoord(woord)){
            this.boggleScherm.woordLabel.setTextFill(Color.LIGHTGREEN);
        } else {
            this.boggleScherm.woordLabel.setTextFill(Color.RED);
        }
        Timeline time = new Timeline(new KeyFrame(Duration.seconds(woordAnimationDelay), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetWoordLabel();
            }
        }));
        time.setCycleCount(1);
        time.play();
    }

    /**
     * kijkt of er een legale zet is gmaakt
     *
     * als eerst kijkt het of deze locatie al een keer is geraden, hierna wordt er gekeken of de afstand tussen de
     * de vorige locatie langer is dan sqrt(2*(buttonSize + buttonspacing)^2) + 10 (de afstand tussen twee diagonale
     * knoppen + 10) als geen van deze statements waar is wordt er false gereturned anders true
     *
     * @param test een Point2D die aangeeft waar de knop zich bevind op het scherm vanaf rechtsboven (0, 0)
     * @return een boolean die aangeeft af het een legale zet was
     */
    private boolean inLines(Point2D test){
        for (int i = 0; i < this.lines.size(); i++) {
            if (this.lines.get(i).getX() == test.getX() && this.lines.get(i).getY() == test.getY()){
                return true;
            }
        }
        double maxLineLength = 10 + sqrt(2*pow(this.buttonSize + this.buttonSpacing, 2));
        if (this.lines.size() != 0 && this.lines.get(this.lines.size() - 1).subtract(test).magnitude() > maxLineLength){
            return true;
        }
        return false;
    }

    /**
     * tekent lijntjes tussen de geraden letters
     *
     * kijkt of er al een lijn getekend kan worden (minimaal twee punten nodig) als dit het geval is loopt het door alle
     * punten heen en tekend er lijnen tussen
     */
    private void drawLines(){
        if (this.lines.size() > 1) {
            int index = this.lines.size() - 1;
            Line tempLine = new Line(
                    this.lines.get(index).getX(), this.lines.get(index).getY(),
                    this.lines.get(index - 1).getX(), this.lines.get(index - 1).getY()
            );
            this.boggleScherm.lineGroup.getChildren().add(tempLine);
        }

    }

    /**
     * start de applicatie
     *
     * start de applicatie
     *
     * @param args String Array met alle systeem argumenten
     */

    public static void main(String[] args) {
        launch();
    }

}
