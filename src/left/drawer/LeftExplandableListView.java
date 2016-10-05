package left.drawer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import left.drawer.AnalogFragment.ViewHolder;
import mode.calibration.CalibrationActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RelativeLayout;
import bottom.drawer.AddChannelViewPager;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.SpinnerValues;
import com.example.mobileacquisition.ThemeLogic;

import common.DataCollection;
import common.XclSingalTransfer;

import floating.window.FloatingService;
import floating.window.FloatingService.MyBinder;

public class LeftExplandableListView extends ExpandableListView implements
		OnChildClickListener, OnGroupExpandListener, OnGroupClickListener {
	private FragmentActivity context = null;
	private ChannelSettingFragment channelSettingFragment = null;
	private DisplayFragment displayFragment = null;// new DisplayFragment();
	private AcquisitionFragment acquiFragment = null;
	private PreTriggerFragment preTriggerFragment = null;
	private TriggerFragment triggerFragment = null;
	private SignalFragment signalFragment = null;
	private SplFragment splFragment = null;
	private OctFragment octFragment = null;
	private FftFragment fftFragment = null;
	private AiFragment aiFragment = null;
	private RpmFragment rpmFragment = null;
	private ColormapFragment colormapFragment = null;
	private OrderFragment orderFragment = null;
	private LeftExplandAdapter adapter = null;
	private MainFragment mainFragment = null;
	private String getValues;
	private String getLanguageValues;
	private Intent floatIntent;
	private SpinnerValues spinnerValues;
	
	public LeftExplandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (FragmentActivity) context;
		mainFragment = (MainFragment) this.context.getSupportFragmentManager()
				.findFragmentByTag("main");
		this.setGroupIndicator(null);
		this.setVerticalScrollBarEnabled(false);
		spinnerValues = new SpinnerValues(this.context, mainFragment);
		mainFragment.setSpinnerValues(spinnerValues);
		/* ����ѡ��Ļ�ɫ���� */
		ColorDrawable drawable_tranparent = new ColorDrawable(Color.rgb(75,119, 190));
	
		if(ThemeLogic.themeType==1){
			setSelector(R.drawable.bt_listview_selector);
		}else{
		    setSelector(R.drawable.bt_listview_selector1);
		}
		// setDividerHeight(1);
		// this.setChildDivider(null);
		getValues = this.context.getIntent().getStringExtra("mainIntent");
		getLanguageValues = this.context.getIntent().getStringExtra(
				"LanguageIntent");
		adapter = new LeftExplandAdapter(context);
		mainFragment.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		setAdapter(adapter);
		this.setOnChildClickListener(this);
		this.setOnGroupClickListener(this);
		this.setOnGroupExpandListener(this);
		newFragment();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {

		switch (groupPosition) {
		case 0:
			childPositionClick_0(childPosition);
			break;
		case 1:
			childPositionClick_1(childPosition);
			break;
		case 2:
			childPositionClick_2(childPosition);
			break;
		default:
			break;
		}
		adapter.notifyDataSetChanged();
		return false;
	}

	public void childPositionClick_0(int childPosition){
		switch(childPosition){
			case 0:
				channelSettingFragment.getAnalog().setViewHolderVisbleHolder();
				FragmentManager manager = context.getSupportFragmentManager();
				manager.beginTransaction().hide(mainFragment).show(channelSettingFragment).commit();
				
				/*
				 * ��
				 */
				if(floatIntent!=null){
					if(mainFragment.isShowFloatWindow()){
						//context.stopService(floatIntent);
						mainFragment.unBind();
					}
				}

				mainFragment.setLeftHandleWidth();
			break;
		case 1:// ����궨����
			if(DataCollection.isRecording){
				Toast.makeText(context.getApplicationContext(),
						 R.string.Collecting_data, Toast.LENGTH_SHORT)//����������
						.show();
				return;
			}
			if (channelSettingFragment != null) {
				 Map<Integer,ViewHolder> viewholders = channelSettingFragment
						.getViewHolders();
				 SharedPreferences preference = context.getSharedPreferences(
							"hz_5D", 0);
				 if(viewholders==null||viewholders.size()==0){
					 Toast.makeText(context, R.string.Choose_channel, Toast.LENGTH_SHORT).show();
				 }else{
					/* if(preference.getString("FFT", "close").equals("close")){
						 Toast.makeText(context, R.string.OpenFFT, Toast.LENGTH_SHORT).show();
						 return;
					 }*/
					XclSingalTransfer xclSingalTransfer = XclSingalTransfer
							.getInstance();
					Iterator it=viewholders.keySet().iterator();
					while (it.hasNext()) {
						int position_int=(Integer)it.next();
						String position_str=String.valueOf(position_int);
						
						if(xclSingalTransfer.containsKey(position_str)){
							continue;
						}else{
							xclSingalTransfer.putValue(position_str,
									viewholders.get(position_int));
						}
					}	
					int hardType =( (MainActivity)context).bottomOperate.getEquipment_Num();//2��Ӳ����1���ֻ�
					Intent intent= new Intent(context,
							CalibrationActivity.class);
					intent.putExtra("Activity", "CalibrationActivity");
					intent.putExtra("hardType", hardType);
//					int[] checkedChannelIndexArray=((MainActivity)context).bottomOperate.addChannelViewPager.checkedChannelIndexArray;
//					intent.putExtra("checkedChannelIndexArray", checkedChannelIndexArray);
					context.startActivityForResult(intent,1);//context.startActivity(intent);
				}
			}
			break;
		case 2:// ������
			floatIntent = new Intent(context, FloatingService.class);
			if (!mainFragment.isShowFloatWindow()) {
				mainFragment.setShowFloatWindow(true);
				context.startService(floatIntent);
				mainFragment.startService();
			} else {
				mainFragment.setShowFloatWindow(false);
				context.stopService(floatIntent);
				mainFragment.stopService();
			}
			// mainFragment.setFloatIntent(floatIntent);
			break;
		default:
			break;
		}
	}
	
	public void childPositionClick_1(int childPosition) {
		if(DataCollection.isRecording){
			Toast.makeText(context.getApplicationContext(),
					 R.string.Collecting_data, Toast.LENGTH_SHORT)//����������
					.show();
			return;
		}
		switch (childPosition) {
		case 0:
			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(acquiFragment).commit();
			break;
		case 1:
			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(preTriggerFragment).commit();
			break;
		case 2:
			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(triggerFragment).commit();
			break;
		default:
			break;
		}
	}

	public void childPositionClick_2(int childPosition) {

		switch (childPosition) {
		case 0:

			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(signalFragment).commit();

			break;
		case 1:

			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(splFragment).commit();

			break;
		case 2:

			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(octFragment).commit();

			break;
		case 3:

			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(fftFragment).commit();

			break;
		case 4:

			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(aiFragment).commit();

			break;
		case 5:

			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(rpmFragment).commit();

			break;
		case 6:

			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(colormapFragment).commit();

			break;
		case 7:

			context.getSupportFragmentManager().beginTransaction()
					.hide(mainFragment).show(orderFragment).commit();

			break;
		default:
			break;
		}
	}

	private void newFragment() {
		channelSettingFragment = new ChannelSettingFragment();
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.maintab, channelSettingFragment, "channelSetting")
				.commit();
//		.hide(channelSettingFragment)

		acquiFragment = new AcquisitionFragment();
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, acquiFragment, "acqui")
				.commit();
		
		orderFragment = new OrderFragment();
		orderFragment.setSpinnerValues(spinnerValues);
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, orderFragment, "order")
				.commit();
