import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * klasse die queries uitvoert op een database
 */

class Sql {

    private Connection conn;
    private int rows;
    private ResultSet results;
    private ArrayList<String[]> resultArray = new ArrayList<>();

    /**
     * constructor van Sql
     *
     * slaat de connectie op naar de database
     *
     * @param conn Connection naar de database
     */
    public Sql(Connection conn) {
        this.conn = conn;
    }

    /**
     * methode voor een update query
     *
     * deze methode voert een query uit zonder de resultaten op te slaan
     *
     * @param query String met de query die moet worden uitgevoerd
     */
    public void update(String query) {
        try {
            this.conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * methode voor een select query
     *
     * voert een query uit en slaat deze resultaten op in arraylist van string arrays. als debug mode aan staat wordt
     * het resultaat uitgeprint naar system.out
     *
     * @param query String met de query die moet worden uitgevoerd
     * @param debug debugmode true = debug aan, false = debug uit
     */
    public void select(String query, boolean debug) {
        try {
            this.results = this.conn.prepareStatement(query).executeQuery();
            saveResults();
            if (debug){
                printResults();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * methode voor een select query
     *
     * voert een query uit en slaat deze resultaten op in arraylist van string arrays.
     *
     * @param query String met de query die moet worden uitgevoerd
     */
    public void select(String query) {
        try {
            this.results = this.conn.prepareStatement(query).executeQuery();
            this.saveResults();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * geeft het aantal rijen dat het resultaat bevat
     *
     * @return int het aantal rijen in het resultaat
     */
    public int getRowCount(){
        return this.rows;
    }

    /**
     * geeft de arraylist van String arrays met het resultaat
     *
     * @return ArrayList van String Arrays met het resultaat
     */
    public ArrayList<String[]> getResults(){
        return this.resultArray;
    }

    /**
     * print de resultaten
     *
     * haalt eerst de kolomnamen op en print deze met een | tussen elke waarde, daatna print het alle resultaten
     *
     * @throws SQLException
     */
    private void printResults() throws SQLException {
        ResultSetMetaData metaData = this.results.getMetaData();
        int columns = metaData.getColumnCount();
        for (int i = 0; i < columns; i++) {
            System.out.print(metaData.getColumnName(i+1) + "|");
        }
        System.out.println();
        for (int i = 0; i < this.resultArray.size(); i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(this.resultArray.get(i)[j] + "|");
            }
            System.out.println();
        }
    }

    /**
     * slaat de resultaten op in een Arralist van String arrays
     *
     * loopt door de resultaten heen en slaat deze op in een Arraylist van String arrays, en telt ook hoeveel rijen
     * er in het resultaat zitten
     *
     * @throws SQLException
     */
    private void saveResults() throws SQLException {
        ResultSetMetaData metaData = this.results.getMetaData();
        int columns = metaData.getColumnCount();
        int currrentRow = 0;
        while (this.results.next()){
            String[] tempRow = new String[columns];
            for (int i = 1; i <= columns; i++) {
                tempRow[i-1] = this.results.getString(metaData.getColumnName(i));
            }
            this.resultArray.add(tempRow);
            currrentRow++;
        }
        this.rows = currrentRow;
    }
}