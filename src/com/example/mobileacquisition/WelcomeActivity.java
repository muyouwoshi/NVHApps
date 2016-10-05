package com.example.mobileacquisition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import left.drawer.LeftExplandableListView;
import bottom.drawer.BottomOperate;
import bottom.drawer.VerticalViewPager;

import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import preference.welcome.EquipmentFragment;
import preference.welcome.LanguageFragment;
import preference.welcome.MyAPP;
import preference.welcome.SetDialog;
import preference.welcome.SkinFragment;
import spiner.popwindow.AbstractSpinerAdapter;
import spiner.popwindow.SpinerPopWindow;
/**欢迎界面*/
public class WelcomeActivity extends FragmentActivity implements OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener, ThemeChangeListener{//
	private final int Equipment0 = 100;
	private final int Equipment1 = 101;
	private final int Equipment2 = 102;
	private final int Chinese =-11 ;
	private final int English =11 ;
	private final int Skin0 = -1;
	private final int Skin1 = 1;
	/** Wifi列表弹窗 */
	private WifiDialog wifiDialog;//=new WifiDialog();
	/** 欢迎界面的消息处理器 */
	private MyHandler handler = null;
	/** Application上下文 **/
	private MyAPP mAPP = null;
	//private Spinner templateSelection;
	private TextView template;
	private FragmentManager manager;
	private SpinerPopWindow mSpinerPopWindow;
	//private ArrayAdapter<String> adapter;
	/** 左上角下拉框数据集合-模板名称 */
	private ArrayList<String> spinnerList=new ArrayList<String>();
	/** 左上角下拉框数据集合-模板路径，必须紧紧配合spinnerList使用 */
	private ArrayList<File> spinnerPathList=new ArrayList<File>();
	private Button blankOrPrevious;
	private ImageButton preferenceSetting;
	private WifiConfiguretion wifiConfiguretion;
	private ImageView equipmentImageView;
	private TextView equipmentValue;//,signalValue,signal_strength;
//	private RelativeLayout equipment;//设备Layout
	private RelativeLayout welcome_activity;
	private int skinChangedId=-1;
	public static int languageChangedId=-11;
	private int equipmentId=2;
	public SetDialog setDialog;
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	@SuppressLint("Recycle")
	private SharedPreferences.Editor editor;
	private boolean ifConnectSuccessfully=false;//判断连接成功的条件，修改人：刘亦茜
	public boolean isActive=false;//记录当前已经进入后台的标记
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
		super.onStop();
		unregisterReceiver(wifiDialog.WifiBroadcast);
		if (!isAppOnForeground()) {  
            //app 进入后台  
               
            //全局变量
			isActive = false;// 记录当前已经进入后台  
		} 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 隐去标题栏（应用程序的名字）
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐去状态栏部分(电池等图标和一切修饰部分)
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			             WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/*SharedPreferences preference_dialog =getSharedPreferences("NotShown", 0);
		editor = preference_dialog.edit();
		editor.remove("isShown");
		editor.commit();*/
		wifiDialog=new WifiDialog(this);
		SharedPreferences preference = getSharedPreferences("Equipment", 0);
		editor = preference.edit();
	
