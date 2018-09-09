package com.weavedin.mplayer.ui.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.weavedin.mplayer.R;
import com.weavedin.mplayer.adapters.SearchAdapter;
import com.weavedin.mplayer.models.SongInfo;
import com.weavedin.mplayer.utils.CustomLinearLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by avasar on 07,September,2018
 */
public class SearchFragment extends Fragment {

    @BindView(R.id.rv_search)
    RecyclerView rv_search;
    RecyclerView.Adapter adapter;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            songInfos = (ArrayList<SongInfo>) getArguments().getSerializable("songInfos");
        }
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        setupViews();
        return view;
    }

    private void setupViews() {
        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_search.setLayoutManager(customLayoutManager);
        if (songInfos != null) {
            adapter = new SearchAdapter(songInfos, getActivity());
            rv_search.setAdapter(adapter);
        }
    }
}
