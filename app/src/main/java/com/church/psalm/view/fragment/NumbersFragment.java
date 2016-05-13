package com.church.psalm.view.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.presenter.fragment.PresenterNumbersFragment;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.view.activity.MainActivity;
import com.church.psalm.view.activity.NewScoreActivity;
import com.church.psalm.view.view.ViewNumberFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NumbersFragment extends Fragment implements ViewNumberFragment {
    //TODO: when user types track number, show title in real-time.
    //TODO: create different layout in landscape orientation
    @Inject
    PresenterNumbersFragment presenterNumbersFragment;
    @Bind(R.id.display)
    TextView display;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Songsandhymnsoflife) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numbers, container, false);
        ButterKnife.bind(this, view);

        SpannableString hint = new SpannableString(getString(R.string.enter_track_number));
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20, true);
        hint.setSpan(ass, 0, hint.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        display.setHint(new SpannableString(hint));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenterNumbersFragment.setView(this);
        presenterNumbersFragment.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenterNumbersFragment.setView(null);
    }

    @OnClick({R.id.dp0, R.id.dp1, R.id.dp2, R.id.dp3, R.id.dp4, R.id.dp5
            , R.id.dp6, R.id.dp7, R.id.dp8, R.id.dp9})
    public void onClickOnNumbers(View view) {
        CharSequence num = ((TextView) view).getText();
        presenterNumbersFragment.numberClicked(num);
    }

    @OnClick(R.id.backspace)
    public void onClickBackspace() {
        presenterNumbersFragment.backspaceClicked();
    }

    @OnClick(R.id.accept)
    public void onClickAccept() {
        presenterNumbersFragment.acceptClicked();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showNumberError() {
        Snackbar.make(getActivity().findViewById(android.R.id.content)
                , getString(R.string.long_digits_warning)
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.clear, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        presenterNumbersFragment.clearDisplay();
                    }
                })
                .show();
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(getActivity().findViewById(android.R.id.content)
                , getString(R.string.network_error)
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.open_settings, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    @Override
    public void incrementFreq(int position) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            ((MainActivity) getActivity()).incrementFreq(position);
        }
    }

    @Override
    public void updateDisplay(String s) {
        display.setText(s);
    }

    @Override
    public void startScoreActivity(int trackNumber) {
        Intent intent = NewScoreActivity.getLaunchIntent(getActivity(), trackNumber);
        startActivity(intent);
    }
}
