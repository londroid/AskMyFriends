package com.londroid.askmyfriends.activities;

import com.londroid.askmyfriends.R;
import com.londroid.askmyfriends.R.id;
import com.londroid.askmyfriends.R.layout;
import com.londroid.askmyfriends.R.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public void createNewQuestion(View view) {
		Intent intent = new Intent(this, SendSMS.class);
		startActivity(intent);
	}

	public void seeResults(View view) {

		preferences = getSharedPreferences("results", Context.MODE_PRIVATE);
		String results = "";

		results += "Q: " + preferences.getString("question", "No question yet") + "\n";
		results += "A - " + preferences.getString("optionA", "No option yet") + " - "
				+ preferences.getInt("resultA", 0) + "\n";
		results += "B - " + preferences.getString("optionB", "No option yet") + " - "
				+ preferences.getInt("resultB", 0) + "\n";
		results += "C - " + preferences.getString("optionC", "No option yet") + " - "
				+ preferences.getInt("resultC", 0) + "\n";
		results += "D - " + preferences.getString("optionD", "No option yet") + " - "
				+ preferences.getInt("resultD", 0);

		Toast.makeText(this, results, Toast.LENGTH_SHORT).show();
	}
}
