package com.church.psalm.view.fragment;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.presenter.fragment.PresenterNumbersFragment;
import com.church.psalm.songsandhymnsoflife;
import com.church.psalm.view.activity.ScoreActivity;
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
    @Bind(R.id.dp1)
    Button dp1;
    @Bind(R.id.dp2)
    Button dp2;
    @Bind(R.id.dp3)
    Button dp3;
    @Bind(R.id.dp4)
    Button dp4;
    @Bind(R.id.dp5)
    Button dp5;
    @Bind(R.id.dp6)
    Button dp6;
    @Bind(R.id.dp7)
    Button dp7;
    @Bind(R.id.dp8)
    Button dp8;
    @Bind(R.id.dp9)
    Button dp9;
    @Bind(R.id.backspace)
    ImageButton backspace;
    @Bind(R.id.dp0)
    Button dp0;
    @Bind(R.id.accept)
    ImageButton accept;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((songsandhymnsoflife) getActivity().getApplication()).getComponent().inject(this);

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

    @OnClick({R.id.dp0, R.id.dp1, R.id.dp2, R.id.dp3, R.id.dp4, R.id.dp5
            , R.id.dp6, R.id.dp7, R.id.dp8, R.id.dp9})
    public void onClickOnNumbers(View view) {
        CharSequence num = ((Button) view).getText();
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
    public void updateDisplay(String s) {
        display.setText(s);
    }

    @Override
    public void startScoreActivity(int trackNumber) {
        Intent intent = ScoreActivity.getLaunchIntent(getActivity(), trackNumber);
        startActivity(intent);
    }
}
