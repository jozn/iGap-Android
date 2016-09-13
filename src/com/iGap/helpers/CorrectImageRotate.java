package com.iGap.helpers;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

public class CorrectImageRotate {

	
	/**
	 * if picture has a wrong rotate correct rotate and save it to file
	 * @param filepath picture file address
	 * @return return correct rotate bitmap or return null if file path not exist
	 */
	public static Bitmap correct(String filepath){

		Log.e("dddddddddddddddddddd", "filepath   :  " + filepath);
		
		Bitmap bitmap = null;
		
		try {

				if(filepath.length() > 0 ){
					File file = new  File(filepath);
					bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				}else{
					return null;
				}

			ExifInterface ei = new ExifInterface(filepath);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

			switch(orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				bitmap=   rotateImage(bitmap, 90);
				if(filepath.length()>0)
					SaveBitmapToFile(filepath, bitmap);
				Log.e("dddddddddddddddddddd", "90");
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				bitmap=   rotateImage(bitmap, 180);
				if(filepath.length()>0)
					SaveBitmapToFile(filepath, bitmap);
				Log.e("dddddddddddddddddddd", "180");
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				bitmap=   rotateImage(bitmap, 270);
				if(filepath.length()>0)
					SaveBitmapToFile(filepath, bitmap);
				Log.e("dddddddddddddddddddd", "270");
				break;
			}
		} 
		catch (IOException e) {}


		return bitmap;
	}


	private static void SaveBitmapToFile(String filepath, Bitmap bitmap) {

		FileOutputStream out = null;
		try {
			File imgFile = new  File(filepath);
			out = new FileOutputStream(imgFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private static Bitmap rotateImage(Bitmap source, float angle) {
		Bitmap retVal;

		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

		return retVal;
	}

}
