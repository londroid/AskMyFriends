package com.londroid.askmyfriends.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.londroid.askmyfriends.persistence.contentprovider.ContactLoader;

public class ContactsAutoCompleteTextView extends AutoCompleteTextView {
		
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
	 * Called when the text changes and filtering is triggered in the UI. 
	 * The contact loader is then restarted using the new filtering string passed in the Bundle
	 */
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
    	
    	Bundle args = new Bundle();
    	args.putString("filterString", text.toString());
    	Log.w("AMF", "Filtering string changed: " +  text.toString());
    	
    	contactLoader.restart(args);
    }

    public void setContactLoader(ContactLoader contactLoader) {
    	this.contactLoader = contactLoader;
    }
		
}
