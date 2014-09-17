package com.londroid.askmyfriends.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.londroid.askmyfriends.R;
import com.londroid.askmyfriends.activities.helpers.SendSMSHelper;
import com.londroid.askmyfriends.activities.helpers.SendSMSViewData;

public class SendSMS extends ActionBarActivity {

	private EditText mQuestion, mOptionA, mOptionB, mOptionC, mOptionD,
			mFriend1, mFriend2, mFriend3;

	// private SharedPreferences preferences;
	
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
		mFriend1 = (AutoCompleteTextView) findViewById(R.id.atFriend1);
		mFriend2 = (EditText) findViewById(R.id.etFriend2);
		mFriend3 = (EditText) findViewById(R.id.etFriend3);
		
		sendSmsHelper = SendSMSHelper.setupAndGet(SendSMS.this);
		Log.d("AMF","Setup completed.");
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
	
	
	
	
	
	
	
	
//	public void sendSMS(View view) {
//
//		List<String> phoneNumbers = new ArrayList<String>();
//		resetResults();
//		
//		phoneNumbers.add(mFriend1.getText().toString());
//		phoneNumbers.add(mFriend2.getText().toString());
//		phoneNumbers.add(mFriend3.getText().toString());
//
//		String message = "Sent using AskMyFriends app for Android. "
//				+ "I want to ask you something. To vote for an option send me back "
//				+ "a single-letter sms. e.g. to vote A text back A.";
//
//		message += " " + mQuestion.getText().toString() + "? ";
//		message += "A - " + mOptionA.getText().toString() + "; ";
//		message += "B - " + mOptionB.getText().toString() + "; ";
//		message += "C - " + mOptionC.getText().toString() + "; ";
//		message += "D - " + mOptionD.getText().toString() + "; ";
//
//		SmsManager smsManager = SmsManager.getDefault();
//
//		for (String phoneNumber : phoneNumbers) {
//			ArrayList<String> parts = smsManager.divideMessage(message);
//
//			smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null,
//					null);
//
//			ContentValues values = new ContentValues();
//
//			values.put("address", phoneNumber);
//
//			values.put("body", message);
//
//			getContentResolver()
//					.insert(Uri.parse("content://sms/sent"), values);
//		}
//	}
//
//	public void resetResults() {
//		preferences = getSharedPreferences("results", Context.MODE_PRIVATE);
//		preferences.edit().clear().commit();
//		saveOptionsInPreferences();
//	}
//
//	public void saveOptionsInPreferences() {
//		Editor editor = preferences.edit();
//		editor.putString("question", mQuestion.getText().toString());
//		editor.putString("optionA", mOptionA.getText().toString());
//		editor.putString("optionB", mOptionB.getText().toString());
//		editor.putString("optionC", mOptionC.getText().toString());
//		editor.putString("optionD", mOptionD.getText().toString());
//
//		// Commit the edits!
//		editor.commit();
//	}
	
	
	
	
	
	
	
}
