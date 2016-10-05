package com.example.mobileacquisition;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;


import com.algorithm.helper.AIHelper;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_ScreenUtil;
import com.cuiweiyou.fragment.CVU_RightDrawerFragment;

import preference.welcome.MyAPP;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import bottom.drawer.BottomOperate;
import bottom.drawer.VerticalViewPager;
import common.CustomTab;
import common.DataCollection;
import common.PullBookParser;
import common.ScaleView;
import floating.window.FloatingService;
import left.drawer.AnalogFragment;
import left.drawer.ChannelSettingFragment;
import left.drawer.LeftExplandableListView;
import left.drawer.MainFragment;
import left.drawer.TriggerFragment;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements OnClickListener{
	//ThemeLogicright
	public BottomOperate bottomOperate = null;
	public MainFragment mainFragment = new MainFragment();
	public ChannelSettingFragment channelSettingFragment;
	public AnalogFragment analogFragment = null;
	public CVU_RightDrawerFragment rightDrawerFragment;
	private ArrayList<String> tabs = null;
	private LeftExplandableListView leftListView;
	public ArrayList<Fragment> fragmentList = null;
	public CustomTab mainCustomTab;
	public TriggerFragment triggerFragment = null;
	private FragmentManager manager;
	public  RelativeLayout bottomHandle, rightHandle, leftHandle;
	public  int screen_width, screen_height;
	public  RelativeLayout rightContent;
	// private TextView ggg,ggg1;
	public ArrayList<View> viewList = new ArrayList<View>();
	public  SlidingDrawer leftDrawer, rightDrawer, bottomDrawer;
	private Boolean isPlayBackState = false;
	// SkinChanged skinChanged;
	/** MainActivity里的消息处理器 */
	public CustomHandler customHandler = new CustomHandler(this);
	private String replayPath;
	private int leftDrawerOpen = -1;
	private int bottomDrawerOpen = -1;
	private int rightDrawerOpen = -1;
	public int resultCode=-1;
	private boolean isActive=false;
	
//	private AIHelper aiHelper = AIHelper.getInstance();
	
	static class CustomHandler extends Handler {

		WeakReference<MainActivity> mActivity;
		Context ctx;

		public CustomHandler(MainActivity activity) {
			mActivity = new WeakReference<MainActivity>(activity);
			ctx = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity theActivity = mActivity.get();
			switch (msg.what) {
			case 1:
				int[] buffer = (int[]) msg.obj;
				Log.v("bug11", ""+buffer.length);
				AIHelper.getInstance().startCaculate(buffer);
//				for (View view : theActivity.viewList) {
//					
//					if(view.getClass().getSimpleName().contains("AI")){
//					
////						((AI)view).startCaculate();
//
//					}else{
//						((ScaleView)view).startCaculate();
//
//					}
//					
//			
//				}
				break;
			case 0:
				theActivity.bottomOperate.stopAudioTrack();
			}
			super.handleMessage(msg);
			
			// 驾驶模式点击“停止”按钮，发来消息，改变普通模式界面显示状态
			if(null != msg.obj && "record_stop".equals(msg.obj.toString())){
				theActivity.bottomOperate.setStopRecordState();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags( WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
		        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
		        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screen_width = wm.getDefaultDisplay().getWidth();
		screen_height = wm.getDefaultDisplay().getHeight();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		switch(ThemeLogic.themeType){
		case 1:
			setTheme(R.style.mode1);
			break;
		case 2:
			setTheme(R.style.mode2);
		}
		setContentView(R.layout.activity_main);
		manager = getSupportFragmentManager();
		setTemplate();

		manager.beginTransaction().add(R.id.leftContent, mainFragment, "main").commit();
		
		leftDrawer = (SlidingDrawer) findViewById(R.id.leftDrawer);
		leftHandle = (RelativeLayout) findViewById(R.id.leftHandle);
		rightDrawer = (SlidingDrawer) findViewById(R.id.rightDrawer);
		rightHandle = (RelativeLayout) findViewById(R.id.rightHandle);
		rightContent = (RelativeLayout) findViewById(R.id.rightContent);
		bottomDrawer = (SlidingDrawer) findViewById(R.id.bottomDrawer);
		bottomHandle = (RelativeLayout) findViewById(R.id.bottomHandle);
		rightDrawerFragment = new CVU_RightDrawerFragment();
		manager.beginTransaction()
				.add(R.id.rightContent, rightDrawerFragment, "rightDraw")
				.commit();
		
		mainCustomTab = (CustomTab) findViewById(R.id.mainCustomTab);
		addMainCustomTab();
		setRightDrawerWidth();
		SetBottomClickListener();
		openOneDrawer();	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		int type=this.getResources().getConfiguration().orientation;
		if(type == Configuration.ORIENTATION_PORTRAIT){
			
		}else{
			
		}
	}

	@Override
	public View onCreateView(String name, @NonNull Context context,
			@NonNull AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}

	@Override
	public void onAttachedToWindow() {
		
		FragmentTransaction transaction = manager.beginTransaction();
		leftListView = mainFragment.getLeftExplandableListView();
		channelSettingFragment = leftListView.getChannelSettingFragment();
		analogFragment = channelSettingFragment.getAnalog();
		transaction.hide(channelSettingFragment)
		.hide(leftListView.getSignalFragment())
		.hide(leftListView.getSplFragment())
		.hide(leftListView.getOctFragment())
		.hide(leftListView.getFftFragment())
		.hide(leftListView.getAiFragment())
		.hide(leftListView.getRpmFragment())
		.hide(leftListView.getColormapFragment())
		.hide(leftListView.getDisplayFragment())
		.hide(leftListView.getAcquisitionFragment())
		.hide(leftListView.getPreTriggerFragment())
		.hide(leftListView.getTriggerFragment())
		.hide(leftListView.getOrderFragment())
		.hide(manager.findFragmentByTag("preleft")).commit();

		super.onAttachedToWindow();
	}

//	public void SaveTo(String oldPath, String newPath) {
//		try {   
//           int byteread = 0;   
//           File oldfile = new File(oldPath);
//           File newfile = new File(newPath);
//           if(!newfile.getParentFile().exists()) {
//        	   newfile.getParentFile().mkdirs();
//           }
//           
//           
//           
//           if (oldfile.exists()) { //闂佽法鍠嶉懠搴ｆ兜闁垮顏堕梺璺ㄥ枑閺嬪骞忛悜鑺ユ櫢闁哄倶鍊栫�褰掑籍閿燂拷  
//   			
//
//               InputStream inStream = new FileInputStream(oldPath); //闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�鐎氬綊宕㈤悢鍏兼櫢濞撴哎鍎抽妴瀣箯閿燂拷  
//               FileOutputStream fs = new FileOutputStream(newPath);
//               byte[] buffer = new byte[1024];
//               while ( (byteread = inStream.read(buffer)) != -1) {
//                   fs.write(buffer, 0, byteread);   
//               }
//               Log.v("bug11", ""+oldfile.getName());
//               inStream.close();
//               fs.close();
//           }   
//       }   
//       catch (Exception e) {   
////    	   Toast.makeText(getActivity().getApplicationContext(), "闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�鐎氱懓螣閿熺姵鏅搁柡鍌樺�鐎氬綊鏌ㄩ悢鍛婄伄闁归鍏橀弫鎾绘晸閿燂拷,Toast.LENGTH_SHORT).show();  
//           e.printStackTrace(); 
//       }
//	}
	
	private void setTemplate() {
		String setstr = getIntent().getStringExtra("template");
		Log.e("ard", "开始界面传入:" + setstr);
		 SharedPreferences preference = this.getSharedPreferences("hz_5D", 0);
		 SharedPreferences.Editor editor = preference.edit();
		if(setstr.equals("Test Settings")){
			
			 editor.clear();
			 editor.putString("Signal", "open");
			 editor.commit();
		}
		else if(setstr.equals("Previous Settings")){
			/*
			 * 首次安装程序，前次设置为空时，默认打开Signal
			 */
			 String str=preference.getString("Signal", "");
			 if(str.equals("")){
				 editor.putString("Signal", "open");
				 editor.commit();
			 }
		}
		else{
			
			 String oldPath = "/sdcard/data/Template/" + setstr; // “开始界面”所设定的xxx.xml模板文件，作为 old
//			 String newPath = getApplicationContext().getFilesDir().getAbsolutePath();
//			 int num = newPath.lastIndexOf("/");
//			 String str = newPath.substring(0,num+1);
//			 newPath = str+ "shared_prefs/hz_5D.xml";
//			 SaveTo(oldPath,newPath);
			 PullBookParser templateParser = new PullBookParser(oldPath);
			 SharedPreferences preferences = getSharedPreferences("hz_5D",0);
			 reLoadPreferences(templateParser,preferences);
			 CVU_SPUtil.saveTemplateFromWhere(this, "wel");     // 模板来源4
			 CVU_SPUtil.saveTemplate4SaveRecord(this, oldPath);
		}
	}
	
	private void reLoadPreferences(PullBookParser templateParser,SharedPreferences preferences) {
		SharedPreferences.Editor editor = preferences.edit();
		Map<String,String> map = templateParser.getValueMap();
		Set<String> keySet = map.keySet();
		
		for(String key: keySet){
			String value = map.get(key);
			editor.putString(key, value);
		}
		editor.commit();
	}

	@SuppressWarnings("deprecation")
	private void openOneDrawer() {

		leftDrawer
				.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

					@Override
					public void onDrawerOpened() {
						leftDrawerOpen = 0;
						if (bottomDrawerOpen == 0) {
							bottomDrawer.close();
						}
						if (rightDrawerOpen == 0) {
							rightDrawer.close();
						}
						/*
						 * 璁剧疆宸︿晶鎷夎彍鍗旽andle鐨勫搴�
						 */
						if(bottomOperate.isFloat||bottomOperate.isAutoRangeFloat)return;
						leftDrawerOpen();
					}
				});
		leftDrawer
				.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
					@Override
					public void onDrawerClosed() {
						leftDrawerOpen = -1;
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								-1, -1);
						params.width = dip2px(270);
						params.height = -1;
						params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						leftDrawer.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = dip2px(15);
						params.height = -1;
						leftHandle.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = -1;
						params.height = dip2px(120);
						params.leftMargin = 0;//dip2px(30);
						params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						bottomDrawer.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = -1;
						params.height = dip2px(15);
						bottomHandle.setLayoutParams(params);
					}
				});
			rightDrawer
				.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

					@Override
					public void onDrawerOpened() {
						rightDrawerOpen = 0;
						if (bottomDrawerOpen == 0) {
							bottomDrawer.close();
						}
						if (leftDrawerOpen == 0) {
							leftDrawer.close();
						}
						if(bottomOperate.isFloat||bottomOperate.isAutoRangeFloat)return;
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								-1, -1);
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						rightDrawer.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = screen_width - dip2px(235);// rightDrawer.getWidth()+dip2px(150);
						params.height = -1;
						rightHandle.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = dip2px(235);
						params.height = -1;
						rightContent.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = -1;
						params.height = dip2px(120);
						params.rightMargin = dip2px(230);
						params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						bottomDrawer.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = -1;
						params.height = dip2px(15);
						bottomHandle.setLayoutParams(params);

					}
				});
		rightDrawer
				.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
					@Override
					public void onDrawerClosed() {
						rightDrawerOpen = -1;
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								-1, -1);
						params.width = dip2px(250);
						params.height = -1;
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						rightDrawer.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = dip2px(15);
						params.height = -1;
						rightHandle.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = -1;
						params.height = dip2px(120);
						params.rightMargin = 0;//dip2px(30)
						params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						bottomDrawer.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = -1;
						params.height = dip2px(15);
						bottomHandle.setLayoutParams(params);
					}
				});
		bottomDrawer
				.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

					@Override
					public void onDrawerOpened() {
						bottomDrawerOpen = 0;
						if (rightDrawerOpen == 0) {
							rightDrawer.close();
						}
						if (leftDrawerOpen == 0) {
							leftDrawer.close();
						}
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								-1, -1);
						params.rightMargin = 0;
						params.leftMargin = 0;
						MainContextView mainContextView=(MainContextView)mainCustomTab.mainContextViewList.get(0);
						params.height=screen_height-mainCustomTab.TabUpLayout.getHeight()-mainContextView.algorithm_spinner_layout.getHeight();
						params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						bottomDrawer.setLayoutParams(params);
						params = new RelativeLayout.LayoutParams(-1, -1);
						params.width = -1;
						params.height = (screen_height-mainCustomTab.TabUpLayout.getHeight()-mainContextView.algorithm_spinner_layout.getHeight()) - dip2px(105);
						params.rightMargin = 0;
						params.leftMargin = 0;
						bottomHandle.setLayoutParams(params);
					}
				});
		bottomDrawer
				.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
					@Override
					public void onDrawerClosed() {
						bottomDrawerOpen = -1;
						if (bottomOperate.isFloat||bottomOperate.isAutoRangeFloat) {
							bottomDrawer.open();
						} else {
							RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
							params.width = -1;
							params.height = dip2px(120);
							params.rightMargin = 0;
							params.leftMargin = 0;
							params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
							bottomDrawer.setLayoutParams(params);
							params = new RelativeLayout.LayoutParams(-1, -1);
							params.width = -1;
							params.height = dip2px(15);
							params.rightMargin = 0;
							params.leftMargin = 0;
							bottomHandle.setLayoutParams(params);

						}
						/*
						 * YDZD-1766
						 */
						isRecordingSetDrawerHandle();
					}
				});
	}

	public void leftDrawerOpen(){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				-1, -1);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		leftDrawer.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width = screen_width - dip2px(255);// MainActivity.leftDrawer.getWidth()
		params.height = -1;
		leftHandle.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width = -1;
		params.height = dip2px(120);
		params.leftMargin = dip2px(230);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		bottomDrawer.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width = -1;
		params.height = dip2px(15);
		bottomHandle.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width = dip2px(250);
		params.height = -1;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rightDrawer.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width =dip2px(15);
		params.height = -1;
		rightHandle.setLayoutParams(params);
	}
	
	public void isRecordingSetDrawerHandle(){
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
		if(!rightDrawer.isOpened()||!leftDrawer.isOpened()){
			if(DataCollection.isRecording&&!bottomOperate.autoRange.isSelected()){
				params.width = dip2px(0);
			}else{
				params.width = dip2px(15);
			}
			params.height = -1;
			leftHandle.setLayoutParams(params);
			rightHandle.setLayoutParams(params);
		}
	}
	
	public int dip2px(float dpValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public void addMainCustomTab() {

		tabs = new ArrayList<String>();
		
//		SharedPreferences temppf = getSharedPreferences("Hz_5D", 0);
		
		
		/*
		 * 閿熸枻鎷疯繋閿熸枻鎷烽敓鏂ゆ嫹鍋忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熷彨浼欐嫹
		 */

		SharedPreferences prefenences = getSharedPreferences("hz_5D", 0);
		String signal_str=prefenences.getString("Signal", "open");
		int pagecount = Integer.parseInt(prefenences.getString("PageCount","1"));
		List<String> str = null;
		String getLanguageValues = getIntent().getStringExtra("LanguageIntent");
		fragmentList = new ArrayList<Fragment>();	
		
		if (pagecount < 1) pagecount = 1;
		if (pagecount > 5) pagecount = 5;
		for (int i = 0; i < pagecount; i++) {
			if (getLanguageValues.equals("EnglishEnter")) {  
				updateLange(Locale.ENGLISH);
				mainFragment.languageNumber = 1;
			} else if (getLanguageValues.equals("ChineseEnter")) {
				updateLange(Locale.SIMPLIFIED_CHINESE);
			}
			tabs.add(getResources().getString(R.string.Main_Window));
			str = getPreDisplayStringList(prefenences.getString(
					"page" + String.valueOf(i), null));
			if (str != null) {
				OneTabFragment oneTabFragment = OneTabFragment.newInstance(
						(ArrayList<String>) str, mainCustomTab.viewPager);
				fragmentList.add(oneTabFragment);

			}else{
				fragmentList = new ArrayList<Fragment>();
				ArrayList<String> textList = new ArrayList<String>();
				if(signal_str.equals("open")){
					textList.add("Signal");
				}else{
					textList.add("ChannelWatch");
				}
				OneTabFragment oneTabFragment = OneTabFragment.newInstance(
						textList, mainCustomTab.viewPager);
				fragmentList.add(oneTabFragment);
			}
		}
		
		mainCustomTab.setPage(pagecount);
		mainCustomTab.setTabs(tabs);
		mainCustomTab.setAddButtonVisible(fragmentList);
		mainCustomTab.setup(manager, fragmentList);
		
	}

	// 閿熸枻鎷烽敓鑺ュ偍閿熸枻鎷烽敓琛楄妭纰夋嫹閿熻鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯浆閿熸枻鎷蜂负閿熻鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熻緝鈽呮嫹
	@SuppressWarnings("unchecked")
	private List<String> getPreDisplayStringList(String oldString) {
		if (oldString == null || oldString.equals("")) {
			return null;
		}
		byte[] bytes = Base64.decode(oldString, Base64.DEFAULT);
		List<String> displaylist = null;
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				bytes);
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
		} catch (StreamCorruptedException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
			
		}
		try {
			displaylist = (ArrayList<String>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return displaylist;
	}

	private void SetBottomClickListener() {
		/*----------------bottom drawer-----------------*/
		bottomOperate = new BottomOperate(this);
		mainFragment.setBottomOperate(bottomOperate);
		bottomOperate.setMainFragment(mainFragment);
		bottomOperate.addChannelViewPager();
		bottomOperate.drivingMode.setOnClickListener(this);
		bottomOperate.startFloatBtn.setOnClickListener(this);
		bottomOperate.replayFloatBtn.setOnClickListener(this);
		bottomOperate.autoRange.setOnClickListener(this);
		bottomOperate.locking.setOnClickListener(this);
		bottomOperate.startCollection.setOnClickListener(this);
		bottomOperate.replayBtn.setOnClickListener(this);
		bottomOperate.cricleBtn.setOnClickListener(this);
		bottomOperate.armBtn.setOnClickListener(this);
	}

	/* 閿熷壙绛规嫹閿熸枻鎷烽敓鏂ゆ嫹 */
	private long exitTime = 0;
	int back = 0;

	// 閿熷壙绛规嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(),
						R.string.exit,
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
				back = 0;
			} else {

				if (back == 0) {
					back = 1;
				} else {
					stopService(new Intent(this, FloatingService.class));
					mainFragment.unBind();
					
					CVU_SPUtil.deleteCurrentProject(this);
					
					finish();
					System.exit(0);
					back = 0;
					/*
					 * 驾驶模式图例初始显示位置
					 */
					SharedPreferences preference =getSharedPreferences("hz_5D", 0);
					SharedPreferences.Editor editor = preference.edit();
					editor.putInt("drive_legend_left", -1);
					editor.putInt("drive_legend_top", -1);
					editor.commit();
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK ) { final AlertDialog dlg = new
	 * AlertDialog.Builder(this).create(); dlg.show(); Window window =
	 * dlg.getWindow(); window.setContentView(R.layout.dialog_normal_layout);
	 * Button ok = (Button) window.findViewById(R.id.positiveButton);
	 * ok.setOnClickListener(new View.OnClickListener() { public void
	 * onClick(View v) { finish(); } }); Button cancel = (Button)
	 * window.findViewById(R.id.negativeButton); cancel.setOnClickListener(new
	 * View.OnClickListener() { public void onClick(View v) { dlg.cancel(); }
	 * });
	 * 
	 * }
	 * 
	 * return false;
	 * 
	 * }
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startBtn:  // 录制按钮
			MyAPP.setCvu_handler(customHandler);
			bottomOperate.startListener();
			break;
		case R.id.reStartBtn: // 回放按钮
			bottomOperate.replayListener();
			break;
		case R.id.drivingMode:// 切换到驾驶模式
			bottomOperate.drivingMode();
			break;
		case R.id.start_floatBtn:// 閿熺即纭锋嫹鐘舵�鏃堕敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽挳
			// 閿熺即纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹
			bottomOperate.floatButtonListener();
			break;
		case R.id.replay_floatBtn:// 閿熸埅鍑ゆ嫹鏃堕敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽挳
			// 閿熸埅鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
			bottomOperate.floatButtonListener();
			break;
		case R.id.autoRange:// 閿熸枻鎷烽敓鏁欐枻鎷烽敓鏂ゆ嫹
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( -1, -1);
			params.width = -1;
			params.height = dip2px(120);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			bottomDrawer.setLayoutParams(params);
			params = new RelativeLayout.LayoutParams(-1, -1);
			params.width = -1;
			params.height = dip2px(15);
			bottomHandle.setLayoutParams(params);
			bottomOperate.autoRange();
			break;
		case R.id.locking:
			bottomOperate.LockViewPager();
			break;
		case R.id.cricleBtn:
			bottomOperate.cricleBtnListener();
			break;
		case R.id.armBtn:
			bottomOperate.armButtonListener();
			break;
		default:
			break;
		}
	}

	/*
	 * 閿熸枻鎷烽敓鏂ゆ嫹閿熷彨浼欐嫹
	 */
	public void updateLange(Locale locale) {
		Resources res = getResources();
		Configuration config = res.getConfiguration();
		config.locale = locale;
		DisplayMetrics dm = res.getDisplayMetrics();
		res.updateConfiguration(config, dm);
	}

	public void addViewList(View v) {
		this.viewList.add(v);
	}

	public void removeViewList(View v) {
		this.viewList.remove(v);
	}

	public ArrayList<View> getViewList() {
		return this.viewList;
	}

	public Boolean getPlayBackState() {
		return isPlayBackState;
	}

	public void setPlayBackState(Boolean isPlayBackState) {
		this.isPlayBackState = isPlayBackState;
	}

	public void setReplayPath(String replayPath) {
		this.replayPath = replayPath;
	}

	public String getReplayPath() {
		return replayPath;
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		mainFragment.show();
		if(mainCustomTab.fragmentList.size()!=0){
		int tabNumber = mainCustomTab.reAddTab(mainCustomTab.fragmentList);
		if (mainFragment.languageNumber == 1) {
			for (int j = 0; j < tabNumber; j++) {
				updateLange(Locale.ENGLISH);
			}
		} else {
			for (int j = 0; j < tabNumber; j++) {
				updateLange(Locale.SIMPLIFIED_CHINESE);
			}
		}
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		if(mainFragment.wifiDialog!=null){
			registerReceiver(mainFragment.wifiDialog.WifiBroadcast, filter);
		}
	}
	public void languageChanged(){
		tabs.clear();
		if(mainCustomTab.fragmentList.size()!=0){
			int tabNumber = mainCustomTab.reAddTab(mainCustomTab.fragmentList);
			for (int j = 0; j < tabNumber; j++) {
				tabs.add(getResources().getString(R.string.Main_Window));
			}
			mainCustomTab.setTabs(tabs);
		}
		bottomOperate.languageChanged();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mainFragment.unBind();
	}
	
	 
	@Override
	protected void onStop() {
		if (!isAppOnForeground()) {  
            //app 进入后台  
               
            //全局变量
			isActive = false;// 记录当前已经进入后台  
		} 
		if(mainFragment.wifiDialog!=null){
			unregisterReceiver(mainFragment.wifiDialog.WifiBroadcast);
		}
		super.onStop();
		mainFragment.unBind();
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
	public void onDestroy() {
		super.onDestroy();
		mainFragment.unBind();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		this.resultCode=resultCode;
	}

	//////////////////////////////// 16.3.9 崔 普通模式与驾驶模式的数据同步 -begin
	@Override
	protected void onResume() {
		super.onResume();
		if(resultCode!=0){
			if (!isActive) {  
	            //app 从后台唤醒，进入前台  
	            isActive = true; } 
			if(isActive)return;
		}
		/*
		 * 区分从驾驶模式返回还是标定返回
		 * ((MainActivity)context).resultCode==0驾驶模式返回主界面
		 * ((MainActivity)context).resultCode==1标定返回主界面
		 */
		if(resultCode==1)return;
		VerticalViewPager viewPager = (VerticalViewPager)findViewById(R.id.chan_viewpager);
		if(null != bottomOperate){
			bottomOperate.setDataCollectionHandler();
			bottomOperate.setDataCollectionContext();
			bottomOperate.addChannelViewPager.addViewPager(viewPager);
			bottomOperate.addChannelViewPager.changeBottomChannelActivitedState();
			if(!BottomOperate.dataCollection.isRecording){
				bottomOperate.setStopRecordState();
			} else {
				bottomOperate.startCollection.setSelected(false);
				bottomOperate.startListener();
			}
		}
	}
	//////////////////////////////// - end
	
	private void setRightDrawerWidth(){
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rightDrawer.getLayoutParams();
		lp.width = CVU_ScreenUtil.getScreenWidth(this) / 8 * 3;
		rightDrawer.setLayoutParams(lp);
	}
}
