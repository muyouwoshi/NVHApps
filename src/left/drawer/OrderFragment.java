package left.drawer;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.MainContextView;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.SpinnerValues;
import com.example.mobileacquisition.ThemeLogic;

import common.CustomTab;
import common.DataCollection;
import common.XclSingalTransfer;

/** ��������������ã�Order��Order���ܿ�����12468��ѡ��string*/
@SuppressLint("InflateParams")
public class OrderFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
	private View view = null;
	private Switch orderSwitch = null;
	private SpinnerValues spinnerValues;
	private SharedPreferences.Editor editor;
	private String switchStr;
	private ArrayList<Double> order_num_list = new ArrayList<Double>();
	public ArrayList<CheckBox> order_checkbox_list = new ArrayList<CheckBox>();
	private ArrayList<String> order_values_str_list = new ArrayList<String>();
	private ArrayList<String> order_names_str_list = new ArrayList<String>();
	private ScrollView scrollView;
	private LinearLayout order_layout;
	private EditText order_edit;
	private Button addOrder;
	private ArrayList<Boolean> order_values_list = new ArrayList<Boolean>();
	private String order_names, order_values;
	private Spinner xAxis_Spinner,yAxis_Spinner;
	private String xAxisStr;
	private String yAxisStr;
	public LinearLayout layout;
	public CheckBox one_order,two_order;
	public OrderFragment() {

	}

	public void setSpinnerValues(SpinnerValues spinnerValues) {
		this.spinnerValues = spinnerValues;
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.order, container, false);
		view.setOnClickListener(this);
		xAxis_Spinner = (Spinner) view.findViewById(R.id.OrderXAxis_spinner);
		yAxis_Spinner = (Spinner) view.findViewById(R.id.OrderYAxis_spinner);
		xAxis_Spinner.setOnItemSelectedListener(this);
		yAxis_Spinner.setOnItemSelectedListener(this);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		LayoutParams lp = scrollView.getLayoutParams();
		lp.height = ((MainActivity)getActivity()).screen_height - dip2px(210);
		scrollView.setLayoutParams(lp);
		order_layout = (LinearLayout) view.findViewById(R.id.order_layout);
		order_edit = (EditText) view.findViewById(R.id.order_edit);
		addOrder = (Button) view.findViewById(R.id.addOrder);
		addOrder.setOnClickListener(this);
		
		order_num_list.add((double) 1);
		order_values_list.add(false);
		order_num_list.add((double) 2);
		order_values_list.add(false);
		order_num_list.add((double) 4);
		order_values_list.add(false);
		order_num_list.add((double) 6);
		order_values_list.add(false);
		order_num_list.add((double) 8);
		order_values_list.add(false);
		for(int i=0;i<order_names_str_list.size();i++){
			if(!order_num_list.contains(Double.parseDouble(order_names_str_list.get(i)))){
				order_num_list.add(Double.parseDouble(order_names_str_list.get(i)));
				order_values_list.add(order_values_str_list.get(i).equals("true"));
			}
		}
		
		Collections.sort(order_num_list);
		
		int size = 0;
		if (order_num_list.size() % 2 == 0) {
			size = order_num_list.size() / 2;
		} else {
			size = order_num_list.size() / 2 + 1;
		}
		for (int i = 0; i < size; i++) {
			order_layout.addView(addOrderView());
		}
		order_names = ToArrayList(order_num_list);
		order_values = ToArrayList(order_values_list);
		languageChanged();
		view.findViewById(R.id.Back_Order).setOnClickListener(this);
		orderSwitch = (Switch) view.findViewById(R.id.OrderSwitchButton);
		orderSwitch.setThumbResource(R.drawable.switchtitle);
		orderSwitch.setSwitchTextAppearance(view.getContext(),R.color.black);
		init();

		Switch();
		for(int f=0;f<order_checkbox_list.size();f++){
			if(ThemeLogic.themeType==1){
				order_checkbox_list.get(f).setTextColor( Color.rgb(255, 255, 255));
			}else{
				order_checkbox_list.get(f).setTextColor( Color.rgb(0, 0, 0));
				
			}
		}
		return view;
	}

	/**  */
	private View addOrderView() {
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.channel_left_order_one, null);
		
		 one_order = (CheckBox) layout.findViewById(R.id.one_order);
		 two_order = (CheckBox) layout.findViewById(R.id.two_order);
