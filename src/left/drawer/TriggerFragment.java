package left.drawer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import common.DataCollection;

public class TriggerFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener, OnFocusChangeListener {
	private View view = null;
	private Spinner mode_Spinner, Type_spinner;
	private RelativeLayout Not_Time,Type_layout, Duration_layout;
	private DataCollection dataCollection;
	private int timeMode;
	private SharedPreferences.Editor editor;
	private String modeStr,typeStr,lowerStr,upperStr,stepLengthStr,durationStr;
	private EditText lowerEdit,upperEdit,stepEdit,durationEdit;
	public ArrayAdapter<String>  modeAdapter,typeAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.trigger, container, false);
		view.setOnClickListener(this);
		mode_Spinner = (Spinner) view.findViewById(R.id.Mode_spinner);
		modeAdapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Trigger_Mode));
	    //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mode_Spinner.setAdapter(modeAdapter);
		Not_Time = (RelativeLayout) view.findViewById(R.id.Not_Time);
		Type_spinner = (Spinner) view.findViewById(R.id.Type_spinner);
		typeAdapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Trigger_Time_Type));
	    //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Type_spinner.setAdapter(typeAdapter);
		Type_layout  = (RelativeLayout) view.findViewById(R.id.Type_layout);
		Duration_layout = (RelativeLayout) view
				.findViewById(R.id.Duration_layout);
		
		upperEdit = (EditText) view.findViewById(R.id.TriggerUpper_edit);
		lowerEdit = (EditText) view.findViewById(R.id.TriggerLower_edit);
		stepEdit = (EditText) view.findViewById(R.id.Trigger_Step_edit);
		durationEdit = (EditText) view.findViewById(R.id.Trigger_Duration_edit);
		
		
		view.findViewById(R.id.Back_Trig).setOnClickListener(this);
		dataCollection = ((MainActivity) getActivity()).bottomOperate.dataCollection;
		
		//init();
		
		Type_spinner.setOnItemSelectedListener(this);
		mode_Spinner.setOnItemSelectedListener(this);
		
		upperEdit.setOnFocusChangeListener(this);
		lowerEdit.setOnFocusChangeListener(this);
		stepEdit.setOnFocusChangeListener(this);
		durationEdit.setOnFocusChangeListener(this);
		durationEdit.addTextChangedListener(textWatcherDurationEdit);
		upperEdit.addTextChangedListener(textWatcherUpperEdit);
		lowerEdit.addTextChangedListener(textWatcherLowerEdit);
		stepEdit.addTextChangedListener(textWatcherStepEdit);
		init();
