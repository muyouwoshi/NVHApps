package left.drawer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.MainContextView;
import com.example.mobileacquisition.OneTabFragment;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.SpinnerValues;
import com.example.mobileacquisition.ThemeLogic;

import common.CustomTab;
import common.DataCollection;
import draw.map.FFT;

public class FftFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener, OnFocusChangeListener {

	private View view = null;
	private Spinner freqRang_Spinner = null;
	private EditText overlap_Edit = null;
	private Spinner freqRes_Spinner = null;
	private Spinner freqWeight_Spinner = null;
	private EditText averaging_Edit = null;
	private Spinner window_Spinner = null;
	private Spinner xAxis_Spinner = null;
	private Spinner yAxis_Spinner = null;
	private Switch fftSwitch = null;
	private SpinnerValues spinnerValues;
	private String xAxisStr;
	private String yAxisStr;
	private String switchStr;
	private String averagingStr;
	private String overlapStr;
	private String freqWeightStr;
	private String freqRangStr;
	private String freqResStr;
	private String windowStr;
	private String acquiFreqStr;
	private ArrayList<String> unitsList = new ArrayList<String>(); 

	private ArrayList<String> freqRes_spinner_list = new ArrayList<String>();//Ƶ�ʷֱ���Spinner��List����
	//public ArrayList<String> freqRang_spinner_list = new ArrayList<String>();;//Ƶ�ʷ�ΧSpinner��List����

	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> resAdapter;
	private SharedPreferences.Editor editor;
	
	public FftFragment() {
		
	}

	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fft, container, false);
		view.setOnClickListener(this);
		unitsList.add("Pa");
		unitsList.add("dB");
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, unitsList);
		fftSwitch = (Switch) view.findViewById(R.id.FFTSwitchButton);
		fftSwitch.setThumbResource(R.drawable.switchtitle);
		fftSwitch.setSwitchTextAppearance(view.getContext(),R.color.black);
		view.findViewById(R.id.Back_Fft).setOnClickListener(this);
		freqRang_Spinner = (Spinner) view
				.findViewById(R.id.FFTFreqRange_spinner);
//		ArrayAdapter<String>  mAdapter = new FFTWindowSpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.FFT_Freq_Range));
//		freqRang_Spinner.setAdapter(mAdapter);
		freqWeight_Spinner = (Spinner) view
				.findViewById(R.id.FFTFreqWeight_spinner);
		ArrayAdapter<String> freqWeight_adapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Freq_Weight));
		freqWeight_adapter.notifyDataSetChanged();
		freqWeight_Spinner.setAdapter(freqWeight_adapter);
		window_Spinner = (Spinner) view.findViewById(R.id.FFTWindow_spinner);
		ArrayAdapter<String> mAdapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Window));
		window_Spinner.setAdapter(mAdapter);
		xAxis_Spinner = (Spinner) view.findViewById(R.id.FFTXAxis_spinner);
		yAxis_Spinner = (Spinner) view.findViewById(R.id.FFTYAxis_spinner);
		yAxis_Spinner.setAdapter(adapter);
		overlap_Edit = (EditText) view.findViewById(R.id.FFTOverlap_edit);
		freqRes_Spinner = (Spinner) view.findViewById(R.id.FFTFreqRes_spinner);
		averaging_Edit = (EditText) view.findViewById(R.id.FFTAveraging_edit);


		resAdapter = new CustomSpinnerArrayAdapter(getActivity(), freqRes_spinner_list);
		setSpinnerList(freqRang_Spinner.getSelectedItem().toString());
		resAdapter.notifyDataSetChanged();
		freqRes_Spinner.setAdapter(resAdapter);

		overlap_Edit.setOnFocusChangeListener(this);
		averaging_Edit.setOnFocusChangeListener(this);
		
		
		window_Spinner.setOnItemSelectedListener(this);
		xAxis_Spinner.setOnItemSelectedListener(this);
		yAxis_Spinner.setOnItemSelectedListener(this);
	
		overlap_Edit.addTextChangedListener(textWatcherOverlap_Edit);
		averaging_Edit.addTextChangedListener(textWatcherAveraging_Edit);
		
		init();
		freqRes_Spinner.setOnItemSelectedListener(this);	//��������Ҫ����init����֮��ִ��
		freqRang_Spinner.setOnItemSelectedListener(this);
		freqWeight_Spinner.setOnItemSelectedListener(this);

		Switch();
