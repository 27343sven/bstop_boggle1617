import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * klasse die een connectie maakt met de database
 */
public class Connect {

    private Connection conn;

    /**
     * constructor van de klasse die verder niks doet
     */
    public Connect(){
    }

    /**
     * maakt een connectie met de database
     *
     * maakt een connectie met de database via de JDBC drivers en probeert te verbinden
     *
     * @param database String naam van de database
     * @param user String naam van de gebruiker
     * @param password String wachtwoord van de gebruiker
     * @throws SQLException geeft een SQLExeption als er geen connectie kan worden gemaakt met de database
     */
    public void setConnection(String database, String user, String password) throws SQLException{
        String format = String.format("jdbc:postgresql://localhost/%s", database);
        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);
        this.conn = DriverManager.getConnection(format, props);
        this.conn.setAutoCommit(true);
    }

    /**
     * geeft de connectie
     *
     * @return Connection geeft de connectie
     */
    public Connection getConnection(){
        return this.conn;
    }

    /**
     * sluit de connectie met de database
     *
     * probeert de connectie te sluiten en print een error message als dit misslukt
     */
    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
