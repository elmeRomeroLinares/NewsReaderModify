package com.example.newsreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.newsreader.model.Article;
import com.example.newsreader.model.ArticleResponse;
import com.example.newsreader.model.Results;
import com.example.newsreader.network.GetDataService;
import com.example.newsreader.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.newsreader.DetailActivity.DETAILBUNDLE;

public class PanelSearchView extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.list)
    ListView mListView;

    @BindView(R.id.emptyView)
    TextView mEmptyView;

    @BindView(R.id.panel_search_progress)
    ProgressBar mSearchProgressBar;

    private ArrayAdapter mAdapter;

    private ArrayList<Article> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_panel_search_view);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mListView.setEmptyView(mEmptyView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent startIntent = new Intent(PanelSearchView.this, DetailActivity.class);
                startIntent.putExtra(DETAILBUNDLE, mData.get(i));
                startActivity(startIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(mSearch);
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(mSearchProgressBar != null) {
                    mSearchProgressBar.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                if(mAdapter != null && !mAdapter.isEmpty()) {
                    mAdapter.clear();
                    mData.clear();
                }
                loadData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void loadData(String query) {

        Call<ArticleResponse> call = RetrofitClientInstance.getRetrofitInstance().
                getData(GetDataService.INITIAL_DATE, GetDataService.END_DATE, GetDataService.FIELDS,
                        1, "20", query, GetDataService.VALUE_KEY);

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull Response<ArticleResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    // call data converter
                    dataConverter(response.body().getResponse().getResults());

                } else {
                    Toast.makeText(PanelSearchView.this, "An error occurred please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {
                Toast.makeText(PanelSearchView.this, "An error occurred please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void dataConverter(List<Results> apiResults) {
        for (int i = 0; i < apiResults.size(); i++) {
            Results r = apiResults.get(i);
            mData.add(new Article(r.getSectionName(), r.getWebUrl(), r.getFields().getHeadline(),
                    r.getFields().getThumbnail(), r.getFields().getBody()));
        }

        bindData(mData);
    }

    private void bindData(ArrayList<Article> articles){
        ArrayList<String> listData = new ArrayList<>();

        for (int i = 0; i < articles.size(); i++) {
            listData.add(articles.get(i).getHeadline());
        }

        if (mSearchProgressBar != null) {
            mSearchProgressBar.setVisibility(View.GONE);
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listData);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.addAll(listData);
            mAdapter.notifyDataSetChanged();
        }
    }
}
