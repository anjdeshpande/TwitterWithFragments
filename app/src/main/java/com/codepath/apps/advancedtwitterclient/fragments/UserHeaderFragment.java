package com.codepath.apps.advancedtwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.advancedtwitterclient.R;
import com.codepath.apps.advancedtwitterclient.models.Tweet;
import com.codepath.apps.advancedtwitterclient.models.User;
import com.codepath.apps.advancedtwitterclient.restEndpoints.TwitterApplication;
import com.codepath.apps.advancedtwitterclient.restEndpoints.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by adeshpa on 6/12/16.
 */
public class UserHeaderFragment extends Fragment {
    private TwitterClient client;
    private ImageView ivProfileImage;
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvStatus;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        String owner = getArguments().getString("owner");
        String screenName = getArguments().getString("screen_name");
        if(Boolean.parseBoolean(owner)) {
            getOwnerProfile();
        }
        else {
            getUserProfile(screenName);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_header, container, false);
        setUpViews(v);
        return v;
    }

    public static UserHeaderFragment newInstance(String owner, String screenName) {
        UserHeaderFragment userHeaderFragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putString("owner", owner);
        args.putString("screen_name",screenName);
        userHeaderFragment.setArguments(args);
        return userHeaderFragment;
    }

    private void setUpViews(View v) {
        ivProfileImage = (ImageView)v.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView)v.findViewById(R.id.tvUserName);
        tvScreenName = (TextView)v.findViewById(R.id.tvScreenName);
        tvStatus = (TextView)v.findViewById((R.id.tvProStatus));
        tvFollowers=(TextView)v.findViewById(R.id.tvProFollowers);
        tvFollowing=(TextView)v.findViewById(R.id.tvProFollowing);


    }
    public void getOwnerProfile(){
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                // deserialize json and create models add to adapter
                user = User.fromJson(response);
                setFragmentFields();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }

        });
    }

    public void getUserProfile(String screenName){
        screenName = screenName.replace("@","");
        client.userLookup(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                // deserialize json and create models add to adapter
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    user = User.fromJson(jsonObject);
                    setFragmentFields();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }


        }, screenName);

    }

    private void setFragmentFields() {
        Glide.with(getActivity()).load(user.getProfileImageUrl()).placeholder(R.drawable.abc_spinner_mtrl_am_alpha).into(ivProfileImage);
        tvUserName.setText(user.getName().toString());
        tvScreenName.setText(user.getScreenName().toString());
        tvStatus.setText(user.getStatus().toString());
        tvFollowers.setText(String.valueOf(user.getFollowers()));
        tvFollowing.setText(String.valueOf(user.getFollowing()));
    }
}
