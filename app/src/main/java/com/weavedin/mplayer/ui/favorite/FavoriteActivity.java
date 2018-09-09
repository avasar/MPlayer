package com.weavedin.mplayer.ui.favorite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weavedin.mplayer.R;
import com.weavedin.mplayer.adapters.FavoriteAdapter;
import com.weavedin.mplayer.adapters.SearchAdapter;
import com.weavedin.mplayer.database.DBManager;
import com.weavedin.mplayer.models.Favorites;
import com.weavedin.mplayer.utils.CustomLinearLayoutManager;
import com.weavedin.mplayer.utils.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.toolbar_fav)
    Toolbar toolbar_fav;
    @BindView(R.id.rv_fav)
    RecyclerView rv_fav;
    @BindView(R.id.tv_dummy_nofav)
    TextView tv_dummy_nofav;
    @BindView(R.id.tv_fav_count)
    TextView tv_fav_count;
    FavoriteAdapter adapter;
    private DBManager dbManager;
    private List<Favorites> favorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        setupViews();
    }

    private void setupViews() {
        dbManager = new DBManager(this);
        dbManager.open();
        setSupportActionBar(toolbar_fav);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(FavoriteActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_fav.setLayoutManager(layoutManager);
        setData();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_fav);


    }

    private void setData() {
        if (dbManager.geFavorites() != null && dbManager.geFavorites().size() > 0) {
            favorites = dbManager.geFavorites();
            tv_dummy_nofav.setVisibility(View.GONE);
            adapter = new FavoriteAdapter(dbManager.geFavorites(), FavoriteActivity.this);
            rv_fav.setAdapter(adapter);
            tv_fav_count.setText("Favorites - " + String.valueOf(dbManager.geFavorites().size()));
        } else {
            tv_fav_count.setVisibility(View.GONE);
            tv_dummy_nofav.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof FavoriteAdapter.FavoritesViewHolder) {
            // remove the item from recycler view
            dbManager.deleteFavorite(favorites.get(position).getTrackId());
            adapter.removeItem(position);
            setData();

        }
    }
}
