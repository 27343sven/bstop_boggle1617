/**
 * Created by sven on 29-Jun-17.
 */
public class BoggleGame {
    int players;
    int currentPlayer;
    Connect connecetion = new Connect();

    public BoggleGame(){

    }

    public void connectDB(String database, String user, String password){
        this.connecetion.setConnection(database, user, password);
    }

    public void testQuery(String query){
        Sql sql = new Sql(this.connecetion.getConnection());
        sql.select(query, true);
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

}
