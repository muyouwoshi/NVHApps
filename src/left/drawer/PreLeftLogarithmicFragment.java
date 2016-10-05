package left.drawer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.R;
import common.XclSingalTransfer;

public class PreLeftLogarithmicFragment extends Fragment implements OnClickListener{
	
	private View view=null;
	private EditText noise,a0_EditText_index;
	private EditText Acc,p0_EditText_index;
	private Handler logarithmSettingHandler;
	public Button Restore_default,Save;
	private List<String> logrithmList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.logarithmic_setting, container,false);
		
		 Save = (Button) view.findViewById(R.id.Save);
		 Restore_default = (Button) view.findViewById(R.id.Restore_default);
		 Save.setOnClickListener(this);
		 Restore_default.setOnClickListener(this);
		noise = (EditText) view.findViewById(R.id.a0_EditText);
		Acc = (EditText) view.findViewById(R.id.p0_EditText);
		a0_EditText_index = (EditText) view.findViewById(R.id.a0_EditText_index);
		p0_EditText_index = (EditText) view.findViewById(R.id.p0_EditText_index);
		setValues();
		logarithmSettingHandler=(Handler)XclSingalTransfer.getInstance().getValue("logarithmSettingHandler");
		logrithmList=new ArrayList<String>();
		return view;
	}
	
	private void setValues() {
		SharedPreferences preferences = getActivity().getSharedPreferences("displayInfo", 0);
		noise.setText(preferences.getString("Noise", ""));
		Acc.  setText(preferences.getString("Acc"  , ""));
		a0_EditText_index.setText(preferences.getString("a0_EditText_index_str"  , ""));
		p0_EditText_index.setText(preferences.getString("p0_EditText_index_str"  , ""));
		if(preferences.getString("Noise", "").equals("")){
			noise.setText("1");
		}
		if(preferences.getString("a0_EditText_index_str"  , "").equals("")){
			a0_EditText_index.setText("-5");
		}
		if(preferences.getString("Acc", "").equals("")){
			Acc.setText("2");
		}
		if(preferences.getString("p0_EditText_index_str"  , "").equals("")){
			p0_EditText_index.setText("-5");
		}
	}
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.Save:
			save();
			Toast.makeText(
					view.getContext(),//fileNameRule.getText().toString()+
					 R.string.Save_settings,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.Restore_default:
			noise.setText("1");
			a0_EditText_index.setText("-5");
			Acc.setText("2");
			p0_EditText_index.setText("-5");
			save();
			Toast.makeText(
					view.getContext(),R.string.Recover_settings,
					Toast.LENGTH_SHORT).show();
			break;
		}
		
		
	}
	private void save(){
		SharedPreferences preferences = getActivity().getSharedPreferences("displayInfo", 0);
		SharedPreferences.Editor editor = preferences.edit();
		String acoustic= noise.getText().toString().trim();
		String a0_EditText_index_str= a0_EditText_index.getText().toString().trim();
		String acceleration= Acc.getText().toString().trim();
		String p0_EditText_index_str= p0_EditText_index.getText().toString().trim();
		if(acoustic.equals("")||acceleration.equals("")||a0_EditText_index_str.equals("")||p0_EditText_index_str.equals("")){
			Toast.makeText(getActivity(),R.string.Save_settings_failed, Toast.LENGTH_SHORT).show();
			return;
		}
	
		editor.putString("Noise", acoustic);
		editor.putString("a0_EditText_index_str", a0_EditText_index_str);
		editor.putString("Acc",   acceleration);
		editor.putString("p0_EditText_index_str", p0_EditText_index_str);
		editor.commit();
		if(logarithmSettingHandler!=null){
			logrithmList.clear();
			logrithmList.add(acoustic);
			logrithmList.add(acceleration);
			Message msg=logarithmSettingHandler.obtainMessage();
			msg.obj=logrithmList;
			logarithmSettingHandler.sendMessage(msg);
		}
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.acoustic_log)).setText(R.string.acoustic_log);
		((TextView)view.findViewById(R.id.acceleration_log)).setText(R.string.acceleration_log);
		((Button)view.findViewById(R.id.Save)).setText(R.string.Save);
		((Button)view.findViewById(R.id.Restore_default)).setText(R.string.Recover);
		
	}
}