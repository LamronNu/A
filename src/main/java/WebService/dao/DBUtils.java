package WebService.dao;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Olga on 22.09.2014.
 */
public class DBUtils {
    private static Connection connection = null;
    private static final Logger log = Logger.getLogger(DBUtils.class);
    public static Connection getConnection() {
        if (connection != null)
            return connection;
        else {
            try {
                Properties prop = new Properties();
                InputStream inputStream = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
                prop.load(inputStream);
                String driver = prop.getProperty("driver");
                String url = prop.getProperty("url");
                String user = prop.getProperty("user");
                String password = prop.getProperty("password");
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                log.error("catch ex:" + e.getMessage());
                //e.printStackTrace();
            } catch (SQLException e) {
                log.error("catch ex:" + e.getMessage());
            } catch (FileNotFoundException e) {
                log.error("catch ex:" + e.getMessage());
            } catch (IOException e) {
                log.error("catch ex:" + e.getMessage());
            }
            return connection;
        }

    }
}
