// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.helpers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.iGap.Channel;
import com.iGap.R;
import com.iGap.adapter.G;
import com.iGap.instruments.CalculationUtil;
import com.iGap.instruments.JSONParser;
import com.iGap.services.TimerServies;


public class PageMessagingPopularFunction {

    private Context    mContext;
    private Dialog     pDialog;
    private JSONParser jParser = new JSONParser();


    //==================================================ViewOn And ViewOff=============================================

    public void viewOn(LinearLayout firstLayout, LinearLayout secondLayout, int position) {
        WindowManager windowManager = (WindowManager) G.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        TranslateAnimation tAnim = new TranslateAnimation(0, -width, 0, 0);
        tAnim.setDuration(300);
        firstLayout.setVisibility(View.GONE);
        secondLayout.setVisibility(View.VISIBLE);
        TranslateAnimation tAnim2 = new TranslateAnimation(width, 0, 0, 0);
        tAnim2.setDuration(300);
        secondLayout.setAnimation(tAnim2);
    }


    public void viewOff(LinearLayout firstLayout, LinearLayout secondLayout, int position) {
        WindowManager windowManager = (WindowManager) G.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        TranslateAnimation tAnim = new TranslateAnimation(0, width, 0, 0);
        tAnim.setDuration(300);
        secondLayout.setVisibility(View.GONE);
        firstLayout.setVisibility(View.VISIBLE);
        TranslateAnimation tAnim2 = new TranslateAnimation( -width, 0, 0, 0);
        tAnim2.setDuration(300);
        firstLayout.setAnimation(tAnim2);
    }


    //==========================================Draw Image To Text====================================================

    /**
     * 
     * get a text and return string span contain image or link or dash
     */
    public SpannableStringBuilder parseText(String text, String state, Boolean withLink, Context mcContext, int position) {
        mContext = mcContext;

        while (true) {
            if (text.endsWith("\n"))
                text = text.substring(0, text.length() - 1);
            else
                break;

        }

        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (text.length() > 0) {
            String[] str = text.split("].png");

            int count = 0;
            Pattern p = Pattern.compile("].png");
            Matcher m = p.matcher(text);
            while (m.find()) {
                count++;
            }

            int index = -1;
            for (int i = 0; i < str.length; i++) {
                index = str[i].lastIndexOf("[");
                if (index >= 0) {

                    if (withLink) {
                        textWithLink(str[i].substring(0, index), builder);
                        textWithHash(str[i].substring(0, index), builder, position);
                        textWithUrl(str[i].substring(0, index), builder);
                    }
                    else
                        builder.append(str[i].substring(0, index));

                    if (i < count) {
                        drawImagetoTextView(builder, str[i].substring(index, str[i].length()) + "].png", state);
                    }
                    else {
                        if (withLink) {
                            textWithLink(str[i].substring(index, str[i].length()), builder);
                            textWithHash(str[i].substring(index, str[i].length()), builder, position);
                            textWithUrl(str[i].substring(index, str[i].length()), builder);
                        }
                        else {
                            builder.append(str[i].substring(index, str[i].length()));
                        }
                    }
                }
                else if (str[i].length() > 0) {

                    if (withLink) {
                        textWithLink(str[i].toString(), builder);
                        textWithHash(str[i].toString(), builder, position);
                        textWithUrl(str[i].toString(), builder);
                    }
                    else
                        builder.append(str[i].toString());

                }
            }
        }
        return builder;
    }


