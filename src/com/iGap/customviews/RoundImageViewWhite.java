// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * get a picture and set white circle shape to this picture
 *
 */

public class RoundImageViewWhite extends ImageView {

    public RoundImageViewWhite(Context context) {
        super(context);
    }


    public RoundImageViewWhite(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    public RoundImageViewWhite(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    public void setBorderWidth(int borderWidth) {
        this.invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        try {
            Bitmap b = ((BitmapDrawable) drawable).getBitmap();
            Bitmap bitmap = b.copy(Config.ARGB_8888, true);

            Bitmap roundBitmap = roundImage(bitmap, canvas.getWidth(), canvas.getHeight());

            canvas.drawBitmap(roundBitmap, 0, 0, null);
        }
        catch (Exception e) {}

    }


    public Bitmap roundImage(Bitmap b, int with, int hight) {

        Bitmap output = null;

        try {
            output = Bitmap.createBitmap(with, hight, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            Bitmap bitmapScale = Bitmap.createScaledBitmap(b, with, hight, false);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#ffffff"));

            Rect rect = new Rect(0, 0, with, hight);

            canvas.drawCircle(bitmapScale.getWidth() / 2, bitmapScale.getHeight() / 2, bitmapScale.getWidth() / 2, paint);

            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmapScale, rect, rect, paint);

        }
        catch (Exception e) {}

        return output;

    }

}
