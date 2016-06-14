package com.codepath.apps.advancedtwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.advancedtwitterclient.R;
import com.codepath.apps.advancedtwitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.advancedtwitterclient.listeners.EndlessScrollListener;
import com.codepath.apps.advancedtwitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adeshpa on 6/11/16.
 */
public abstract class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    protected ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;

    // Inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        setUpViews(v);
        return v;
    }

    private void setUpViews(View v) {
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount){
                customLoadMore(page);
                return true;
            }
        });

        swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aTweets.clear();
                customLoadMore(1);
                swipeContainer.setRefreshing(false);

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    // Creation life cycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet>tweets) {
        aTweets.addAll(tweets);
    }

    protected abstract void customLoadMore(int page);

    public ListView getListView() {
        return lvTweets;
    }
}
