package db;

import common.Utility;
import org.tinylog.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataHandler {
    private Connection conn = null;

    private final String path;

    public DataHandler(String path) throws SQLException, ClassNotFoundException {
        this.path = path;
        connect();

    }

    public boolean connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + path);
        Logger.info("Opened database successfully");
        return true;
    }

    public void closeConnect() throws SQLException {
        if (!conn.isClosed()) {
            conn.close();
        }
    }

    public int insertOrIgnoreNews(News[] news) {
        if (news.length == 0)
            return 0;
        Statement stmt = null;
        int affected = 0;
        try {
            stmt = conn.createStatement();
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT OR IGNORE INTO News (channelID, guid, URL, title, cover, author, content, description, postedTime, createdTime, updatedTime) Values \n");
            for (int i = 0; i < news.length; i++) {
                sb.append(news[i].toString());
                if (i != news.length - 1) {
                    sb.append(",\n");
                }
            }
            String sql = sb.toString();
            affected = stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            Logger.error(e.getClass().getName() + ": " + e.getMessage());
        }
        return affected;
    }

    public int insertOrUpdateChannel(Channel channel) {
        Statement stmt = null;
        int channelId = -1;
        try {
            stmt = conn.createStatement();
            String sql = String.format("INSERT OR IGNORE INTO Channel (name, URL, description, createdTime, updatedTime) VALUES ('%s','%s','%s', %d, %d)", Utility.escapeSql(channel.getName()), Utility.escapeSql(channel.getURL()), Utility.escapeSql(channel.getDescription()), channel.getCreatedTime(), channel.getUpdatedTime());
            int afftected = stmt.executeUpdate(sql);
            if (afftected == 0) {
                sql = String.format("UPDATE Channel set URL = '%s', description = '%s', updatedTime = %d where name = '%s'", Utility.escapeSql(channel.getURL()), Utility.escapeSql(channel.getDescription()), channel.getUpdatedTime(), Utility.escapeSql(channel.getName()));
                stmt.executeUpdate(sql);
            }
            sql = String.format("select id from Channel where name = '%s'", Utility.escapeSql(channel.getName()));
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                channelId = rs.getInt("id");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            Logger.error(e.getClass().getName() + ": " + e.getMessage());
        }
        return channelId;
    }

    /**
     * check the news if already existed, and return the existed ones' ids;
     * @param guids  the unique id of every news
     * @param channelID the channel where the news comes from
     * @return  existed ones' id
     */
    public Set<String> filterExistNews(Set<String> guids, int channelID) {
        if (guids.size() == 0) {
            return new HashSet<>();
        }
        Statement stmt = null;
        Set<String> existGuids = new HashSet<>();
        try {
            stmt = conn.createStatement();
            List<String> t = new ArrayList<>();
            for (String guid : guids) {
                t.add(String.format("'%s'", Utility.escapeSql(guid)));
            }
            String sql = String.format("select guid from News where channelID = %d and guid in (%s)", channelID, String.join(",", t));
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                existGuids.add(rs.getString("guid"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            Logger.error(e.getClass().getName() + ": " + e.getMessage());
        }
        return existGuids;
    }


}
