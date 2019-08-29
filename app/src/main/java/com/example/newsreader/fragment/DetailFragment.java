package com.example.newsreader.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsreader.R;
import com.example.newsreader.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.newsreader.DetailActivity.FRAGMENTDATA;

public class DetailFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.imageView)
    ImageView mImageView;

    @BindView(R.id.textView)
    WebView mWebView;

    private String mUrl = "";

    public DetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detail_activity, container, false);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        String [] data = {};
        if(this.getArguments().getStringArray(FRAGMENTDATA) != null) {
            data = this.getArguments().getStringArray(FRAGMENTDATA);
        }
        final String mThumbnail = data[0];
        final String mBodyText = data[1];
        mUrl = data[2];

        GlideApp.with(getContext()).load(mThumbnail).fitCenter().into(mImageView);

        // Displays HTML content
        mWebView.loadDataWithBaseURL(null, mBodyText, "text/html", "utf-8", null);
    }

    @OnClick(R.id.button2)
    public void initBrowse() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
        startActivity(browserIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}