package bottom.drawer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import left.drawer.MainFragment;
import mode.autorange.AutoRangeDataCollection;
import mode.autorange.RangeLayout;
import mode.drive.DriveModeActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cuiweiyou.dynamiclevelnavigation.util.CVU_FileUtil;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.MainContextView;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import common.CustomTab;
import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;

/**
 * <b>类名</b>: BottomOperate，上划界面控制面板<br/>
 * <b>说明</b>: 联动左侧拉面板通道、右侧拉面板project <br/>
 */
public class BottomOperate {
	private boolean autoRangeRecording;
	private ArrayList<Integer> channels = new ArrayList<Integer>();
	public  ArrayList<List<Integer>> channelRangeLevelList = new ArrayList<List<Integer>>();
	private MainActivity context;
	public boolean isFloat = false;
	public boolean isAutoRangeFloat = false;
	private LayoutInflater inflater;
	public View bt_view1, bt_view2;
	public PopupWindow chanPopupWindow;
	/** 驾驶模式按钮 */
	public ImageButton drivingMode;
	public ImageButton startFloatBtn, replayFloatBtn, autoRange, locking, cricleBtn,armBtn;
	/** 普通模式录音按钮 **/
	public ImageButton startCollection;
	public RangeLayout rangeLayout;
	private MainFragment mainFragment;
	/** 上划面板 录音/回放 **/
	public static DataCollection dataCollection;
	private IAudioTrack iAudioTrack;
	/** 回放按钮 **/
	public ImageButton replayBtn;
	private Boolean isCricle = false;
	private AutoRangeDataCollection autoRangeDataCollection;
	private MyViewPager bt_viewPager;
	private int Equipment_Num;
	public AddChannelViewPager addChannelViewPager;
	///** 录音时开启的通道 */
	
	public BottomOperate(MainActivity context) {
		this.context = context;
		channels.add(1);
		iAudioTrack = new IAudioTrack(context, context.customHandler, channels);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addButtonViewPager();
		
		dataCollection = new DataCollection(context.customHandler, context);
		dataCollection.bottomOperate = this;
		
	}

	public void setMainFragment(MainFragment mainFragment) {
		this.mainFragment = mainFragment;
	}

	public void addChannelViewPager() {
		/*
		 * 加载通道ViewPager
		 */
		VerticalViewPager viewPager = (VerticalViewPager) context.findViewById(R.id.chan_viewpager);
		addChannelViewPager = new AddChannelViewPager(context);
//		isActivated_ChanNum = addChannelViewPager.getIsActivated_ChanNum();
		addChannelViewPager.setMainFragment(mainFragment);
		mainFragment.setAddChannelViewPager(addChannelViewPager);
		addChannelViewPager.addViewPager(viewPager);
	}

	public void addButtonViewPager() {
		// 加载左右切换的viewpager --------采集状态与回放状态的布局切换------------------------
		List<View> bt_views;// 左右滑动的Viewpager的view数组

		bt_viewPager = (MyViewPager) context.findViewById(R.id.bt_viewpager);

		bt_view1 = inflater.inflate(R.layout.start_bt, null);
		bt_view2 = inflater.inflate(R.layout.replay_bt, null);
		bt_views = new ArrayList<View>();
		bt_views.add(bt_view1);
		bt_views.add(bt_view2);
		ButtonPagerAdapter buttonPagerAdapter = new ButtonPagerAdapter(bt_views);
		bt_viewPager.setAdapter(buttonPagerAdapter);
		findViewById();
	}

	public void findViewById() {
		drivingMode = (ImageButton) bt_view1.findViewById(R.id.drivingMode);
		armBtn = (ImageButton) bt_view1.findViewById(R.id.armBtn);
		startFloatBtn = (ImageButton) bt_view1.findViewById(R.id.start_floatBtn);
		autoRange = (ImageButton) bt_view1.findViewById(R.id.autoRange);
		locking = (ImageButton) bt_view1.findViewById(R.id.locking);

		startCollection = (ImageButton) bt_view1.findViewById(R.id.startBtn);
		
		replayFloatBtn = (ImageButton) bt_view2.findViewById(R.id.replay_floatBtn);
		replayBtn = (ImageButton) bt_view2.findViewById(R.id.reStartBtn);
		cricleBtn = (ImageButton) bt_view2.findViewById(R.id.cricleBtn);
	}

	public void LockViewPager() {
		if (autoRange.isSelected()) {
			return;
		}
		if (DataCollection.isRecording || autoRangeRecording) {
			return;
		}
		locking.setSelected(locking.isSelected() ? false : true);
		bt_viewPager.setCanScroll(locking.isSelected() ? false : true);
		if (locking.isSelected()) {
			locking_fChanged();
		} else {
			
			locking_nChanged();
			
		}
	}

