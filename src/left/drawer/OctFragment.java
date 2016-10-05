package left.drawer;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

public class OctFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	private View view=null;
	private Spinner freqWeight_Spinner=null;
	private Spinner bandType_Spinner = null;
	private Spinner xAxis_Spinner = null;
	private Spinner yAxis_Spinner = null;
	private Switch octSwitch = null;
	private SpinnerValues spinnerValues;
	
	private String xAxisStr;
	private String yAxisStr;
	private String switchStr;
	private String freqWeightStr;
//	private String freqRangeStr;
//	private String upperStr;
//	private String lowerStr;
	private String bandTypeStr; 
	
	private ArrayList<String> unitsList = new ArrayList<String>(); 
	private ArrayAdapter<String> adapter;
	private SharedPreferences.Editor editor;
	
	public OctFragment() {
		
	}
	
	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.oct, container,false);
		view.setOnClickListener(this);

		view.findViewById(R.id.Back_Oct).setOnClickListener(this);
		freqWeight_Spinner = (Spinner) view.findViewById(R.id.OCTFreqWeight_spinner);
		adapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Freq_Weight));
		adapter.notifyDataSetChanged();
		freqWeight_Spinner.setAdapter(adapter);
		bandType_Spinner = (Spinner) view.findViewById(R.id.OCTBandType_spinner);
		xAxis_Spinner = (Spinner) view.findViewById(R.id.OCTXAxis_spinner);
		adapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Hz));
		adapter.notifyDataSetChanged();
		xAxis_Spinner.setAdapter(adapter);
		yAxis_Spinner = (Spinner) view.findViewById(R.id.OCTYAxis_spinner);
		octSwitch = (Switch) view.findViewById(R.id.OCTSwitchButton);
		octSwitch.setThumbResource(R.drawable.switchtitle);
		octSwitch.setSwitchTextAppearance(view.getContext(),R.color.black);
		
		bandType_Spinner.setOnItemSelectedListener(this);
		

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
		setSlectedItem(bandType_Spinner,bandTypeStr);
		setSlectedItem(freqWeight_Spinner,freqWeightStr);
		setSlectedItem(xAxis_Spinner,xAxisStr);
		setSlectedItem(yAxis_Spinner,yAxisStr);
		setSwitch();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("main");
		OctFragment acquiFragment = (OctFragment) getFragmentManager().findFragmentByTag("oct");
		switch(v.getId()){
		case R.id.Back_Oct:
			getFragmentManager().beginTransaction().hide(acquiFragment).show(mainFragment).commit();
			break;
		}
	}
	public void Switch(){
		
		
		octSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, 
			                boolean isChecked)  {
				SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
				editor = preference.edit();
				CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
				if(ct.algorithm_spinnerList.size()!=0){
				for(int j=0;j<ct.algorithm_spinnerList.size();j++){
				if(ct.algorithm_spinnerList.get(j).getText().equals("OCT")&&DataCollection.isRecording){
					octSwitch.setChecked(true);
					Toast.makeText(getActivity().getApplicationContext(),
							 R.string.Collecting_data, Toast.LENGTH_SHORT)//����������
							.show();
					return;
				}
				}
				}
				if(isChecked){

					octSwitchOpen();

				}else{

					octSwitchOff();

				}
				
			}
		});
	}

	
	@SuppressLint("NewApi")
	private void octSwitchOff() {
		
		spinnerValues.removeValues("OCT");
		octSwitch.setTrackResource(R.drawable.switchbutton_off);
		//��ģ���ļ��е���Ϣɾ��
		editor.putString("OCT", "close");
		editor.putString("OCT_FreqWeight",freqWeight_Spinner.getSelectedItem().toString());
		editor.putString("OCT_BandType", bandType_Spinner.getSelectedItem().toString());
		editor.putString("OCT_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("OCT_YAxis", yAxis_Spinner.getSelectedItem().toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
		removeAllView();
		
	}
	@SuppressLint("NewApi")
	private void octSwitchOpen() {
		spinnerValues.addValues("OCT");
		octSwitch.setTrackResource(R.drawable.switchbutton_on);
		//����Ϣ�����ģ���ļ�
		editor.putString("OCT", "open");
		editor.putString("OCT_FreqWeight",freqWeight_Spinner.getSelectedItem().toString());
		editor.putString("OCT_BandType", bandType_Spinner.getSelectedItem().toString());
		editor.putString("OCT_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("OCT_YAxis", yAxis_Spinner.getSelectedItem().toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
		
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
//		if(!octSwitch.isChecked()) return;
		//ʵʱ����ģ����Ϣ
		
//		  SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
//		  SharedPreferences.Editor editor = preference.edit();
		switch(parent.getId()) {
		case R.id.OCTBandType_spinner:
			 TextView tv = (TextView)view;
			 if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
             tv.setGravity(Gravity.CENTER);
			 editor.putString("OCT_BandType", bandType_Spinner.getSelectedItem().toString());             
			break;
		case R.id.OCTFreqWeight_spinner:
			TextView tv1 = (TextView)view;
			if(ThemeLogic.themeType==1){
				 tv1.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv1.setTextColor(getResources().getColor(R.color.black));
			}
            tv1.setGravity(Gravity.CENTER);
			 editor.putString("OCT_FreqWeight", freqWeight_Spinner.getSelectedItem().toString());
			 setYUnitList(position);
           
			break;
		case R.id.OCTXAxis_spinner:
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
			editor.putString("OCT_XAxis", xAxis_Spinner.getSelectedItem().toString());
			spinnerChangeState(0,xAxis_Spinner.getSelectedItem().toString());
			break;
		case R.id.OCTYAxis_spinner:
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
			editor.putString("OCT_YAxis", yAxis_Spinner.getSelectedItem().toString());
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
		// TODO Auto-generated method stub
		
	}
	private void removeAllView(){
		CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
		for(int j=0;j<ct.algorithm_spinnerList.size();j++){
			if(ct.algorithm_spinnerList.get(j).getText().equals("OCT")){
				ct.algorithm_spinnerList.get(j).setText(spinnerValues.getAlgorithmItems().get(0));
				((MainContextView)ct.mainContextViewList.get(j)).drawMap();
			}
		}
	}
	private void spinnerChangeState(int axisType,String unitType){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("OCT")){
				((MainContextView)ct.mainContextViewList.get(i)).setUnitChangeState(axisType,unitType);
			}
		}
	}

	public void readFromXml(FragmentActivity fragmentActivity) {
		SharedPreferences preference = fragmentActivity.getSharedPreferences("hz_5D", 0);
		editor = preference.edit();
		xAxisStr = preference.getString("OCT_XAxis", "Hz");
		yAxisStr = preference.getString("OCT_YAxis", "Pa");
		switchStr = preference.getString("OCT", "close");
		freqWeightStr = preference.getString("OCT_FreqWeight","None");
//		freqRangeStr =  preference.getString("OCT_FreqRange", "��1");
//		upperStr = preference.getString("OCT_Upper", "��1");
//		lowerStr = preference.getString("OCT_Lower", "��1");
		bandTypeStr = preference.getString("OCT_BandType", "��1");
	}
	
	private void setSlectedItem(Spinner  spinner, String xAxisStr) {
		SpinnerAdapter adapter = spinner.getAdapter();
		int count = adapter.getCount();
		for(int i = 0; i<count;i++ ){
			if(adapter.getItem(i).toString().equals(xAxisStr)){
				spinner.setSelection(i,true);
				if(spinner.getId() == R.id.OCTFreqWeight_spinner){
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
			octSwitch.setChecked(true);
			octSwitchOpen();
		}
		else{
			octSwitch.setChecked(false);
			octSwitchOff();
		}
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.Band_Type)).setText(R.string.BandType);
		((TextView)view.findViewById(R.id.Freq_Weight)).setText(R.string.FreqWeight);
		((TextView)view.findViewById(R.id.X_Axis)).setText(R.string.X_Axis);
		((TextView)view.findViewById(R.id.Y_Axis)).setText(R.string.Y_Axis);
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.OCT_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_oct,Color.YELLOW));
		view.findViewById(R.id.OCT_Layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_signal,Color.YELLOW));
		view.findViewById(R.id.OCT_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		ImageButton Back_Oct = (ImageButton) view.findViewById(R.id.Back_Oct);
		Back_Oct.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		TextView OCT_title = (TextView) view.findViewById(R.id.OCT_title);
		OCT_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Band_Type = (TextView) view.findViewById(R.id.Band_Type);
		Band_Type.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//OCTBandType_spinner
		TextView Freq_Weight_oct = (TextView) view.findViewById(R.id.Freq_Weight);
		Freq_Weight_oct.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//OCTFreqWeight_spinner
		TextView X_Axis_oct = (TextView) view.findViewById(R.id.X_Axis);
		X_Axis_oct.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//OCTXAxis_spinner
		TextView Y_Axis_oct = (TextView) view.findViewById(R.id.Y_Axis);
		Y_Axis_oct.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		
		Spinner OCTFreqWeight_spinner = (Spinner) view.findViewById(R.id.OCTFreqWeight_spinner);
		if((TextView)OCTFreqWeight_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)OCTFreqWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)OCTFreqWeight_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner OCTBandType_spinner = (Spinner) view.findViewById(R.id.OCTBandType_spinner);
		if((TextView)OCTBandType_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)OCTBandType_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)OCTBandType_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner OCTXAxis_spinner = (Spinner) view.findViewById(R.id.OCTXAxis_spinner);
		if((TextView)OCTXAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)OCTXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)OCTXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner OCTYAxis_spinner = (Spinner) view.findViewById(R.id.OCTYAxis_spinner);
		if((TextView)OCTYAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)OCTYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)OCTYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
	}
}
