package com.londroid.askmyfriends.persistence.contentprovider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;

import com.londroid.askmyfriends.activities.SendSMS;
import com.londroid.askmyfriends.util.CommonUtils;


public class ContactInfoAdapter extends ResourceCursorAdapter   {

	private SendSMS smsActivity;

	public ContactInfoAdapter(Context context, int layout, Cursor c, int flags) {

		super(context, layout, c, flags);
		this.smsActivity = (SendSMS) context;
	}

	/**
	 * This method is called when a new view is needed by the adapter
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return smsActivity.bindNewDataToSingleContactView(parent);
	}

	/** 
	 * Called when a new data view is needed, but an old view is 
	 * available for reuse
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

	    int indexName = cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME);
	    int indexPhoneNumber = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
	    int indexPhoto =  cursor.getColumnIndex(CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);
	    

	    String contactName = cursor.getString(indexName);
	    String phoneNumber = cursor.getString(indexPhoneNumber);
	    String photoUriString = cursor.getString(indexPhoto);
	    
		ContactDto contactInfo = new ContactDto();
		contactInfo.setContactName(contactName);
		contactInfo.setPhoneNumber(phoneNumber);
		Bitmap b = photoUriString == null ? null : CommonUtils.getContentResolvedUriAsBitmap(context.getContentResolver(), photoUriString);
		contactInfo.setContactPhoto(b);
		
		Log.d("AMF", "Data retrieved for contact " + contactName);
		
		smsActivity.bindDataToSingleContactView(contactInfo, view);
	}

}
