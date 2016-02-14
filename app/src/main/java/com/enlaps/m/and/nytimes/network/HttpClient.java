package com.enlaps.m.and.nytimes.network;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Atom on 2/13/16.
 */
public class HttpClient {

    private static final String API_KEY  = "8d4590c0e3f2d932247b97d3c1a612b2:9:70701775";
    private static final String URL_BASE = "http://api.nytimes.com/svc/search/v2/";
    private static final String URL_SEARCH = URL_BASE + "http://api.nytimes.com/svc/search/v2/articlesearch.json?";

    // URL parameter tags
    private static final String URL_PARAM_TAG_QUERY = "q";
    private static final String URL_PARAM_TAG_API_KEY = "api-key";
    private static final String URL_PARAM_TAG_API_SORT = "sort";
    private static final String URL_PARAM_TAG_API_END_DATE = "end_date";
    private static final String URL_PARAM_TAG_API_BEGIN_DATE = "begin_date";

    // Preferences
    public int dateEnd;
    public int dateStart;

    // URL parameter values
    private AsyncHttpClient client;

    public HttpClient() {
        this.client = new AsyncHttpClient();
    }

    private String buildString( String searchString) {

        // Sample URL
        // http://api.nytimes.com/svc/search/v2/articlesearch.json?q=hitachi&api-key=8d4590c0e3f2d932247b97d3c1a612b2:9:70701775
        try {
            return URL_SEARCH + "?" + URLEncoder.encode(searchString, "utf-8") + "&" + URL_PARAM_TAG_API_KEY + "&" + API_KEY;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return URL_SEARCH + "?" + URL_PARAM_TAG_API_KEY + "&" + API_KEY;
        }
    }

    public void getArticles(String searchString, JsonHttpResponseHandler handler) {
            client.get( buildString(searchString), handler);
    }
}
