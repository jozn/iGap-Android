// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.R;
import com.iGap.adapter.G;
import com.iGap.interfaces.OnColorChangedListenerSelect;


/**
 * 
 * for draw a paint and send to other user or save in device folder
 *
 */

public class myPaint extends Activity {

    private int             minBrushSize     = 12;
    private int             brushSize        = minBrushSize;
    private int             paintColor       = Color.BLACK;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private boolean         captureimage     = false;

    private Bitmap          mBitmap;
    private Dialog          dialogBrush;
    private Dialog          di;
    private DrawingView     drawingView;
    private Paint           paint;
    private FrameLayout     frameLayout;
    private Uri             fileUri;


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
        setContentView(R.layout.layout_paint);

        init();
    }


    void init() {

        drawingView = new DrawingView(myPaint.this, false, null);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        frameLayout.addView(drawingView);

        setPaintColor();

        Typeface font_awsemn = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        TextView tvclose = (TextView) findViewById(R.id.textView_close);
        tvclose.setTypeface(font_awsemn);
        tvclose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showAlertDialogExit();

            }
        });

        TextView tvRefesh = (TextView) findViewById(R.id.textView_new);
        tvRefesh.setTypeface(font_awsemn);
        tvRefesh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                drawingView = new DrawingView(myPaint.this, false, null);
                frameLayout.removeAllViews();
                frameLayout.addView(drawingView);
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

        TextView tvSend = (TextView) findViewById(R.id.textView_send);
        tvSend.setTypeface(font_awsemn);
        tvSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                savePicToFile("paint", true);
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

        TextView tvSience = (TextView) findViewById(R.id.textView_sience);
        tvSience.setTypeface(font_awsemn);
        tvSience.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openGallery();
            }
        });

        TextView tvCamera = (TextView) findViewById(R.id.textView_camera);
        tvCamera.setTypeface(font_awsemn);
        tvCamera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openCameraForImage();
            }
        });

    }


    private void showDialogSelectColor() {
        ColorPiker d = new ColorPiker(myPaint.this, paintColor, new OnColorChangedListenerSelect() {

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

        if (di != null && di.isShowing()) {
            di.dismiss();
        }

        super.onDestroy();
    }


    void initDialogBrush() {

        dialogBrush = new Dialog(myPaint.this);
        dialogBrush.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBrush.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBrush.setContentView(R.layout.dialog_brush_paint);
        dialogBrush.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogBrush.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        // lp.height = WindowManager.LayoutParams.MATCH_PARENT;
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

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, paint);
            canvas.drawPath(circlePath, circlePaint);
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


        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, paint);
            // kill this so we don't double draw
            mPath.reset();
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
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


    void setPaintClear() {

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#ffffff"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(50);

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


    private void openGallery() {
        captureimage = false;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_en)), G.request_code_PICK_IMAGE);

    }


    private void openCameraForImage() {
        captureimage = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, G.request_code_TAKE_PICTURE);

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
                        Toast.makeText(myPaint.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;
                case G.request_code_TAKE_PICTURE:

                    try {
                        setImageToBitmap1();
                    }
                    catch (Exception e) {
                        Toast.makeText(myPaint.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private void showAlertDialogExit() {

        Typeface robotoBold = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoBold.ttf");

        di = new Dialog(this);
        di.requestWindowFeature(Window.FEATURE_NO_TITLE);
        di.setContentView(R.layout.dialog_exit_prompt);
        di.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        di.show();

        TextView tvMessage = (TextView) di.findViewById(R.id.textView_message_prompt);
        tvMessage.setTypeface(robotoBold);

        Button tvYes = (Button) di.findViewById(R.id.btnView_yes);
        tvYes.setTypeface(robotoBold);
        tvYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                di.cancel();
                myPaint.this.finish();

            }
        });

        Button tvNo = (Button) di.findViewById(R.id.btnView_no);
        tvNo.setTypeface(robotoBold);
        tvNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                di.dismiss();

                if (di != null && di.isShowing()) {
                    di.dismiss();
                }

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(di.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        di.getWindow().setAttributes(lp);
        di.show();
    }


    private void setImageToBitmap(Intent data) {
        Uri selectedImageUri = data.getData();
        drawingView = new DrawingView(myPaint.this, true, selectedImageUri);
        frameLayout.removeAllViews();
        frameLayout.addView(drawingView);
    }


    private void setImageToBitmap1() {

        drawingView = new DrawingView(myPaint.this, true, fileUri);
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
}
