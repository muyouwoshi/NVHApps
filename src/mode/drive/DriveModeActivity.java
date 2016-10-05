package mode.drive;
import java.util.ArrayList;
import java.util.Locale;

import spiner.popwindow.AbstractSpinerAdapter;
import spiner.popwindow.SpinerPopWindow;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import bottom.drawer.AddChannelViewPager;
import bottom.drawer.BottomOperate;
import bottom.drawer.VerticalViewPager;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.RulerView;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.WifiDialog;
import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;

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

/**
 * <b>类名</b>: DriveModeActivity，驾驶模式界面<br/>
 * <b>说明</b>:  <br/>
 */
public class DriveModeActivity extends FragmentActivity implements OnClickListener
										,OnTouchListener, AbstractSpinerAdapter.IOnItemSelectListener,ThemeChangeListener{
	private DataCollection dm_Record;
	private ImageButton startMode;
	public TextView algorithm_title,channel_title;
	private MyViewPager algorithm_view;
	private ArrayList<View> views;
	private ArrayList<ScaleView> scaleViews;
	private ArrayList<View> layouts;
	private ScaleView signal,spl,oct,fft,ai,rpm,colmap,order,channelWatching;
	private ArrayList<String> title_str;
	private RelativeLayout rpm_table;
	private TextView drive_title;
	private LinearLayout algorithm_rpm,algorithm,chan_bt, channel_layout;
	private int type;
	private Drive_Dialog driveDialog;
	private VerticalViewPager verticalViewPager;
	private RelativeLayout drive_legend;
	public AddChannelViewPager addChannelViewPager;
	private LinearLayout bt_layout;
	private LayoutParams params;
	private ImageButton bt_landscrpe,bt_return;
	private Context context;
	public ArrayList<Integer> isActivated_ChanNum;//= new ArrayList<Integer>();
	private int Equipment_Num;
	private int[] activityChannelArray;
//	public String cc;
	private String languagedrive;
	private WifiDialog wifiDialog;//=new WifiDialog();
	public MyPagerAdapter mPagerAdapter;
	private SpinerPopWindow channel_mSpinerPopWindow;
	private ArrayList<String> channelItems=new ArrayList<String>();
	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		registerReceiver(wifiDialog.WifiBroadcast, filter);
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		unregisterReceiver(wifiDialog.WifiBroadcast);
		super.onStop();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 闅愬幓鏍囬鏍忥紙搴旂敤绋嬪簭鐨勫悕瀛楋級
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 闅愬幓鐘舵�鏍忛儴鍒�鐢垫睜绛夊浘鏍囧拰涓�垏淇グ閮ㄥ垎)

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		type=this.getResources().getConfiguration().orientation;
		context=this;
		switch(ThemeLogic.themeType){
		case 1:
			setTheme(R.style.mode1);
			break;
		case 2:
			setTheme(R.style.mode2);
		}
		setContentView(R.layout.driving_mode_activity);
		
		wifiDialog=new WifiDialog(this);
		
		SharedPreferences preference = context.getSharedPreferences("Equipment", 0);
		Equipment_Num=preference.getInt("Equipment", 0);

		Intent intent = this.getIntent(); 
//		isActivated_ChanNum=(ArrayList<Integer>)intent.getSerializableExtra("isActivated_ChanNum");

//		newProject=intent.getStringExtra("newProject");//鑾峰緱鏂板缓鎴栬�杞藉叆鐨勫伐绋�

