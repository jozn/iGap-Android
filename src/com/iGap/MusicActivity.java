package com.iGap;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.iGap.adapter.G;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnComplet;


/**
 * 
 * namayesh layeh mediaplayer dakheli
 *
 */

public class MusicActivity extends Activity {

    private TextView    txt_MusicName;
    private TextView    txt_MusicPlace;
    private TextView    txt_MusicTime;
    private TextView    txt_Timer;
    private TextView    txt_musicInfo;

    private String      place     = "";
    private String      musicTime = "";
    private String      musicName = "";
    private String      str_info  = "";

    private SeekBar     musikSeekbar;
    private ImageView   img_MusicImage;
    private Button      btnPlay;

    private MediaPlayer mp;
    private Timer       mTimer, mTimeSecend;
    private long        time      = 0;
    private int         amoungToupdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.music_layout);

        try {
            place = MusicPlayer.place;
            musicTime = MusicPlayer.txt_music_time.getText() + "";
            musicName = MusicPlayer.txt_music_name.getText() + "";
        }
        catch (Exception e) {

        }

        try {
            mp = MusicPlayer.mp;

            if (mp == null) {
                finish();
                NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notifManager.cancelAll();
            }
        }
        catch (Exception e) {
            NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            finish();
        }

        init();

        getMusicInfo();
    }


    private void getMusicInfo() {

        str_info = "";

        MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
        Uri uri = (Uri) Uri.fromFile(new File(MusicPlayer.getMusicFile()));
        mediaMetadataRetriever.setDataSource(MusicActivity.this, uri);

        String title = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        if (title != null) {
            str_info += title + "\n";

        }

        String albumName = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        if (albumName != null) {
            str_info += albumName + "\n";

        }

        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (artist != null) {
            str_info += artist + "\n";

        }

        txt_musicInfo.setText(str_info);

        byte[] data = mediaMetadataRetriever.getEmbeddedPicture();
        if (data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            img_MusicImage.setImageBitmap(bitmap);
        }

    }


    private void init() {

        txt_MusicName = (TextView) findViewById(R.id.ml_txt_music_name);
        txt_MusicName.setText(musicName);

        txt_MusicPlace = (TextView) findViewById(R.id.ml_txt_music_place);
        txt_MusicPlace.setText(place);

        txt_MusicTime = (TextView) findViewById(R.id.ml_txt_music_time);
        txt_MusicTime.setText(musicTime);

        txt_Timer = (TextView) findViewById(R.id.ml_txt_timer);

        try {
            txt_Timer.setText(MusicPlayer.milliSecondsToTimer(mp.getCurrentPosition()));
        }
        catch (Exception e) {

        }

        txt_musicInfo = (TextView) findViewById(R.id.ml_txt_music_info);

        img_MusicImage = (ImageView) findViewById(R.id.ml_img_music_picture);

        musikSeekbar = (SeekBar) findViewById(R.id.ml_seekBar1);
        musikSeekbar.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    if (mp != null) {
                        mp.seekTo(musikSeekbar.getProgress() * amoungToupdate);
                        time = mp.getCurrentPosition();
                        txt_Timer.setText(MusicPlayer.milliSecondsToTimer(time));
                    }
                }
                catch (IllegalStateException e) {
                    Log.e("wer", e.toString());
                }

                return false;
            }
        });

        Button btnBack = (Button) findViewById(R.id.ml_btn_back);
        btnBack.setTypeface(G.fontAwesome);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    exitPlayer();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Button btnRpearMode = (Button) findViewById(R.id.ml_btn_music_mode);
        btnRpearMode.setTypeface(G.fontAwesome);
        btnRpearMode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                repeatMode();

            }
        });

        Button btnPrevious = (Button) findViewById(R.id.ml_btn_Previous_music);
        btnPrevious.setTypeface(G.fontAwesome);
        btnPrevious.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                previousMusic();

            }
        });

        btnPlay = (Button) findViewById(R.id.ml_btn_play_music);
        btnPlay.setTypeface(G.fontAwesome);
        btnPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    playMusic();
                }
                catch (Exception e) {}

            }
        });

        Button btnNextMusic = (Button) findViewById(R.id.ml_btn_forward_music);
        btnNextMusic.setTypeface(G.fontAwesome);
        btnNextMusic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                nextMusic();

            }
        });

        Button btnRandrom = (Button) findViewById(R.id.ml_btn_random_music);
        btnRandrom.setTypeface(G.fontAwesome);
        btnRandrom.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                randomMusic();

            }
        });

        if (mp != null) {
            if (mp.isPlaying()) {
                btnPlay.setText(getString(R.string.fa_pause));
            }
            else {
                btnPlay.setText(getString(R.string.fa_play));
            }
        }

        try {
            mp.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    btnPlay.setText(getString(R.string.fa_play));
                    MusicPlayer.setTextButton();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void exitPlayer() {

        finish();

    }


    private void repeatMode() {
        // write code here later
    }


    private void previousMusic() {
        if (MusicPlayer.previous(new OnComplet() {

            @Override
            public void complet(Boolean result, String message) {

                try {
                    if (result) {
                        runOnUiThread(new Runnable() {

                            public void run() {

                                txt_musicInfo.setText("downloading  completed  ");

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        if (MusicPlayer.previous(null, false))
                                            reloadActivity();
                                    }
                                }, 1500);
                            }
                        });

                    }
                    else {
                        int x = Integer.parseInt(message);
                        if (x > 0 && x < 100) {
                            musikSeekbar.setProgress(x);
                            txt_musicInfo.setText("downloading  " + message);
                        }
                    }
                }
                catch (NumberFormatException e) {

                }
            }
        }, false)) {

            reloadActivity();
        }
    }


    private void playMusic() {

        if (mp != null) {

            MusicPlayer.playMediaPlayer(false);

            if (mp.isPlaying()) {
                btnPlay.setText(getString(R.string.fa_pause));
                updateProgress();
            }
            else {
                btnPlay.setText(getString(R.string.fa_play));

                if (mTimeSecend != null) {
                    mTimeSecend.cancel();
                    mTimeSecend = null;
                }

                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
            }
        }

    }


    private void nextMusic() {
        if (MusicPlayer.next(new OnComplet() {

            @Override
            public void complet(Boolean result, String message) {

                try {
                    if (result) {
                        runOnUiThread(new Runnable() {

                            public void run() {

                                txt_musicInfo.setText("downloading  completed  ");

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        if (MusicPlayer.next(null, false))
                                            reloadActivity();
                                    }
                                }, 1500);
                            }
                        });

                    }
                    else {
                        int x = Integer.parseInt(message);
                        if (x > 0 && x < 100) {
                            musikSeekbar.setProgress(x);
                            txt_musicInfo.setText("downloading  " + message);
                        }
                    }
                }
                catch (NumberFormatException e) {

                }

            }
        }, false)) {

            reloadActivity();
        }
    }


    private void reloadActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    private void randomMusic() {
        // write code here later
    }


    private void updateProgress() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimeSecend != null) {
            mTimeSecend.cancel();
            mTimeSecend = null;
        }

        final int duration = mp.getDuration();
        amoungToupdate = duration / 100;

        musikSeekbar.setProgress(mp.getCurrentPosition() / amoungToupdate);

        if ( !mp.isPlaying())
            return;

        time = mp.getCurrentPosition();

        mTimeSecend = new Timer();
        mTimeSecend.schedule(new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    public void run() {
                        txt_Timer.setText(MusicPlayer.milliSecondsToTimer(time));

                    }
                });

                time += 1000;

            }
        }, 0, 1000);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (musikSeekbar.getProgress() < 100) {
                            int p = musikSeekbar.getProgress();
                            p += 1;
                            musikSeekbar.setProgress(p);
                        }
                        else {
                            mTimer.cancel();

                            if (mTimeSecend != null) {
                                mTimeSecend.cancel();
                                mTimeSecend = null;
                            }

                        }
                    }
                });
            };
        }, 0, amoungToupdate);

    }


    @Override
    protected void onStop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;

        }

        if (mTimeSecend != null) {
            mTimeSecend.cancel();
            mTimeSecend = null;
        }

        super.onStop();
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);

        try {
            updateProgress();

            NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPause() {

        try {
            MusicPlayer.updateNotification(musicName, place);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

}
