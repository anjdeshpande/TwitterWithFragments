package com.codepath.apps.advancedtwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.apps.advancedtwitterclient.R;

/**
 * Created by adeshpa on 6/5/16.
 */
public class ComposeDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private TextView tvCompose;
    private Button btnTweet;

    public ComposeDialogFragment() {
    }

    public interface ComposeDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public static ComposeDialogFragment newInstance(String title) {
        ComposeDialogFragment frag = new ComposeDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_tweet, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tvCompose = (TextView) view.findViewById(R.id.tvTweetMsg);

        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        // Fetch arguments from bundle and set title
        //String title = getArguments().getString("title", "Enter Name");
        //getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        tvCompose.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        tvCompose.setOnEditorActionListener(this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            ComposeDialogListener activity = (ComposeDialogListener) getActivity();
            activity.onFinishEditDialog(tvCompose.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }

    private void closeDialog() {
        this.dismiss();
    }
}
