package com.iGap.helpers;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.AddMemberToGroup;
import com.iGap.Channel;
import com.iGap.R;
import com.iGap.adapter.ChannelAdapter;
import com.iGap.adapter.G;
import com.iGap.instruments.JSONParser;
import com.iGap.interfaces.OnComplet;
import com.iGap.services.MyService;
import com.iGap.services.TimerServies;


public class HelperDialog {

    private String        channelName;
    private String        channelDesc;
    private String        channelUid;

    private String        groupdesc;
    private String        groupname;

    private TimerServies  ts;
    private HelperGetTime helperGetTime;
    private String        time;

    private Dialog        pDialog;

    private Context       context;

    private JSONParser    jParser = new JSONParser();

    private MyService     mService;


    public HelperDialog(Context context) {

        this.context = context;

        helperGetTime = new HelperGetTime();
        ts = new TimerServies();

        initDialogWaiting();

    }


    /**
     * init dialog waiting sun
     */
    private void initDialogWaiting() {

        pDialog = new Dialog(context);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    }


    /**
     * show layout dialog crate channel
     */

    @SuppressWarnings("deprecation")
    public void dialogcreatechannels() {

        final Dialog dialog = new Dialog(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_channels, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        final EditText edtchannelName = (EditText) dialogView.findViewById(R.id.edt_channelName);
        final EditText edtchannelDesc = (EditText) dialogView.findViewById(R.id.edt_channelDesc);
        final EditText edtchannelUid = (EditText) dialogView.findViewById(R.id.edt_channelUid);
        TextView txt_channel = (TextView) dialogView.findViewById(R.id.txt_channel);
        txt_channel.setTypeface(G.robotoBold);

        TextView txt_channel_text = (TextView) dialogView.findViewById(R.id.txt_channel_text);
        txt_channel_text.setTypeface(G.robotoBold);

        TextView txt_channel_name = (TextView) dialogView.findViewById(R.id.txt_channel_name);
        txt_channel_name.setTypeface(G.robotoBold);

        TextView txt_channel_des = (TextView) dialogView.findViewById(R.id.txt_channel_des);
        txt_channel_des.setTypeface(G.robotoBold);

        TextView txt_channel_nick_name = (TextView) dialogView.findViewById(R.id.txt_channel_nick_name);
        txt_channel_nick_name.setTypeface(G.robotoBold);
        Button btncreate = (Button) dialogView.findViewById(R.id.btn_create);
        btncreate.setTypeface(G.robotoBold);
        btncreate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                channelName = edtchannelName.getText().toString();
                channelDesc = edtchannelDesc.getText().toString();
                channelUid = edtchannelUid.getText().toString();

                if (channelName != null && !channelName.isEmpty() && !channelName.equals("null") && !channelName.equals("")) {
                    if (channelDesc != null && !channelDesc.isEmpty() && !channelDesc.equals("null") && !channelDesc.equals("")) {
                        if (channelUid != null && !channelUid.isEmpty() && !channelUid.equals("null") && !channelUid.equals("")) {

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            pDialog.show();
                            createChannel();

                        } else {
                            Toast.makeText(context, context.getString(R.string.please_enter_channel_uid_en), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, context.getString(R.string.please_enter_channel_description_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.please_enter_channel_name_en), Toast.LENGTH_SHORT).show();
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        lp.width = (display.getWidth());
        dialog.getWindow().setAttributes(lp);

        dialog.show();

    }


    /**
     * crate a new channel
     */

    private void createChannel() {
        new ChannelAdapter(G.context).createChannel(channelUid, channelName, channelDesc, new OnComplet() {

            @Override
            public void complet(Boolean result, final String message) {

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                if (result == false) {

                    ((Activity) context).runOnUiThread(new Runnable() {

                        public void run() {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    String currentTimea;
                    try {
                        currentTimea = ts.getDateTime();
                    }
                    catch (Exception e) {
                        currentTimea = helperGetTime.getTime();
                    }
                    String currenttime = currentTimea;

                    //yani sakhte shod
                    G.cmd.addchannel(channelUid, channelName, channelDesc, "1", "", "", "you created this channel", currenttime, "0", "1");
                    G.cmd.addchannelhistory(channelUid, "first", "you created this channel", currenttime, "1", "", "1", "0", "1", "0", "");
                    NewChannel(channelUid, channelName, channelDesc, "1", "", "you created this channel", currenttime, "1");
                    NewChannelSendToAll("3", channelUid, channelName, channelDesc, "1", "", "you created this channel", currenttime, "1");
                    Intent intent = new Intent(context, Channel.class);
                    intent.putExtra("channeluid", channelUid);
                    intent.putExtra("channelName", channelName);
                    intent.putExtra("channelDesc", channelDesc);
                    intent.putExtra("channelavatarlq", "");
                    intent.putExtra("channelmembership", "1");
                    intent.putExtra("channelmembersnumber", "1");
                    intent.putExtra("channelactive", "1");
                    context.startActivity(intent);

                    //                    Intent intent2 = new Intent("loadChannelInfo");
                    //                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent2);

                }
            }
        });
    }


    /**
     * 
     * show layout dialog create group
     */

    @SuppressWarnings("deprecation")
    public void dialogcreategroup(MyService mService) {

        this.mService = mService;

        final Dialog dialog = new Dialog(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_group, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        final EditText edtgroupName = (EditText) dialogView.findViewById(R.id.edt_groupName);
        final EditText edtgroupDesc = (EditText) dialogView.findViewById(R.id.edt_groupDesc);

        TextView txtgroup = (TextView) dialogView.findViewById(R.id.txt_group);
        txtgroup.setTypeface(G.robotoBold);

        TextView txt_group_name = (TextView) dialogView.findViewById(R.id.txt_group_name);
        txt_group_name.setTypeface(G.robotoBold);

        TextView txt_group_text = (TextView) dialogView.findViewById(R.id.txt_group_text);
        txt_group_text.setTypeface(G.robotoLight);

        TextView txt_group_des = (TextView) dialogView.findViewById(R.id.txt_group_des);
        txt_group_des.setTypeface(G.robotoBold);

        Button btncreate = (Button) dialogView.findViewById(R.id.btn_create);
        btncreate.setTypeface(G.robotoBold);
        btncreate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                groupdesc = edtgroupDesc.getText().toString();
                groupname = edtgroupName.getText().toString();

                if (groupname != null && !groupname.isEmpty() && !groupname.equals("null") && !groupname.equals("")) {
                    if (groupdesc != null && !groupdesc.isEmpty() && !groupdesc.equals("null") && !groupdesc.equals("")) {

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        String currentTimea;
                        try {
                            currentTimea = ts.getDateTime();
                        }
                        catch (Exception e) {
                            currentTimea = helperGetTime.getTime();
                        }
                        time = currentTimea;
                        new creategroup().execute();

                    } else {
                        Toast.makeText(context, context.getString(R.string.please_enter_group_description_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.please_enter_group_name_en), Toast.LENGTH_SHORT).show();
                }

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        lp.width = (display.getWidth());
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }


    /**
     * 
     * create new group
     *
     */
    class creategroup extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", groupname));
                params.add(new BasicNameValuePair("description", groupdesc));

                JSONObject jsonobj = jParser.getJSONFromUrl(G.creategroupchat, params, "POST", G.basicAuth, null);

                try {

                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {

                        JSONObject result = json.getJSONObject("result");
                        String id = result.getString("id") + "@group.igap.im";

                        mService.joinroom(id, groupname, groupdesc, "", "", "You've created this group", time, "1");

                        G.cmd.Addgroupchat(id, groupname, "1", "1", "", "", "You've created this group", time, groupdesc);
                        G.cmd.addgroupchathistory("", id, "empty", "You Created this group", "1", time, "2", "empty", "empty", "100", null, null, null, G.username, "", "", "");
                        //      NewChannelSendToAll("2", id, groupname, groupdesc, "1", "", "You Created this group", time, "1");

                        Intent intent = new Intent(context, AddMemberToGroup.class);
                        intent.putExtra("id", id);
                        intent.putExtra("name", groupname);
                        intent.putExtra("groupdesc", groupdesc);
                        intent.putExtra("gchmembership", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                    } else {

                        if (statuscode.equals("400")) {
                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(context, context.getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(context, context.getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                catch (JSONException e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(context, context.getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(context, context.getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    /**
     * broad cast to update list channel in page messaging all
     */

    private void NewChannelSendToAll(String model, String uid, String name, String description, String membersnumbers, String avatar_lq, String lastmsg, String lastdate, String membership) {

        Intent intentAll = new Intent("addNewInAll");
        intentAll.putExtra("MODEL", model); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("NAME", name);
        intentAll.putExtra("DESC", description);
        intentAll.putExtra("MEMBERS_NUMBER", membersnumbers);
        intentAll.putExtra("MEMBER_SHIP", membership);
        intentAll.putExtra("AVATAR_LQ", avatar_lq);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentAll);
    }

}
