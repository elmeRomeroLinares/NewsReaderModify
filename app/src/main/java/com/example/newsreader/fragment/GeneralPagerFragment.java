package com.example.newsreader.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsreader.CategoriesRecyclerAdapter;
import com.example.newsreader.DetailActivity;
import com.example.newsreader.R;
import com.example.newsreader.data.DatabaseLoader;
import com.example.newsreader.data.NewsContract.NewsEntry;
import com.example.newsreader.model.Article;
import com.example.newsreader.model.ArticleResponse;
import com.example.newsreader.model.Results;
import com.example.newsreader.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class GeneralPagerFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Article>>,
        CategoriesRecyclerAdapter.OnCategoryItemClick, CategoriesRecyclerAdapter.OnSaveButtonItemClick {

    private Unbinder unbinder;

    @BindView(R.id.progress_bar_pager)
    ProgressBar mProgressBar;

    @BindView(R.id.no_connection_tv)
    TextView mNoConnectionTV;

    // RecyclerView and adapter
    @BindView(R.id.category_recycler_view)
    RecyclerView mCategoryRecyclerView;
    private CategoriesRecyclerAdapter mCategoriesRecyclerAdapter;

    // Loaders
    private static final String CATEGORY_TYPE = "loaderType";

    // Loader ID initialisation
    private int mCategoryId = 0;

    // Query variable
    private String mQuery;

    // Page number initialisation
    private int mPageNumber = 1;

    // Recycler Views State
    private Boolean mIsScrolling = false;
    private int mCurrentItems;
    private int mTotalItems;
    private int mScrollItems;

    // Keys
    // Bundle key receiving query Params
    private static String CATEGORY = "category";

    // Detail Activity Bundle key
    private static String DETAIL_BUNDLE = "detailData";

    // Loader ID constant
    private static String LOADER_ID = "loaderId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories_recycler, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("GeneralPagerFragment", "onViewCreated");

        if (this.getArguments() != null) {
            mQuery = this.getArguments().getString(CATEGORY);
            mCategoryId = this.getArguments().getInt(LOADER_ID);
        }

        unbinder = ButterKnife.bind(this, view);

        final LinearLayoutManager lm = new LinearLayoutManager(getContext());

        // Set Layout Manager on Recycler View
        mCategoryRecyclerView.setLayoutManager(lm);

        mCategoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mCurrentItems = lm.getChildCount();
                mTotalItems = lm.getItemCount();
                mScrollItems = lm.findFirstVisibleItemPosition();

                if (mIsScrolling && (mCurrentItems + mScrollItems == mTotalItems) && mCategoryId != 5) {
                    mIsScrolling = false;
                    recyclerSetup();
                }
            }
        });

        if (mCategoryId != 5) {
            if (isNetworkConnected()) {
                mProgressBar.setVisibility(View.VISIBLE);
                loadFromApi(mQuery, mPageNumber);
            }
        } else {
            loadReadLater();
        }
    }

    private void recyclerSetup() {
        mCategoryRecyclerView.post(() -> {
            mPageNumber++;
            if (isNetworkConnected()) {
                loadFromApi(mQuery, mPageNumber);
            }
        });
    }

    private void loadFromApi(String category, int page) {

        Call<ArticleResponse> call = RetrofitClientInstance.getRetrofitInstance().
                getData(page, category);

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull Response<ArticleResponse> response) {
                if (response.isSuccessful() && response.body() != null && mProgressBar != null) {

                    mProgressBar.setVisibility(GONE);

                    // call data converter
                    dataConverter(response.body().getResponse().getResults());
                } else {
                    Toast.makeText(getContext(), "An error occurred please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "An error occurred please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dataConverter(List<Results> apiResults) {
        ArrayList<Article> articles = new ArrayList<>();
        for (int i = 0; i < apiResults.size(); i++) {
            Results r = apiResults.get(i);
            articles.add(new Article(r.getSectionName(), r.getWebUrl(), r.getFields().getHeadline(),
                    r.getFields().getThumbnail(), r.getFields().getBody()));
        }

        bindData(articles);
    }

    private void bindData(ArrayList<Article> results) {

        if (mCategoriesRecyclerAdapter == null) {
            mCategoriesRecyclerAdapter = new CategoriesRecyclerAdapter(results, this, this, mCategoryId);
            mCategoryRecyclerView.setAdapter(mCategoriesRecyclerAdapter);
        } else {
            if (mCategoryRecyclerView.getAdapter() == null) {
                mCategoryRecyclerView.setAdapter(mCategoriesRecyclerAdapter);
            }
            mCategoriesRecyclerAdapter.addItems(results, mCategoryId);
        }
    }

    private void loadReadLater() {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(CATEGORY_TYPE, mCategoryId);
        if (getActivity() != null) {
            LoaderManager.getInstance(getActivity()).restartLoader(mCategoryId, queryBundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, @Nullable Bundle args) {

        return new DatabaseLoader(requireContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(GONE);
        }

        bindData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Article>> loader) {
    }

    @Override
    public void onItemClick(Article news) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DETAIL_BUNDLE, news);

        startActivity(intent);
    }

    @Override
    public void onButtonClick(Article article, int itemPosition) {
        if (mCategoryId != 5) {
            // ContentValues object
            ContentValues values = new ContentValues();
            values.put(NewsEntry.COLUMN_ARTICLE_BODY, article.getBody());
            values.put(NewsEntry.COLUMN_ARTICLE_THUMBNAIL, article.getThumbnail());
            values.put(NewsEntry.COLUMN_ARTICLE_HEADLINE, article.getHeadline());
            values.put(NewsEntry.COLUMN_ARTICLE_WEB_URL, article.getWebUrl());
            values.put(NewsEntry.COLUMN_ARTICLE_SECTION, article.getSectionName());

            // Insert new article into provider, returning content URI for new article

            Uri newUri = null;


            if (getContext() != null) {
                newUri = getContext().
                        getContentResolver().insert(NewsEntry.CONTENT_URI, values);
            }

            // Toast message showing insertion result
            if (newUri == null) {
                Toast.makeText(getContext(), getString(R.string.editor_insert_article_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.editor_insert_article_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            String[] slectionArgs = {article.getHeadline()};
            int deletedArticle = -1;
            if (getContext() != null) {
                deletedArticle = getContext().getContentResolver().delete(NewsEntry.CONTENT_URI,
                        NewsEntry.COLUMN_ARTICLE_HEADLINE + "=?", slectionArgs);
            }

            if (deletedArticle < 0) {
                Toast.makeText(getContext(), getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.delete_successful), Toast.LENGTH_SHORT).show();
                //TODO call delete function, and update view calling notify data set changed
                mCategoriesRecyclerAdapter.deleteItem(itemPosition);
            }
        }

    }

    public void reload() {
        loadReadLater();
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = null;

        if (getContext() != null) {
            cm = (ConnectivityManager) getContext().
                    getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI ||
                            ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);
                    if (nc != null) {
                        return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                    }
                }
            }
        }

        return false;
    }

    public static GeneralPagerFragment getInstance(String query, int loaderId) {
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, query);
        bundle.putInt(LOADER_ID, loaderId);
        GeneralPagerFragment fragment = new GeneralPagerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onDestroyView() {
        Log.d("GeneralPagerFragment", "onDestroyView");

        super.onDestroyView();
        unbinder.unbind();
    }
}
