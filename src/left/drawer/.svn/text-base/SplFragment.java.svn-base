package left.drawer;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import common.XclSingalTransfer;

public class SplFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	private View view=null;
	private Spinner freqWeight_Spinner = null;
	private Spinner timeWeight_Spinner = null;
	private Spinner xAxis_Spinner = null;
	private Spinner yAxis_Spinner = null;
	private Switch splSwitch = null;
	private SpinnerValues spinnerValues;
	
	private String xAxisStr;
	private String yAxisStr;
	private String switchStr;
	private String timeWeightStr;
	private String freqWeightStr;
	private int selectedItemPosition;
	private ArrayList<String> unitsList = new ArrayList<String>(); 
	private ArrayAdapter<String> adapter;
	private SharedPreferences.Editor editor;
	
	
	public SplFragment() {
		
	}
	
	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.spl, container,false);
		view.setOnClickListener(this);
		view.findViewById(R.id.Back_Spl).setOnClickListener(this);
		freqWeight_Spinner = (Spinner) view.findViewById(R.id.SPLFreqWeight_spinner);
		adapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Freq_Weight));
		adapter.notifyDataSetChanged();
		freqWeight_Spinner.setAdapter(adapter);
		timeWeight_Spinner = (Spinner) view.findViewById(R.id.SPLTimeWeight_spinner);
		xAxis_Spinner = (Spinner) view.findViewById(R.id.SPLXAxis_spinner);
		selectedItemPosition=xAxis_Spinner.getSelectedItemPosition();
		yAxis_Spinner = (Spinner) view.findViewById(R.id.SPLYAxis_spinner);
		adapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.SplOrColor_X_Axis));
		adapter.notifyDataSetChanged();
		xAxis_Spinner.setAdapter(adapter);
		splSwitch = (Switch) view.findViewById(R.id.SPLSwitchButton);
		splSwitch.setThumbResource(R.drawable.switchtitle);
		splSwitch.setSwitchTextAppearance(view.getContext(),R.color.black);
		
		timeWeight_Spinner.setOnItemSelectedListener(this);
		unitsList.add("Pa");
		unitsList.add("dB");
		adapter = new CustomSpinnerArrayAdapter(getActivity(), unitsList);
		yAxis_Spinner.setAdapter(adapter);
		
		init();
		
		xAxis_Spinner.setOnItemSelectedListener(this);
		yAxis_Spinner.setOnItemSelectedListener(this);
		freqWeight_Spinner.setOnItemSelectedListener(this);
		
		Switch();

		return view;
	}
	
	public void init(){
		setSlectedItem(freqWeight_Spinner,freqWeightStr);
		setSlectedItem(timeWeight_Spinner,timeWeightStr);
		setSlectedItem(xAxis_Spinner,xAxisStr);
		setSlectedItem(yAxis_Spinner,yAxisStr);
		setSwitch();
	}
	
	/*
	 * ���Ը���
	 */
