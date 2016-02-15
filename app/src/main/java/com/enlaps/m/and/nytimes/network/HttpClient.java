package com.enlaps.m.and.nytimes.network;

import android.util.Log;

import com.enlaps.m.and.nytimes.models.NewsArticle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Atom on 2/13/16.
 */
public class HttpClient {

    private static final String API_KEY  = "8d4590c0e3f2d932247b97d3c1a612b2:9:70701775";
    private static final String URL_BASE = "http://www.nytimes.com/";
    private static final String URL_API = "http://api.nytimes.com/svc/search/v2/";
    private static final String URL_SEARCH = URL_API + "articlesearch.json?";

    // URL parameter tags
    private static final String URL_PARAM_TAG_QUERY = "q";
    private static final String URL_PARAM_TAG_API_KEY = "api-key";
    private static final String URL_PARAM_TAG_PAGE_NUMBER = "page";
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

    private String buildString( String searchString, int pageNumber) {

        // Sample URL
        // http://api.nytimes.com/svc/search/v2/articlesearch.json?q=hitachi&api-key=8d4590c0e3f2d932247b97d3c1a612b2:9:70701775
        try {
            return  URL_SEARCH
                    + URL_PARAM_TAG_QUERY + "=" + URLEncoder.encode(searchString, "utf-8") + "&"
                    + URL_PARAM_TAG_API_KEY + "=" + API_KEY + "&"
                    + URL_PARAM_TAG_PAGE_NUMBER + "=" + Integer.toString(pageNumber);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return URL_SEARCH + "?" + URL_PARAM_TAG_API_KEY + "&" + API_KEY;
        }
    }

    public void getArticles(String searchString, int pageNumber, JsonHttpResponseHandler handler) {
        String url =  buildString(searchString, pageNumber);
        Log.i("URL", url);
        client.get(url, handler);
    }

    public ArrayList<NewsArticle> processGet(JSONObject jsonResponse) {

        final String JSON_TAG_DOCS = "docs";
        final String JSON_TAG_RESPONSE = "response";

        JSONObject  response;
        JSONArray   jarrayDocs;
        try {
            //Log.i("Response", jsonResponse.toString());
            if( false == jsonResponse.has(JSON_TAG_RESPONSE)) {
                Log.e("Response JSON Error", "No element: " + JSON_TAG_RESPONSE);
                return new ArrayList<>();
            }

            response = jsonResponse.getJSONObject(JSON_TAG_RESPONSE);

            if( false == response.has(JSON_TAG_DOCS)) {
                Log.e("Response JSON Error", "No element: " + JSON_TAG_DOCS);
                return new ArrayList<>();
            }

            jarrayDocs = response.getJSONArray(JSON_TAG_DOCS);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return jsonArray2Articles(jarrayDocs);
    }

    public ArrayList<NewsArticle> jsonArray2Articles(JSONArray jarray) {

        NewsArticle temp;
        ArrayList<NewsArticle> rvalue = new ArrayList<>();
        int count = jarray.length();
        for (int i=0; i < count; i++) {

            try {
                if( null != (temp = json2Article(jarray.getJSONObject(i)))) {
                    rvalue.add(temp);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                return rvalue;
            }
        }

        Log.i("Article Count", Integer.toString(rvalue.size()));
        return rvalue;
    }

    public NewsArticle json2Article(JSONObject object) {

        final String JSON_TAG_URL = "url";
        final String JSON_TAG_WEB_URL = "web_url";
        final String JSON_TAG_SNIPPET = "snippet";

        final String JSON_TAG_MEDIA = "multimedia";
        final String JSON_TAG_MEDIA_TYPE = "type";
        final String JSON_TAG_MEDIA_SUBTYPE = "subtype";

        final String JSON_VALUE_MEDIA_TYPE_IMAGE = "image";
        final String JSON_VALUE_MEDIA_SUBTYPE_THUMBNAIL = "thumbnail";

        NewsArticle rarticle = new NewsArticle();

        try {

            if( (false == object.has(JSON_TAG_WEB_URL))
             || (false == object.has(JSON_TAG_SNIPPET))
             || (false == object.has(JSON_TAG_MEDIA))) {
                return null;
            }

            rarticle.url = object.get(JSON_TAG_WEB_URL).toString();
            rarticle.title = object.get(JSON_TAG_SNIPPET).toString();

            JSONArray jarrayMedia = object.getJSONArray(JSON_TAG_MEDIA);

            int count = jarrayMedia.length();
            Log.i("Media Count", Integer.toString(count));
            Log.i("Media Response", jarrayMedia.toString());
            for( int i=0; i<count; i++) {
                JSONObject jmedia = jarrayMedia.getJSONObject(i);


                if( ("image".equals(jmedia.getString(JSON_TAG_MEDIA_TYPE)))
                 && ("wide".equals(jmedia.getString(JSON_TAG_MEDIA_SUBTYPE))) ) {
                    rarticle.thumbnail_url = URL_BASE + jmedia.get(JSON_TAG_URL);
                    Log.i("Thumbnail", rarticle.thumbnail_url);
                    break;  // Break after the first Thumbnail
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return rarticle;
    }
}
