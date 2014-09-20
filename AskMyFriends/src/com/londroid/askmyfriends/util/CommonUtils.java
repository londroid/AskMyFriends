package com.londroid.askmyfriends.util;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class CommonUtils {
	
	public static Bitmap getContentResolvedUriAsBitmap(ContentResolver contentResolver, String uri) {
		InputStream input = null;

		try {	

			input = contentResolver.openInputStream(Uri.parse(uri));

			if (input != null) {
				Bitmap contactPhotoBitmap = BitmapFactory.decodeStream(input);
				return contactPhotoBitmap;
			} else {
				return null;
			}
			
		} catch (FileNotFoundException e) {
			Log.i("AMF", "FileNotFoundException");
			return null;
		}
	}
}
