package com.londroid.askmyfriends.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.LoaderManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.londroid.askmyfriends.R;
import com.londroid.askmyfriends.activities.helpers.SendSMSHelper;
import com.londroid.askmyfriends.activities.helpers.SendSMSViewData;
import com.londroid.askmyfriends.facade.SurveyFacade;
import com.londroid.askmyfriends.persistence.contentprovider.ContactInfoAdapter;
import com.londroid.askmyfriends.persistence.contentprovider.ContactLoader;
import com.londroid.askmyfriends.utils.ContactsAutoCompleteTextView;


public class SendSMSActivity extends ActionBarActivity {

	private EditText mQuestion, mOptionA, mOptionB, mOptionC, mOptionD, mFriend2, mFriend3;
	
	private ContactsAutoCompleteTextView mFriend1;
	
	private SendSMSHelper sendSmsHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_send_sms);

		mQuestion = (EditText) findViewById(R.id.etQuestion);
		mOptionA = (EditText) findViewById(R.id.etOptionA);
		mOptionB = (EditText) findViewById(R.id.etOptionB);
		mOptionC = (EditText) findViewById(R.id.etOptionC);
		mOptionD = (EditText) findViewById(R.id.etOptionD);
		mFriend1 = (ContactsAutoCompleteTextView) findViewById(R.id.atFriend1);
		mFriend2 = (EditText) findViewById(R.id.etFriend2);
		mFriend3 = (EditText) findViewById(R.id.etFriend3);
		
		sendSmsHelper = SendSMSHelper.setupAndGet(SendSMSActivity.this);
		
		setupContactView(mFriend1);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_sm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Setup of contact info adapter using Cursor loader and custom autocomplete view ----
	 */
	private void setupContactView(final ContactsAutoCompleteTextView contactsView) {
		
		// Setup custom list adapter extending cursor adapter
		ContactInfoAdapter contactAdapter = new ContactInfoAdapter(this, R.layout.single_contact, null, 0);
		contactsView.setAdapter(contactAdapter);
		
		// Write the phone number in the view when clicking on a contact from the list
		contactsView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {    
                LinearLayout ll = (LinearLayout) arg1;
                TextView tv = (TextView) ll.findViewById(R.id.tv_contact_phone_number);
                contactsView.setText(tv.getText().toString());
            }
        });
		
		// Setup the contact loader from the content provider
		LoaderManager loaderManager = getLoaderManager();
		ContactLoader contactLoader = new ContactLoader(this, loaderManager, contactAdapter);
		loaderManager.initLoader(0, null, contactLoader);
		contactsView.setContactLoader(contactLoader);
		
		Log.w("AMF","Contacts view setup completed.");
	}
	
	public void sendSMS(View view) {
		SendSMSViewData viewData = collectDataFromViews();
		sendSmsHelper.sendSMS(viewData);
		Log.d("AMF","Sending SMS...");
	}
	
	private SendSMSViewData collectDataFromViews() {
		
		SendSMSViewData viewData = new SendSMSViewData();
		
		List<String> phoneNumbers = new ArrayList<String>();
		
		//TODO: change to get number from contact data
		phoneNumbers.add(mFriend1.getText().toString());
		phoneNumbers.add(mFriend2.getText().toString()); 
		phoneNumbers.add(mFriend3.getText().toString());
		
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		options.put("A", mOptionA.getText().toString());
		options.put("B", mOptionB.getText().toString());
		options.put("C", mOptionC.getText().toString());
		options.put("D", mOptionD.getText().toString());		
		
		String question = mQuestion.getText().toString();
		
		viewData.setOptions(options);
		viewData.setQuestion(question);
		viewData.setPhoneNumbers(phoneNumbers);

		return viewData;
	}
}
