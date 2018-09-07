package com.weavedin.mplayer.ui.search;

import android.util.Log;

import com.weavedin.mplayer.models.Songs;
import com.weavedin.mplayer.network.NetworkClient;
import com.weavedin.mplayer.network.NetworkInterface;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by avasar on 07,September,2018
 */
public class SearchPresenter implements SearchPresenterInterface{
    SearchViewInterface svi;
    private String TAG = "SearchPresenter";
    private String term = "";

    public SearchPresenter(SearchViewInterface svi,String s) {
        this.svi = svi;
        this.term = s;
    }

    @Override
    public void getSongs() {
        getObservable().subscribeWith(getObserver());
    }

    public Observable<Songs> getObservable() {
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .getSearchList(term,"music")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<Songs> getObserver() {
        return new DisposableObserver<Songs>() {

            @Override
            public void onNext(@NonNull Songs postsResponse) {
                svi.showSongs(postsResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                svi.displayError("Error fetching Posts Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                svi.hideProgressBar();
            }
        };
    }
}
