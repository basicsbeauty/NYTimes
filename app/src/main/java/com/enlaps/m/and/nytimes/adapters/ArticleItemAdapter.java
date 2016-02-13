package com.enlaps.m.and.nytimes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enlaps.m.and.nytimes.models.NewsArticle;
import com.enlaps.m.and.nytimes.R;

import java.util.ArrayList;

/**
 * Created by vsatish on 2/12/2016.
 */
public class ArticleItemAdapter extends ArrayAdapter<NewsArticle> {

    // View Holder
    private static class ViewHolder {
        public TextView     tvTitle;
        public ImageView    ivThumbnail;
    }

    public ArticleItemAdapter(Context context, ArrayList<NewsArticle> objects) {
        super( context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NewsArticle article = getPosition(position);

        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_article_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvArticleTitle);
            viewHolder.ivThumbnail = (ImageView) convertView.findViewById(R.id.ivArticleThumbnail);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

    }
}
