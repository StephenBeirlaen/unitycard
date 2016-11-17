package be.nmct.unitycard.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Stephen on 17/11/2016.
 */

public class TimestampHelper {
    // Adapted from http://stackoverflow.com/questions/249760/how-to-convert-a-unix-timestamp-to-datetime-and-vice-versa
    public static long DateToUnixTimeStamp(Date timeStamp)
    {
        return timeStamp.getTime() / 1000; // getTime geeft milliseconden terug
    }

    public static String convertDateToString(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(date);
        }
        return null;
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        if (dateString != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(dateString);
        }
        return null;
    }
}
