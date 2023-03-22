import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/personsdb";
    private static final String DATABASE = "personsdb";
    private static final int PORT = 3306;
    private static final String UNAME = "esp32";
    private static final String PASSWD = "microcontrollerslab@123";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");//register your mysql jdbc driver with the java application.
        return DriverManager.getConnection(URL,UNAME,PASSWD);
    }

    public static String getPersonName() {
        return UNAME;
    }

}
