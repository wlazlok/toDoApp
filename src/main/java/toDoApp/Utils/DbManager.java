package toDoApp.Utils;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Karol Wlazło
 * toDoApp
 */
public class DbManager {

        private static final String JDBC_CONNECTION_H2 = "jdbc:h2:./todoapp";
        private static ConnectionSource connectionSource;
        private static final String log4jConfPath = "src/main/java/log4j.properties";

        public static void createConnectionSource(){
                PropertyConfigurator.configure(log4jConfPath);
                try {
                        connectionSource = new JdbcConnectionSource(JDBC_CONNECTION_H2);
                } catch (SQLException throwables) {
                        throwables.printStackTrace();
                }
        }

        public static ConnectionSource getConnectionSource(){
                if(connectionSource == null)
                        createConnectionSource();
                return connectionSource;
        }

        public static void closeConnectionSource(){
                if(connectionSource != null){
                        try {
                                connectionSource.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }
}
