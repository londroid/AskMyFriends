package com.londroid.askmyfriends.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.londroid.askmyfriends.R;
import com.londroid.askmyfriends.activities.helpers.SendSMSHelper;
import com.londroid.askmyfriends.activities.helpers.SendSMSViewData;
import com.londroid.askmyfriends.persistence.contentprovider.ContactDto;
import com.londroid.askmyfriends.persistence.contentprovider.ContactInfoAdapter;
import com.londroid.askmyfriends.persistence.contentprovider.ContactLoader;


public class SendSMS extends ActionBarActivity {

	private EditText mQuestion, mOptionA, mOptionB, mOptionC, mOptionD, mFriend2, mFriend3;
	
	private ContactsAutoCompleteTextView mFriend1;
	
	private SendSMSHelper sendSmsHelper;
	
	private ContactInfoAdapter contactAdapter;

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
		
		sendSmsHelper = SendSMSHelper.setupAndGet(SendSMS.this);
		
		// set the adapter
		this.contactAdapter = new ContactInfoAdapter(this, R.layout.single_contact, null, 0);
		mFriend1.setAdapter(contactAdapter);
        
		mFriend1.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                 
                LinearLayout ll = (LinearLayout) arg1;
                TextView tv = (TextView) ll.findViewById(R.id.tv_contact_phone_number);
                mFriend1.setText(tv.getText().toString());
                 
            }

        });
		
		ContactLoader contactLoader = new ContactLoader(this, contactAdapter);
	
		// Initialize the loader
		getLoaderManager().initLoader(0, null, contactLoader);
		mFriend1.setLoaderManager(getLoaderManager());
		mFriend1.setContactLoader(contactLoader);
		
		Log.w("AMF","Setup completed.");

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
	
	
	//TODO: This should be in a reusable activity helper (FillViewWithContactData)
	
	/**
	 * This is called by a list-type adapter when a an existing view can be reused
	 * 
	 */
	public void bindDataToSingleContactView(ContactDto contactInfo, View view) {
		
		// Name of the contact
		TextView nameTextView = (TextView) view.findViewById(R.id.tv_contact_name);
		nameTextView.setText(contactInfo.getContactName());

		// Phone number of the contact
		TextView phoneNumbeTextView = (TextView) view.findViewById(R.id.tv_contact_phone_number);
		phoneNumbeTextView.setText(contactInfo.getPhoneNumber());		
		
		// Picture of the contact
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_contact_photo);
		Bitmap contactPhotoBitmap = contactInfo.getContactThumbnail();
		
		if (contactPhotoBitmap == null) {
			contactPhotoBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_contact_picture);
		} 	

		imageView.setImageBitmap(contactPhotoBitmap);
	}
	
	/**
	 * This is called by a list-type adapter when requesting a new view
	 *  
	 */
	public View bindNewDataToSingleContactView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		return inflater.inflate(R.layout.single_contact, parent, false);
	}
}
