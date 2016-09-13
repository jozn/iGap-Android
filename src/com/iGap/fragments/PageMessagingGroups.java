// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.fragments;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.iGap.R;
import com.iGap.adapter.G;
import com.iGap.adapter.GroupRecycleAdapter;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.SlidingTabsColorsFragment;
import com.iGap.services.TimerServies;


/**
 * 
 * for show list of all item contain group and chat and channel and update view if change
 *
 */

public class PageMessagingGroups extends Fragment {

    private ArrayList<String>          gchatidarray          = new ArrayList<String>();
    private ArrayList<String>          gchatnamearray        = new ArrayList<String>();
    private ArrayList<String>          gchatmembershiparray  = new ArrayList<String>();
    private ArrayList<String>          gchatlastmessagearray = new ArrayList<String>();
    private ArrayList<String>          gchatlastdatearray    = new ArrayList<String>();
    private ArrayList<String>          gchatdescriptionarray = new ArrayList<String>();
    private ArrayList<String>          gchatavatararray      = new ArrayList<String>();
    private ArrayList<String>          gchatavatararrayHq    = new ArrayList<String>();
    private ArrayList<String>          gchatsoundarray       = new ArrayList<String>();
    private ArrayList<String>          gchatunreadarray      = new ArrayList<String>();
    private ArrayList<String>          gchatactivearray      = new ArrayList<String>();
    private ArrayList<Boolean>         visibleView           = new ArrayList<Boolean>();

    private String                     day                   = "";
    private String                     month                 = "";
    private String                     year                  = "";

    private boolean                    success;
    private boolean                    onScrollListener;

    private RecyclerView               lvmessagegroups;
    private RecyclerView.LayoutManager mLayoutManager;
    private Dialog                     pDialog;

    private View                       view;
    private ImageLoader1               il;
    private LinearLayout               lytEmpty;
    private GroupRecycleAdapter        mAdapter;
    private Handler                    h1;
    public static Context              mcContext;
    private JSONParser                 jParser               = new JSONParser();


