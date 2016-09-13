package com.iGap;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.G;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;
import com.iGap.services.TimerServies;


/**
 * 
 * jostejo dar kanal haye omumi
 */

public class ChannelSearch extends Activity {

    private ArrayList<String> uidarray;
    private ArrayList<String> namearray;
    private ArrayList<String> descriptionarray;
    private ArrayList<String> totalMembararray;
    private ArrayList<String> avatarHqarray;
    private ArrayList<String> avatarLqarray;
    private ArrayList<String> activearray;

    private Dialog            pDialog;
    private EditText          edtsearch;
    private ListView          channelslv;
    private ImageLoader1      il;
    private JSONParser        jParser    = new JSONParser();

    private String            searchtext = "";
    private boolean           success;
    private int               pos;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_channel_search);

        il = new ImageLoader1(this, G.basicAuth);

        channelslv = (ListView) findViewById(R.id.registeredlv);
        edtsearch = (EditText) findViewById(R.id.edt_search);
        Button btnsearchicon = (Button) findViewById(R.id.btn_searchicon);
        Button btnback = (Button) findViewById(R.id.btn_back);

        btnback.setTypeface(G.fontAwesome);
        btnsearchicon.setTypeface(G.fontAwesome);

        uidarray = new ArrayList<String>();
        namearray = new ArrayList<String>();
        descriptionarray = new ArrayList<String>();
        totalMembararray = new ArrayList<String>();
        avatarHqarray = new ArrayList<String>();
        avatarLqarray = new ArrayList<String>();
        activearray = new ArrayList<String>();

        btnback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnsearchicon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchtext = edtsearch.getText().toString();
                if (searchtext != null && !searchtext.isEmpty() && !searchtext.equals("")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new searchChannels().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new searchChannels().execute();
                    }
                }
            }
        });
    }


    class searchChannels extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(ChannelSearch.this);
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
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.channelsearch + searchtext, params, "GET", G.basicAuth, null);

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success = json.getBoolean(G.TAG_SUCCESS);
                    if (success == true) {

                        try {
                            uidarray.clear();
                            namearray.clear();
                            descriptionarray.clear();
                            totalMembararray.clear();
                            avatarHqarray.clear();
                            avatarLqarray.clear();
                            activearray.clear();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        JSONArray result = json.getJSONArray("result");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject response = result.getJSONObject(i);
                            String uid = response.getString("uid");
                            String name = response.getString("name");
                            String description = response.getString("description");
                            String totalMembar = response.getString("totalMember");
                            String avatarHq = response.getString("avatarHq");
                            String avatarLq = response.getString("avatarLq");
                            boolean active = response.getBoolean("active");
                            uidarray.add(uid);
                            namearray.add(name);
                            descriptionarray.add(description);
                            totalMembararray.add(totalMembar);
                            avatarHqarray.add(avatarHq);
                            avatarLqarray.add(avatarLq);

                            if (active) {
                                activearray.add("1");
                            } else {
                                activearray.add("2");
                            }

                        }

                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(ChannelSearch.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(ChannelSearch.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(ChannelSearch.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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

            if (success == true) {
                channelslv.setAdapter(new AA());
            }
        }

    }


    class joinchannel extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(ChannelSearch.this);
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
            try {

                String uid = args[0];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.addmembertochannel + uid + "/join", params, "POST", G.basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success = json.getBoolean(G.TAG_SUCCESS);

                    if ( !success) {
                        if (statuscode.equals("404")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(ChannelSearch.this, getString(R.string.channel_not_exist), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (statuscode.equals("403")) {
                            String errorStatus = "";
                            JSONObject result = json.getJSONObject("result");
                            errorStatus = result.getString("errorStatus");
                            if (errorStatus.equals("1")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(ChannelSearch.this, getString(R.string.channel_deleted), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (errorStatus.equals("2")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(ChannelSearch.this, getString(R.string.channel_closed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(ChannelSearch.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(ChannelSearch.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(ChannelSearch.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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
                G.cmd.addchannel(uidarray.get(pos), namearray.get(pos), descriptionarray.get(pos), totalMembararray.get(pos), avatarLqarray.get(pos), avatarHqarray.get(pos),
                        "You Joined to this channel", currentTimea, "0", "0");
                G.cmd.addchannelhistory(uidarray.get(pos), "ChannelInvite", "You Joined to this channel", currentTimea, "100", null, "0", "0", "", "0", "");
                NewChannel(uidarray.get(pos), namearray.get(pos), descriptionarray.get(pos), totalMembararray.get(pos), avatarLqarray.get(pos), avatarHqarray.get(pos), "You Joined to this channel",
                        currentTimea, "0");
                NewChannelSendToAll(uidarray.get(pos), namearray.get(pos), descriptionarray.get(pos), totalMembararray.get(pos), avatarLqarray.get(pos), avatarHqarray.get(pos),
                        "You Joined to this channel", currentTimea, "0");

                Intent intent = new Intent(ChannelSearch.this, Channel.class);
                intent.putExtra("channeluid", uidarray.get(pos));
                intent.putExtra("channelName", namearray.get(pos));
                intent.putExtra("channelavatarlq", avatarLqarray.get(pos));
                intent.putExtra("channelavatarhq", avatarHqarray.get(pos));
                intent.putExtra("channelmembership", "0");
                intent.putExtra("channelDesc", descriptionarray.get(pos));
                intent.putExtra("channelmembersnumber", totalMembararray.get(pos));
                intent.putExtra("channelactive", "1");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }


    class AA extends ArrayAdapter<String> {

        public AA() {
            super(ChannelSearch.this, R.layout.list_item_channels_search, uidarray);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater in = getLayoutInflater();
            View row = in.inflate(R.layout.list_item_channels_search, parent, false);

            LinearLayout lyt = (LinearLayout) row.findViewById(R.id.ll_first_channel);
            ImageView imgavatar = (ImageView) row.findViewById(R.id.img_avatar_channel);
            TextView txticon = (TextView) row.findViewById(R.id.txt_icon_channel);
            TextView txtname = (TextView) row.findViewById(R.id.txt_name_channel);
            TextView txtmembers = (TextView) row.findViewById(R.id.txt_members_channel);
            TextView txtdesc = (TextView) row.findViewById(R.id.txt_desc_channel);

            txticon.setTypeface(G.fontAwesome);
            txtname.setText(namearray.get(position));
            txtdesc.setText(descriptionarray.get(position));
            txtmembers.setText(totalMembararray.get(position));

            if (avatarLqarray.get(position) != null && !avatarLqarray.get(position).isEmpty() && !avatarLqarray.get(position).equals("null") && !avatarLqarray.get(position).equals("")) {
                il.DisplayImage(avatarLqarray.get(position), R.drawable.difaultimage, imgavatar);
            } else {
                HelperDrawAlphabet pf = new HelperDrawAlphabet();
                Bitmap bm = pf.drawAlphabet(ChannelSearch.this, namearray.get(position), imgavatar);
                imgavatar.setImageBitmap(bm);

            }

            lyt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    String role = "0";

                    if (G.cmd.isChannelExist("channels", uidarray.get(position)) == 0) {

                        pos = position;
                        new joinchannel().execute(uidarray.get(position));

                    } else {
                        Intent intent = new Intent(ChannelSearch.this, Channel.class);
                        intent.putExtra("channeluid", uidarray.get(position));
                        intent.putExtra("channelName", namearray.get(position));
                        intent.putExtra("channelavatarlq", avatarLqarray.get(position));
                        intent.putExtra("channelavatarhq", avatarHqarray.get(position));
                        intent.putExtra("channelmembership", role);
                        intent.putExtra("channelDesc", descriptionarray.get(position));
                        intent.putExtra("channelmembersnumber", totalMembararray.get(position));
                        intent.putExtra("channelactive", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }

                }
            });

            return (row);
        }
    }


    private void NewChannel(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {
        Intent intent = new Intent("addNewChannel");
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("avatar_hq", avatar_hq);
        intent.putExtra("membership", membership);
        intent.putExtra("membersnumbers", membersnumbers);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(ChannelSearch.this).sendBroadcast(intent);
    }


    private void NewChannelSendToAll(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {

        Intent intentAll = new Intent("addNewInAll");
        intentAll.putExtra("MODEL", "3"); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("NAME", name);
        intentAll.putExtra("DESC", description);
        intentAll.putExtra("MEMBERS_NUMBER", membersnumbers);
        intentAll.putExtra("MEMBER_SHIP", membership);
        intentAll.putExtra("AVATAR_LQ", avatar_lq);
        intentAll.putExtra("AVATAR_HQ", avatar_hq);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(ChannelSearch.this).sendBroadcast(intentAll);
    }
}
