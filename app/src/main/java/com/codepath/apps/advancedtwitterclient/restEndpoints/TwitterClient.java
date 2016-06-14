package com.codepath.apps.advancedtwitterclient.restEndpoints;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "07HsKI3Dyy4Y3TIxDLb0VcutQ";       // Change this
	public static final String REST_CONSUMER_SECRET = "xZwPR9SKY1K5QEPSMLMzvURMNC8jqWxOeT3gSkIIAtQwFtCvos"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://adsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}


    // endpoint apis

    public void getHomeTimeline(AsyncHttpResponseHandler handler, int sinceId) {
        String apiUrl = REST_URL + "/statuses/home_timeline.json";
        RequestParams params = new RequestParams();
        params.put ("count", 50);
        params.put ("since_id", sinceId);

        // Execute the request
        getClient().get(apiUrl, params, handler);

    }

    // Comose the tweets
    public void compose(AsyncHttpResponseHandler handler, String tweet) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		client.post(apiUrl,params,handler);
    }

	public void getOwnProfile(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl,null,handler);
	}

	public void getMentionsTimeline(JsonHttpResponseHandler handler) {
		String apiUrl = REST_URL + "/statuses/mentions_timeline.json";
		RequestParams params = new RequestParams();
		params.put ("count", 25);

		// Execute the request
		getClient().get(apiUrl, params, handler);
	}

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = REST_URL + "/statuses/user_timeline.json";
        RequestParams params = new RequestParams();
        params.put ("count", 25);
        params.put ("screen_name", screenName);

        // Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl,null,handler);
    }

	public void userLookup(AsyncHttpResponseHandler handler, String screenName){
		String apiUrl = getApiUrl("users/lookup.json");
		RequestParams params = new RequestParams();
		params.put("screen_name",screenName);
		client.get(apiUrl,params,handler);
	}

	public void getFollowersList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		client.get(apiUrl,null,handler);
	}
}