/*	public void refresh(){
		SPL_title.setText(R.string.SPL);
		TextView Freq_Weight=(TextView)view.findViewById(R.id.Freq_Weight);
		Freq_Weight.setText(R.string.FreqWeight);
		TextView Time_Weight=(TextView)view.findViewById(R.id.Time_Weight);
		Time_Weight.setText(R.string.TimeWeight);
		TextView X_Axis =(TextView)view.findViewById(R.id.X_Axis);
		X_Axis.setText(R.string.X_Axis);
		TextView Y_Axis =(TextView)view.findViewById(R.id.Y_Axis);
		Y_Axis.setText(R.string.Y_Axis);
	}*/
	@Override
	public void onClick(View v) {
		MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("main");
		SplFragment acquiFragment = (SplFragment) getFragmentManager().findFragmentByTag("spl");
		switch(v.getId()){
		case R.id.Back_Spl:
			getFragmentManager().beginTransaction().hide(acquiFragment).show(mainFragment).commit();
			break;
		}
	}
	public void Switch(){
		splSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, 
			                boolean isChecked)  {
//				SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
//				SharedPreferences.Editor editor = preference.edit();
				CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
				if(ct.algorithm_spinnerList.size()!=0){
				for(int j=0;j<ct.algorithm_spinnerList.size();j++){
				if(ct.algorithm_spinnerList.get(j).getText().equals("SPL")&&DataCollection.isRecording){
					splSwitch.setChecked(true);
					Toast.makeText(getActivity().getApplicationContext(),
							 R.string.Collecting_data, Toast.LENGTH_SHORT)//����������
							.show();
					return;
				}
				}
				}
				if(isChecked){
					splSwitchOpen();
				}else{
					splSwitchOff();
				}
				
			}
		});
	}

	
	@SuppressLint("NewApi")
	private void splSwitchOpen() {
		spinnerValues.addValues("SPL");
		splSwitch.setTrackResource(R.drawable.switchbutton_on);
		//����Ϣ�����ģ���ļ�
		editor.putString("SPL", "open");
		editor.putString("SPL_FreqWeight", freqWeight_Spinner.getSelectedItem().toString());
		editor.putString("SPL_TimeWeight", timeWeight_Spinner.getSelectedItem().toString());
		editor.putString("SPL_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("SPL_YAxis", yAxis_Spinner.getSelectedItem().toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
	}
	
	@SuppressLint("NewApi")
	private void splSwitchOff() {
		
		spinnerValues.removeValues("SPL");
		splSwitch.setTrackResource(R.drawable.switchbutton_off);
		//��ģ���ļ��е���Ϣɾ��
		editor.putString("SPL", "close");
		editor.putString("SPL_FreqWeight", freqWeight_Spinner.getSelectedItem().toString());
		editor.putString("SPL_TimeWeight", timeWeight_Spinner.getSelectedItem().toString());
		editor.putString("SPL_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("SPL_YAxis", yAxis_Spinner.getSelectedItem().toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
		removeAllView();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
//		if(!splSwitch.isChecked()) return;
		//ʵʱ����ģ����Ϣ
		/*SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
		SharedPreferences.Editor editor = preference.edit();*/
		
		switch(parent.getId()) {
		case R.id.SPLFreqWeight_spinner:			
			TextView tv = (TextView)view;
			 if(tv==null){
				 return;
			 }
			 if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
            tv.setGravity(Gravity.CENTER);				
			
			editor.putString("SPL_FreqWeight", freqWeight_Spinner.getSelectedItem().toString());
			editor.commit();

			XclSingalTransfer xclSingalTransfer=XclSingalTransfer.getInstance();
			if(xclSingalTransfer.containsKey("splParamHandler")){
				Handler splParamHandler=(Handler)xclSingalTransfer.getValue("splParamHandler");
				Message msg=splParamHandler.obtainMessage();
				msg.what=0;
				msg.arg1=position;
				splParamHandler.sendMessage(msg);

			}
			setYUnitList(position);

			break;
		case R.id.SPLTimeWeight_spinner:
	        
	 		  TextView tv1 = (TextView)view;
	 		 if(tv1==null){
				 return;
			 }
	 		if(ThemeLogic.themeType==1){
				 tv1.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv1.setTextColor(getResources().getColor(R.color.black));
			}
	             tv1.setGravity(Gravity.CENTER);
				editor.putString("SPL_TimeWeight", timeWeight_Spinner.getSelectedItem().toString());
				editor.commit();

				XclSingalTransfer xclSingalTransfer1=XclSingalTransfer.getInstance();
				if(xclSingalTransfer1.containsKey("splParamHandler")){
					Handler splParamHandler=(Handler)xclSingalTransfer1.getValue("splParamHandler");
					Message msg=splParamHandler.obtainMessage();
					msg.what=1;
					msg.arg1=position;
					splParamHandler.sendMessage(msg);
				}
				
			break;
		case R.id.SPLXAxis_spinner:
			 TextView tv2 = (TextView)view;
			 if(tv2==null){
				 return;
			 }
			 if(ThemeLogic.themeType==1){
				 tv2.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv2.setTextColor(getResources().getColor(R.color.black));
			}
             tv2.setGravity(Gravity.CENTER);
             SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
			 String triggerMode = preference.getString("Trigger_Mode", "Time");
			 if(DataCollection.isRecording){
				 if(!tv2.getText().toString().equals("RPM")){
					 selectedItemPosition=position;
				 }
				
				 if(triggerMode.equals("Time")){
					 if(tv2.getText().toString().equals("RPM"))
						 Toast.makeText(getActivity(), R.string.Change_trigger_mode, Toast.LENGTH_SHORT).show();
						 xAxis_Spinner.setSelection(selectedItemPosition);
						 return;
				 }
			 }
		     editor.putString("SPL_XAxis", xAxis_Spinner.getSelectedItem().toString());
            
			spinnerChangeState(0,xAxis_Spinner.getSelectedItem().toString());
			break;
		case R.id.SPLYAxis_spinner:
			 TextView tv3 = (TextView)view;
			 if(tv3==null){
				 return;
			 }
			 if(ThemeLogic.themeType==1){
				 tv3.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv3.setTextColor(getResources().getColor(R.color.black));
			}
             tv3.setGravity(Gravity.CENTER);
			     editor.putString("SPL_YAxis", yAxis_Spinner.getSelectedItem().toString());
			
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

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	private void removeAllView(){
		CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
		for(int j=0;j<ct.algorithm_spinnerList.size();j++){
			if(ct.algorithm_spinnerList.get(j).getText().equals("SPL")){
				ct.algorithm_spinnerList.get(j).setText(spinnerValues.getAlgorithmItems().get(0));
				((MainContextView)ct.mainContextViewList.get(j)).drawMap();
			}
		}
	}
	private void spinnerChangeState(int axisType,String unitType){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("SPL")){
				((MainContextView)ct.mainContextViewList.get(i)).setUnitChangeState(axisType,unitType);
			}
		}
	}

	public void readFromXml(FragmentActivity fragmentActivity) {
		SharedPreferences preference = fragmentActivity.getSharedPreferences("hz_5D", 0);
		editor = preference.edit();
		xAxisStr = preference.getString("SPL_XAxis", "s");
		yAxisStr = preference.getString("SPL_YAxis", "Pa");
		timeWeightStr = preference.getString("SPL_TimeWeight", "Fast");
		freqWeightStr = preference.getString("SPL_FreqWeight", "None");
		switchStr = preference.getString("SPL", "close");
	}
	
	private void setSlectedItem(Spinner  spinner, String xAxisStr) {
		SpinnerAdapter adapter = spinner.getAdapter();
		int count = adapter.getCount();
		for(int i = 0; i<count;i++ ){
			if(adapter.getItem(i).toString().equals(xAxisStr)){
				spinner.setSelection(i,true);
				if(spinner.getId() == R.id.SPLFreqWeight_spinner){
					setYUnitList(i);
				}
				break;
			}
		}
	}
	private void setSwitch() {
		if(null == switchStr)
			return;
		
		if(switchStr.equals("open")) {
			splSwitch.setChecked(true);
			splSwitchOpen();
		}
		else{
			splSwitch.setChecked(false);
			splSwitchOff();
		}
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.Freq_Weight)).setText(R.string.FreqWeight);
		((TextView)view.findViewById(R.id.Time_Weight)).setText(R.string.TimeWeight);
		((TextView)view.findViewById(R.id.X_Axis)).setText(R.string.X_Axis);
		((TextView)view.findViewById(R.id.Y_Axis)).setText(R.string.Y_Axis);
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.SPL_Layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_spl,Color.YELLOW));
		RelativeLayout spl = 	(RelativeLayout) view.findViewById(R.id.SPL_layout);//
		spl.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_spl,Color.YELLOW));
		
		view.findViewById(R.id.SPL_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		TextView SPL_title = (TextView) view.findViewById(R.id.SPL_title);
		ImageButton Back_Spl = (ImageButton) view.findViewById(R.id.Back_Spl);
		Back_Spl.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		SPL_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Freq_Weight = (TextView) view.findViewById(R.id.Freq_Weight);
		Freq_Weight.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//SPLFreqWeight_spinner
		TextView Time_Weight = (TextView) view.findViewById(R.id.Time_Weight);
		Time_Weight.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//SPLTimeWeight_spinner
		TextView X_Axis_spl = (TextView) view.findViewById(R.id.X_Axis);
		X_Axis_spl.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//SPLXAxis_spinner
		TextView Y_Axis_spl = (TextView) view.findViewById(R.id.Y_Axis);
		Y_Axis_spl.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		Spinner SPLFreqWeight_spinner = (Spinner) view.findViewById(R.id.SPLFreqWeight_spinner);
		if((TextView)SPLFreqWeight_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)SPLFreqWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)SPLFreqWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		Spinner SPLTimeWeight_spinner = (Spinner) view.findViewById(R.id.SPLTimeWeight_spinner);
		if((TextView)SPLTimeWeight_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)SPLTimeWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)SPLTimeWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		Spinner SPLXAxis_spinner = (Spinner) view.findViewById(R.id.SPLXAxis_spinner);
		if((TextView)SPLXAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)SPLXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)SPLXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		Spinner SPLYAxis_spinner = (Spinner) view.findViewById(R.id.SPLYAxis_spinner);
		if((TextView)SPLYAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)SPLYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)SPLYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
	}
}