    private void drawImagetoTextView(SpannableStringBuilder builder, String picName, String state) {

        try {

            float dp = 20;
            if (state.equals("CHAT_LIST")) {
                dp = 24;
            } else if (state.equals("PAGE_MESSAGING_LIST")) {
                dp = 15;
            }

            InputStream inInputStream = G.context.getAssets().open("smile/" + picName);
            Drawable drawable = Drawable.createFromStream(inInputStream, null);
            int SmileZize = CalculationUtil.convertDpToPx(dp, G.context);
            drawable.setBounds(0, 0, SmileZize, SmileZize);
            builder.append(picName);
            builder.setSpan(new ImageSpan(drawable), builder.length() - picName.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void textWithLink(String text, SpannableStringBuilder builder) {

        String s = "";
        String tmp = "";
        Boolean isAT = false;

        if (text != null)
            if (text.length() > 0) {

                for (int i = 0; i < text.length(); i++) {

                    s = text.substring(i, i + 1);

                    if (s.equals("@")) {
                        isAT = true;
                        if (tmp.equals(""))
                            tmp = "@";
                        else {
                            insertString(builder, tmp);
                            tmp = "@";
                        }
                        continue;
                    }

                    if (isAT) {
                        if (s.equals("!") || s.equals("#") || s.equals("$") || s.equals("%") || s.equals("^") || s.equals("&") ||
                                s.equals("(") || s.equals(")") || s.equals("-") || s.equals("+") || s.equals("=") || s.equals("!") ||
                                s.equals("`") || s.equals("{") || s.equals("}") || s.equals("[") || s.equals("]") || s.equals(";") ||
                                s.equals(":") || s.equals("'") || s.equals("?") || s.equals("<") || s.equals(">") || s.equals(",") ||
                                s.equals("\\") || s.equals("|") || s.equals("//") || s.equals(" ")) {

                            insertLinkString(builder, tmp);

                            tmp = "";
                            isAT = false;
                        }

                        else {
                            tmp += s;
                        }

                    }

                    if ( !isAT) {
                        insertString(builder, s);
                    }
                }

                if (isAT) {
                    if (tmp.equals("@"))
                        insertString(builder, tmp);
                    else
                        insertLinkString(builder, tmp);
                }
                else
                    insertString(builder, tmp);
            }

    }


    private void insertString(SpannableStringBuilder builder, String text) {

        builder.append(text);
    }


    private void insertLinkString(SpannableStringBuilder builder, final String text) {

        builder.append(text);

        builder.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View arg0) {
                String[] uid = text.split("@");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new CheckChanneluid().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid[1]);
                } else {
                    new CheckChanneluid().execute(uid[1]);
                }
            }
        }, builder.length() - text.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    private void textWithHash(String text, SpannableStringBuilder builder, int position) {

        String s = "";
        String tmp = "";
        Boolean isHash = false;
        int start = 0;

        if (text != null)
            if (text.length() > 0) {

                for (int i = 0; i < text.length(); i++) {

                    s = text.substring(i, i + 1);

                    if (s.equals("#")) {
                        isHash = true;
                        tmp = "";
                        start = i;
                        continue;
                    }

                    if (isHash) {
                        if (s.equals("!") || s.equals("@") || s.equals("$") || s.equals("%") || s.equals("^") || s.equals("&") ||
                                s.equals("(") || s.equals(")") || s.equals("-") || s.equals("+") || s.equals("=") || s.equals("!") ||
                                s.equals("`") || s.equals("{") || s.equals("}") || s.equals("[") || s.equals("]") || s.equals(";") ||
                                s.equals(":") || s.equals("'") || s.equals("?") || s.equals("<") || s.equals(">") || s.equals(",") ||
                                s.equals("\\") || s.equals("|") || s.equals("//") || s.equals(" ")) {

                            insertHashLink(tmp, builder, start, text.length(), position);

                            tmp = "";
                            isHash = false;
                        }

                        else {
                            tmp += s;
                        }

                    }

                }

                if (isHash) {
                    if ( !tmp.equals(""))
                        insertHashLink(tmp, builder, start, text.length(), position);
                }
            }

    }


