package left.drawer;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import common.ScaleView;

public class PreLeftDecimalFragment extends Fragment implements OnClickListener{
	private View view;
	private Spinner paSpinner,dbSpinner,rpmSpinner,hzSpinner,sSpinner,msSpinner,percentSpinner;
	public Button save,recover;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.decimal_setting, container, false);
		 paSpinner = (Spinner) view.findViewById(R.id.pa_spinner);
		 dbSpinner = (Spinner) view.findViewById(R.id.db_spinner);
		 rpmSpinner = (Spinner) view.findViewById(R.id.rpm_spinner);
		 hzSpinner = (Spinner) view.findViewById(R.id.hz_spinner);
		 sSpinner = (Spinner) view.findViewById(R.id.s_spinner);
		 msSpinner = (Spinner) view.findViewById(R.id.ms_spinner);
		 percentSpinner = (Spinner) view.findViewById(R.id.percent_spinner);
		 save = (Button) view.findViewById(R.id.Save);
		 recover = (Button) view.findViewById(R.id.Restore_default);
		 save.setOnClickListener(this);
		 recover.setOnClickListener(this);
		 
		 init();
		 
		 return view;
	}
	
	

	private void init() {
		// TODO Auto-generated method stub
		SharedPreferences preferences=getActivity().getSharedPreferences("displayInfo", Context.MODE_PRIVATE);

		setSlectedItem(paSpinner, ""+preferences.getInt("pa_digits", 2));
		setSlectedItem(dbSpinner, ""+preferences.getInt("db_digits", 1));
		setSlectedItem(rpmSpinner, ""+preferences.getInt("rpm_digits", 1));
		setSlectedItem(hzSpinner, ""+preferences.getInt("hz_digits_set", 1));
		setSlectedItem(sSpinner, ""+preferences.getInt("s_digits", 3));
		setSlectedItem(msSpinner, ""+preferences.getInt("ms_digits", 0));
		setSlectedItem(percentSpinner, ""+preferences.getInt("percent_digits", 1));

		
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()== save.getId()){
			save();
			Toast.makeText(getActivity(),
					R.string.Save_settings, Toast.LENGTH_SHORT)
					.show();	
		}
		else if(v.getId()==recover.getId()){
			recover();
			save();
			Toast.makeText(
					getActivity(),R.string.Recover_settings,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void recover() {
//		SharedPreferences.Editor editor=getActivity().getSharedPreferences("displayInfo", Context.MODE_PRIVATE).edit();
//		editor.putInt("pa_digits", 3);
//		editor.putInt("db_digits", 1);
//		editor.putInt("rpm_digits", 1);
//		editor.putInt("hz_digits", 1);
//		editor.putInt("s_digits", 3);
//		editor.putInt("ms_digits", 0);
//		editor.commit();
//		
		paSpinner.setSelection(1);
		dbSpinner.setSelection(1);
		rpmSpinner.setSelection(1);
		hzSpinner.setSelection(1);
		sSpinner.setSelection(3);
		msSpinner.setSelection(0);
		percentSpinner.setSelection(1);
	}

	private void save() {
		SharedPreferences.Editor editor=getActivity().getSharedPreferences("displayInfo", Context.MODE_PRIVATE).edit();
		editor.putInt("pa_digits", Integer.parseInt(paSpinner.getSelectedItem().toString()));
		editor.putInt("db_digits", Integer.parseInt(dbSpinner.getSelectedItem().toString()));
		editor.putInt("rpm_digits", Integer.parseInt(rpmSpinner.getSelectedItem().toString()));
		editor.putInt("hz_digits_set", Integer.parseInt(hzSpinner.getSelectedItem().toString()));
		editor.putInt("s_digits", Integer.parseInt(sSpinner.getSelectedItem().toString()));
		editor.putInt("ms_digits", Integer.parseInt(msSpinner.getSelectedItem().toString()));
		editor.putInt("percent_digits", Integer.parseInt(percentSpinner.getSelectedItem().toString()));

		editor.commit();
		
		ArrayList<View> views = ((MainActivity)getActivity()).getViewList();
		for(int i = 0; i< views.size();i++){
			((ScaleView)views.get(i)).refreshLabelState();
		}
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
	public void languageChanged(){
		((TextView)view.findViewById(R.id.decimal_set1)).setText(getResources().getString(R.string.decimal_set));
		((TextView)view.findViewById(R.id.decimal_set2)).setText(getResources().getString(R.string.decimal_set));
		((TextView)view.findViewById(R.id.decimal_set3)).setText(getResources().getString(R.string.decimal_set));
		((TextView)view.findViewById(R.id.decimal_set4)).setText(getResources().getString(R.string.decimal_set));
		((TextView)view.findViewById(R.id.decimal_set5)).setText(getResources().getString(R.string.decimal_set));
		((TextView)view.findViewById(R.id.decimal_set6)).setText(getResources().getString(R.string.decimal_set));
		((TextView)view.findViewById(R.id.decimal_set7)).setText(getResources().getString(R.string.decimal_set));
		((Button)view.findViewById(R.id.Save)).setText(R.string.Save);
		((Button)view.findViewById(R.id.Restore_default)).setText(R.string.Recover);
	}
}
