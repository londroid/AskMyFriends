package com.londroid.askmyfriends.persistence.contentprovider;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;

import com.londroid.askmyfriends.activities.SendSMS;

public class ContactLoader implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private ContactInfoAdapter contactAdapter;
	private SendSMS smsActivity;
	
	// Contacts data
	private static final String[] CONTACTS_ROWS = new String[] {
				CommonDataKinds.Phone._ID,
				CommonDataKinds.Phone.DISPLAY_NAME, 
				CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI, 
				CommonDataKinds.Phone.NUMBER
	};
	
	public ContactLoader(SendSMS smsActivity, ContactInfoAdapter adapter) {
		this.contactAdapter = adapter;
		this.smsActivity = smsActivity;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		String filterString =  args == null ? "A" : args.getString("filterString", "A");
		
//		// String used to filter contacts 
		String select = "((" + CommonDataKinds.Phone.DISPLAY_NAME + " NOTNULL) AND ("
				+ CommonDataKinds.Phone.DISPLAY_NAME + " != '') AND ( " +
				  CommonDataKinds.Phone.DISPLAY_NAME +" LIKE ? ))";
//
//		// String used for defining the sort order
		String sortOrder = CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
//
//	    return new CursorLoader(smsActivity, Contacts.CONTENT_URI, CONTACTS_ROWS,
//						select, null, sortOrder);
	    
	    Uri uri = CommonDataKinds.Phone.CONTENT_URI;
	    return new CursorLoader(smsActivity, uri, CONTACTS_ROWS, select, new String[]{ filterString + "%"}, sortOrder);
	    
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor into the adapter
		contactAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		contactAdapter.swapCursor(null);
	}
	
}
