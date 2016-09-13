// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iGap.adapter.G;


/**
 * 
 * some of instruments for use in project
 *
 */

public class Utils {

    /**
     * 
     * copy from source to destination
     * 
     * @param input source
     * @param output destination
     */

    public static void CopyStream(InputStream input, OutputStream output)
    {
        final int buffer_size = 1024;
        try
        {
            byte[] bytes = new byte[buffer_size];
            for (;;)
            {
                int count = input.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                output.write(bytes, 0, count);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get a bitmap file and save it in download folder in device
     * 
     * @param bitmap source file
     * @return true if image save in device and false if not save
     */

    public static Boolean SavePicToDownLoadFolder(Bitmap bitmap) {

        try {
            if (bitmap == null)
                return false;

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            Calendar calendar = Calendar.getInstance();
            java.util.Date now = calendar.getTime();
            Timestamp tsTemp = new Timestamp(now.getTime());
            String ts = tsTemp.toString();

            File file = new File(path, ts + ".jpg");

            OutputStream fOut = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fOut);

            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }

    }


    /**
     * get a textView and set layout direction ltr or rtl to it
     */

    public static void setLayoutDirection(TextView textView, LinearLayout parent) {

        if (textView == null)
            return;

        String Text = textView.getText().toString();

        if (Text == null)
            return;

        if (Text.length() < 1)
            return;

        String str = "a";
        for (int i = 0; i < Text.length(); i++) {

            str = Text.substring(i, i + 1);

            if (str.equals("[")) {

                int j = Text.substring(i + 1).indexOf("].png");
                if (j > 0) {
                    i = j + 5 + i;
                    continue;
                }

            } else if (isCharacter(str)) {
                break;
            }
        }

        int unicode = str.codePointAt(0);

        if (unicode > 1280 && unicode < 2034) { // persian code

            //            textView.setTextDirection(View.TEXT_DIRECTION_RTL);

            if (parent != null)
                parent.setGravity(Gravity.RIGHT);

            //                parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            //            textView.setGravity(Gravity.RIGHT);

        } else {

            //            textView.setTextDirection(View.TEXT_DIRECTION_LTR);
            //            textView.setGravity(Gravity.LEFT);
            if (parent != null)
                parent.setGravity(Gravity.LEFT);
            //                parent.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }


    public static Boolean isCharacter(String s) {

        int code = s.codePointAt(0);

        if ((code > 64 && code < 91) || (code > 97 && code < 122) || (code > 1560 && code < 1920)) {
            return true;
        }
        return false;

    }


    /**
     * check the selected language user and set the language if change it
     */

    public static void checkLanguage(Context context) {

        try {
            String selectedLanguage = G.SelectedLanguage;

            if (selectedLanguage == null)
                return;

            String currentLalnguage = Locale.getDefault().getLanguage();

            if ( !selectedLanguage.equals(currentLalnguage)) {

                Locale locale = new Locale(selectedLanguage);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }
        catch (Exception e) {}

    }

}