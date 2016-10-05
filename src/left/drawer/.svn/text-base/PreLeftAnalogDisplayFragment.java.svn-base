package left.drawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.mobileacquisition.R;
import common.XclSingalTransfer;

public class PreLeftAnalogDisplayFragment extends Fragment implements
		OnClickListener {
	
	private View view = null;
	private CheckBox physical,transducer,point,direction,coupling,sensitivity,range;
	private String checkName[] = {"physical","transducer","point","direction","coupling","sensitivity","range"};
	private ArrayList<CheckBox> checkBoxs = new ArrayList<CheckBox>();
	public Button save,restore_default;
	private Map<Integer,Integer> analogVisibleMap=new HashMap<Integer,Integer>();
	private SharedPreferences.Editor editor;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.analog_display_settings, container,
				false);
		// view.findViewById(R.id.Back_Ai).setOnClickListener(this);
		initViewListener();
		
		getState();
		
		return view;
	}

	private void saveState() {
		// TODO Auto-generated method stub
		SharedPreferences mySharedPreferences= getActivity().getSharedPreferences("displayInfo",Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		
		for (int i = 0; i < checkBoxs.size(); i ++) {
			if (checkBoxs.get(i).isChecked()) {
				editor.putBoolean(checkName[i], true);
			} else {
				editor.putBoolean(checkName[i], false);
			}
		}
		editor.commit();
	}
	
	private void getState() {
		// TODO Auto-generated method stub
		SharedPreferences mySharedPreferences= getActivity().getSharedPreferences("displayInfo",Activity.MODE_PRIVATE);
		Boolean currentState = false;
		
		for (int i = 0; i < checkBoxs.size(); i ++) {
			currentState = mySharedPreferences.getBoolean(checkName[i],true);
			checkBoxs.get(i).setChecked(currentState);
			if(currentState){
				analogVisibleMap.put(i, 0);
			}else{
				analogVisibleMap.put(i, 8);
			}
			
			checkBoxs.get(i).setButtonDrawable(checkBoxs.get(i).isChecked()?R.drawable.checked_active:R.drawable.checked);
			XclSingalTransfer xclSingalTransfer2=XclSingalTransfer.getInstance();
			xclSingalTransfer2.putValue("analogVisibleMap", analogVisibleMap);
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.Physical:
			if (physical.isChecked()) {
				physical.setButtonDrawable(R.drawable.checked_active);
				physical.setChecked(true);
				analogVisibleMap.put(0, 0);
			} else {
				physical.setButtonDrawable(R.drawable.checked);
				physical.setChecked(false);
				analogVisibleMap.put(0, 8);
			}
			break;
		case R.id.Transducer:
			if (transducer.isChecked()) {
				transducer.setButtonDrawable(R.drawable.checked_active);
				transducer.setChecked(true);
				analogVisibleMap.put(1, 0);
			} else {
				transducer.setButtonDrawable(R.drawable.checked);
				transducer.setChecked(false);
				analogVisibleMap.put(1, 8);
			}
			break;
		case R.id.Point:
			if (point.isChecked()) {
				point.setButtonDrawable(R.drawable.checked_active);
				point.setChecked(true);
				analogVisibleMap.put(2, 0);
			} else {
				point.setButtonDrawable(R.drawable.checked);
				point.setChecked(false);
				analogVisibleMap.put(2, 8);
			}
			break;
		case R.id.Direction:
			if (direction.isChecked()) {
				direction.setButtonDrawable(R.drawable.checked_active);
				direction.setChecked(true);
				analogVisibleMap.put(3,0);
			} else {
				direction.setButtonDrawable(R.drawable.checked);
				direction.setChecked(false);
				analogVisibleMap.put(3, 8);
			}
			break;
		case R.id.Coupling:
			if (coupling.isChecked()) {
				coupling.setButtonDrawable(R.drawable.checked_active);
				coupling.setChecked(true);
				analogVisibleMap.put(4, 0);
			} else {
				coupling.setButtonDrawable(R.drawable.checked);
				coupling.setChecked(false);
				analogVisibleMap.put(4, 8);
			}
			break;
		case R.id.Sensitivity:
			if (sensitivity.isChecked()) {
				sensitivity.setButtonDrawable(R.drawable.checked_active);
				sensitivity.setChecked(true);
				analogVisibleMap.put(5, 0);
			} else {
				sensitivity.setButtonDrawable(R.drawable.checked);
				sensitivity.setChecked(false);
				analogVisibleMap.put(5, 8);
			}
			break;
		case R.id.Range:
			if (range.isChecked()) {
				range.setButtonDrawable(R.drawable.checked_active);
				range.setChecked(true);
				analogVisibleMap.put(6, 0);
			} else {
				range.setButtonDrawable(R.drawable.checked);
				range.setChecked(false);
				analogVisibleMap.put(6, 8);
			}
			break;
		case R.id.Save:
			saveState();
			XclSingalTransfer xclSingalTransfer=XclSingalTransfer.getInstance();
			xclSingalTransfer.putValue("analogVisibleMap", analogVisibleMap);
			Toast.makeText(getActivity().getApplicationContext(),
					R.string.Save_settings, Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.Restore_default:
			physical.setChecked(true);
			analogVisibleMap.put(0, 0);
			transducer.setChecked(true);
			analogVisibleMap.put(1, 0);
			point.setChecked(true);
			analogVisibleMap.put(2, 0);
			coupling.setChecked(true);
			analogVisibleMap.put(4, 0);
			sensitivity.setChecked(true);
			direction.setChecked(true);
			analogVisibleMap.put(5, 0);
			range.setChecked(true);
			analogVisibleMap.put(6, 0);
			
			physical.setButtonDrawable(R.drawable.checked_active);
			transducer.setButtonDrawable(R.drawable.checked_active);
			point.setButtonDrawable(R.drawable.checked_active);
			coupling.setButtonDrawable(R.drawable.checked_active);
			direction.setButtonDrawable(R.drawable.checked_active);
			sensitivity.setButtonDrawable(R.drawable.checked_active);
			range.setButtonDrawable(R.drawable.checked_active);
			saveState();
			XclSingalTransfer xclSingalTransfer2=XclSingalTransfer.getInstance();
			xclSingalTransfer2.putValue("analogVisibleMap", analogVisibleMap);
			Toast.makeText(getActivity(),R.string.Recover_settings,Toast.LENGTH_SHORT).show();
			break;
		}
	}

	public void initViewListener() {
		physical=(CheckBox)view.findViewById(R.id.Physical);
		transducer=(CheckBox)view.findViewById(R.id.Transducer);
		point=(CheckBox)view.findViewById(R.id.Point);
		direction=(CheckBox)view.findViewById(R.id.Direction);
		coupling=(CheckBox)view.findViewById(R.id.Coupling);
		sensitivity=(CheckBox)view.findViewById(R.id.Sensitivity);
		range=(CheckBox)view.findViewById(R.id.Range);
		checkBoxs.add(physical);
		checkBoxs.add(transducer);
		checkBoxs.add(point);
		checkBoxs.add(direction);
		checkBoxs.add(coupling);
		checkBoxs.add(sensitivity);
		checkBoxs.add(range);
		
		physical.setOnClickListener(this);
		transducer.setOnClickListener(this);
		point.setOnClickListener(this);
		direction.setOnClickListener(this);
		coupling.setOnClickListener(this);
		sensitivity.setOnClickListener(this);
		range.setOnClickListener(this);
		
		save=(Button)view.findViewById(R.id.Save);
		restore_default=(Button)view.findViewById(R.id.Restore_default);
		
		save.setOnClickListener(this);
		restore_default.setOnClickListener(this);
		
	}
	public void languageChanged(){
		physical.setText(R.string.Physical);
		transducer.setText(R.string.Transducer);
		point.setText(R.string.Point);
		direction.setText(R.string.Direction);
		coupling.setText(R.string.Coupling);
		sensitivity.setText(R.string.Sensitivity);
		range.setText(R.string.Range);
		save.setText(R.string.Save);
		restore_default.setText(R.string.Recover);
	}
}