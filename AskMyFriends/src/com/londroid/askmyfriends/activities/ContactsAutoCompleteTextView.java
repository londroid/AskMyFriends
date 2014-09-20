package com.londroid.askmyfriends.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.londroid.askmyfriends.persistence.contentprovider.ContactLoader;

public class ContactsAutoCompleteTextView extends AutoCompleteTextView {
	
	private LoaderManager loaderManager;


	private ContactLoader contactLoader;

	
	public ContactsAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	
	
	public ContactsAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public ContactsAutoCompleteTextView(Context context) {
		super(context);
	}

	/**
	 * Called when the text changes and filtering is triggered in the UI. Restart contactLoader here
	 */
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
//        String filterText = "";
//        super.performFiltering(filterText, keyCode);
    	
    	Bundle args = new Bundle();
    	args.putString("filterString", text.toString());
    	
    	Log.w("AMF", "Filtering string changed: " +  text.toString());
    	
    	loaderManager.restartLoader(0, args, contactLoader);
    }

    public void setContactLoader(ContactLoader contactLoader) {
    	this.contactLoader = contactLoader;
    }
    
	public void setLoaderManager(LoaderManager loaderManager) {
		this.loaderManager = loaderManager;
	}
}
