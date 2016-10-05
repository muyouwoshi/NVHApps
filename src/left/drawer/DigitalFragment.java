package left.drawer;

import java.io.Serializable;

import com.example.mobileacquisition.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class DigitalFragment extends Fragment implements Serializable,OnClickListener,OnFocusChangeListener{
	
	private static final long serialVersionUID = -760644662584953751L;
	private Context context;
	private View view=null;
	private CheckBox chan1;
	private EditText threshold1;
	private EditText pluse1;
	private CheckBox chan2;
	private EditText threshold2;
	private EditText pluse2;
	private String chan1Str,threshold1Str,pluse1Str,chan2Str,threshold2Str,pluse2Str;
//	private SharedPreferences.Editor editor;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.digital, container, false);
		chan1 = (CheckBox) view.findViewById(R.id.digital_chan1);
		threshold1 = (EditText) view.findViewById(R.id.ch1_edit_Threshold);
		pluse1 = (EditText) view.findViewById(R.id.ch1_edit_Pluse);
		chan2 = (CheckBox) view.findViewById(R.id.digital_chan2);
		threshold2 = (EditText) view.findViewById(R.id.ch2_edit_Threshold);
		pluse2 = (EditText) view.findViewById(R.id.ch2_edit_Pluse);
		
		chan1.setOnClickListener(this);
		chan2.setOnClickListener(this);
		threshold1.setOnFocusChangeListener(this);
		threshold2.setOnFocusChangeListener(this);
		pluse1.setOnFocusChangeListener(this);
		pluse2.setOnFocusChangeListener(this);
		
		context = this.getActivity();
		/*LinearLayout digital_layout=(LinearLayout)view.findViewById(R.id.digital_layout);
		String getSkinValues = getActivity().getIntent().getStringExtra("SkinIntent");
		if (getSkinValues != null) {
			if (getSkinValues.equals("Skin0")) {
				digital_layout.setBackgroundColor(Color.WHITE);
			}else if (getSkinValues.equals("Skin1")) {
				digital_layout.setBackgroundColor(Color.LTGRAY);
			}
		}*/
		
		init();
		
		
		return view;
	}
	
	public  void init() {
		chan1.setChecked(chan1Str==null?false:chan1Str.equals("true"));
		threshold1.setText(threshold1Str);
		pluse1.setText(pluse1Str);
		chan2.setChecked(chan2Str==null?false:chan2Str.equals("true"));
		threshold2.setText(threshold2Str);
		pluse2.setText(pluse2Str);
	}
	
	public void readFromXml(FragmentActivity fragmentActivity) {

		SharedPreferences preference = fragmentActivity.getSharedPreferences("hz_5D", 0);
//		editor = preference.edit();
		chan1Str = preference.getString("Digital_Chan1", "false");
		threshold1Str = preference.getString("Ch1_Threshold", "");
		pluse1Str = preference.getString("Ch1_Pluse", "");
		chan2Str = preference.getString("Digital_Chan2", "false");
		threshold2Str = preference.getString("Ch2_Threshold", "");
		pluse2Str = preference.getString("Ch2_Pluse", "");
	}
	
	public void reset(FragmentActivity fragmentActivity){
		readFromXml(fragmentActivity);
		init();
	}
	/*	public void refresh() {
		LeftLanguageChanged leftLanguageChanged=new LeftLanguageChanged();
		leftLanguageChanged.setDigitalView(view);
	}*/
	@Override
	public void onClick(View v) {
		
		SharedPreferences preferences= context.getSharedPreferences("hz_5D", 0);
		SharedPreferences.Editor editor = preferences.edit();
		switch (v.getId()){
		case R.id.digital_chan1:
			editor.putString("Digital_Chan1", String.valueOf(((CheckBox) v).isChecked()));
			editor.putString("Ch1_Threshold",threshold1.getText().toString());
			editor.putString("Ch1_Pluse",pluse1.getText().toString());
			break;
		case R.id.digital_chan2:
			editor.putString("Digital_Chan2", String.valueOf(((CheckBox) v).isChecked()));
			editor.putString("Ch2_Threshold",threshold2.getText().toString());
			editor.putString("Ch2_Pluse",pluse2.getText().toString());
			break;
		}
		editor.commit();

	}
	
	
	@Override
	public void onFocusChange(View v, boolean value) {
		SharedPreferences preferences= context.getSharedPreferences("hz_5D", 0);
		SharedPreferences.Editor editor = preferences.edit();
		switch(v.getId()){
		case R.id.ch1_edit_Threshold:
			editor.putString("Ch1_Threshold",threshold1.getText().toString());
			break;
		case R.id.ch2_edit_Threshold:
			editor.putString("Ch2_Threshold",threshold2.getText().toString());
			break;
		case R.id.ch1_edit_Pluse:
			editor.putString("Ch1_Pluse",pluse1.getText().toString());
			break;
		case R.id.ch2_edit_Pluse:
			editor.putString("Ch2_Pluse",pluse2.getText().toString());
			break;
		}
		editor.commit();
	}
	
	public void languageChanged(){
		((CheckBox)view.findViewById(R.id.digital_chan1)).setText(R.string.chan1);
 		((TextView)view.findViewById(R.id.ch1_text_Threshold)).setText(R.string.Threshold);
 		((TextView)view.findViewById(R.id.ch1_text_Pluse)).setText(R.string.text_Pulse);
 		((CheckBox)view.findViewById(R.id.digital_chan2)).setText(R.string.chan2);
 		((TextView)view.findViewById(R.id.ch2_text_Threshold)).setText(R.string.Threshold);
 		((TextView)view.findViewById(R.id.ch2_text_Pluse)).setText(R.string.text_Pulse);
	}
}