//		if(ThemeLogic.themeType==1){
//			one_order.setTextColor(getResources().getColor(R.color.white4));
//			two_order.setTextColor(getResources().getColor(R.color.white4));
//		}else{
//			
//			one_order.setTextColor(getResources().getColor(R.color.black));
//			two_order.setTextColor(getResources().getColor(R.color.black));
//		}
		order_checkbox_list.add(one_order);
		order_checkbox_list.add(two_order);
		if (order_checkbox_list.size() > order_num_list.size()) {
			order_checkbox_list.get(order_checkbox_list.size() - 1).setVisibility(View.GONE);
		}
		for (int i = 0; i < order_checkbox_list.size(); i++) {
			order_checkbox_list.get(i).setOnClickListener(this);
			order_checkbox_list.get(i).setTag(i);
			if(ThemeLogic.themeType==1){
				order_checkbox_list.get(i).setTextColor( Color.rgb(255, 255, 255));
			}else{
				order_checkbox_list.get(i).setTextColor( Color.rgb(0, 0, 0));
				
			}
		}
		
		return layout;
	}

	public void init() {
		setSlectedItem(xAxis_Spinner,xAxisStr);
		setSlectedItem(yAxis_Spinner,yAxisStr);
		setSwitch();
	}

	public void reset(FragmentActivity fragmentActivity) {
		readFromXml(fragmentActivity);
		init();
	}

	@Override
	public void onClick(View v) {
		MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("main");
		OrderFragment orderFragment = (OrderFragment) getFragmentManager().findFragmentByTag("order");
		switch (v.getId()) {
		case R.id.one_order:
		case R.id.two_order:
			/********************************* Order�޸� *****************************/
			if (((CheckBox) v).isChecked()) {
				((CheckBox) v).setButtonDrawable(R.drawable.checked_active);
				((CheckBox) v).setChecked(true);
				// editor.putString("order_num_list"+v.getTag(), "true");
				// editor.commit();
			} else {
				((CheckBox) v).setButtonDrawable(R.drawable.checked);
				((CheckBox) v).setChecked(false);
				// editor.putString("order_num_list"+v.getTag(), "false");
				// editor.commit();
			}
			int tag = (Integer) v.getTag(); ////////// 16.3.11 ��ά�ѡ���order_values_list.remove��������Integer��Ϊint��
			order_values_list.remove(tag);  ////////// �ų� order_values_list������ɾ��ԭֵ�����bug
			order_values_list.add((Integer) v.getTag(), ((CheckBox) v).isChecked());
			order_values = ToArrayList(order_values_list);
//			Log.e("ard", "order_values 1 ��" + order_values);
//			Log.e("ard", "order_names 1 ��" + order_names);
			editor.putString("order_values", order_values);
			editor.putString("order_names", order_names);
			break;
		case R.id.addOrder:

			if (order_edit.getText().toString().equals("")) {
				Toast.makeText(getActivity().getApplicationContext(), R.string.order_not_null, Toast.LENGTH_SHORT).show();// ����������
				return;
			}else if (Double.parseDouble(order_edit.getText().toString())==0.0) {
				Toast.makeText(getActivity().getApplicationContext(), R.string.order_not_zero, Toast.LENGTH_SHORT).show();// ����������
				return;
			}
			int point = 0;
			for (int i = 0; i < order_edit.getText().toString().length(); i++) {
				// if(order_edit.getText().toString().indexOf(".")!=-1){
				if (order_edit.getText().toString().charAt(i) == '.')
					point = point + 1;
				// }
			}
			if (point > 1) {
				Toast.makeText(getActivity().getApplicationContext(), R.string.order_wrong, Toast.LENGTH_SHORT).show();// ����������
				return;
			}
			if (order_num_list.contains(Double.parseDouble(order_edit.getText().toString()))) {
				Toast.makeText(getActivity().getApplicationContext(), R.string.order_exists, Toast.LENGTH_SHORT).show();// ����������
				return;
			}
			if (order_num_list.size() % 2 == 0) {
				order_layout.addView(addOrderView());
			} else {
				order_checkbox_list.get(order_checkbox_list.size() - 1).setVisibility(View.VISIBLE);
				
			}
			order_num_list.add(Double.parseDouble(order_edit.getText().toString()));
			Collections.sort(order_num_list);
			int index = order_num_list.indexOf(Double.parseDouble(order_edit.getText().toString()));
//			Log.e("ard", "index��" + index);
			order_values_list.add(index, false);
			languageChanged();
			order_names = ToArrayList(order_num_list);
			Log.e("ard", "addname��" + order_names);
			order_values = ToArrayList(order_values_list);
			Log.e("ard", "addorder_values��" + order_values);
			for (int i = 0; i < order_values_list.size(); i++) {
				
				order_checkbox_list.get(i).setChecked(order_values_list.get(i));
				if (order_values_list.get(i)) {
					order_checkbox_list.get(i).setButtonDrawable(R.drawable.checked_active);
				} else {
					order_checkbox_list.get(i).setButtonDrawable(R.drawable.checked);
				}
			}
			break;
		case R.id.Back_Order:
			getFragmentManager().beginTransaction().hide(orderFragment).show(mainFragment).commit();
			break;
		}
		editor.commit();
	}

	public void Switch() {

		orderSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
				if (ct.algorithm_spinnerList.size() != 0) {
					for (int j = 0; j < ct.algorithm_spinnerList.size(); j++) {
						if (ct.algorithm_spinnerList.get(j).getText().equals("Order") && DataCollection.isRecording) {
							orderSwitch.setChecked(true);
							Toast.makeText(getActivity().getApplicationContext(), R.string.Collecting_data, 0).show();// ����������
									
							return;
						}
					}
				}
				if (isChecked) {
					orderSwitchOpen();
				} else {
					orderSwitchOff();
				}

			}
		});
	}
	private void setSlectedItem(Spinner  spinner, String xAxisStr) {
		SpinnerAdapter adapter = spinner.getAdapter();
		int count = adapter.getCount();
		for(int i = 0; i<count;i++ ){
			if(adapter.getItem(i).toString().equals(xAxisStr)){
				spinner.setSelection(i);
				break;
			}
		}
	}
	@SuppressLint("NewApi")
	private void orderSwitchOpen() {
		spinnerValues.addValues("Order");
		orderSwitch.setTrackResource(R.drawable.switchbutton_on);
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.putString("Order", "open");
		editor.putString("Order_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("Order_YAxis", yAxis_Spinner.getSelectedItem().toString());
		editor.commit();
	}

	@SuppressLint("NewApi")
	private void orderSwitchOff() {
		if (null == spinnerValues)
			return;
		orderSwitch.setTrackResource(R.drawable.switchbutton_off);
		spinnerValues.removeValues("Order");
		spinnerValues.getAdapter().notifyDataSetChanged();
		editor.putString("Order", "close");
		editor.putString("Order_XAxis", xAxis_Spinner.getSelectedItem().toString());
		editor.putString("Order_YAxis", yAxis_Spinner.getSelectedItem().toString());
		editor.commit();
		removeAllView();

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		switch(parent.getId()) {
		case R.id.OrderXAxis_spinner:
			 TextView tv2 = (TextView)view;
			 if(tv2==null){
				 return;
			 }
				if(ThemeLogic.themeType==1){
					 tv2.setTextColor(getResources().getColor(R.color.white4));
				}else{
					
					tv2.setTextColor(getResources().getColor(R.color.black));
				}
             tv2.setGravity(Gravity.CENTER);
           
		     editor.putString("Order_XAxis", xAxis_Spinner.getSelectedItem().toString());
            
			spinnerChangeState(0,xAxis_Spinner.getSelectedItem().toString());
			break;
		case R.id.OrderYAxis_spinner:
			 TextView tv3 = (TextView)view;
			 if(tv3==null){
				 return;
			 }
				if(ThemeLogic.themeType==1){
					 tv3.setTextColor(getResources().getColor(R.color.white4));
				}else{
					
					tv3.setTextColor(getResources().getColor(R.color.black));
				}
             tv3.setGravity(Gravity.CENTER);
			     editor.putString("Order_YAxis", yAxis_Spinner.getSelectedItem().toString());
			
			spinnerChangeState(1,yAxis_Spinner.getSelectedItem().toString());
			break;
		}
		editor.commit();
		

		switch (parent.getId()) {
		}
	}
	private void removeAllView(){
		CustomTab ct = ((MainActivity) getActivity()).mainCustomTab;
		//ArrayList<Fragment> pageList = ct.getFragmentList();
		for(int j=0;j<ct.algorithm_spinnerList.size();j++){
			if(ct.algorithm_spinnerList.get(j).getText().equals("Order")){
				ct.algorithm_spinnerList.get(j).setText(spinnerValues.getAlgorithmItems().get(0));
				((MainContextView)ct.mainContextViewList.get(j)).drawMap();
			}
		}
	}
	private void spinnerChangeState(int axisType,String unitType){
		CustomTab ct=((MainActivity)getActivity()).mainCustomTab;
		for(int i=0;i<ct.algorithm_spinnerList.size();i++){
			if(ct.algorithm_spinnerList.get(i).getText().equals("Order")){
				((MainContextView)ct.mainContextViewList.get(i)).setUnitChangeState(axisType,unitType);
			}
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	public void readFromXml(FragmentActivity fragmentActivity) {

		SharedPreferences preference = fragmentActivity.getSharedPreferences("hz_5D", 0);
		editor = preference.edit();

		switchStr = preference.getString("Order", "close");
		xAxisStr = preference.getString("Order_XAxis", "RPM");
		yAxisStr = preference.getString("Order_YAxis", "RPM");
		String order_names =preference.getString("order_names", "");
		String order_names_str[] = order_names.split(" ");  
		String order_values =preference.getString("order_values", "");
		String order_values_str[] = order_values.split(" "); 
		order_values_str_list.clear();
		order_names_str_list.clear();
		for(int i=0;i<order_values_str.length;i++){
			if(!order_values_str[i].equals("")){
				order_values_str_list.add(order_values_str[i]);
			}
		}
		for(int i=0;i<order_names_str.length;i++){
			if(!order_names_str[i].equals("")){
				order_names_str_list.add(order_names_str[i]);
			}
		}
	}

	private void setSwitch() {
		if (null == switchStr ? false : switchStr.equals("open")) {
			orderSwitch.setChecked(true);
			orderSwitchOpen();
			if(order_values_str_list.size()!=0){
				order_values_list.clear();
			}
			for (int i = 0; i < order_values_str_list.size(); i++) {
				order_checkbox_list.get(i).setChecked(order_values_str_list.get(i).equals("true"));
				order_values_list.add(order_values_str_list.get(i).equals("true"));
				/*if(order_values_str_list.get(i).equals("true")){
					order_checkbox_list.get(i).setButtonDrawable(R.drawable.checked_active);
				} else {
					order_checkbox_list.get(i).setButtonDrawable(R.drawable.checked);
				}*/
				order_names = ToArrayList(order_num_list);
				order_values = ToArrayList(order_values_str_list);
				editor.putString("order_values", order_values);
				editor.putString("order_names", order_names);
				editor.commit();
			}
		} else {
			orderSwitch.setChecked(false);
			orderSwitchOff();
			for (int i = 0; i < order_values_str_list.size(); i++) {
				order_checkbox_list.get(i).setChecked(false);
				order_names = ToArrayList(order_num_list);
				order_values = ToArrayList(order_values_str_list);
				editor.putString("order_values", order_values);
				editor.putString("order_names", order_names);
				editor.commit();
				
			}
		}
	}

	/********************************* Order�޸� *****************************/
	/** ��ListתΪ�ÿո��ֵ��ַ� */
	private String ToArrayList(ArrayList list) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			str += list.get(i) + " ";
		}
		return str;
	}

	public void languageChanged() {
		for (int i = 0; i < order_num_list.size(); i++) {
			order_checkbox_list.get(i).setText(order_num_list.get(i) + " " + getActivity().getResources().getString(R.string.order_unit));
		
		
		}
		addOrder.setText(R.string.add_order);
		((TextView)view.findViewById(R.id.X_Axis)).setText(R.string.X_Axis);
		((TextView)view.findViewById(R.id.Y_Axis)).setText(R.string.Y_Axis);
	}

	public int dip2px(float dpValue) {
		final float scale = getActivity().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	public void skinChanged(TypedArray typedArray){
		for(int f=0;f<order_checkbox_list.size();f++){
			if(ThemeLogic.themeType==1){
				order_checkbox_list.get(f).setTextColor( Color.rgb(255, 255, 255));
			}else{
				order_checkbox_list.get(f).setTextColor( Color.rgb(0, 0, 0));
				
			}
		}	
		view.findViewById(R.id.main_order_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_main_order,Color.YELLOW));
		view.findViewById(R.id.Order_Layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_main_order,Color.YELLOW));
		view.findViewById(R.id.Order_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		ImageButton Back_Order = (ImageButton) view.findViewById(R.id.Back_Order);
		Back_Order.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		TextView Order_title = (TextView) view.findViewById(R.id.Order_title);
		Order_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		TextView X_Axis_order = (TextView) view.findViewById(R.id.X_Axis);
		X_Axis_order.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//OrderXAxis_spinner
		TextView Y_Axis_order = (TextView) view.findViewById(R.id.Y_Axis);
		Y_Axis_order.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));//OrderYAxis_spinner
		
		((CheckBox)layout.findViewById(R.id.one_order)).setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		((CheckBox)layout.findViewById(R.id.two_order)).setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		
		
		
		
		Spinner OrderXAxis_spinner = (Spinner) view.findViewById(R.id.OrderXAxis_spinner);
		if((TextView)OrderXAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)OrderXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)OrderXAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
		
		Spinner OrderYAxis_spinner = (Spinner) view.findViewById(R.id.OrderYAxis_spinner);
		if((TextView)OrderYAxis_spinner.getSelectedView()!=null){
			if(ThemeLogic.themeType==1){
				((TextView)OrderYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white4));
		   }else{
			
			((TextView)OrderYAxis_spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
		   }
		}
	}
}
