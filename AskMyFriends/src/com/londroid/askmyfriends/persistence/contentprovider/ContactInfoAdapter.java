package com.londroid.askmyfriends.persistence.contentprovider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract.CommonDataKinds;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.londroid.askmyfriends.R;
import com.londroid.askmyfriends.utils.CommonUtils;


public class ContactInfoAdapter extends ResourceCursorAdapter   {

	private Context context;

	public ContactInfoAdapter(Context context, int layout, Cursor c, int flags) {

		super(context, layout, c, flags);
		this.context = context;
	}

	/**
	 * This method is called when a new view is needed by the adapter
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return bindNewDataToSingleContactView(parent);
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
		
		bindDataToSingleContactView(contactInfo, view);
	}
	
	
	/**
	 * This is called by a list-type adapter when a an existing view can be reused
	 * 
	 */
	private void bindDataToSingleContactView(ContactDto contactInfo, View view) {
		
		// Name of the contact
		TextView nameTextView = (TextView) view.findViewById(R.id.tv_contact_name);
		nameTextView.setText(contactInfo.getContactName());

		// Phone number of the contact
		TextView phoneNumbeTextView = (TextView) view.findViewById(R.id.tv_contact_phone_number);
		phoneNumbeTextView.setText(contactInfo.getPhoneNumber());		
		
		// Picture of the contact
		ImageView imageView = (ImageView) view.findViewById(R.id.img_contact_photo);
		Bitmap contactPhotoBitmap = contactInfo.getContactThumbnail();
		
		if (contactPhotoBitmap == null) {
			contactPhotoBitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.ic_contact_picture);
		} 	

		imageView.setImageBitmap(contactPhotoBitmap);
	}
	
	/**
	 * This is called by a list-type adapter when requesting a new view
	 *  
	 */
	public View bindNewDataToSingleContactView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		return inflater.inflate(R.layout.single_contact, parent, false);
	}

}
