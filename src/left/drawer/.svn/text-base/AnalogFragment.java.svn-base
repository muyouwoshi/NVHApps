package left.drawer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import left.drawer.CustomSpinner.OnCostomListener;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.MainContextView;
import com.example.mobileacquisition.OneTabFragment;
import com.example.mobileacquisition.R;
import common.CustomTab;
import common.DataCollection;
import common.XclSingalTransfer;

/**
 * <b>����</b>: AnalogFragment��ͨ�����ý��� <br/>
 * <b>˵��</b>: �������ͨ�����ã�ͨ�����ã�ģ����ͨ�� <br/>
 */
public class AnalogFragment extends Fragment implements OnClickListener, OnCostomListener, OnFocusChangeListener, OnItemSelectedListener {
	private View view;
	public LinearLayout lvt;
	private ArrayList<ViewHolder> viewHolders;
	public Button copy, paste, checkAll;
	private Copy cp = new Copy();
	private LayoutInflater inflater = null;
	private int checkChannelNum = 0;
	// ͨ������
	private int Device_ChannelNum = 0;
	private CheckBox Noise, Vibration, AllChannel, HideChannel;
	private int maxChannelNum = 64;
	private int equipment_Num;
	/** ѡ���˵�ͨ������ */
	public static Map<Integer, ViewHolder> checkedViewMap;
	private XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
	private List<String> channel_spinnerList;
	public CustomTab ct;
	public TextView Chan,Physical,Transducer,Point,Direction,Coupling,Sensitivity,Sensitivity_unit,Range;
	public LinearLayout Type;
	//public static ArrayList<List<Integer>> channelRangeLevelList = new ArrayList<List<Integer>>();
	private List<Integer> levelList =new ArrayList<Integer>();
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.analog, container, false);
		Noise = (CheckBox) view.findViewById(R.id.Noise);
		// NoiseĬ��ѡ��
		Noise.setChecked(true);
		Vibration = (CheckBox) view.findViewById(R.id.Vibration);
		AllChannel = (CheckBox) view.findViewById(R.id.AllChannel);
		HideChannel = (CheckBox) view.findViewById(R.id.HideChannel);
		copy = (Button) view.findViewById(R.id.Copy);
		paste = (Button) view.findViewById(R.id.Paste);
		checkAll = (Button) view.findViewById(R.id.CheckAll);
		Chan = (TextView) view.findViewById(R.id.Chan);
		Physical = (TextView) view.findViewById(R.id.Physical);
		Transducer = (TextView) view.findViewById(R.id.Transducer);
		Point = (TextView) view.findViewById(R.id.Point);
		Direction = (TextView) view.findViewById(R.id.Direction);
		Coupling = (TextView) view.findViewById(R.id.Coupling);
		Sensitivity = (TextView) view.findViewById(R.id.Sensitivity);
		Sensitivity_unit = (TextView) view.findViewById(R.id.Sensitivity_unit);
		Type = (LinearLayout) view.findViewById(R.id.Type);
		Range = (TextView) view.findViewById(R.id.Range);
		Noise.setOnClickListener(this);
		Vibration.setOnClickListener(this);
		AllChannel.setOnClickListener(this);
		HideChannel.setOnClickListener(this);
		copy.setOnClickListener(this);
		paste.setOnClickListener(this);
		checkAll.setOnClickListener(this);
		ct = ((MainActivity) getActivity()).mainCustomTab;
		channel_spinnerList = ct.channel_spinnerList;
		this.inflater = inflater;
		viewHolders = new ArrayList<ViewHolder>();
		lvt = (LinearLayout) view.findViewById(R.id.ChannelList);
		SharedPreferences preference = getActivity().getSharedPreferences("Equipment", 0);
		equipment_Num = preference.getInt("Equipment", 0);
		switch (equipment_Num) {
		case 0:// ��Ӳ��
			Device_ChannelNum = 64;
			break;
		case 1:// �ֻ�
			Device_ChannelNum = 1;
			break;
		case 2:// Ӳ��
			Device_ChannelNum = 8;
			break;
		}
		InitChannelList(Device_ChannelNum);

		return view;
	}

	public void InitChannelList(int ChannelNum) {
		Noise.setChecked(true);
		Vibration.setChecked(false);
		AllChannel.setChecked(false);
		HideChannel.setChecked(false);
		copy.setSelected(false);
		paste.setSelected(false);
		checkAll.setSelected(false);
		//this.ChannelNum = ChannelNum;
		this.Device_ChannelNum = ChannelNum;
		SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);

		for (int i = 1; i <= ChannelNum; i++) {
			ViewHolder viewHolder = new ViewHolder();
			View view = inflater.inflate(R.layout.analog_list, lvt, false);
			view.setTag(i - 1);
			viewHolder.view = view;
			((CheckBox) view.findViewById(R.id.Check)).setText("" + i);
			((CustomSpinner) view.findViewById(R.id.Physical)).setOnCustomListener(this);

			viewHolder.check = (CheckBox) view.findViewById(R.id.Check);
			viewHolder.check.setTag(i - 1);
			viewHolder.Physical = (CustomSpinner) view.findViewById(R.id.Physical);
			// �ֻ�ģʽ�£����鲻��ѡ
			if (Device_ChannelNum == 1) {
				viewHolder.Physical.setEnabled(false);
			}
			viewHolder.Physical.setTag(i - 1);
		    //ʹ���Զ����ArrayAdapter
			ArrayAdapter<String>  mAdapter = new AnalogSpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Physical));
		    
		    //���������б���(��䲻ЩҲ��)
		    //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			viewHolder.Physical.setAdapter(mAdapter);
			
			
			viewHolder.Transducer = (EditText) view.findViewById(R.id.Transducer);
			viewHolder.Transducer.setTag(i - 1);

			viewHolder.Point = (EditText) view.findViewById(R.id.Point);
			viewHolder.Point.setTag(i - 1);
			viewHolder.Direction = (CustomSpinner) view.findViewById(R.id.Direction);
			viewHolder.Direction.setTag(i - 1);
			 //ʹ���Զ����ArrayAdapter
			mAdapter = new AnalogSpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Direction));
		    //���������б���(��䲻ЩҲ��)
		    //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			viewHolder.Direction.setAdapter(mAdapter);
			
			
			viewHolder.Coupling = (CustomSpinner) view.findViewById(R.id.Coupling);
			viewHolder.Coupling.setTag(i - 1);
			mAdapter = new AnalogSpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Coupling));
		    //���������б���(��䲻ЩҲ��)
		    //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			viewHolder.Coupling.setAdapter(mAdapter);
			
			viewHolder.Sensitivity = (EditText) view.findViewById(R.id.Sensitivity);
			viewHolder.Sensitivity.setTag(i - 1);
			viewHolder.Unit = (CustomSpinner) view.findViewById(R.id.unit);
			viewHolder.Unit.setTag(i - 1);
			mAdapter = new AnalogSpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Unit));
		    //���������б���(��䲻ЩҲ��)
		    //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			viewHolder.Unit.setAdapter(mAdapter);
			viewHolder.Range = (CustomSpinner) view.findViewById(R.id.Range);
			viewHolder.Range.setTag(i - 1);
			mAdapter = new AnalogSpinnerAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.Range));
		    //���������б���(��䲻ЩҲ��)
		    //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			viewHolder.Range.setAdapter(mAdapter);
			viewHolder.Range.setSelection(0, true);
			viewHolder.check.setOnClickListener(this);
			viewHolder.Transducer.setOnFocusChangeListener(this);
			viewHolder.Physical.setOnCustomListener(this);
			viewHolder.Physical.setOnItemSelectedListener(this);
			viewHolder.Point.setOnFocusChangeListener(this);
			viewHolder.Direction.setOnCustomListener(this);
			viewHolder.Direction.setOnItemSelectedListener(this);
			viewHolder.Coupling.setOnCustomListener(this);
			viewHolder.Coupling.setOnItemSelectedListener(this);
			viewHolder.Sensitivity.setOnFocusChangeListener(this);
			viewHolder.Unit.setOnCustomListener(this);
			viewHolder.Unit.setOnItemSelectedListener(this);

			lvt.addView(view, new LayoutParams(-1, -2));
			viewHolders.add(viewHolder);
			int channelNum = Integer.parseInt(preference.getString("Channel" + (i - 1), "" + (-1)));
			if (channelNum == (i - 1)) {
				viewHolder.check.setChecked(true);
				viewHolder.check.setButtonDrawable(R.drawable.checked_active);
				addChannelSpinnerListValues(channelNum);
				if (equipment_Num != 1) {
					setSlectedItem(viewHolder.Physical, preference.getString("Physical" + (i - 1), "Noise"));
				}
				viewHolder.Transducer.setText(preference.getString("Transducer" + (i - 1), ""));
				viewHolder.Point.setText(preference.getString("Point" + (i - 1), ""));
				setSlectedItem(viewHolder.Direction, preference.getString("Direction" + (i - 1), "+X"));
				setSlectedItem(viewHolder.Coupling, preference.getString("Coupling" + (i - 1), "ICP"));
				viewHolder.Sensitivity.setText(preference.getString("Sensitivity" + (i - 1), ""));
				setSlectedItem(viewHolder.Unit, preference.getString("Unit" + (i - 1), "mV/Pa"));
				setSlectedItem(viewHolder.Range, preference.getString("Range" + (i - 1), "10V"));
				String range=preference.getString("Range" + (i - 1), "10V");
				/*if("100mV".equals(range)){
					levelList.add(2);
				} else if("1V".equals(range)){
					levelList.add(1);
				} else if("10V".equals(range)){
					levelList.add(0);
				}*/
				equipment_Num = getActivity().getSharedPreferences("Equipment", 0).getInt("Equipment", 0);
				if (equipment_Num != 0) {
					XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
					Handler buttonStateChangeHandler = (Handler) xclSingalTransfer.getValue("buttonStateChange");
					Message buttonStateChangeMessage = buttonStateChangeHandler.obtainMessage();
					buttonStateChangeMessage.what = 1;
					buttonStateChangeMessage.arg1 = i - 1;
					buttonStateChangeHandler.sendMessageAtFrontOfQueue(buttonStateChangeMessage);
				}
				for (int j = 0; j < viewHolders.size(); j++) {
					if (checkedViewMap == null) {
						checkedViewMap = new HashMap<Integer, ViewHolder>();
					}
					checkedViewMap.put(j + 1, viewHolders.get(j));
				}
			}
			viewHolder.Range.setOnCustomListener(this);
			viewHolder.Range.setOnItemSelectedListener(this);
			
		}
		/*if(channelRangeLevelList.size()==0){
			channelRangeLevelList.add(levelList);
		}*/
		getXcalSingalTransfer();
		setViewHolderVisbleHolder();
		changeChannelRange();// ������ͨ��range����
	}
	public void addListener() {

	}

	public void clearOldChannel() {
		SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.remove("ChannelNum");
		equipment_Num = getActivity().getSharedPreferences("Equipment", 0).getInt("Equipment", 0);
		viewHolders.clear();
		for (int i = 0; i < maxChannelNum; i++) {
			editor.remove("Channel" + i);
			editor.remove("Physical" + i);
			editor.remove("Transducer" + i);
			editor.remove("Point" + i);
			editor.remove("Direction" + i);
			editor.remove("Coupling" + i);
			editor.remove("Sensitivity" + i);
			editor.remove("Unit" + i);
			editor.remove("Range" + i);
			editor.commit();
			XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();

			// equipment_Num = getActivity().getSharedPreferences(
			// "Equipment", 0).getInt("Equipment", 0);
			// if (equipment_Num != 0) {
			// Handler buttonStateChangeHandler = (Handler) xclSingalTransfer
			// .getValue("buttonStateChange");
			// Message buttonStateChangeMessage = buttonStateChangeHandler
			// .obtainMessage();
			// buttonStateChangeMessage.what = 0;
			// buttonStateChangeMessage.arg1 = i;
			// buttonStateChangeHandler
			// .sendMessageAtFrontOfQueue(buttonStateChangeMessage);
			// }
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!copy.isSelected() && !hasFocus) {
			SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
			SharedPreferences.Editor editor = preference.edit();
			switch (v.getId()) {
			case R.id.Transducer:
				editor.putString("Transducer" + v.getTag(), ((EditText) v).getText().toString());
				break;
			case R.id.Point:
				editor.putString("Point" + v.getTag(), ((EditText) v).getText().toString());
				break;
			case R.id.Sensitivity:
				editor.putString("Sensitivity" + v.getTag(), ((EditText) v).getText().toString());
				break;
			}
			editor.commit();
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.Noise:
			setChecked(v);
			HintTypeChannel(0);
			returnButton();
			break;
		case R.id.Vibration:
			setChecked(v);
			HintTypeChannel(1);
			returnButton();
			break;
		case R.id.AllChannel:
			setChecked(v);
			HintTypeChannel(-1);
			break;
		case R.id.HideChannel:
			HintUnactivatedChannels(HideChannel.isChecked());
			returnButton();
			break;
		case R.id.Paste:
			if (!paste.isSelected()) {
				copy.setSelected(!copy.isSelected());
				setAllEnable(copy.isSelected() ? false : true);
				copy.setText(R.string.Copy);
				setAllCheck(false);
				paste.setText(R.string.Cancel);
			} else {
				resetChannel();
				paste.setText(R.string.Paste);
			}
			copy.setSelected(false);
			setAllEnable(true);
			paste.setSelected(!paste.isSelected());

			break;
		case R.id.Copy:
			if (!copy.isSelected()) {
				setAllCheck(false);
				copy.setText(R.string.Cancel);
			} else {
				resetChannel();
				copy.setText(R.string.Copy);
				
			}
			paste.setSelected(false);
			copy.setSelected(!copy.isSelected());
			// copy.setSelected(copy.isSelected() ? false : true);
			// setAllCheck(false);
			setAllEnable(copy.isSelected() ? false : true);
			break;
			
		// ���item��˵ĸ�ѡ��
		case R.id.Check:
			if (checkedViewMap == null) {
				checkedViewMap = new HashMap<Integer, ViewHolder>();
			}
			int tag = (Integer) v.getTag();
			if (copy.isSelected()) {
				Item2Copy(tag);
				setAllCheck(false);
				((CheckBox) v).setChecked(true);
				((CheckBox) v).setButtonDrawable(R.drawable.checked_active);
				setAllEnable(false);
				setItemEnable(tag);
			} else if (paste.isSelected()) {
				Copy2Item(tag);
			} else {
				// ���d������ͨ��id����
				SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
				SharedPreferences.Editor editor = preference.edit();
				if (((CheckBox) v).isChecked()) {
					((CheckBox) v).setSelected(true);
					((CheckBox) v).setButtonDrawable(R.drawable.checked_active);
					int channelNum = (Integer) v.getTag();
					checkChannelNum++;
					editor.putString("checkChannelNum", String.valueOf(checkChannelNum));
					editor.putString("Channel" + channelNum, "" + channelNum);
					editor.putString("Physical" + v.getTag(), viewHolders.get((Integer) v.getTag()).Physical.getSelectedItem().toString());
					editor.putString("Transducer" + v.getTag(), viewHolders.get((Integer) v.getTag()).Transducer.getText().toString());
					editor.putString("Point" + v.getTag(), viewHolders.get((Integer) v.getTag()).Point.getText().toString());
					editor.putString("Direction" + v.getTag(), viewHolders.get((Integer) v.getTag()).Direction.getSelectedItem().toString());
					editor.putString("Coupling" + v.getTag(), viewHolders.get((Integer) v.getTag()).Coupling.getSelectedItem().toString());
					editor.putString("Sensitivity" + v.getTag(), viewHolders.get((Integer) v.getTag()).Sensitivity.getText().toString());
					editor.putString("Unit" + v.getTag(), viewHolders.get((Integer) v.getTag()).Unit.getSelectedItem().toString());
					editor.putString("Range" + v.getTag(), viewHolders.get((Integer) v.getTag()).Range.getSelectedItem().toString());
					editor.commit();
					
					// ͨ���������Ϣ
					XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
					equipment_Num = getActivity().getSharedPreferences("Equipment", 0).getInt("Equipment", 0);
					if (equipment_Num != 0) {
						Handler buttonStateChangeHandler = (Handler) xclSingalTransfer.getValue("buttonStateChange");
						if (buttonStateChangeHandler != null) {
							Message buttonStateChangeMessage = buttonStateChangeHandler.obtainMessage();
							buttonStateChangeMessage.what = 1;
							buttonStateChangeMessage.arg1 = (Integer) v.getTag();
							buttonStateChangeHandler.sendMessageAtFrontOfQueue(buttonStateChangeMessage);
						} else {
							Toast.makeText(getActivity(), R.string.activation_channel_message, Toast.LENGTH_SHORT).show();
						}
					}
					
					// ���ر�ѡ�м���
					int position = (Integer) v.getTag();
					checkedViewMap.put(position + 1, viewHolders.get(position));
					addChannelSpinnerListValues(tag);
					/*if(!channel_spinnerList.contains("Channel" + (tag + 1))){
						channel_spinnerList.add("Channel" + (tag + 1));
					}*/
					
				} else {
					if (DataCollection.isRecording) {
						Toast.makeText(getActivity().getApplicationContext(), R.string.Collecting_data, Toast.LENGTH_SHORT)// ���ڲɼ����
								.show();
						((CheckBox) v).setSelected(true);

						((CheckBox) v).setButtonDrawable(R.drawable.checked_active);
					} else {
						removeAllView();
						((CheckBox) v).setSelected(false);
						((CheckBox) v).setButtonDrawable(R.drawable.checked);
						checkChannelNum --;
						editor.putString("checkChannelNum", String.valueOf(checkChannelNum));
						editor.remove("Channel" + v.getTag());
						editor.remove("Physical" + v.getTag());
						editor.remove("Transducer" + v.getTag());
						editor.remove("Point" + v.getTag());
						editor.remove("Direction" + v.getTag());
						editor.remove("Coupling" + v.getTag());
						editor.remove("Sensitivity" + v.getTag());
						editor.remove("Unit" + v.getTag());
						editor.remove("Range" + v.getTag());
						editor.commit();
						// ͨ��ȡ������Ϣ
						XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
						equipment_Num = getActivity().getSharedPreferences("Equipment", 0).getInt("Equipment", 0);
						if (equipment_Num != 0) {
							Handler buttonStateChangeHandler = (Handler) xclSingalTransfer.getValue("buttonStateChange");
							if (buttonStateChangeHandler == null) {
								return;
							}
							Message buttonStateChangeMessage = buttonStateChangeHandler.obtainMessage();
							buttonStateChangeMessage.what = 0;
							buttonStateChangeMessage.arg1 = (Integer) v.getTag();
							buttonStateChangeHandler.sendMessageAtFrontOfQueue(buttonStateChangeMessage);

						}
						// ����ѡ�м����е���
						int position = (Integer) v.getTag();
						checkedViewMap.remove(position + 1);
						removeChannelSpinnerListValues(position);
						/*if(channel_spinnerList.size()>0){
							for(int i=0;i<channel_spinnerList.size();i++){
								if(channel_spinnerList.get(i).contains(String.valueOf(position+1))){
									channel_spinnerList.remove(i);
								}
							}
						} */
					}

				}
			}
			
			break;
		case R.id.CheckAll:
			if (checkedViewMap == null) {
				checkedViewMap = new HashMap<Integer, ViewHolder>();
			}
			SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
			SharedPreferences.Editor editor = preference.edit();
			XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
			if (checkAll.isSelected()) {
				if (DataCollection.isRecording) {
					Toast.makeText(getActivity().getApplicationContext(), R.string.Collecting_data, Toast.LENGTH_SHORT)// ����������
							.show();
					return;

				} else {
					checkAll.setText(R.string.CheckAll);
					setAllCheck(false);
					for (ViewHolder holder : viewHolders) {
						checkChannelNum = 0;
						editor.putString("checkChannelNum", String.valueOf(checkChannelNum));
						editor.remove("Channel" + holder.view.getTag());
						editor.remove("Physical" + holder.view.getTag());
						editor.remove("Transducer" + holder.view.getTag());
						editor.remove("Point" + holder.view.getTag());
						editor.remove("Direction" + holder.view.getTag());
						editor.remove("Coupling" + holder.view.getTag());
						editor.remove("Sensitivity" + holder.view.getTag());
						editor.remove("Unit" + holder.view.getTag());
						editor.remove("Range" + holder.view.getTag());
						editor.commit();
						equipment_Num = getActivity().getSharedPreferences("Equipment", 0).getInt("Equipment", 0);
						if (equipment_Num != 0) {
							Handler buttonStateChangeHandler = (Handler) xclSingalTransfer.getValue("buttonStateChange");

							Message buttonStateChangeMessage = buttonStateChangeHandler.obtainMessage();
							buttonStateChangeMessage.what = 0;
							buttonStateChangeMessage.arg1 = (Integer) holder.view.getTag();
							buttonStateChangeHandler.sendMessageAtFrontOfQueue(buttonStateChangeMessage);
						}

						int index = (Integer) holder.view.getTag();
						if(checkedViewMap.size()>0){
							checkedViewMap.remove(index + 1);
						}
						removeChannelSpinnerListValues(index);
						/*if(channel_spinnerList.size()>0){
							for(int i=0;i<channel_spinnerList.size();i++){
								if(channel_spinnerList.get(i).contains(String.valueOf(index+1))){
									channel_spinnerList.remove(i);
								}
							}
						}*/
					}
				}
			} else {
				setAllCheck(true);
				checkAll.setText(R.string.Cancel);
				for (ViewHolder holder : viewHolders) {
					int channelNum = (Integer) holder.view.getTag();

					checkChannelNum = viewHolders.size();

					editor.putString("checkChannelNum", String.valueOf(checkChannelNum));
					editor.putString("Channel" + channelNum, "" + channelNum);
					editor.putString("Physical" + holder.view.getTag(), holder.Physical.getSelectedItem().toString());
					editor.putString("Transducer" + holder.view.getTag(), holder.Transducer.getText().toString());
					editor.putString("Point" + holder.view.getTag(), holder.Point.getText().toString());
					editor.putString("Direction" + holder.view.getTag(), holder.Direction.getSelectedItem().toString());
					editor.putString("Coupling" + holder.view.getTag(), holder.Coupling.getSelectedItem().toString());
					editor.putString("Sensitivity" + holder.view.getTag(), holder.Sensitivity.getText().toString());
					editor.putString("Unit" + holder.view.getTag(), holder.Unit.getSelectedItem().toString());
					editor.putString("Range" + holder.view.getTag(), holder.Range.getSelectedItem().toString());
					editor.commit();

					equipment_Num = getActivity().getSharedPreferences("Equipment", 0).getInt("Equipment", 0);
					if (equipment_Num != 0) {
						Handler buttonStateChangeHandler = (Handler) xclSingalTransfer.getValue("buttonStateChange");
						if (buttonStateChangeHandler != null) {
							Message buttonStateChangeMessage = buttonStateChangeHandler.obtainMessage();
							buttonStateChangeMessage.what = 1;
							buttonStateChangeMessage.arg1 = (Integer) holder.view.getTag();
							buttonStateChangeHandler.sendMessageAtFrontOfQueue(buttonStateChangeMessage);

						}
					}
					int index = (Integer) holder.view.getTag();
					checkedViewMap.put(index + 1, holder);
					/*123
					if(!channel_spinnerList.contains("Channel" + (index + 1))){
						channel_spinnerList.add("Channel" + (index + 1));
					}*/
					addChannelSpinnerListValues(index);
				}
			}
			checkAll.setSelected(checkAll.isSelected() ? false : true);
			returnButton();
			break;
		case R.id.Physical:
		case R.id.Direction:
		case R.id.Coupling:
		case R.id.unit:
		case R.id.Range:
			if (copy.isSelected())
				return;
			v.performClick();
			break;
		}
		/*
		 * ����ͨ����Ϊ0ʱ��ͨ��SpinnerĬ����ʾ����ͨ�������еĵ�һ��
		 */
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("Waterfall")){
				if(channel_spinnerList.size()>0){
					((MainContextView)ct.mainContextViewList.get(i))
						.setChannelSpinnerPosition(channel_spinnerList.get(0));
				}else{
					((MainContextView)ct.mainContextViewList.get(i))
					.setChannelSpinnerPosition("");
				}
			}
		}
	}
	private void addChannelSpinnerListValues(int index){
		if(!channel_spinnerList.contains("Channel" + (index + 1))){
			channel_spinnerList.add("Channel" + (index + 1));
		}
	}
	private void removeChannelSpinnerListValues(int inedx){
		if(channel_spinnerList.size()>0){
			for(int i=0;i<channel_spinnerList.size();i++){
				if(channel_spinnerList.get(i).contains(String.valueOf(inedx+1))){
					channel_spinnerList.remove(i);
				}
			}
		} 
	}
	private void resetChannel() {
		SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
		for (int i = 0; i < viewHolders.size(); i++) {
			if (preference.getString("Channel" + i, "-1").equals(String.valueOf(i))) {
				viewHolders.get(i).check.setChecked(true);
				viewHolders.get(i).check.setButtonDrawable(R.drawable.checked_active);
			} else {
				viewHolders.get(i).check.setChecked(false);
				viewHolders.get(i).check.setButtonDrawable(R.drawable.checked);
			}
		}
	}

	public ArrayList<ViewHolder> getViewHolders() {
		return viewHolders;
	}

	public void getXcalSingalTransfer() {
		XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
		HashMap<String, Object> xcalSingalTransferMap = xclSingalTransfer.getMap();
		if (xclSingalTransfer.getSize() > 0) {
			Iterator<String> it = xcalSingalTransferMap.keySet().iterator();
			while (it.hasNext()) {
				String index = it.next();
				Object view = xclSingalTransfer.getValue(index);
				if (!view.getClass().getSimpleName().equals("ViewHolder")) {
					return;
				}
				ViewHolder viewholder = (ViewHolder) view;
				if(Integer.valueOf(index)<viewHolders.size()){
					viewHolders.get(Integer.valueOf(index)).Sensitivity.setText(viewholder.Sensitivity.getText());
				}
			}
		}
	}

	public void setChecked(View v) {
		Noise.setChecked(false);
		Vibration.setChecked(false);
		AllChannel.setChecked(false);
		((CheckBox) v).setChecked(true);
	}

	public void HintTypeChannel(int position) {
		for (ViewHolder holder : viewHolders) {
			if (holder.Physical.getSelectedItemPosition() == position || position == -1) {
				holder.view.setVisibility(View.VISIBLE);
			} else {
				holder.view.setVisibility(View.GONE);
			}
		}
		if (HideChannel.isChecked())
			HintUnactivatedChannels(HideChannel.isChecked());
	}

	public void HintUnactivatedChannels(Boolean hint) {
		if (hint) {
			for (ViewHolder holder : viewHolders) {
				if (!holder.check.isChecked()) {
					holder.view.setVisibility(View.GONE);
				}
			}
		} else {
			for (ViewHolder holder : viewHolders) {
				holder.view.setVisibility(View.VISIBLE);
			}
			if (Noise.isChecked()) {
				HintTypeChannel(0);
			} else if (Vibration.isChecked()) {
				HintTypeChannel(1);

			} else if (AllChannel.isChecked()) {
				HintTypeChannel(-1);

			}
		}
	}

	public void Item2Copy(int tag) {
		cp.Physical = viewHolders.get(tag).Physical.getSelectedItemPosition();
		cp.Direction = viewHolders.get(tag).Direction.getSelectedItemPosition();
		cp.Coupling = viewHolders.get(tag).Coupling.getSelectedItemPosition();
		cp.Unit = viewHolders.get(tag).Unit.getSelectedItemPosition();
		cp.Range = viewHolders.get(tag).Range.getSelectedItemPosition();
		cp.Transducer = viewHolders.get(tag).Transducer.getText().toString();
		cp.Point = viewHolders.get(tag).Point.getText().toString();
		cp.Sensitivity = viewHolders.get(tag).Sensitivity.getText().toString();
	}

	public void Copy2Item(int tag) {
		viewHolders.get(tag).Physical.setSelection(cp.Physical);
		viewHolders.get(tag).Direction.setSelection(cp.Direction);
		viewHolders.get(tag).Coupling.setSelection(cp.Coupling);
		viewHolders.get(tag).Unit.setSelection(cp.Unit);
		viewHolders.get(tag).Range.setSelection(cp.Range);
		viewHolders.get(tag).Transducer.setText(cp.Transducer);
		viewHolders.get(tag).Point.setText(cp.Point);
		viewHolders.get(tag).Sensitivity.setText(cp.Sensitivity);
		viewHolders.get(tag).check.setChecked(false);
	}

	public void setAllEnable(Boolean enable) {
		if (enable) {
			for (ViewHolder holder : viewHolders) {
				holder.Physical.setAlpha(1);
				holder.Transducer.setAlpha(1);
				holder.Transducer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
				holder.Point.setAlpha(1);
				holder.Point.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
				holder.Direction.setAlpha(1);
				holder.Coupling.setAlpha(1);
				holder.Sensitivity.setAlpha(1);
				holder.Sensitivity.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
				holder.Unit.setAlpha(1);
				holder.Range.setAlpha(1);
			}
		} else {
			for (ViewHolder holder : viewHolders) {
				holder.Physical.setAlpha(0.3f);
				holder.Transducer.setAlpha(0.3f);
				holder.Transducer.setInputType(InputType.TYPE_NULL);
				holder.Point.setAlpha(0.3f);
				holder.Point.setInputType(InputType.TYPE_NULL);
				holder.Direction.setAlpha(0.3f);
				holder.Coupling.setAlpha(0.3f);
				holder.Sensitivity.setAlpha(0.3f);
				holder.Sensitivity.setInputType(InputType.TYPE_NULL);
				holder.Unit.setAlpha(0.3f);
				holder.Range.setAlpha(0.3f);
			}
		}
	}

	public void setItemEnable(int tag) {
		viewHolders.get(tag).Physical.setAlpha(1);
		viewHolders.get(tag).Transducer.setAlpha(1);
		viewHolders.get(tag).Transducer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
		viewHolders.get(tag).Point.setAlpha(1);
		viewHolders.get(tag).Point.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
		viewHolders.get(tag).Direction.setAlpha(1);
		viewHolders.get(tag).Coupling.setAlpha(1);
		viewHolders.get(tag).Sensitivity.setAlpha(1);
		viewHolders.get(tag).Sensitivity.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
		viewHolders.get(tag).Unit.setAlpha(1);
		viewHolders.get(tag).Range.setAlpha(1);
	}

	public void setAllCheck(Boolean allCheck) {
		if (allCheck) {
			for (ViewHolder holder : viewHolders) {
				holder.check.setChecked(true);
				holder.check.setButtonDrawable(R.drawable.checked_active);

			}
		} else {
			for (ViewHolder holder : viewHolders) {
				holder.check.setChecked(false);
				holder.check.setButtonDrawable(R.drawable.checked);
			}
		}
	}

	public class Copy {
		public String check;
		public int Physical;
		public String Transducer;
		public String Point;
		public int Direction;
		public int Coupling;
		public String Sensitivity;
		public int Unit;
		public int Range;
	}

	public class ViewHolder implements Serializable {
		public View view;
		public CheckBox check;
		public CustomSpinner Physical;
		public EditText Transducer;
		public EditText Point;
		public CustomSpinner Direction;
		public CustomSpinner Coupling;
		public EditText Sensitivity;
		public CustomSpinner Unit;
		public CustomSpinner Range;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		//if(position==0)return;
		SharedPreferences.Editor editor = null;
		if (viewHolders.get((Integer) parent.getTag()).check.isChecked()) {

			SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
			editor = preference.edit();

		}
		switch (parent.getId()) {
		case R.id.Physical:
//			TextView tv = (TextView) view;
//			tv.setTextSize(12);
//			tv.setGravity(Gravity.LEFT);
			if (viewHolders.get((Integer) parent.getTag()).check.isChecked()) {
				editor.putString("Physical" + parent.getTag(), ((CustomSpinner) parent).getSelectedItem().toString());
			}
			
			break;
		case R.id.Direction:
//			TextView tv1 = (TextView) view;
//			tv1.setTextSize(12);
//			tv1.setGravity(Gravity.LEFT);
			if (viewHolders.get((Integer) parent.getTag()).check.isChecked()) {
				editor.putString("Direction" + parent.getTag(), ((CustomSpinner) parent).getSelectedItem().toString());
			}
			break;
		case R.id.Coupling:
//			TextView tv2 = (TextView) view;
//			tv2.setTextSize(12);
//			tv2.setGravity(Gravity.LEFT);
			if (viewHolders.get((Integer) parent.getTag()).check.isChecked()) {
				editor.putString("Coupling" + parent.getTag(), ((CustomSpinner) parent).getSelectedItem().toString());
			}
			break;
		case R.id.unit:
//			TextView tv3 = (TextView) view;
//			tv3.setTextSize(12);
//			tv3.setGravity(Gravity.LEFT);
			if (viewHolders.get((Integer) parent.getTag()).check.isChecked()) {
				editor.putString("Unit" + parent.getTag(), ((CustomSpinner) parent).getSelectedItem().toString());
			}
			break;
		case R.id.Range:
//			TextView tv4 = (TextView) view;
//			tv4.setTextSize(12);
//			tv4.setGravity(Gravity.LEFT);
			if (viewHolders.get((Integer) parent.getTag()).check.isChecked()) {
				editor.putString("Range" + parent.getTag(), ((CustomSpinner) parent).getSelectedItem().toString());
				//editor.putString("previousRange" + parent.getTag(), ((CustomSpinner) parent).getSelectedItem().toString());
				editor.commit();
				//twoRangeLevelListValues();
			}
			
			break;
		}
		if (viewHolders.get((Integer) parent.getTag()).check.isChecked()) {
			editor.commit();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
	/*private void twoRangeLevelListValues(){
		SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
		List<Integer> channelList=((MainActivity)getActivity()).bottomOperate.addChannelViewPager.isActivated_ChanNum;
		List<Integer> levelList_2 =new ArrayList<Integer>();
		for(int i=0;i<channelList.size();i++){
			String level = preference.getString("Range"+ channelList.get(i).toString(), null);
			if("10V".equals(level)){
				levelList_2.add(0);
			} else if("1V".equals(level)){
				levelList_2.add(1);
			} else if("100mV".equals(level)){
				levelList_2.add(2);
			}
		}
		channelRangeLevelList.add(levelList_2);
		if(channelRangeLevelList.size()>2){
			channelRangeLevelList.remove(0);
		}
	}*/
	private void setViewHolderVisible(ViewHolder viewHolder, int viewHolderID, int ifVisible) {
		switch (viewHolderID) {
		case 0:
			viewHolder.Physical.setVisibility(ifVisible);
			view.findViewById(R.id.Physical).setVisibility(ifVisible);
			break;
		case 1:
			viewHolder.Transducer.setVisibility(ifVisible);
			view.findViewById(R.id.Transducer).setVisibility(ifVisible);
			break;
		case 2:
			viewHolder.Point.setVisibility(ifVisible);
			view.findViewById(R.id.Point).setVisibility(ifVisible);
			break;
		case 3:
			viewHolder.Direction.setVisibility(ifVisible);
			view.findViewById(R.id.Direction).setVisibility(ifVisible);
			break;
		case 4:
			viewHolder.Coupling.setVisibility(ifVisible);
			view.findViewById(R.id.Coupling).setVisibility(ifVisible);
			break;
		case 5:
			viewHolder.Sensitivity.setVisibility(ifVisible);
			view.findViewById(R.id.Sensitivity).setVisibility(ifVisible);
			viewHolder.Unit.setVisibility(ifVisible);
			view.findViewById(R.id.Sensitivity_unit).setVisibility(ifVisible);
			break;
		case 6:
			viewHolder.Range.setVisibility(ifVisible);
			view.findViewById(R.id.Range).setVisibility(ifVisible);
			break;
		}
	}

	public void setViewHolderVisbleHolder() {
		XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
		if (xclSingalTransfer.containsKey("analogVisibleMap")) {
			Map<Integer, Integer> viewHolderMap = (Map<Integer, Integer>) xclSingalTransfer.getValue("analogVisibleMap");
			Iterator<Integer> it = viewHolderMap.keySet().iterator();
			while (it.hasNext()) {
				int viewHolderID = (int) it.next();
				Iterator<ViewHolder> viewIt = viewHolders.iterator();
				while (viewIt.hasNext()) {
					setViewHolderVisible((ViewHolder) viewIt.next(), viewHolderID, (int) viewHolderMap.get(viewHolderID));
				}
			}
		}
	}

	private void removeAllView() {
		CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
		ArrayList<Fragment> pageList = ct.getFragmentList();
		if(pageList.size()==0)return;
		OneTabFragment oneTabFragment = (OneTabFragment) pageList.get(ct.getPageNo());
		if (oneTabFragment.getMapList() != null) {
			List<String> mapList = oneTabFragment.getMapList();
			int i = 0;
			for (String name : mapList) {
				switch (i) {
				case 0:
				case 1:
				case 2:
				case 3:
					for (int j = 0; j < ct.algorithm_spinnerList.size(); j++) {
						((MainContextView) ct.mainContextViewList.get(j)).drawMap();
						((RelativeLayout) ct.LegendList.get(j)).removeAllViews();
					}
					break;
				}
				i++;
			}
		}
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
	public void returnButton(){
		/*
		 * �������ʱ������ơ�ճ��ȫѡ��ѡ����ȡ���ơ�ճ��ȫѡ��ѡ��״̬��
		 */
		resetChannel();
		copy.setSelected(false);
		copy.setText(R.string.Copy);
		paste.setSelected(false);
		paste.setText(R.string.Paste);
		//checkAll.setSelected(false);
		//checkAll.setText(R.string.CheckAll);
		setAllEnable(true);
	}
	private void changeChannelRange() {
		Handler rangehandler = new Handler() {
			public void handleMessage(Message age) {
				SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
				SharedPreferences.Editor editor = preference.edit();
				ViewHolder v = viewHolders.get(age.arg1);
				v.Range.setSelection(age.arg2);
				//����Ԥ������������ͬ����ʾ
				editor.putString("Range" + age.arg1, v.Range.getSelectedItem().toString());
				editor.commit();
				//twoRangeLevelListValues();
			}
		};
		XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
		xclSingalTransfer.putValue("rangehandler", rangehandler);
	}

	public Map<Integer, ViewHolder> getCheckedViewMap() {
		return checkedViewMap;
	}
	public void languageChanged(){
		((CheckBox) view.findViewById(R.id.Noise)).setText(R.string.Noise);
		((CheckBox) view.findViewById(R.id.Vibration))
				.setText(R.string.Vibration);
		((CheckBox) view.findViewById(R.id.AllChannel))
				.setText(R.string.All_channels);
		((CheckBox) view.findViewById(R.id.HideChannel))
				.setText(R.string.Hidden_channels);
		((TextView) view.findViewById(R.id.Chan)).setText(R.string.Chan);
		((TextView) view.findViewById(R.id.Physical))
				.setText(R.string.Physical);
		((TextView) view.findViewById(R.id.Transducer))
				.setText(R.string.Transducer);
		((TextView) view.findViewById(R.id.Point)).setText(R.string.Point);
		((TextView) view.findViewById(R.id.Direction))
				.setText(R.string.Direction);
		((TextView) view.findViewById(R.id.Coupling))
				.setText(R.string.Coupling);
		((TextView) view.findViewById(R.id.Sensitivity))
				.setText(R.string.Sensitivity);
		((TextView) view.findViewById(R.id.Sensitivity_unit))
				.setText(R.string.Sensitivity_unit);
		((TextView) view.findViewById(R.id.Range)).setText(R.string.Range);
		((Button) view.findViewById(R.id.Copy)).setText(R.string.Copy);
		((Button) view.findViewById(R.id.Paste)).setText(R.string.Paste);
		((Button) view.findViewById(R.id.CheckAll))
				.setText(R.string.CheckAll);
	}
	public void skinChanged(TypedArray typedArray){
		 checkAll.setBackground(typedArray.getDrawable(R.styleable.myStyle_check_all));
		 copy.setBackground(typedArray.getDrawable(R.styleable.myStyle_copy));
		 paste.setBackground(typedArray.getDrawable(R.styleable.myStyle_paste));
		 checkAll.setTextColor(typedArray.getColor(R.styleable.myStyle_textColor,Color.YELLOW));
		 Chan.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Physical.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Transducer.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Point.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Direction.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Coupling.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Sensitivity.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Sensitivity_unit.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Range.setTextColor(typedArray.getColor(R.styleable.myStyle_anologtext,Color.YELLOW));
		 Type.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_anologtype,Color.YELLOW));
	}
}
