package com.example.mobileacquisition;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.DataCollection;
import common.IAudioTrack;
import common.ScaleView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LegendFragment extends Fragment implements OnClickListener{
	private CheckBox show_legend,hide_legend;
	private RelativeLayout legend;
	private TextView algorithm_spinner;
	private SharedPreferences.Editor editor;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.custom_dialog_legend, container, false);
		show_legend = (CheckBox)view.findViewById(R.id.show_legend);
		hide_legend = (CheckBox)view.findViewById(R.id.hide_legend);
		if(getActivity().getClass().getSimpleName().equals("MainActivity")){
				addListener();
		}else if(getActivity().getClass().getSimpleName().equals("DriveModeActivity")){
			algorithm_spinner=(TextView)getActivity().findViewById(R.id.algorithm_title);
			legend = (RelativeLayout)getActivity().findViewById(R.id.drive_legend);
			SharedPreferences preference =getActivity().getSharedPreferences("hz_5D", 0);
			editor=preference.edit();
			editor.putBoolean("show_legend"+algorithm_spinner.getText(), true);
			editor.commit();
			addListener();
			}
		return view;
	}
	private void addListener(){
		if(algorithm_spinner.getText().equals("OCT")||algorithm_spinner.getText().equals("Waterfall")
				||algorithm_spinner.getText().equals("ChannelWatch")){
			show_legend.setButtonDrawable(R.drawable.checked);
			show_legend.setChecked(false);
			hide_legend.setButtonDrawable(R.drawable.checked_active);
			hide_legend.setChecked(true);
			legend.setVisibility(View.GONE);
			if(getActivity().getClass().getSimpleName().equals("DriveModeActivity")){
				editor.putBoolean("show_legend"+algorithm_spinner.getText(), false);
				editor.commit();
			}
		}else{
			if(legend.getVisibility()==0){
				show_legend.setButtonDrawable(R.drawable.checked_active);
				show_legend.setChecked(true);
				hide_legend.setButtonDrawable(R.drawable.checked);
				hide_legend.setChecked(false);
			}else{
				show_legend.setButtonDrawable(R.drawable.checked);
				show_legend.setChecked(false);
				hide_legend.setButtonDrawable(R.drawable.checked_active);
				hide_legend.setChecked(true);
			}
			show_legend.setOnClickListener(this);
			hide_legend.setOnClickListener(this);
			if(getActivity().getClass().getSimpleName().equals("DriveModeActivity")){
				editor.putBoolean("show_legend"+algorithm_spinner.getText(), true);
				editor.commit();
			}
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.show_legend:
			if(show_legend.isChecked()){
				show_legend.setButtonDrawable(R.drawable.checked_active);
				show_legend.setChecked(true);
				hide_legend.setButtonDrawable(R.drawable.checked);
				hide_legend.setChecked(false);
				legend.setVisibility(View.VISIBLE);
			}else{
				show_legend.setButtonDrawable(R.drawable.checked);
				show_legend.setChecked(false);
				hide_legend.setButtonDrawable(R.drawable.checked_active);
				hide_legend.setChecked(true);
				legend.setVisibility(View.GONE);
			}
			break;
		case R.id.hide_legend:
			if(hide_legend.isChecked()){
				hide_legend.setButtonDrawable(R.drawable.checked_active);
				hide_legend.setChecked(true);
				show_legend.setButtonDrawable(R.drawable.checked);
				show_legend.setChecked(false);
				legend.setVisibility(View.GONE);
			}else{
				hide_legend.setButtonDrawable(R.drawable.checked);
				hide_legend.setChecked(false);
				show_legend.setButtonDrawable(R.drawable.checked_active);
				show_legend.setChecked(true);
				legend.setVisibility(View.VISIBLE);
			}
			break;
		}
	}
	public RelativeLayout getLegend() {
		return legend;
	}
	public void setLegend(RelativeLayout legend) {
		this.legend = legend;
	}
	public TextView getAlgorithm_spinner() {
		return algorithm_spinner;
	}
	public void setAlgorithm_spinner(TextView algorithm_spinner) {
		this.algorithm_spinner = algorithm_spinner;
	}
}