	/** 进入驾驶模式 */
	public void drivingMode() {
		if (autoRange.isSelected()) {
			Toast.makeText(context, R.string.exit_range, Toast.LENGTH_SHORT).show();
			return;
		}

		/*
		 * if(DataCollection.isRecording||autoRangeRecording){
		 * Toast.makeText(context.getApplicationContext(),
		 * R.string.Collecting_data, Toast.LENGTH_SHORT)//���������� .show(); return;
		 * }else{
		 */

		Bundle bundle = new Bundle();
		String getLanguageValues = context.getIntent().getStringExtra("LanguageIntent");
		if (getLanguageValues.equals("EnglishEnter")) {
			bundle.putString("languagedrive", "1");
		} else if (getLanguageValues.equals("ChineseEnter")) {
			bundle.putString("languagedrive", "-1");
		}

		if (context.mainFragment.languageNumber == 1) {
			bundle.putString("languageNum", "1");
		} else {
			bundle.putString("languageNum", "-1");
		}

		bundle.putSerializable("isActivated_ChanNum", addChannelViewPager.isActivated_ChanNum);

		Intent driveModeIntent = new Intent(context, DriveModeActivity.class);
		bundle.putString("DM","DM");
		driveModeIntent.putExtras(bundle);
		// TODO  �����
		context.startActivityForResult(driveModeIntent,0);//.startActivity(driveModeIntent);
		// }
	}

	// 16.3.4 崔维友 统一普通模式和驾驶模式的录波器
	/** 获取BottomOperate使用的录波器 */
	public static DataCollection getDataCollection() {
		return dataCollection;
	}

	/** 点击普通模式时的录音按钮 */
	public void startListener() {
		CustomTab ct = ((MainActivity) context).mainCustomTab;
		ArrayList<Fragment> pageList = ct.getFragmentList();
		if(pageList.size()==0)return;
		if(context.findViewById(R.id.channal_spinner)==null){
			return;
		}
		
		// 16.4.28 崔 SD卡存储空间不足提示 -begin
		long usize = CVU_FileUtil.getUsableSDStorageSize();
		if(usize < 1024 * 600){ // 小于600mb给出提示
			Toast.makeText(context, context.getResources().getString(R.string.no_enough_space_but_can), 0).show();
		}
		if(usize < 1024 * 200){ // 小于200mb即不得录制
			Toast.makeText(context, context.getResources().getString(R.string.no_enough_space), 0).show();
			return ;
		}
		// -end
		
		// if(Equipment_Num==0){
		SharedPreferences Equipment_preference = context.getSharedPreferences("Equipment", 0);
		Equipment_Num = Equipment_preference.getInt("Equipment", 0);
		// }
		// SharedPreferences preference = context.getSharedPreferences("hz_5D",
		// 0);
		// int ChannelNum = preference.getInt("ChannelNum", 0);
		if (Equipment_Num == 0) {
			/*
			 * Toast.makeText(context, "Choised a device to get datas,please",
			 * Toast.LENGTH_SHORT).show();
			 */
			return;
		}

		// else if (context.findViewById(R.id.channal_spinner).VISIBLE == View.GONE) { // 16.3.4 ��+��
		//else if (context.findViewById(R.id.channal_spinner).getVisibility() == View.GONE) {
			if (addChannelViewPager.isActivated_ChanNum.size() == 0) {//�����ͨ��������ж�
				Toast.makeText(context, R.string.Choose_channel, Toast.LENGTH_SHORT).show();
				return;
			}
		//}

		if (!startCollection.isSelected()) {
			Log.e("ard", "record is running. 开始录音了");
			resetDrawMapXChange();

			//判断fftview是否已经创建，如果创建设置重叠率、频率分辨率和加窗给算法，传递平均给
//			for (int i = 0; i < context.getViewList().size(); i ++) {
//				if(context.getViewList().get(i).getClass().getName().equals("draw.map.FFT")) {
//					FftFragment fftFragment = (FftFragment)context.getSupportFragmentManager().findFragmentByTag("fft");
//					AcquisitionFragment acquiFragment = (AcquisitionFragment)context.getSupportFragmentManager().findFragmentByTag("acqui");
//					FFT fft = (FFT)context.getViewList().get(i);
//					fft.setMean(fftFragment.getAveraging());
//					Arith_FFT arith_FFT = fft.getArith_FFT();
//					arith_FFT.GetWindowType(fftFragment.getWindowPosition());
//					arith_FFT.GetWinLen(acquiFragment.getAcquiFreq() / fftFragment.getFreqRes());
//					arith_FFT.GetWinShift(acquiFragment.getAcquiFreq() / fftFragment.getFreqRes() * (1 - fftFragment.getOverlap() / 100));
//				}
//			}

			if (!autoRange.isSelected()) {
				
				stopChanged();
				
				if (Equipment_Num == 2) {
					dataCollection.Start(0);// Ӳ��ģʽ
				} else if (Equipment_Num == 1) {
					dataCollection.Start(1);// �ֻ�ģʽ
				}
				if (context.findViewById(R.id.channal_spinner).getVisibility() == View.GONE) {
					if (AddChannelViewPager.channelCount.size() == 0) {
						Toast.makeText(context, R.string.startMessage, Toast.LENGTH_SHORT).show();
					}
				}
//				context.setReplayPath(dataCollection.getAudioFile().getAbsolutePath());
				
			} else {
				
				//if (autoRangeDataCollection == null) {
					autoRangeDataCollection = new AutoRangeDataCollection(context);
				//}
				autoRangeDataCollection.setRangeLayout(rangeLayout);
				autoRangeDataCollection.setHandler(rangeLayout.getViewPageHandler());
				rangeLayout.setAutoRangeDataCollection(autoRangeDataCollection);
				reStart_autoRange_StopChanged(startCollection);
				
				autoRangeDataCollection.setChannelList(addChannelViewPager.getActivated_ChanNumList());
				autoRangeDataCollection.start();
				autoRangeRecording = true;
			}
			
			locking.setSelected(false);
			locking_fChanged();
			
			bt_viewPager.setCanScroll(false);
		} else { /////////////////////////////////////////////////////////////// recording is stoped 停
			Log.e("ard", "recording is stoped,，停止录音了");
			if (!autoRange.isSelected()) {
				startChanged();
				dataCollection.Stop();
				
			} else {
				reStart_autoRangeStart_Changed(startCollection);
				autoRangeDataCollection.stop();
				autoRangeRecording = false;
			}
			locking.setSelected(true);
			locking_nChanged();
			bt_viewPager.setCanScroll(true);
			
		}

		addChannelViewPager.setStopCollection(startCollection, dataCollection);
		/*
		 * YDZD-1717����ɼ�ʱ��ֹ�������Ҳ����˵� 
		 */
		context.isRecordingSetDrawerHandle();
	} // end startListener()

