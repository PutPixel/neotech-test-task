package neotech.test.ds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.fabric.jdbc.FabricMySQLDataSource;

public class TimestampDataSource implements DataSource {

    private FabricMySQLDataSource ds;

    public TimestampDataSource() {
        String hostname = System.getProperty("ds.hostname");
        String port = System.getProperty("ds.port");
        String user = System.getProperty("ds.username");
        String password = System.getProperty("ds.password");

        ds = new FabricMySQLDataSource();
        ds.setServerName(hostname);
        ds.setPort(Integer.valueOf(port));
        ds.setUser(user);
        ds.setPassword(password);

        try {
            if (!com.mysql.jdbc.Util.isJdbc4()) {
                Class.forName("com.mysql.fabric.jdbc.FabricMySQLDriver");
            }

            createDb();
            createTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createTable() throws SQLException {
        ds.setDatabaseName("test");
        Connection rawConnection = getConnection();
        Statement statement = rawConnection.createStatement();
        statement.executeUpdate(
                "create table if not exists test ( 'timestamp' DATETIME NOT NULL, PRIMARY KEY ('timestamp'), UNIQUE INDEX 'timestamp_UNIQUE' ('timestamp' ASC))");
        statement.close();
        rawConnection.close();
    }

    private void createDb() throws SQLException {
        ds.setDatabaseName("mysql");
        Connection rawConnection = getConnection();
        Statement statement = rawConnection.createStatement();
        statement.executeUpdate("create database if not exists test");
        statement.close();
        rawConnection.close();
    }

    @Override
    public boolean saveTimestamp(Date timestamp) {
        try (Connection connection = getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement("INSERT INTO test.test VALUES (?)");
            prepareStatement.setTimestamp(1, new Timestamp(timestamp.getTime()));
            return prepareStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public List<Date> readTimestamps() {
        try (Connection connection = getConnection()) {
            ResultSet resultSet = connection.prepareStatement("SELECT timestamp FROM  test.test").getResultSet();
            List<Date> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new Date(resultSet.getTimestamp(1).getTime()));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
