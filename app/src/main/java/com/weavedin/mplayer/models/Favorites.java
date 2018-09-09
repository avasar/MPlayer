package com.weavedin.mplayer.models;

/**
 * Created by avasar on 09,September,2018
 */
public class Favorites {

    private Integer trackId;
    private String trackName;
    private String artistName;
    private String collectionName;
    private String imageUrl;
    private String previewUrl;

    /**
     * @param trackId
     * @param trackName
     * @param artistName
     * @param collectionName
     * @param imageUrl
     * @param previewUrl
     */
    public Favorites(Integer trackId, String trackName, String artistName, String collectionName, String imageUrl, String previewUrl) {
        super();
        this.trackId = trackId;
        this.trackName = trackName;
        this.collectionName = collectionName;
        this.artistName = artistName;
        this.collectionName = collectionName;
        this.imageUrl = imageUrl;
        this.previewUrl = previewUrl;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

}