    public static PageMessagingGroups newInstance(String page) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("key_page", page);
        PageMessagingGroups fragment = new PageMessagingGroups();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_messaging_groups, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mcContext = getActivity();
        this.view = view;
        initUI();
        loadinfo(getActivity());
    }


    private void initUI() {
        lytEmpty = (LinearLayout) view.findViewById(R.id.lyt_empty);
        lvmessagegroups = (RecyclerView) view.findViewById(R.id.lv_message_groups);
        lvmessagegroups.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        lvmessagegroups.setLayoutManager(mLayoutManager);
        TimerServies ts = new TimerServies();
        String currenttime;
        try {

            currenttime = ts.getDateTime();
        }
        catch (Exception e) {
            HelperGetTime helperGetTime = new HelperGetTime();
            currenttime = helperGetTime.getTime();
        }

        String[] splitedtime = currenttime.split("\\s+");
        String date = splitedtime[0];
        String[] splited_date = date.split("-");
        year = splited_date[0];
        month = splited_date[1];
        day = splited_date[2];

        il = new ImageLoader1(getActivity(), G.basicAuth);

        lvmessagegroups.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (G.messagingPageNumber == 3 && onScrollListener) {

                    if (dy > 0) {
                        if (G.showButton) { // agar button namayesh dade mishavad
                            G.showButton = false;
                            Intent intent = new Intent("HIDE_BUTTON");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        }
                    } else {
                        if ( !G.showButton) { // agar button namayesh dade nemishavad
                            G.showButton = true;
                            Intent intent = new Intent("SHOW_BUTTON");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        }
                    }
                }
            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("loadMessageingGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSound, new IntentFilter("updateSoundGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deleteWithUid, new IntentFilter("deleteWithUidGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(clearHistoryWithUid, new IntentFilter("clearHistoryWithUid"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(addNewGroup, new IntentFilter("addNewGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newPostGroup, new IntentFilter("newPostGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(kikedFromGroup, new IntentFilter("kikedFromGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSeenGroup, new IntentFilter("updateSeenGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateAvatarGroup, new IntentFilter("updateAvatarGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(emptyListGroup, new IntentFilter("EMPTY_LIST_GROUP"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateGroupItem, new IntentFilter("updateGroupItem"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(ActiveItemGroup, new IntentFilter("ActiveItemGroup"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateGroupMembers, new IntentFilter("updateGroupMembers"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateDateType, new IntentFilter("updateDateType"));
    }


    @Override
    public void onDestroy() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        super.onDestroy();
    }

    private BroadcastReceiver updateDateType      = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {
                                                          String hijri = intent.getStringExtra("hijri");
                                                          mAdapter.changeDateType(hijri);
                                                      }
                                                  };

    private BroadcastReceiver updateGroupMembers  = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {
                                                          String groupID = intent.getStringExtra("groupID");
                                                          mAdapter.updateGroupMembersNumber(groupID);
                                                      }
                                                  };

    private BroadcastReceiver ActiveItemGroup     = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {

                                                          String uid = intent.getStringExtra("uid");
                                                          String active = intent.getStringExtra("active");
                                                          String lastmsg = intent.getStringExtra("lastmsg");
                                                          String lastdate = intent.getStringExtra("lastdate");

                                                          mAdapter.activeItemgroup(uid, active, lastmsg, lastdate);
                                                      }
                                                  };

    private BroadcastReceiver updateGroupItem     = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {

                                                          String uid = intent.getStringExtra("uid");
                                                          String name = intent.getStringExtra("name");
                                                          String description = intent.getStringExtra("description");
                                                          String avatar_lq = intent.getStringExtra("avatar_lq");
                                                          String avatar_hq = intent.getStringExtra("avatar_hq");
                                                          String membership = intent.getStringExtra("membership");

                                                          mAdapter.updateItemgroup(uid, name, description, avatar_lq, avatar_hq, membership);
                                                      }
                                                  };

    private BroadcastReceiver mMessageReceiver    = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {
                                                          loadinfo(context);
                                                      }
                                                  };
    private BroadcastReceiver updateSound         = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {

                                                          String uid = intent.getStringExtra("uid");
                                                          String value = intent.getStringExtra("value");
                                                          updatesoundwithuid(uid, value);
                                                      }
                                                  };
    private BroadcastReceiver updateAvatarGroup   = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {

                                                          String uid = intent.getStringExtra("uid");
                                                          String avatarlq = intent.getStringExtra("avatarlq");
                                                          updateavatar(uid, avatarlq);
                                                      }
                                                  };
    private BroadcastReceiver deleteWithUid       = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {
                                                          String uid = intent.getStringExtra("uid");
                                                          deletewithuid(uid);
                                                      }
                                                  };
    private BroadcastReceiver clearHistoryWithUid = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {
                                                          String uid = intent.getStringExtra("uid");
                                                          clearhistorywithuid(uid);
                                                      }
                                                  };
    private BroadcastReceiver addNewGroup         = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {

                                                          String uid = intent.getStringExtra("uid");
                                                          String name = intent.getStringExtra("name");
                                                          String description = intent.getStringExtra("description");
                                                          String avatar_lq = intent.getStringExtra("avatar_lq");
                                                          String avatar_Hq = intent.getStringExtra("avatar_hq");
                                                          String lastmsg = intent.getStringExtra("lastmsg");
                                                          String lastdate = intent.getStringExtra("lastdate");
                                                          String membership = intent.getStringExtra("membership");

                                                          addnewgroup(uid, name, description, avatar_lq, avatar_Hq, lastmsg, lastdate, membership);

                                                      }
                                                  };
    private BroadcastReceiver newPostGroup        = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {

                                                          String uid = intent.getStringExtra("uid");
                                                          String lastmsg = intent.getStringExtra("lastmsg");
                                                          String lastdate = intent.getStringExtra("lastdate");
                                                          newpostgroup(uid, lastmsg, lastdate);
                                                      }
                                                  };
    private BroadcastReceiver kikedFromGroup      = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {

                                                          String uid = intent.getStringExtra("uid");
                                                          String lastmsg = intent.getStringExtra("lastmsg");
                                                          String lastdate = intent.getStringExtra("lastdate");
                                                          kikedfromgroup(uid, lastmsg, lastdate);
                                                      }
                                                  };
    private BroadcastReceiver updateSeenGroup     = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {

                                                          String uid = intent.getStringExtra("uid");
                                                          updateseen(uid);
                                                      }
                                                  };
    private BroadcastReceiver emptyListGroup      = new BroadcastReceiver() {

                                                      @Override
                                                      public void onReceive(Context context, Intent intent) {
                                                          showDummy();
                                                      }
                                                  };


    private void loadinfo(Context context) {

        try {
            gchatidarray.clear();
            gchatnamearray.clear();
            gchatmembershiparray.clear();
            gchatlastmessagearray.clear();
            gchatlastdatearray.clear();
            gchatdescriptionarray.clear();
            gchatavatararray.clear();
            gchatavatararrayHq.clear();
            gchatsoundarray.clear();
            gchatunreadarray.clear();
            gchatactivearray.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int size = G.cmd.getRowCount("groupchatrooms");
        if (size == 0) {
            lvmessagegroups.setVisibility(View.GONE);
            lytEmpty.setVisibility(View.VISIBLE);
        } else {
            if (lvmessagegroups.getVisibility() == View.GONE) {
                lvmessagegroups.setVisibility(View.VISIBLE);
                lytEmpty.setVisibility(View.GONE);

            }

            Cursor cursor = G.cmd.selectPageMessagingByTime("groupchatrooms", "lastdate");

            while (cursor.moveToNext()) {
                String gchatid = cursor.getString(cursor.getColumnIndex("groupchatid"));
                String gchatname = cursor.getString(cursor.getColumnIndex("groupname"));
                String gchatmembership = cursor.getString(cursor.getColumnIndex("membership"));
                String gchatavatar = cursor.getString(cursor.getColumnIndex("groupavatar"));
                String gchatavatarHq;
                try {
                    gchatavatarHq = cursor.getString(cursor.getColumnIndex("groupavatarhq"));
                }
                catch (Exception e) {
                    gchatavatarHq = gchatavatar;
                }
                String gchatlastmessage = cursor.getString(cursor.getColumnIndex("lastmessage"));
                String gchatlastdate = cursor.getString(cursor.getColumnIndex("lastdate"));
                String gchatdescription = cursor.getString(cursor.getColumnIndex("description"));
                String gchatsound = cursor.getString(cursor.getColumnIndex("sound"));
                String gchatActive = cursor.getString(cursor.getColumnIndex("active"));

                int unread = G.cmd.getRowCountunreadgroupchat(gchatid);
                gchatunreadarray.add(String.valueOf(unread));
                gchatidarray.add(gchatid);
                gchatnamearray.add(gchatname);
                gchatmembershiparray.add(gchatmembership);
                gchatlastmessagearray.add(gchatlastmessage);
                gchatlastdatearray.add(gchatlastdate);
                gchatdescriptionarray.add(gchatdescription);
                gchatavatararray.add(gchatavatar);
                gchatavatararrayHq.add(gchatavatarHq);
                gchatactivearray.add(gchatActive);
                gchatsoundarray.add(gchatsound);
            }

        }

        if (context == null)
            context = G.context;

        mAdapter = new GroupRecycleAdapter(gchatidarray, gchatnamearray, gchatmembershiparray, gchatlastmessagearray, gchatlastdatearray, gchatdescriptionarray, gchatavatararray, gchatavatararrayHq, gchatsoundarray, gchatunreadarray, gchatactivearray, context, year, month, day, il, view);

        lvmessagegroups.setAdapter(mAdapter);

        if (visibleView.size() != gchatidarray.size()) {

            int ekhtelaf = gchatidarray.size() - visibleView.size();

            for (int i = 0; i < ekhtelaf; i++) {
                visibleView.add(false);
            }
        }
        try {
            if ( !gchatidarray.isEmpty()) {
                Runnable fitsOnScreen = new Runnable() {

                    @Override
                    public void run() {

                        int last = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                        if (last == lvmessagegroups.getAdapter().getItemCount() - 1 && lvmessagegroups.getChildAt(last).getBottom() <= lvmessagegroups.getHeight()) {
                            // It fits!
                            onScrollListener = false;
                        } else {
                            // It doesn't fit...
                            onScrollListener = true;
                        }
                    }
                };

                lvmessagegroups.post(fitsOnScreen);
            }
        }
        catch (Exception e) {
            onScrollListener = true;
        }

    }


    class deletegroup extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(getActivity());

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
            String pos = args[0];
            try {
                String[] splited = gchatidarray.get(Integer.parseInt(pos)).split("@");
                String id = splited[0];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.deletegroup + id, params, "DELETE", G.basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {

                        G.cmd.cleargroupchathistory(gchatidarray.get(Integer.parseInt(pos)));
                        G.cmd.deletegroupchatrooms(gchatidarray.get(Integer.parseInt(pos)));
                    } else {
                        if (statuscode.equals("403")) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), getString(R.string.chatroom_has_member_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (success == true) {
                loadinfo(getActivity());
            }
        }

    }


    class deletegroupmember extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(getActivity());

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
            String pos = args[0];
            try {
                String[] splited = gchatidarray.get(Integer.parseInt(pos)).split("@");
                String id = splited[0];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONArray array = new JSONArray();
                array.put(G.username);
                JSONObject jsonobj = jParser.getJSONFromUrl(G.deletegroup + id + "/members", params, "DELETE-BODY", G.basicAuth, array.toString());

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {

                        G.cmd.cleargroupchathistory(gchatidarray.get(Integer.parseInt(pos)));
                        G.cmd.deletegroupchatrooms(gchatidarray.get(Integer.parseInt(pos)));
                    } else {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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
                loadinfo(getActivity());
            }
        }
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

            if (SlidingTabsColorsFragment.btnCreate != null) {
                G.showButton = true;
                G.showCreateButton = true;
                SlidingTabsColorsFragment.btnCreate.setVisibility(View.VISIBLE);
                SlidingTabsColorsFragment.btnCreate.setBackgroundResource(R.drawable.circle_shadow);
                SlidingTabsColorsFragment.btnCreate.setText(getString(R.string.fa_plusa));
            }
            G.messagingPageNumber = 3;
        }
    }


    private void showDummy() {
        lvmessagegroups.setVisibility(View.GONE);
        lytEmpty.setVisibility(View.VISIBLE);
    }


    public void updatesoundwithuid(String uid, String value) {
        mAdapter.updateSoundWithUid(uid, value);
    }


    public void deletewithuid(String uid) {

        mAdapter.deleteWithUid(uid);

    }


    public void clearhistorywithuid(String uid) {
        mAdapter.clearehistorywithuid(uid);
    }


    public void addnewgroup(String uid, String name, String description, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {

        if (mAdapter.getItemCount() == 0) {
            lvmessagegroups.setVisibility(View.VISIBLE);
            lytEmpty.setVisibility(View.GONE);
        }
        mAdapter.insert(uid, name, description, avatar_lq, avatar_hq, lastmsg, lastdate, membership);

    }


    public void newpostgroup(String uid, String lastmsg, String lastdate) {
        mAdapter.newPost(uid, lastmsg, lastdate);
    }


    public void kikedfromgroup(String uid, String lastmsg, String lastdate) {
        mAdapter.kiked(uid, lastmsg, lastdate);
    }


    public void updateseen(String uid) {
        mAdapter.updateSeen(uid);
    }


    public void updateavatar(String uid, String avatarlq) {
        mAdapter.updateAvatar(uid, avatarlq);
    }

}
