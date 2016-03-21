package com.church.psalm.view.fragment;


import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.church.psalm.DbAdapter;
import com.church.psalm.interfaces.OnClickInterface;
import com.church.psalm.presenter.Presenter;
import com.church.psalm.presenter.fragment.PresenterListsFragment;
import com.church.psalm.songsandhymnsoflife;
import com.church.psalm.view.DividerItemDecoration;
import com.church.psalm.R;
import com.church.psalm.view.activity.ScoreActivity;
import com.church.psalm.view.adapter.RecyclerViewAdapter;
import com.church.psalm.model.Song;
import com.church.psalm.sortListener;
import com.church.psalm.view.view.ViewListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

//TODO: searchView
public class ListsFragment extends Fragment implements sortListener, ViewListFragment, OnClickInterface {
    DbAdapter dbAdapter;
    private RecyclerViewAdapter _listAdapter;
    private MaterialDialog _dialog;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Inject
    PresenterListsFragment presenterListsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((songsandhymnsoflife) getActivity().getApplication()).getComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        ButterKnife.bind(this, view);
        recyclerview = new RecyclerView(getActivity());
        _listAdapter = new RecyclerViewAdapter(getActivity(), this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenterListsFragment.setView(this);
        presenterListsFragment.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenterListsFragment.setView(null);
    }

    @OnItemClick(R.id.recyclerview)
    public void onItemClick(int position) {
        presenterListsFragment.onItemClick(position);
    }

    @Override
    public void showProgressDialog() {
        _dialog = new MaterialDialog.Builder(getActivity())
                .cancelable(false)
                .title("Loading data")
                .content("Please wait")
                .progress(true, 0)
                .show();
    }

    @Override
    public void dismissProgressDialog() {
        if (_dialog != null && _dialog.isShowing()) {
            _dialog.dismiss();
        }
    }

    @Override
    public void refreshListData(List<Song> songs) {
        _listAdapter.setData(songs);
    }

    @Override
    public void startScoreActivity(int position) {
        Intent intent = ScoreActivity.getLaunchIntent(getActivity(), position);
        startActivity(intent);
    }

    @Override
    public void setFavStar(int position, int value) {
        _listAdapter.setFavStar(position, value);
    }

    @Override
    public void showErrorSnackbar() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content)
                , getResources().getString(R.string.network_error)
                , Snackbar.LENGTH_LONG);
        snackbar.setAction("Settings", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        snackbar.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


    }

    @Override
    public void onSortByFav() {
/*        ArrayList<Song> result = new ArrayList<Song>();
        result = dbAdapter.getMostFavSongs();
        if (result.size() > 0) {
            adapter.setData(result);

        }*/
    }

    @Override
    public void onSortByFreq() {
/*        ArrayList<Song> result = new ArrayList<Song>();
        result = dbAdapter.getMostPlayedSongs();
        if (result.size() > 0) {
            adapter.setData(result);

        }*/
    }

    @Override
    public void onSortByTrack() {
/*        ArrayList<Song> result = new ArrayList<Song>();
        result = dbAdapter.getAllSongs();
        if (result.size() > 0) {
            adapter.setData(result);

        }*/
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClicked(int position) {
        presenterListsFragment.onFavStarClicked(position);
    }
}

