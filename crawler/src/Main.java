
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        // args[0] is the path to the sqlite db;
        Timer timer = new Timer(1, args[0]);
        // start to crawl the news from various news platforms like cnn, fox;
        // and will periodically check and update
        timer.start();
    }

}
