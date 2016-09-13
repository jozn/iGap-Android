package com.iGap.instruments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.iGap.adapter.G;


/**
 * 
 * for update database table and row when the version change and user install new version
 *
 */

public class CheckDatabase {

    private static boolean isColumnExist(String tableName, String fildName) {

        Boolean result = false;
        Cursor mCursor = null;

        try {
            mCursor = G.mydb.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", null);

            if (mCursor.getColumnIndex(fildName) != -1)
                result = true;
            else
                result = false;
        }
        catch (Exception e) {
            result = false;
        } finally {
            if (mCursor != null)
                mCursor.close();
        }

        return result;
    }


    private static Boolean isTableExist(String tableName) {
        Boolean result = false;
        Cursor mCursor = null;

        try {
            mCursor = G.mydb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

            if (mCursor != null) {
                if (mCursor.getCount() > 0) {
                    mCursor.close();
                    result = true;
                }
                mCursor.close();
            }
            else {
                result = false;
            }
        }
        catch (Exception e) {
            result = false;
        } finally {
            if (mCursor != null)
                mCursor.close();
        }

        return result;
    }


    public static void checkDb(Context context) {

        if (isTableExist("websiteinfo") == false) {
            String creatTableWebsiteinfo = " CREATE TABLE IF NOT EXISTS websiteinfo ( id integer primary key autoincrement ,  url text ,  wtitle  text  ,   wdescription text ,   wicon ,  text ) ";
            sqlQuery(creatTableWebsiteinfo);
        }

        if (isColumnExist("chathistory", "filemime") == false) {

            String addFild = " ALTER TABLE   chathistory  ADD COLUMN filemime text ";
            sqlQuery(addFild);
        }

        if (isColumnExist("groupchathistory", "filemime") == false) {

            String addFild = " ALTER TABLE   groupchathistory  ADD COLUMN filemime text ";
            sqlQuery(addFild);
        }

        if (isColumnExist("channelhistory", "filemime") == false) {
            String addFild = " ALTER TABLE   channelhistory  ADD COLUMN filemime text ";
            sqlQuery(addFild);
        }

        if (isColumnExist("messagingcache", "filemime") == false) {
            String addFild = " ALTER TABLE messagingcache ADD COLUMN filemime text ";
            sqlQuery(addFild);
        }

        if (isColumnExist("Countries", "utc") == false) {

            String addFild = " ALTER TABLE   Countries  ADD COLUMN utc INT DEFAULT 0  ";
            sqlQuery(addFild);

            fillTableCountry(context);
        }

        if (isColumnExist("groupchatrooms", "groupavatarhq") == false) {
            String addFild = " ALTER TABLE groupchatrooms ADD COLUMN groupavatarhq text ";
            sqlQuery(addFild);
        }

        if (isColumnExist("setting", "country") == false) {
            String addFild = " ALTER TABLE setting ADD COLUMN country text ";
            sqlQuery(addFild);
        }

        if (isColumnExist("setting", "hijridate") == false) {
            String addFild = " ALTER TABLE setting ADD COLUMN hijridate INT DEFAULT 0 ";
            sqlQuery(addFild);
        }

        if (isColumnExist("setting", "crop") == false) {
            String addFild = " ALTER TABLE setting ADD COLUMN crop INT DEFAULT 0 ";
            sqlQuery(addFild);
        }
    }


    private static void sqlQuery(String query) {

        try {
            G.mydb.execSQL(query);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }

    }


    private static void fillTableCountry(Context context) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("countries.txt")));
            String mLine = reader.readLine();
            while (mLine != null) {

                String[] str = mLine.split(",");

                if (str[0] != null)
                    if (str[0].length() > 0) {

                        if (str[1] != null)
                            if (str.length > 0) {
                                ContentValues values = new ContentValues();
                                values.put("utc", str[1]);
                                G.mydb.update("Countries", values, " id = ? ", new String[]{ str[0] });
                            }

                    }

                mLine = reader.readLine();
            }
            reader.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
