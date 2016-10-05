package com.example.mobileacquisition;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import common.ScaleView;

public class XaxisFragment extends Fragment implements OnClickListener{
	private TextView algorithm_spinner;
	private EditText editText1,editText2;
	private Spinner XAxis_spinner;
	//private ArrayList<String> spinner_list = new ArrayList<String>();
	private String xLableState;
	private ScaleView scaleView;
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
		View view = inflater.inflate(R.layout.x_axis, container, false);
		XAxis_spinner = (Spinner) view.findViewById(R.id.XAxis_spinner);
		editText1 = (EditText) view.findViewById(R.id.editText1);
		editText2 = (EditText) view.findViewById(R.id.editText2);
		button = (Button) view.findViewById(R.id.xaxis_apply);
		if(ThemeLogic.themeType==1){
			button.setBackgroundResource(R.drawable.bt_blue_selector);
		}else{
			button.setBackgroundResource(R.drawable.bt_blue_selector1);
			
		}
		algorithmChanged();
		if("s".equals(xLableState)){
			XAxis_spinner.setSelection(0);
		} else if( "ms".equals(xLableState)){
			XAxis_spinner.setSelection(1);
		}else if( "RPM".equals(xLableState)){
			if(algorithm_spinner.getText().equals("Order")){
				XAxis_spinner.setSelection(0);
			}else{
				XAxis_spinner.setSelection(2);
			}
		}else if( "Hz".equals(xLableState)){
			XAxis_spinner.setSelection(0);
		}
		XAxis_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						// TODO Auto-generated method stub
						if(v==null){
							return;
						}
						//if (getActivity().getClass().getSimpleName().equals("MainActivity")) {
						if (scaleView != null) {
							scaleView.setXLableState(((TextView)v).getText().toString());
							scaleView.invalidate();
							xLableState = ((TextView)v).getText().toString();
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

	public void setXLableState(String xLableState) {
		this.xLableState = xLableState;
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
		if (algorithm_spinner.getText().equals("Signal")
				|| algorithm_spinner.getText().equals("RPM")) {
			spinner_list.clear();
			spinner_list.add("s");
			spinner_list.add("ms");
		} else if (algorithm_spinner.getText().equals("SPL")
				|| algorithm_spinner.getText().equals("AI")
				|| algorithm_spinner.getText().equals("Waterfall")) {
			spinner_list.clear();
			spinner_list.add("s");
			spinner_list.add("ms");
			spinner_list.add("RPM");
			
		} else if (algorithm_spinner.getText().equals("OCT")
				|| algorithm_spinner.getText().equals("FFT")) {
			spinner_list.clear();
			spinner_list.add("Hz");
		}else if(algorithm_spinner.getText().equals("Order")){
			spinner_list.clear();
			spinner_list.add("RPM");
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				spinner_list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.notifyDataSetChanged();
		XAxis_spinner.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		
		String minNum = editText1.getText().toString();
		String maxNum = editText2.getText().toString();
		
		float minValue,maxValue;
		
		Pattern pattern = Pattern.compile("([0-9]+.[0-9]+)|[0-9]+");
		
		if((!pattern.matcher(minNum).matches()) && (!pattern.matcher(maxNum).matches())){
			Toast.makeText(XaxisFragment.this.getActivity(),
					R.string.enter_number, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!pattern.matcher(minNum).matches()){
			minValue = 0;
		}else{
			minValue = Float.parseFloat(minNum);
		}
		
		if(!pattern.matcher(maxNum).matches()){
			maxValue = 5;
		}else{
			maxValue = Float.parseFloat(maxNum);
		}
		
		if(minValue > maxValue){
			Toast.makeText(XaxisFragment.this.getActivity(),
					R.string.range_error, Toast.LENGTH_SHORT).show();
			return;
		}
		
		int vieww = scaleView.getWidth();
		float xGrid = scaleView.getXGrid();
		float xmultiple = scaleView.getXMultiple();
		
		if("s".equals(xLableState)){
			if(minValue<0){
				Toast.makeText(XaxisFragment.this.getActivity(),
						R.string.minimum_value, Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(maxValue > ((int)((vieww-50)/xGrid))/2.0){
				Toast.makeText(XaxisFragment.this.getActivity(),
						getActivity().getResources().getString(R.string.maximum_value)
						+((int)((vieww-50)/xGrid))/2.0, Toast.LENGTH_SHORT).show();
				return;
			}
		} else if("ms".equals(xLableState)){
			if(minValue<0){
				Toast.makeText(XaxisFragment.this.getActivity(),
						R.string.minimum_value, Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(maxValue/1000>((int)((vieww-50)/xGrid))/2.0){
				Toast.makeText(XaxisFragment.this.getActivity(),
						getActivity().getResources().getString(R.string.maximum_value)
						+((int)((vieww-50)/xGrid))/2.0*1000, Toast.LENGTH_SHORT).show();
				return;
			}
		}else if("Hz".equals(xLableState)){
			if(minValue<0){
				Toast.makeText(XaxisFragment.this.getActivity(),
						R.string.minimum_value, Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(maxValue>((int)(vieww-50)*xmultiple)){
				Toast.makeText(XaxisFragment.this.getActivity(),
						getActivity().getResources().getString(R.string.maximum_value)
						+((int)(vieww-50)*xmultiple), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		
		scaleView.setXExent(minValue,maxValue);
		
	}

}
