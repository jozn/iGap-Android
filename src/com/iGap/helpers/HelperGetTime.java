// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iGap.R;
import com.iGap.adapter.G;
import com.iGap.instruments.database;


public class HelperGetTime {

    private long     secondA;
    private long     minuteA;
    private long     hourA;
    private long     dayA;
    private long     monthA;
    private long     yearA;
    private Calendar calendarA;
    private String   monthh, dayy, hourr, minutee, secondd;

    
    /**
     * return a currnt data and time
     * @return
     */

    public String getTime() {
        calendarA = Calendar.getInstance();
        secondA = calendarA.get(Calendar.SECOND);
        minuteA = calendarA.get(Calendar.MINUTE);
        hourA = calendarA.get(Calendar.HOUR_OF_DAY);
        dayA = calendarA.get(Calendar.DAY_OF_MONTH);
        monthA = calendarA.get(Calendar.MONTH) + 1;
        yearA = calendarA.get(Calendar.YEAR);

        if (monthA < 10) {
            monthh = "0" + monthA;
        } else {
            monthh = "" + monthA;
        }
        if (dayA < 10) {
            dayy = "0" + dayA;
        } else {
            dayy = "" + dayA;
        }
        if (hourA < 10) {
            hourr = "0" + hourA;
        } else {
            hourr = "" + hourA;
        }

        if (minuteA < 10) {
            minutee = "0" + minuteA;
        } else {
            minutee = "" + minuteA;
        }
        if (secondA < 10) {
            secondd = "0" + secondA;
        } else {
            secondd = "" + secondA;
        }

        String timerDateA = "" + yearA + "-"
                + monthh + "-"
                + dayy + " "
                + hourr + ":"
                + minutee + ":"
                + secondd;

        return timerDateA;
    }

/**
 * return time  contain hour : minute : secend
 * @return
 */
    public String getTimeClock() {
        calendarA = Calendar.getInstance();
        secondA = calendarA.get(Calendar.SECOND);
        minuteA = calendarA.get(Calendar.MINUTE);
        hourA = calendarA.get(Calendar.HOUR_OF_DAY);
        if (hourA < 10) {
            hourr = "0" + hourA;
        } else {
            hourr = "" + hourA;
        }

        if (minuteA < 10) {
            minutee = "0" + minuteA;
        } else {
            minutee = "" + minuteA;
        }
        if (secondA < 10) {
            secondd = "0" + secondA;
        } else {
            secondd = "" + secondA;
        }

        String timerDateA = hourr + ":"
                + minutee + ":"
                + secondd;

        return timerDateA;
    }

/**
 * get a time string and return statuse time or data
 * 
 *
 */
    public static String getStringTime(String time, database db) {

        if (time == null)
            return "";

        String result = "";

        if (time.equals("never"))
            result = "last seen a long time ago";

        else if (time.equals("recently"))
            result = "last seen recently";

        else if (time.equals("online"))
            result = "online";

        else {
            String country = db.getCountry();
            String utc = db.select("Countries", "country_name = '" + country + "'", 5);
            long utcMillis = Integer.parseInt(utc) * 1000;
            String lasts = convertWithSingleTime(time, utcMillis);
            String[] str = lasts.split("\\s+");
            String[] strTime = str[0].split("-");
            String[] strTimewithoutsecond = str[1].split(":");
            String hmtime = strTimewithoutsecond[0] + ":" + strTimewithoutsecond[1];
            String ymdtime = str[0] + " " + hmtime;

            try {
                int year = Integer.parseInt(strTime[0]);
                int mounth = Integer.parseInt(strTime[1]);
                int day = Integer.parseInt(strTime[2]);

                Calendar c = Calendar.getInstance();

                String hijri = G.hijriDate;

                if (c.get(Calendar.YEAR) != year || c.get(Calendar.MONTH) + 1 != mounth || c.get(Calendar.DAY_OF_MONTH) != day) {

                    if (hijri.equals("0")) { // tarikh miladi
                        result = ymdtime;
                    } else {
                        HelperCalendarTools convertTime = new HelperCalendarTools();
                        convertTime.GregorianToPersian(year, mounth, day);
                        String date = convertTime.getYear() + "-" + convertTime.getMonth() + "-" + convertTime.getDay();
                        result = date;
                    }

                } else {
                    result = hmtime;
                }
            }
            catch (Exception e) {
                result = ymdtime;
            }
        }

        if (G.SelectedLanguage.equals("fa")) {

            result = stringNumberToPersianNumberFormat(result);
        }

        return result;
    }

