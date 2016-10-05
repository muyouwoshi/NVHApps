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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

public class AiFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	
	private View view=null;
	private Spinner mode_Spinner = null;
	private Spinner xAxis_Spinner = null;
	private Spinner yAxis_Spinner = null;
	private Switch aiSwitch = null;
	private SpinnerValues spinnerValues; 
	
	private String xAxisStr;
	private String yAxisStr;
	private String switchStr;
	private String modeStr;
	private int selectedItemPosition;
	private SharedPreferences.Editor editor;

	public AiFragment() {
		
	}
	
	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ai, container,false);
		view.setOnClickListener(this);
		/*
		 * ����
		 */
		/*
		String getSkinValues = getActivity().getIntent().getStringExtra("SkinIntent");
		if (getSkinValues != null) {
			if (getSkinValues.equals("Skin0")) {
				AI_layout.setBackgroundColor(Color.WHITE);
				AI_title.setBackgroundColor(Color.BLUE);
			}else if (getSkinValues.equals("Skin1")) {
				AI_layout.setBackgroundColor(Color.LTGRAY);
				AI_title.setBackgroundColor(Color.GREEN);
			}
		}*/
		view.findViewById(R.id.Back_Ai).setOnClickListener(this);
		mode_Spinner = ((Spinner)view.findViewById(R.id.AIMode_spinner));
		xAxis_Spinner = ((Spinner)view.findViewById(R.id.AIXAxis_spinner));
		selectedItemPosition=xAxis_Spinner.getSelectedItemPosition();
		yAxis_Spinner = ((Spinner)view.findViewById(R.id.AIYAxis_spinner));
		aiSwitch = (Switch) view.findViewById(R.id.AISwitchButton);
		aiSwitch.setThumbResource(R.drawable.switchtitle);
		aiSwitch.setSwitchTextAppearance(view.getContext(),R.color.black);
		mode_Spinner.setOnItemSelectedListener(this);
		xAxis_Spinner.setOnItemSelectedListener(this);
		yAxis_Spinner.setOnItemSelectedListener(this);
		
		init();
		
		Switch();

		
		return view;
	}
	public void init(){
		setSlectedItem(mode_Spinner,modeStr);
		setSlectedItem(xAxis_Spinner,xAxisStr);
		setSlectedItem(yAxis_Spinner,yAxisStr);
		setSwitch();
	}
	/*
	 * ���Ը���
	 */