		SharedPreferences skin_preferencefinal = getSharedPreferences("Skin", 0);
		String  kon =skin_preferencefinal.getString("skin", "skins");
		if(kon.equals("skin1")){
			ThemeLogic.themeType=1;
			ThemeLogic.getInstance().notifiyChange();
		}else if(kon.equals("skin2")){
			ThemeLogic.themeType=2;
			ThemeLogic.getInstance().notifiyChange();
		}
		switch(ThemeLogic.themeType){
		case 1:
			setTheme(R.style.mode1);
			break;
		case 2:
			setTheme(R.style.mode2);
		}
		setContentView(R.layout.welcome_activity);
	
//		Toast.makeText(getApplicationContext(),"kkkkkkkkkkkkkkkkkkk", Toast.LENGTH_LONG)//未连接到wifi
//				.show();
		handler = new MyHandler();
		mAPP = (MyAPP) getApplication();
		manager = getSupportFragmentManager();
		EquipmentFragment equipmentFragment = new EquipmentFragment();
		LanguageFragment languageFragment = new LanguageFragment();
		languageFragment.setEquipmentFragment(equipmentFragment);
		SkinFragment skinFragment = new SkinFragment();
		languageFragment.setSkinFragment(skinFragment);
		fragmentList.add(equipmentFragment);
		fragmentList.add(languageFragment);
		fragmentList.add(skinFragment);
		SharedPreferences language_preference = getSharedPreferences("Language", 0);
		String language=language_preference.getString("language", "chinese");
		if(language.equals("chinese")){
			languageChangedId=-11;
			updateLange(Locale.SIMPLIFIED_CHINESE);
		}else if(language.equals("english")){
			updateLange(Locale.ENGLISH);
			languageChangedId=11;
		}
//		SharedPreferences skin_preference = getSharedPreferences("Skin", 0);
//		String skin=skin_preference.getString("skin", "skin1");
//		if(skin.equals("skin1")){
//			skinChangedId=-1;
//		}else if(skin.equals("skin2")){
//			skinChangedId=1;
//		}
		welcome_activity=(RelativeLayout)findViewById(R.id.welcome_activity);
		addListener();
		Template();   
		wifiConfiguretion=new WifiConfiguretion(this);
		wifiConfiguretion.disconnectWifi();
		SharedPreferences default_device_preference = getSharedPreferences("default_device", 0);
		String default_device_SSID=default_device_preference.getString("WifiSSID", "");
		if(wifiConfiguretion.isOpen()&&default_device_SSID!=null){
			/*Toast.makeText(getApplicationContext(),
					("\"" + default_device_SSID + "\""), Toast.LENGTH_SHORT)//未连接到wifi
					.show();*/
			for(int i=0;i<wifiConfiguretion.getConfiguration().size();i++){
				
				if(wifiConfiguretion.getConfiguration().get(i).SSID.equals(("\"" + default_device_SSID + "\""))){
					//wifiConfiguretion.disconnectWifi();
					ifConnectSuccessfully=wifiConfiguretion.connectConfiguration(i);
				}
			}
			
			try{  
	            Thread.sleep(5000);   
	        }  
	        catch (InterruptedException e){  
	            e.printStackTrace();  
	        }   
		}
		refreshLayout();
		ThemeLogic.getInstance().addListener(this);
//		if(skin=="skin1"){
//			ThemeLogic.themeType=1;
//			ThemeLogic.getInstance().notifiyChange();
//		}else if(skin=="skin2"){
//			ThemeLogic.themeType=2;
//			ThemeLogic.getInstance().notifiyChange();
//		}
		
//		manager.beginTransaction().add(setDialog,"setDialog").commit();
	}
	public void refreshLayout(){
		if(isActive)return;
		wifiConfiguretion.flushWifiInfo();
		if(!wifiConfiguretion.isOpen()||!wifiConfiguretion.isWifiConnected()){
			no_Equipment();
		}else {
			if(!ifConnectSuccessfully){//判断连接失败，修改人：刘亦茜
				no_Equipment();
			}else{//判断连接成功，修改人：刘亦茜
				equipmentId=2;
				editor.putInt("Equipment", 2);
				editor.commit();
	        	equipmentValue.setText(wifiConfiguretion.getSSID().
	        			substring(1,wifiConfiguretion.getSSID().length()-1));
	        	//signal_strength.setVisibility(View.VISIBLE);
	        	//signalValue.setText(String.valueOf(wifiConfiguretion.getRssi()+100));
	        	
	        	//signalValue.setText(((Integer)wifiConfiguretion.getRssi()).toString());
	        	obtainWifiInfo();
			}
			
		}
	}
	private void no_Equipment(){
		equipmentId=0;
		editor.putInt("Equipment", 0);
		editor.commit();
		Toast.makeText(getApplicationContext(),
				R.string.Not_Connected, Toast.LENGTH_SHORT)//未连接到wifi
				.show();
		if(languageChangedId==-11){
			equipmentImageView.setBackgroundResource(R.drawable.ico_01_2);
		 }else if(languageChangedId==11){
			 equipmentImageView.setBackgroundResource(R.drawable.ico_01_2_en);
		 }
    	equipmentValue.setText(R.string.NO);
//    	signal_strength.setVisibility(View.GONE);
//    	signalValue.setText("");
	}
	public void obtainWifiInfo(){    	
		if(wifiConfiguretion.getRssi()<=0&&wifiConfiguretion.getRssi()>-25){
    		equipmentImageView.setBackgroundResource(R.drawable.ico_01_1);
    	}else if(wifiConfiguretion.getRssi()<=-25&&wifiConfiguretion.getRssi()>-50){
    		equipmentImageView.setBackgroundResource(R.drawable.ico_01_1_1);
    	}else if(wifiConfiguretion.getRssi()<=-50&&wifiConfiguretion.getRssi()>-75){
    		equipmentImageView.setBackgroundResource(R.drawable.ico_01_1_2);
    	}else if(wifiConfiguretion.getRssi()<=-75&&wifiConfiguretion.getRssi()>=-100){
    		equipmentImageView.setBackgroundResource(R.drawable.ico_01_1_3);
    	}else{
    		equipmentImageView.setBackgroundResource(R.drawable.ico_01_1_4);
    	}
	}
	public void updateLange(Locale locale){    	
		 Resources res = getResources();
		 Configuration config = res.getConfiguration();
		 config.locale = locale;
		 DisplayMetrics dm = res.getDisplayMetrics();
		 res.updateConfiguration(config, dm); 
		 languageChanged();
	}
	public void languageChanged(){
		Button bt=(Button)findViewById(R.id.blank_previous);
		 if(bt.getText().toString().equals("Previous Settings")||bt.getText().toString().equals("前次设置")){
			 bt.setText(R.string.previous_setting);
		 }else{
			 bt.setText(R.string.blank_previous);
		 }
		// ((TextView)findViewById(R.id.signal_strength)).setText(R.string.signal_strength);
		 ((Button)findViewById(R.id.enter)).setText(R.string.welcome_start);
	} 
