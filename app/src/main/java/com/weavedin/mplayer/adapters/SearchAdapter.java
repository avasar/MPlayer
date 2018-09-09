package com.weavedin.mplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weavedin.mplayer.R;
import com.weavedin.mplayer.models.SongInfo;
import com.weavedin.mplayer.ui.player.PlayerActivity;

import java.util.List;

/**
 * Created by avasar on 07,September,2018
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<SongInfo> songsList;
    private Context context;
    private int row_index = -1;

    public SearchAdapter(List<SongInfo> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_search, parent, false);
        SearchAdapter.SearchViewHolder aph = new SearchAdapter.SearchViewHolder(v);
        return aph;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchViewHolder holder, final int position) {

        Glide.with(context)
                .load(songsList.get(position).getArtworkUrl100())
                .into(holder.iv_search_row);
        holder.tv_search_title.setText(songsList.get(position).getTrackName());
        holder.tv_search_body.setText(songsList.get(position).getArtistName() + " | " + songsList.get(position).getCollectionName());
        holder.cv_row_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("TRACK_ID", songsList.get(position).getTrackId());
                intent.putExtra("IMAGE_URL", songsList.get(position).getArtworkUrl100());
                intent.putExtra("PREVIEW_URL", songsList.get(position).getPreviewUrl());
                intent.putExtra("TRACK_TITLE", songsList.get(position).getTrackName());
                intent.putExtra("ARTIST_TITLE", songsList.get(position).getArtistName());
                intent.putExtra("COLLECTION_TITLE", songsList.get(position).getCollectionName());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        });

        if (row_index == position) {
            holder.cv_row_search.setBackgroundColor(context.getResources().getColor(R.color.colorHighlight));
        } else {
            holder.cv_row_search.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_search_row;
        private TextView tv_search_title, tv_search_body;
        private CardView cv_row_search;

        public SearchViewHolder(View v) {
            super(v);
            iv_search_row = (ImageView) v.findViewById(R.id.iv_search_row);
            tv_search_title = (TextView) v.findViewById(R.id.tv_search_title);
            tv_search_body = (TextView) v.findViewById(R.id.tv_search_body);
            cv_row_search = (CardView) v.findViewById(R.id.cv_row_search);
        }
    }
}