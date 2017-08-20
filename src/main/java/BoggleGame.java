import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sven on 29-Jun-17.
 *
 * klasse die communiseert met de database via de SQL klasse en bijhoudt welke speler er op dit moment bezig is
 */
public class BoggleGame {
    private int[] players;
    private int currentPlayer = 0;
    private Connect connecetion = new Connect();
    private boolean uniekeWoorden = false;

    /**
     * constructor van BoggleGame
     *
     * doet verder helemaal niks anders dan bestaan, taken van deze Constructor:
     * - niks
     * - helemaal niks
     * - nada
     *
     * @see NullPointerException
     */
    public BoggleGame(){

    }

    /**
     * maakt nieuwe spelers aan
     *
     * haalt de hoogste Id op die de database maakt en maakt op basis hiervan id's aan voor de nieuwe spelers, in de
     * database worden naast de id's de opgegeven naam, datum van aanmaken en tijd van aanmaken ingevuld, vervolgens
     * worden ge gebruikte id's opgeslagen in deze klasse en gereturned
     *
     * @param names String Array met de namen van de spelers
     * @return int array met de id's van de spelers van de database
     * @see Sql
     */
    public int[] addNewPlayers(String[] names){
        Sql sql = new Sql(this.connecetion.getConnection());
        int[] playerIds = new int[names.length];
        int currentId = this.getMaxPlayerid() + 1;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        for (int i = 0; i < names.length; i++) {
            sql.update(String.format("INSERT INTO speler VALUES (%d, '%s', '%s', '%s');", currentId, names[i],
                    dateFormat.format(date), timeFormat.format(date)));
            playerIds[i] = currentId;
            currentId++;
        }
        this.players = playerIds;
        return playerIds;
    }

    /**
     * reset het spel
     *
     * de spelers worden op null gezet en alle opties worden terug gezet naar de originele waarden
     */
    public void reset(){
        this.players = null;
        this.currentPlayer = 0;
        this.uniekeWoorden = false;
    }

    /**
     * update de optie voor unieke woorden
     *
     * update de boolean in de klasse die aangeeft of een woord door meerdere spelers kan worden geraden
     *
     * @param isUniek boolean welke aangeeft of een woord door meerdere speler kan worden geraden
     */
    public void setUniekeWoorden(boolean isUniek){
        this.uniekeWoorden = isUniek;
    }

    /**
     * geeft de geraden woorden van de speler die bezig is
     *
     * vraagt aan de database wat de woorden zijn die de speler al heeft geraden dit geeft een Arralist van alle kolomen
     * met daarin [0]woord [1]score
     *
     * @return ArrayList van een String array met alle geraden woorden
     */
    public ArrayList<String[]> getGeradenWoorden(){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(String.format(
                "SELECT sw.woord, w.score FROM (SELECT * FROM spelerwoord WHERE Speler_Id = %d) sw INNER JOIN " +
                "woord w ON sw.woord = w.woord;", this.players[this.currentPlayer]));
        return sql.getResults();
    }

    /**
     * geeft de speler id's
     *
     * returned een int array met alle speler id's
     *
     * @return int array mer alle speler id's
     */
    public int[] getSpelers(){
        return this.players;
    }

    /**
     * checkt of een speler een core heeft
     *
     * vraagt aan de database of de speler al een keer een woord heeft geraden als dit het geval is returned het true
     * anders false
     *
     * @param playerId int id van de speler waar van moet worden gecontroleerd op deze een score heeft
     * @return boolean die aangeeft of de score bestaat
     */
    public boolean playerScoreExists(int playerId){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(String.format("SELECT 'gevonden!' FROM spelerwoord WHERE Speler_Id = %d LIMIT 1;", playerId));
        return (sql.getRowCount() > 0);
    }

    /**
     * geeft de 5 beste woorden van een speler
     *
     * haalt de 5 beste woorden van een speler op en returned deze
     *
     * @param playerId int id van de speler waaarvan de 5 beste woorden moeten worden opgehaald
     * @return Arraylist van een String Array met de 5 beste woorden en hun scores
     */
    public ArrayList<String[]> getBesteWoorden(int playerId){
        String query = String.format(
                "SELECT sw.woord, w.score FROM (SELECT *  FROM spelerwoord WHERE Speler_Id = %d) sw " +
                        "INNER JOIN woord w ON sw.woord = w.woord ORDER BY w.score DESC LIMIT 5;", playerId
        );
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(query);
        return sql.getResults();
    }

