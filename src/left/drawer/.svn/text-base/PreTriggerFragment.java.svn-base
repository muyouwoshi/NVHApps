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

public class PreTriggerFragment extends Fragment implements OnClickListener,OnFocusChangeListener {
	private View view = null;
	private SharedPreferences.Editor editor;
	private EditText Pre_Trigger_time_edit;
	private String preTriggerTimeStr;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pre_trigger, container, false);
		view.setOnClickListener(this);
		Pre_Trigger_time_edit = (EditText) view.findViewById(R.id.Pre_Trigger_time_edit);
		view.findViewById(R.id.Back_PreTrig).setOnClickListener(this);
		Pre_Trigger_time_edit.setOnFocusChangeListener(this);
		init();
		return view;
	}
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
		Pre_Trigger_time_edit.setText(preTriggerTimeStr);

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
		PreTriggerFragment triggerFragment = (PreTriggerFragment) getFragmentManager()
				.findFragmentByTag("pretrig");
		switch (v.getId()) {
		case R.id.Back_PreTrig:
			getFragmentManager().beginTransaction().hide(triggerFragment)
					.show(mainFragment).commit();
			break;
		}
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		SharedPreferences preference = getActivity().getSharedPreferences(
				"hz_5D", 0);
		switch (v.getId()) {		
		case R.id.Pre_Trigger_time_edit:
			editor.putString("Pre_Trigger_Time",((EditText)v).getText().toString());
        	editor.commit();
			break;
		}
	}
	
	public void readFromXml(FragmentActivity fragmentActivity) {

		SharedPreferences preference = fragmentActivity.getSharedPreferences(
				"hz_5D", 0);
		editor = preference.edit();
		preTriggerTimeStr = preference.getString("Pre_Trigger_Time", "0");

	}
	
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.Pre_Trigger_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_trigger,Color.YELLOW));
		view.findViewById(R.id.Pre_Trigger_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		ImageButton Back_Trig = (ImageButton) view.findViewById(R.id.Back_PreTrig);
		Back_Trig.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		TextView Trigger_title = (TextView) view.findViewById(R.id.Pre_Trigger_title);
		Trigger_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));	
		TextView Pre_Trigger_time = (TextView) view.findViewById(R.id.Pre_Trigger_time);
		Pre_Trigger_time.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Pre_Trigger_time_unit = (TextView) view.findViewById(R.id.Pre_Trigger_time_unit);
		Pre_Trigger_time_unit.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.Pre_Trigger_title)).setText(R.string.left_childItems_22);
	    ((TextView)view.findViewById(R.id.Pre_Trigger_time)).setText(R.string.Pre_Trigger_time);
	}
}