//	public void onRestart(){
//		
//		 setDialog = new SetDialog(this,fragmentList);
//		 manager.beginTransaction().add(setDialog,"setDialog").commit();
//	}
	/*退出程序*/
	private long exitTime = 0;
	int back=0;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			 if ((System.currentTimeMillis() - exitTime)>2000) {
				 Toast.makeText(getApplicationContext(),
							R.string.exit,
							Toast.LENGTH_SHORT).show();
				
				    exitTime = System.currentTimeMillis();
				    back=0;
			}else{
				
					if(back==0){
				     	back=1;
			    	}else{
						finish();
						System.exit(0);
						back=0;
			    	}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
/*    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
        	 final AlertDialog dlg = new AlertDialog.Builder(this).create();
	         dlg.show();
	          Window window = dlg.getWindow();
	          window.setContentView(R.layout.dialog_normal_layout);  
	          Button ok = (Button) window.findViewById(R.id.positiveButton);
	          ok.setOnClickListener(new View.OnClickListener() {
	              public void onClick(View v) {
	                 finish(); 
	            }
	         });       
	        Button cancel = (Button) window.findViewById(R.id.negativeButton);
	       cancel.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View v) {
	          dlg.cancel();
	                 }
	               }); 
  
        }  
          
        return false;  
          
    }*/
	public void addListener(){
		blankOrPrevious=(Button)findViewById(R.id.blank_previous);
		blankOrPrevious.setOnClickListener(this);
		//signal_strength=(TextView)findViewById(R.id.signal_strength);
		equipmentImageView=(ImageView)findViewById(R.id.equipmentImageView);
		equipmentValue=(TextView)findViewById(R.id.equipment_value);
		//signalValue=(TextView)findViewById(R.id.signal_value);
		equipmentImageView.setOnClickListener(this);
		preferenceSetting=(ImageButton)findViewById(R.id.preference_setting);
		preferenceSetting.setOnClickListener(this);
		findViewById(R.id.enter).setOnClickListener(this);
	}
	@SuppressLint("SdCardPath")
	public void Template(){
		template = (TextView) findViewById(R.id.template_selection);
		template.setOnClickListener(this);
		//templateSelection=(Spinner)findViewById(R.id.template_selection);
		SharedPreferences prefenences = this.getSharedPreferences("displayInfo", 0);
		int dataSaveCatalog=prefenences.getInt("dataSaveCatalog", 0);
		String sdDir = null;
		switch(dataSaveCatalog){
		case 0://读取sd卡
			boolean sdCardExist=Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED );
			if(sdCardExist){
				sdDir=Environment.getExternalStorageDirectory().toString();//获取根目录
			}else{
				Toast.makeText(this, R.string.SD_card_detected, Toast.LENGTH_SHORT).show();
				sdDir="sdcard";
				prefenences.edit().putInt("dataSaveCatalog", 1);
			}
			break;
		case 1://读取手机
			sdDir=Environment.getRootDirectory().toString();
			break;
		}
		sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		File file = new File(sdDir+"/data/Template");
		spinnerList.add("Test Settings");
		spinnerPathList.add(null);
		
		spinnerList.add("Previous Settings");		
		spinnerPathList.add(null);
		
		if(file.exists()){
			String[] fileList=file.list();
			File[] filePathList=file.listFiles();
			if(fileList==null){
				return;
			}
			for(int i=0;i<fileList.length;i++){
				spinnerList.add(file.list()[i]);
				spinnerPathList.add(filePathList[i].getAbsoluteFile());
			}
		}else{
			file.mkdirs();
		}
		//template.setText(spinnerList.get(0));
		//YDZD-1650默认模板设置为前次设置 
		setValues(1);
/*		adapter= new ArrayAdapter<String>(this, R.layout.myspinner, spinnerList);
		adapter.setDropDownViewResource(R.layout.drop_down_item);
		//templateSelection.setAdapter(adapter);
*/		
		mSpinerPopWindow = new SpinerPopWindow(this.getApplicationContext());
		mSpinerPopWindow.refreshData(spinnerList, 0);
		mSpinerPopWindow.setItemListener(this);
	}
	
	/** 根据左上角下拉框选择的item的position进行保存 */
	private void setValues(int position){
		if (position >= 0 && position <= spinnerList.size()){
			String value = spinnerList.get(position);
			template.setText(value);
		}
		if (position == 0){
			blankOrPrevious.setText(R.string.previous_setting);//"前次设置"
			blankOrPrevious.setSelected(false);
		}else if(position == 1){
			blankOrPrevious.setText(R.string.blank_previous);//"空白模板"
			blankOrPrevious.setSelected(true);
		} else {
			String name = spinnerList.get(position);
			String path = spinnerPathList.get(position).getAbsolutePath();
			Log.e("ard", "welaty 选择左上角下拉框item：name=" + name + ",path=" + path);
			CVU_SPUtil.saveTemplateFromWhere(this, "abc");
			CVU_SPUtil.saveTemplate4SaveRecord(this, path);
		}
	}
	
	/** 打开左上角下拉框 */
	private void showSpinWindow(){
		mSpinerPopWindow.setWidth(template.getWidth()+20);
		mSpinerPopWindow.setHeight(dip2px(131));
		mSpinerPopWindow.showAsDropDown(template,0, dip2px(17));//
	}

	/** 左上角下拉框选择事件 */
	@Override
	public void onItemClick(int position) {
		setValues(position);
	}
	public int dip2px(float dpValue){
		final float scale =getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){	
		case R.id.blank_previous://空白模板
			if(blankOrPrevious.isSelected()){
				blankOrPrevious.setText(R.string.previous_setting);//"前次设置"
				template.setText(spinnerList.get(0));
		//		templateSelection.setSelection(0);
				blankOrPrevious.setSelected(false);
			}else{
				blankOrPrevious.setText(R.string.blank_previous);//"空白模板"
		//		templateSelection.setSelection(1);
				template.setText(spinnerList.get(1));
				blankOrPrevious.setSelected(true);
			}
			break;
		case R.id.preference_setting://偏好设置
			mAPP.setHandler(handler);
//			FragmentManager manager = getSupportFragmentManager();
			Fragment isShown=manager.findFragmentByTag("setDialog");
			if(isShown!=null)return;
			setDialog = new SetDialog(this,fragmentList);
			setDialog.show(manager, "setDialog");
//			manager.beginTransaction().add(setDialog,"setDialog").commit();
			break;
		case R.id.equipmentImageView: //设备信息
//			Log.e("ard", "查看wifi列表");
			
			mAPP.setHandler(handler);
			wifiDialog.ShowPreWifiDialog(languageChangedId);
			break;
		case R.id.enter://进入主界面
			 enterMainActivity();
			break;
		case R.id.template_selection:
			showSpinWindow();
			break;
		default:
			break;
		}
	}
	
	public void enterMainActivity(){
		
//		 String equipment_str=equipmentValue.getText().toString();
		 Intent mainIntent = new Intent();
		 mainIntent.setClass(WelcomeActivity.this,MainActivity.class);
		 /*
		  * 设备选择切换
		  */		 
		/* if(equipmentId==1){//本机
			 mainIntent.putExtra("mainIntent", "MobilePhoneEnter");
		 }else if(equipmentId==0||equipment_str.equals("")){
			 mainIntent.putExtra("mainIntent", "NoHardwareEnter");
		 }else{
			 mainIntent.putExtra("mainIntent", "Enter");
		 }*/
		 /*
		  * 语言切换
		  */
		 if(languageChangedId==-11){
			 mainIntent.putExtra("LanguageIntent", "ChineseEnter");
		 }else /*if(languageChangedId==11)*/{
			 mainIntent.putExtra("LanguageIntent", "EnglishEnter");
		 }/*else{
			 mainIntent.putExtra("LanguageIntent", "ChineseEnter");
		 }*/
		 /*
		  * 皮肤切换
		  */
	/*	 if(skinChangedId==-1){
			 mainIntent.putExtra("SkinIntent", "Skin0");
		 }else if(skinChangedId==1){
			 mainIntent.putExtra("SkinIntent", "Skin1");
		 }else{
			 mainIntent.putExtra("SkinIntent", "Skin0");
		 }*/
		 
		// String str = (String) templateSelection.getSelectedItem();
		 String str = (String) template.getText();
		 mainIntent.putExtra("template", str);
		 startActivity(mainIntent);
		 finish();
	}
	
	/**
	 * 欢迎界面的消息处理器
	 * @author mark
	 */
	public final class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			/*
			 * 设备切换
			 */
			case Equipment0:
				editor.putInt("Equipment", 0);
				equipmentId=0;
				break;
			case Equipment1:
				editor.putInt("Equipment", 1);
				equipmentId=1;
				break;
			case Equipment2:
				editor.putInt("Equipment", 2);
				equipmentId=2;
				break;
			/*
			 * 换肤
			 */
			case Skin0:
				skinChangedId=-1;
				break;
			case Skin1:
				skinChangedId=1;
				break;
				/*
				 * 语言切换
				 */
			case Chinese:
				languageChangedId=-11;
				break;
			case English:
				languageChangedId=11;
				break;
			}
			editor.commit();
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onThemeChanged() {
		switch (ThemeLogic.themeType) {

		/** 1閵嗗倸鍘涢弴瀛樻煀娑撳顣�閿涗緤绱掗敓锟� */
		case 1:
			setTheme(R.style.mode1);
			break;
		case 2:
			setTheme(R.style.mode2);
			break;
		}
		TypedArray typedArray = obtainStyledAttributes(R.styleable.myStyle);
		RelativeLayout	top = (RelativeLayout) findViewById(R.id.top);
		TextView	template_text = (TextView) findViewById(R.id.template_selection);
//		template_text.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		template_text.setBackground(typedArray.getDrawable(R.styleable.myStyle_template_selection));
		blankOrPrevious.setBackground(typedArray.getDrawable(R.styleable.myStyle_blank_previous));
//		blankOrPrevious.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		top.setBackground(typedArray.getDrawable(R.styleable.myStyle_top));
		((Button)findViewById(R.id.enter)).setBackground(typedArray.getDrawable(R.styleable.myStyle_enter));
		welcome_activity.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_welcome_activity,Color.YELLOW));
		preferenceSetting.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_preference_setting));
		typedArray.recycle();
		
	}
	/** 
     * 程序是否在前台运行 
     *  
     * @return 
     */  
    public boolean isAppOnForeground() {  
            // Returns a list of application processes that are running on the  
            // device  
               
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);  
            String packageName = getApplicationContext().getPackageName();  

            List<RunningAppProcessInfo> appProcesses = activityManager  
                            .getRunningAppProcesses();  
            if (appProcesses == null)  
                    return false;  

            for (RunningAppProcessInfo appProcess : appProcesses) {  
                    // The name of the process that this object is associated with.  
                    if (appProcess.processName.equals(packageName)  
                                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
                            return true;  
                    }  
            }  

            return false;  
    } 
    @Override
	protected void onResume() {
		super.onResume();
		if (!isActive) {  
            //app 从后台唤醒，进入前台  
            isActive = true; 
            } 
		SharedPreferences language_preference = getSharedPreferences("Language", 0);
		String language=language_preference.getString("language", "chinese");
		if(language.equals("chinese")){
			languageChangedId=-11;
			updateLange(Locale.SIMPLIFIED_CHINESE);
		}else if(language.equals("english")){
			updateLange(Locale.ENGLISH);
			languageChangedId=11;
		}
	}
}
