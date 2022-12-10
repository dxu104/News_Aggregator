package org.xiao;

import org.tinylog.Logger;
import org.xiao.db.DataHandler;
import org.xiao.feeder.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Timer {

    private long delay;
    private String dbPath;

    public Timer(long hour, String dbPath) {
        this.delay = hour;
        this.dbPath = dbPath;
    }

    public void start() throws InterruptedException, SQLException, ClassNotFoundException {
        while (true) {
            Logger.info("after every network connection we will sleep for " + delay + " seconds");
            startOnce();
            Logger.info("\nwait for next time in " + delay + " hours\n");
            TimeUnit.HOURS.sleep(delay);
        }
    }

    public void startOnce() throws SQLException, ClassNotFoundException {
        DataHandler dh = new DataHandler(dbPath);
        int totalInserted = 0;
        List<BaseFeeder> feederList = new ArrayList<>();
        feederList.add(new CNNFeeder(dh));
        feederList.add(new ABCFeeder(dh));
        feederList.add(new FoxFeeder(dh));
        feederList.add(new King5Feeder(dh));
        feederList.add(new SeattleTimesFeeder(dh));
        for (BaseFeeder feeder :
                feederList) {
            Logger.info("\ncheck and update channel: " + feeder.getChannelID());
            int affected = feeder.checkAndUpdate();
            Logger.info("Affected rows: " + affected);
            totalInserted += affected;
        }
        Logger.info("\nTotal affected rows: " + totalInserted);
        dh.closeConnect();
    }
}
