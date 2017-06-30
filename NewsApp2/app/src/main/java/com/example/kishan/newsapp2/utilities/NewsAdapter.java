package com.example.kishan.newsapp2.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kishan.newsapp2.R;
import com.example.kishan.newsapp2.model.NewsItem;

import java.util.ArrayList;

/**
 * Created by Kishan on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder>  {

    private static final String TAG = NewsAdapter.class.getSimpleName();

    private ArrayList<NewsItem> data;

    final private ItemClickListener listener;

    public NewsAdapter(ArrayList<NewsItem> data, ItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, attachToParent);

        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView NewsTitle;
        public final TextView NewsDescription;
        public final TextView NewsTime;

        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            NewsTitle = (TextView) itemView.findViewById(R.id.Title);
            NewsDescription = (TextView) itemView.findViewById(R.id.News_Description);
            NewsTime = (TextView) itemView.findViewById(R.id.News_Time);
            itemView.setOnClickListener(this);
        }

        public void bind(int pos) {
            NewsItem newsItem = data.get(pos);
            NewsTitle.setText(newsItem.getTitle());
            NewsDescription.setText(newsItem.getDescription());
            NewsTime.setText(newsItem.getPublishedAt());
        }

        @Override
        public void onClick(View v) {
            listener.onListItemClick(getAdapterPosition());
        }
    }

    public void setData(ArrayList<NewsItem> data) {
        this.data = data;
    }
}