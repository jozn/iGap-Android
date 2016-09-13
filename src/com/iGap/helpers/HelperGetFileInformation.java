package com.iGap.helpers;

import java.io.File;
import java.text.NumberFormat;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.iGap.adapter.G;
import com.iGap.helpers.HelperGetDataFromOtherApp.FileType;


public class HelperGetFileInformation {

    //**********************************************************************************************************************

    /**
     * get a uri address and return a thumbnail bitmap if exist or return null
     */

    public static Bitmap getThumbnail(Uri uri) {

        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(getFilePathFromUri(uri), MediaStore.Video.Thumbnails.MICRO_KIND);

        return bMap;
    }


    //**********************************************************************************************************************

    /**
     * get a content uri adaress and return a real path address
     */

    private static String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = G.context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    //**********************************************************************************************************************

    public static FileType getMimeType(Uri uri) {
        String extension;
        FileType fileType = FileType.file;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(G.context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        extension = extension.toLowerCase();

        if (extension.endsWith("jpg") || extension.endsWith("jpeg") || extension.endsWith("png") || extension.endsWith("bmp"))
            fileType = FileType.image;
        else if (extension.endsWith("mp3") || extension.endsWith("ogg") || extension.endsWith("wma") || extension.endsWith("m4a") || extension.endsWith("amr") || extension.endsWith("wav"))
            fileType = FileType.audio;
        else if (extension.endsWith("mp4") || extension.endsWith("3gp") || extension.endsWith("avi") || extension.endsWith("mpg") || extension.endsWith("flv") || extension.endsWith("wmv") || extension.endsWith("m4v"))
            fileType = FileType.video;

        return fileType;
    }


    //**********************************************************************************************************************

    public static String getFilePathFromUri(Uri uri) {

        String path = "";

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            path = getRealPathFromURI(uri);
        } else {
            path = uri.getPath();
        }

        return path;
    }


    //**********************************************************************************************************************

    public static String getVidoSizeAndTime(Uri uri) {

        String result = "";

        String path = getFilePathFromUri(uri);

        MediaPlayer mp = MediaPlayer.create(G.context, uri);
        int duration = mp.getDuration();
        mp.release();

        result = milisecendToStringTime(duration);

        File file = new File(path);
        long length = file.length();

        result = GetSizetoString(length) + "  " + result;

        return result;
    }


    //**********************************************************************************************************************

    private static String GetSizetoString(long size) {

        String value = "";

        long Filesize = size / 1024;
        if (Filesize >= 1024) {
            NumberFormat nf = NumberFormat.getInstance(); // get instance
            nf.setMaximumFractionDigits(2); // set decimal places

            String x = nf.format((float) Filesize / 1024);

            value = x + " mb";

        }
        else
            value = Filesize + " Kb";

        return value;
    }


    //**********************************************************************************************************************

    private static String milisecendToStringTime(long time) {

        String result = "";

        long duration = time / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);

        if (hours > 0) {
            result += hours + ":";
        }

        result += minutes + ":";

        if (seconds < 10)
            result += "0" + seconds;
        else
            result += seconds;

        return result;
    }
    
    //**********************************************************************************************************************


	/**
	 * open a file by appropriate Program
	 * @param fileName for realize type of file like image.png or dd.pdf
	 * @return intent for open file
	 */
	public static Intent appropriateProgram(String fileName){


		File file = new File(fileName);

		String path=fileName.toLowerCase();

		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);


		if(path.endsWith(".txt") || path.endsWith(".csv") || path.endsWith(".xml")|| path.endsWith(".html")){

			intent.setDataAndType(Uri.fromFile(file), "text/*");
		}
		else if(path.endsWith(".mp3") || path.endsWith(".ogg")  || path.endsWith(".wma") || path.endsWith(".m4a") || path.endsWith(".amr") || path.endsWith(".wav") || path.endsWith(".mid") || path.endsWith(".midi") ){

			intent.setDataAndType(Uri.fromFile(file), "audio/*");
		}
		else if(path.endsWith(".mp4") || path.endsWith(".3gp") || path.endsWith(".avi") || path.endsWith(".mpg")  || path.endsWith(".mpeg") || path.endsWith(".flv") || path.endsWith(".wmv") || path.endsWith(".m4v")   ){

			intent.setDataAndType(Uri.fromFile(file), "video/*");
		}
		else if(path.endsWith(".pdf")){

			intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		}
		else if(path.endsWith(".jpg") || path.endsWith(".bmp") || path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(".jpeg") || path.endsWith(".tiff") ){

			intent.setDataAndType(Uri.fromFile(file), "image/*");  
		}
		else if(path.endsWith(".apk")){

			intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		}
		else if(path.endsWith(".gz") || path.endsWith(".gz") || path.endsWith(".zip")){
			intent.setDataAndType(Uri.fromFile(file),"package/*");
		}

		else if(path.endsWith(".ppt") || path.endsWith(".pptx") ){

			intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-powerpoint");
		}
		else if(path.endsWith(".xls") || path.endsWith(".xlsx") ){
			intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
		}
		else if(path.endsWith(".rtf")  ){
			intent.setDataAndType(Uri.fromFile(file),"application/rtf");
		}


		return intent;
	}


}