//		if(ThemeLogic.themeType==1){
//			((TextView)mode_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
//			((TextView)mode_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
//			
//			mode_Spinner.setTextColor(Color.rgb(255,255, 255));
//			PreView.setTextColor(Color.rgb(255,255, 255));
//			skin_bt.setTextColor(Color.rgb(255,255, 255));
//			lan_bt.setTextColor(Color.rgb(255,255, 255));
//			conn_bt.setTextColor(Color.rgb(255,255, 255));
//			setup_bt.setTextColor(Color.rgb(255,255, 255));
//		}else{
//			((TextView)mode_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
//			((TextView)mode_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
//			SaveTemplate.setTextColor(Color.rgb(0,0, 0));
//			PreView.setTextColor(Color.rgb(0,0, 0));
//			skin_bt.setTextColor(Color.rgb(0,0, 0));
//			lan_bt.setTextColor(Color.rgb(0,0, 0));
//			conn_bt.setTextColor(Color.rgb(0,0, 0));
//			setup_bt.setTextColor(Color.rgb(0,0, 0));
//			
//		}
		return view;
	}
	private TextWatcher textWatcherDurationEdit = new TextWatcher() {  
		  
		@Override  
		public void onTextChanged(CharSequence s, int start, int before,  
		int count) {  
			SharedPreferences preference = getActivity().getSharedPreferences(
					"hz_5D", 0);
			oneDot(durationEdit);
			//InputFilter[] fArray =new InputFilter[1]; 
        	//int maxLength =10; 
			//fArray[0]=new  InputFilter.LengthFilter(maxLength);
			//durationEdit.setFilters(fArray);
			if(Type_spinner.getSelectedItemPosition()==1){
				//start to time��0-27900s
				if(!durationEdit.getText().toString().equals("")&&Float.parseFloat(durationEdit.getText().toString())>(float)27900){
					durationEdit.setText(preference.getString("Trigger_Duration", "0"));
					Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT).show();
					return;
				}	
			}else if(Type_spinner.getSelectedItemPosition()==2){
				//time to stop��0 - 655s
				if(!durationEdit.getText().toString().equals("")&&Float.parseFloat(durationEdit.getText().toString())>(float)655){
					durationEdit.setText(preference.getString("Trigger_Duration", "0"));
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
		
		private TextWatcher textWatcherUpperEdit = new TextWatcher() {  
			  
			@Override  
			public void onTextChanged(CharSequence s, int start, int before,  
			int count) {  
				SharedPreferences preference = getActivity().getSharedPreferences(
						"hz_5D", 0);
				oneDot(upperEdit);
			}  
			  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count,  
			int after) {  
			//    
			}  

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
				
			}; 
			private TextWatcher textWatcherLowerEdit = new TextWatcher() {  
				  
				@Override  
				public void onTextChanged(CharSequence s, int start, int before,  
				int count) {  
					SharedPreferences preference = getActivity().getSharedPreferences(
							"hz_5D", 0);
					oneDot(lowerEdit);
				}  
				  
				@Override  
				public void beforeTextChanged(CharSequence s, int start, int count,  
				int after) {     
					
				}  

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
				}  
				};
				private TextWatcher textWatcherStepEdit = new TextWatcher() {  
					  
					@Override  
					public void onTextChanged(CharSequence s, int start, int before,  
					int count) {  
						SharedPreferences preference = getActivity().getSharedPreferences(
								"hz_5D", 0);
						oneDot(stepEdit);
						if(!stepEdit.getText().toString().equals("")){
							if(Float.parseFloat(stepEdit.getText().toString())>(float)11880){		
								stepEdit.setText(preference.getString("Trigger_Step_Length", ""));
								Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT).show();
								return;
							}
						}
					}  
					  
					@Override  
					public void beforeTextChanged(CharSequence s, int start, int count,  
					int after) {     
						
					}  

					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
					}  
					};
	private void oneDot(EditText editText){
		//����ֻ������һ��С���
		int point = 0;
		for (int i = 0; i < editText.getText().toString().length(); i++) {
			// if(order_edit.getText().toString().indexOf(".")!=-1){
			if (editText.getText().toString().charAt(i) == '.')
				point = point + 1;
			// }
		}
		if (point > 1) {
			Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT).show();
			return;
		}
	}
	public void init() {
		setSlectedItem(Type_spinner, typeStr);
		setSlectedItem(mode_Spinner, modeStr);
		
		upperEdit.setText(upperStr);
		lowerEdit.setText(lowerStr);
		stepEdit.setText(stepLengthStr);
		durationEdit.setText(durationStr);
		if(!durationStr.equals("")){
			dataCollection.setDuration(Float.valueOf(durationStr));
		}
		dataCollection.setTriggerMode(Type_spinner.getSelectedItemPosition());

	}
	
	public void reset(FragmentActivity fragmentActivity){
		readFromXml(fragmentActivity);
		init();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainFragment mainFragment = (MainFragment) getFragmentManager()
				.findFragmentByTag("main");
		TriggerFragment triggerFragment = (TriggerFragment) getFragmentManager()
				.findFragmentByTag("trig");
		switch (v.getId()) {
		case R.id.Back_Trig:
			getFragmentManager().beginTransaction().hide(triggerFragment)
					.show(mainFragment).commit();
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parentView, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parentView.getId()) {
		case R.id.Mode_spinner:
			TextView tv = (TextView) view;
			if(tv==null)return;
			if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
			tv.setGravity(Gravity.CENTER);
			editor.putString("Trigger_Mode",tv.getText().toString());
			editor.putInt("Trigger_Mode_ID", position);
        	editor.commit();
			if (position == 0) {
				Not_Time.setVisibility(View.GONE);
				Type_layout.setVisibility(View.VISIBLE);
			} else {
				Not_Time.setVisibility(View.VISIBLE);
				Type_layout.setVisibility(View.GONE);
				
			}
			break;
		case R.id.Type_spinner:
			timeMode = position;
			TextView tv1 = (TextView) view;
			if(tv1==null)return;
			if(ThemeLogic.themeType==1){
				 tv1.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv1.setTextColor(getResources().getColor(R.color.black));
			}
			tv1.setGravity(Gravity.CENTER);
			editor.putString("Trigger_Type",tv1.getText().toString());
        	editor.commit();
        	
			if (position == 0) {
				Duration_layout.setVisibility(View.GONE);
			} else {
				Duration_layout.setVisibility(View.VISIBLE);
			}
			dataCollection.setTriggerMode(timeMode);
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		SharedPreferences preference = getActivity().getSharedPreferences(
				"hz_5D", 0);
		switch (v.getId()) {
		case R.id.TriggerLower_edit:
			if(!lowerEdit.getText().toString().equals("")){
				if(Float.parseFloat(lowerEdit.getText().toString())<(float)120
						||Float.parseFloat(lowerEdit.getText().toString())>(float)12000){
					lowerEdit.setText(preference.getString("Trigger_Lower", ""));
					Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT).show();
					return;
				}else{
					editor.putString("Trigger_Lower",((EditText)v).getText().toString());
					editor.commit();
				}
			}
			break;
		case R.id.TriggerUpper_edit:
			if(!upperEdit.getText().toString().equals("")){
				if(Float.parseFloat(upperEdit.getText().toString())<(float)120
						||Float.parseFloat(upperEdit.getText().toString())>(float)12000){
					upperEdit.setText(preference.getString("Trigger_Upper", ""));
					Toast.makeText(getActivity().getApplicationContext(), R.string.enter_wrong, Toast.LENGTH_SHORT).show();
					return;
				}else{
					editor.putString("Trigger_Upper",((EditText)v).getText().toString());
		        	editor.commit();
				}
			}

			break;
		case R.id.Trigger_Step_edit:
			Log.v("bug11", "Trigger_Step==="+((EditText)v).getText().toString());
			editor.putString("Trigger_Step_Length",((EditText)v).getText().toString());
			editor.commit();
			break;
				
		case R.id.Trigger_Duration_edit:
			editor.putString("Trigger_Duration",((EditText)v).getText().toString());
        	editor.commit();

			if (timeMode == 0) {
				return;
			}
			
			if (hasFocus == false) {
				String durationStr = ((EditText) v).getText().toString();
				Pattern pattern=Pattern.compile("[0-9]*");
				Matcher isNum=pattern.matcher(durationStr);
				if(!isNum.matches()||durationStr.equals("")){
					return;
				}
				dataCollection.setDuration(Float.valueOf(durationStr));
				dataCollection.setTriggerMode(timeMode);
			}
			break;
		}
	}
	
	public void readFromXml(FragmentActivity fragmentActivity) {

		SharedPreferences preference = fragmentActivity.getSharedPreferences(
				"hz_5D", 0);
		editor = preference.edit();
		modeStr = preference.getString("Trigger_Mode", "Time");
		typeStr = preference.getString("Trigger_Type", "Start to Stop");
		lowerStr = preference.getString("Trigger_Lower", "120");
		upperStr = preference.getString("Trigger_Upper", "120");
		stepLengthStr = preference.getString("Trigger_Step_Length", "0");
		durationStr = preference.getString("Trigger_Duration", "0");

	}
	
	private void setSlectedItem(Spinner spinner, String xAxisStr) {
		
		SpinnerAdapter adapter = spinner.getAdapter();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			if (adapter.getItem(i).toString().equals(xAxisStr)) {
				spinner.setSelection(i);
				break;
			}
		}
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.Trigger_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_trigger,Color.YELLOW));
		view.findViewById(R.id.Trigger_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		TextView Trigger_title = (TextView) view.findViewById(R.id.Trigger_title);
		ImageButton Back_Trig = (ImageButton) view.findViewById(R.id.Back_Trig);
		Back_Trig.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		
		Trigger_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));	
		TextView Trig_Mode = (TextView) view.findViewById(R.id.Trig_Mode);
		Trig_Mode.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Lower = (TextView) view.findViewById(R.id.Lower);
		Lower.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView OvTriggerLower_unit = (TextView) view.findViewById(R.id.OvTriggerLower_unit);
		OvTriggerLower_unit.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Upper = (TextView) view.findViewById(R.id.Upper);
		Upper.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView OvTriggerUpper_unit = (TextView) view.findViewById(R.id.OvTriggerUpper_unit);
		OvTriggerUpper_unit.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Step = (TextView) view.findViewById(R.id.Step);
		Step.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Step_unit = (TextView) view.findViewById(R.id.Step_unit);
		Step_unit.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView Type = (TextView) view.findViewById(R.id.Type);
		Type.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Duration = (TextView) view.findViewById(R.id.Duration);
		Duration.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Duration_unit = (TextView) view.findViewById(R.id.Duration_unit);
		Duration_unit.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		modeAdapter.notifyDataSetChanged();
		typeAdapter.notifyDataSetChanged();
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.Trigger_title)).setText(R.string.Trigger);
	    ((TextView)view.findViewById(R.id.Trig_Mode)).setText(R.string.Mode);
	    ((TextView)view.findViewById(R.id.Lower)).setText(R.string.Lower);
	    ((TextView)view.findViewById(R.id.Upper)).setText(R.string.Upper);
	    ((TextView)view.findViewById(R.id.Step)).setText(R.string.Step);
	    ((TextView)view.findViewById(R.id.Type)).setText(R.string.Type);
	    ((TextView)view.findViewById(R.id.Duration)).setText(R.string.Duration);
	}
}