    private void insertHashLink(final String text, SpannableStringBuilder builder, int start, int length, final int position) {

        builder.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View arg0) {
                sendHash(text, position);
            }
        }, builder.length() - length + start, builder.length() - length + start + text.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    private void textWithUrl(String text, SpannableStringBuilder builder) {

        String tmp[] = text.replaceAll("\n", " ").split(" ");

        int start = 0;
        int end = 0;

        if (tmp == null)
            return;
        if (tmp.length < 1)
            return;

        for (int i = 0; i < tmp.length; i++) {

            start = end;

            if (tmp[i].length() < 1) {
                continue;
            } else {

                end += tmp[i].length();

                final String s = tmp[i];

                if (s.contains(".org") || s.contains(".net") || s.contains(".com") || s.contains(".ir") ||
                        s.contains(".edu") || s.contains(".gov") || s.contains(".int") || s.contains(".inf") ||
                        s.contains(".biz") || s.contains(".web") || s.contains(".ws") || s.contains(".us") ||
                        s.contains(".tv") || s.contains(".museum") || s.contains(".aero") || s.contains(".mil") ||
                        s.contains(".cat") || s.contains(".coop") || s.contains(".jobs") || s.contains(".mobi") ||
                        s.contains(".pro") || s.contains(".travel"))
                {

                    builder.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View arg0) {}
                    }, builder.length() - text.length() + start + i, builder.length() - text.length() + end + i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }
        }
    }


    private void sendHash(String text, int position) {

        if (G.hashSearchType == 1) {
            Intent intent = new Intent("hashSearchSingleChat");
            intent.putExtra("TEXT", text);
            intent.putExtra("POSITION", position);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        } else if (G.hashSearchType == 2) {
            Intent intent = new Intent("hashSearchGroupChat");
            intent.putExtra("TEXT", text);
            intent.putExtra("POSITION", position);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        } else if (G.hashSearchType == 3) {
            Intent intent = new Intent("hashSearchChannel");
            intent.putExtra("TEXT", text);
            intent.putExtra("POSITION", position);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }


    private SpannableStringBuilder setAlinment(SpannableStringBuilder builder) {

        String text = builder.toString();

        int start = 0;
        int end = 0;

        String arrayText[] = text.split("\n");

        if (arrayText == null)
            return builder;

        int count = arrayText.length;

        String Normal = "left";

        if (isRightAlinment(arrayText[0]))
            Normal = "right";

        for (int i = 0; i < count; i++) {

            String str = arrayText[i];

            start = end;
            end = start + str.length();

            if (str.length() > 0) {

                if (isRightAlinment(str) && Normal.equals("left")) {

                    builder.setSpan(new AlignmentSpan.Standard(Alignment.ALIGN_OPPOSITE), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
                else if (isRightAlinment(str) && Normal.equals("right")) {

                    builder.setSpan(new AlignmentSpan.Standard(Alignment.ALIGN_NORMAL), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                else if ( !isRightAlinment(str) && Normal.equals("left")) {

                    builder.setSpan(new AlignmentSpan.Standard(Alignment.ALIGN_NORMAL), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                else if ( !isRightAlinment(str) && Normal.equals("right")) {

                    builder.setSpan(new AlignmentSpan.Standard(Alignment.ALIGN_OPPOSITE), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }

            end++;

        }

        return builder;
    }


    private Boolean isRightAlinment(String Text) {

        if (Text == null)
            return false;

        if (Text.length() < 1)
            return false;

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
            return true;

        } else {

            return false;
        }

    }


    private Boolean isCharacter(String s) {

        int code = s.codePointAt(0);

        if ((code > 64 && code < 91) || (code > 97 && code < 122) || (code > 1560 && code < 1920)) {

            return true;
        }
        return false;
    }


    private class CheckChanneluid extends AsyncTask<String, String, String> {

        private Dialog     pDialog;
        private JSONParser jParser = new JSONParser();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(mContext);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(pDialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            pDialog.getWindow().setAttributes(layoutParams);
            pDialog.setCancelable(false);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                pDialog.setContentView(R.layout.dialog_wait);
            } else {
                pDialog.setContentView(R.layout.dialog_wait_simple);
            }
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            String uid = args[0];
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.addmembertochannel + uid + "/info", params, "GET", G.basicAuth, null);

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {
                        JSONObject result = json.getJSONObject("result");
                        String name = result.getString("name");
                        String description = result.getString("description");
                        String totalMember = result.getString("totalMember");
                        String avatarHq = result.getString("avatarHq");
                        String avatarLq = result.getString("avatarLq");
                        String role = "0";

                        if (G.cmd.isChannelExist("channels", uid) == 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                new joinchannel().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, name, description, totalMember, avatarLq, avatarHq);
                            } else {
                                new joinchannel().execute(uid, name, description, totalMember, avatarLq, avatarHq);
                            }

                        } else {
                            Intent intent = new Intent(mContext, Channel.class);
                            intent.putExtra("channeluid", uid);
                            intent.putExtra("channelName", name);
                            intent.putExtra("channelavatarlq", avatarLq);
                            intent.putExtra("channelmembership", role);
                            intent.putExtra("channelDesc", description);
                            intent.putExtra("channelmembersnumber", totalMember);
                            intent.putExtra("channelactive", "1");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }

                    } else {
                        ((Activity) mContext).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getString(R.string.success_false_channel_uid_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String v) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }


    private void NewChannel(String uid, String name, String description, String membersnumbers, String avatar_lq, String lastmsg, String lastdate, String membership) {
        Intent intent = new Intent("addNewChannel");
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("membership", membership);
        intent.putExtra("membersnumbers", membersnumbers);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    private void NewChannelSendToAll(String uid, String name, String description, String membersnumbers, String avatar_lq, String lastmsg, String lastdate, String membership) {

        Intent intentAll = new Intent("addNewInAll");
        intentAll.putExtra("MODEL", "3"); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("NAME", name);
        intentAll.putExtra("DESC", description);
        intentAll.putExtra("MEMBERS_NUMBER", membersnumbers);
        intentAll.putExtra("MEMBER_SHIP", membership);
        intentAll.putExtra("AVATAR_LQ", avatar_lq);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    class joinchannel extends AsyncTask<String, String, String> {

        boolean success;

        String  uid;
        String  name;
        String  description;
        String  totalMember;
        String  avatarLq;
        String  avatarHq;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... args) {
            try {
                uid = args[0];
                name = args[1];
                description = args[2];
                totalMember = args[3];
                avatarLq = args[4];
                avatarHq = args[5];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.addmembertochannel + uid + "/join", params, "POST", G.basicAuth, null);

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success = json.getBoolean(G.TAG_SUCCESS);
                    if (success == true) {

                        String time = null;
                        try {
                            time = new TimerServies().getDateTime();
                        }
                        catch (Exception e) {
                            HelperGetTime helperGetTime = new HelperGetTime();
                            time = helperGetTime.getTime();
                        }

                        String currentTimea = HelperGetTime.convertWithSingleTime(time, G.utcMillis);

                        G.cmd.addchannel(uid, name, description, totalMember, avatarLq, avatarHq, "You Joined to this channel", currentTimea, "0", "0");
                        G.cmd.addchannelhistory(uid, "ChannelInvite", "You Joined to this channel", currentTimea, "100", null, "0", "0", "", "0", "");
                        NewChannel(uid, name, description, totalMember, avatarLq, "You Joined to this channel", currentTimea, "0");
                        NewChannelSendToAll(uid, name, description, totalMember, avatarLq, "You Joined to this channel", currentTimea, "0");
                        Intent intent = new Intent(mContext, Channel.class);
                        intent.putExtra("channeluid", uid);
                        intent.putExtra("channelName", name);
                        intent.putExtra("channelavatarlq", avatarLq);
                        intent.putExtra("channelmembership", "0");
                        intent.putExtra("channelDesc", description);
                        intent.putExtra("channelmembersnumber", totalMember);
                        intent.putExtra("channelactive", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                    }

                }
                catch (JSONException e) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(mContext, mContext.getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

    }

}
