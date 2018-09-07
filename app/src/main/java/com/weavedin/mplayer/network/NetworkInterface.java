package com.weavedin.mplayer.network;

import com.weavedin.mplayer.models.Songs;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by avasar on 06,September,2018
 */
public interface NetworkInterface {

    @GET("search?")
    Observable<Songs> getSearchList(@Query("term") String term, @Query("media") String media);

}
