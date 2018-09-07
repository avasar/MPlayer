package com.weavedin.mplayer.ui.search;

import com.weavedin.mplayer.models.Songs;

import java.util.List;

/**
 * Created by avasar on 07,September,2018
 */
public interface SearchViewInterface {
    void showProgressBar();

    void hideProgressBar();

    void showToast(String s);

    void showSongs(Songs songs);

    void displayError(String s);
}
