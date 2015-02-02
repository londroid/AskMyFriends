package com.londroid.askmyfriends.activities.handlers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.londroid.askmyfriends.activities.SendSMSActivity;

import java.lang.ref.WeakReference;

public class SmsSendingResultHandler  extends Handler {

	public static final int SHOW_FAILED = 1;
	public static final int SHOW_PARTIALLY_SUCCESSFUL = 2;
	public static final int SHOW_SUCCESSFUL = 3;

	private WeakReference<SendSMSActivity> activity = new WeakReference<SendSMSActivity>(null);
	
	public SmsSendingResultHandler(SendSMSActivity activity) {
        this.activity = new WeakReference<SendSMSActivity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {

        SendSMSActivity sendSmsActivity = activity.get();

        if (sendSmsActivity == null) {
            return;
        }


        switch (msg.what) {

            case SHOW_FAILED:
                Toast.makeText(sendSmsActivity, "Failed to send SMS with survey.", Toast.LENGTH_LONG).show();
                break;

            case SHOW_PARTIALLY_SUCCESSFUL:
                // Display the downloaded image to the user.
                Toast.makeText(sendSmsActivity, "Some jurors could not get the message, survey was saved.", Toast.LENGTH_LONG).show();
                break;

            case SHOW_SUCCESSFUL:
                // Display the downloaded image to the user.
                Toast.makeText(sendSmsActivity, "Sms with survey successfully sent.", Toast.LENGTH_LONG).show();
                break;
        }




    }
}
