package com.church.psalm.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Darren Gu on 10/1/2015.
 */
public class Song extends RealmObject {
    @PrimaryKey
    private int _id;
    @Index
    private int _trackNumber;
    @Required
    @Index
    private String _title;
    @Index
    private String _pinyin;
    private int _frequency;
    private boolean _downloaded;
    @Index
    private String _lyrics;
    private boolean _favorite;
    private int _firstOccurrence;
    private int _category;
    private int _subcategory;

    public Song() {

    }

    /* Only used to create dummy Song */
    public Song(String title) {
        _id = Constants.HEADER_ID;
        _title = title;
    }

    public int get_trackNumber() {
        return _trackNumber;
    }

    public void set_trackNumber(int _trackNumber) {
        this._trackNumber = _trackNumber;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public int get_frequency() {
        return _frequency;
    }

    public void set_frequency(int _frequency) {
        this._frequency = _frequency;
    }

    public boolean is_downloaded() {
        return _downloaded;
    }

    public void set_downloaded(boolean _downloaded) {
        this._downloaded = _downloaded;
    }

    public String get_lyrics() {
        return _lyrics;
    }

    public void set_lyrics(String _lyrics) {
        this._lyrics = _lyrics;
    }

    public boolean is_favorite() {
        return _favorite;
    }

    public void set_favorite(boolean _favorite) {
        this._favorite = _favorite;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_pinyin() {
        return _pinyin;
    }

    public void set_pinyin(String _pinyin) {
        this._pinyin = _pinyin;
    }

    public int get_firstOccurrence() {
        return _firstOccurrence;
    }

    public void set_firstOccurrence(int _firstOccurrence) {
        this._firstOccurrence = _firstOccurrence;
    }

    public int get_category() {
        return _category;
    }

    public void set_category(int _category) {
        this._category = _category;
    }

    public int get_subcategory() {
        return _subcategory;
    }

    public void set_subcategory(int _subcategory) {
        this._subcategory = _subcategory;
    }
}
