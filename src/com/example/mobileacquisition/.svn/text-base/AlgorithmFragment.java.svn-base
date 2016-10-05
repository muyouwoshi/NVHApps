package com.example.mobileacquisition;

import java.util.ArrayList;

import mode.drive.DriveModeActivity;
import common.CustomTab;

import draw.map.AI;
import draw.map.FFT;
import draw.map.OCT;
import draw.map.SPL;
import draw.map.Signal;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AlgorithmFragment extends Fragment implements OnClickListener{
	//private TextView algorithm_spinner;
	private TextView Signal,SPL,OCT,FFT,AI,RPM,Waterfall,Order;
	private ArrayList<TextView> textViewList = new ArrayList<TextView>();
	private XaxisFragment xaxisFragment;
	public XaxisFragment getXaxisFragment() {
		return xaxisFragment;
	}
	public void setXaxisFragment(XaxisFragment xaxisFragment) {
		this.xaxisFragment = xaxisFragment;
	}
	private YaxisFragment yaxisFragment;
	public YaxisFragment getYaxisFragment() {
		return yaxisFragment;
	}
	public void setYaxisFragment(YaxisFragment yaxisFragment) {
		this.yaxisFragment = yaxisFragment;
	}
	private MainContextView mainContextView;
	public MainContextView getMainContextView() {
		return mainContextView;
	}
	public void setMainContextView(MainContextView mainContextView) {
		this.mainContextView = mainContextView;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_algorithm, container, false);
		//algorithm_spinner=(TextView)getActivity().findViewById(R.id.algorithm_spinner);
		Signal = (TextView)view.findViewById(R.id.Signal);
		SPL = (TextView)view.findViewById(R.id.SPL);
		OCT = (TextView)view.findViewById(R.id.OCT);
		FFT = (TextView)view.findViewById(R.id.FFT);
		AI = (TextView)view.findViewById(R.id.AI);
		RPM = (TextView)view.findViewById(R.id.RPM);
		Waterfall = (TextView)view.findViewById(R.id.Waterfall);
		Order = (TextView)view.findViewById(R.id.Order);
		Signal.setOnClickListener(this);
		SPL.setOnClickListener(this);
		OCT.setOnClickListener(this);
		FFT.setOnClickListener(this);
		AI.setOnClickListener(this);
		RPM.setOnClickListener(this);
		Waterfall.setOnClickListener(this);
		Order.setOnClickListener(this);
		textViewList.add(Signal);
		textViewList.add(SPL);
		textViewList.add(OCT);
		textViewList.add(FFT);
		textViewList.add(AI);
		textViewList.add(RPM);
		textViewList.add(Waterfall);
		textViewList.add(Order);
		algorithmChanged();
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.Signal:
		case R.id.SPL:
		case R.id.OCT:
		case R.id.FFT:
		case R.id.AI:
		case R.id.RPM:
		case R.id.Waterfall:
		case R.id.Order:
			if (getActivity().getClass().getSimpleName().equals("MainActivity")) {
				mainContextView.getAlgorithm_spinner().setText(((TextView)v).getText());
				mainContextView.drawMap();
			}else if (getActivity().getClass().getSimpleName().equals("DriveModeActivity")) {
				TextView algorithm_spinner = (TextView) getActivity().findViewById(
						R.id.algorithm_title);
				algorithm_spinner.setText(((TextView)v).getText());
				((DriveModeActivity)getActivity()).refreshView();
			}
			//v.setTextColor(Color.GREEN);
			break;
		
		}
		xaxisFragment.algorithmChanged();
		yaxisFragment.algorithmChanged();
		algorithmChanged();
	}
	
	
	private void algorithmChanged(){
		SharedPreferences preference =getActivity().getSharedPreferences("hz_5D", 0);
		String algorithmChecked = preference.getString("Signal", "close");
		if(algorithmChecked.equals("open")) {
			Signal.setTextColor(Color.rgb(0, 0, 0));
			Signal.setClickable(true);
			
		}else{
			Signal.setTextColor(Color.rgb(219, 219, 219));
			Signal.setClickable(false);
		}
		algorithmChecked = preference.getString("SPL", "close");
		if(algorithmChecked.equals("open")) {
			SPL.setTextColor(Color.rgb(0, 0, 0));
			SPL.setClickable(true);
			
		}else{
			SPL.setTextColor(Color.rgb(219, 219, 219));
			SPL.setClickable(false);
		}
		algorithmChecked = preference.getString("OCT", "close");
		if(algorithmChecked.equals("open")) {
			OCT.setTextColor(Color.rgb(0, 0, 0));
			OCT.setClickable(true);
			
		}else{
			OCT.setTextColor(Color.rgb(219, 219, 219));
			OCT.setClickable(false);
		}
		algorithmChecked = preference.getString("FFT", "close");
		if(algorithmChecked.equals("open")) {
			FFT.setTextColor(Color.rgb(0, 0, 0));
			FFT.setClickable(true);
			
		}else{
			FFT.setTextColor(Color.rgb(219, 219, 219));
			FFT.setClickable(false);
		}
		algorithmChecked = preference.getString("AI", "close");
		if(algorithmChecked.equals("open")) {
			AI.setTextColor(Color.rgb(0, 0, 0));
			AI.setClickable(true);
			
		}else{
			AI.setTextColor(Color.rgb(219, 219, 219));
			AI.setClickable(false);
		}
		algorithmChecked = preference.getString("RPM", "close");
		if(algorithmChecked.equals("open")) {
			RPM.setTextColor(Color.rgb(0, 0, 0));
			RPM.setClickable(true);
			
		}else{
			RPM.setTextColor(Color.rgb(219, 219, 219));
			RPM.setClickable(false);
		}
		algorithmChecked = preference.getString("Waterfall", "close");
		if(algorithmChecked.equals("open")) {
			Waterfall.setTextColor(Color.rgb(0, 0, 0));
			Waterfall.setClickable(true);
		}else{
			Waterfall.setTextColor(Color.rgb(219, 219, 219));
			Waterfall.setClickable(false);
		}
		algorithmChecked = preference.getString("Order", "close");
		if(algorithmChecked.equals("open")) {
			Order.setTextColor(Color.rgb(0, 0, 0));
			Order.setClickable(true);
		}else{
			Order.setTextColor(Color.rgb(219, 219, 219));
			Order.setClickable(false);
		}
		if (getActivity().getClass().getSimpleName().equals("MainActivity")) {
			for(TextView textView :textViewList){
				if(textView.getText().equals(mainContextView.getAlgorithm_spinner().getText())){
					textView.setTextColor(Color.GREEN);
				}
			}
		}else if (getActivity().getClass().getSimpleName().equals("DriveModeActivity")) {
			TextView algorithm_spinner = (TextView) getActivity().findViewById(
					R.id.algorithm_title);
			for(TextView textView :textViewList){
				if(textView.getText().equals(algorithm_spinner.getText())){
					textView.setTextColor(Color.GREEN);
				}
			}
		}
	}
}
