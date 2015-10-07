package com.church.psalm;

/**
 * Created by Darren Gu on 10/1/2015.
 */
public class Song {
    //private int _id;
    private int trackNumber;
    private String title;
    private int frequency;
    private String downloaded;
    private String lyrics;
    private int favorite;

    public Song(int trackNumber, String title, int frequency, String downloaded, String lyrics,
                int favorite){
        this.trackNumber = trackNumber;
        this.title = title;
        this.frequency = frequency;
        this.downloaded = downloaded;
        this.lyrics = lyrics;
        this.favorite = favorite;

    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getTitle() {
        return title;
    }

    /*public void setTitle(String title) {
        this.title = title;
    }*/

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getFavorite() {
        return favorite;
    }
}
