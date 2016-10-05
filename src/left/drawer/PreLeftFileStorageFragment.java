package left.drawer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bottom.drawer.BottomOperate;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;

public class PreLeftFileStorageFragment extends Fragment {
	private View view = null;
	private Spinner projectModeChangeSpinner, measurementModeChangeSpinner;
	private EditText projectFileNameRule, measurementFileNameRule;
	private Spinner pathSpinner;
	private Spinner projectFolderSpinner;
	private int saveMethod;
	public Button Restore_default ,Save;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.file_storage_settings, container, false);
		// view.findViewById(R.id.Back_Ai).setOnClickListener(this);
		projectFileNameRule = (EditText) view.findViewById(R.id.project_file_naming_rule_edit);
		measurementFileNameRule = (EditText) view.findViewById(R.id.measurement_file_naming_rule_edit);
		pathSpinner = (Spinner) view.findViewById(R.id.path_spinner);
		projectFolderSpinner = (Spinner) view.findViewById(R.id.projectFolderSpinner);
		projectModeChangeSpinner = (Spinner) view.findViewById(R.id.Mode_Switch_spinner);
		measurementModeChangeSpinner = (Spinner) view.findViewById(R.id.measurement_Mode_Switch_spinner);
		Save = (Button) view.findViewById(R.id.Save);
		Restore_default = (Button) view.findViewById(R.id.Restore_default);
		ArrayAdapter<String> mAdapter = new PreFileModeSpinnerAdapter(getActivity(), getActivity().getResources().getStringArray(R.array.Mode_Switch));
		projectModeChangeSpinner.setAdapter(mAdapter);
		measurementModeChangeSpinner.setAdapter(mAdapter);
		mAdapter = new PreFileModeSpinnerAdapter(getActivity(), getActivity().getResources().getStringArray(R.array.Data_Storage_Directory));
		pathSpinner.setAdapter(mAdapter);
		mAdapter = new PreFileModeSpinnerAdapter(getActivity(), getActivity().getResources().getStringArray(R.array.project_folder));
		projectFolderSpinner.setAdapter(mAdapter);
		
		setProjectFolderSelectListener();		// �����ļ����л�
		initModeChangeListener();				// 2������ģʽ�л�
		initfilePageSaveListener();				// ����
		initfilePageRestoreDefaultListener();	// ����
		setContent();							// ������������

		return view;
	}

	/*
	public void refresh() { 
		LeftLanguageChanged leftLanguageChanged = new LeftLanguageChanged();
		leftLanguageChanged.setPreLeftFileStorageView(view); 
	}
	 */

	/** ---------------�����ļ��������˵�--------------------*/
	private void setProjectFolderSelectListener() {
		projectFolderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				saveMethod = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/** ---------------ģʽ�л������˵�--------------------*/
	private void initModeChangeListener() {

		projectModeChangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				switch (position) {
				case 0:
					projectFileNameRule.setFocusable(true);
					projectFileNameRule.setFocusableInTouchMode(true);
					projectFileNameRule.setAlpha(0.8f);
					break;
				case 1:
					projectFileNameRule.setFocusable(false);
					projectFileNameRule.setFocusableInTouchMode(false);
					projectFileNameRule.setAlpha(0.5f);
					projectFileNameRule.setText("Project");
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		measurementModeChangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				switch (position) {
				case 0:
					measurementFileNameRule.setFocusable(true);
					measurementFileNameRule.setFocusableInTouchMode(true);
					measurementFileNameRule.setAlpha(0.8f);
					break;
				case 1:
					measurementFileNameRule.setFocusable(false);
					measurementFileNameRule.setFocusableInTouchMode(false);
					measurementFileNameRule.setAlpha(0.5f);
					measurementFileNameRule.setText("measurement");
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/** ---------------���水ť-------------------- */
	private void initfilePageSaveListener() {
		((Button) view.findViewById(R.id.Save)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// --------------�ļ�������------------
				if (projectFileNameRule.getText().toString().equals("")) { // Ϊ�յ��������
					new AlertDialog.Builder(view.getContext()).setTitle(R.string.warning).setMessage(R.string.recorver_default).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// method stub
							projectModeChangeSpinner.setSelection(1);
							projectFileNameRule.setText("Project");
							// measurementModeChangeSpinner.setSelection(1);
							// measurementFileNameRule.setText("template");
							saveValues();
						}
					}).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Toast.makeText(view.getContext(), R.string.Filename_empty, Toast.LENGTH_SHORT).show();
						}
					}).create().show();
				} else if (measurementFileNameRule.getText().toString().equals("")) {
					new AlertDialog.Builder(view.getContext()).setTitle(R.string.warning).setMessage(R.string.recorver_default).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// method stub
							// projectModeChangeSpinner.setSelection(1);
							// /projectFileNameRule.setText("measurement");
							measurementModeChangeSpinner.setSelection(1);
							measurementFileNameRule.setText("measurement");
							saveValues();
						}
					}).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Toast.makeText(view.getContext(), R.string.Filename_empty, Toast.LENGTH_SHORT).show();
						}
					}).create().show();
				} else {
					saveValues();
					Toast.makeText(view.getContext(),// fileNameRule.getText().toString()+
							R.string.Save_settings, Toast.LENGTH_SHORT).show();
				}
			}// end onclick
		});
	}

	/** ---------------���ð�ť-------------------- */
	private void initfilePageRestoreDefaultListener() {
		((Button) view.findViewById(R.id.Restore_default)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				projectModeChangeSpinner.setSelection(1);
				measurementModeChangeSpinner.setSelection(1);
				pathSpinner.setSelection(0);
				projectFolderSpinner.setSelection(0);

				projectFileNameRule.setText("Project");
				measurementFileNameRule.setText("measurement");
				saveValues();
				Toast.makeText(view.getContext(), R.string.Recover_settings, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/** -----------------��ݴ洢Ŀ¼------------------*/
	private void initDataSaveCatalog() {
		pathSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				SharedPreferences prefenences = getActivity().getSharedPreferences("displayInfo", Context.MODE_PRIVATE);
				Editor editor = prefenences.edit();
				editor.putInt("dataSaveCatalog", position);
				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/** ���û����ù���󱣴���Щ���� */
	public void saveValues() {
		measurementFileNameRule.setText(measurementFileNameRule.getText().toString().replace("_", ""));
		projectFileNameRule.setText(projectFileNameRule.getText().toString().replace("_", ""));
		
		SharedPreferences preferences = getActivity().getSharedPreferences("displayInfo", 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("fileNameRule", projectFileNameRule.getText().toString().replace("_", ""));
		editor.putString("MeasurementFileNameRule", measurementFileNameRule.getText().toString().replace("_", ""));
		editor.putInt("modeChange", projectModeChangeSpinner.getSelectedItemPosition());
		editor.putInt("measurementModeChange", measurementModeChangeSpinner.getSelectedItemPosition());
		editor.putInt("Dir", pathSpinner.getSelectedItemPosition());
		editor.putInt("project", projectFolderSpinner.getSelectedItemPosition());
		editor.commit();
	}

	/** ��SP��ȡ�������� */
	private void setContent() {
		SharedPreferences preferences = getActivity().getSharedPreferences("displayInfo", 0);
		projectFileNameRule.setText(preferences.getString("fileNameRule", "Project"));
		measurementFileNameRule.setText(preferences.getString("MeasurementFileNameRule", "measurement"));
		projectModeChangeSpinner.setSelection(preferences.getInt("modeChange", 0));
		measurementModeChangeSpinner.setSelection(preferences.getInt("measurementModeChange", 0));
		pathSpinner.setSelection(preferences.getInt("Dir", 0));
		projectFolderSpinner.setSelection(preferences.getInt("project", 0));
	}

	public void languageChanged() {
		((EditText) view.findViewById(R.id.measurement_file_naming_rule_edit)).setHint(R.string.dont_enter_underline);
		((EditText) view.findViewById(R.id.project_file_naming_rule_edit)).setHint(R.string.dont_enter_underline);
		((TextView) view.findViewById(R.id.project_file_naming_rule)).setText(R.string.file_naming_rule);
		((TextView) view.findViewById(R.id.project_mode_switch)).setText(R.string.Mode_switch);
		((TextView) view.findViewById(R.id.measurement_file_naming_rule)).setText(R.string.file_naming_rule);
		((TextView) view.findViewById(R.id.measurement_mode_switch)).setText(R.string.Mode_switch);
		((TextView) view.findViewById(R.id.data_storage_directory)).setText(R.string.data_storage_directory);
		((TextView) view.findViewById(R.id.project_folder)).setText(R.string.project_folder);
		((Button) view.findViewById(R.id.Save)).setText(R.string.Save);
		((Button) view.findViewById(R.id.Restore_default)).setText(R.string.Recover);
		ArrayAdapter<String> mAdapter = new PreFileModeSpinnerAdapter(getActivity(), getActivity().getResources().getStringArray(R.array.Mode_Switch));
		projectModeChangeSpinner.setAdapter(mAdapter);
		measurementModeChangeSpinner.setAdapter(mAdapter);
		mAdapter = new PreFileModeSpinnerAdapter(getActivity(), getActivity().getResources().getStringArray(R.array.Data_Storage_Directory));
		pathSpinner.setAdapter(mAdapter);
		mAdapter = new PreFileModeSpinnerAdapter(getActivity(), getActivity().getResources().getStringArray(R.array.project_folder));
		projectFolderSpinner.setAdapter(mAdapter);
	}
}