package com.example.mobileacquisition;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.ScaleView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class YaxisFragment extends Fragment implements OnClickListener{
	private TextView algorithm_spinner;
	private Spinner YAxis_spinner;
	//private ArrayList<String> spinner_list = new ArrayList<String>();
	private String yLableState;
	private ScaleView scaleView;
	private EditText editText1,editText2;
	private Button button;
	public TextView getAlgorithm_spinner() {
		return algorithm_spinner;
	}

	public void setAlgorithm_spinner(TextView algorithm_spinner) {
		this.algorithm_spinner = algorithm_spinner;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.y_axis, container, false);
		YAxis_spinner = (Spinner) view.findViewById(R.id.YAxis_spinner);
		editText1=(EditText)view.findViewById(R.id.editText1);
		editText2=(EditText)view.findViewById(R.id.editText2);
		button = (Button) view.findViewById(R.id.apply);
		if(ThemeLogic.themeType==1){
			button.setBackgroundResource(R.drawable.bt_blue_selector);
		}else{
			button.setBackgroundResource(R.drawable.bt_blue_selector1);
			
		}
		algorithmChanged();
		if("Pa".equals(yLableState)){
			YAxis_spinner.setSelection(0);
		} else if( "dB".equals(yLableState)){
			YAxis_spinner.setSelection(1);
		} else if( "dBA".equals(yLableState)){
			YAxis_spinner.setSelection(1);
		} else if( "dBC".equals(yLableState)){
			YAxis_spinner.setSelection(1);
		} else if( "RPM".equals(yLableState)){
			YAxis_spinner.setSelection(0);
		}
		YAxis_spinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						// TODO Auto-generated method stub
						if(v==null){
							return;
						}
						//if (getActivity().getClass().getSimpleName().equals("MainActivity")) {
						if (scaleView != null) {
							scaleView.setYLableState(((TextView)v).getText().toString());
							scaleView.invalidate();
							yLableState = ((TextView)v).getText().toString();
						}
						//}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		button.setOnClickListener(this);
		return view;
	}

	public void setYLableState(String yLableState) {
		this.yLableState = yLableState;
	}

	public void setScaleView(ScaleView scaleView) {
		this.scaleView = scaleView;
	}
	public void algorithmChanged(){
		ArrayList<String> spinner_list = new ArrayList<String>();
		if (getActivity().getClass().getSimpleName().equals("DriveModeActivity")) {
			algorithm_spinner = (TextView) getActivity().findViewById(
					R.id.algorithm_title);
			
		}
		if(algorithm_spinner.getText().equals("Signal")
				||algorithm_spinner.getText().equals("Order")){
			spinner_list.clear();
			spinner_list.add("Pa");
			spinner_list.add("dB");
		}else if(algorithm_spinner.getText().equals("AI")){
			spinner_list.clear();
			spinner_list.add("%");
		}else if(algorithm_spinner.getText().equals("RPM")){
			spinner_list.clear();
			spinner_list.add("RPM");
		}else if(algorithm_spinner.getText().equals("Waterfall")){
			spinner_list.clear();
			spinner_list.add("Hz");
		}else{
			SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
			String str="";
			if(algorithm_spinner.getText().equals("SPL")){
				str = preference.getString("SPL_FreqWeight", "None");
			}else if(algorithm_spinner.getText().equals("OCT")){
				str= preference.getString("OCT_FreqWeight","None");
			}else if(algorithm_spinner.getText().equals("FFT")){
				str= preference.getString("FFT_FreqWeight", "None");
			}
			if(str.equals("None")){
				spinner_list.clear();
				spinner_list.add("Pa");
				spinner_list.add("dB");
			}else if(str.equals("A")){
				spinner_list.clear();
				spinner_list.add("Pa");
				spinner_list.add("dBA");
			}else if(str.equals("C")){
				spinner_list.clear();
				spinner_list.add("Pa");
				spinner_list.add("dBC");
			}
			
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				spinner_list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.notifyDataSetChanged();
		YAxis_spinner.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		String minNum = editText1.getText().toString();
		String maxNum = editText2.getText().toString();
		
		float minValue,maxValue;
		
		Pattern pattern = Pattern.compile("(-$[0-9]+.[0-9]+)|[-$0-9]+");
		
		if((!pattern.matcher(minNum).matches()) && (!pattern.matcher(maxNum).matches())){
			Toast.makeText(YaxisFragment.this.getActivity(),
					R.string.enter_number, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!pattern.matcher(minNum).matches()){
			Toast.makeText(YaxisFragment.this.getActivity(),
					R.string.minimum_error, Toast.LENGTH_SHORT).show();
			return;
		}else{
			minValue = Float.parseFloat(minNum);
		}
		
		if(!pattern.matcher(maxNum).matches()){
			Toast.makeText(YaxisFragment.this.getActivity(),
					R.string.maximum_error, Toast.LENGTH_SHORT).show();
			return;
		}else{
			maxValue = Float.parseFloat(maxNum);
		}
		
		if(minValue > maxValue){
			Toast.makeText(YaxisFragment.this.getActivity(),
					R.string.range_error, Toast.LENGTH_SHORT).show();
			return;
		}
		
		int viewh = scaleView.getHeight();
		float yGrid = scaleView.getYGrid();
		
		if("Pa".equals(yLableState)){
			if (maxValue-minValue > (int)((viewh-50)/yGrid)) {
				Toast.makeText(
						YaxisFragment.this.getActivity(),
						getActivity().getResources().getString(R.string.maximum_and_minimum)
						+(int)((viewh-50)/yGrid), Toast.LENGTH_SHORT).show();
				return;
			}
		} else if("dB".equals(yLableState) || "dBA".equals(yLableState) || "dBC".equals(yLableState)){
			if (maxValue-minValue > (int)((viewh-50)/yGrid)*100) {
				Log.v("bug11", "value:"+(maxValue-minValue));
				Log.v("bug11", "dB:"+((int)((viewh-50)/yGrid)*100));

				Toast.makeText(
						YaxisFragment.this.getActivity(),
						getActivity().getResources().getString(R.string.maximum_and_minimum)
						+(int)((viewh-50)/yGrid)*100, Toast.LENGTH_SHORT).show();
				return;
			}
		}
		scaleView.setYExent(minValue,maxValue);
		
	}
}
