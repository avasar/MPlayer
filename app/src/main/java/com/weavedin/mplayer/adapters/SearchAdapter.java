package com.weavedin.mplayer.adapters;

import android.content.Context;
import android.content.Intent;
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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.AlbumPhotoViewHolder> {

    private List<SongInfo> songsList;
    private Context context;

    public SearchAdapter(List<SongInfo> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public SearchAdapter.AlbumPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_search, parent, false);
        SearchAdapter.AlbumPhotoViewHolder aph = new SearchAdapter.AlbumPhotoViewHolder(v);
        return aph;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.AlbumPhotoViewHolder holder, int position) {

        Glide.with(context)
                .load(songsList.get(position).getArtworkUrl100())
                .into(holder.iv_search_row);
        holder.tv_search_title.setText(songsList.get(position).getTrackName());
        holder.tv_search_body.setText(songsList.get(position).getArtistName() + " | "+songsList.get(position).getCollectionName());
}

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class AlbumPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_search_row;
        private TextView tv_search_title, tv_search_body;

        public AlbumPhotoViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            iv_search_row = (ImageView) v.findViewById(R.id.iv_search_row);
            tv_search_title = (TextView) v.findViewById(R.id.tv_search_title);
            tv_search_body = (TextView) v.findViewById(R.id.tv_search_body);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("IMAGE_URL", songsList.get(getAdapterPosition()).getArtworkUrl100());
            intent.putExtra("PREVIEW_URL", songsList.get(getAdapterPosition()).getPreviewUrl());
            intent.putExtra("TRACK_ID", songsList.get(getAdapterPosition()).getTrackId());
            intent.putExtra("TRACK_TITLE", songsList.get(getAdapterPosition()).getTrackName());
            intent.putExtra("ARTIST_TITLE", songsList.get(getAdapterPosition()).getArtistName());
            intent.putExtra("COLLECTION_TITLE", songsList.get(getAdapterPosition()).getCollectionName());
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(intent);
        }
    }
}