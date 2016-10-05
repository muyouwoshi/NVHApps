package com.example.mobileacquisition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import left.drawer.MainFragment;
import spiner.popwindow.AbstractSpinerAdapter;
import spiner.popwindow.SpinerPopWindow;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import common.CustomTab;
import common.DataCollection;
import common.MyViewPager;
import common.ScaleView;

import draw.map.AI;
import draw.map.ChannelWatching;
import draw.map.ColorMap;
import draw.map.FFT;
import draw.map.OCT;
import draw.map.Order;
import draw.map.RPM_Curve;
import draw.map.RPM_Digit;
import draw.map.RPM_Tachometer;
import draw.map.SPL;
import draw.map.Signal;

public class MainContextView extends RelativeLayout implements OnClickListener,
		OnTouchListener, AbstractSpinerAdapter.IOnItemSelectListener {
	public boolean enlarge = false;
	public Handler myHandler;
	private View view = null;
	private RelativeLayout legend;
	private MainActivity context;
	private ScaleView newView, oldView;
	private LayoutParams oldParams;
	private int width;
	private int height;
	private int clickTag;
	private MyViewPager viewPager;
	// private Spinner algorithm_spinner;
	public RelativeLayout algorithm_spinner_layout,channal_spinner_layout;
	public TextView algorithm_spinner, channel_spinner;
	public RelativeLayout algorithm_title;
	private ImageView algorithm_img, channel_img;
	private SpinerPopWindow algorithm_mSpinerPopWindow;
	private SpinerPopWindow channel_mSpinerPopWindow;
	private ArrayList<String> algorithmItems;
	public ArrayList<String> channelItems;
	private RelativeLayout mapView;
	private OneTabFragment oneTabFragment;
	private ArrayList<String> mapList;
	private boolean check_algorithmspinner = false;
	private boolean check_channelspinner = false;
	private ImageButton rulerButton;
	private RulerView rulerView;
	private View newMainView;

	public MainContextView(Context context, MyViewPager viewPager) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = (MainActivity) context;
		this.viewPager = viewPager;
		view = LayoutInflater.from(context).inflate(R.layout.activitymain_context, null);
		mapView = (RelativeLayout) view.findViewById(R.id.algorithm_view);
		legend = (RelativeLayout) view.findViewById(R.id.legend);
		legend.setOnTouchListener(this);

		MainFragment mainFragment = (MainFragment) this.context
				.getSupportFragmentManager().findFragmentByTag("main");
		mapView.setOnTouchListener(this);
		mapView.setClickable(true);
		algorithm_title = (RelativeLayout)view.findViewById(R.id.algorithm_title);
		algorithm_spinner = (TextView) view
				.findViewById(R.id.algorithm_spinner);
		channel_spinner = (TextView) view.findViewById(R.id.channal_spinner);
		mainFragment.setAlgorithm_spinner(algorithm_spinner);
		//algorithm_spinner.setOnClickListener(this);
		//channel_spinner.setOnClickListener(this);
		algorithm_img = (ImageView) view.findViewById(R.id.algorithm_img);
		channel_img = (ImageView) view.findViewById(R.id.channel_img);
		//algorithm_img.setOnClickListener(this);
		//channel_img.setOnClickListener(this);
		algorithm_spinner_layout = (RelativeLayout) view.findViewById(R.id.algorithm_spinner_layout);
		channal_spinner_layout = (RelativeLayout) view.findViewById(R.id.channal_spinner_layout);
		algorithm_spinner_layout.setOnClickListener(this);
		channal_spinner_layout.setOnClickListener(this);
		
		/*
		 * �㷨������spinner����
		 */

		algorithmItems = mainFragment.getSpinnerValues().getAlgorithmItems();
		algorithm_mSpinerPopWindow = new SpinerPopWindow(context);
		algorithm_mSpinerPopWindow.refreshData(algorithmItems, 0);
		algorithm_mSpinerPopWindow.setItemListener(this);

		CustomTab ct = ((MainActivity) context).mainCustomTab;
		channelItems = ct.channel_spinnerList;
		channel_mSpinerPopWindow = new SpinerPopWindow(context);
		channel_mSpinerPopWindow.refreshData(channelItems, 0);
		channel_mSpinerPopWindow.setItemListener(this);
		
		addView(view, new RelativeLayout.LayoutParams(-1, -1));

		rulerView = (RulerView) view.findViewById(R.id.ruler_view);
		rulerButton = (ImageButton) view.findViewById(R.id.ruler_button);
	}

	private int x, y, left, top, right, bottom;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		// ȡ���

		v.performClick();
		if (v.getId() == R.id.legend) {
			// v.getParent().requestDisallowInterceptTouchEvent(true);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				viewPager.setCanScroll(false);
				x = (int) event.getRawX();
				y = (int) event.getRawY();
				left = v.getLeft();
				top = v.getTop();
				right = v.getRight();
				bottom = v.getBottom();
				break;
			case MotionEvent.ACTION_MOVE:
				int moveX = (int) (event.getRawX() - x);
				int moveY = (int) (event.getRawY() - y);
				if (left + moveX < 0)
					moveX = 1 - left;
				if (right + moveX > this.getWidth())
					moveX = this.getWidth() - right;
				if (top + moveY < 0)
					moveY = 1 - top;
				if (bottom + moveY > this.getHeight())
					moveY = this.getHeight() - bottom;
				v.layout(left + moveX, top + moveY, right + moveX, bottom
						+ moveY);
				break;
			case MotionEvent.ACTION_UP:
				viewPager.setCanScroll(true);
				break;
			}

		}
		return true;
	}

	private void showSpinWindow(View spinner_view,
			SpinerPopWindow spinerPopWindow) {
		if(ThemeLogic.themeType==1){
				spinerPopWindow.mListView.setSelector(R.drawable.spinnerDivider1);
				spinerPopWindow.pop_window.setBackgroundResource(R.drawable.input_box);
		   }else{
			   spinerPopWindow.mListView.setSelector(R.drawable.spinnerDivider);
			   spinerPopWindow.mListView.setDividerHeight(3);
			   spinerPopWindow.pop_window.setBackgroundResource(R.drawable.input_box1);
		   }
		spinerPopWindow.setWidth(spinner_view.getWidth() + 20);
		spinerPopWindow.setHeight(dip2px(100));//
		spinerPopWindow.showAsDropDown(spinner_view, dip2px(20), 0);
	}

	@Override
	public void onItemClick(int position) {
		if (check_algorithmspinner) {
			if (algorithmItems.size() != 1
					&& algorithm_spinner.getText().equals(
							algorithmItems.get(position)))
				return;
			if (position >= 0 && position <= algorithmItems.size()) {
				String value = algorithmItems.get(position);
				algorithm_spinner.setText(value);
			}
			drawMap();
		}
		
		if (channel_img.getVisibility() == View.VISIBLE) {
			if (check_channelspinner) {
				if (channelItems.size() > 1
						&& channel_spinner.getText().equals(
								channelItems.get(position)))
					return;
				String value = channelItems.get(position);
				channel_spinner.setText(value);
				((ColorMap)newView).setChannelIndex(position);
			}
		}
		
		saveTemplate();
	}

	public void drawMap() {
		oldView = newView;
		mapView.removeAllViews();

		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
		if (algorithm_spinner.getText().equals("Signal")) {
			newView = new Signal(context, viewPager);
			newView.setMainContextView(this);
			String xaxis = preference.getString("Signal_XAxis", null);
			String yaxis = preference.getString("Signal_YAxis", null);
			newView.setXLableState(xaxis);
			newView.setYLableState(yaxis);
			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.VISIBLE);
			channel_spinner.setVisibility(View.GONE);
			channel_img.setVisibility(View.GONE);
			rulerView.init(viewPager, newView, rulerButton);
		} else if (algorithm_spinner.getText().equals("SPL")) {
			newView = new SPL(context, viewPager);
			newView.setMainContextView(this);
			String xaxis = preference.getString("SPL_XAxis", null);
			String yaxis = preference.getString("SPL_YAxis", null);
			newView.setXLableState(xaxis);
			newView.setYLableState(yaxis);
			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.VISIBLE);
			channel_spinner.setVisibility(View.GONE);
			channel_img.setVisibility(View.GONE);
			rulerView.init(viewPager, newView, rulerButton);
		} else if (algorithm_spinner.getText().equals("OCT")) {
			newView = new OCT(context, viewPager);
			newView.setMainContextView(this);
			String yaxis = preference.getString("OCT_YAxis", null);
			String xaxis = preference.getString("OCT_XAxis", null);
			newView.setXLableState(xaxis);
			newView.setYLableState(yaxis);
			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.VISIBLE);
			channel_spinner.setVisibility(View.GONE);
			channel_img.setVisibility(View.GONE);
			rulerView.init(viewPager, newView, rulerButton);
		} else if (algorithm_spinner.getText().equals("FFT")) {
		
			newView = new FFT(context, viewPager);
			newView.setMainContextView(this);
			String yaxis = preference.getString("FFT_YAxis", null);
			String xaxis = preference.getString("FFT_XAxis", null);
			newView.setXLableState(xaxis);
			newView.setYLableState(yaxis);

			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.VISIBLE);
			oneTabFragment.setMapList(mapList);
			channel_spinner.setVisibility(View.GONE);
			channel_img.setVisibility(View.GONE);
			rulerView.init(viewPager, newView, rulerButton);
		} else if (algorithm_spinner.getText().equals("AI")) {
	
			newView = new AI(context, viewPager);
			newView.setMainContextView(this);
			String xaxis = preference.getString("AI_XAxis", null);
			String yaxis = preference.getString("AI_YAxis", null);
			newView.setXLableState(xaxis);
			newView.setYLableState(yaxis);

			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.VISIBLE);
			channel_spinner.setVisibility(View.GONE);
			channel_img.setVisibility(View.GONE);
			rulerView.init(viewPager, newView, rulerButton);
		} else if (algorithm_spinner.getText().equals("RPM")) {
			String rpm_display=preference.getString("RPM_Display", null);
			int num=0;
			if(rpm_display.equals("Digit")){
				num=0;
			}else if(rpm_display.equals("Tachometer")){
				num=1;
			}else if(rpm_display.equals("Curve")){
//				rulerView.init(viewPager, newView, rulerButton);
//				rulerView.showInMap(false);
				num=2;
			}
			channel_spinner.setVisibility(View.GONE);
			channel_img.setVisibility(View.GONE);
			drawRPM(num);
		} else if (algorithm_spinner.getText().equals("Waterfall")) {
			
			newView = new ColorMap(context, viewPager);
			String xaxis = preference.getString("ColorMap_XAxis", null);
			String yaxis = preference.getString("ColorMap_YAxis", null);
			newView.setXLableState(xaxis);
			newView.setYLableState(yaxis);
			newView.setMainContextView(this);
			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.GONE);
			channel_spinner.setVisibility(View.VISIBLE);
			
			channel_img.setVisibility(View.VISIBLE);
			rulerView.init(viewPager, newView);
			rulerButton.setVisibility(View.GONE);
			/*
			 * ����ͨ����Ϊ0ʱ��ͨ��SpinnerĬ����ʾ����ͨ�������еĵ�һ��
			 */
			if(channelItems.size()>0){
				channel_spinner.setText(channelItems.get(0));
			}else{
				channel_spinner.setText("");
			}
		} else if (algorithm_spinner.getText().equals("ChannelWatch")) {
			
			newView = new ChannelWatching(context, viewPager);
			newView.setMainContextView(this);
			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.GONE);
			channel_spinner.setVisibility(View.GONE);
			channel_img.setVisibility(View.GONE);
			int isPhone = 0;
			
			rulerView.init(viewPager, rulerButton);
			
			

		}else if(algorithm_spinner.getText().equals("Order")){
			newView = new Order(context);
			newView.setMainContextView(this);
			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.VISIBLE);
			channel_spinner.setVisibility(View.GONE);
			channel_img.setVisibility(View.GONE);
			rulerView.init(viewPager, newView, rulerButton);
		}
		
		context.addViewList(newView);
		context.removeViewList(oldView);
		
		//ChannelWatch��Waterfall��Ļ����
		if (algorithm_spinner.getText().equals("ChannelWatch")||algorithm_spinner.getText().equals("Waterfall")|| algorithm_spinner.getText().equals("OCT") ){
			DisplayMetrics metrics =context.getResources().getDisplayMetrics();
	        float xdpi = metrics.xdpi;
	        int width = metrics.widthPixels;
	        int height = metrics.heightPixels;
	        double z = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
	        double eqequipment_size = Math.round(z /xdpi);
	        if(eqequipment_size >7) return;
			if(!enlarge) {
				enlarge();
				if(oneTabFragment.getMainContextView2() != null){
					Toast.makeText(context, algorithm_spinner.getText()+getResources().getString(R.string.hasenlarge), Toast.LENGTH_SHORT).show();
				}
			}
			newView.setAlwaysEnlager(true);
			
		}else{
			if(newView!=null){
				newView.setAlwaysEnlager(false);
			}
			setZorder();
		}
		saveTemplate();
	}

	private void saveTemplate() {
		// TODO Auto-generated method stub
		SharedPreferences preference = context.getSharedPreferences(
				"hz_5D", 0);
		
		SharedPreferences.Editor editor = preference.edit();
		
		int pageNum = 0;
		ArrayList<Fragment> fragements = context.mainCustomTab.getFragmentList();
		for(int i = 0;i< fragements.size();i++){
			if(oneTabFragment == fragements.get(i)) pageNum = i;
		}
		ArrayList<String> strList = new ArrayList<String>();
		if(oneTabFragment.getMainContextView1()!= null)strList.add(oneTabFragment.getMainContextView1().algorithm_spinner.getText().toString());
		if(oneTabFragment.getMainContextView3()!= null)strList.add(oneTabFragment.getMainContextView3().algorithm_spinner.getText().toString());
		if(oneTabFragment.getMainContextView2()!= null)strList.add(oneTabFragment.getMainContextView2().algorithm_spinner.getText().toString());
		if(oneTabFragment.getMainContextView4()!= null)strList.add(oneTabFragment.getMainContextView4().algorithm_spinner.getText().toString());
		
		
		
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(strList);
			String displaylist=new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
			objectOutputStream.close();
			editor.putString("page"+pageNum, displaylist);
			editor.commit();
			byteArrayOutputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawRPM(int position) {
		mapView.removeAllViews();
		switch (position) {
		case 0:
			RPM_Digit rpm_Digit = new RPM_Digit(context);
			mapView.addView(rpm_Digit, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.GONE);
			rulerView.init(viewPager, rulerButton);

			break;
		case 1:
			RPM_Tachometer rpm_Tachometer = new RPM_Tachometer(context);
			mapView.addView(rpm_Tachometer, new RelativeLayout.LayoutParams(-1,
					-1));
			legend.setVisibility(View.GONE);
			rulerView.init(viewPager, rulerButton);
			break;
		case 2:
			SharedPreferences preference = context.getSharedPreferences(
					"hz_5D", 0);
			newView = new RPM_Curve(context, viewPager);
			newView.setMainContextView(this);
			String xaxis = preference.getString("RPM_XAxis", null);
			String yaxis = preference.getString("RPM_YAxis", null);
			newView.setXLableState(xaxis);
			newView.setYLableState(yaxis);
			
			rulerView.init(viewPager, newView, rulerButton);
			
			mapView.addView(newView, new RelativeLayout.LayoutParams(-1, -1));
			legend.setVisibility(View.VISIBLE);
			
			break;
		}

	}

	public int dip2px(float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);

	}

	public View getView() {
		return view;
	}

	public void changeLayout() {
		oldParams = (LayoutParams) getLayoutParams();
		if (clickTag % 2 != 0) {
			width = oldParams.width;
			height = oldParams.height;
			oldParams.width = -1;
			oldParams.height = -1;
			setLayoutParams(oldParams);
			bringToFront();
		} else {
			oldParams.width = width;
			oldParams.height = height;
			setLayoutParams(oldParams);
		}

	}
	
	public int getClickTag(){
		return this.clickTag;
	}
	
	public void enlarge(){
		enlarge = true;
		oldParams = (LayoutParams) getLayoutParams();
		width = oldParams.width;
		height = oldParams.height;
		oldParams.width = -1;
		oldParams.height = -1;
		setLayoutParams(oldParams);
		bringToFront();
		
	}
	
	public void reduce(){
		enlarge = false;
		oldParams.width = width;
		oldParams.height = height;
		setLayoutParams(oldParams);
		setZorder();
	}
	private void setZorder() {
		// TODO Auto-generated method stub
		if(oneTabFragment.getMainContextView1()!=null&&oneTabFragment.getMainContextView1().enlarge && 
				oneTabFragment.getMainContextView1()!= this) {
			oneTabFragment.getMainContextView1().bringToFront();
		}
		if(oneTabFragment.getMainContextView2()!=null&&oneTabFragment.getMainContextView2().enlarge&& 
				oneTabFragment.getMainContextView2()!= this){
			oneTabFragment.getMainContextView2().bringToFront();
		}
		if(oneTabFragment.getMainContextView3()!=null&&oneTabFragment.getMainContextView3().enlarge&& 
				oneTabFragment.getMainContextView3()!= this){
			oneTabFragment.getMainContextView3().bringToFront();

		}
		if(oneTabFragment.getMainContextView4()!=null && oneTabFragment.getMainContextView4().enlarge&& 
				oneTabFragment.getMainContextView4()!= this) {
			oneTabFragment.getMainContextView4().bringToFront();
		}

	}
	public void setSpinnerPosition(String str) {

		// algorithm_spinner.setSelection(getIndex(str));
		algorithm_spinner.setText(str);
		drawMap();
		//saveTemplate();
	}
	public void setChannelSpinnerPosition(String str) {
		channel_spinner.setText(str);
		/*
		 * ����ͨ����Ϊ0ʱ��ͨ��SpinnerĬ����ʾ����ͨ�������еĵ�һ��
		 */
		if(channelItems.size()>0){
			channel_spinner.setText(channelItems.get(0));
		}else{
			channel_spinner.setText("");
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() ==R.id.algorithm_spinner_layout) {// R.id.algorithm_spinner|| v.getId() == R.id.algorithm_img
			showSpinWindow(algorithm_spinner, algorithm_mSpinerPopWindow);
			check_algorithmspinner = true;
			check_channelspinner = false;
		}
		if(DataCollection.isRecording){
			Toast.makeText(context.getApplicationContext(), R.string.Collecting_data, Toast.LENGTH_SHORT).show();
			return;
		}
		if (v.getId() ==R.id.channal_spinner_layout ) {//R.id.channal_spinner || v.getId() == R.id.channel_img
			showSpinWindow(channel_spinner, channel_mSpinerPopWindow);
			check_algorithmspinner = false;
			check_channelspinner = true;
		}
	}

	public RelativeLayout getLegend() {
		return legend;
	}

	public void setLegend(RelativeLayout legend) {
		this.legend = legend;
	}

	public void setUnitChangeState(int axisType, String unitType) {
		if (newView == null) {
			return;
		}
		switch (axisType) {
		case 0:
			newView.setXLableState(unitType);
			break;
		case 1:
			newView.setYLableState(unitType);
			break;
		}
		newView.invalidate();
	}

	public View getNewMainView() {
		return newMainView;
	}

	public void setNewMainView(View newMainView) {
		this.newMainView = newMainView;
	}

	public void setOneTabFragment(OneTabFragment oneTabFragment, int position) {
		this.oneTabFragment = oneTabFragment;
	}

	public void setMapList(ArrayList<String> mapList) {
		this.mapList = mapList;
	}

	public TextView getAlgorithm_spinner() {
		return algorithm_spinner;
	}

	public void setAlgorithm_spinner(TextView algorithm_spinner) {
		this.algorithm_spinner = algorithm_spinner;
	}
	
	public void removeContextList(){
		context.removeViewList(newView);
	}
}