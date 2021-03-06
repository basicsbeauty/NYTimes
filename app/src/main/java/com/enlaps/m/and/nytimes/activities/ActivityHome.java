package com.enlaps.m.and.nytimes.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.enlaps.m.and.nytimes.R;
import com.enlaps.m.and.nytimes.adapters.ArticleItemAdapter;
import com.enlaps.m.and.nytimes.adapters.EndlessRecyclerViewScrollListener;
import com.enlaps.m.and.nytimes.models.NewsArticle;
import com.enlaps.m.and.nytimes.network.HttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ActivityHome extends AppCompatActivity {

    @Bind(R.id.toolbar)     Toolbar toolbar;
    @Bind(R.id.rvArticles)  RecyclerView rvArticles;

    int     currentPage;
    String  currentQuery;

    HttpClient          httpClient;
    ArticleItemAdapter  articleAdapter;

    ArrayList<NewsArticle> articles;

    // Intent result codes
    final int INTENT_RCODE_SETTINGS=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        customize();
        setAdapters();
        currentQuery = "yahoo";
        fetchArticles(currentQuery, 0);
    }

    protected void init() {
        ButterKnife.bind(this);

        //rvArticles = (RecyclerView) findViewById(R.id.rvArticles);


        setSupportActionBar(toolbar);

        httpClient = new HttpClient();
        articles = new ArrayList<>();
    }

    protected void customize() {
    }

    protected void setAdapters() {
        articleAdapter = new ArticleItemAdapter( this, articles);

        rvArticles.setAdapter(articleAdapter);

        //rvArticles.setLayoutManager(new LinearLayoutManager(this));

        GridLayoutManager layoutManager = new GridLayoutManager( this, 4);

        rvArticles.setLayoutManager(layoutManager);

        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                currentPage = page;
                fetchArticles( currentQuery, page);
            }
        });

        // Item: On Click


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem miSearch = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(miSearch);

        // Search Settings: Add Listener
        MenuItem miSettings = menu.findItem(R.id.miSettings);

        miSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent i = new Intent( ActivityHome.this, ActivitySettings.class);
                startActivityForResult(i,INTENT_RCODE_SETTINGS);

                return false;
            }
        });

        // Search Item: Add Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Query", query);

                currentQuery = query;
                articles.clear();

                fetchArticles(query, 0);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    public void fetchArticles( String query, final int pageNumber) {

        httpClient.getArticles(query, pageNumber, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<NewsArticle> tempArticles;
                tempArticles = httpClient.processGet(response);

                articles.addAll(tempArticles);
                Log.i("Count 3:", Integer.toString(articles.size()));

                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Context context = getApplicationContext();
                Toast.makeText(context, context.getText(R.string.string_internet_error), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
