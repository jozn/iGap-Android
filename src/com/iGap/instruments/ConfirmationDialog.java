// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.iGap.R;
import com.iGap.adapter.G;
import com.iGap.interfaces.OnColorChangedListenerSelect;


/**
 * 
 * for do something show a message to user that user can cancel it
 *
 */
public class ConfirmationDialog {

    private Context                      mContext;
    private OnColorChangedListenerSelect Listener;


    public ConfirmationDialog(Context context, OnColorChangedListenerSelect onColorChangedListenerSelect) {

        super();
        mContext = context;
        Listener = onColorChangedListenerSelect;
    }


    public void showdialog(String message) {

        final Dialog di = new Dialog(mContext);
        di.requestWindowFeature(Window.FEATURE_NO_TITLE);
        di.setContentView(R.layout.dialog_exit_prompt);
        di.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvMessage = (TextView) di.findViewById(R.id.textView_message_prompt);
        Button tvYes = (Button) di.findViewById(R.id.btnView_yes);
        Button tvNo = (Button) di.findViewById(R.id.btnView_no);

        tvMessage.setText(message);

        tvMessage.setTypeface(G.robotoBold);
        tvYes.setTypeface(G.robotoBold);
        tvNo.setTypeface(G.robotoBold);

        tvYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (di != null && di.isShowing()) {
                    di.dismiss();
                }

                Listener.Confirmation(true);

            }
        });
        tvNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (di != null && di.isShowing()) {
                    di.dismiss();
                }
                Listener.Confirmation(false);
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(di.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        di.getWindow().setAttributes(lp);
        di.show();
    }

}
