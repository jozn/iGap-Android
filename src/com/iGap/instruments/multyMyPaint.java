// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.R;
import com.iGap.adapter.G;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.services.MyService;


/**
 * 
 * online tow person paint
 *
 */

public class multyMyPaint extends Activity {

    private MyService               mService;

    private Dialog                  dialog;
    private Dialog                  dialogBrush;

    private int                     j                = 0;
    private int                     model            = 0;
    private int                     minBrushSize     = 12;
    private int                     brushSize        = minBrushSize;
    private int                     paintColor       = Color.BLACK;
    private int                     height;
    private int                     width;

    private boolean                 mBounded;
    private boolean                 captureimage     = false;

    private DrawingView             drawingView;
    private Paint                   paint, paintrecive;
    private FrameLayout             frameLayout;
    private Bitmap                  mBitmap;
    private Uri                     fileUri;
    private ArrayList<Float>        xarray, yarray;
    public static final int         MEDIA_TYPE_IMAGE = 1;

    private final ServiceConnection mConnection      = new ServiceConnection() {

                                                         @SuppressWarnings("unchecked")
                                                         @Override
                                                         public void onServiceConnected(final ComponentName name, final IBinder service) {
                                                             mService = ((LocalBinder<MyService>) service).getService();
                                                             mBounded = true;
                                                         }


                                                         @Override
                                                         public void onServiceDisconnected(final ComponentName name) {
                                                             mService = null;
                                                             mBounded = false;
                                                         }
                                                     };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.layout_paint_chat);
        doBindService();
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        LocalBroadcastManager.getInstance(multyMyPaint.this).registerReceiver(newpaint, new IntentFilter("newpaint"));
        LocalBroadcastManager.getInstance(multyMyPaint.this).registerReceiver(ClearPaint, new IntentFilter("ClearPaint"));

        init();

    }


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


    private void sendpaint() {
        if (xarray.size() != 0) {
            JSONObject paintjo = new JSONObject();
            try {
                paintjo.put("ISSEEN", "2");
                paintjo.put("xarray", xarray.toString());
                paintjo.put("yarray", yarray.toString());
                paintjo.put("paintcolor", String.valueOf(paintColor));
                paintjo.put("paintsize", String.valueOf(brushSize));
                paintjo.put("height", String.valueOf(height));
                paintjo.put("width", String.valueOf(width));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            mService.sendpaint(paintjo.toString());

            try {
                xarray.clear();
                yarray.clear();
            }
            catch (Exception e) {}
        }
    }


    private void sendClearPaint() {
        JSONObject paintjo = new JSONObject();
        try {
            paintjo.put("ISSEEN", "3");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        mService.sendpaint(paintjo.toString());
    }


    void doBindService() {
        bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    void doUnbindService() {
        if (mBounded) {
            unbindService(mConnection);
        }
    }


    void init() {

        xarray = new ArrayList<Float>();
        yarray = new ArrayList<Float>();
        drawingView = new DrawingView(multyMyPaint.this, false, null);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        frameLayout.addView(drawingView);

        setPaintColor();

        Typeface font_awsemn = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        TextView tvRefesh = (TextView) findViewById(R.id.textView_new);
        tvRefesh.setTypeface(font_awsemn);
        tvRefesh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                drawingView = new DrawingView(multyMyPaint.this, false, null);
                frameLayout.removeAllViews();
                frameLayout.addView(drawingView);
                sendClearPaint();
            }
        });

        TextView tvSave = (TextView) findViewById(R.id.textView_save);
        tvSave.setTypeface(font_awsemn);
        tvSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                savePicToFile("paint", false);
            }
        });

        TextView tvEraser = (TextView) findViewById(R.id.textView_erase);
        tvEraser.setTypeface(font_awsemn);
        tvEraser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setPaintClear();
            }
        });

        TextView tvPaint = (TextView) findViewById(R.id.textView_paintsize);
        tvPaint.setTypeface(font_awsemn);
        tvPaint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                initDialogBrush();
                setPaintColor();
            }
        });

        TextView tvColor = (TextView) findViewById(R.id.textView_color);
        tvColor.setTypeface(font_awsemn);
        tvColor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialogSelectColor();
            }
        });

    }


    private void resetPaint() {
        drawingView.setmodel(0);
        drawingView = new DrawingView(multyMyPaint.this, false, null);
        frameLayout.removeAllViews();
        frameLayout.addView(drawingView);
    }


    private void showDialogSelectColor() {
        ColorPiker d = new ColorPiker(multyMyPaint.this, paintColor, new OnColorChangedListenerSelect() {

            @Override
            public void colorChanged(String key, int color) {

                if (key.equals("ok")) {
                    paintColor = color;
                    setPaintColor();
                }
            }


            @Override
            public void Confirmation(Boolean result) {

            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        @SuppressWarnings("deprecation") int screenWidth = display.getWidth();

        d.getWindow().setLayout(screenWidth, WindowManager.LayoutParams.WRAP_CONTENT);
        d.setCancelable(true);

        d.show();

    }


    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        doUnbindService();
        super.onDestroy();
    }


    void initDialogBrush() {

        dialogBrush = new Dialog(multyMyPaint.this);
        dialogBrush.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBrush.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBrush.setContentView(R.layout.dialog_brush_paint);
        dialogBrush.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogBrush.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        dialogBrush.show();
        dialogBrush.getWindow().setAttributes(layoutParams);

        final TextView tvBrushSize = (TextView) dialogBrush.findViewById(R.id.textView_brush_size);

        SeekBar skBrushSize = (SeekBar) dialogBrush.findViewById(R.id.seekBar_brush_size);
        skBrushSize.setProgress(brushSize - minBrushSize);
        skBrushSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setPaintColor();
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvBrushSize.setText(brushSize + "");
            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brushSize = progress + minBrushSize;
                tvBrushSize.setText(brushSize + "");
            }
        });

    }


    public class DrawingView extends View {

        public int      width;
        public int      height;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        Context         context;
        private Paint   circlePaint;
        private Path    circlePath;
        private Boolean fromGallery;
        private Uri     PicAddress;
        private int     mode;


        public DrawingView(Context c, boolean FromGallery, Uri picAddress) {
            super(c);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
            this.fromGallery = FromGallery;
            this.PicAddress = picAddress;
            mode = model;
        }


        public void setmodel(int model) {
            mode = model;
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(Color.WHITE);
            mCanvas = new Canvas(mBitmap);

            if (fromGallery) {

                if ( !captureimage) {
                    String[] projection = { MediaColumns.DATA };
                    @SuppressWarnings("deprecation") Cursor cursor = managedQuery(PicAddress, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedImagePath = cursor.getString(column_index);
                    cursor.close();
                    File imgFile = new File(selectedImagePath);
                    Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    b = Bitmap.createScaledBitmap(b, w, h, false);
                    Paint paint = new Paint();
                    mCanvas.drawBitmap(b, 0, 0, paint);
                } else {
                    String filePath = fileUri.getPath();
                    File imgFile = new File(filePath);
                    Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    b = Bitmap.createScaledBitmap(b, w, h, false);
                    Paint paint = new Paint();
                    mCanvas.drawBitmap(b, 0, 0, paint);
                }

            }
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mode == 0) {
                canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
                canvas.drawPath(mPath, paint);
                canvas.drawPath(circlePath, circlePaint);
            } else {
                canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
                canvas.drawPath(mPath, paintrecive);
                canvas.drawPath(circlePath, circlePaint);
            }

        }

        private float              mX, mY;
        private static final float TOUCH_TOLERANCE = 4;


        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }


        private void touch_move(float x, float y) {
            xarray.add(x);
            yarray.add(y);
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }

        }


        private void touch_uprecive() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            if (mode == 0) {
                mCanvas.drawPath(mPath, paint);
            } else {
                mCanvas.drawPath(mPath, paintrecive);
            }

            // kill this so we don't double draw
            mPath.reset();
        }


        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            if (mode == 0) {
                mCanvas.drawPath(mPath, paint);
            } else {

                mCanvas.drawPath(mPath, paintrecive);
            }

            // kill this so we don't double draw
            mPath.reset();
            sendpaint();
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {

            setmodel(0);
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xarray.clear();
                    yarray.clear();
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:

                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }


    void setPaintColor() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(paintColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(brushSize);

    }


    private void setPaintColorrecive(int paintcol, int paintsiz) {
        paintrecive = new Paint();
        paintrecive.setAntiAlias(true);
        paintrecive.setDither(true);
        paintrecive.setColor(paintcol);
        paintrecive.setStyle(Paint.Style.STROKE);
        paintrecive.setStrokeJoin(Paint.Join.ROUND);
        paintrecive.setStrokeCap(Paint.Cap.ROUND);
        paintrecive.setStrokeWidth(paintsiz);
    }


    void setPaintClear() {
        paintColor = Color.parseColor("#ffffff");
        brushSize = 50;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(paintColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(brushSize);

    }


    void savePicToFile(String fileName, Boolean send) {

        File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/igap/paint");
        if ( !dir.exists())
            dir.mkdirs();
        File f = new File(dir, fileName + ".png");

        int x = 0;
        while (f.exists()) {
            f = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                    + "/igap/paint", fileName + Integer.toString(x) + ".png");
            x++;
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
        catch (Exception e) {
            e.printStackTrace();
            f = null;
        }

        if (send) {
            Intent data = new Intent();
            data.setData(Uri.parse(f.getAbsolutePath()));
            setResult(Activity.RESULT_OK, data);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.picture_is_saved_en), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case G.request_code_PICK_IMAGE:

                    try {
                        setImageToBitmap(data);
                    }
                    catch (Exception e) {
                        Toast.makeText(multyMyPaint.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;
                case G.request_code_TAKE_PICTURE:

                    try {
                        setImageToBitmap1();
                    }
                    catch (Exception e) {
                        Toast.makeText(multyMyPaint.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;
                default:
                    break;
            }

        }

    }


    private void setImageToBitmap(Intent data) {
        Uri selectedImageUri = data.getData();
        drawingView = new DrawingView(multyMyPaint.this, true, selectedImageUri);
        frameLayout.removeAllViews();
        frameLayout.addView(drawingView);
    }


    private void setImageToBitmap1() {

        drawingView = new DrawingView(multyMyPaint.this, true, fileUri);
        frameLayout.removeAllViews();
        frameLayout.addView(drawingView);
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if ( !mediaStorageDir.exists()) {
            if ( !mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private BroadcastReceiver ClearPaint = new BroadcastReceiver() {

                                             @Override
                                             public void onReceive(Context context, Intent intent) {
                                                 resetPaint();
                                             }
                                         };
    private BroadcastReceiver newpaint   = new BroadcastReceiver() {

                                             @Override
                                             public void onReceive(Context context, Intent intent) {
                                                 String message = intent.getStringExtra("message");
                                                 tarsim(message);
                                             }
                                         };


    private void tarsim(final String message) {
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    JSONObject jo = new JSONObject(message);

                    String x = jo.getString("xarray");
                    String y = jo.getString("yarray");
                    String color = jo.getString("paintcolor");
                    String size = jo.getString("paintsize");
                    String heightrecive = jo.getString("height");
                    String widthrecive = jo.getString("width");
                    float xcalculator = height / Float.parseFloat(heightrecive);
                    float ycalculator = width / Float.parseFloat(widthrecive);
                    setPaintColorrecive(Integer.parseInt(color), Integer.parseInt(size));
                    String[] xelements = x.substring(1, x.length() - 1).split(", ");
                    final List<Float> xresult = new ArrayList<Float>(xelements.length);
                    for (String item: xelements) {
                        xresult.add(Float.valueOf(item) * xcalculator);
                    }
                    String[] yelements = y.substring(1, y.length() - 1).split(", ");
                    final List<Float> yresult = new ArrayList<Float>(yelements.length);
                    for (String item: yelements) {
                        yresult.add(Float.valueOf(item) * ycalculator);
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            drawingView.setmodel(1);
                            drawingView.touch_start(xresult.get(0), yresult.get(0));
                            drawingView.invalidate();

                            for (int i = 1; i < xresult.size(); i++) {
                                drawingView.touch_move(xresult.get(i), yresult.get(i));
                                drawingView.invalidate();
                            }
                            drawingView.touch_uprecive();
                            drawingView.invalidate();
                        }
                    });

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

}
