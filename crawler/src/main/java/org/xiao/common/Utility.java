package org.xiao.common;

import org.tinylog.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {

    public static int convertMonth2int(String monthText) {
        int month = 0;
        switch (monthText) {
            case "Dec":
            case "December":
                month++;
            case "Nov":
            case "November":
                month++;
            case "Oct":
            case "October":
                month++;
            case "Sept":
            case "September":
                month++;
            case "Aug":
            case "August":
                month++;
            case "July":
                month++;
            case "June":
                month++;
            case "May":
                month++;
            case "April":
                month++;
            case "March":
                month++;
            case "Feb":
            case "February":
                month++;
            case "Jan":
            case "January":
                month++;
        }
        return month;
    }

    public static long dateText2timestamp(String dateFormat, String dateText) {
        SimpleDateFormat myFmt = new SimpleDateFormat(dateFormat);
        long ts = 0;
        try {
            Date date = myFmt.parse(dateText);
            ts = date.getTime();
        } catch (ParseException e) {
//            throw new RuntimeException(e);
            Logger.error("dateText convert to timestamp error: " + e.getMessage());
        }
        return ts;
    }

    public static long dateText2timestamp(String dateFormat, String dateText, String timeZoneID) {
        SimpleDateFormat myFmt = new SimpleDateFormat(dateFormat);
        myFmt.setTimeZone(TimeZone.getTimeZone(timeZoneID));
        long ts = 0;
        try {
            Date date = myFmt.parse(dateText);
            ts = date.getTime();
        } catch (ParseException e) {
//            throw new RuntimeException(e);
            Logger.error("dateText convert to timestamp error: " + e.getMessage());
        }
        return ts;
    }

    public static String escapeSql(String sql) {
        if (sql == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            if (ch == '\'') {
                sb.append("''");
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
