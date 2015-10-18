package com.church.psalm;


import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NumbersFragment extends Fragment{

	TextView display;
	Button dp1 ,dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp0;
	ImageButton backspace;
	ImageButton accept;
	StringBuilder sBuilder;
	public static NumbersFragment getInstance(int position){
		NumbersFragment numbersFragment = new NumbersFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		numbersFragment.setArguments(args);
		return numbersFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View V = inflater.inflate(R.layout.fragment_numbers, container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle bundle = getArguments();
		if (bundle != null){

		}
		sBuilder = new StringBuilder();
		display = (TextView)V.findViewById(R.id.display);
		SpannableString hint = new SpannableString(getString(R.string.enter_track_number));
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(25, true);
		hint.setSpan(ass, 0, hint.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		display.setHint(new SpannableString(hint));
		dp0 = (Button)V.findViewById(R.id.dp0);
		dp1 = (Button)V.findViewById(R.id.dp1);
		dp2 = (Button)V.findViewById(R.id.dp2);
		dp3 = (Button)V.findViewById(R.id.dp3);
		dp4 = (Button)V.findViewById(R.id.dp4);
		dp5 = (Button)V.findViewById(R.id.dp5);
		dp6 = (Button)V.findViewById(R.id.dp6);
		dp7 = (Button)V.findViewById(R.id.dp7);
		dp8 = (Button)V.findViewById(R.id.dp8);
		dp9 = (Button)V.findViewById(R.id.dp9);
		backspace = (ImageButton)V.findViewById(R.id.backspace);
		accept = (ImageButton)V.findViewById(R.id.accept);
		onClickButtonListener(dp0);
		onClickButtonListener(dp1);
		onClickButtonListener(dp2);
		onClickButtonListener(dp3);
		onClickButtonListener(dp4);
		onClickButtonListener(dp5);
		onClickButtonListener(dp6);
		onClickButtonListener(dp7);
		onClickButtonListener(dp8);
		onClickButtonListener(dp9);

		backspace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (sBuilder.length() > 0){
					sBuilder.setLength(sBuilder.length() - 1);
					display.setText(sBuilder);
				}
				
			}
		});

		accept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isNetworkConnected()){
					if (display.length() == 0){
						return;
					}
					long number = Integer.parseInt(display.getText().toString());
					if ((number > 0)&&(number < 587)){
						/*
						Intent intent = new Intent(getActivity(), pictureactivity.class);
						intent.putExtra("link", (int)number);
						startActivity(intent);
						*/
					} else {
						Toast.makeText(getActivity(), getString(R.string.long_digits_warning)
								, Toast.LENGTH_SHORT).show();
						sBuilder.setLength(0);
						display.setText(null);
					}
				} else {
					Toast.makeText(getActivity(), getString(R.string.network_error)
							, Toast.LENGTH_SHORT).show();

				}
			}
		});


		
		return V;
	}

	
	public void onClickButtonListener(final Button button){
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (sBuilder.length() < 4){
					sBuilder.append(button.getText());
					display.setText(sBuilder);
				} else {
					sBuilder.setLength(0);
					display.setText(null);
					Toast.makeText(getContext(), getString(R.string.long_digits_warning)
							, Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public boolean isNetworkConnected(){
		ConnectivityManager cm =
				(ConnectivityManager)getActivity().getSystemService(
						getActivity().CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
		return isConnected;
	}
	

	

}