    /**
     * convert english number to persian number
     * @param text
     * @return
     */

    public static String stringNumberToPersianNumberFormat(String text) {

        String out = "";

        if (text == null)
            return out;
        int length = text.length();

        if (length < 1)
            return out;

        String[] persianNumbers = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else {
                out += c;
            }

        }

        return out;
    }

/**
 * convert utc time to local time 
 * @param date
 * @param clock
 * @param utcMillis
 * @return
 */
    public static String convertTime(String date, String clock, long utcMillis) {

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        long mil = 0;
        try {
            Date date11 = isoFormat.parse(date + "T" + clock);
            mil = date11.getTime();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        mil = mil + utcMillis;

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        cal.setTimeInMillis(mil);
        String offtime = cal.get(Calendar.YEAR) + "-" + CorrectTime((cal.get(Calendar.MONTH)) + 1) + "-" + CorrectTime(cal.get(Calendar.DAY_OF_MONTH)) + " " + CorrectTime(cal.get(Calendar.HOUR_OF_DAY)) + ":" + CorrectTime(cal.get(Calendar.MINUTE)) + ":" + CorrectTime(cal.get(Calendar.SECOND));

        return offtime;
    }

/**
 * get time 
 * @param cal
 * @return
 */
    public static String convertTimeWithCalendar(Calendar cal) {

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String offtime = year + "-" + (CorrectTime(month + 1)) + "-" + CorrectTime(day) + " " + CorrectTime(hour) + ":" + CorrectTime(minute) + ":" + CorrectTime(second);

        return offtime;
    }

/**
 * set zero befor number if number less then 10 and return string
 * @param time
 * @return
 */
    static String CorrectTime(int time) {

        String s;

        if (time < 10) {
            s = "0" + time;
        } else {
            s = time + "";
        }
        return s;
    }


    public static String convertWithSingleTime(String singelTime, long utcMillis) {

        String[] splitedtime = singelTime.split("\\s+");
        String date = splitedtime[0];
        String clock = splitedtime[1];

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        long mil = 0;
        try {
            Date date11 = isoFormat.parse(date + "T" + clock);
            mil = date11.getTime();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        mil = mil + utcMillis;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        cal.setTimeInMillis(mil);

        String offtime = cal.get(Calendar.YEAR) + "-" + CorrectTime((cal.get(Calendar.MONTH)) + 1) + "-" + CorrectTime(cal.get(Calendar.DAY_OF_MONTH)) + " " + CorrectTime(cal.get(Calendar.HOUR_OF_DAY)) + ":" + CorrectTime(cal.get(Calendar.MINUTE)) + ":" + CorrectTime(cal.get(Calendar.SECOND));

        return offtime;
    }

/**
 * 
 *return a clock time or date time 
 */
    public static String getLastSeen(String lastSeenTime, String year, String month, String day) {

        Log.e("ttyy", lastSeenTime);

        String result = "";

        try {
            String time = "";
            String date = "";

            if (year == null || month == null || day == null || lastSeenTime == null)
                return result;

            if (year.length() == 0 || month.length() == 0 || day.length() == 0 || lastSeenTime.length() == 0)
                return result;

            String[] splitedrowtime = lastSeenTime.split("\\s+");

            if (splitedrowtime.length >= 2) {
                date = splitedrowtime[0];
                time = splitedrowtime[1];
            }
            else if (splitedrowtime.length == 1) {
                date = splitedrowtime[0];
            }

            String[] t = time.split(":");

            if (t.length >= 3)
                time = t[0] + ":" + t[1];

            String[] splited_currentpositiondate = date.split("-");

            if (splited_currentpositiondate.length < 3)
                return result;

            String currentpositionyear = splited_currentpositiondate[0];
            String currentpositionmonth = splited_currentpositiondate[1];
            String currentpositionday = splited_currentpositiondate[2];

            int intcurrentpositionyear = Integer.parseInt(currentpositionyear);
            int intcurrentpositionmonth = Integer.parseInt(currentpositionmonth);
            int intcurrentpositionday = Integer.parseInt(currentpositionday);

            if (G.hijriDate.equals("0")) { // tarikh miladi bashad

                if (Integer.parseInt(year) > intcurrentpositionyear) {
                    result = date;
                } else if (Integer.parseInt(month) > intcurrentpositionmonth) {
                    result = date;
                } else if (Integer.parseInt(day) > intcurrentpositionday) {
                    result = date;
                } else {
                    result = time;
                }
            } else { // tarikh shamsi bashad

                HelperCalendarTools convertTime = new HelperCalendarTools();
                convertTime.GregorianToPersian(intcurrentpositionyear, intcurrentpositionmonth, intcurrentpositionday);
                date = convertTime.getYear() + "-" + convertTime.getMonth() + "-" + convertTime.getDay();

                if (Integer.parseInt(year) > intcurrentpositionyear) {
                    result = date;
                } else if (Integer.parseInt(month) > intcurrentpositionmonth) {
                    result = date;
                } else if (Integer.parseInt(day) > intcurrentpositionday) {
                    result = date;
                } else {
                    result = time;
                }
            }

            if (G.SelectedLanguage.equals("fa")) {

                result = stringNumberToPersianNumberFormat(result);
            }

        }
        catch (NumberFormatException e) {
            return result;
        }

        return result;

    }

/**
 * get a time and remove secend from it
 * @param data
 * @return
 */
    public static String removeSecendFromTime(String data) {

        String[] splitedrowtime = data.split("\\s+");

        if (splitedrowtime.length < 2)
            return "";

        String time = splitedrowtime[1];

        try {
            String[] t = splitedrowtime[1].split(":");
            time = t[0] + ":" + t[1];
        }
        catch (Exception e1) {}

        if (G.SelectedLanguage.equals("fa")) {

            time = stringNumberToPersianNumberFormat(time);
        }

        return time;
    }

/**
 * 
 *get String today or date time 
 */
    public static void setTxtToday(Context mContext, LinearLayout layout, TextView txtTime, String curentDate, String beforDate, int position, String year, String month, String day) {

        if (position != 0) {
            //current
            String[] splitedcurrentposition = curentDate.split("\\s+");
            String currentpositiondate = splitedcurrentposition[0];
            String[] splited_currentpositiondate = currentpositiondate.split("-");
            String currentpositionyear = splited_currentpositiondate[0];
            String currentpositionmonth = splited_currentpositiondate[1];
            String currentpositionday = splited_currentpositiondate[2];
            //befor
            String[] splitedbeforposition = beforDate.split("\\s+");
            String beforpositiondate = splitedbeforposition[0];
            String[] splited_beforpositiondate = beforpositiondate.split("-");
            String beforpositionyear = splited_beforpositiondate[0];
            String beforpositionmonth = splited_beforpositiondate[1];
            String beforpositionday = splited_beforpositiondate[2];
            int intcurrentpositionyear = Integer.parseInt(currentpositionyear);
            int intcurrentpositionmonth = Integer.parseInt(currentpositionmonth);
            int intcurrentpositionday = Integer.parseInt(currentpositionday);
            int intbeforpositionyear = Integer.parseInt(beforpositionyear);
            int intbeforpositionmonth = Integer.parseInt(beforpositionmonth);
            int intbeforpositionday = Integer.parseInt(beforpositionday);

            if (G.hijriDate.equals("0")) { // tarikh miladi

                if (intcurrentpositionyear > intbeforpositionyear) {
                    layout.setVisibility(View.VISIBLE);
                    if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                        txtTime.setText(mContext.getString(R.string.today_en));
                    } else {
                        txtTime.setText("--  " + currentpositiondate + "  --");
                    }
                } else {
                    if (intcurrentpositionmonth > intbeforpositionmonth) {
                        layout.setVisibility(View.VISIBLE);
                        if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                            txtTime.setText(mContext.getString(R.string.today_en));
                        } else {
                            txtTime.setText("--  " + currentpositiondate + "  --");
                        }
                    } else {
                        if (intcurrentpositionday > intbeforpositionday) {
                            layout.setVisibility(View.VISIBLE);
                            if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                                txtTime.setText(mContext.getString(R.string.today_en));
                            } else {
                                txtTime.setText("--  " + currentpositiondate + "  --");
                            }
                        } else {
                            layout.setVisibility(View.GONE);

                        }
                    }
                }

            } else {

                HelperCalendarTools convertTime = new HelperCalendarTools();
                convertTime.GregorianToPersian(intcurrentpositionyear, intcurrentpositionmonth, intcurrentpositionday);
                String date = convertTime.getYear() + "-" + convertTime.getMonth() + "-" + convertTime.getDay();

                if (intcurrentpositionyear > intbeforpositionyear) {
                    layout.setVisibility(View.VISIBLE);
                    if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                        txtTime.setText(mContext.getString(R.string.today_en));
                    } else {
                        txtTime.setText("--  " + date + "  --");
                    }
                } else {
                    if (intcurrentpositionmonth > intbeforpositionmonth) {
                        layout.setVisibility(View.VISIBLE);
                        if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                            txtTime.setText(mContext.getString(R.string.today_en));
                        } else {
                            txtTime.setText("--  " + date + "  --");
                        }
                    } else {
                        if (intcurrentpositionday > intbeforpositionday) {
                            layout.setVisibility(View.VISIBLE);
                            if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                                txtTime.setText(mContext.getString(R.string.today_en));
                            } else {
                                txtTime.setText("--  " + date + "  --");
                            }
                        } else {
                            layout.setVisibility(View.GONE);
                        }
                    }
                }
            }

        } else {
            String[] splitedcurrentposition = curentDate.split("\\s+");
            String currentpositiondate = splitedcurrentposition[0];
            String[] splited_currentpositiondate = currentpositiondate.split("-");
            String currentpositionyear = splited_currentpositiondate[0];
            String currentpositionmonth = splited_currentpositiondate[1];
            String currentpositionday = splited_currentpositiondate[2];
            int intcurrentpositionyear = Integer.parseInt(currentpositionyear);
            int intcurrentpositionmonth = Integer.parseInt(currentpositionmonth);
            int intcurrentpositionday = Integer.parseInt(currentpositionday);
            layout.setVisibility(View.VISIBLE);
            if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                txtTime.setText(mContext.getString(R.string.today_en));
            } else {

                if (G.hijriDate.equals("0")) {
                    txtTime.setText("--  " + currentpositiondate + "  --");
                } else {
                    HelperCalendarTools convertTime = new HelperCalendarTools();
                    convertTime.GregorianToPersian(intcurrentpositionyear, intcurrentpositionmonth, intcurrentpositionday);
                    String date = convertTime.getYear() + "-" + convertTime.getMonth() + "-" + convertTime.getDay();
                    txtTime.setText("--  " + date + "  --");
                }
            }
        }

        if (G.SelectedLanguage.equals("fa")) {

            txtTime.setText(stringNumberToPersianNumberFormat(txtTime.getText().toString()));
        }

    }

}
