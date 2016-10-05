package left.drawer;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.SpinnerValues;
import com.example.mobileacquisition.ThemeLogic;

import common.CustomTab;
import common.DataCollection;

public class RpmFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	
	private View view=null;
	private Spinner display_Spinner = null;
	private Spinner xAxis_Spinner = null;
	private Spinner yAxis_Spinner = null;
	private Switch rpmSwitch = null;
	private SpinnerValues spinnerValues;
	private RelativeLayout Unit_Layout;
	private String xAxisStr;
	private String yAxisStr;
	private String switchStr;
	private String displayStr;
	private SharedPreferences.Editor editor;
	
	public RpmFragment() {
		
	}
	
	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rpm, container,false);
		view.setOnClickListener(this);
		/*
		 * ����
		 */
		/*
		String getSkinValues = getActivity().getIntent().getStringExtra("SkinIntent");
		if (getSkinValues != null) {
			if (getSkinValues.equals("Skin0")) {
				RPM_layout.setBackgroundColor(Color.WHITE);
				RPM_title.setBackgroundColor(Color.BLUE);
			}else if (getSkinValues.equals("Skin1")) {
				RPM_layout.setBackgroundColor(Color.LTGRAY);
				RPM_title.setBackgroundColor(Color.GREEN);
			}
		}*/
		Unit_Layout=(RelativeLayout)view.findViewById(R.id.Unit_Layout);
		view.findViewById(R.id.Back_Rpm).setOnClickListener(this);
		display_Spinner = (Spinner) view.findViewById(R.id.RPMDisplay_spinner);
		ArrayAdapter<String>  mAdapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Display));
		display_Spinner.setAdapter(mAdapter);
		xAxis_Spinner = (Spinner) view.findViewById(R.id.RPMXAxis_spinner);
		yAxis_Spinner = (Spinner) view.findViewById(R.id.RPMYAxis_spinner);
		rpmSwitch = (Switch) view.findViewById(R.id.RPMSwitchButton);
		rpmSwitch.setThumbResource(R.drawable.switchtitle);
		rpmSwitch.setSwitchTextAppearance(view.getContext(),R.color.black);
		display_Spinner.setOnItemSelectedListener(this);
		xAxis_Spinner.setOnItemSelectedListener(this);
		yAxis_Spinner.setOnItemSelectedListener(this);
		
		init();
		
		Switch();
		return view;
	}
	public void init(){
		setSlectedItem(display_Spinner,displayStr);
		setSlectedItem(xAxis_Spinner,xAxisStr);
		setSlectedItem(yAxis_Spinner,yAxisStr);
		setSwitch();
	}
	/*
	 * ���Ը���
	 */
	/*public void refresh(){
		RPM_title.setText(R.string.RPM);
		TextView Display=(TextView)view.findViewById(R.id.Display);
		Display.setText(R.string.Display);
		TextView X_Axis =(TextView)view.findViewById(R.id.X_Axis);
		X_Axis.setText(R.string.X_Axis);
		TextView Y_Axis =(TextView)view.findViewById(R.id.Y_Axis);
		Y_Axis.setText(R.string.Y_Axis);
	}*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("main");
		RpmFragment acquiFragment = (RpmFragment) getFragmentManager().findFragmentByTag("rpm");
		switch(v.getId()){
		case R.id.Back_Rpm:
			getFragmentManager().beginTransaction().hide(acquiFragment).show(mainFragment).commit();
			break;
		}
	}
	public void Switch(){
		
		rpmSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, 
			                boolean isChecked)  {
//				SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
//				SharedPreferences.Editor editor = preference.edit();
				CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
				if(ct.algorithm_spinnerList.size()!=0){
				for(int j=0;j<ct.algorithm_spinnerList.size();j++){
				if(ct.algorithm_spinnerList.get(j).getText().equals("RPM")&&DataCollection.isRecording){
					rpmSwitch.setChecked(true);
					Toast.makeText(getActivity().getApplicationContext(),
							 R.string.Collecting_data, Toast.LENGTH_SHORT)//����������
							.show();
					return;
				}
				}
				}
				if(isChecked){
					rpmSwitchOpen();
				}else{
					rpmSwitchOff();
				}
				
			}
		});
	}
	
	@SuppressLint("NewApi")
	private void rpmSwitchOpen() {
		spinnerValues.addValues("RPM");
		rpmSwitch.setTrackResource(R.drawable.switchbutton_on);
		//����Ϣ�����ģ���ļ�
		editor.putString("RPM", "open");
		editor.putString("RPM_Display", display_Spinner.getSelectedItem().toString());
		editor.putString("RPM_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("RPM_YAxis", yAxis_Spinner.getSelectedItem().toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
	}
	
	@SuppressLint("NewApi")
	private void rpmSwitchOff() {
		
		spinnerValues.removeValues("RPM");
		rpmSwitch.setTrackResource(R.drawable.switchbutton_off);
		//��ģ���ļ��е���Ϣɾ��
		editor.putString("RPM", "close");
		editor.putString("RPM_Display", display_Spinner.getSelectedItem().toString());
		editor.putString("RPM_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("RPM_YAxis", yAxis_Spinner.getSelectedItem().toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
		removeAllView();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		//ʵʱ����ģ����Ϣ
//		SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
//		SharedPreferences.Editor editor = preference.edit();
		switch(parent.getId()) {
		case R.id.RPMDisplay_spinner:
			 TextView tv = (TextView)view;
			 if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
             tv.setGravity(Gravity.CENTER);
             editor.putString("RPM_Display", display_Spinner.getSelectedItem().toString());
			if(position==2){
				Unit_Layout.setVisibility(View.VISIBLE);
			}else{
				Unit_Layout.setVisibility(View.GONE);
			}
			displayChangeState(position);
			break;
		case R.id.RPMXAxis_spinner:
			 TextView tv1 = (TextView)view;
			 if(ThemeLogic.themeType==1){
				 tv1.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv1.setTextColor(getResources().getColor(R.color.black));
			}
             tv1.setGravity(Gravity.CENTER);
			editor.putString("RPM_XAxis", xAxis_Spinner.getSelectedItem().toString());
			spinnerChangeState(0,xAxis_Spinner.getSelectedItem().toString());
			
			break;
		case R.id.RPMYAxis_spinner:
			 TextView tv2 = (TextView)view;
			 if(ThemeLogic.themeType==1){
				 tv2.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv2.setTextColor(getResources().getColor(R.color.black));
			}
             tv2.setGravity(Gravity.CENTER);
			editor.putString("RPM_YAxis", yAxis_Spinner.getSelectedItem().toString());
			spinnerChangeState(1,yAxis_Spinner.getSelectedItem().toString());
			
			break;
		}
	    editor.commit();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	private void displayChangeState(int position){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("RPM")){
				((MainContextView)ct.mainContextViewList.get(i)).drawRPM(position);
			}
		}
	} 
	private void removeAllView(){
		CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
		//ArrayList<Fragment> pageList = ct.getFragmentList();
		for(int j=0;j<ct.algorithm_spinnerList.size();j++){
			if(ct.algorithm_spinnerList.get(j).getText().equals("RPM")){
				ct.algorithm_spinnerList.get(j).setText(spinnerValues.getAlgorithmItems().get(0));
				((MainContextView)ct.mainContextViewList.get(j)).drawMap();
			}
		}
	}
	private void spinnerChangeState(int axisType,String unitType){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("RPM")){
				((MainContextView)ct.mainContextViewList.get(i)).setUnitChangeState(axisType,unitType);
			}
		}
	}
	public void readFromXml(FragmentActivity fragmentActivity) {
		// TODO Auto-generated method stub
		SharedPreferences preference = fragmentActivity.getSharedPreferences("hz_5D", 0);
		editor = preference.edit();
		xAxisStr = preference.getString("RPM_XAxis", "s");
		yAxisStr = preference.getString("RPM_YAxis", "Pa");
		displayStr = preference.getString("RPM_Display", "Digit");
		switchStr = preference.getString("RPM", "close");
	}
	
	private void setSlectedItem(Spinner  spinner, String xAxisStr) {
		SpinnerAdapter adapter = spinner.getAdapter();
		int count = adapter.getCount();
		for(int i = 0; i<count;i++ ){
			if(adapter.getItem(i).toString().equals(xAxisStr)){
				spinner.setSelection(i);
				break;
			}
		}
	}
	private void setSwitch() {
		// TODO Auto-generated method stub
		if(switchStr.equals("open")) {
			rpmSwitch.setChecked(true);
			rpmSwitchOpen();
		}
		else{
			rpmSwitch.setChecked(false);
			rpmSwitchOff();
		}
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.X_Axis)).setText(R.string.X_Axis);
		((TextView)view.findViewById(R.id.Y_Axis)).setText(R.string.Y_Axis);
		((TextView)view.findViewById(R.id.Display)).setText(R.string.Display);
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.RPM_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_rpm,Color.YELLOW));
		view.findViewById(R.id.RPM_Layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_rpm,Color.YELLOW));
		view.findViewById(R.id.RPM_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		ImageButton Back_Rpm = (ImageButton) view.findViewById(R.id.Back_Rpm);
		Back_Rpm.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		TextView RPM_title = (TextView) view.findViewById(R.id.RPM_title);
		RPM_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Display = (TextView) view.findViewById(R.id.Display);
		Display.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//RPMDisplay_spinner
		
		TextView rpm_X_Axis=(TextView) view.findViewById(R.id.X_Axis);
		rpm_X_Axis.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView rpm_Y_Axis=(TextView) view.findViewById(R.id.Y_Axis);
		rpm_Y_Axis.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		Spinner RPMDisplay_spinner = (Spinner) view.findViewById(R.id.RPMDisplay_spinner);
		if((TextView)RPMDisplay_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)RPMDisplay_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)RPMDisplay_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner RPMXAxis_spinner = (Spinner) view.findViewById(R.id.RPMXAxis_spinner);
		if((TextView)RPMXAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)RPMXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)RPMXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner RPMYAxis_spinner = (Spinner) view.findViewById(R.id.RPMYAxis_spinner);
		if((TextView)RPMYAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)RPMYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)RPMYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
	}
}
