import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.io.FileWriter;
import java.io.PrintWriter;
public class CsvToPostgres {

    public static void main(String[] args) {
        // Specify the CSV file name
        String csvFileName = "Data/test.csv";

        // Specify your PostgreSQL connection details
        String jdbcUrl = "jdbc:postgresql://localhost:5432/test-playground";
        String user = "postgres";
        String password = "12345678";
        
        // Specify the table name
        String tableName = "test_table";

        // Measure execution time
        long startTime = System.currentTimeMillis();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        BufferedReader reader = new BufferedReader(new FileReader(csvFileName))) {

       // Read the header and skip it
       reader.readLine();
       ResultSetMetaData metaData = connection.prepareStatement("SELECT * FROM " + tableName + " LIMIT 1")
       .executeQuery().getMetaData();
       // Assuming the rest of the lines are data rows
       String line;
       int linesCount = 0;

       while ((line = reader.readLine()) != null) {
           linesCount++;
           String[] values = line.split(",");
           String placeholders = getPlaceholders(values.length);
           // Build the dynamic INSERT INTO query
           String insertQuery = String.format("INSERT INTO %s VALUES (%s)", tableName, placeholders);

           try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (int i = 0; i < values.length; i++) {
                // Determine the data type of the column and set the value accordingly
                int columnType = metaData.getColumnType(i + 1);
                switch (columnType) {
                    case Types.INTEGER:
                        preparedStatement.setInt(i + 1, Integer.parseInt(values[i]));
                        break;
                    case Types.FLOAT:
                        preparedStatement.setFloat(i + 1, Float.parseFloat(values[i]));
                        break;
                    case Types.DOUBLE:
                        preparedStatement.setDouble(i + 1, Double.parseDouble(values[i]));
                        break;
                    // Add more cases for other data types as needed
                    default:
                        preparedStatement.setString(i + 1, values[i]);
                        break;
                        
                }
            }
            preparedStatement.executeUpdate();
        }
    }

    PrintWriter writer = new PrintWriter(new FileWriter("java_output.txt", true));

    // Write to the file
    writer.println("Rows: " + linesCount);

    // Close the writer to flush and release resources
    writer.close();   

    // System.out.println("Data inserted successfully.");

} catch (SQLException | IOException e) {
    e.printStackTrace();
}

// Measure execution time
long endTime = System.currentTimeMillis();
long executionTime = endTime - startTime;

try {
    // Open a PrintWriter with a FileWriter for appending
    PrintWriter writer = new PrintWriter(new FileWriter("java_output.txt", true));

    // Write to the file
    writer.println("Data insertion using Java took " + executionTime + " milliseconds.");
    writer.println("-------------------------------------------------------------------");
    // Close the writer to flush and release resources
    writer.close();

    System.out.println("Data written to file.");
} catch (Exception e) {
    e.printStackTrace();
}
}

private static String getPlaceholders(int count) {
StringBuilder placeholders = new StringBuilder();
for (int i = 0; i < count; i++) {
    placeholders.append("?");
    if (i < count - 1) {
        placeholders.append(",");
    }
}
return placeholders.toString();
}
}