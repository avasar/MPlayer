package com.weavedin.mplayer.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.weavedin.mplayer.models.SongInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avasar on 07,September,2018
 */
public class SearchFragmentPageAdapter extends FragmentStatePagerAdapter {

    private List<SongInfo> songInfos;
    private Context context;
    private int resultCount, fragmentCount, rowCount;

    public SearchFragmentPageAdapter(Context c, FragmentManager fragmentManager, List<SongInfo> songInfos, int resultCount,
                                     int fragmentCount, int rowCount) {
        super(fragmentManager);
        this.resultCount = resultCount;
        this.fragmentCount = fragmentCount;
        this.songInfos = songInfos;
        this.rowCount = rowCount;
        this.context = c;
    }

    @Override
    public Fragment getItem(int position) {

        ArrayList<SongInfo> tempSongList = new ArrayList<>();

        int start = rowCount * position;
        int end;
        if(position + 1 == fragmentCount){
            end = resultCount;
        }else {
            end = rowCount * (position + 1);
        }
        System.out.println("start:"+start+"end:"+end);


        for (int i = start; i < end; i++){
            tempSongList.add(songInfos.get(i));
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("songInfos", tempSongList);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        return searchFragment;

    }

    @Override
    public int getCount() {
        return fragmentCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return songInfos.get(position).getTrackName();
    }

}