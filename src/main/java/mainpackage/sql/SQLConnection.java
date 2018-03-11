package mainpackage.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnection {
  private static final String DB = "jdbc:mysql://localhost/assignment_2";
  private static final String USER = "voldemort";
  private static final String PWD = "hewhomustnotbenamed";

  private static SQLConnection instance = null;
  private Properties props;

  /**
   * Doesn't seem to need load the jdbc driver class
   * via Class.forName("com.jdbc.driver")
   * TODO: debug this issue to make sure it actually is not needed
   */
  private SQLConnection() {
    props = new Properties();
    props.setProperty("user", USER);
    props.setProperty("password", PWD);
    props.setProperty("useSSL", "false");
  }

  public static SQLConnection instance() {
    if (instance == null)
      instance = new SQLConnection();
    return instance;
  }

  public Connection createConnection() throws SQLException {
    return DriverManager.getConnection(DB, props);
  }

}
