import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgresConnector {
    private String dbname;
    private String user;
    private String password;
    private String host;
    private int port;
    private Connection connection;

    public PostgresConnector(String dbname, String user, String password, String host, int port) {
        this.dbname = dbname;
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
        this.connection = null;
    }

    public void connect() {
        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Unable to connect to the database.");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeQuery(String query, Object... values) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
            System.out.println("Query executed successfully.");
        } catch (SQLException e) {
            System.out.println("Error executing the query.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
            return;
        }
        PostgresConnector connector = new PostgresConnector("test-playground", "postgres",  "12345678", "localhost", 5432);
        connector.connect();

        // Example insert query
        // String insertQuery = "INSERT INTO your_table (column1, column2) VALUES (?, ?)";
        // connector.executeQuery(insertQuery, "value1", "value2");

        connector.disconnect();
    }
}
