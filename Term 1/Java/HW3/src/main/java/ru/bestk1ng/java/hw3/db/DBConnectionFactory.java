package ru.bestk1ng.java.hw3.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.bestk1ng.java.hw3.App;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DBConnectionFactory {
    private final static String pathToDb = "./sqlite/airtrans.sqlite";
    private static final HikariConfig config = new HikariConfig();
    private static final DataSource ds;

    static {
        File file = new File(pathToDb);
        file.getParentFile().mkdir();

        config.setJdbcUrl("jdbc:sqlite:" + pathToDb);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
        try {
            prepareDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private static void prepareDB() throws SQLException {
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQL("/create_tables.sql");
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getSQL(String file) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        App.class.getResourceAsStream(file),
                        StandardCharsets.UTF_8
                )
        )) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }
}