//		setFFTSpinnerItemListener();

		return view;
	}
	private TextWatcher textWatcherOverlap_Edit = new TextWatcher() {  
		  
		@Override  
		public void onTextChanged(CharSequence s, int start, int before,  
		int count) {  
			int point = 0;
			for (int i = 0; i < overlap_Edit.getText().toString().length(); i++) {
				// if(order_edit.getText().toString().indexOf(".")!=-1){
				if (overlap_Edit.getText().toString().charAt(i) == '.')
					point = point + 1;
				// }
			}
			if (point > 1) {
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
			//overlaps��0 - 99.99
			if(!overlap_Edit.getText().toString().equals("")&&Float.parseFloat(overlap_Edit.getText().toString())>(float)99.99){
				overlap_Edit.setText(preference.getString("FFT_Overlap", "50"));
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
 
		}  
		  
		@Override  
		public void beforeTextChanged(CharSequence s, int start, int count,  
		int after) {  
		//    
		}  

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}  
		};
	private TextWatcher textWatcherAveraging_Edit = new TextWatcher() {  
			  
			@Override  
			public void onTextChanged(CharSequence s, int start, int before,  
			int count) {  
				int point = 0;
				for (int i = 0; i < averaging_Edit.getText().toString().length(); i++) {
					// if(order_edit.getText().toString().indexOf(".")!=-1){
					if (averaging_Edit.getText().toString().charAt(i) == '.')
						point = point + 1;
					// }
				}
				if (point > 1) {
					Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT).show();
					return;
				}
				SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
				//average��1 - 1e09
				if(!averaging_Edit.getText().toString().equals("")){
					if(Float.parseFloat(averaging_Edit.getText().toString())<1
							||Float.parseFloat(averaging_Edit.getText().toString())>(float)Math.pow(10,9)){
						averaging_Edit.setText(preference.getString("FFT_Averaging", "3"));
						Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT).show();
					return;
					}
				}
			}  
			  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count,  
			int after) {  
			//    
			}  

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}  
			};
	public void init() {
		setAcquiFreq(Integer.parseInt(acquiFreqStr));
		setSlectedItem(freqRang_Spinner, freqRangStr);
		setSlectedItem(freqWeight_Spinner, freqWeightStr);
		window_Spinner.setSelection(Integer.parseInt(windowStr));
		setSlectedItem(yAxis_Spinner, yAxisStr);
		setSlectedItem(xAxis_Spinner, xAxisStr);
		setSlectedItem(freqRes_Spinner, freqResStr);
		
		if(!overlapStr.matches("[0-9]+|[0.9]+.[0-9]+")){
			overlapStr = "50";
			editor.putString("FFT_Overlap", "50");
			editor.commit();
		}
		overlap_Edit.setText(overlapStr);
		
		if(!averagingStr.matches("[0-9]+")){
			averagingStr = "3";
			editor.putString("FFT_Averaging", "3");
			editor.commit();
		}
		averaging_Edit.setText(averagingStr);
		setSwitch();
	}

	@Override
	public void onClick(View v) {
		MainFragment mainFragment = (MainFragment) getFragmentManager()
				.findFragmentByTag("main");
		FftFragment acquiFragment = (FftFragment) getFragmentManager()
				.findFragmentByTag("fft");
		switch (v.getId()) {
		case R.id.Back_Fft:
			getFragmentManager().beginTransaction().hide(acquiFragment)
					.show(mainFragment).commit();
			break;
		}
	}

	public void Switch() {

		fftSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// SharedPreferences preference =
				// getActivity().getSharedPreferences("hz_5D", 0);
				// SharedPreferences.Editor editor = preference.edit();
				CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
				if (ct.algorithm_spinnerList.size() != 0) {
					for (int j = 0; j < ct.algorithm_spinnerList.size(); j++) {
						if (ct.algorithm_spinnerList.get(j).getText()
								.equals("FFT")
								&& DataCollection.isRecording) {
							fftSwitch.setChecked(true);
							Toast.makeText(
									getActivity().getApplicationContext(),
									R.string.Collecting_data,
									Toast.LENGTH_SHORT)// ����������
									.show();
							return;
						}
					}
				}
				if (isChecked) {
					fftSwitchOpen();
				} else {
					fftSwitchOff();
				}
				// editor.commit();
			}
		});

	}

	@SuppressLint("NewApi")
	private void fftSwitchOpen() {
		spinnerValues.addValues("FFT");
		fftSwitch.setTrackResource(R.drawable.switchbutton_on);
		// ����Ϣ�����ģ���ļ�
		editor.putString("FFT", "open");
		editor.putString("FFT_FreqRange", freqRang_Spinner.getSelectedItem()
				.toString());
		editor.putString("FFT_Overlap", overlap_Edit.getText().toString());
		editor.putString("FFT_FreqRes", freqRes_Spinner.getSelectedItem()
				.toString());
		editor.putString("FFT_FreqWeight", freqWeight_Spinner.getSelectedItem()
				.toString());
		editor.putString("FFT_Averaging", averaging_Edit.getText().toString());
		editor.putString("FFT_Window_Position", ""+window_Spinner.getSelectedItemPosition());

		editor.putString("FFT_XAxis", xAxis_Spinner.getSelectedItem()
				.toString());
		editor.putString("FFT_YAxis", yAxis_Spinner.getSelectedItem()
				.toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
	}

	@SuppressLint("NewApi")
	private void fftSwitchOff() {

		spinnerValues.removeValues("FFT");
		fftSwitch.setTrackResource(R.drawable.switchbutton_off);
		// ��ģ���ļ��е���Ϣɾ��
		editor.putString("FFT", "close");
		editor.putString("FFT_FreqRange", freqRang_Spinner.getSelectedItem()
				.toString());
		editor.putString("FFT_Overlap", overlap_Edit.getText().toString());

		editor.putString("FFT_FreqRes", freqRes_Spinner.getSelectedItem()
				.toString());
		editor.putString("FFT_FreqWeight", freqWeight_Spinner.getSelectedItem()
				.toString());
		editor.putString("FFT_Averaging", averaging_Edit.getText().toString());
		editor.putString("FFT_Window_Position", ""+window_Spinner.getSelectedItemPosition());

		editor.putString("FFT_XAxis", xAxis_Spinner.getSelectedItem()
				.toString());
		editor.putString("FFT_YAxis", yAxis_Spinner.getSelectedItem()
				.toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
		removeAllView();

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		SharedPreferences preference = getActivity().getSharedPreferences(
				"hz_5D", 0);
		SharedPreferences.Editor editor = preference.edit();
		switch (parent.getId()) {
		case R.id.FFTFreqRange_spinner:
			TextView tv = (TextView) view;
			if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
			tv.setGravity(Gravity.CENTER);
			editor.putString("FFT_FreqRange", freqRang_Spinner
					.getSelectedItem().toString());
			setFrepRangToMap();
			setSpinnerList(tv.getText().toString());
			resAdapter.notifyDataSetChanged();
			
			if(freqRes_Spinner.getSelectedItemPosition() == 0) freqRes_Spinner.setSelection(1,true);
			freqRes_Spinner.setSelection(0, true);
			if(freqResStr!=null){
				setSlectedItem(freqRes_Spinner, freqResStr);
			}
			break;
		case R.id.FFTFreqRes_spinner:
			TextView tv0 = (TextView) view;
			if(null == tv0) return;
			if(ThemeLogic.themeType==1){
				 tv0.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv0.setTextColor(getResources().getColor(R.color.black));
			}
			tv0.setGravity(Gravity.CENTER);
			editor.putString("FFT_FreqRes", freqRes_Spinner.getSelectedItem()
					.toString());
			setFftRes();

			break;
		case R.id.FFTFreqWeight_spinner:
			TextView tv1 = (TextView) view;
			if(ThemeLogic.themeType==1){
				 tv1.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv1.setTextColor(getResources().getColor(R.color.black));
			}
			tv1.setGravity(Gravity.CENTER);
			editor.putString("FFT_FreqWeight", freqWeight_Spinner
					.getSelectedItem().toString());
			
			setYUnitList(position);
			
			break;
		case R.id.FFTWindow_spinner:
			TextView tv2 = (TextView) view;
			if(ThemeLogic.themeType==1){
				 tv2.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv2.setTextColor(getResources().getColor(R.color.black));
			}
			tv2.setGravity(Gravity.RIGHT);
			editor.putString("FFT_Window_Position", ""+window_Spinner.getSelectedItemPosition());
			setFftWindow();
			break;
		case R.id.FFTXAxis_spinner:
			TextView tv3 = (TextView) view;
			if(ThemeLogic.themeType==1){
				 tv3.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv3.setTextColor(getResources().getColor(R.color.black));
			}
			tv3.setGravity(Gravity.CENTER);
			editor.putString("FFT_XAxis", xAxis_Spinner.getSelectedItem()
					.toString());
			spinnerChangeState(0,xAxis_Spinner.getSelectedItem().toString());
			break;
		case R.id.FFTYAxis_spinner:
			TextView tv4 = (TextView) view;
			if(tv4!=null){
				if(ThemeLogic.themeType==1){
					 tv4.setTextColor(getResources().getColor(R.color.white4));
				}else{
					
					tv4.setTextColor(getResources().getColor(R.color.black));
				}
				tv4.setGravity(Gravity.CENTER);
			}
			editor.putString("FFT_YAxis", yAxis_Spinner.getSelectedItem()
					.toString());
			spinnerChangeState(1,yAxis_Spinner.getSelectedItem().toString());
			
			break;
		}
		editor.commit();

	}

	private void setYUnitList(int position) {
		// TODO Auto-generated method stub
		switch(position){
		case 0:
			unitsList.clear();
			unitsList.add("Pa");
			unitsList.add("dB");
			yAxis_Spinner.setAdapter(adapter);
			break;
		case 1:
			unitsList.clear();
			unitsList.add("Pa");
			unitsList.add("dBA");
			yAxis_Spinner.setAdapter(adapter);
			yAxis_Spinner.setSelection(1);
			break;
		case 2:
			unitsList.clear();
			unitsList.add("Pa");
			unitsList.add("dBC");
			yAxis_Spinner.setAdapter(adapter);
			yAxis_Spinner.setSelection(1);
			break;
		}
	}

	private void setSpinnerList(String string) {
		if(null == string)
			return;
		float freqRange = Float.parseFloat(string)*1000;

	//	DecimalFormat df = new DecimalFormat("###0.00");
		
		freqRes_spinner_list.clear();

		int size=0;
		if(freqRange==51200||freqRange==48000||freqRange==25600||freqRange==24000){
			size=10;
		}else if(freqRange==12800||freqRange==12000){
			size=8;
		}else if(freqRange==6400||freqRange==6000){
			size=7;
		}else if(freqRange==3200||freqRange==3000){
			size=6;
		}else if(freqRange==1600||freqRange==1500){
			size=5;
		}else if(freqRange==800||freqRange==750){
			size=4;
		}else{
			size=3;
		}
		for(int i=30;i>0;i--){
			float ff=(float) (freqRange/Math.pow(2,i));
			if(ff>0.7&&freqRes_spinner_list.size()<size){
				freqRes_spinner_list.add(""+freqRange/Math.pow(2,i));
			}
		}
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		// if(!fftSwitch.isChecked()) return;
		if (hasFocus)
			return;
		// ʵʱ����ģ����Ϣ
		SharedPreferences preference = getActivity().getSharedPreferences(
				"hz_5D", 0);
		SharedPreferences.Editor editor = preference.edit();
		switch (v.getId()) {
		case R.id.FFTOverlap_edit:
			editor.putString("FFT_Overlap", overlap_Edit.getText().toString());
			
			if(!overlap_Edit.getText().toString().matches("s*")){
				setFftOverlap();
			}
			
			break;
		case R.id.FFTAveraging_edit:
			editor.putString("FFT_Averaging", averaging_Edit.getText()
					.toString());
			
			if(!averaging_Edit.getText().toString().matches("s*")){
				setFftAveraging();
			}
			break;
		}
		editor.commit();
	}



	private void removeAllView() {
		CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
		for (int j = 0; j < ct.algorithm_spinnerList.size(); j++) {
			ct.algorithm_spinnerList.get(j).setText(
					spinnerValues.getAlgorithmItems().get(0));
			((MainContextView) ct.mainContextViewList.get(j))
					.drawMap();
		}
	}

	private void spinnerChangeState(int axisType,String unitType){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("FFT")){
				((MainContextView)ct.mainContextViewList.get(i)).setUnitChangeState(axisType,unitType);
			}
		}
	}

	public void readFromXml(FragmentActivity fragmentActivity) {

		SharedPreferences preference = fragmentActivity.getSharedPreferences(
				"hz_5D", 0);
		editor = preference.edit();
		xAxisStr = preference.getString("FFT_XAxis", "Hz");
		yAxisStr = preference.getString("FFT_YAxis", "Pa");
		averagingStr = preference.getString("FFT_Averaging", "3");
		overlapStr = preference.getString("FFT_Overlap", "50");
		switchStr = preference.getString("FFT", "close");
		freqWeightStr = preference.getString("FFT_FreqWeight", "None");
		freqRangStr = preference.getString("FFT_FreqRange", "0.4");
		freqResStr = preference.getString("FFT_FreqRes", "0.78");
		windowStr = preference.getString("FFT_Window_Position", "0");
		acquiFreqStr = preference.getString("AcquiFreq_spinner_values", "12800");
		
//		Log.v("bug11", freqResStr);

	}

	private void setSlectedItem(Spinner spinner, String xAxisStr) {
		SpinnerAdapter adapter = spinner.getAdapter();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			if (adapter.getItem(i).toString().equals(xAxisStr)) {
				spinner.setSelection(i,true);
				if(spinner.getId() ==R.id.FFTFreqRange_spinner){
					setSpinnerList(adapter.getItem(i).toString());
					resAdapter.notifyDataSetChanged();
					//Toast.makeText(getActivity(),adapter.getItem(i).toString(), Toast.LENGTH_SHORT).show();
				}else if(spinner.getId() == R.id.FFTFreqWeight_spinner){
					setYUnitList(i);
				}
				
				break;
			}
		}
	}

	private void setSwitch() {
		if(null == switchStr)
			return;
		
		if (switchStr.equals("open")) {
			fftSwitch.setChecked(true);
			fftSwitchOpen();
		} else {
			fftSwitch.setChecked(false);
			fftSwitchOff();
		}
	}

	private void setFftRes() {
		// TODO Auto-generated method stub
		float value = Float.parseFloat(freqRes_Spinner.getSelectedItem().toString());
		
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.FFT")) {
				FFT fft = (FFT)context.getViewList().get(i);
				fft.setFreqRes(value);
			}

		}
	}
	
	private void setFftWindow() {
		// TODO Auto-generated method stub
		int value = window_Spinner.getSelectedItemPosition();
		
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.FFT")) {
				FFT fft = (FFT)context.getViewList().get(i);
				fft.setWindowType(value);
			}
		}
	}


	private void setFftAveraging() {
		// TODO Auto-generated method stub
		int value = 3;
		String avgData=averaging_Edit.getText().toString();
		if(avgData.matches("[0-9]+")){
			value= Integer.valueOf(avgData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_averaging_wrong, Toast.LENGTH_SHORT);
			value = 3;
		}
		
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.FFT")) {
				FFT fft = (FFT)context.getViewList().get(i);
				fft.setMean(value);
			}
		}
	}
	//��ȡ�ص���
	public void setFftOverlap() {
		float value;
		String overlapData=overlap_Edit.getText().toString();
		if(overlapData.matches("[0-9]+|[0.9]+.[0-9]+")){
			value = Float.valueOf(overlapData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_overlap_wrong, Toast.LENGTH_SHORT);
			value = 50;
		}
		
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.FFT")) {
				FFT fft = (FFT)context.getViewList().get(i);
				fft.setOverlap(value/100);
			}

		}
		
	}
	//��ȡƵ�ʷֱ���
	public float getFreqRes() {
		String freqResData=freqRes_Spinner.getSelectedItem().toString();
		if(freqResData.matches("[0-9]+|[0.9]+.[0-9]+")){
			return Float.valueOf(freqResData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_freqRes_wrong, Toast.LENGTH_SHORT);
			
			return Float.parseFloat(getActivity().getSharedPreferences("hz_5D", 0).getString("FFT_FreqRes", "0.05"));
		}
	}
	//��ȡƽ��
	public int getAveraging() {
		String avgData=averaging_Edit.getText().toString();
		if(avgData.matches("[0-9]+")){
			return Integer.valueOf(avgData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_averaging_wrong, Toast.LENGTH_SHORT);
			return 3;
		}
	}
	//��ȡ�Ӵ�Spinner��position
	public int getWindowPosition() {
		return window_Spinner.getSelectedItemPosition();
	}
	
	public void setAcquiFreq(int acquiFreq){
		/*
		 * Ƶ�ʷ�Χ��12800��6400��3200,1600,800,400
		 *		  25600��12800,6400��3200,1600,800,400
		 *		  51200��25600��12800,6400��3200,1600,800
		 *		  96000��48000��25600��12800,6400��3200,1600
		 *		  102400��51200��48000��25600��12800,6400��3200
		 */
		ArrayList<String> freqRang_spinner_list = new ArrayList<String>();

		for(int i=6;i>0;i--){
			if(acquiFreq/(int)Math.pow(2,i)>=400){
				freqRang_spinner_list.add(String.valueOf(acquiFreq/Math.pow(2,i)/1000));//
			}
		}
		CustomSpinnerArrayAdapter adapter = new CustomSpinnerArrayAdapter(getActivity(), freqRang_spinner_list);
		adapter.notifyDataSetChanged();
		freqRang_Spinner.setAdapter(adapter);
		freqRang_Spinner.setSelection(0,true);
		
		MainActivity context = (MainActivity)getActivity();
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.FFT")) {
				FFT fft = (FFT)context.getViewList().get(i);
				fft.setAcquiFreq(acquiFreq);
			}

		}

	}
	
	private void setFrepRangToMap(){
		float freqRang = Float.parseFloat(freqRang_Spinner.getSelectedItem().toString());
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.FFT")) {
				FFT fft = (FFT)context.getViewList().get(i);
				fft.setFreqRang(freqRang*1000);
			}

		}
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.FFT_Freq_Range)).setText(R.string.FreqRange);
		((TextView)view.findViewById(R.id.Overlap)).setText(R.string.Overlap);
		((TextView)view.findViewById(R.id.Freq_Weight)).setText(R.string.FreqWeight);
		((TextView)view.findViewById(R.id.Freq_Res)).setText(R.string.FreqResolution);
		((TextView)view.findViewById(R.id.X_Axis)).setText(R.string.X_Axis);
		((TextView)view.findViewById(R.id.Y_Axis)).setText(R.string.Y_Axis);
		((TextView)view.findViewById(R.id.Window)).setText(R.string.Window);
		((TextView)view.findViewById(R.id.Averaging)).setText(R.string.Averaging);
	}

	public float getFreqRang() {
		// TODO Auto-generated method stub
		return Float.parseFloat(freqRang_Spinner.getSelectedItem().toString())*1000;
	}
	
	public float getOverlap(){
		float value;
		String overlapData=overlap_Edit.getText().toString();
		if(overlapData.matches("[0-9]+|[0.9]+.[0-9]+")){
			value = Float.valueOf(overlapData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_overlap_wrong, Toast.LENGTH_SHORT);
			value = 50;
		}
		
		return value/100;
	}

	public String getWindowType() {
		// TODO Auto-generated method stub
		return window_Spinner.getSelectedItem().toString();
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.FFT_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_fft,Color.YELLOW));
		view.findViewById(R.id.FFT_Layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_fft,Color.YELLOW));
		ImageButton Back_Fft = (ImageButton) view.findViewById(R.id.Back_Fft);
		Back_Fft.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		view.findViewById(R.id.FFT_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		TextView fft_title = (TextView) view.findViewById(R.id.FFT_title);
		fft_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView FFT_Freq_Range = (TextView) view.findViewById(R.id.FFT_Freq_Range);
		FFT_Freq_Range.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
//		Spinner FFTFreqRange_spinner = (Spinner) view.findViewById(R.id.FFTFreqRange_spinner);
//		FFTFreqRange_spinner.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView FreqRange_KHz = (TextView) view.findViewById(R.id.FreqRange_KHz);
		FreqRange_KHz.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView Overlap = (TextView) view.findViewById(R.id.Overlap);
		Overlap.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		//EditText FFTOverlap_edit = (EditText) view.findViewById(R.id.FFTOverlap_edit);
		//FFTOverlap_edit.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Overlap_percent = (TextView) view.findViewById(R.id.Overlap_percent);
		Overlap_percent.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
	
		
		TextView Freq_Res = (TextView) view.findViewById(R.id.Freq_Res);
		Freq_Res.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
//		Spinner FFTFreqRes_spinner = (Spinner) view.findViewById(R.id.FFTFreqRes_spinner);
//		FFTFreqRes_spinner.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView FreqRes_Hz = (TextView) view.findViewById(R.id.FreqRes_Hz);
		FreqRes_Hz.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView Freq_Weight_fft = (TextView) view.findViewById(R.id.Freq_Weight);
		Freq_Weight_fft.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
//		Spinner FFTFreqWeight_spinner = (Spinner) view.findViewById(R.id.FFTFreqWeight_spinner);
//		FFTFreqWeight_spinner.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		
		TextView Averaging = (TextView) view.findViewById(R.id.Averaging);
		Averaging.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		EditText FFTAveraging_edit = (EditText) view.findViewById(R.id.FFTAveraging_edit);
		//FFTAveraging_edit.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView Window = (TextView) view.findViewById(R.id.Window);
		Window.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
//		Spinner FFTWindow_spinner = (Spinner) view.findViewById(R.id.FFTWindow_spinner);
//		FFTWindow_spinner.getSelectedItem().(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView X_Axis_AI = (TextView) view.findViewById(R.id.X_Axis);
		X_Axis_AI.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
//		Spinner FFTXAxis_spinner = (Spinner) view.findViewById(R.id.FFTXAxis_spinner);
//		FFTXAxis_spinner.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Y_Axis_AI = (TextView) view.findViewById(R.id.Y_Axis);
		Y_Axis_AI.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
//		Spinner FFTYAxis_spinner = (Spinner) view.findViewById(R.id.FFTYAxis_spinner);
//		FFTYAxis_spinner.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		
		Spinner FFTFreqRange_spinner = (Spinner) view.findViewById(R.id.FFTFreqRange_spinner);
		if((TextView)FFTFreqRange_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)FFTFreqRange_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)FFTFreqRange_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner FFTFreqWeight_spinner = (Spinner) view.findViewById(R.id.FFTFreqWeight_spinner);
		if((TextView)FFTFreqWeight_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)FFTFreqWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)FFTFreqWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner FFTWindow_spinner = (Spinner) view.findViewById(R.id.FFTWindow_spinner);
		if((TextView)FFTWindow_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)FFTWindow_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)FFTWindow_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner FFTXAxis_spinner = (Spinner) view.findViewById(R.id.FFTXAxis_spinner);
		if((TextView)FFTXAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)FFTXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)FFTXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		
		Spinner FFTYAxis_spinner = (Spinner) view.findViewById(R.id.FFTYAxis_spinner);
		if((TextView)FFTYAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)FFTYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)FFTYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		
		Spinner FFTFreqRes_spinner = (Spinner) view.findViewById(R.id.FFTFreqRes_spinner);
		if((TextView)FFTFreqRes_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)FFTFreqRes_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)FFTFreqRes_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
	}
}
