import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sven on 29-Jun-17.
 */
public class BoggleGame {
    private int[] players;
    private int currentPlayer = 0;
    private Connect connecetion = new Connect();
    private boolean uniekeWoorden = false;

    public BoggleGame(){

    }


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

    public void setUniekeWoorden(boolean isUniek){
        this.uniekeWoorden = isUniek;
    }

    public ArrayList<String[]> getGeradenWoorden(){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(String.format(
                "SELECT sw.woord, w.score FROM (SELECT * FROM spelerwoord WHERE Speler_Id = %d) sw INNER JOIN " +
                "woord w ON sw.woord = w.woord;", this.players[this.currentPlayer]));
        return sql.getResults();
    }

    public void play(){
        this.currentPlayer = 0;
    }

    public int[] getSpelers(){
        return this.players;
    }

    public ArrayList<String[]> getBesteWoorden(int playerId){
        String query = String.format(
                "SELECT sw.woord, w.score FROM (SELECT *  FROM spelerwoord WHERE Speler_Id = %d) sw " +
                        "INNER JOIN woord w ON sw.woord = w.woord ORDER BY w.score DESC LIMIT 5;", playerId
        );
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(query);
        return sql.getResults();
    }


    public boolean nextPlayer(){
        if (this.currentPlayer + 1 < this.players.length){
            this.currentPlayer++;
            return true;
        } else {
            return false;
        }
    }

    public String getPlayerName(){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(String.format("select naam from speler where id = %d;", this.players[this.currentPlayer]));
        if (sql.getRowCount() > 0){
            return sql.getResults().get(0)[0];
        } else {
            return "<Unknown>";
        }
    }

    public String getPlayerName(int playerId){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(String.format("select naam from speler where id = %d;", playerId));
        if (sql.getRowCount() > 0){
            return sql.getResults().get(0)[0];
        } else {
            return "<Unknown>";
        }
    }

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

    public void connectDB(String database, String user, String password){
        this.connecetion.setConnection(database, user, password);
    }

    public void testQuery(String query){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(query, true);
    }

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

    public ArrayList<String[]> getTotalScore(){
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < this.players.length; i++) {
            if (i == 0){
                string.append(String.format(" WHERE Speler_Id = %d", this.players[i]));
            } else {
                string.append(String.format(" OR Speler_Id = %d ", this.players[i]));
            }
        }
        String query = String.format(
                "SELECT sp.naam, sc.score FROM (SELECT t.Speler_Id, SUM(t.score) AS score FROM (SELECT sw.Speler_Id, " +
                        "w.score FROM spelerwoord sw INNER JOIN woord w ON w.woord = sw.woord %s) t GROUP BY " +
                        "t.Speler_Id ORDER BY score DESC) sc INNER JOIN speler sp ON sp.Id = sc.Speler_Id;",
                string.toString());
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(query);
        return sql.getResults();
    }

    public boolean guessWoord(String woord){
        boolean isWoord = this.checkWoord(woord);
        boolean isGeraden = this.isWoordAlGeraden(woord);
        if (isWoord && !isGeraden){
            Sql sql = new Sql(this.connecetion.getConnection());
            sql.update(String.format("INSERT INTO SpelerWoord VALUES (%d, '%s')", this.players[currentPlayer], woord));
        }
        return (isWoord && !isGeraden);
    }

    public void clearAllPlayers(){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.update("delete from spelerwoord;");
        sql.update("delete from speler;");
    }

    public void removePlayer(int playerId){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.update(String.format("delete from spelerwoord where Speler_Id = %d;", playerId));
        sql.update(String.format("delete from speler where Id = %d;", playerId));
    }

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

    public int getMaxPlayerid(){
        Sql sql = new Sql(this.connecetion.getConnection());
        String query = "SELECT COALESCE(MAX(id), 0) FROM speler;";
        sql.select(query);
        ArrayList<String[]> results = sql.getResults();
        return Integer.parseInt(results.get(0)[0]);
    }

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
