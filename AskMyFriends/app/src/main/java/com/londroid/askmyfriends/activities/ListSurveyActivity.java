package com.londroid.askmyfriends.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.londroid.askmyfriends.R;
import com.londroid.askmyfriends.activities.mapper.MapperUtil;
import com.londroid.askmyfriends.facade.SurveyFacade;
import com.londroid.askmyfriends.facade.SurveyFacadeImpl;
import com.londroid.askmyfriends.persistence.greendao.domain.Survey;
import com.londroid.askmyfriends.viewobjects.SurveyDto;

import java.util.ArrayList;
import java.util.List;

public class ListSurveyActivity extends ActionBarActivity {

    private ArrayAdapter<SurveyDto> surveyAdapter;
    private SurveyFacade surveyFacade;
    private MapperUtil mapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mapper = MapperUtil.setupAndGet();
        surveyFacade = SurveyFacadeImpl.get(this);
        List<SurveyDto> surveyList = getSurveyList();

        Log.i("AMF", "There are " + surveyList.size() + " surveys saved, attaching adapter to ListView");

        surveyAdapter = new ArrayAdapter<SurveyDto>(this, android.R.layout.simple_list_item_1, surveyList);
        ListView listView = (ListView) findViewById(R.id.listViewSurveys);
        listView.setAdapter(surveyAdapter);

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
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public List<SurveyDto> getSurveyList() {
        List<Survey> surveys = surveyFacade.findAllSurveys();
        List<SurveyDto> surveysDtos = new ArrayList<SurveyDto>();
        for (Survey survey: surveys) {
            surveysDtos.add(mapper.mapToSurveyDto(survey));
            Log.d("AMF", "Survey read, question " + survey.getQuestion().getText());
        }
        return surveysDtos;
    }




}
