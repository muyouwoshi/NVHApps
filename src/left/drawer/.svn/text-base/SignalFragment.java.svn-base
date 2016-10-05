package left.drawer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
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

public class SignalFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener {
	private View view = null;
	private Spinner xAxis_Spinner = null;
	private Spinner yAxis_Spinner = null;
	private Switch signalSwitch = null;
	private SpinnerValues spinnerValues;
	private String xAxisStr;
	private String yAxisStr;
	private String switchStr;
	private SharedPreferences.Editor editor;
	public SignalFragment() {
		// TODO Auto-generated constructor stub
		// this.spinnerValues=spinnerValues;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signal, container, false);
		view.setOnClickListener(this);
		view.findViewById(R.id.Back_Signal).setOnClickListener(this);
		signalSwitch = (Switch) view.findViewById(R.id.SignalSwitchButton);
		signalSwitch.setThumbResource(R.drawable.switchtitle);
		signalSwitch.setSwitchTextAppearance(view.getContext(),R.color.black);
		xAxis_Spinner = (Spinner) view.findViewById(R.id.SignalXAxis_spinner);
		yAxis_Spinner = (Spinner) view.findViewById(R.id.SignalYAxis_spinner);
		
		xAxis_Spinner.setOnItemSelectedListener(this);
		yAxis_Spinner.setOnItemSelectedListener(this);
//		if((TextView)xAxis_Spinner.getSelectedView()!=null){
//			if(ThemeLogic.themeType==1){
//				((TextView)xAxis_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
//		   }else{
//			
//			((TextView)xAxis_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
//		   }
//		}
//		if((TextView)yAxis_Spinner.getSelectedView()!=null){
//			if(ThemeLogic.themeType==1){
//				((TextView)yAxis_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
//		   }else{
//			
//			((TextView)yAxis_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
//		   }
//		}
		init();
		
		Switch();

		return view;
	}
	
	public void init(){
		setSlectedItem(xAxis_Spinner, xAxisStr);
		setSlectedItem(yAxis_Spinner, yAxisStr);
		setSwitch();
	}

