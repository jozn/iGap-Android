package com.iGap;

import java.io.File;
import java.util.ArrayList;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.iGap.adapter.G;
import com.iGap.instruments.database;
import com.iGap.interfaces.OnComplet;
import com.iGap.interfaces.OnDownloadListener;
import com.iGap.services.DownloaderService;


/**
 * 
 * namayesh layeh music dar barnamh va dar notification
 *
 */

public class MusicPlayer {

    private static ArrayList<Item>     listItem      = new ArrayList<Item>();

    private static int                 downloadPosition;

    private static boolean             resultPlay    = false, isPause = false;

    public static String               place;
    private static String              musicName;
    private static String              uid;
    private static String              type;
    private static String              musicFileHash = "";
    private static String              musicFile     = "";

    private static Button              btnStopMusic, btnPlayMusic, btnCloseMusic;
    private static LinearLayout        layoutMusic;

    static TextView                    txt_music_time;
    static TextView                    txt_music_name;

    public static MediaPlayer          mp;
    private static RemoteViews         remoteViews;
    private static NotificationManager notificationManager;
    private static Notification        notification;


    public MusicPlayer(Context context, LinearLayout layoutMusic, String name, String basicAuth, database db, String uid, String Type) {
        MusicPlayer.layoutMusic = layoutMusic;

        remoteViews = new RemoteViews(G.context.getPackageName(), R.layout.music_layout_notification);
        notificationManager = (NotificationManager) G.context.getSystemService(G.context.NOTIFICATION_SERVICE);

        if (name.equals("small")) {
            initLayoutSmall();
        }

        type = Type;
        MusicPlayer.uid = uid;
        try {

            fillListItem(Type, uid);
        }
        catch (Exception e) {}

    }


