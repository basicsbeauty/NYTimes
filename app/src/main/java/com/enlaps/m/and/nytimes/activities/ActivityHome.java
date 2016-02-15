package com.enlaps.m.and.nytimes.activities;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    HttpClient          httpClient;
    ArticleItemAdapter  articleAdapter;

    ArrayList<NewsArticle> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        customize();
        setAdapters();
        fetchArticles("yahoo", 0);
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

        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem miSearch = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(miSearch);

        // Search Item: Add Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Query", query);

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

        httpClient.getArticles(query, new JsonHttpResponseHandler() {

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
