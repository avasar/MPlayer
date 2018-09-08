package com.weavedin.mplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avasar on 07,September,2018
 */
public class Songs implements Serializable{

    @SerializedName("resultCount")
    @Expose
    private Integer resultCount;
    @SerializedName("results")
    @Expose
    private List<SongInfo> results = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Songs() {
    }

    /**
     *
     * @param resultCount
     * @param results
     */
    public Songs(Integer resultCount,  List<SongInfo> results) {
        super();
        this.resultCount = resultCount;
        this.results = results;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public List<SongInfo> getResults() {
        return results;
    }

    public void setResults(List<SongInfo> results) {
        this.results = results;
    }
}
