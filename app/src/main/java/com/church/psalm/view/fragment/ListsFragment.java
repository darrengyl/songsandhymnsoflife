package com.church.psalm.view.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.church.psalm.interfaces.OnClickInterface;
import com.church.psalm.presenter.fragment.PresenterListsFragment;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.R;
import com.church.psalm.view.activity.MainActivity;
import com.church.psalm.view.activity.NewScoreActivity;
import com.church.psalm.view.adapter.RealmListviewAdapter;
import com.church.psalm.model.Song;

import com.church.psalm.view.view.ViewListFragment;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

//TODO: searchView
public class ListsFragment extends Fragment implements ViewListFragment, OnClickInterface {
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private MaterialDialog _dialog;
    @Bind(R.id.list)
    ListView listView;
    @Inject
    PresenterListsFragment presenterListsFragment;

    private RealmListviewAdapter _songsAdapter;

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
/*        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new MaterialDialog.Builder(getContext())
                        .title("Permission Needed")
                        .content("Without the permission to write data to your storage, none of song info will be stored in the app. Your user experience cannot be guaranteed")
                        .positiveText("OK")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST);
                            }
                        })
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            }
        } else {*/
            presenterListsFragment.start();
        //}
    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST)
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).setOnTabSelectedListener();
                }
                presenterListsFragment.start();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).removeMenuItem();
                }
            }
    }*/


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
        Intent intent = NewScoreActivity.getLaunchIntent(getActivity(), position);
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

    private void showSortByDialog() {
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

    public void onClickSort() {
        showSortByDialog();
    }

    public void scrollToFirst() {
        if (listView.getFirstVisiblePosition() < 6) {
            listView.smoothScrollToPosition(0);
        } else {
            listView.setSelection(6);
            listView.smoothScrollToPosition(0);
        }
    }

    public void incrementFreq(int position) {
        presenterListsFragment.incrementFreq(position);
    }
}