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

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        String[] data = {};

        if (this.getArguments() != null) {
            data = this.getArguments().getStringArray(FRAGMENTDATA);
        }

        if (data != null){
            GlideApp.with(requireContext()).load(data[0]).fitCenter().into(mImageView);

            // Displays HTML content
            mWebView.loadDataWithBaseURL(null, data[1], "text/html", "utf-8", null);

            mUrl = data[2];
        }
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