    public static void setTextButton() {
        try {
            btnPlayMusic.setText(G.context.getString(R.string.fa_play));
            remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.play);
        }
        catch (Exception e) {

        }
    }


    public static String getMusicFile() {
        return musicFile;
    }


    private void initLayoutSmall() {

        layoutMusic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(G.context, MusicActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                G.context.startActivity(intent);

            }
        });

        txt_music_time = (TextView) layoutMusic.findViewById(R.id.mls_txt_music_time);
        txt_music_name = (TextView) layoutMusic.findViewById(R.id.mls_txt_music_name);

        btnStopMusic = (Button) layoutMusic.findViewById(R.id.mls_btn_stop);
        btnStopMusic.setTypeface(G.fontAwesome);
        btnStopMusic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                stopSound();

            }
        });

        btnPlayMusic = (Button) layoutMusic.findViewById(R.id.mls_btn_play_music);
        btnPlayMusic.setTypeface(G.fontAwesome);
        btnPlayMusic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                playMediaPlayer(true);
            }
        });

        btnCloseMusic = (Button) layoutMusic.findViewById(R.id.mls_btn_close);
        btnCloseMusic.setTypeface(G.fontAwesome);
        btnCloseMusic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                closeLayoutMediaPlayer();

            }
        });

    }


    public static void playMediaPlayer(Boolean updatenotification) {

        if (mp != null) {

            if (mp.isPlaying())
                pauseSound(updatenotification);
            else
                playSound(updatenotification);
        }
        else {
            closeLayoutMediaPlayer();
        }

    }


    public static void closeLayoutMediaPlayer() {

        if (layoutMusic != null)
            layoutMusic.setVisibility(View.GONE);

        stopSound();

        if (mp != null) {
            mp.release();
            mp = null;
        }

        NotificationManager notifManager = (NotificationManager) G.context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }


    private static void pauseSound(Boolean updateNotification) {

        try {
            btnPlayMusic.setText(G.context.getString(R.string.fa_play));

            remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.play);
            if (updateNotification)
                notificationManager.notify(0, notification);
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        isPause = true;
        mp.pause();

    }


    private static void playSound(Boolean updateNotification) {

        try {
            btnPlayMusic.setText(G.context.getString(R.string.fa_pause));
            remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.pause);
            if (updateNotification)
                notificationManager.notify(0, notification);
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        if (isPause) {
            if (mp != null) {
                mp.start();
                isPause = false;
            }
        }
        else {
            startPlayer(musicFile, place, musicFileHash);
        }

    }


    public static void stopSound() {

        isPause = false;
        try {
            btnPlayMusic.setText(G.context.getString(R.string.fa_play));

            remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.play);
            notificationManager.notify(0, notification);
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        if (mp != null) {
            mp.stop();
        }

    }


    public static void updateView() {

        if (mp != null) {

            if (mp.isPlaying()) {
                try {
                    btnPlayMusic.setText(G.context.getString(R.string.fa_pause));

                    remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.pause);
                    notificationManager.notify(0, notification);
                }
                catch (Exception e) {

                    e.printStackTrace();
                }

            }
            txt_music_time.setText(milliSecondsToTimer((long) mp.getDuration()));

            if (musicName != null)
                txt_music_name.setText(musicName);

        }

    }


    public static void startPlayer(String filePath, String place, String filehash) {

        musicFile = filePath;

        MusicPlayer.place = place;
        musicFileHash = filehash;

        if (layoutMusic.getVisibility() == View.GONE)
            layoutMusic.setVisibility(View.VISIBLE);

        if (mp != null) {
            mp.stop();
            mp.reset();

            try {
                mp.setDataSource(musicFile);
                mp.prepare();
                mp.start();

                try {
                    btnPlayMusic.setText(G.context.getString(R.string.fa_pause));

                    remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.pause);
                    notificationManager.notify(0, notification);
                }
                catch (Exception e) {

                    e.printStackTrace();
                }

                txt_music_time.setText(milliSecondsToTimer((long) mp.getDuration()));

                musicName = musicFile.substring(musicFile.lastIndexOf("/") + 1);
                txt_music_name.setText(musicName);

                updateNotification(musicName, place);

            }
            catch (Exception e) {}

        }

        else {

            mp = new MediaPlayer();

            try {
                mp.setDataSource(musicFile);
                mp.prepare();
                mp.start();

                txt_music_time.setText(milliSecondsToTimer((long) mp.getDuration()));

                try {
                    btnPlayMusic.setText(G.context.getString(R.string.fa_pause));
                    remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.pause);
                    notificationManager.notify(0, notification);
                }
                catch (Exception e) {}

                mp.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopSound();
                    }
                });

                musicName = musicFile.substring(musicFile.lastIndexOf("/") + 1);
                txt_music_name.setText(musicName);
                updateNotification(musicName, place);

            }
            catch (Exception e) {

            }
        }

    }


    public static String milliSecondsToTimer(long milliseconds) {

        if (milliseconds == -1)
            return " ";

        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    public static void updateNotification(String musicName, String place) {

        PendingIntent pi = PendingIntent.getActivity(G.context, 10, new Intent(G.context, MusicActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setTextViewText(R.id.mln_txt_music_name, MusicPlayer.musicName);
        remoteViews.setTextViewText(R.id.mln_txt_music_place, MusicPlayer.place);

        if (mp != null)
            if (mp.isPlaying())
                remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.pause);
            else {
                remoteViews.setImageViewResource(R.id.mln_btn_play_music, R.drawable.play);
            }

        try {
            MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
            Uri uri = (Uri) Uri.fromFile(new File(musicFile));
            mediaMetadataRetriever.setDataSource(G.context, uri);
            byte[] data = mediaMetadataRetriever.getEmbeddedPicture();
            if (data != null)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                remoteViews.setImageViewBitmap(R.id.mln_img_picture_music, bitmap);
            }
            else {
                remoteViews.setImageViewResource(R.id.mln_img_picture_music, R.drawable.igap_music);
            }
        }
        catch (Exception e) {

        }

        Intent intentPrevious = new Intent(G.context, customButtonListener.class);
        intentPrevious.putExtra("mode", "previous");
        PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(G.context, 1, intentPrevious, 0);
        remoteViews.setOnClickPendingIntent(R.id.mln_btn_Previous_music, pendingIntentPrevious);

        Intent intentPlayPause = new Intent(G.context, customButtonListener.class);
        intentPlayPause.putExtra("mode", "play");
        PendingIntent pendingIntentPlayPause = PendingIntent.getBroadcast(G.context, 2, intentPlayPause, 0);
        remoteViews.setOnClickPendingIntent(R.id.mln_btn_play_music, pendingIntentPlayPause);

        Intent intentforward = new Intent(G.context, customButtonListener.class);
        intentforward.putExtra("mode", "forward");
        PendingIntent pendingIntentforward = PendingIntent.getBroadcast(G.context, 3, intentforward, 0);
        remoteViews.setOnClickPendingIntent(R.id.mln_btn_forward_music, pendingIntentforward);

        Intent intentClose = new Intent(G.context, customButtonListener.class);
        intentClose.putExtra("mode", "close");
        PendingIntent pendingIntentClose = PendingIntent.getBroadcast(G.context, 4, intentClose, 0);
        remoteViews.setOnClickPendingIntent(R.id.mln_btn_close, pendingIntentClose);

        notification = new NotificationCompat.Builder(G.context.getApplicationContext())
                .setTicker("music")
                .setSmallIcon(R.drawable.j_audio)
                .setContentTitle(musicName)
                //  .setContentText(place)
                .setContent(remoteViews)
                .setContentIntent(pi)
                .setAutoCancel(false)
                .build();

        notificationManager.notify(0, notification);

    }


    public static class customButtonListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String str = intent.getExtras().getString("mode");

            if (str.equals("previous")) {
                previous(null, true);
            }
            else if (str.equals("play")) {
                playMediaPlayer(true);
            }
            else if (str.equals("forward")) {
                next(null, true);
            }
            else if (str.equals("close")) {
                closeLayoutMediaPlayer();
            }

        }
    }


    public static void musicDownloder(final String filehash, int position, final OnComplet complet, final Boolean playAfterDownload, final Boolean next) {

        if (filehash.equals(""))
            return;

        String status = G.cmd.getfile(4, filehash);

        if (status.equals("0") || status.equals("1")) {

            if ( !listItem.get(downloadPosition).downloading) {

                if (playAfterDownload) {

                    remoteViews.setTextViewText(R.id.mln_txt_music_name, "downloading ...");
                    notificationManager.notify(0, notification);
                }

                listItem.get(downloadPosition).downloading = true;

                DownloaderService downloaderService = new DownloaderService();

                final OnDownloadListener listener = new OnDownloadListener() {

                    @Override
                    public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                        if (complet != null) {
                            complet.complet(false, percent + "");

                        }

                        if (completeDownload) {
                            if (complet != null)
                                complet.complet(true, "100");

                            if (playAfterDownload) {

                                remoteViews.setTextViewText(R.id.mln_txt_music_name, "completeDownload");
                                notificationManager.notify(0, notification);

                                try {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            if (next)
                                                next(null, false);
                                            else
                                                previous(null, false);
                                        }
                                    }, 1500);

                                }
                                catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }

                        }
                    }
                };

                String url = G.cmd.getfile(2, filehash);
                downloaderService.downloadPath(url)
                        .listener(listener)
                        .Authorization(G.basicAuth)
                        .filetype("4")
                        .filepath(G.DIR_TEMP + "/" + filehash + ".mp3")
                        .stopDownload(false)
                        .fileHash(filehash)
                        .download(G.context);
            }

        } else if (status.equals("2") || status.equals("5")) {

            String filepath = G.cmd.getfile(6, filehash);
            startPlayer(filepath, place, filehash);

            resultPlay = true;
        }

    }


    public static String getNextOrPreviousMusicFileHash(Boolean next) {

        String result = "";

        try {
            int position = -1;

            int size = listItem.size();

            for (int i = 0; i < size; i++) {

                if (listItem.get(i).fileHash.equals(musicFileHash)) {
                    position = i;

                    break;
                }

            }

            if (next) {
                if (position - 1 >= 0) {
                    result = listItem.get(position - 1).fileHash;
                    downloadPosition = position - 1;
                }
            }
            else {
                if (position + 1 < size) {
                    result = listItem.get(position + 1).fileHash;
                    downloadPosition = position + 1;
                }

            }
        }
        catch (Exception e) {}

        return result;
    }


    private void fillListItem(String type, String id) {

        listItem.clear();

        Cursor cu = null;

        if (type.equals("1")) {
            cu = G.cmd.getSharedMediaItem("chathistory", id);
        }
        else if (type.equals("2")) {
            cu = G.cmd.getSharedMediaItem("groupchathistory", id);
        }
        else if (type.equals("3")) {

            cu = G.cmd.getSharedMediaItem("channelhistory", id);
        }

        while (cu.moveToNext()) {

            Item item = new Item();

            item.fileHash = cu.getString(0);
            item.fileType = cu.getString(1);
            item.fileUrl = cu.getString(2);
            item.fileThumb = cu.getString(3);

            if (item.fileType.equals("4") || item.fileType.equals("8")) {
                listItem.add(item);
            }

        }

        cu.close();

    }


    private class Item {

        String  fileHash;
        String  fileType;
        String  fileUrl;
        String  fileThumb;
        Boolean downloading = false;
    }


    public static boolean next(OnComplet complet, Boolean playAfterDownload) {

        resultPlay = false;

        String filehash = getNextOrPreviousMusicFileHash(true);
        musicDownloder(filehash, downloadPosition, complet, playAfterDownload, true);

        if (resultPlay)
            return true;
        return false;

    }


    public static boolean previous(OnComplet complet, Boolean playAfterDownload) {
        resultPlay = false;

        String filehash = getNextOrPreviousMusicFileHash(false);
        musicDownloder(filehash, downloadPosition, complet, playAfterDownload, false);

        if (resultPlay)
            return true;
        return false;
    }

}
