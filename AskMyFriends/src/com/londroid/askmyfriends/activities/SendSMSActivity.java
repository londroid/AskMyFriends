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
import android.widget.Toast;

import com.londroid.askmyfriends.R;
import com.londroid.askmyfriends.activities.helpers.SendSMSHelper;
import com.londroid.askmyfriends.activities.helpers.SendSMSViewData;
import com.londroid.askmyfriends.persistence.contentprovider.ContactInfoAdapter;
import com.londroid.askmyfriends.persistence.contentprovider.ContactLoader;
import com.londroid.askmyfriends.utils.ContactsAutoCompleteTextView;
import com.londroid.askmyfriends.viewobjects.AnswerDto;
import com.londroid.askmyfriends.viewobjects.JurorDto;
import com.londroid.askmyfriends.viewobjects.QuestionDto;
import com.londroid.askmyfriends.viewobjects.SurveyDto;
import com.londroid.askmyfriends.viewobjects.SurveyType;


public class SendSMSActivity extends ActionBarActivity {

	private EditText mQuestion, mOptionA, mOptionB, mOptionC, mOptionD, mFriend2, mFriend3;
	
	private ContactsAutoCompleteTextView mFriend1;
	
	private SendSMSHelper sendSmsHelper;
	
	public static final String EXTRA_SURVEY_ID_KEY = "surveyId";

	public static final String EXTRA_ACTION_KEY = "action";
	
	private SurveyDto selectedSurvey;
	
	public void initData() {
		// Get the helper (controller) to manage actions
		sendSmsHelper = SendSMSHelper.setupAndGet(SendSMSActivity.this);
	}
	
	public void setupViews() {
		// Setup views
		setContentView(R.layout.activity_send_sms);

		mQuestion = (EditText) findViewById(R.id.etQuestion);
		mOptionA = (EditText) findViewById(R.id.etOptionA);
		mOptionB = (EditText) findViewById(R.id.etOptionB);
		mOptionC = (EditText) findViewById(R.id.etOptionC);
		mOptionD = (EditText) findViewById(R.id.etOptionD);
		mFriend1 = (ContactsAutoCompleteTextView) findViewById(R.id.atFriend1);
		mFriend2 = (EditText) findViewById(R.id.etFriend2);
		mFriend3 = (EditText) findViewById(R.id.etFriend3);
		setupContactView(mFriend1);
	
	}
	
	public void initActivityAccordingToIntent() {
		
		Bundle bundle = getIntent().getExtras();
		
		if (bundle != null) {
			
			ActionType actionType = (ActionType)bundle.get(EXTRA_ACTION_KEY);
			
			if (actionType != null) {
				
				if (actionType == ActionType.EDIT_SURVEY) {
					
					Long surveyId = bundle.getLong(EXTRA_ACTION_KEY);
					selectedSurvey = sendSmsHelper.getSurvey(surveyId);
					fillUiWithSurvey();
				}
			}
		}

		fillUiWithDefaults();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupViews();
		initData();
		
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
	 * Setup of contact info adapter using Cursor loader and custom autocomplete view for this activity
	 * 
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
	
	/**
	 * This is the action performed when tapping the Send button in the UI
	 * 
	 * @param view
	 */
	public void sendSMS(View view) {
		SurveyDto surveyDto = collectSurveyDataFromUi();
		try {
			sendSmsHelper.sendSMS(surveyDto);
			Toast.makeText(this, "Survey successfully sent", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "Error sending survey", Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getTextFromEditText(EditText editText) {
		if (editText.getText() != null) {
			String text = editText.getText().toString().trim();
			if (!"".equals(text)) {
				return text;
			}	
			return null;
		}
		return null;
	}
	
	private SurveyDto collectSurveyDataFromUi() {
		
		SurveyDto surveyDto = new SurveyDto();
		surveyDto.setTitle("Title Of Survey");
		surveyDto.setSurveyType(SurveyType.SINGLE_ANSWER);
		String question = mQuestion.getText() != null ? mQuestion.getText().toString() : null;
		QuestionDto questionDto = new QuestionDto();
		questionDto.setText(question);
		
		//TODO: change Editables to get number from contact data
		List<JurorDto> jurorDtos = new ArrayList<JurorDto>();
		
		String phoneNumber1 = getTextFromEditText(mFriend1);
		String phoneNumber2 = getTextFromEditText(mFriend2);
		String phoneNumber3 = getTextFromEditText(mFriend3);
		
		//TODO: Validate jurors
		JurorDto jurorDto = new JurorDto();
		
		if (phoneNumber1 != null) {
			jurorDto = new JurorDto();
			jurorDto.setName(phoneNumber1);
			jurorDtos.add(jurorDto);
		}
		
		if (phoneNumber2 != null) {
			jurorDto = new JurorDto();
			jurorDto.setName(phoneNumber2);
			jurorDtos.add(jurorDto);
		}
		
		if (phoneNumber3 != null) {
			jurorDto = new JurorDto();
			jurorDto.setName(phoneNumber3);
			jurorDtos.add(jurorDto);
		}
		
		// Collect answers
		List<AnswerDto> answerDtos = new ArrayList<AnswerDto>();
		
		Map<String, String> answers = new LinkedHashMap<String, String>();
		
		String optionA = getTextFromEditText(mOptionA);
		String optionB = getTextFromEditText(mOptionB);
		String optionC = getTextFromEditText(mOptionC);
		String optionD = getTextFromEditText(mOptionD);
		
		AnswerDto answerDto;
		
		if (optionA != null) {
			answerDto = new AnswerDto();
			answerDto.setListingTag("A");
			answerDto.setOrder(0);
			answerDto.setText(optionA);
			answerDtos.add(answerDto);
		}
		
		if (optionB != null) {
			answerDto = new AnswerDto();
			answerDto.setListingTag("B");
			answerDto.setOrder(1);
			answerDto.setText(optionB);
			answerDtos.add(answerDto);
		}
		
		if (optionC != null) {
			answerDto = new AnswerDto();
			answerDto.setListingTag("C");
			answerDto.setOrder(2);
			answerDto.setText(optionC);
			answerDtos.add(answerDto);
		}
		
		if (optionD != null) {
			answerDto = new AnswerDto();
			answerDto.setListingTag("D");
			answerDto.setOrder(3);
			answerDto.setText(optionD);
			answerDtos.add(answerDto);
		}
				
		surveyDto.setJurors(jurorDtos);
		surveyDto.setAnswers(answerDtos);
		
		return surveyDto;
	}

	private void fillUiWithSurvey() {
		if (selectedSurvey != null) {
			//TODO: take selected object and fill UI with it
		}
	}
	
	private void fillUiWithDefaults() {
		//TODO: reset or put default data in the UI
	}
}
