package com.londroid.askmyfriends.persistence.contentprovider;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;

public class ContactLoader implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private ContactInfoAdapter contactAdapter;
	private LoaderManager loaderManager;
	private Context context;
	
	// Contacts data
	private static final String[] CONTACTS_ROWS = new String[] {
				CommonDataKinds.Phone._ID,
				CommonDataKinds.Phone.DISPLAY_NAME, 
				CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI, 
				CommonDataKinds.Phone.NUMBER
	};
	
	public ContactLoader(Context context, LoaderManager loaderManager, ContactInfoAdapter adapter) {
		this.contactAdapter = adapter;
		this.context = context;
		this.loaderManager = loaderManager;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		String filterString =  args == null ? "A" : args.getString("filterString", "A");
		
		// String used to filter contacts 
		String select = "((" + CommonDataKinds.Phone.DISPLAY_NAME + " NOTNULL) AND ("
				+ CommonDataKinds.Phone.DISPLAY_NAME + " != '') AND ( " +
				  CommonDataKinds.Phone.DISPLAY_NAME +" LIKE ? ))";

		// String used for defining the sort order
		String sortOrder = CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
	    
	    Uri uri = CommonDataKinds.Phone.CONTENT_URI;
	    
	    return new CursorLoader(context, uri, CONTACTS_ROWS, select, new String[]{ filterString + "%"}, sortOrder);
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
	
	public void restart(Bundle args) {
		loaderManager.restartLoader(0, args, this);
	}
	
}
