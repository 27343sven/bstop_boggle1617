import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

class Sql {

    private Connection conn;
    private int rows;
    private ResultSet results;
    private ArrayList<String[]> resultArray = new ArrayList<>();

    public Sql(Connection conn) {
        this.conn = conn;
    }

    public void update(String query) {
        try {
            this.conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void select(String query, boolean debug) {
        try {
            this.results = this.conn.prepareStatement(query).executeQuery();
            getResults();
            if (debug){
                printResults();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void select(String query) {
        try {
            this.results = this.conn.prepareStatement(query).executeQuery();
            getResults();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getRowCount(){
        return this.rows;
    }

    private void reset(){
        this.rows = 0;
        this.resultArray.clear();
    }

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

    private void getResults() throws SQLException {
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