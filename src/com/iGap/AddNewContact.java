// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.util.ArrayList;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.G;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.Utils;


/**
 * 
 * braye ezafe kardan yek shomareh telfone be list contact goshi karbar
 *
 */

public class AddNewContact extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_add_new_contact);
        Bundle extras = getIntent().getExtras();
        final String mobile = extras.getString("MOBILE");
        String userchatavatar = extras.getString("userchatavatar");
        Typeface fontawsome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        final EditText edt = (EditText) findViewById(R.id.edt_name);
        final EditText edtlastname = (EditText) findViewById(R.id.edt_lastname);

        Button btnsave = (Button) findViewById(R.id.btn_add);
        btnsave.setTypeface(fontawsome);

        Button btnback = (Button) findViewById(R.id.btn_back);
        btnback.setTypeface(fontawsome);

        btnback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView txtreplayfrom = (TextView) findViewById(R.id.txt_replay_from);
        txtreplayfrom.setText(mobile);

        ImageView imgreply = (ImageView) findViewById(R.id.img_reply);

        ImageLoader1 il = new ImageLoader1(AddNewContact.this, G.basicAuth);
        il.DisplayImage(userchatavatar, R.drawable.difaultimage, imgreply);
        btnsave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String DisplayName = edt.getText().toString();
                String lastname = edtlastname.getText().toString();
                String MobileNumber = mobile;

                if (DisplayName != null & !DisplayName.equals("")) {

                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                            .build());

                    //------------------------------------------------------ Names
                    if (DisplayName != null) {
                        ops.add(ContentProviderOperation.newInsert(
                                ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName + " " + lastname).build());
                    }

                    //------------------------------------------------------ Mobile Number                     
                    if (MobileNumber != null) {
                        ops.add(ContentProviderOperation.
                                newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                .build());
                    }

                    try {
                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        Singlechat.ll_iscontact.setVisibility(View.GONE);
                        updateContactInChatList(mobile + "@igap.im");
                        finish();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddNewContact.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                newcontact(DisplayName + " " + lastname, mobile + "@igap.im");
            }

        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    private void updateContactInChatList(String userChat) {
        Intent intent = new Intent("UpdateContactUserAvatar");
        intent.putExtra("UserChat", userChat);
        LocalBroadcastManager.getInstance(AddNewContact.this).sendBroadcast(intent);
    }


    private void newcontact(String name, String userchat) {

        Intent intent = new Intent("newContact");
        intent.putExtra("name", name);
        intent.putExtra("userchat", userchat);
        LocalBroadcastManager.getInstance(AddNewContact.this).sendBroadcast(intent);
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        G.appIsShowing = false;
    }
}
