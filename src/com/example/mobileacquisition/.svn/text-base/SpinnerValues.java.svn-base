package com.example.mobileacquisition;

import java.util.ArrayList;
import java.util.List;

import common.CustomTab;
import common.DataCollection;
import common.ScaleView;

import draw.map.AI;
import draw.map.ChannelWatching;
import draw.map.FFT;
import draw.map.OCT;
import draw.map.SPL;
import draw.map.Signal;

import left.drawer.MainFragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

public class SpinnerValues {

	private ArrayList<String> algorithmItems = new ArrayList<String>(); 
	private ArrayAdapter<String> adapter;
	private MainFragment mainFragment;
	public Handler myHandler;
	private MainActivity context;
	private ScaleView newView, oldView;
	private RelativeLayout mapView;
	public ArrayAdapter<String> getAdapter() {
		return adapter;
	}
	public void setAdapter(ArrayAdapter<String> adapter) {
		this.adapter = adapter;
	}
	public SpinnerValues(Activity context,MainFragment mainFragment) {
		// TODO Auto-generated constructor stub
		this.context=(MainActivity)context;
		this.mainFragment=mainFragment;
		adapter = new ArrayAdapter<String>((MainActivity)context,android.R.layout.simple_spinner_item, algorithmItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
	}
	
	public void  addValues(String str){//ArrayList<String> list
		if(!algorithmItems.contains(str)){
			algorithmItems.add(str);
		}
	}
	public void removeValues(String str){
		for(int i=0;i<algorithmItems.size();i++){
			if(algorithmItems.get(i).equals(str)){
				algorithmItems.remove(i);
			}
		}
//		if(mainFragment.getAlgorithm_spinner()!=null){
//			mainFragment.getAlgorithm_spinner().setText(algorithmItems.get(0));
//			/*RelativeLayout mapView = (RelativeLayout) context.findViewById(R.id.algorithm_view);
//			mapView.removeAllViews();*/		
//		}
	//	removeView();
	}
	public ArrayList<String> getAlgorithmItems() {
		return algorithmItems;
	}
	public void setAlgorithmItems(ArrayList<String> algorithmItems) {
		this.algorithmItems = algorithmItems;
	}
	
}
