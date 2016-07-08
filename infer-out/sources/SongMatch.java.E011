package com.church.psalm.model;

/**
 * Created by darrengu on 5/10/16.
 */
public class SongMatch implements Comparable<SongMatch>{
    private String keyword;
    private int start;
    private int end;
    private String partialLyrics;
    private Song song;
    private boolean inTitle;

    public SongMatch() {}

    public SongMatch(Song song, String keyword, int start, String partialLyrics) {
        this.song = song;
        this.keyword = keyword;
        this.start = start;
        this.partialLyrics = partialLyrics;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getPartialLyrics() {
        return partialLyrics;
    }

    public void setPartialLyrics(String partialLyrics) {
        this.partialLyrics = partialLyrics;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Override
    public int compareTo(SongMatch another) {
        return song.get_trackNumber() - another.getSong().get_trackNumber();
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isInTitle() {
        return inTitle;
    }

    public void setInTitle(boolean inTitle) {
        this.inTitle = inTitle;
    }
}