//		languagedrive=intent.getStringExtra("languagedrive");
		

		////////////////// 16.3.4 启动此aty的intent传递的参数都在bundle里，so，应该从bundle里提取。 崔维友 -begin

		Bundle bundle = intent.getExtras();
		languagedrive = bundle.getString("languagedrive");
		isActivated_ChanNum = (ArrayList)(bundle.getParcelableArrayList("isActivated_ChanNum"));
		Log.e("ard", "椹鹃┒妯″紡瀵煎叆鏁版嵁锛歩sActivated_ChanNum=" + isActivated_ChanNum); 
		////////////////// -over
	    addView();
	    for(int i=0;i<isActivated_ChanNum.size();i++){
			channelItems.add("Channel"+(i+1));
		}
	    if(channelItems.size()>0){
	    	channel_title.setText(channelItems.get(0));
	    }else{
	    	channel_title.setText("");
	    }
	    /////////////////// 16.3.8 同步 TODO 录制
	    
	    if(BottomOperate.getDataCollection().isRecording){
	    	addChannelViewPager.setContext(DriveModeActivity.this);
	    	startMode.setSelected(false);
			onClick(startMode);
	    }
	    
	    /////////////////////////////////   
		ThemeLogic.getInstance().addListener(this);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		type=this.getResources().getConfiguration().orientation;
		if(type == Configuration.ORIENTATION_PORTRAIT){
			if(ThemeLogic.themeType==1){
				bt_landscrpe.setImageResource(R.drawable.button_heng_selector);
			}else{
				
				bt_landscrpe.setImageResource(R.drawable.button_heng_selector1);
			}
			
			params = (LayoutParams) drive_title.getLayoutParams();
			params.width = -1;
			params.height = 0;
			params.weight=(float) 0.3;
			drive_title.setLayoutParams(params);
			algorithm_rpm.setOrientation(LinearLayout.VERTICAL);
			params = (LayoutParams) algorithm.getLayoutParams();
			params.width = -1;
			params.height = 0;
			params.weight=2;
			algorithm.setLayoutParams(params);
			params = (LayoutParams) rpm_table.getLayoutParams();
			params.width = -1;
			params.height = 0;
			params.weight=(float) 0.8;
			rpm_table.setLayoutParams(params);
			chan_bt.setOrientation(LinearLayout.VERTICAL);
			verticalViewPager.setLayoutParams(params);
			bt_layout.setLayoutParams(params);
			if(languagedrive.equals("1")){
				updateLange(Locale.ENGLISH);
				
			}else{
				updateLange(Locale.SIMPLIFIED_CHINESE);
			}
		 }else{
			
			if(ThemeLogic.themeType==1){
				bt_landscrpe.setImageResource(R.drawable.button_shu_selector);
			}else{
				
				bt_landscrpe.setImageResource(R.drawable.button_shu_selector1);
			}
			params = (LayoutParams) drive_title.getLayoutParams();
			params.width = -1;
			params.height =0;
			params.weight=(float) 0.5;
			drive_title.setLayoutParams(params);
			algorithm_rpm.setOrientation(LinearLayout.HORIZONTAL);
			params = (LayoutParams) algorithm.getLayoutParams();
			params.width = 0;
			params.height =-1;
			params.weight=3;
			algorithm.setLayoutParams(params);
			params = (LayoutParams) rpm_table.getLayoutParams();
			params.width = 0;
			params.height =-1;
			params.weight=1;
			rpm_table.setLayoutParams(params);
			chan_bt.setOrientation(LinearLayout.HORIZONTAL);
			verticalViewPager.setLayoutParams(params);
			bt_layout.setLayoutParams(params); 
			if(languagedrive.equals("1")){
				updateLange(Locale.ENGLISH);
				
			}else{
				updateLange(Locale.SIMPLIFIED_CHINESE);
			}
		 }
		super.onConfigurationChanged(newConfig);
	}
	
	private void updateLange(Locale locale) {
		Resources res = getResources();
		Configuration config = res.getConfiguration();
		config.locale = locale;
		DisplayMetrics dm = res.getDisplayMetrics();
		res.updateConfiguration(config, dm);
	}
	
	private void addView(){
		    verticalViewPager = (VerticalViewPager)findViewById(R.id.channel_view);
		    addChannelViewPager=new AddChannelViewPager(this);
		    addChannelViewPager.showDriveChannelViewPager(verticalViewPager,isActivated_ChanNum);
		    drive_legend = (RelativeLayout)findViewById(R.id.drive_legend);
			//	scrollView=(ScrollView)view.findViewById(R.id.scrollView1);
		    drive_legend.setOnTouchListener(this);
		    addChannelViewPager.setDrive_legend(drive_legend);
		    drive_title=(TextView)findViewById(R.id.drive_title);
		    algorithm_rpm=(LinearLayout)findViewById(R.id.algorithm_rpm);
		    algorithm=(LinearLayout)findViewById(R.id.algorithm);
		    chan_bt=(LinearLayout)findViewById(R.id.chan_bt);
			title_str = new ArrayList<String>();
			algorithm_title=(TextView)findViewById(R.id.algorithm_title);
			algorithm_view=(MyViewPager)findViewById(R.id.drive_algorithm_view);
			algorithm_view.setOnPageChangeListener(PageChange);
			startMode=(ImageButton)findViewById(R.id.startMode);
			if(ThemeLogic.themeType==1){
				startMode.setImageResource(R.drawable.drive_button_play_n);
			}else{
				
				startMode.setImageResource(R.drawable.button_play_n1);
			}
			startMode.setOnClickListener(this);
			views = new ArrayList<View>();
			scaleViews = new ArrayList<ScaleView>();
			layouts = new ArrayList<View>();
			rpm_table=(RelativeLayout)findViewById(R.id.rpm_table);
			rpm_table.setOnClickListener(this);
			bt_layout=(LinearLayout)findViewById(R.id.bt_layout);
			bt_landscrpe=(ImageButton)findViewById(R.id.bt_landscrpe);
			bt_landscrpe.setOnClickListener(this);
			channel_layout = (LinearLayout)findViewById(R.id.channel_layout);
			channel_layout.setOnClickListener(this);
			channel_title = (TextView)findViewById(R.id.channel_title);
			bt_return=(ImageButton)findViewById(R.id.bt_return);
			bt_return.setOnClickListener(this);
			channel_mSpinerPopWindow = new SpinerPopWindow(this);
			channel_mSpinerPopWindow.refreshData(channelItems, 0);
			channel_mSpinerPopWindow.setItemListener(this);
			SharedPreferences preference =getSharedPreferences("hz_5D", 0);
			String algorithmChecked = preference.getString("Signal", "close");
			if(algorithmChecked.equals("open")) {
				signal = new Signal(this,algorithm_view);
				RelativeLayout layout = new RelativeLayout(this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				layout.addView(signal);
				
				addRuler(layout,signal,algorithm_view);
				
				layouts.add(layout);
				views.add(signal);
				scaleViews.add(signal);
				title_str.add("Signal");
			}
			algorithmChecked = preference.getString("SPL", "close");
			if(algorithmChecked.equals("open")) {
				spl = new SPL(this,algorithm_view);
				RelativeLayout layout = new RelativeLayout(this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				layout.addView(spl);
				
				addRuler(layout,spl,algorithm_view);
				
				layouts.add(layout);
				views.add(spl);
				scaleViews.add(spl);

				title_str.add("SPL");
			}
			algorithmChecked = preference.getString("OCT", "close");
			if(algorithmChecked.equals("open")) {
				oct = new OCT(this);
				RelativeLayout layout = new RelativeLayout(this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				layout.addView(oct);
				addRuler(layout,oct,algorithm_view);

				layouts.add(layout);
				views.add(oct);
				scaleViews.add(oct);

				title_str.add("OCT");
			}
			algorithmChecked = preference.getString("FFT", "close");
			if(algorithmChecked.equals("open")) {
				fft = new FFT(this,algorithm_view);
				RelativeLayout layout = new RelativeLayout(this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				layout.addView(fft);
				
				addRuler(layout,fft,algorithm_view);
				
				layouts.add(layout);
				views.add(fft);
				scaleViews.add(fft);

				title_str.add("FFT");
			}
			algorithmChecked = preference.getString("AI", "close");
			if(algorithmChecked.equals("open")) {
				ai = new AI(this,algorithm_view);
				RelativeLayout layout = new RelativeLayout(this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				layout.addView(ai);
				addRuler(layout,ai,algorithm_view);
				layouts.add(layout);
				views.add(ai);
				scaleViews.add(ai);

				title_str.add("AI");
			}
			algorithmChecked = preference.getString("RPM", "close");
			if(algorithmChecked.equals("open")) {
				String rpm_display=preference.getString("RPM_Display", null);
				
				RelativeLayout layout = new RelativeLayout(this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				if(rpm_display.equals("Digit")){
					RPM_Digit rpm_Digit = new RPM_Digit(context);
					layout.addView(rpm_Digit);
					layouts.add(layout);
					views.add(rpm_Digit);
					
				}else if(rpm_display.equals("Tachometer")){
					RPM_Tachometer rpm_Tachometer=new RPM_Tachometer(context);
					layout.addView(rpm_Tachometer);
					layouts.add(layout);
					views.add(rpm_Tachometer);
				}else if(rpm_display.equals("Curve")){
					rpm = new RPM_Curve(this);
					layout.addView(rpm);
					addRuler(layout,rpm,algorithm_view);
					layouts.add(layout);
					
					views.add(rpm);
					scaleViews.add(rpm);

				}
				title_str.add("RPM");
			}
			algorithmChecked = preference.getString("Waterfall", "close");
			if(algorithmChecked.equals("open")) {
				colmap = new ColorMap(this, algorithm_view);
				RelativeLayout layout = new RelativeLayout(this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				layout.addView(colmap);
				layouts.add(layout);
				views.add(colmap);
				title_str.add("Waterfall");
			}
			algorithmChecked = preference.getString("Order", "close");
			if(algorithmChecked.equals("open")) {
				order = new Order(this);
				RelativeLayout layout = new RelativeLayout(this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				layout.addView(order);
				layouts.add(layout);
				views.add(order);
				title_str.add("Order");
			}
			channelWatching = new ChannelWatching(this);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			layout.addView(channelWatching);
			layouts.add(layout);
			views.add(channelWatching);
			title_str.add("ChannelWatch");
			int isPhone = 0;
			try {
				TelephonyManager t = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				isPhone = t.getPhoneType();
				if (isPhone == 1) {// 鏄墜鏈哄氨鍏ㄥ睆鏀惧ぇ

				}
			} catch (Exception e) {
				isPhone = 0;
			}

	        //濉厖ViewPager鐨勬暟鎹�閰嶅櫒

	        mPagerAdapter = new MyPagerAdapter(layouts);
	        algorithm_view.setAdapter(mPagerAdapter);
	        if(title_str.size()!=0){
	        	algorithm_title.setText(title_str.get(0));
	        }
	        setChannelLayoutVisible();
	        for(int i=0;i < AddChannelViewPager.drive_channelCount.size();i++){
	        	addChannelViewPager.chanButtonList.get(AddChannelViewPager.drive_channelCount.get(i)-1).setBackgroundResource(R.drawable.ico_channel_sel2);
	        	addChannelViewPager.chanButtonList.get(AddChannelViewPager.drive_channelCount.get(i)-1).setSelected(true);
	        }
	        
	        if(AddChannelViewPager.drive_channelCount.size()!=0){
	        	addChannelViewPager.addDriveLegendView();
	        }
	        
	        addChannelViewPager.setScaleViews(scaleViews);
	}
	
	private void addRuler(RelativeLayout layout, ScaleView view, MyViewPager viewPager) {
		
		RulerView rulerView = new RulerView(this);
		rulerView.setId(1000);
		RelativeLayout.LayoutParams rulerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		rulerParams.leftMargin = 50;
		rulerParams.bottomMargin = 50;
//		rulerView.setLayoutParams(rulerParams);
		layout.addView(rulerView,rulerParams);

		ImageButton rulerButton = new ImageButton(context);
		rulerButton.setBackgroundResource(R.drawable.ico_biaochi01);
		RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(-1,-1);
		buttonParams.leftMargin = 25;
		buttonParams.bottomMargin = 20;
		
		buttonParams.height = 30;
		buttonParams.width = 50;
		
		buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		rulerButton.setLayoutParams(buttonParams);
		
		activityChannelArray = new int[]{0,0,0,0,0,0,0,0};
//		view.setActivityChannelArray(activityChannelArray);
		rulerView.init(viewPager, view, rulerButton);
		
		layout.addView(rulerButton);

	}

	/** 弹出框修改算法 */
	public void refreshView(){
		mPagerAdapter.removeView(algorithm_view.getCurrentItem());
		View currentView = algorithm_view.getChildAt(algorithm_view.getCurrentItem());
		algorithm_view.removeView(currentView);
		algorithm_view.removeAllViews();
		if(algorithm_title.getText().equals("Signal")){
			signal = new Signal(this,algorithm_view);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			layout.addView(signal);
			addRuler(layout,signal,algorithm_view);
			title_str.remove(algorithm_view.getCurrentItem());
			layouts.add(algorithm_view.getCurrentItem(), layout);
			title_str.add(algorithm_view.getCurrentItem(), "Signal");
			
		}else if(algorithm_title.getText().equals("SPL")){
			spl = new SPL(this,algorithm_view);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			layout.addView(spl);
			addRuler(layout,spl,algorithm_view);
			title_str.remove(algorithm_view.getCurrentItem());
			layouts.add(algorithm_view.getCurrentItem(), layout);
			title_str.add(algorithm_view.getCurrentItem(), "SPL");
		}else if(algorithm_title.getText().equals("OCT")){
			oct = new OCT(this);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			layout.addView(oct);
			addRuler(layout,oct,algorithm_view);
			title_str.remove(algorithm_view.getCurrentItem());
			layouts.add(algorithm_view.getCurrentItem(), layout);
			title_str.add(algorithm_view.getCurrentItem(), "OCT");
		}else if(algorithm_title.getText().equals("FFT")){
			fft = new FFT(this,algorithm_view);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			layout.addView(fft);
			addRuler(layout,fft,algorithm_view);
			title_str.remove(algorithm_view.getCurrentItem());
			layouts.add(algorithm_view.getCurrentItem(), layout);
			title_str.add(algorithm_view.getCurrentItem(), "FFT");
		}else if(algorithm_title.getText().equals("AI")){
			ai = new AI(this,algorithm_view);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			layout.addView(ai);
			addRuler(layout,ai,algorithm_view);
			title_str.remove(algorithm_view.getCurrentItem());
			layouts.add(algorithm_view.getCurrentItem(), layout);
			title_str.add(algorithm_view.getCurrentItem(), "AI");
		}else if(algorithm_title.getText().equals("RPM")){
			SharedPreferences preference =getSharedPreferences("hz_5D", 0);
			String rpm_display=preference.getString("RPM_Display", null);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			if(rpm_display.equals("Digit")){
				RPM_Digit rpm_Digit = new RPM_Digit(context);
				layout.addView(rpm_Digit);
			}else if(rpm_display.equals("Tachometer")){
				RPM_Tachometer rpm_Tachometer=new RPM_Tachometer(context);
				layout.addView(rpm_Tachometer);
			}else if(rpm_display.equals("Curve")){
				rpm = new RPM_Curve(this);
				layout.addView(rpm);
				addRuler(layout,rpm,algorithm_view);
			}
			title_str.remove(algorithm_view.getCurrentItem());
			layouts.add(algorithm_view.getCurrentItem(), layout);
			title_str.add(algorithm_view.getCurrentItem(), "RPM");
			
		}else if(algorithm_title.getText().equals("Waterfall")){
			colmap = new ColorMap(this);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			layout.addView(colmap);
			title_str.remove(algorithm_view.getCurrentItem());
			layouts.add(algorithm_view.getCurrentItem(), layout);
			title_str.add(algorithm_view.getCurrentItem(), "Waterfall");
			
		}else if(algorithm_title.getText().equals("Order")){
			order = new Order(this);
			RelativeLayout layout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(lp);
			layout.addView(order);
			title_str.remove(algorithm_view.getCurrentItem());
			layouts.add(algorithm_view.getCurrentItem(), layout);
			title_str.add(algorithm_view.getCurrentItem(), "Order");
			
		}
        mPagerAdapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View v) {
		if(driveDialog==null){
			driveDialog = new Drive_Dialog(this);
		}
		switch(v.getId()){	
		case R.id.startMode:
			if (!startMode.isSelected()) {
				if(Equipment_Num==0){
					/*Toast.makeText(context, "Choised a device to get datas,please",
							Toast.LENGTH_SHORT).show();*/
					return;
				}else {
					if(isActivated_ChanNum.size()==0){//addChannelViewPager.channelCount.size()==0||
						Toast.makeText(context, R.string.Choose_channel, Toast.LENGTH_SHORT).show();
						return;
					}else{
						play();
					}
				}
			} else {
				stop();
			}
			break;
		case R.id.bt_landscrpe:

			 //璁剧疆灞忓箷涓烘í灞�

            if(type == Configuration.ORIENTATION_PORTRAIT){
                DriveModeActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            //璁剧疆涓虹疆灞忓箷涓虹珫灞�

            }else{      	
            	DriveModeActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
			break;
		case R.id.bt_return:
			setResult(0);
			finish();
			//addChannelViewPager.drive_channelCount.clear();
			break;
		case R.id.rpm_table:
			driveDialog.setVisibility(true);
			break;
		case R.id.channel_layout:
			showSpinWindow(channel_title, channel_mSpinerPopWindow);
			break;
		default:
			break;
		}
	}
	private void showSpinWindow(View spinner_view,
			SpinerPopWindow spinerPopWindow) {
		spinerPopWindow.setWidth(spinner_view.getWidth() + 20);
		spinerPopWindow.setHeight(dip2px(100));//
		spinerPopWindow.showAsDropDown(spinner_view, dip2px(20), 0);
	}
	public int dip2px(float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);

	}
	private void play(){	

		if(ThemeLogic.themeType==1){
			startMode.setImageResource(R.drawable.drive_button_stop_n);
		}else{
			
			startMode.setImageResource(R.drawable.button_stop_n1);
		}

		startMode.setSelected(true);
		

		//////// 驾驶模式和普通模式使用同一个DataCollection（此类应设计为单例。如果模式切换不暂停录制还须使之运行于Server中）
//		dm_Record = new DataCollection(MyHandler,this);
		dm_Record = BottomOperate.getDataCollection();
		dm_Record.setHandler(MyHandler);
		dm_Record.setContext(this);
		
		/////////////////////  - over
		
		dm_Record.Start(1);// TODO 暂时指定为本机模式

		if(!algorithm_title.getText().equals("Waterfall")){
			if(AddChannelViewPager.drive_channelCount.size()==0){
				Toast.makeText(context,
						R.string.startMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void stop(){
		if(ThemeLogic.themeType==1){
			startMode.setImageResource(R.drawable.drive_button_play_n);
		}else{
			
			startMode.setImageResource(R.drawable.button_play_n1);
		}
		
		startMode.setSelected(false);
		if(dm_Record!=null){
			dm_Record.Stop();
		}
	}
	private void setChannelLayoutVisible(){
		if(algorithm_title.getText().toString().equals("Waterfall")){
        	channel_layout.setVisibility(View.VISIBLE);
        }else{
        	channel_layout.setVisibility(View.GONE);
        }
	}
	private OnPageChangeListener PageChange = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			algorithm_title.setText(title_str.get(arg0));
			SharedPreferences preference =getSharedPreferences("hz_5D", 0);
			boolean isShown= preference.getBoolean("show_legend"+algorithm_title.getText(), false);
			if(isShown){
				drive_legend.setVisibility(View.GONE);
			}else{
				drive_legend.setVisibility(View.VISIBLE);
				if(algorithm_title.getText().equals("ChannelWatch")
		        		||algorithm_title.getText().equals("Waterfall")
		        		||algorithm_title.getText().equals("OCT")){
		        	drive_legend.setVisibility(View.GONE);
		        }else if(algorithm_title.getText().equals("RPM")){
		        	String rpm_display=preference.getString("RPM_Display", null);
					
					if(rpm_display.equals("Digit")){
						drive_legend.setVisibility(View.GONE);
					}else if(rpm_display.equals("Tachometer")){
						drive_legend.setVisibility(View.GONE);
					}else if(rpm_display.equals("Curve")){
						drive_legend.setVisibility(View.VISIBLE);
					}
		        	
		        }else{
		        	drive_legend.setVisibility(View.VISIBLE);
		        }
			}
			setChannelLayoutVisible();
		    int left = preference.getInt("drive_legend_left", -1);
		    int top = preference.getInt("drive_legend_top", -1);
		  //  int right=legend_preference.getInt("drive_legend_right",0);
		  //  int bottom=legend_preference.getInt("drive_legend_bottom", 0);
		    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
		    params.leftMargin=left;
		    params.topMargin=top;
		    if(left!=-1&&top!=-1){
		    	drive_legend.setLayoutParams(params);
		    }
			//drive_legend.layout(left, top, right, bottom);
			//drive_legend.setX(left);
			//drive_legend.setY(top);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
	
	Handler MyHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 1:
				for(View i : views) {
//					((ScaleView)i).startCaculate();
				}
			}
			super.handleMessage(msg);
		}
	};
	
	private int x,y,left,top,right,bottom;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		SharedPreferences preference =getSharedPreferences("hz_5D", 0);
		SharedPreferences.Editor editor = preference.edit();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
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
				if (right + moveX > algorithm.getWidth())
					moveX = algorithm.getWidth()- right;
				if (top + moveY < 0)
					moveY = 1 - top;
				if (bottom + moveY > algorithm.getHeight()-algorithm_title.getHeight())
					moveY = algorithm.getHeight()-algorithm_title.getHeight() - bottom;
				v.layout(left + moveX, top + moveY, right + moveX, bottom+ moveY);
				editor.putInt("drive_legend_left", left + moveX);
				editor.putInt("drive_legend_top", top + moveY);
				//editor.putInt("drive_legend_right",right + moveX);
				//editor.putInt("drive_legend_bottom", bottom+ moveY);
				editor.commit();
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
			return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(DataCollection.isRecording){
			Toast.makeText(context.getApplicationContext(),

					 R.string.Collecting_data, Toast.LENGTH_SHORT)//璇疯緭鍏ュ瘑鐮�

					.show();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
					finish();
				
		}
		return super.onKeyDown(keyCode, event);
	}
	public AddChannelViewPager getAddChannelViewPager(){
		return addChannelViewPager;
	}

	@Override
	public void onItemClick(int position) {
		// TODO Auto-generated method stub
		if (channelItems.size() > 1
				&& channel_title.getText().equals(
						channelItems.get(position)))
			return;
		String value = channelItems.get(position);
		channel_title.setText(value);
		((ColorMap)colmap).setChannelIndex(position);
	}

	@SuppressLint("NewApi")
	@Override
	public void onThemeChanged() {
		switch (ThemeLogic.themeType) {

		case 1:
			setTheme(R.style.mode1);
			break;
		case 2:
			setTheme(R.style.mode2);
			break;
		}
		
		TypedArray typedArray = obtainStyledAttributes(R.styleable.myStyle);
		drive_title.setBackground(typedArray.getDrawable(R.styleable.myStyle_TabUpLayout));
		bt_landscrpe.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_landscrpe));
		bt_return.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_return));

		typedArray.recycle();
		
	}
	public ArrayList<View> getViews(){
		return views;
	}
	public int getViewsSize(){
		return views.size();
	}
}