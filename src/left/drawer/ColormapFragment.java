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
import android.widget.CheckBox;
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

import draw.map.ColorMap;
import draw.map.FFT;

public class ColormapFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener, OnFocusChangeListener {

	private View view = null;
	private Spinner freqWeight_Spinner = null;
	private Spinner xAxis_Spinner = null;
	private Spinner yAxis_Spinner = null;
	private Switch colorMapSwitch = null;
	private SpinnerValues spinnerValues;

	private String xAxisStr;
	private String yAxisStr;
	private String switchStr;
	private String freqWeightStr;
	private String colorMapFreqRangeStr;
	private String colorMapFreqResStr;
	private String  colorMapWindowStr;
	private String colorMapAveragingStr;
	private String colorMapOverlapStr;
	private String acquiFreqStr;

	private int selectedItemPosition;
	private SharedPreferences.Editor editor;
	private Spinner colorMapFreqRange_spinner;
	//private ArrayAdapter<String> colorMapFreqRangeAdapter;
	private Spinner colorMapFreqRes_spinner;
	private ArrayAdapter<String> colorMapFreqResAdapter;
//	private ArrayList<String> unitsList = new ArrayList<String>();
//	private ArrayAdapter<String> adapter;
	private ArrayList<String> colorMapFreqRes_spinner_list = new ArrayList<String>();//Ƶ�ʷֱ���Spinner��List����
	private Spinner colorMapWindow_spinner;
	private EditText colorMapOverlap_edit;
	private EditText colorMapAveraging_edit;
	public ColormapFragment() {

	}

	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.colormap, container, false);
		view.setOnClickListener(this);
		view.findViewById(R.id.Back_Colormap).setOnClickListener(this);
		freqWeight_Spinner = (Spinner) view.findViewById(R.id.ColorMapFreqWeight_spinner);
		colorMapFreqRange_spinner = (Spinner) view.findViewById(R.id.colorMapFreqRange_spinner);
		
		colorMapFreqRes_spinner = (Spinner) view.findViewById(R.id.colorMapFreqRes_spinner);
		
		colorMapFreqResAdapter = new CustomSpinnerArrayAdapter(getActivity(), colorMapFreqRes_spinner_list);
		setSpinnerList(colorMapFreqRange_spinner.getSelectedItem().toString());
		colorMapFreqResAdapter.notifyDataSetChanged();
		colorMapFreqRes_spinner.setAdapter(colorMapFreqResAdapter);
		
		colorMapWindow_spinner = (Spinner) view.findViewById(R.id.colorMapWindow_spinner);
		ArrayAdapter<String> mAdapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Window));
		colorMapWindow_spinner.setAdapter(mAdapter);
		colorMapOverlap_edit  = (EditText) view.findViewById(R.id.colorMapOverlap_edit);
		colorMapAveraging_edit = (EditText) view.findViewById(R.id.colorMapAveraging_edit);
		xAxis_Spinner = (Spinner) view.findViewById(R.id.ColorMapXAxis_spinner);
		selectedItemPosition=xAxis_Spinner.getSelectedItemPosition();
		yAxis_Spinner = (Spinner) view.findViewById(R.id.ColorMapYAxis_spinner);
		colorMapSwitch = (Switch) view.findViewById(R.id.ColorMapSwitchButton);
		colorMapSwitch.setThumbResource(R.drawable.switchtitle);
		colorMapSwitch.setSwitchTextAppearance(view.getContext(),R.color.black);
		colorMapWindow_spinner.setOnItemSelectedListener(this);
		freqWeight_Spinner.setOnItemSelectedListener(this);
		xAxis_Spinner.setOnItemSelectedListener(this);
		yAxis_Spinner.setOnItemSelectedListener(this);
		
		colorMapOverlap_edit.setOnFocusChangeListener(this);
		colorMapAveraging_edit.setOnFocusChangeListener(this);
		colorMapOverlap_edit.addTextChangedListener(textWatcherOverlap_Edit);
		colorMapAveraging_edit.addTextChangedListener(textWatcherAveraging_Edit);
		//unitsList.add("Pa");
		//unitsList.add("dB");
		//adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, unitsList);
		//yAxis_Spinner.setAdapter(adapter);
		init();
		colorMapFreqRange_spinner.setOnItemSelectedListener(this);	//��������Ҫ����init����֮��ִ��
		colorMapFreqRes_spinner.setOnItemSelectedListener(this);
		Switch();

		return view;
	}

	public void init() {
		
		setAcquiFreq(Integer.parseInt(acquiFreqStr));
		setSlectedItem(colorMapFreqRange_spinner, colorMapFreqRangeStr);
		colorMapWindow_spinner.setSelection(Integer.parseInt(colorMapWindowStr));
		setSlectedItem(freqWeight_Spinner, freqWeightStr);
		setSlectedItem(xAxis_Spinner, xAxisStr);
		setSlectedItem(yAxis_Spinner, yAxisStr);
		setSlectedItem(colorMapFreqRes_spinner, colorMapFreqResStr);


		if(!colorMapOverlapStr.matches("[0-9]+|[0.9]+.[0-9]+")){
			colorMapOverlapStr = "50";
			editor.putString("colorMapOverlap", "50");
			editor.commit();
		}
		colorMapOverlap_edit.setText(colorMapOverlapStr);
		
		if(!colorMapAveragingStr.matches("[0-9]+")){
			colorMapAveragingStr = "3";
			editor.putString("colorMapAveraging", "3");
			editor.commit();
		}
		
		colorMapAveraging_edit.setText(colorMapAveragingStr);
		setSwitch();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainFragment mainFragment = (MainFragment) getFragmentManager()
				.findFragmentByTag("main");
		ColormapFragment acquiFragment = (ColormapFragment) getFragmentManager()
				.findFragmentByTag("colormap");
		switch (v.getId()) {
		case R.id.Back_Colormap:
			getFragmentManager().beginTransaction().hide(acquiFragment)
					.show(mainFragment).commit();
			break;
		}
	}

	public void Switch() {

		colorMapSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
										.equals("Waterfall")
										&& DataCollection.isRecording) {
									colorMapSwitch.setChecked(true);
									Toast.makeText(
											getActivity()
													.getApplicationContext(),
											R.string.Collecting_data,
											Toast.LENGTH_SHORT)// ����������
											.show();
									return;
								}
							}
						}
						if (isChecked) {
							colorMapSwitchOpen();
						} else {
							colorMapSwitchOff();
						}

					}
				});
	}

	@SuppressLint("NewApi")
	private void colorMapSwitchOpen() {
		spinnerValues.addValues("Waterfall");
		colorMapSwitch.setTrackResource(R.drawable.switchbutton_on);
		// ����Ϣ�����ģ���ļ�
		editor.putString("Waterfall", "open");
		editor.putString("colorMapFreqRange", colorMapFreqRange_spinner
				.getSelectedItem().toString());
		editor.putString("colorMapFreqRes", colorMapFreqRes_spinner
				.getSelectedItem().toString());
		editor.putString("colorMapWindow", ""+colorMapWindow_spinner.getSelectedItemPosition());
		editor.putString("ColorMap_FreqWeight", freqWeight_Spinner
				.getSelectedItem().toString());
		editor.putString("colorMapOverlap", colorMapOverlap_edit.getText().toString());
		editor.putString("colorMapAveraging", colorMapAveraging_edit.getText()
				.toString());
		editor.putString("ColorMap_XAxis", xAxis_Spinner.getSelectedItem()
				.toString());
		editor.putString("ColorMap_YAxis", yAxis_Spinner.getSelectedItem()
				.toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
	}

	@SuppressLint("NewApi")
	private void colorMapSwitchOff() {
		spinnerValues.removeValues("Waterfall");
		colorMapSwitch.setTrackResource(R.drawable.switchbutton_off);
		// ��ģ���ļ��е���Ϣɾ��
		editor.putString("Waterfall", "close");
		editor.putString("colorMapFreqRange", colorMapFreqRange_spinner
				.getSelectedItem().toString());
		editor.putString("colorMapFreqRes", colorMapFreqRes_spinner
				.getSelectedItem().toString());
		editor.putString("colorMapWindow",""+colorMapWindow_spinner.getSelectedItemPosition());
		editor.putString("ColorMap_FreqWeight", freqWeight_Spinner
				.getSelectedItem().toString());
		editor.putString("colorMapOverlap", colorMapOverlap_edit.getText().toString());
		editor.putString("colorMapAveraging", colorMapAveraging_edit.getText()
				.toString());
		editor.putString("ColorMap_XAxis", xAxis_Spinner.getSelectedItem()
				.toString());
		editor.putString("ColorMap_YAxis", yAxis_Spinner.getSelectedItem()
				.toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
		removeAllView();
	}
	private TextWatcher textWatcherOverlap_Edit = new TextWatcher() {  
		  
		@Override  
		public void onTextChanged(CharSequence s, int start, int before,  
		int count) {  
			int point = 0;
			for (int i = 0; i < colorMapOverlap_edit.getText().toString().length(); i++) {
				// if(order_edit.getText().toString().indexOf(".")!=-1){
				if (colorMapOverlap_edit.getText().toString().charAt(i) == '.')
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
			if(!colorMapOverlap_edit.getText().toString().equals("")&&Float.parseFloat(colorMapOverlap_edit.getText().toString())>(float)99.99){
				colorMapOverlap_edit.setText(preference.getString("colorMapOverlap", "50"));
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
			for (int i = 0; i < colorMapAveraging_edit.getText().toString().length(); i++) {
				// if(order_edit.getText().toString().indexOf(".")!=-1){
				if (colorMapAveraging_edit.getText().toString().charAt(i) == '.')
					point = point + 1;
				// }
			}
			if (point > 1) {
				Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT).show();
				return;
			}
			SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
			//average��1 - 1e09
			if(!colorMapAveraging_edit.getText().toString().equals("")){
				if(Float.parseFloat(colorMapAveraging_edit.getText().toString())<1
						||Float.parseFloat(colorMapAveraging_edit.getText().toString())>(float)Math.pow(10,9)){
					colorMapAveraging_edit.setText(preference.getString("colorMapAveraging", ""));
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
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// if(!colorMapSwitch.isChecked()) return;
		// ʵʱ����ģ����Ϣ
		// SharedPreferences preference =
		// getActivity().getSharedPreferences("hz_5D", 0);
		// SharedPreferences.Editor editor = preference.edit();
		switch (parent.getId()) {

		case R.id.colorMapFreqRange_spinner:
			TextView textView = (TextView) view;
			if(ThemeLogic.themeType==1){
				textView.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				textView.setTextColor(getResources().getColor(R.color.black));
			}
			textView.setGravity(Gravity.CENTER);
			editor.putString("colorMapFreqRange", colorMapFreqRange_spinner
				.getSelectedItem().toString());
			setFrepRangToMap();
			setSpinnerList(textView.getText().toString());
			colorMapFreqResAdapter.notifyDataSetChanged();
			
			
			if(colorMapFreqRes_spinner.getSelectedItemPosition() == 0) colorMapFreqRes_spinner.setSelection(1,true);
			colorMapFreqRes_spinner.setSelection(0, true);
			if(colorMapFreqResStr!=null){
				setSlectedItem(colorMapFreqRes_spinner, colorMapFreqResStr);
			}
			break;
		case R.id.colorMapFreqRes_spinner:
			TextView textView1 = (TextView) view;
			if(null == textView1) return;
			if(ThemeLogic.themeType==1){
				textView1.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				textView1.setTextColor(getResources().getColor(R.color.black));
			}
			textView1.setGravity(Gravity.CENTER);
			editor.putString("colorMapFreqRes", colorMapFreqRes_spinner
				.getSelectedItem().toString());
			
			setFftRes();
			
			break;
		case R.id.colorMapWindow_spinner:
			TextView textView2 = (TextView) view;
			if(ThemeLogic.themeType==1){
				textView2.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				textView2.setTextColor(getResources().getColor(R.color.black));
			}
			textView2.setGravity(Gravity.RIGHT);
			editor.putString("colorMapWindow", ""+colorMapWindow_spinner.getSelectedItemPosition());
			
			setFftWindow();
			break;
		case R.id.ColorMapFreqWeight_spinner:
			TextView tv = (TextView) view;
			if(ThemeLogic.themeType==1){
				tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
			tv.setGravity(Gravity.CENTER);
			editor.putString("ColorMap_FreqWeight", freqWeight_Spinner
				.getSelectedItem().toString());
			
			/*switch(position){
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
			}*/
			break;
		case R.id.ColorMapXAxis_spinner:
			TextView tv1 = (TextView) view;
			if(tv1==null){
				return;
			}
			if(ThemeLogic.themeType==1){
				tv1.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv1.setTextColor(getResources().getColor(R.color.black));
			}
			tv1.setGravity(Gravity.CENTER);
			SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
			String triggerMode = preference.getString("Trigger_Mode", "Time");
			 if(DataCollection.isRecording){
				 if(!tv1.getText().toString().equals("RPM")){
					 selectedItemPosition=position;
				 }
				 if(triggerMode.equals("Time")){
					 if(tv1.getText().toString().equals("RPM"))
						 Toast.makeText(getActivity(), R.string.Change_trigger_mode, Toast.LENGTH_SHORT).show();
						 xAxis_Spinner.setSelection(selectedItemPosition);
						 return;
				 }
			 }
			editor.putString("ColorMap_XAxis", xAxis_Spinner
						.getSelectedItem().toString());
			spinnerChangeState(0, xAxis_Spinner.getSelectedItem()
					.toString());
			
			break;
		case R.id.ColorMapYAxis_spinner:
			TextView tv2 = (TextView) view;
			if(tv2==null){
				return;
			}
			if(ThemeLogic.themeType==1){
				tv2.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv2.setTextColor(getResources().getColor(R.color.black));
			}
			tv2.setGravity(Gravity.CENTER);
			if (colorMapSwitch.isChecked()) {
				editor.putString("ColorMap_YAxis", yAxis_Spinner
						.getSelectedItem().toString());
				spinnerChangeState(1, yAxis_Spinner.getSelectedItem()
						.toString());
			}
			break;
		}
		editor.commit();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// if(!colorMapSwitch.isChecked()) return;
		// if(hasFocus) return;
		// //ʵʱ����ģ����Ϣ
		// SharedPreferences preference =
		// getActivity().getSharedPreferences("hz_5D", 0);
		// SharedPreferences.Editor editor = preference.edit();
		// switch(v.getId()) {
		// case R.id.TimeRange_edit:
		// editor.putString("ColorMap_timeRange",
		// timeRange_Edit.getText().toString());
		// break;
		// case R.id.FreqRes_edit:
		// editor.putString("ColorMap_FreqRes",
		// freqRes_Edit.getText().toString());
		// break;
		// case R.id.TimeRes_edit:
		// editor.putString("ColorMap_TimeRes",
		// timeRes_Edit.getText().toString());
		// break;
		// }
		// editor.commit();
		// if(!fftSwitch.isChecked()) return;
				if (hasFocus)
					return;
				// ʵʱ����ģ����Ϣ
				SharedPreferences preference = getActivity().getSharedPreferences(
						"hz_5D", 0);
				SharedPreferences.Editor editor = preference.edit();
				switch (v.getId()) {
				case R.id.colorMapOverlap_edit:
					editor.putString("colorMapOverlap", colorMapOverlap_edit.getText().toString());
					
					if(!colorMapOverlap_edit.getText().toString().matches("s*")){
						setFftOverlap();
					}
					break;
				case R.id.colorMapAveraging_edit:
					editor.putString("colorMapAveraging", colorMapAveraging_edit.getText()
							.toString());
					
					if(!colorMapOverlap_edit.getText().toString().matches("s*")){
						setFftAveraging();
					}
					break;
				}
				editor.commit();
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
		colorMapFreqRange_spinner.setAdapter(adapter);
		colorMapFreqRange_spinner.setSelection(0,true);
		
		MainActivity context = (MainActivity)getActivity();
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.ColorMap")) {
				ColorMap colorMap = (ColorMap)context.getViewList().get(i);
				colorMap.setAcquiFreq(acquiFreq);
			}

		}

	}
	public void setSpinnerList(String string) {
		if(null == string)
			return;
		float freqRange = Float.parseFloat(string)*1000;

		//DecimalFormat df = new DecimalFormat("###0.00");
		
		colorMapFreqRes_spinner_list.clear();
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
			if(ff>0.7&&colorMapFreqRes_spinner_list.size()<size){
				colorMapFreqRes_spinner_list.add(""+freqRange/Math.pow(2,i));
			}
		}
		

	}
	private void removeAllView() {
		CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
		for (int j = 0; j < ct.algorithm_spinnerList.size(); j++) {
			if(ct.algorithm_spinnerList.get(j).getText().equals("Waterfall")){
				ct.algorithm_spinnerList.get(j).setText(spinnerValues.getAlgorithmItems().get(0));
				((MainContextView) ct.mainContextViewList.get(j)).drawMap();
			}
		}
	}
	private void spinnerChangeState(int axisType,String unitType){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("Waterfall")){
				((MainContextView)ct.mainContextViewList.get(i)).setUnitChangeState(axisType,unitType);
			}
		}
	}

	public void readFromXml(FragmentActivity fragmentActivity) {

		SharedPreferences preference = fragmentActivity.getSharedPreferences(
				"hz_5D", 0);
		editor = preference.edit();
		xAxisStr = preference.getString("ColorMap_XAxis", "s");
		yAxisStr = preference.getString("ColorMap_YAxis", "Hz");
		switchStr = preference.getString("Waterfall", "close");
		freqWeightStr = preference.getString("ColorMap_FreqWeight", "close");
		colorMapFreqRangeStr = preference.getString("colorMapFreqRange", "0.4");
		colorMapFreqResStr = preference.getString("colorMapFreqRes", "0.78");
		colorMapWindowStr =preference.getString("colorMapWindow", "0");
		colorMapAveragingStr = preference.getString("colorMapAveraging", "3"); 
		colorMapOverlapStr = preference.getString("colorMapOverlap", "50");
		acquiFreqStr = preference.getString("AcquiFreq_spinner_values", "12800");

	}

	private void setSlectedItem(Spinner spinner, String xAxisStr) {
		SpinnerAdapter adapter = spinner.getAdapter();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			if (adapter.getItem(i).toString().equals(xAxisStr)) {
				spinner.setSelection(i,true);
				if(spinner.getId() ==R.id.colorMapFreqRange_spinner){
					setSpinnerList(adapter.getItem(i).toString());
					colorMapFreqResAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
	}

	private void setSwitch() {
		// TODO Auto-generated method stub
		if (switchStr.equals("open")) {
			colorMapSwitch.setChecked(true);
			colorMapSwitchOpen();
		} else {
			colorMapSwitch.setChecked(false);
			colorMapSwitchOff();
		}
	}
	public void languageChanged(){
		//((TextView)view.findViewById(R.id.ColorMap_title)).setText(R.string.ColorMap);
		((TextView)view.findViewById(R.id.calculation)).setText(R.string.calculation);
		((TextView)view.findViewById(R.id.display)).setText(R.string.Display);
		((TextView)view.findViewById(R.id.colorMap_Freq_Range)).setText(R.string.FreqRange);
		((TextView)view.findViewById(R.id.colorMapOverlap)).setText(R.string.Overlap);
		((TextView)view.findViewById(R.id.colorMapFreq_Res)).setText(R.string.FreqResolution);
		((TextView)view.findViewById(R.id.colorMapWindow)).setText(R.string.Window);
		((TextView)view.findViewById(R.id.colorMapAveraging)).setText(R.string.Averaging);
		((TextView)view.findViewById(R.id.Freq_Weight)).setText(R.string.FreqWeight);
		((TextView)view.findViewById(R.id.X_Axis)).setText(R.string.X_Axis);
		((TextView)view.findViewById(R.id.Y_Axis)).setText(R.string.Y_Axis);
		
	}

	public float getFreqRang() {
		// TODO Auto-generated method stub

		return Float.parseFloat(colorMapFreqRange_spinner.getSelectedItem().toString())*1000;
	}

	public float getOverlap() {
		// TODO Auto-generated method stub
		float value;
		String overlapData=colorMapOverlap_edit.getText().toString();

		if(overlapData.matches("[0-9]+|[0.9]+.[0-9]+")){
			value = Float.valueOf(overlapData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_overlap_wrong, Toast.LENGTH_SHORT);
			value = 50;
		}
		
		return value/100;
	}

	public float getFreqRes() {
		// TODO Auto-generated method stub
		String freqResData=colorMapFreqRes_spinner.getSelectedItem().toString();

		if(freqResData.matches("[0-9]+|[0.9]+.[0-9]+")){
			return Float.valueOf(freqResData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_freqRes_wrong, Toast.LENGTH_SHORT);
			
			return Float.parseFloat(getActivity().getSharedPreferences("hz_5D", 0).getString("colorMapFreqRes", "0.05"));
		}
	}

	public int getWindowPosition() {
		// TODO Auto-generated method stub
		return colorMapWindow_spinner.getSelectedItemPosition();

	}

	public int getAveraging() {
		// TODO Auto-generated method stub
		String avgData=colorMapAveraging_edit.getText().toString();
		if(avgData.matches("[0-9]+")){
			return Integer.valueOf(avgData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_averaging_wrong, Toast.LENGTH_SHORT);
			return 3;
		}
	}
	
	private void setFftRes() {
		// TODO Auto-generated method stub
		float value = Float.parseFloat(colorMapFreqRes_spinner.getSelectedItem().toString());
		
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.ColorMap")) {
				ColorMap colorMap = (ColorMap)context.getViewList().get(i);
				colorMap.setFreqRes(value);
			}

		}
	}
	
	private void setFftWindow() {
		// TODO Auto-generated method stub
		int value = colorMapWindow_spinner.getSelectedItemPosition();
		
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.ColorMap")) {
				ColorMap colorMap = (ColorMap)context.getViewList().get(i);
				colorMap.setWindowType(value);
			}
		}
	}
	
	public void setFftOverlap() {
		float value;
		String overlapData=colorMapOverlap_edit.getText().toString();
		if(overlapData.matches("[0-9]+|[0.9]+.[0-9]+")){
			value = Float.valueOf(overlapData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_overlap_wrong, Toast.LENGTH_SHORT);
			value = 50;
		}
		
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.ColorMap")) {
				ColorMap colorMap = (ColorMap)context.getViewList().get(i);
				colorMap.setOverlap(value/100);
			}

		}
		
	}
	private void setFftAveraging() {
		// TODO Auto-generated method stub
		int value = 3;
		String avgData=colorMapAveraging_edit.getText().toString();
		if(avgData.matches("[0-9]+")){
			value= Integer.valueOf(avgData);
			
		}else {
			Toast.makeText(getActivity(), R.string.fft_averaging_wrong, Toast.LENGTH_SHORT);
			value = 3;
		}
		
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.ColorMap")) {
				ColorMap colorMap = (ColorMap)context.getViewList().get(i);
				colorMap.setMean(value);
			}
		}
	}

	
	private void setFrepRangToMap(){
		float freqRang = Float.parseFloat(colorMapFreqRange_spinner.getSelectedItem().toString());
		MainActivity context = (MainActivity)getActivity();
		
		for (int i = 0; i < context.getViewList().size(); i ++) {
			if(context.getViewList().get(i).getClass().getName().equals("draw.map.ColorMap")) {
				ColorMap colorMap = (ColorMap)context.getViewList().get(i);
				colorMap.setFreqRang(freqRang*1000);
			}

		}
	}

	public String getWindowType() {
		// TODO Auto-generated method stub
		return colorMapWindow_spinner.getSelectedItem().toString();
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.ColorMap_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_colormap,Color.YELLOW));
		view.findViewById(R.id.Colormap_Layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_colormap,Color.YELLOW));
		view.findViewById(R.id.ColorMap_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		ImageButton Back_Colormap = (ImageButton) view.findViewById(R.id.Back_Colormap);
		Back_Colormap.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		TextView ColorMap_title = (TextView) view.findViewById(R.id.ColorMap_title);
		ColorMap_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView Freq_Weight_color = (TextView) view.findViewById(R.id.Freq_Weight);
		Freq_Weight_color.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//ColorMapFreqWeight_spinner
		CheckBox calculation = (CheckBox) view.findViewById(R.id.calculation);
		calculation.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		CheckBox display = (CheckBox) view.findViewById(R.id.display);
		display.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView X_Axis_color = (TextView) view.findViewById(R.id.X_Axis);
		X_Axis_color.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//ColorMapXAxis_spinner
		TextView Y_Axis_color = (TextView) view.findViewById(R.id.Y_Axis);
		Y_Axis_color.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//ColorMapYAxis_spinner
		
		
		TextView colorMap_Freq_Range = (TextView) view.findViewById(R.id.colorMap_Freq_Range);
		colorMap_Freq_Range.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView colorMapFreqRange_KHz = (TextView) view.findViewById(R.id.colorMapFreqRange_KHz);
		colorMapFreqRange_KHz.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView colorMapOverlap = (TextView) view.findViewById(R.id.colorMapOverlap);
		colorMapOverlap.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView colorMapOverlap_percent = (TextView) view.findViewById(R.id.colorMapOverlap_percent);
		colorMapOverlap_percent.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView colorMapFreq_Res = (TextView) view.findViewById(R.id.colorMapFreq_Res);
		colorMapFreq_Res.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView colorMapFreqRes_Hz = (TextView) view.findViewById(R.id.colorMapFreqRes_Hz);
		colorMapFreqRes_Hz.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView colorMapAveraging = (TextView) view.findViewById(R.id.colorMapAveraging);
		colorMapAveraging.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView colorMapWindow = (TextView) view.findViewById(R.id.colorMapWindow);
		colorMapWindow.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		Spinner ColorMapFreqWeight_spinner = (Spinner) view.findViewById(R.id.ColorMapFreqWeight_spinner);
		if((TextView)ColorMapFreqWeight_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)ColorMapFreqWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)ColorMapFreqWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		Spinner colorMapFreqRange_spinner = (Spinner) view.findViewById(R.id.colorMapFreqRange_spinner);
		if((TextView)colorMapFreqRange_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)colorMapFreqRange_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)colorMapFreqRange_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		Spinner colorMapFreqRes_spinner = (Spinner) view.findViewById(R.id.colorMapFreqRes_spinner);
		if((TextView)colorMapFreqRes_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)colorMapFreqRes_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)colorMapFreqRes_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		Spinner colorMapWindow_spinner = (Spinner) view.findViewById(R.id.colorMapWindow_spinner);
		if((TextView)colorMapWindow_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)colorMapWindow_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)colorMapWindow_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		Spinner ColorMapXAxis_spinner = (Spinner) view.findViewById(R.id.ColorMapXAxis_spinner);
		if((TextView)ColorMapXAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)ColorMapXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)ColorMapXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		Spinner ColorMapYAxis_spinner = (Spinner) view.findViewById(R.id.ColorMapYAxis_spinner);
		if((TextView)ColorMapYAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)ColorMapYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)ColorMapYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
	}
}
