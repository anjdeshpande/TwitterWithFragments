package com.codepath.apps.advancedtwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.advancedtwitterclient.models.Tweet;
import com.codepath.apps.advancedtwitterclient.restEndpoints.TwitterApplication;
import com.codepath.apps.advancedtwitterclient.restEndpoints.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by adeshpa on 6/11/16.
 */
public class MentionsTimelineFragment extends TweetsListFragment{
    private TwitterClient client;

    @Override
    protected void customLoadMore(int page) {
        populateTimeline(page);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); // singletone client
        // Initialize to show first page
        populateTimeline(1);
    }

    // send api request to get timeline json
    // fill listview
    private void populateTimeline(int sinceId) {
        client.getMentionsTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // deserialize json and create models add to adapter
                addAll(Tweet.fromJSONArray(json));
                // load model data in list view
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
