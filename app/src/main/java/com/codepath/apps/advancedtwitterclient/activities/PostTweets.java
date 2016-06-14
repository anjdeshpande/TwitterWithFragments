package com.codepath.apps.advancedtwitterclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.advancedtwitterclient.R;
import com.codepath.apps.advancedtwitterclient.models.User;
import com.codepath.apps.advancedtwitterclient.restEndpoints.TwitterApplication;
import com.codepath.apps.advancedtwitterclient.restEndpoints.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PostTweets extends AppCompatActivity {
    private TwitterClient client;
    private EditText etTweet;
    private ImageView ivOwnerProfileImage;
    private TextView tvOwnerUserName;
    private TextView tvOwnerScreenName;
    private TextView tvCharCount;
    private User user;

    private static final int RESULT_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweets);

        client = TwitterApplication.getRestClient(); // singletone client
        setUpView();
        getOwnProfile();
        final Button btnTweet = (Button) findViewById(R.id.btnTweet);

        // Create a text watcher to check remaining characters
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int value = 140 - s.length();
                tvCharCount.setText(String.valueOf(value));
                if (value < 0) {
                    btnTweet.setEnabled(false);
                } else {
                    btnTweet.setEnabled(true);
                }
            }
            public void afterTextChanged(Editable s) {

            }
        };
        etTweet.addTextChangedListener(mTextEditorWatcher);
    }

    private void setUpView(){
        etTweet = (EditText)findViewById(R.id.etTweet);
        tvCharCount = (TextView)findViewById(R.id.tvCharCount);
        ivOwnerProfileImage = (ImageView) findViewById(R.id.ivOwnerProfileImage);
        tvOwnerUserName = (TextView)findViewById(R.id.tvOwnerProfileName);
        tvOwnerScreenName = (TextView)findViewById(R.id.tvOwnerScreenName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_post_tweet, menu);
        return true;
    }

    // Get current user profile
    public void getOwnProfile() {
        client.getOwnProfile(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                // deserialize json and create models add to adapter
                user=  User.fromJson(json);
                Glide.with(getApplicationContext()).load(user.getProfileImageUrl()).placeholder(R.drawable.abc_spinner_mtrl_am_alpha).into(ivOwnerProfileImage);
                tvOwnerUserName.setText(user.getName().toString());
                tvOwnerScreenName.setText(user.getScreenName().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    // post the tweet
    public void onPostTweet(View view) {
        String tweet = etTweet.getText().toString();
        client.compose(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                // deserialize json and create models add to adapter
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        }, tweet);
    }

    public void onCancel(View view) {
        finish();
    }

}
