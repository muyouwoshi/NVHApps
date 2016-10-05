package left.drawer;

import java.util.ArrayList;
import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

public class Display_Custom implements OnClickListener,OnItemSelectedListener{

	private RelativeLayout layout = null;
	private RelativeLayout child = null;
	//private  final String[] algorithm= {"Signal","SPL","OCT","FFT","AI","RPM","ColorMap"};
	private LinearLayout viewlayout_right;
	private DisplayFragment df = null;
	private ArrayList<TextView> textViewList = null;
	private ArrayList<String> strList = null;
	private Spinner map_num = null;
	private float scale;
	private AddAlgorithmItems addAlgorithmItems;
	public TextView map_num_text;
	public Button custom_layout_save;
	public double eqequipment_size;
	public Display_Custom(DisplayFragment df) {
		// TODO Auto-generated constructor stub
		this.df = df;
		DisplayMetrics metrics =df.getActivity().getResources().getDisplayMetrics();
        float xdpi = metrics.xdpi;
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        double z = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        eqequipment_size = Math.round(z /xdpi);
		layout = (RelativeLayout) df.getActivity().findViewById(R.id.maintab);
		child = (RelativeLayout) LayoutInflater.from(df.getActivity()).inflate(R.layout.custom_layout, layout, false);
		map_num = (Spinner) child.findViewById(R.id.map_num_spinner);
		map_num.setOnItemSelectedListener(this);
		scale = df.getActivity().getResources().getDisplayMetrics().density;
		textViewList = new ArrayList<TextView>();
		textViewList.add((TextView)child.findViewById(R.id.custom_textview_ul));
		textViewList.add((TextView)child.findViewById(R.id.custom_textview_ur));
		textViewList.add((TextView)child.findViewById(R.id.custom_textview_dl));
		textViewList.add((TextView)child.findViewById(R.id.custom_textview_dr));
		viewlayout_right =(LinearLayout)child.findViewById(R.id.viewlayout_right);
		map_num_text=(TextView)child.findViewById(R.id.map_num_text);
		custom_layout_save=(Button)child.findViewById(R.id.custom_layout_save);
		if(ThemeLogic.themeType==1){
			custom_layout_save.setBackgroundResource(R.drawable.bt_blue_selector);
		}else{
			custom_layout_save.setBackgroundResource(R.drawable.bt_blue_selector1);
			
		}
		custom_layout_save.setOnClickListener(this);
		LayoutParams params = new LayoutParams(-2,-2);
		params.height = dip2px(310);
		params.leftMargin = dip2px(310);
		params.addRule(RelativeLayout.CENTER_VERTICAL, -1);
		layout.addView(child, params);
		child.setVisibility(View.GONE);
		
	}
	public int dip2px(float dpValue) {
		return (int)(dpValue * scale +0.5f);
	}
	
	public void setVisibility(Boolean visible) {
		child.setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	public Boolean getVisibility() {
		return child.getVisibility() == 0 ? true : false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.custom_textview_ul:
		case R.id.custom_textview_ur:
		case R.id.custom_textview_dl:
		case R.id.custom_textview_dr:
			AlgorithmItemsPopWindow popWindow = new AlgorithmItemsPopWindow(df.getActivity(),v);
			if(addAlgorithmItems.getAlgorithmItems().size()!=0){
				popWindow.InitListView(addAlgorithmItems.getAlgorithmItems());
				popWindow.showPopupWindow();
			}
			
			break;
		case R.id.custom_layout_save:
			int position = map_num.getSelectedItemPosition();
			strList = new ArrayList<String>();
			for(int i = 0; i <= position; i ++) {
				strList.add(textViewList.get(i).getText().toString());
			}
			df.common.addView(strList);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(position==0){
			viewlayout_right.setVisibility(View.GONE);
		}else{
			viewlayout_right.setVisibility(View.VISIBLE);
		}
		for(int i = 0; i < textViewList.size(); i ++) {
			if(i <= position) {
				textViewList.get(i).setVisibility(View.VISIBLE);
			} else {
				textViewList.get(i).setVisibility(View.GONE);
			}
			if(addAlgorithmItems.getAlgorithmItems().size()!=0){
				textViewList.get(i).setText(addAlgorithmItems.getAlgorithmItems().get(0));
//				if(eqequipment_size >7) continue;
//				map_num.setEnabled(true);
			}else{
				textViewList.get(i).setText("ChannelWatch");
//				if(eqequipment_size >7) continue;
//				map_num.setEnabled(false);
			}
		}
		
		if(position == 0) {
			LinearLayout linearlayout = (LinearLayout) child.findViewById(R.id.viewlayout_right);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearlayout.getLayoutParams();
			params.weight = 0;
			linearlayout.setLayoutParams(params);
			if(eqequipment_size >7) return;
			addAlgorithmItems.addItems(df.getActivity());
		} else {
			LinearLayout linearlayout = (LinearLayout) child.findViewById(R.id.viewlayout_right);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearlayout.getLayoutParams();
			params.weight = 1;
			linearlayout.setLayoutParams(params);
			if(eqequipment_size >7) return;
			addAlgorithmItems.noPageOne();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	public void setAddAlgorithmItems(AddAlgorithmItems addAlgorithmItems) {
		// TODO Auto-generated method stub
		for(TextView v : textViewList) {
			if(addAlgorithmItems.getAlgorithmItems().size()!=0){
				v.setText(addAlgorithmItems.getAlgorithmItems().get(0));
//				if(eqequipment_size >7) continue;
//				map_num.setEnabled(true);
			}else{
				v.setText("ChannelWatch");
//				if(eqequipment_size >7) continue;
//				map_num.setEnabled(false);
			}
			v.setOnClickListener(this);
		}
		this.addAlgorithmItems=addAlgorithmItems;
		
	}
	public void spinnerItemPositionChanged(){
		if(eqequipment_size >7) return;
		if(map_num.getSelectedItemPosition()==0){
			addAlgorithmItems.addItems(df.getActivity());
		}else{
			addAlgorithmItems.noPageOne();
		}
	}
}
