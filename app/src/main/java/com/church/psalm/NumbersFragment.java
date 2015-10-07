package com.church.psalm;



import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	ImageButton discard;
	ImageButton accept;
	StringBuilder sBuffer;
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
		Bundle bundle = getArguments();
		if (bundle != null){

		}
		sBuffer = new StringBuilder();
		display = (TextView)V.findViewById(R.id.display);
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
				if (sBuffer.length() > 0){
					sBuffer.setLength(sBuffer.length() - 1);
					display.setText(sBuffer);
				}
				
			}
		});

		accept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConnectivityManager cm =
				        (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
				
				NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
				boolean isConnected = activeNetwork != null &&
				                      activeNetwork.isConnectedOrConnecting();
				if (isConnected){
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
						Toast.makeText(getActivity(), "Please insert a number between 1 and 586", Toast.LENGTH_SHORT).show();
						sBuffer.setLength(0);
						display.setText(null);
					}
				} else {
					Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();;

				}
			}
		});


		
		return V;
	}
	
	public void onClickButtonListener(final Button button){
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sBuffer.append(button.getText());
				display.setText(sBuffer);
			}
		});

	}
	

	

}