	/**
	 * <b>����</b>: resetDrawMapXChange<br/>
	 * <b>˵��</b>: z����X��</br>
	 * @author zj
	 */
	private void resetDrawMapXChange() {
		// TODO Auto-generated method stub
		List<View> viewList = context.getViewList();
		Iterator<View> it = viewList.iterator();
		while(it.hasNext()){
			ScaleView view = (ScaleView) it.next();
			view.resetXChange();
		}
	}

	public void cricleBtnListener() {
		// if(!replayBtn.isSelected())return;
		cricleBtn.setSelected(cricleBtn.isSelected() ? false : true);
		isCricle = cricleBtn.isSelected() ? true : false;
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		if (!isCricle) {
			cricleBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_biutton_xunhuan_n));
			
		} else {
			cricleBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_biutton_xunhuan_f));
			
		}
		typedArray.recycle();
	}

	/** 回放 **/
	public void replayListener() {

		/*
		 * if (startCollection.isSelected()) { Toast.makeText(context,
		 * "����ֹͣ�ɼ�...", Toast.LENGTH_LONG).show(); return; }
		 */
		if (context.getReplayPath() == null) {
			Toast.makeText(context, R.string.Choose_signal, Toast.LENGTH_LONG).show();
			return;
		}
		if (!replayBtn.isSelected()) {
			startAudioTrack();
		} else {
			cricleBtn.setSelected(false);
			isCricle = cricleBtn.isSelected() ? true : false;
			TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
			cricleBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_biutton_xunhuan_n));
			typedArray.recycle();
			stopAudioTrack();
		}
	}

	public void startAudioTrack() {
		
		bt_viewPager.setCanScroll(false);
		context.setPlayBackState(true);
		reStart_autoRange_StopChanged(replayBtn);
		// if(Equipment_Num==0){
		SharedPreferences Equipment_preference = context.getSharedPreferences("Equipment", 0);
		Equipment_Num = Equipment_preference.getInt("Equipment", 0);
		// }
		if (Equipment_Num == 2) {
			iAudioTrack.start(0);// Ӳ��ģʽ
		} else if (Equipment_Num == 1) {
			iAudioTrack.start(1);// ����ģʽ
		}

		bt_viewPager.setCanScroll(false);
	}

	public void stopAudioTrack() {
		if (isCricle) {
			if (Equipment_Num == 2) {
				iAudioTrack.start(0);// Ӳ��ģʽ
			} else if (Equipment_Num == 1) {
				iAudioTrack.start(1);// ����ģʽ
			}
		} else {
			context.setPlayBackState(false);
			reStart_autoRangeStart_Changed(replayBtn);
			iAudioTrack.stop();
			bt_viewPager.setCanScroll(true);
		}

		bt_viewPager.setCanScroll(true);
	}
	public void armButtonListener(){
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		if (!armBtn.isSelected()) {
			armBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_unarm_selector));
			armBtn.setSelected(true);
		} else {
			armBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_arm_selector));
	        armBtn.setSelected(false);
		}
	}
	public void floatButtonListener() {
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		if (!startFloatBtn.isSelected()) {
			isFloat = true;
	        startFloatBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_start_float_f));
	        replayFloatBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_replay_float_f));
			startFloatBtn.setSelected(true);
			replayFloatBtn.setSelected(true);
		} else {
			isFloat = false;
			startFloatBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_start_float_n));
	        replayFloatBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_replay_float_n));
			startFloatBtn.setSelected(false);
			replayFloatBtn.setSelected(false);
		}
	}

	// ����
	public void autoRange() {
		if(DataCollection.isRecording){
			Toast.makeText(context.getApplicationContext(), R.string.Collecting_data, Toast.LENGTH_SHORT).show();
			return;
		}
		twoRangeLevelListValues();
		if (!autoRange.isSelected()) {
			stopFloat();
			rangeLayout = new RangeLayout(context);
			context.getFragmentManager().beginTransaction().add(R.id.mainContent, rangeLayout, "rangeLayoutttttt").commit();
			rangeLayout.setChannelRangeLevelList(channelRangeLevelList);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
			params.width = -1;
			params.height = dip2px(105);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			((MainActivity)context).bottomDrawer.setLayoutParams(params);
			params = new RelativeLayout.LayoutParams(-1, -1);
			params.width = -1;
			params.height = 0;
			((MainActivity)context).bottomHandle.setLayoutParams(params);
			isAutoRangeFloat = true;
			autoRange.setSelected(true);
			bt_viewPager.setCanScroll(false);
			locking.setSelected(true);
			startFloatBtn.setSelected(true);
			autoRange.setVisibility(View.GONE);
			startFloatBtn.setVisibility(View.GONE);
			locking.setVisibility(View.GONE);
			drivingMode.setVisibility(View.GONE);
			armBtn.setVisibility(View.GONE);
			reStart_autoRangeStart_Changed(startCollection);
		} else {
			rangeLayoutReturn();
		}
	}
	public void rangeLayoutReturn(){
		if (autoRangeRecording) {
			Toast.makeText(context.getApplicationContext(), R.string.RangeRecording, Toast.LENGTH_SHORT).show();
			return;
		}
		twoRangeLevelListValues();
		startFloat();
		context.getFragmentManager().beginTransaction().hide(rangeLayout).commit();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		MainContextView mainContextView=(MainContextView)(((MainActivity)context).mainCustomTab).mainContextViewList.get(0);
		params.height=((MainActivity)context).screen_height-(((MainActivity)context).mainCustomTab).TabUpLayout.getHeight()-mainContextView.algorithm_spinner_layout.getHeight();
		((MainActivity)context).bottomDrawer.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams(-1, -1);
		params.width = -1;
		params.height = (((MainActivity)context).screen_height-(((MainActivity)context).mainCustomTab).TabUpLayout.getHeight()-mainContextView.algorithm_spinner_layout.getHeight()) - dip2px(105);
		//params.height = ((MainActivity)context).screen_height - dip2px(105);
		((MainActivity)context).bottomHandle.setLayoutParams(params);
		isAutoRangeFloat = false;
		autoRange.setSelected(false);
		bt_viewPager.setCanScroll(true);
		locking.setSelected(false);
		startFloatBtn.setSelected(false);
		startChanged();
		autoRange.setVisibility(View.VISIBLE);
		startFloatBtn.setVisibility(View.VISIBLE);
		locking.setVisibility(View.VISIBLE);
		drivingMode.setVisibility(View.VISIBLE);
		armBtn.setVisibility(View.VISIBLE);
	}
	private void twoRangeLevelListValues(){
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
		List<Integer> channelList=addChannelViewPager.isActivated_ChanNum;
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
		channelRangeLevelList.clear();
		channelRangeLevelList.add(levelList_2);
	}
	public int dip2px(float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private void stopFloat() {
		/*
		 * ����ʾ
		 */
		if (mainFragment.isShowFloatWindow()) {
			// context.stopService(new Intent(context, FloatingService.class));
			mainFragment.unBind();
		}
	}

	private void startFloat() {
		/*
		 * ����ʾ
		 */
		if (mainFragment.isShowFloatWindow()) {
			// context.startService(new Intent(context, FloatingService.class));
			mainFragment.show();
		}/*
		 * else{ context.stopService(mainFragment.getFloatIntent()); }
		 */
	}

	/** 为DataCollection的弹出框设置消息处理器 */
	public void setDataCollectionHandler() {
		if (null != dataCollection)
			dataCollection.setHandler(context.customHandler);
	}

	/** 为DataCollection的弹出框设置Context */
	public void setDataCollectionContext() {
		if (null != dataCollection)
			dataCollection.setContext(context);
	}
	
	/**
	 * <b>功能</b>: setStopRecordState，停止MainAcitivy的上划界面的录制<br/>
	 */
	public void setStopRecordState(){
		startChanged();

		if (!autoRange.isSelected()) {
//			dataCollection.Stop();
		} else {
			autoRangeDataCollection.stop();
			autoRangeRecording = false;
		}

		locking.setSelected(true);
		locking_nChanged();
		bt_viewPager.setCanScroll(true);
	}
	
	/**
	 * <b>功能</b>: setProcessRecordState，MainAcitivy的上划界面的录制正在进行状态<br/>
	 */
	public void setProcessRecordState(){
		startChanged();
		
		if (!autoRange.isSelected()) {
			dataCollection.Stop();
			
		} else {
			autoRangeDataCollection.stop();
			autoRangeRecording = false;
		}
		
		locking.setSelected(true);
		locking_nChanged();
		bt_viewPager.setCanScroll(true);
	}
	public void languageChanged(){
		if(rangeLayout!=null){
			rangeLayout.languageChanged();
		}
	}
	public int getEquipment_Num(){
		SharedPreferences Equipment_preference = context.getSharedPreferences("Equipment", 0);
		Equipment_Num = Equipment_preference.getInt("Equipment", 0);
		return Equipment_Num;
	}
	
	public void locking_nChanged(){
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		locking.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_locking_n));
		typedArray.recycle();
	}
	public void locking_fChanged(){
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		locking.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_locking_f));
		typedArray.recycle();
	}
	public void startChanged(){
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		startCollection.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_start_selector));
		startCollection.setSelected(false);
		typedArray.recycle();
	}
	public void stopChanged(){
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		startCollection.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_stop_selector));
		startCollection.setSelected(true);
		typedArray.recycle();
	}
	public void reStart_autoRangeStart_Changed(ImageButton imageButton){
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		imageButton.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_restart_selector));
		imageButton.setSelected(false);
		typedArray.recycle();
	}
	public void reStart_autoRange_StopChanged(ImageButton imageButton){
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.myStyle);
		imageButton.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_restartstop_selector));
		imageButton.setSelected(true);
		typedArray.recycle();
	}
	public void skinChanged(TypedArray typedArray){
		//下侧啦
		drivingMode.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_drive_selector));
		if (armBtn.isSelected()) {
			armBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_unarm_selector));
		} else {
			armBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_arm_selector));
		}
		startFloatBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_start_float_selector));// 閿熺即纭锋嫹鏃堕敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閽敓渚ョ》鎷烽敓鏂ゆ嫹閿熼摪纭锋嫹
		replayFloatBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_replay_float_selector));// 閿熸埅鍑ゆ嫹鏃堕敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閽敓渚ョ》鎷烽敓鏂ゆ嫹閿熼摪纭锋嫹
		autoRange.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_autorange_selector));
		locking.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_locking_selector));
		if(locking.isSelected()){
			locking.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_locking_f));
		}else{
			locking.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_locking_n));
		}
		startCollection.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_start_selector));
		if(replayBtn.isSelected()){
			replayBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_restartstop_selector));
		}else{
			replayBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_bt_restart_selector));
		}
		if(cricleBtn.isSelected()){
			cricleBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_biutton_xunhuan_f));
		}else{
			cricleBtn.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_biutton_xunhuan_n));
		}
		LinearLayout bottomView = (LinearLayout) context.findViewById(R.id.bottomView);
		bottomView.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_bottomView,Color.YELLOW));
		addChannelViewPager.skinChanged();
	}
}
