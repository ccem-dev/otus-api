package org.ccem.otus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String TIME_ZONE = "UTC";

  public static String toISODate(Date date){
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
    return dateFormat.format(date);
  }

  public static String nowToISODate(){
    return toISODate(new Date());
  }

  public static String getDatePlusDays(String dateStr, int days) throws ParseException {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
    calendar.setTime(dateFormat.parse(dateStr));
    calendar.add(Calendar.DATE, days);
    return toISODate(calendar.getTime());
  }

  public static boolean compareDateWithoutTime(String dateStr1, String dateStr2) throws ParseException {
    return dateStr1.substring(0, 11).equals(dateStr2.substring(0, 11));
  }

  public static boolean before(String dateStr1, String dateStr2) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

    Calendar calendar1 = Calendar.getInstance();
    calendar1.setTime(dateFormat.parse(dateStr1));

    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTime(dateFormat.parse(dateStr2));

    System.out.println(calendar1.getTime());
    System.out.println(calendar2.getTime());

    return calendar1.before(calendar2);
  }

}
