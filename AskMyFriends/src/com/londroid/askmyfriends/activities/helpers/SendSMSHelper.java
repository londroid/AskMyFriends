package com.londroid.askmyfriends.activities.helpers;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.telephony.SmsManager;

public class SendSMSHelper {

	private static SendSMSHelper instance;

	private static final String QUESTION_RESULTS_PREFERENCE_KEY = "results";
	
	private Context activityContext;
	
	private SharedPreferences preferences;
	
	private SendSMSHelper() {}
	
	public static SendSMSHelper setupAndGet(Context context) {
		
		if (instance == null) {
			instance = new SendSMSHelper();
			instance.setContext(context);
		}
		
		return instance;
	}
	
	private void setContext(Context context) {
		this.activityContext= context;
		initPreferences();
	}

	private void initPreferences() {
		this.preferences = activityContext.getSharedPreferences(QUESTION_RESULTS_PREFERENCE_KEY, Context.MODE_PRIVATE);
	}
	
	public void sendSMS(SendSMSViewData smsActivityViewData) {

		resetResults();
		String message = composeMessage(smsActivityViewData.getQuestion(), smsActivityViewData.getOptions());
		
		SmsManager smsManager = SmsManager.getDefault();

		for (String phoneNumber : smsActivityViewData.getPhoneNumbers()) {
			
			ArrayList<String> parts = smsManager.divideMessage(message);
			smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
			
			ContentValues values = new ContentValues();
			values.put("address", phoneNumber);
			values.put("body", message);

			activityContext.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
		}
		
	}
	
	private String composeMessage(String question, Map<String, String> options) {
		String message = 
				"Sent using AskMyFriends app for Android. " + 
	            "I want to ask you something. To vote for an option send me back " + 
				"a single-letter sms. e.g. to vote A text back A. \n \n";
		
		message += " " + question + "? ";
		
		for (String optionLetter : options.keySet()) {
			message += optionLetter + " - " + options.get(optionLetter) + "; \n";
		}
		
		return message;
	}

	public void resetResults() {
		
		preferences.edit().clear().commit();
	}

	public void saveOptionsInPreferences(String question, Map<String, String> optionMap) {
		Editor editor = preferences.edit();
		
		editor.putString("question", question);
		
		for (String optionLetter : optionMap.keySet()) {
			editor.putString("option" + optionLetter, optionMap.get(optionLetter));
		}
		
		// Commit the edits!
		editor.commit();
	}
	
}