/*	public void refresh(){
		AI_title.setText(R.string.AI);
		TextView Mode=(TextView)view.findViewById(R.id.Mode);
		Mode.setText(R.string.Mode);
		TextView X_Axis =(TextView)view.findViewById(R.id.X_Axis);
		X_Axis.setText(R.string.X_Axis);
		TextView Y_Axis =(TextView)view.findViewById(R.id.Y_Axis);
		Y_Axis.setText(R.string.Y_Axis);
	}*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainFragment mainFragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("main");
		AiFragment acquiFragment = (AiFragment) getFragmentManager().findFragmentByTag("ai");
		switch(v.getId()){
		case R.id.Back_Ai:
			getFragmentManager().beginTransaction().hide(acquiFragment).show(mainFragment).commit();
			break;
		}
	}
	public void Switch(){
		
		aiSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, 
			                boolean isChecked)  {
//				SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
				CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
				if(ct.algorithm_spinnerList.size()!=0){
				for(int j=0;j<ct.algorithm_spinnerList.size();j++){
				if(ct.algorithm_spinnerList.get(j).getText().equals("AI")&&DataCollection.isRecording){
					aiSwitch.setChecked(true);
					Toast.makeText(getActivity().getApplicationContext(),
							 R.string.Collecting_data, Toast.LENGTH_SHORT)//����������
							.show();
					return;
				}
				}
				}
				if(isChecked){
					aiSwitchOpen();
				}else{
					aiSwitchOff();
				}
				
			}
		});
	}
	
	@SuppressLint("NewApi")
	private void aiSwitchOpen() {
		spinnerValues.addValues("AI");
		aiSwitch.setTrackResource(R.drawable.switchbutton_on);
		//����Ϣ�����ģ���ļ�
		editor.putString("AI", "open");
		editor.putString("AI_Mode", mode_Spinner.getSelectedItem().toString());
		editor.putString("AI_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("AI_YAxis", yAxis_Spinner.getSelectedItem().toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
	}
	
	@SuppressLint("NewApi")
	private void aiSwitchOff() {
		// �л�����̨����д������ʱ�� NullPointerException
		spinnerValues.removeValues("AI");
		aiSwitch.setTrackResource(R.drawable.switchbutton_off);
		//��ģ���ļ��е���Ϣɾ��
		editor.putString("AI", "close");
		editor.putString("AI_Mode", mode_Spinner.getSelectedItem().toString());
		editor.putString("AI_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("AI_YAxis", yAxis_Spinner.getSelectedItem().toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
		removeAllView();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
//		if(!aiSwitch.isChecked()) return;
		//ʵʱ����ģ����Ϣ
//		SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
//		SharedPreferences.Editor editor = preference.edit();
		switch(parent.getId()) {
		case R.id.AIMode_spinner:
			 TextView tv = (TextView)view;
			 if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
             tv.setGravity(Gravity.CENTER);
			editor.putString("AI_Mode", mode_Spinner.getSelectedItem().toString());
		   
			break;
		case R.id.AIXAxis_spinner:
			 TextView tv1 = (TextView)view;
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
			editor.putString("AI_XAxis", xAxis_Spinner.getSelectedItem().toString());
			spinnerChangeState(0,xAxis_Spinner.getSelectedItem().toString());
			
			break;
		case R.id.AIYAxis_spinner:
			 TextView tv2 = (TextView)view;
			 if(ThemeLogic.themeType==1){
				 tv2.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv2.setTextColor(getResources().getColor(R.color.black));
			}
             tv2.setGravity(Gravity.CENTER);
			editor.putString("AI_YAxis", yAxis_Spinner.getSelectedItem().toString());
			spinnerChangeState(1,yAxis_Spinner.getSelectedItem().toString());
			
			break;
		}
		editor.commit();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	private void removeAllView(){
		CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
		for(int j=0;j<ct.algorithm_spinnerList.size();j++){
			if(ct.algorithm_spinnerList.get(j).getText().equals("AI")){
				ct.algorithm_spinnerList.get(j).setText(spinnerValues.getAlgorithmItems().get(0));
				((MainContextView)ct.mainContextViewList.get(j)).drawMap();
			}
			
		}
	}
	private void spinnerChangeState(int axisType,String unitType){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("AI")){
				((MainContextView)ct.mainContextViewList.get(i)).setUnitChangeState(axisType,unitType);
			}
		}
	}
	public void readFromXml(FragmentActivity fragmentActivity) {
		// TODO Auto-generated method stub
		SharedPreferences preference = fragmentActivity.getSharedPreferences("hz_5D", 0);
		editor = preference.edit();
		xAxisStr = preference.getString("AI_XAxis", "s");
		yAxisStr = preference.getString("AI_YAxis", "Pa");
		modeStr = preference.getString("AI_Mode", "open");
		switchStr = preference.getString("AI", "close");
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
		if("open".equals(switchStr)) {
			aiSwitch.setChecked(true);
			aiSwitchOpen();
		}
		else{
			aiSwitch.setChecked(false);
			aiSwitchOff();
		}
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.Mode)).setText(R.string.Mode);
		((TextView)view.findViewById(R.id.X_Axis)).setText(R.string.X_Axis);
		((TextView)view.findViewById(R.id.Y_Axis)).setText(R.string.Y_Axis);
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.AI_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_ai,Color.YELLOW));
		view.findViewById(R.id.AI_Layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_ai,Color.YELLOW));
		ImageButton Back_Ai = (ImageButton) view.findViewById(R.id.Back_Ai);
		Back_Ai.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		view.findViewById(R.id.AI_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		TextView AI_title = (TextView) view.findViewById(R.id.AI_title);
		AI_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView X_Axis = (TextView) view.findViewById(R.id.X_Axis);
		X_Axis.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
//		Spinner AIXAxis_spinner = (Spinner) view.findViewById(R.id.AIXAxis_spinner);
//		AIXAxis_spinner.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Y_Axis = (TextView) view.findViewById(R.id.Y_Axis);
		Y_Axis.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
//		Spinner AIYAxis_spinner = (Spinner) view.findViewById(R.id.AIYAxis_spinner);
//		AIYAxis_spinner.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		Spinner AIMode_spinner = (Spinner) view.findViewById(R.id.AIMode_spinner);
		if((TextView)AIMode_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)AIMode_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)AIMode_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner AIYAxis_spinner = (Spinner) view.findViewById(R.id.AIYAxis_spinner);
		if((TextView)AIYAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)AIYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)AIYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner AIXAxis_spinner = (Spinner) view.findViewById(R.id.AIXAxis_spinner);
		if((TextView)AIXAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)AIXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)AIXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
	}
}
