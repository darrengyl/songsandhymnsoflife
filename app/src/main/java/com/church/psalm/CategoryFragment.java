/*
package com.church.psalm;



import android.widget.ExpandableListAdapter;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryFragment extends Fragment implements OnChildClickListener{
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	
	List<String> PraiseandWorship;
	List<String> FulnessoftheSpirit;
	List<String> TheChurch;
	List<String> ExperiencesontheJourneyofLife;
	List<String> PracticesoftheChristianlife;
	List<String> TheGoodNewsofJesusChrist;
	List<String> Serviceandministering;
	List<String> TriumphantinChrist;
	List<String> ExpectingtheLordsReturn;
	List<String> Choruses;
	List<String> Scripture;
	List<String> Addenda;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View V = inflater.inflate(R.layout.fragment_category, container, false);
		expListView = (ExpandableListView)V.findViewById(R.id.lvExp);
		prepareListData();
		listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
		expListView.setAdapter(listAdapter);
		ConnectivityManager cm =
		        (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
		expListView.setOnChildClickListener(this);
		

		return V;
	}
	
	private void prepareListData(){
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		
		listDataHeader.add(" (Praise and Worship)");
		listDataHeader.add(" (Fulness of the Spirit)");
		listDataHeader.add("(The Church)");
		listDataHeader.add("(Experiences on the Journey of Life)");
		listDataHeader.add("(Practices of the Christian life)");
		listDataHeader.add("(The Good News of Jesus Christ)");
		listDataHeader.add("(Service and ministering)");
		listDataHeader.add("(Triumphant in Christ)");
		listDataHeader.add("(Expecting the Lords Return)");
		listDataHeader.add("(Choruses)");
		listDataHeader.add("(Scripture)");
		listDataHeader.add("(Addenda)");
		
		PraiseandWorship = new ArrayList<String>();
		FulnessoftheSpirit = new ArrayList<String>();
		TheChurch = new ArrayList<String>();
		ExperiencesontheJourneyofLife = new ArrayList<String>();
		PracticesoftheChristianlife = new ArrayList<String>();
		TheGoodNewsofJesusChrist = new ArrayList<String>();
		Serviceandministering = new ArrayList<String>();
		TriumphantinChrist = new ArrayList<String>();
		ExpectingtheLordsReturn = new ArrayList<String>();
		Choruses = new ArrayList<String>();
		Scripture = new ArrayList<String>();
		Addenda = new ArrayList<String>();
		
		addPraiseandWorship();
		addFulnessoftheSpirit();
		addTheChurch();
		addExperiencesontheJourneyofLife();
		addPracticesoftheChristianlife();
		addTheGoodNewsofJesusChrist();
		addServiceandministering();
		addTriumphantinChrist();
		addExpectingtheLordsReturn();
		addChoruses();
		addScripture();
		addAddenda();

		listDataChild.put(listDataHeader.get(0), PraiseandWorship);
		listDataChild.put(listDataHeader.get(1), FulnessoftheSpirit);
		listDataChild.put(listDataHeader.get(2), TheChurch);
		listDataChild.put(listDataHeader.get(3), ExperiencesontheJourneyofLife);
		listDataChild.put(listDataHeader.get(4), PracticesoftheChristianlife);
		listDataChild.put(listDataHeader.get(5), TheGoodNewsofJesusChrist);
		listDataChild.put(listDataHeader.get(6), Serviceandministering);
		listDataChild.put(listDataHeader.get(7), TriumphantinChrist);
		listDataChild.put(listDataHeader.get(8), ExpectingtheLordsReturn);
		listDataChild.put(listDataHeader.get(9), Choruses);
		listDataChild.put(listDataHeader.get(10), Scripture);
		listDataChild.put(listDataHeader.get(11), Addenda);

	}
	
	private void addPraiseandWorship(){


	}
	private void addFulnessoftheSpirit(){


	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		ConnectivityManager cm =
		        (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		if (isConnected){
			String selected = (String)listAdapter.getChild(groupPosition, childPosition);
			if (selected.charAt(2) == ' '){
				selected = selected.substring(0, 2);
			} else {
				selected = selected.substring(0, 3);
			}
			Intent intent = new Intent(getActivity(), pictureactivity.class);
			intent.putExtra("link", Integer.parseInt(selected));
			startActivity(intent);
		} else {
			Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();;
		}

		
		
		return true;
	}

	
}
*/
