package com.codepath.apps.advancedtwitterclient.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.advancedtwitterclient.R;
import com.codepath.apps.advancedtwitterclient.fragments.UserHeaderFragment;
import com.codepath.apps.advancedtwitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.advancedtwitterclient.models.User;
import com.codepath.apps.advancedtwitterclient.restEndpoints.TwitterApplication;
import com.codepath.apps.advancedtwitterclient.restEndpoints.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 123;
    private static final int RESULT_OK = 200;
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        // get account info
        boolean owner;

        // get screen name passed from activity launched it
        String screenName = getIntent().getStringExtra("screen_name");
        ActionBar actionBar = getSupportActionBar();

        if(screenName != null){
            owner=false;
            actionBar.setTitle(screenName);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        else{
            owner = true;
            actionBar.setTitle("My Profile");
        }

        if (savedInstanceState == null) {
            UserTimelineFragment fragmentTimeline = UserTimelineFragment.newInstance(screenName);
            UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(String.valueOf(owner),screenName);

            // Display user fragment in this activity - dynamic way
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentTimeline);
            ft.replace(R.id.flUserHeader,userHeaderFragment);
            ft.commit();
        }
    }

    private void populateUserHeader(User user) {
        // Load information from user object
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_compose:
                Intent i = new Intent(this,PostTweets.class);
                startActivityForResult(i, REQUEST_CODE);
                return true;

            case R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
