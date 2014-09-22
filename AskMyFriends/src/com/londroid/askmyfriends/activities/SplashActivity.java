package com.londroid.askmyfriends.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.londroid.askmyfriends.R;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);

        //TODO: use a Handler to post a Runnable which will load needed resources for the app and launch
        // the main activity when finished. This is not needed for the moment. For now just touch the screen 
        // and the main activity will be shown.
    }
    
    public void goToMain(View view) {
    	Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
    	finish();
    }
  
}
