package com.example.newsreader;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newsreader.data.NewsContract.NewsEntry;
import com.example.newsreader.fragment.DetailFragment;
import com.example.newsreader.model.Article;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static String DETAILBUNDLE = "detailData";

    public static String FRAGMENTDATA = "fragmentData";

    // Article data
    Article mdata;

    @BindView(R.id.article_header)
    TextView mArticleHeadLine;

    // constructor
    public DetailActivity() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get information from intent extras
        mdata = getIntent().getParcelableExtra(DETAILBUNDLE);

        // set bundle to pass information to fragment in detail activity
        Bundle bundleInfo = new Bundle();
        String[] fragmentData = {mdata.getThumbnail(), mdata.getBody(), mdata.getWebUrl()};
        bundleInfo.putStringArray(FRAGMENTDATA, fragmentData);

        // fragment placement on UI
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundleInfo);
        fragmentTransaction.add(R.id.fragment_frame, detailFragment);
        fragmentTransaction.commit();

        mArticleHeadLine.setText(mdata.getHeadline());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_article_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_action_button:
                saveArticle();
                break;
        }
        return false;
    }

    private void saveArticle() {

        // ContentValues object
        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_ARTICLE_BODY, mdata.getBody());
        values.put(NewsEntry.COLUMN_ARTICLE_THUMBNAIL, mdata.getThumbnail());
        values.put(NewsEntry.COLUMN_ARTICLE_HEADLINE, mdata.getHeadline());
        values.put(NewsEntry.COLUMN_ARTICLE_WEB_URL, mdata.getWebUrl());
        values.put(NewsEntry.COLUMN_ARTICLE_SECTION, mdata.getSectionName());

        // Insert new article into provider, returning content URI for new article
        Uri newUri = getContentResolver().insert(NewsEntry.CONTENT_URI, values);

        // Toast message showing insertion result
        if (newUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_article_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_insert_article_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
