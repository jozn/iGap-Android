package com.iGap.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.iGap.adapter.G;
import com.iGap.services.MyService;
import com.iGap.services.PublicService;
import com.iGap.services.SplashService;


/**
 * 
 * tashkhise inke etela'ati dakhele barname baraye eshterak gozari ersal shode va ya barname be surate mamul baz shode ast
 *
 */

public class HelperGetDataFromOtherApp {

    public enum FileType {
        message,
        video,
        file,
        audio,
        image
    }

    private Intent                    intent;

    public static boolean             hasSharedData = false;                    // after use intent set this to false
    public static FileType            messageType;
    public static String              message       = "";
    public static ArrayList<Uri>      messageFileAddress;
    public static ArrayList<FileType> fileTypeArray = new ArrayList<FileType>();


    public HelperGetDataFromOtherApp(Intent intent) {

        this.intent = intent;

        if (intent == null) {
            return;
        }

        checkData(intent);
    }


    /**
     * check exist new version or user registered before
     * 
     * @return true if MainActivity Should continue class And false if Should not continue
     */

    public boolean continueClass() {

        boolean continueProcess = true;

        String needUpdate = HelperCheckUpadte.checkUpdate();
        if (needUpdate.equals("1")) {
            continueProcess = false;
        }

        int islogin = G.cmd.getRowCount("info");
        if (islogin == 0) { // agar tabehal sabtenam nakarde bud be safheye splash ferestade mishavasd ta reval mamule sabtenam anjam shavad
            continueProcess = false;
        } else {
            G.context.startService(new Intent(G.context, MyService.class));
            G.context.startService(new Intent(G.context, SplashService.class));
            G.context.startService(new Intent(G.context, PublicService.class));
        }

        return continueProcess;
    }


    /**
     * check intent data and get type and address message
     * 
     * @param intent
     */
    private void checkData(Intent intent) {
        if (G.language.equals("0")) { // for select language  
            G.SelectedLanguage = "en";
        } else if (G.language.equals("1")) {
            G.SelectedLanguage = "fa";
        }

        String action = intent.getAction();
        String type = intent.getType();

        if (action == null || type == null)
            return;

        if (Intent.ACTION_SEND.equals(action)) {

            if (type.equals("text/plain")) {

                handleSendText(intent);
            }
            else if (type.startsWith("image/")) {

                SetOutPutSingleFile(FileType.image);

            }

            else if (type.startsWith("video/")) {

                SetOutPutSingleFile(FileType.video);

            }

            else if (type.startsWith("audio/")) {

                SetOutPutSingleFile(FileType.audio);

            }
            else {

                SetOutPutSingleFile(FileType.file);

            }

        }
        else if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {

            if (type.startsWith("image/")) {

                SetOutPutMultipleFile(FileType.image);
            }
            else if (type.startsWith("video/")) {

                SetOutPutMultipleFile(FileType.video);

            }
            else if (type.startsWith("audio/")) {

                SetOutPutMultipleFile(FileType.audio);

            }
            else {

                SetOutPutMultipleFile(FileType.file);
            }
        }

    }


    //*****************************************************************************************************

    private void SetOutPutSingleFile(FileType type) {

        Uri fileAddressUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (fileAddressUri != null) {
            hasSharedData = true;
            messageType = type;
            messageFileAddress = new ArrayList<Uri>();
            messageFileAddress.add(fileAddressUri);
        }

    }


    //*****************************************************************************************************

    private void SetOutPutMultipleFile(FileType type) {

        ArrayList<Uri> fileAddressUri = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (fileAddressUri != null) {

            hasSharedData = true;
            messageType = type;
            messageFileAddress = fileAddressUri;

            for (int i = 0; i < messageFileAddress.size(); i++) {
                FileType fileType = HelperGetFileInformation.getMimeType(fileAddressUri.get(i));
                fileTypeArray.add(fileType);
            }

        }
    }


    //*****************************************************************************************************

    void handleSendText(Intent intent) {

        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (sharedText != null) {

            hasSharedData = true;
            messageType = FileType.message;
            message = sharedText;
        }else {
            SetOutPutSingleFile(FileType.file);
        }

    }


    //*****************************************************************************************************

    /**
     * get every data in bundle from intent
     */
    private void getAllDAtaInIntent(Intent intent) {

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();

            while (it.hasNext()) {
                String key = it.next();
                Log.i("LOG", key + "=" + bundle.get(key));
            }
        }
    }


    public ArrayList<Uri> getInfo() {
        return messageFileAddress;
    }

}