//		.hide(orderFragment)
		
//		.hide(acquiFragment)
		preTriggerFragment = new PreTriggerFragment();
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, preTriggerFragment, "pretrig")
				.commit();
		triggerFragment = new TriggerFragment();
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, triggerFragment, "trig")
				.commit();
//		.hide(triggerFragment)
		displayFragment = new DisplayFragment();
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, displayFragment, "display")
				.commit();
//		.hide(displayFragment)
		signalFragment = new SignalFragment();
		signalFragment.setSpinnerValues(spinnerValues);
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, signalFragment, "signal")
				.commit();
//		.hide(signalFragment)
		splFragment = new SplFragment();
		splFragment.setSpinnerValues(spinnerValues);
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, splFragment, "spl")
				.commit();
//		.hide(splFragment)
		octFragment = new OctFragment();
		octFragment.setSpinnerValues(spinnerValues);
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, octFragment, "oct")
				.commit();
//		.hide(octFragment)
		fftFragment = new FftFragment();
		fftFragment.setSpinnerValues(spinnerValues);
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, fftFragment, "fft")
				.commit();
		
//		.hide(fftFragment)
		aiFragment = new AiFragment();
		aiFragment.setSpinnerValues(spinnerValues);
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, aiFragment, "ai")
				.commit();
//		.hide(aiFragment)
		rpmFragment = new RpmFragment();
		rpmFragment.setSpinnerValues(spinnerValues);
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, rpmFragment, "rpm")
				.commit();
//		.hide(rpmFragment)
		colormapFragment = new ColormapFragment();
		colormapFragment.setSpinnerValues(spinnerValues);
		context.getSupportFragmentManager().beginTransaction()
				.add(R.id.leftContent, colormapFragment, "colormap")
				.commit();
		
//		.hide(colormapFragment)
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		
		if (groupPosition == 3) {
			FragmentManager manager = context.getSupportFragmentManager();
			// displayFragment.refresh();
			manager.beginTransaction().hide(mainFragment).show(displayFragment)
					.commit();
		} else {
			// ��̬�ı�ExpandableListView�߶�
			int expandHeight = dip2px(40) * 4 + dip2px(35)
					* adapter.getChildrenCount(groupPosition) + dip2px(10);
			ViewGroup.LayoutParams params = this.getLayoutParams();
			if (isGroupExpanded(groupPosition)) {
				params.height = dip2px(40) * 4 + dip2px(10);
			} else {
				params.height = expandHeight;
			}
			this.setLayoutParams(params);
		}
		return false;
	}

	public int dip2px(float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		// display������group������
		if (groupPosition == 3)
			return;
		// groupItem����
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			if (i != groupPosition && isGroupExpanded(i)) {
				this.collapseGroup(i);
			}
		}
	}

	public ChannelSettingFragment getChannelSettingFragment(){
		return this.channelSettingFragment;
	}
	public SignalFragment getSignalFragment() {
		return this.signalFragment;
	}

	public SplFragment getSplFragment() {
		return this.splFragment;
	}

	public OctFragment getOctFragment() {
		return this.octFragment;
	}

	public FftFragment getFftFragment() {
		return this.fftFragment;
	}

	public AiFragment getAiFragment() {
		return this.aiFragment;
	}

	public RpmFragment getRpmFragment() {
		return this.rpmFragment;
	}

	public ColormapFragment getColormapFragment() {
		return this.colormapFragment;
	}

	public DisplayFragment getDisplayFragment() {
		return this.displayFragment;
	}

	public AcquisitionFragment getAcquisitionFragment() {
		return this.acquiFragment;
	}

	public TriggerFragment getTriggerFragment() {
		return this.triggerFragment;
	}
	public PreTriggerFragment getPreTriggerFragment() {
		return this.preTriggerFragment;
	}
	public OrderFragment getOrderFragment() {
		return this.orderFragment;
	}
}
