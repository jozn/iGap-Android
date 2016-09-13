package com.iGap.helpers;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.iGap.adapter.G;


public class HelperCheckUpadte {

    /**
     * check local db for detection exist new upadte for app or no
     * 
     * @return return 1 if need update otherwise return 0
     */

    public static String checkUpdate() {
        String versionCode = G.cmd.getsetting(8);
        if (versionCode == null) {
            versionCode = "0";
        }

        PackageInfo pInfo = null;
        try {
            pInfo = G.context.getPackageManager().getPackageInfo(G.context.getPackageName(), 0);
        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        int appVersionCode = pInfo.versionCode;
        int lastVersionCode = 0;
        try {
            lastVersionCode = Integer.parseInt(versionCode);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (appVersionCode == lastVersionCode) {
            G.cmd.updatever(0, "" + lastVersionCode);
        }

        String needUpdate = G.cmd.getsetting(7);
        return needUpdate;
    }

}
