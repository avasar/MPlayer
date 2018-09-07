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
                .into(holder.ivSearch);
        holder.tvSTitle.setText(songsList.get(position).getTrackName());
        holder.tvSBody.setText(songsList.get(position).getArtistName() + " | "+songsList.get(position).getCollectionName());
}

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class AlbumPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivSearch;
        private TextView tvSTitle, tvSBody;

        public AlbumPhotoViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ivSearch = (ImageView) v.findViewById(R.id.ivSearch);
            tvSTitle = (TextView) v.findViewById(R.id.tvSTitle);
            tvSBody = (TextView) v.findViewById(R.id.tvSBody);
        }

        @Override
        public void onClick(View view) {
           
        }
    }
}