// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.iGap.adapter.G;


public class HelperDrawAlphabet {

    /**
     * draw first charahcter of given name and lastname to imageview
     * 
     * @param name username contain name and last name
     * @param img imageView to draw character on it
     * @return
     */
    public static Bitmap drawAlphabet(Context context, String name, ImageView img) {
        name = name.trim();
        String[] splited = name.split("\\s+");

        int size = splited.length;
        String charfirst, mname, Upname;

        if ( !splited[0].equals("") && splited[0] != null && !splited[0].isEmpty()) {
            if (size == 1) {
                charfirst = splited[0].trim();
                mname = charfirst.substring(0, 1);
            } else {
                charfirst = splited[0].trim();
                String charlast = splited[size - 1].trim();
                mname = charfirst.substring(0, 1) + charlast.substring(0, 1);
            }
            Upname = mname.toUpperCase();

        } else {
            Upname = " ";
        }
        Bitmap bm = G.utileProg.drawAlphabetOnPicture(img.getLayoutParams().width, Upname, "#ffffff");
        return bm;
    }
}
