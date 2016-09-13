// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import com.iGap.helpers.HelperGetTime;


/**
 * 
 * return a current time in online or offline position
 *
 */

public class TimerServies extends Service {

    private int           monthLi       = 31;
    private Handler       customHandler = new Handler();
    private long          second;
    private long          minute;
    private long          hour;
    private long          day;
    private long          month;
    private long          year;
    private int           dayOFYear;
    private int           dOfY          = 366;
    private static String timerDate;
    private static String timerClock;
    Context               context;
    private String        monthh, dayy, hourr, minutee, secondd;


    public void setDate(String date)
    {
        getDate(this);
    }


    public void setDayOfYear(int dayOFYear)
    {
        this.dOfY = dayOFYear;
        getDate(this);
    }


    @Override
    public IBinder onBind(Intent intent)
    {

        return null;

    }


    @Override
    public void onCreate()
    {
        context = TimerServies.this;
        super.onCreate();
        context = TimerServies.this;

        getDate(context);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;

    }


    public String getDate(Context ct)
    {
        HelperGetTime helperGetTime = new HelperGetTime();

        return helperGetTime.getTime();

    }

    private Runnable updateTimerThread = new Runnable()
                                       {

                                           public void run() {

                                               if (dayOFYear == (dOfY + 1))
                                               {
                                                   dayOFYear = 0;
                                               }
                                               if (dayOFYear <= 31)
                                               {
                                                   monthLi = 31;
                                               }
                                               else if (dayOFYear <= 61 && dayOFYear > 31)
                                               {
                                                   monthLi = 29;
                                               }
                                               else if (dayOFYear <= 91 && dayOFYear > 61)
                                               {
                                                   monthLi = 31;
                                               }
                                               else if (dayOFYear <= 121 && dayOFYear > 91)
                                               {
                                                   monthLi = 30;
                                               }
                                               else if (dayOFYear <= 152 && dayOFYear > 121)
                                               {
                                                   monthLi = 31;
                                               }
                                               else if (dayOFYear <= 182 && dayOFYear > 152)
                                               {
                                                   monthLi = 30;
                                               }
                                               else if (dayOFYear <= 213 && dayOFYear > 182)
                                               {
                                                   monthLi = 31;
                                               }
                                               else if (dayOFYear <= 244 && dayOFYear > 213)
                                               {
                                                   monthLi = 31;
                                               }
                                               else if (dayOFYear <= 274 && dayOFYear > 244)
                                               {
                                                   monthLi = 30;
                                               }
                                               else if (dayOFYear <= 305 && dayOFYear > 274)
                                               {
                                                   monthLi = 31;
                                               }
                                               else if (dayOFYear <= 335 && dayOFYear > 305)
                                               {
                                                   monthLi = 30;
                                               }
                                               else if (dayOFYear <= dOfY && dayOFYear > 335)
                                               {
                                                   monthLi = 31;
                                               }
                                               else
                                               {
                                                   monthLi = 30;
                                               }

                                               second++;
                                               if (second == 60)
                                               {
                                                   second = 0;
                                                   minute++;
                                               }
                                               if (minute == 60)
                                               {
                                                   minute = 0;
                                                   hour++;
                                               }
                                               if (hour == 24)
                                               {
                                                   hour = 0;
                                                   day++;
                                                   dayOFYear++;
                                               }
                                               if (day > monthLi)
                                               {
                                                   day = 1;
                                                   month++;
                                               }
                                               if (month > 12)
                                               {
                                                   month = 1;
                                                   year++;
                                               }

                                               if (month < 10) {
                                                   monthh = "0" + month;
                                               } else {
                                                   monthh = "" + month;
                                               }
                                               if (day < 10) {
                                                   dayy = "0" + day;
                                               } else {
                                                   dayy = "" + day;
                                               }
                                               if (hour < 10) {
                                                   hourr = "0" + hour;
                                               } else {
                                                   hourr = "" + hour;
                                               }

                                               if (minute < 10) {
                                                   minutee = "0" + minute;
                                               } else {
                                                   minutee = "" + minute;
                                               }
                                               if (second < 10) {
                                                   secondd = "0" + second;
                                               } else {
                                                   secondd = "" + second;
                                               }
                                               timerClock = hourr + ":"
                                                       + minutee + ":"
                                                       + secondd;

                                               timerDate = "" + year + "-"
                                                       + monthh + "-"
                                                       + dayy + " "
                                                       + timerClock;

                                               customHandler.postDelayed(this, 1000);

                                           }

                                       };


    public String getDateTime() {

        HelperGetTime helperGetTime = new HelperGetTime();

        timerDate = helperGetTime.getTime();

        return timerDate;
    }


    public String getTime()
    {

        if (timerClock == null) {
            HelperGetTime helperGetTime = new HelperGetTime();
            timerClock = helperGetTime.getTimeClock();
        }

        return timerClock;
    }

}
