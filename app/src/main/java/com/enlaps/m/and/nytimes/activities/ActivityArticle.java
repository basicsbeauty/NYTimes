package com.enlaps.m.and.nytimes.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.enlaps.m.and.nytimes.R;
import com.enlaps.m.and.nytimes.models.NewsArticle;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityArticle extends AppCompatActivity {

    @Bind(R.id.wvArticle)
    WebView vwArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    protected void init() {
        ButterKnife.bind(this);

        NewsArticle article = (NewsArticle) getIntent().getParcelableExtra("article");

        vwArticle.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        vwArticle.loadUrl(article.url);
    }

}
