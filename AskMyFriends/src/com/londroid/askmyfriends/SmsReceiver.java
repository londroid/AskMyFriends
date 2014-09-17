package com.londroid.askmyfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private SharedPreferences preferences;

	@Override
	public void onReceive(Context context, Intent intent) {
		// get the received bundle
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			// get the received SMS
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += msgs[i].getMessageBody().toString();
			}

			// Check if the message length is 1
			if (str.length() == 1) {
				// only if it does we go into more detail
				// to check if it is an answer for our question or not
				if (str.equals("A") || str.equals("B") || str.equals("C")
						|| str.equals("D")) {

					// It is an answer to our question
					// We get the preferences and save the new value
					preferences = context.getSharedPreferences("results",
							Context.MODE_PRIVATE);

					savePreferences(str);
				}
			}
		}
	}

	public void savePreferences(String str) {
		Editor editor = preferences.edit();
		// Just to add 1 to the count for the voted option
		editor.putInt("result" + str, preferences.getInt("result" + str, 0) + 1);

		editor.commit();
	}
}