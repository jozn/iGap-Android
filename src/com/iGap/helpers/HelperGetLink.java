package com.iGap.helpers;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;
import com.iGap.WebBrowser;


public class HelperGetLink extends LinkMovementMethod {

    public static String linkUrl;
    private Context      context;
    private boolean      runWebView;


    /**
     * this class get a link string and after touch this link open it in webBrowser page
     * 
     * @param context
     */

    public HelperGetLink(Context context) {
        this.context = context;
    }


    @Override
    public boolean onTouchEvent(final TextView widget, final Spannable buffer, final MotionEvent event) {
        final int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            runWebView = true;
        }
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            final int x = (int) event.getX() - widget.getTotalPaddingLeft() + widget.getScrollX();
            final int y = (int) event.getY() - widget.getTotalPaddingTop() + widget.getScrollY();
            final Layout layout = widget.getLayout();
            final int line = layout.getLineForVertical(y);
            final int off = layout.getOffsetForHorizontal(line, x);
            final ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            if (link.length != 0 && runWebView) {
                runWebView = false;
                String url = buffer.subSequence(buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0])).toString();

                int length = url.length();
                String http = "";
                String https = "";
                if (length > 8) {
                    http = url.substring(0, 7);
                    https = url.substring(0, 8);
                }

                if ( !https.equals("https://") && !http.equals("http://")) {
                    url = "http://" + url;
                }

                final String linkUrl = url;

                Intent intent = new Intent(context, WebBrowser.class);
                intent.putExtra("link", linkUrl);
                context.startActivity(intent);

            }

            if (link.length != 0) {
                return true;
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}
