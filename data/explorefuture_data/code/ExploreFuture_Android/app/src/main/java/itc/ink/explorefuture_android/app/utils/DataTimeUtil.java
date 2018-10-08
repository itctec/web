package itc.ink.explorefuture_android.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yangwenjiang on 2018/9/26.
 */

public class DataTimeUtil {
    /**
     * @param dataTimeOneStr           input timestamp No.1
     * @param dataTimeTwoStr           input timestamp No.2
     * @param timestampFormat the format object for time stamp
     * @return if return value is 1 -> time 1 is newer
     * value is -1 -> time 2 is newer
     * value is 0 -> same timestamps
     */
    public static int dateTimeCompare(String dataTimeOneStr, String dataTimeTwoStr, SimpleDateFormat timestampFormat) {

        int compareResult = 0;
        if (dataTimeOneStr.equals(dataTimeTwoStr))
            return compareResult;

        try {
            Date dataTimeOne = timestampFormat.parse(dataTimeOneStr);
            Date dataTimeTwo = timestampFormat.parse(dataTimeTwoStr);

            Calendar calendarOne = Calendar.getInstance();
            Calendar calendarTwo = Calendar.getInstance();
            calendarOne.setTime(dataTimeOne);
            calendarTwo.setTime(dataTimeTwo);

            if (calendarOne.compareTo(calendarTwo) > 0) {
                return 1;
            }else{
                return -1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return compareResult;
    }
}