    /**
     * zet de currentSpeler op de volgende speler en geeft aan of het spel voorbij is
     *
     * controleert of er nog een volgende speler is zo ja wordt deze volgende speler de currentSpeler en wordt er true
     * gereturned anders returned het false
     *
     * @return boolean die aangeeft of er nog een beurt moet worden gespeeld
     */
    public boolean nextPlayer(){
        if (this.currentPlayer + 1 < this.players.length){
            this.currentPlayer++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * geeft de naam van de speler die nu bezig is
     *
     * vraagt aan de database wat de naam is van de speler die nu bezig is, als er geen resultaat is wordt de naam
     * unknown
     *
     * @return String met de naam van de speler
     */
    public String getPlayerName(){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(String.format("select naam from speler where id = %d;", this.players[this.currentPlayer]));
        if (sql.getRowCount() > 0){
            return sql.getResults().get(0)[0];
        } else {
            return "<Unknown>";
        }
    }

    /**
     * geeft de naam van een speler op basis van een id
     *
     * vraagt aan de database wat de naam is van de speler met het id, als deze niet bekend is wordt de naam
     * gereturned en anders wordt er unknown gereturned
     *
     * @param playerId int id van de speler waarvan de naam moet worden opgevraagd
     * @return String met de naam van de speler
     */
    public String getPlayerName(int playerId){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(String.format("select naam from speler where id = %d;", playerId));
        if (sql.getRowCount() > 0){
            return sql.getResults().get(0)[0];
        } else {
            return "<Unknown>";
        }
    }

    /**
     * probeert een woord toe te voegen
     *
     * als eert kijk het of het woord als bestaat en of het alleen uit kleine letters bestaat, als een van deze cases
     * waar is wordt heer een message van gereturned, anders wordt het woord toegevoegd
     *
     * @param woord woord die moet worden toegevoegd
     * @return String met daarin het resultaat van het toevoegen van het woord
     */
    public String addWoord(String woord){
        if (this.checkWoord(woord)){
            return "Woord Bestaat al!";
        } else if (!woord.equals(woord.toLowerCase())){
            return "Allen kleine letters!";
        } else {
            Sql sql = new Sql(this.connecetion.getConnection());
            int score = this.calcWoordScore(woord);
            sql.update(String.format("INSERT INTO woord VALUES ('%s', %d)", woord, score));
            return "Opgeslagen!";
        }
    }

    /**
     * probeert een connectie te leggen met de database
     *
     * probeert een connectie te maken en geeft een exeption als dit niet lukt
     *
     * @param database String met de naam van de database
     * @param user String met de username
     * @param password string met het wachtwoord voor de database
     * @throws SQLException Exeption die wordt gegeven als er geen connectie kan worden gelegd met de database
     */
    public void connectDB(String database, String user, String password) throws SQLException{
        this.connecetion.setConnection(database, user, password);
    }

    /**
     * methode om een query te testen
     *
     * test een query en toont de uitkomst in sys.out
     *
     * @param query String query
     */
    public void testQuery(String query){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(query, true);
    }

    /**
     * kijkt of een woord al is geraden
     *
     * kijkt eerst of een woord door meerdere spelers kan worden geraden, als dit niet kan wordt er voor alle spelers
     * gekeken of het woord al is geraden, anders wordt dit alleen gecontroleerd voor de speler die nu bezig is
     *
     * @param woord String woord waarvan moet worden gekeken of het al is geraden
     * @return boolean, true als het woord is geraden, anders false
     */
    private boolean isWoordAlGeraden(String woord){
        if (this.uniekeWoorden){
            for (int id : this.players) {
                if (this.CheckGeradenWoordenPlayer(id, woord)){
                    return true;
                }
            }
            return false;
        } else {
            return this.CheckGeradenWoordenPlayer(players[currentPlayer], woord);
        }
    }

    /**
     * kijkt of een bepaalde speler een bepaald woord al heeft geraden
     *
     * vraagt aan de database of de speler het woord al een keer hefft graden, als er resultaat terug komt returnt het
     * true anders false
     *
     * @param playerId int het id van de speler waarvan moet worden gekeken of deze al een keer een woord heeft greaden
     * @param woord String het woord waarvoor het moet worden gecontroleerd
     * @return boolean true = woord is al geraden door de speler anders false
     */
    private boolean CheckGeradenWoordenPlayer(int playerId, String woord){
        Sql sql = new Sql(this.connecetion.getConnection());
        String query = String.format("SELECT 'gevonden!' FROM (SELECT * FROM spelerwoord WHERE Speler_Id = %d) w " +
                "WHERE w.woord = '%s' LIMIT 1;", playerId , woord);
        sql.select(query);
        if (sql.getRowCount() > 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * geeft de score van de speler(s)
     *
     * het programma loopt door alle spelers om deze toe tevoegen aan de query, dit wordt gedaan met een StringBuilder.
     * hierna returnt het de results
     *
     * @param byId boolean, true = geeft playerId's terug, false = geeft speler namen terug
     * @return Arraylist van String array met de score van de speler(s)
     */
    public ArrayList<String[]> getTotalScore(boolean byId){
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < this.players.length; i++) {
            if (i == 0){
                string.append(String.format(" WHERE Speler_Id = %d", this.players[i]));
            } else {
                string.append(String.format(" OR Speler_Id = %d ", this.players[i]));
            }
        }
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(this.getQuery(byId, string.toString()));
        return sql.getResults();
    }

    /**
     * geeft een query die alle spelers en hun scores terug geeft
     *
     * er wordt gekeken of er id's of namen moeten worden teruggegeven, en op basis hiervan geeft het een query
     *
     * @param byId boolean true = geeft playerId's terug, false = geeft speler namen terug
     * @param playerSql sql code met de spelers waarvan er data moet worden opgevraagd
     * @return String Sql code die de scores van alle speler teruggeeft
     */
    public String getQuery(boolean byId, String playerSql){
        if (byId){
            return String.format(
                    "SELECT t.Speler_Id, SUM(t.score) AS score FROM (SELECT sw.Speler_Id, w.score FROM spelerwoord " +
                            "sw INNER JOIN woord w on w.woord = sw.woord %s) t GROUP BY t.Speler_Id " +
                            "ORDER BY score DESC;",
                    playerSql);

        } else {
            return String.format(
                    "SELECT sp.naam, sc.score FROM (SELECT t.Speler_Id, SUM(t.score) AS score FROM (SELECT " +
                            "sw.Speler_Id, w.score FROM spelerwoord sw INNER JOIN woord w ON w.woord = sw.woord %s) " +
                            "t GROUP BY t.Speler_Id) sc INNER JOIN speler sp ON sp.Id = sc.Speler_Id "+
                            "ORDER BY sc.score DESC;",
                    playerSql);
        }
    }

    /**
     * checkt of een woord punten oplevert
     *
     * er wordt gekeken of het woord bestaat en of het al is geraden, als het woord bestaat en niet al is geraden wordt
     * het woordt bij de currentSpeler toegevoegd en true gereturned anders false
     *
     * @param woord String woord dat moet worden gecontroleerd
     * @return boolean true = geldig woord, false = niet een geldig woord
     */
    public boolean guessWoord(String woord){
        boolean isWoord = this.checkWoord(woord);
        boolean isGeraden = this.isWoordAlGeraden(woord);
        if (isWoord && !isGeraden){
            Sql sql = new Sql(this.connecetion.getConnection());
            sql.update(String.format("INSERT INTO SpelerWoord VALUES (%d, '%s')", this.players[currentPlayer], woord));
        }
        return (isWoord && !isGeraden);
    }

    /**
     * verwijdert alle speler data
     *
     * dit is een functie gebruikt bij het bug-testen, het verwijdert alle data van spelerwoord en speler
     */
    public void clearAllPlayers(){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.update("delete from spelerwoord;");
        sql.update("delete from speler;");
    }

    /**
     * verwijderdt alle data van een speler
     *
     * deze methode is gebruikt om te bug-testen, het verwijdert alle data van een bepaalde speler
     *
     * @param playerId int id van de speler waarvan alle data moet worden verwijderd
     */
    public void removePlayer(int playerId){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.update(String.format("delete from spelerwoord where Speler_Id = %d;", playerId));
        sql.update(String.format("delete from speler where Id = %d;", playerId));
    }

    /**
     * kijkt of een woord bestaat
     *
     * vraagt aan de database om een woord te vinden als het iets terug geeft wordt true megegeven anders false
     *
     * @param woord String woord waarvan moet worden gekeken of het bestaat
     * @return boolean true = woord bestaat, false = woord bestaat niet
     */
    public boolean checkWoord(String woord){
        Sql sql = new Sql(this.connecetion.getConnection());
        String query = String.format("SELECT 'gevonden!' FROM woord WHERE woord = '%s' LIMIT 1;", woord);
        sql.select(query);
        if (sql.getRowCount() > 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * zoekt naar het hoogste id nummer
     *
     * vraagt aan de database wat het hoogste idnummer is als deze niet bestaat geeft het 0
     *
     * @return int hoogste id nummer dat in de database staat
     */
    public int getMaxPlayerid(){
        Sql sql = new Sql(this.connecetion.getConnection());
        String query = "SELECT COALESCE(MAX(id), 0) FROM speler;";
        sql.select(query);
        ArrayList<String[]> results = sql.getResults();
        return Integer.parseInt(results.get(0)[0]);
    }

    /**
     * geeft de score van een woord
     *
     * afhankelijk van de lengte geeft het een score:
     * <5 = 1
     * 5 = 2
     * 6 = 3
     * 7 = 5
     * 7> = 11
     *
     * @param woord String woord waarvan de score moet worden bepaald
     * @return int score van het woord
     */
    private int calcWoordScore(String woord){
        if (woord.length() < 5){
            return 1;
        } else if (woord.length() == 5){
            return 2;
        } else if (woord.length() == 6){
            return 3;
        } else if (woord.length() == 7) {
            return 5;
        } else {
            return 11;
        }
    }

}
