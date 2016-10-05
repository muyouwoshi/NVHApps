package com.example.mobileacquisition;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import common.ScaleView;

public class CursorFragment extends Fragment implements OnClickListener{
	private CheckBox show_cursor,hide_cursor;
	private TextView algorithm_spinner;
//	private int[] channelArray;
	private ScaleView scaleView;
	private LinearLayout selectLayout;
	private int currentChan = -1;
	private ArrayList<Integer> isActivated_ChanNum;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.custom_dialog_cursor, container, false);
		show_cursor = (CheckBox)view.findViewById(R.id.show_cursor);
		hide_cursor = (CheckBox)view.findViewById(R.id.hide_cursor);
		show_cursor.setOnClickListener(this);
		hide_cursor.setOnClickListener(this);
		show_cursor.setChecked(true);
		
		
		
		selectLayout = (LinearLayout) view.findViewById(R.id.select_layout);
		
		isActivated_ChanNum =((MainActivity)this.getActivity()).bottomOperate.addChannelViewPager.isActivated_ChanNum;		
		addCheckBox();
		
		if(scaleView != null&& scaleView.getRuler().isShown()) setChecked(show_cursor);
		else setChecked(hide_cursor);
		return view;
		
//		add channnel View
	}
	
	private void addCheckBox() {
		// TODO Auto-generated method stub
		selectLayout.removeAllViews();
		if (this.scaleView!= null){
			boolean isFrist = true;
			for(int i = 0; i<isActivated_ChanNum.size();i++ ){
				int index=isActivated_ChanNum.get(i);
					CheckBox channelCheck = new CheckBox(getActivity());
					channelCheck.setText(""+index);
					channelCheck.setButtonDrawable(R.drawable.analog_check);
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-2,-2);

					if(isFrist){
						isFrist = false;
					}else param.leftMargin =50;
					if(index == currentChan){
						channelCheck.setChecked(true);
					}					
					channelCheck.setLayoutParams(param);
					channelCheck.setTag("cursor_check"+index);
					
					channelCheck.setOnClickListener(this);
					selectLayout.addView(channelCheck);
			}
		}
	}

	public void setView(ScaleView scaleView){
		this.scaleView = scaleView;
		this.currentChan = scaleView.getCursorNum();	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.show_cursor:
			setChecked(v);
			break;
		case R.id.hide_cursor:
			setChecked(v);
			break;
		}
		
		if(v.getTag()!= null && v.getTag().toString().contains("cursor_check")){
			setChannelCheck(v);
		}
		
	}
	private void setChannelCheck(View v) {
		// TODO Auto-generated method stub
		int channelNum =  selectLayout.getChildCount();
		for(int i = 0;i< channelNum; i++){
			CheckBox checkBox = (CheckBox) selectLayout.getChildAt(i);
			if (!checkBox.getTag().equals(v.getTag())) checkBox.setChecked(false);
			else if(this.scaleView!= null){
				if(checkBox.isChecked()){
					currentChan = Integer.parseInt(checkBox.getTag().toString().replace("cursor_check", ""));
					scaleView.setCursorNum(currentChan);
				}
				else{
					scaleView.setCursorNum(-1);
					currentChan = -1;
				}
				scaleView.getRuler().postInvalidate();
			}
		}
	}

	public TextView getAlgorithm_spinner() {
		return algorithm_spinner;
	}
	public void setAlgorithm_spinner(TextView algorithm_spinner) {
		this.algorithm_spinner = algorithm_spinner;
	}
	
	private void setChecked(View v) {
		show_cursor.setChecked(false);
		hide_cursor.setChecked(false);
		((CheckBox) v).setChecked(true);
		if(v.getId()==R.id.show_cursor){
			selectLayout.setVisibility(View.VISIBLE);
			this.scaleView.getRuler().setVisible(View.VISIBLE);
		}else {
			selectLayout.setVisibility(View.GONE);
			this.scaleView.getRuler().setVisible(View.GONE);
		}
	}
}
