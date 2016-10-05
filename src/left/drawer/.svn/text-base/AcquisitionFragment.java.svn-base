package left.drawer;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import common.XclSingalTransfer;

public class AcquisitionFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	private View view=null;
	private Spinner sampling_rate_n_spinner,sampling_rate_v_spinner;
	private SharedPreferences.Editor editor;
	private String acquiFreqStr,sampling_rate_v_str;
	public ArrayAdapter<String> adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.acquisition, container,false);
		view.setOnClickListener(this);
		sampling_rate_n_spinner=(Spinner)view.findViewById(R.id.sampling_rate_n_spinner);
		sampling_rate_v_spinner=(Spinner)view.findViewById(R.id.sampling_rate_v_spinner);
		adapter = new MySpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.AcquiFreq_spinner));
		adapter.notifyDataSetChanged();
		sampling_rate_n_spinner.setAdapter(adapter);
		sampling_rate_v_spinner.setAdapter(adapter);
		view.findViewById(R.id.Back_Acqui).setOnClickListener(this);
		
		init();
		sampling_rate_n_spinner.setOnItemSelectedListener(this);
		sampling_rate_v_spinner.setOnItemSelectedListener(this);

		return view;
	}
	
	
	public int getAcquiFreq() {
		return Integer.valueOf(sampling_rate_n_spinner.getSelectedItem().toString());
	}


	public void init() {
		setSlectedItem(sampling_rate_n_spinner, acquiFreqStr);
		setSlectedItem(sampling_rate_v_spinner, sampling_rate_v_str);
	}
	
	public void reset(FragmentActivity fragmentActivity){
		readFromXml(fragmentActivity);
		init();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("main");
		AcquisitionFragment acquiFragment = (AcquisitionFragment) getFragmentManager().findFragmentByTag("acqui");
		switch(v.getId()){
		case R.id.Back_Acqui:
			getFragmentManager().beginTransaction().hide(acquiFragment).show(mainFragment).commit();
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		
		if(arg0==sampling_rate_n_spinner){
			TextView tv = (TextView)arg1;
			if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
	       
	        tv.setGravity(Gravity.CENTER);
			sampling_rate_v_spinner.setSelection(position);
        	editor.putString("AcquiFreq_spinner_values",tv.getText().toString());
        	editor.commit();
			
        	FftFragment fftFragment=  (FftFragment) getFragmentManager().findFragmentByTag("fft");
			fftFragment.setAcquiFreq(Integer.parseInt((String)(sampling_rate_n_spinner.getSelectedItem())));
			
        	ColormapFragment colormapFragment=  (ColormapFragment) getFragmentManager().findFragmentByTag("colormap");
			colormapFragment.setAcquiFreq(Integer.parseInt((String)(sampling_rate_n_spinner.getSelectedItem())));
			
			
			/*
<<<<<<< .mine
			 *  采样频率=24kHz时，频率分辨率=3.75、7.5、15、30、60Hz
				采样频率=32kHz时，频率分辨率=5、10、20、40、80Hz
				采样频率=44.1kHz时，频率分辨率=6.89、13.78、27.56、55.125、110.25Hz
				采样频率=48kHz时，频率分辨率=7.5、15、30、60、120Hz
				采样频率=96kHz时，频率分辨率=15、30、60、120、240Hz
=======
			 *	采样频率=12.8kHz时，频率分辨率=2,4,8,16,32Hz
 				采样频率=25.6kHz时，频率分辨率=4,8,16,32,64Hz
 				采样频率=51.2kHz时，频率分辨率=8,16,32,64,128Hz
 				采样频率=102.4kHz时，频率分辨率=16,32,64,128,256Hz
>>>>>>> .r6506
			 */
			//adapter.notifyDataSetChanged();

        }else if(arg0==sampling_rate_v_spinner){
        	TextView tv = (TextView)arg1;
        	if(ThemeLogic.themeType==1){
				 tv.setTextColor(getResources().getColor(R.color.white4));
			}else{
				
				tv.setTextColor(getResources().getColor(R.color.black));
			}
            tv.setGravity(Gravity.CENTER);
        	sampling_rate_n_spinner.setSelection(position);
        	editor.putString("sampling_rate_v_spinner_values",tv.getText().toString());
        	editor.commit();
        }
		
	}

	
	public void readFromXml(FragmentActivity fragmentActivity) {

		SharedPreferences preference = fragmentActivity.getSharedPreferences(
				"hz_5D", 0);
		editor = preference.edit();
		acquiFreqStr = preference.getString("AcquiFreq_spinner_values", "12800");
		sampling_rate_v_str = preference.getString("sampling_rate_v_spinner_values", "32000");
	}
	
	private void setSlectedItem(Spinner spinner, String xAxisStr) {
		
		SpinnerAdapter adapter = spinner.getAdapter();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			if (adapter.getItem(i).toString().equals(xAxisStr)) {
				spinner.setSelection(i,true);

				break;
			}
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		// TODO Auto-generated method stub
		
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.Acquisition_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_oct,Color.YELLOW));
		view.findViewById(R.id.Acquisition_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		adapter.notifyDataSetChanged();
		ImageButton Back_Acqui = (ImageButton) view.findViewById(R.id.Back_Acqui);
		Back_Acqui.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		TextView Acquisition_title = (TextView) view.findViewById(R.id.Acquisition_title);
		Acquisition_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView sampling_rate_n = (TextView) view.findViewById(R.id.sampling_rate_n);
		sampling_rate_n.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//sampling_rate_n_spinner
		TextView sampling_rate_n_Hz = (TextView) view.findViewById(R.id.sampling_rate_n_Hz);
		sampling_rate_n_Hz.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView sampling_rate_v = (TextView) view.findViewById(R.id.sampling_rate_v);
		sampling_rate_v.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//sampling_rate_v_spinner
		TextView sampling_rate_v_Hz = (TextView) view.findViewById(R.id.sampling_rate_v_Hz);
		sampling_rate_v_Hz.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.Acquisition_title)).setText(R.string.AcquisitionSetting);
		((TextView)view.findViewById(R.id.sampling_rate_n)).setText(R.string.sampling_rate_n);
		((TextView)view.findViewById(R.id.sampling_rate_v)).setText(R.string.sampling_rate_v);
	}
}
