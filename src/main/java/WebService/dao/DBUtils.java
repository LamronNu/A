package WebService.dao;

import Library.Consts;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Olga on 22.09.2014.
 */
public class DBUtils {
    private static final Logger log = Logger.getLogger(DBUtils.class);
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection != null)
            return connection;
        else {
            try {
                //get profile name
                Properties env = System.getProperties();
                String profile = env.getProperty("profile");
                log.info("get system property 'profile': " + profile);
                profile = profile == null ? Consts.PROFILE_DEFAULT : profile;
                log.info("use profile: " + profile);

                //todo better
                if (profile == Consts.PROFILE_PRODUCTION) {
                    connection = getProdConnection();
                } else {
                    //get connection
                    Properties prop = new Properties();
                    InputStream inputStream = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
                    prop.load(inputStream);
                    String driver = prop.getProperty(profile + ".driver");
                    String url = prop.getProperty(profile + ".url");
                    String user = prop.getProperty(profile + ".user");
                    String password = prop.getProperty(profile + ".password");
                    Class.forName(driver);
                    connection = DriverManager.getConnection(url, user, password);
                }
            } catch (ClassNotFoundException | IOException | URISyntaxException e) {
                log.error("catch ex:" + e.getMessage());
                //e.printStackTrace();
            }
            return connection;
        }

    }

    private static Connection getProdConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void createTables() throws SQLException {
        //postgresql
        Statement stmt = getConnection().createStatement();

        String sqlCreate = "" +
                /*Users*/
                "CREATE TABLE IF NOT EXISTS users\n" +
                "                (\n" +
                "                id serial PRIMARY KEY,\n" +
                "                createdOn  timestamp DEFAULT current_timestamp NOT NULL,\n" +
                "                login varchar(50) UNIQUE  NOT NULL,\n" +
                "                password varchar(50) NOT NULL,\n" +
                "                firstName varchar(50) NOT NULL,\n" +
                "                lastName varchar(50)\n" +
                "                );";

        log.info("users" + sqlCreate);
        stmt.execute(sqlCreate);

        sqlCreate = "" +
                /*lots*/
                "CREATE TABLE IF NOT EXISTS lots\n" +
                "                (\n" +
                "                id serial PRIMARY KEY,\n" +
                "                createdOn  timestamp DEFAULT current_timestamp NOT NULL,\n" +
                "                name varchar(100) NOT NULL,\n" +
                "                finishDate  timestamp DEFAULT current_timestamp NOT NULL,\n" +
                "                startPrice float(2) NOT NULL,\n" +
                "                description varchar(500),\n" +
                "                ownerId int  NOT NULL,\n" +
                "                state varchar(10) DEFAULT 'Active' NOT NULL\n" +
                "                )\n" +
                "                ;\n" +
                "ALTER TABLE lots ADD CONSTRAINT lots_user\n" +
                "                FOREIGN KEY (ownerId) REFERENCES users (id);";

        log.info("lots " + sqlCreate);
        stmt.execute(sqlCreate);

        sqlCreate = "" +
//                "/*bids*/\n" +
                "CREATE TABLE IF NOT EXISTS bids\n" +
                "                (\n" +
                "                id  serial PRIMARY KEY ,\n" +
                "                lotId int NOT NULL,\n" +
                "                ownerId int NOT NULL,\n" +
                "                value float (2) NOT NULL,\n" +
                "                createdOn  timestamp DEFAULT current_timestamp  NOT NULL\n" +
                "                );\n" +
                "ALTER TABLE bids ADD CONSTRAINT bids_user\n" +
                "                FOREIGN KEY (ownerId) REFERENCES users (id);\n" +
                "ALTER TABLE bids ADD CONSTRAINT lots_user\n" +
                "                FOREIGN KEY (lotId) REFERENCES lots (id);";

        log.info("bids" + sqlCreate);
        stmt.execute(sqlCreate);
    }
}
