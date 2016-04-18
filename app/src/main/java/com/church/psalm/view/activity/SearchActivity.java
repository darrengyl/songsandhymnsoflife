package com.church.psalm.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.church.psalm.R;
import com.church.psalm.model.Song;
import com.church.psalm.presenter.activity.PresenterSearchActivity;
import com.church.psalm.songsandhymnsoflife;
import com.church.psalm.view.adapter.RealmSearchAdapter;
import com.church.psalm.view.view.ViewSearchActivity;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by darrengu on 4/2/16.
 */
public class SearchActivity extends AppCompatActivity implements ViewSearchActivity {

    @Bind(R.id.search_bar)
    Toolbar toolbar;
    @Bind(R.id.search)
    SearchView searchView;
    @Bind(R.id.search_result)
    ListView listView;
    @Inject
    PresenterSearchActivity presenter;
    private ImageView _close;
    private Drawable _closeIconDrawable;
    private EditText _searchViewEditText;
    private RealmSearchAdapter _adapter;
    private Subscription _suscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        ((songsandhymnsoflife) getApplication()).getComponent().inject(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setUpSearchview();
        _adapter = new RealmSearchAdapter(this, null, true);
        listView.setAdapter(_adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
        presenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.setView(null);
        presenter.stop();
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
        _suscription = RxTextView.textChangeEvents(_searchViewEditText)
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
    public void updateAdapter(RealmResults<Song> results) {
        _adapter.updateRealmResults(results);
    }

    @Override
    public void showEmptyView() {
        _adapter.updateRealmResults(null);
    }

    private void submitSearch(String keyword) {
        presenter.search(keyword);
    }
}