	public SpinnerValues getSpinnerValues() {
		return spinnerValues;
	}

	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainFragment mainFragment = (MainFragment) getFragmentManager()
				.findFragmentByTag("main");
		SignalFragment acquiFragment = (SignalFragment) getFragmentManager()
				.findFragmentByTag("signal");
		switch (v.getId()) {
		case R.id.Back_Signal:
			getFragmentManager().beginTransaction().hide(acquiFragment)
					.show(mainFragment).commit();
			break;
		}
	}

	public void Switch() {
		// signalSwitch = (Switch) view.findViewById(R.id.SignalSwitchButton);

		/*
		 * Ĭ��ѡ��Signal
		 */
		// SharedPreferences preference =
		// getActivity().getSharedPreferences("hz_5D", 0);
		// editor = preference.edit();
		// // signalSwitch.setChecked(true);
		// // signalSwitchOpen();
		// spinnerValues.getAlgorithmItems().add("ChannelWatch");
		//
		if(null == spinnerValues || null == spinnerValues.getAlgorithmItems())
			return;
		
		spinnerValues.getAlgorithmItems().add("ChannelWatch");
		// editor.commit();
		signalSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@SuppressLint("ResourceAsColor")
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
				if(ct.algorithm_spinnerList.size()!=0){
				for(int j=0;j<ct.algorithm_spinnerList.size();j++){
				if(ct.algorithm_spinnerList.get(j).getText().equals("Signal")&&DataCollection.isRecording){
					signalSwitch.setChecked(true);
					Toast.makeText(getActivity().getApplicationContext(),
							 R.string.Collecting_data, Toast.LENGTH_SHORT)//����������
							.show();
					return;
				}
				}
				}
				if (isChecked) {
					
					
					signalSwitchOpen();
				} else {
					signalSwitchOff();
				}
			}
		});
	}

	@SuppressLint("NewApi")
	public void signalSwitchOpen() {
		spinnerValues.addValues("Signal");
		signalSwitch.setTrackResource(R.drawable.switchbutton_on);
//		signalSwitch.setTextColor(getResources().getColor(R.color.black));
		// ����Ϣ�����ģ���ļ�
		editor.putString("Signal", "open");
		editor.putString("Signal_XAxis", xAxis_Spinner.getSelectedItem()
				.toString());
		editor.putString("Signal_YAxis", yAxis_Spinner.getSelectedItem()
				.toString());
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.commit();
	}

	@SuppressLint("NewApi")
	public void signalSwitchOff() {
			spinnerValues.removeValues("Signal");
			signalSwitch.setTrackResource(R.drawable.switchbutton_off);
//			signalSwitch.setTextColor(getResources().getColor(R.color.black));
			// ��ģ���ļ��е���Ϣɾ��
			editor.putString("Signal", "close");
			editor.putString("Signal_XAxis", xAxis_Spinner.getSelectedItem()
					.toString());
			editor.putString("Signal_YAxis", yAxis_Spinner.getSelectedItem()
					.toString());
			spinnerValues.getAdapter().notifyDataSetChanged();
			editor.commit();
			removeAllView();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// if(!signalSwitch.isChecked()) return;
		// ʵʱ����ģ����Ϣ
		/*SharedPreferences preference = getActivity().getSharedPreferences(
				"hz_5D", 0);
		SharedPreferences.Editor editor = preference.edit();*/
		switch (parent.getId()) {
		case R.id.SignalXAxis_spinner:
			TextView tv = (TextView) view;
			if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
			tv.setGravity(Gravity.CENTER);
				editor.putString("Signal_XAxis", xAxis_Spinner
						.getSelectedItem().toString());
			
			spinnerChangeState(0, xAxis_Spinner.getSelectedItem().toString());
			break;
		case R.id.SignalYAxis_spinner:
			TextView tv1 = (TextView) view;
			if(ThemeLogic.themeType==1){
				 tv1.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv1.setTextColor(getResources().getColor(R.color.black));
			}
			tv1.setGravity(Gravity.CENTER);
				editor.putString("Signal_YAxis", yAxis_Spinner
						.getSelectedItem().toString());
				spinnerChangeState(1, yAxis_Spinner.getSelectedItem().toString());
			
			break;
		}
	    editor.commit();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	public void readFromXml(FragmentActivity fragmentActivity) {

		SharedPreferences preference = fragmentActivity.getSharedPreferences(
				"hz_5D", 0);
		editor = preference.edit();
		xAxisStr = preference.getString("Signal_XAxis", "s");
		yAxisStr = preference.getString("Signal_YAxis", "Pa");
		switchStr = preference.getString("Signal", "close");

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

	private void setSwitch() {
		if(null == switchStr)
			return;
		
		if (switchStr.equals("open")) {
			signalSwitch.setChecked(true);
			signalSwitchOpen();
		} else {
			signalSwitch.setChecked(false);
			signalSwitchOff();

		}
	}

	public void SaveTo(String oldPath, String newPath) {
		// TODO Auto-generated method stub
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			File newfile = new File(newPath);
			if (!newfile.getParentFile().exists()) {
				newfile.getParentFile().mkdirs();
			}
			if (oldfile.exists()) { // �ļ�����ʱ
				InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			Toast.makeText(getActivity().getApplicationContext(), R.string.save_template_error,
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
private void removeAllView(){
	CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
	//ArrayList<Fragment> pageList = ct.getFragmentList();
	for(int j=0;j<ct.algorithm_spinnerList.size();j++){
		if(ct.algorithm_spinnerList.get(j).getText().equals("Signal")){
			ct.algorithm_spinnerList.get(j).setText(spinnerValues.getAlgorithmItems().get(0));
			((MainContextView)ct.mainContextViewList.get(j)).drawMap();
		}
	}
}
	
	private void spinnerChangeState(int axisType,String unitType){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("Signal")){
				((MainContextView)ct.mainContextViewList.get(i)).setUnitChangeState(axisType,unitType);
			}
		}
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.Signal_title)).setText(R.string.Signal);
		((TextView)view.findViewById(R.id.X_Axis)).setText(R.string.X_Axis);
		((TextView)view.findViewById(R.id.Y_Axis)).setText(R.string.Y_Axis);
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.Signal_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_signal,Color.YELLOW));
		view.findViewById(R.id.Signal_Layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_rpm,Color.YELLOW));
		view.findViewById(R.id.Signal_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		ImageButton Back_Signal = (ImageButton) view.findViewById(R.id.Back_Signal);
		Back_Signal.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		TextView Signal_title = (TextView) view.findViewById(R.id.Signal_title);
		Signal_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView X_Axis_signal = (TextView) view.findViewById(R.id.X_Axis);
		X_Axis_signal.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//SignalYAxis_spinner
		TextView Y_Axis_signal = (TextView) view.findViewById(R.id.Y_Axis);
		Y_Axis_signal.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//SignalXAxis_spinner
	
		Spinner xAxis_Spinner = (Spinner) view.findViewById(R.id.SignalXAxis_spinner);
		Spinner yAxis_Spinner = (Spinner) view.findViewById(R.id.SignalYAxis_spinner);
		if((TextView)xAxis_Spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)xAxis_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)xAxis_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		if((TextView)yAxis_Spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)yAxis_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)yAxis_Spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
	}
}
