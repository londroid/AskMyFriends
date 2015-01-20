package com.londroid.askmyfriends.activities.handlers;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.londroid.askmyfriends.activities.SendSMSActivity;

public class SmsSendingResultHandler  extends Handler {

	public static final int SHOW_FAILED = 1;
	public static final int SHOW_PARTIALLY_SUCCESSFUL = 2;
	public static final int SHOW_SUCCESSFUL = 3;
	
	//TODO:
	//private WeakReference<SendSMSActivity> activity;
	
	public SmsSendingResultHandler(Context ctx) {
		
	}
	
	@Override
	public void dispatchMessage(Message message) {
		
//		switch(message.what) {
//		
//			
//			
//		
//		
//		}
		
		
	}
}
