package com.church.psalm.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.Util;
import com.church.psalm.interfaces.OnClickSongInterface;
import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.model.SongMatch;
import com.church.psalm.presenter.activity.PresenterSearchActivity;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.view.adapter.SearchAdapter;
import com.church.psalm.view.view.ViewSearchActivity;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by darrengu on 4/2/16.
 */
public class SearchActivity extends AppCompatActivity implements ViewSearchActivity, OnClickSongInterface {

    @Bind(R.id.search_bar)
    Toolbar toolbar;
    @Bind(R.id.search)
    SearchView searchView;
    @Bind(R.id.search_result)
    ListView listView;
    @Bind(R.id.banner)
    TextView banner;
    @Bind(R.id.empty_view)
    RelativeLayout emptyView;
    @Inject
    PresenterSearchActivity presenter;
    private ImageView _close;
    private Drawable _closeIconDrawable;
    private EditText _searchViewEditText;
    private SearchAdapter _adapter;
    private Realm _realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        ((Songsandhymnsoflife) getApplication()).getComponent().inject(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setUpSearchview();
        banner.setText(String.format(getString(R.string.filter_result_text), Constants.SEARCH_FILTER_NUMBER));
        _realm = Realm.getDefaultInstance();
        _adapter = new SearchAdapter(this);
        _adapter.setOnClickListener(this);
        listView.setAdapter(_adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.setView(null);
        //_suscription.unsubscribe();
    }

    private void setUpSearchview() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        _searchViewEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id
                .search_src_text);
        _searchViewEditText.setHint(getString(R.string.search_hint));
        searchView.findViewById(android.support.v7.appcompat.R.id.search_plate)
                .setBackgroundColor(Color.TRANSPARENT);
        ((ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon))
                .setImageDrawable(null);

        _close = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        _closeIconDrawable = _close.getDrawable();
        _close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClickDelete();
            }
        });
        enableDeleteButton(false);
        searchView.setIconifiedByDefault(false);
        RxTextView.textChangeEvents(_searchViewEditText)
                .debounce(400, TimeUnit.MICROSECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TextViewTextChangeEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        presenter.onTextChanged(textViewTextChangeEvent.text().toString());
                    }
                });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _searchViewEditText.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void clearQuery() {
        _searchViewEditText.setText("");
    }

    public void enableDeleteButton(boolean enable) {
        if (enable) {
            _close.setImageDrawable(_closeIconDrawable);
        } else {
            _close.setImageDrawable(null);
        }
        _close.setEnabled(enable);
    }

    @Override
    public void updateAdapter(List<SongMatch> results) {
        _adapter.setData(results);
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showLimitBanner() {
        banner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLimitBanner() {
        banner.setVisibility(View.GONE);
    }

    private void incrementFreq(int position) {
        _realm.beginTransaction();
        Song song = _adapter.getItem(position).getSong();
        song.set_frequency(song.get_frequency() + 1);
        _realm.commitTransaction();
    }

    @Override
    public void onClickedItem(int position) {
        if (Util.isNetworkConnected()) {
            incrementFreq(position);
            int trackNumber = _adapter.getItem(position).getSong().get_trackNumber();
            Intent intent = ScoreActivity.getLaunchIntent(this, trackNumber);
            startActivity(intent);
            finish();
        } else {
            Snackbar.make(toolbar
                    , getString(R.string.network_error)
                    , Snackbar.LENGTH_LONG)
                    .setAction(R.string.open_settings, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    })
                    .show();
        }
    }
}
