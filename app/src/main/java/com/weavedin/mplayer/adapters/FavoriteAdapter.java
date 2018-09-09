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
import com.weavedin.mplayer.models.Favorites;
import com.weavedin.mplayer.ui.player.PlayerActivity;

import java.util.List;

/**
 * Created by avasar on 09,September,2018
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoritesViewHolder> {

    private List<Favorites> favorites;
    private Context context;
    private int row_index = -1;

    public FavoriteAdapter(List<Favorites> favorites, Context context) {
        this.favorites = favorites;
        this.context = context;
    }

    @Override
    public FavoriteAdapter.FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_favorite, parent, false);
        FavoriteAdapter.FavoritesViewHolder fvh = new FavoriteAdapter.FavoritesViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.FavoritesViewHolder holder, final int position) {

        Glide.with(context)
                .load(favorites.get(position).getImageUrl())
                .into(holder.iv_fav_row);
        holder.tv_fav_title.setText(favorites.get(position).getTrackName());
        holder.tv_fav_body.setText(favorites.get(position).getArtistName() + " | " + favorites.get(position).getCollectionName());
        holder.cv_row_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("TRACK_ID", favorites.get(position).getTrackId());
                intent.putExtra("IMAGE_URL", favorites.get(position).getImageUrl());
                intent.putExtra("PREVIEW_URL", favorites.get(position).getPreviewUrl());
                intent.putExtra("TRACK_TITLE", favorites.get(position).getTrackName());
                intent.putExtra("ARTIST_TITLE", favorites.get(position).getArtistName());
                intent.putExtra("COLLECTION_TITLE", favorites.get(position).getCollectionName());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        });

        if (row_index == position) {
            holder.cv_row_fav.setBackgroundColor(context.getResources().getColor(R.color.colorHighlight));
        } else {
            holder.cv_row_fav.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_fav_row;
        private TextView tv_fav_title, tv_fav_body;
        public CardView cv_row_fav;

        public FavoritesViewHolder(View v) {
            super(v);
            iv_fav_row = (ImageView) v.findViewById(R.id.iv_fav_row);
            tv_fav_title = (TextView) v.findViewById(R.id.tv_fav_title);
            tv_fav_body = (TextView) v.findViewById(R.id.tv_fav_body);
            cv_row_fav = (CardView) v.findViewById(R.id.cv_row_fav);
        }
    }

    public void removeItem(int position) {
        System.out.println("position:"+position);
        favorites.remove(position);
        row_index = -1;
        notifyItemRemoved(position);

    }
}