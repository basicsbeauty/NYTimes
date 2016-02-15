package com.enlaps.m.and.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.enlaps.m.and.nytimes.models.NewsArticle;
import com.enlaps.m.and.nytimes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vsatish on 2/12/2016.
 */
public class ArticleItemAdapter extends RecyclerView.Adapter<ArticleItemAdapter.ViewHolder> {

    // View Holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView     tvTitle;
        public ImageView    ivThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle     = (TextView)    itemView.findViewById(R.id.tvArticleTitle);
            ivThumbnail = (ImageView)   itemView.findViewById(R.id.ivArticleThumbnail);

        }
    }

    private ArrayList<NewsArticle> mListNewsArticle;


    public ArticleItemAdapter(ArrayList<NewsArticle> objects) {
        mListNewsArticle = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View article = inflater.inflate(R.layout.lv_article_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(article);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsArticle article = mListNewsArticle.get(position);

        holder.tvTitle.setText(article.title);
        Glide.with(getContext()).load(article.thumbnail_url).into(viewHolder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return mListNewsArticle.size();
    }
}
