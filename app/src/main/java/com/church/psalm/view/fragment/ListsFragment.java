package com.church.psalm.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.church.psalm.interfaces.OnClickInterface;
import com.church.psalm.presenter.fragment.PresenterListsFragment;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.R;
import com.church.psalm.view.activity.ScoreActivity;
import com.church.psalm.view.adapter.RealmListviewAdapter;
import com.church.psalm.model.Song;

import com.church.psalm.view.view.ViewListFragment;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class ListsFragment extends Fragment implements ViewListFragment, OnClickInterface {
    @Bind(R.id.list)
    ListView listView;
    @Inject
    PresenterListsFragment presenterListsFragment;
    private RealmListviewAdapter _songsAdapter;
    private MaterialDialog _dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Songsandhymnsoflife) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        ButterKnife.bind(this, view);
        _songsAdapter = new RealmListviewAdapter(getContext(), null);
        _songsAdapter.setListener(this);
        listView.setAdapter(_songsAdapter);
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
    public void refreshListData(RealmResults<Song> songs) {
        _songsAdapter.updateRealmResults(songs);
    }

    @Override
    public void refreshAdapter() {
        _songsAdapter.notifyDataSetChanged();
    }

    @Override
    public void startScoreActivity(int position) {
        Intent intent = ScoreActivity.getLaunchIntent(getActivity(), position);
        startActivity(intent);
    }

    @Override
    public void showErrorSnackbar() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content)
                , getResources().getString(R.string.network_error)
                , Snackbar.LENGTH_LONG);
        snackbar.setAction("Settings", v -> {
            getActivity().startActivity(new Intent(Settings.ACTION_SETTINGS));
        });
        snackbar.show();
    }

    public void showSortByDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("Sort by")
                .items(R.array.sort_by_list)
                .itemsCallbackSingleChoice(presenterListsFragment.getCurrentOrder(),
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                presenterListsFragment.sortBy(which);
                                Log.d("Sort by dialog", "Chose " + which);
                                return true;
                            }
                        })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClickedFav(int position) {
        presenterListsFragment.onFavStarClicked(position);
    }

    @Override
    public void onClickedItem(int position) {
        presenterListsFragment.onItemClick(position);
    }

    @Override
    public void enableFastScroll(boolean enable) {
        listView.setFastScrollEnabled(enable);
    }

    @Override
    public void setSectionIndexData(HashMap<Character, Integer> map, Character[] sections) {
        _songsAdapter.setSectionData(map, sections);
    }

    @Override
    public void showInfoSnackbar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content)
                , message
                , Snackbar.LENGTH_SHORT)
                .show();
    }

    public void scrollToFirst() {
        if (listView.getFirstVisiblePosition() < 6) {
            listView.smoothScrollToPosition(0);
        } else {
            listView.setSelection(6);
            listView.smoothScrollToPosition(0);
        }
    }
}