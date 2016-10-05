package bottom.drawer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import left.drawer.MainFragment;
import mode.drive.DriveModeActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.Toast;

import com.cuiweiyou.dynamiclevelnavigation.util.CVU_JSONUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import common.CustomTab;
import com.example.mobileacquisition.ThemeLogic;

import common.DataCollection;
import common.ScaleView;
import common.XclSingalTransfer;

import draw.map.Legend;

/** �ϻ����������-ͨ�� */
public class AddChannelViewPager implements OnClickListener, OnLongClickListener {
	private FragmentActivity context;
	public ScrollView channelDialogLayout;
	private LayoutInflater inflater;
	private PopupWindow chanPopupWindow;
	private List<View> chan_views = new ArrayList<View>();// ��Ҫ��ҳ��ʾ��Viewװ��������
	/** ������й�ѡ����õ�ͨ�� */
	public ArrayList<Integer> isActivated_ChanNum = new ArrayList<Integer>();
	public static ArrayList<Integer> drive_channelCount = new ArrayList<Integer>();
	/** �Ѿ�������̵�ͨ�� */
	public static ArrayList<Integer> channelCount = new ArrayList<Integer>();
	/** �����������ƶ����ڵ�ͨ��������ͨ�� */
	public ArrayList<CustomImageButton> chanButtonList = new ArrayList<CustomImageButton>();
	public static Map<Integer, Integer> lightChannel = new HashMap<Integer, Integer>();
	private double eqequipment_size;
	private MainFragment mainFragment;
	private SharedPreferences preference;
	private boolean isMobilePhoneEnter;
	private RelativeLayout drive_legend;
	private int chan_num = 0;
	private ImageButton startCollection;
	private DataCollection dataCollection;
	private SharedPreferences.Editor editor;
//	private int[] activityChannelArray = new int[8];
//	public int[] checkedChannelIndexArray = new int[8];// 0 δ���1
														// �����޵�ѹ�źţ�2�����ѹ�źŵͣ�3�����ѹ�ź���4�����ѹ�źŸ�
	private XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
	private ArrayList<ScaleView> scaleViews;

	public Map<String, List<String>> choosedChannelsAndOrders;

	private ArrayList<CheckBox> orderCheckBoxList = new ArrayList<CheckBox>();

	public RelativeLayout getDrive_legend() {
		return drive_legend;
	}

	public void setDrive_legend(RelativeLayout drive_legend) {
		this.drive_legend = drive_legend;
	}

	public MainFragment getMainFragment() {
		return mainFragment;
	}

	public void setMainFragment(MainFragment mainFragment) {
		this.mainFragment = mainFragment;
	}

	/** ���ƽ����Ͽ������ƶ���ͨ�� */
	public AddChannelViewPager(FragmentActivity context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		channelDialogLayout = (ScrollView) inflater.inflate(R.layout.channel_order, null);
		preference = context.getSharedPreferences("window", 0);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float xdpi = metrics.xdpi;
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		double z = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
		eqequipment_size = Math.round(z / xdpi);// (xdpi* density)
		SharedPreferences preference = context.getSharedPreferences("Equipment", 0);
		int Equipment_Num = preference.getInt("Equipment", 0);
		switch (Equipment_Num) {
		case 0:
			chan_num = 0;

			break;
		case 1:
			chan_num = 1;
			break;
		case 2:
			chan_num = 8;
			break;
		}

//		activityChannelArray = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

//		xclSingalTransfer.putValue("activityChannelArray", activityChannelArray);

//		if (context.getClass().getSimpleName().equals("MainActivity")) {
//			ArrayList<View> views = ((MainActivity) context).getViewList();
//			for (int i = 0; i < views.size(); i++) {
//				((ScaleView) views.get(i)).setActivityChannelArray(activityChannelArray);
//			}
//		}
		// Toast.makeText(context.getApplicationContext(),eqequipment_size+"" ,Toast.LENGTH_SHORT).show();  
		if(eqequipment_size<=7){
			smallSize();
		} else {
			bigSize();
		}
	}

	private void smallSize() {
		int size = chan_num / 12 + 1;
		for (int i = 0; i < size; i++) {
			chan_views.add(inflater.inflate(R.layout.chan_page_one, null));
			TableRow tableRow1 = (TableRow) chan_views.get(i).findViewById(R.id.tableRow1);
			TableRow tableRow2 = (TableRow) chan_views.get(i).findViewById(R.id.tableRow2);
			tableRow1.setTag("views" + i);
			tableRow2.setTag("views" + i);
			for (int m = 0; m < 6; m++) {
				tableRow1.addView(new OneChannelView(context, chanButtonList));
			}
			for (int n = 0; n < 6; n++) {
				tableRow2.addView(new OneChannelView(context, chanButtonList));
			}
		}
	}

	private void bigSize() {
		int size = chan_num / 16 + 1;
		for (int i = 0; i < size; i++) {
			chan_views.add(inflater.inflate(R.layout.chan_page_one, null));
			TableRow tableRow1 = (TableRow) chan_views.get(i).findViewById(R.id.tableRow1);
			TableRow tableRow2 = (TableRow) chan_views.get(i).findViewById(R.id.tableRow2);
			tableRow1.setTag("views" + i);
			tableRow2.setTag("views" + i);
			for (int m = 0; m < 8; m++) {
				tableRow1.addView(new OneChannelView(context, chanButtonList));
			}
			for (int n = 0; n < 8; n++) {
				tableRow2.addView(new OneChannelView(context, chanButtonList));
			}
		}
	}

	public void showDriveChannelViewPager(VerticalViewPager viewPager, ArrayList<Integer> isActivated_ChanNum) {
		this.isActivated_ChanNum = isActivated_ChanNum;
		addViewPager(viewPager);
	}

	public void addViewPager(VerticalViewPager viewPager) {
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
		editor = preference.edit();
		ChanPagerAdapter chanPagerAdapter = new ChanPagerAdapter(context, chan_views);
		viewPager.setAdapter(chanPagerAdapter);
		// ͨ�����״̬��Ϣ
		XclSingalTransfer xclSignalTransfer = XclSingalTransfer.getInstance();
		Handler buttonStateChangeHandler = new Handler() {
			@Override
			public void handleMessage(Message age) {
//				if (age.arg1 < checkedChannelIndexArray.length) {
//					checkedChannelIndexArray[age.arg1] = age.what;
//					// xclSingalTransfer.putValue("activityChannelArray",
//					// activityChannelArray);
//				}
				switch (age.what) {
				case 0:// δ����
					chanButtonList.get(age.arg1).setIfChannelActivity(false);
					/*
					 * editor.putBoolean("setIfChannelActivity", false);
					 * editor.commit();
					 */
					if(ThemeLogic.themeType==1){
						chanButtonList.get(age.arg1).setBackgroundResource(R.drawable.ico_channel_noactive);
					}else{
						chanButtonList.get(age.arg1).setBackgroundResource(R.drawable.ico_channel_noactive1);
					}
					
					chanButtonList.get(age.arg1).setSelected(false);
					for (int j = 0; j < isActivated_ChanNum.size(); j++) {
						if (isActivated_ChanNum.get(j).equals(age.arg1)) {
							isActivated_ChanNum.remove(j);
						}
					}
					for (int k = 0; k < channelCount.size(); k++) {
						if (channelCount.get(k).equals(age.arg1 + 1)) {
							channelCount.remove(k);
						}
						addLegendView();
					}
					for (int k = 0; k < drive_channelCount.size(); k++) {
						if (drive_channelCount.get(k).equals(age.arg1 + 1)) {
							drive_channelCount.remove(k);
						}
						if (drive_legend != null) {
							addDriveLegendView();
						}
					}

					/*
					 * SharedPreferences preference =
					 * context.getSharedPreferences("hz_5D", 0);
					 * SharedPreferences.Editor editor = preference.edit();
					 * editor.remove("ChannelNum"); editor.commit();
					 */
					/*
					 * if(startCollection!=null&&isActivated_ChanNum.size()==0&&
					 * startCollection.isSelected()){
					 * startCollection.setImageResource(R.drawable.button_play_n
					 * ); startCollection.setSelected(false);
					 * dataCollection.Stop(); SharedPreferences preference =
					 * context.getSharedPreferences("hz_5D", 0);
					 * SharedPreferences.Editor editor = preference.edit();
					 * editor.remove("ChannelNum"); editor.commit(); }
					 */
					break;
				case 1:// ����
					chanButtonList.get(age.arg1).setIfChannelActivity(true);
					
					if(ThemeLogic.themeType==1){
						chanButtonList.get(age.arg1).setBackgroundResource(R.drawable.ico_channel);
					}else{
						chanButtonList.get(age.arg1).setBackgroundResource(R.drawable.ico_channel1);
					}
					setChannelOnClickListener(age.arg1);
					isActivated_ChanNum.add(age.arg1);
					HashSet h = new HashSet(isActivated_ChanNum);
					isActivated_ChanNum.clear();
					isActivated_ChanNum.addAll(h);
					break;
				case 2:// ��ѹ��
					chanButtonList.get(age.arg1).setImageResource(R.drawable.ico_channel_low);
					break;
				case 3:// ��ѹ��
					chanButtonList.get(age.arg1).setImageResource(R.drawable.ico_channel);
					break;
				case 4:// ��ѹ��
					chanButtonList.get(age.arg1).setImageResource(R.drawable.ico_channel_heigh);
					break;
				}
			}
		};
		xclSignalTransfer.putValue("buttonStateChange", buttonStateChangeHandler);
		for (int i = 0; i < chanButtonList.size(); i++) {
			if (context.getClass().getSimpleName().equals("MainActivity")) {
				
				if(ThemeLogic.themeType==1){
					chanButtonList.get(i).setBackgroundResource(R.drawable.ico_channel_noactive);
				}else{
					chanButtonList.get(i).setBackgroundResource(R.drawable.ico_channel_noactive1);
				}
			} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
				for (int j = 0; j < isActivated_ChanNum.size(); j++) {
					int ChannelNum = isActivated_ChanNum.get(j);
				
					
						chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_jiashi);
					// chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_sel);
					// chanButtonList.get(ChannelNum).setSelected(true);
					// chanButtonList.get(ChannelNum).setOnClickListener(this);
					setChannelOnClickListener(ChannelNum);
				}

			}
			// �����Զ���ImageButton����Ҫ��ʾ���ı�����
			chanButtonList.get(i).setText(String.valueOf(i + 1));
			chanButtonList.get(i).setColor(Color.WHITE);
			chanButtonList.get(i).setTextSize(26);
			int tag = (Integer) chanButtonList.get(i).getTag();
			if (tag > (chan_num - 1)) {
				chanButtonList.get(i).setVisibility(View.GONE);
			}
		}

		//////////////////// ��� 16.3.9 �����ͨģʽ�ͼ�ʻģʽ ͬ�����ߵ�ͨ�� -begin
		if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
			if (channelCount.size() != 0) {
				drive_channelCount.clear();
				for (int j = 0; j < channelCount.size(); j++) {
					int ChannelNum = channelCount.get(j) - 1;
					// chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_jiashi);

					drive_channelCount.add(channelCount.get(j));
					
					if(ThemeLogic.themeType==1){
						chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_sel2);
					}else{
						chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_sel21);
					}
					chanButtonList.get(ChannelNum).setSelected(true);
					int index = (Integer) chanButtonList.get(ChannelNum).getTag();
//					if (index < activityChannelArray.length) {
//						activityChannelArray[index] = 1;
//
//					}
				}

			} else {
				for (int j = 0; j < isActivated_ChanNum.size(); j++) {
					int ChannelNum = isActivated_ChanNum.get(j);
					chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_jiashi);
					chanButtonList.get(ChannelNum).setSelected(false);
					int index = (Integer) chanButtonList.get(ChannelNum).getTag();
//					if (index < activityChannelArray.length) {
//						activityChannelArray[index] = 0;
//					}
				}
				drive_channelCount.clear();
			}
		}
		//////////////////// - end
	}

	//////////////////////////////// 16.3.9 �� ��ͨģʽ���ʻģʽ�����ͬ�� -begin
	/** ����BottomOperate��ͨ������״̬ */
	public void changeBottomChannelActivitedState() {
		// Log.e("ard", "resurme����" + drive_channelCount.size());
		/*
		 * ��ִӼ�ʻģʽ���ػ��Ǳ궨����
		 * ((MainActivity)context).resultCode==0��ʻģʽ����������
		 * ((MainActivity)context).resultCode==1�궨����������
		 */
		if(((MainActivity)context).resultCode==0){
			for (int j = 0; j < isActivated_ChanNum.size(); j++) {
				int ChannelNum = isActivated_ChanNum.get(j);
				
				if(ThemeLogic.themeType==1){
					chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel);
				}else{
					chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel1);
				}
				chanButtonList.get(ChannelNum).setSelected(false);
				int index = (Integer) chanButtonList.get(ChannelNum).getTag();
//				if (index < activityChannelArray.length) {
//					activityChannelArray[index] = 0;
//				}
			}
			// channelCount.clear();
			if (drive_channelCount.size() != 0) {
				channelCount.clear();
				for (int j = 0; j < drive_channelCount.size(); j++) {
					int ChannelNum = drive_channelCount.get(j) - 1;
					// chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_jiashi);
	
					channelCount.add(drive_channelCount.get(j));
					if(ThemeLogic.themeType==1){
						chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_sel);
					}else{
						chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel_sel1);
					}
					
					chanButtonList.get(ChannelNum).setSelected(true);
					int index = (Integer) chanButtonList.get(ChannelNum).getTag();
//					if (index < activityChannelArray.length) {
//						activityChannelArray[index] = 1;
//	
//					}
					addLegendView();
				}
				} else {
					channelCount.clear();
				}
		}

	} ///////////////////// -end

	public void setChannelOnClickListener(int index) {
		chanButtonList.get(index).setOnLongClickListener(this);
		chanButtonList.get(index).setOnClickListener(this);
	}

	@Override
	public boolean onLongClick(View v) {
		CustomImageButton channelButton = (CustomImageButton) v;
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
		String algorithmChecked = preference.getString("Order", "close");
		if (algorithmChecked.equals("close")) {
			Toast.makeText(context.getApplicationContext(), R.string.OpenOrder, 0).show();
			return true;
		} else {
			if (context.getClass().getSimpleName().equals("MainActivity")) {
				if (channelButton.ifChannelActivity()) {
					ArrayList<String> algorithmList = new ArrayList<String>();
					CustomTab ct = ((MainActivity) context).mainCustomTab;
					for (int i = 0; i < ct.algorithm_spinnerList.size(); i++) {
						algorithmList.add(ct.algorithm_spinnerList.get(i).getText().toString());
					}
					if (algorithmList.contains("Order")) {
						chanPopupWindowShown(v);
					} else {
						Toast.makeText(context.getApplicationContext(), R.string.ChooseOrder, 0).show();
					}

				}
			} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
				if (((DriveModeActivity) context).algorithm_title.getText().equals("Order")) {
					chanPopupWindowShown(v);
				} else {
					Toast.makeText(context.getApplicationContext(), R.string.ChooseOrder, 0).show();
				}
			}

		}
		return true;
	}

	private void chanPopupWindowShown(View v) {
		final String channel = v.getTag().toString(); // ͨ��
		// final Map<String, List<String>> choosedChannelsAndOrders;
		//�����map��Ϊȫ�ֱ���
		//////////////////////////// 16.3.18 ʹ��SP�洢��ѡ�״� -begin
		String caojson = CVU_SPUtil.getChannelsAndOrders(context, channel);
		if (null != caojson && !"".equals(caojson)) {
			choosedChannelsAndOrders = CVU_JSONUtil.json2OrderMap(caojson);
		} else {
			choosedChannelsAndOrders = new HashMap<String, List<String>>();
		}
		//////////////////////////// -end

		/********************************* Order�޸� *********************/ // TODO
																			// ����ѡ��״̬
		int oeder_num = 0;
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
		LinearLayout order_layout = (LinearLayout) channelDialogLayout.findViewById(R.id.pop_order_layout);
		order_layout.removeAllViews();
		String order_names = preference.getString("order_names", "");
		String[] order_names_str = order_names.split(" ");
		String order_values = preference.getString("order_values", "");
		// Log.e("ard", "OrderƵ��1��" + order_values);
		String[] order_values_str = order_values.split(" ");
		// Log.e("ard", "OrderƵ��2��" + order_values_str);
		for (int i = 0; i < order_values_str.length; i++) {
			if (order_values_str[i].equals("true")) {
				oeder_num++;
				LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.channel_order_one, null);
				order_layout.addView(linearLayout);
				CheckBox pop_chan_one_order = (CheckBox) linearLayout.findViewById(R.id.pop_chan_one_order);
				orderCheckBoxList.add(pop_chan_one_order);
				final String str1 = i < order_names_str.length ? order_names_str[i] : "";
				// Log.e("ard", "�״Σ�" + str1);
				pop_chan_one_order.setText(str1 + " " + context.getResources().getString(R.string.order_unit));

				///////////////////////// 16.3.11 �� ͨ��Order�㷨��������12468�� ����״̬����
				///////////////////////// -begin
				// 16.3.18 ʹ��SP�洢��ѡ�״� -begin
				List<String> selectedorders = choosedChannelsAndOrders.get(channel);
				if (null != selectedorders) {
					for (int j = 0; j < selectedorders.size(); j++) {
						String order = selectedorders.get(j);
						if (str1.equals(order)) {
							pop_chan_one_order.setChecked(true);
							break;
						}
					}
				}
				pop_chan_one_order.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(choosedChannelsAndOrders.containsKey(channel)){                // ����Ѿ������ͨ����������޸�
							List<String> orders = choosedChannelsAndOrders.get(channel);  // ���

							if(isChecked){                     // ���ѡ��
								if(!orders.contains(str1)){    // ���δ�洢��ʹ洢
									orders.add(str1);
								}
							} else { // ����ÿ�
								if (orders.contains(str1)) { // ����д洢�����
									orders.remove(str1);
								}
							}
							/*
							 * �޶����ֻ��ѡ��3���״�
							 */
							for(int m=0;m<orderCheckBoxList.size();m++){
								if(orders.size()>2){
									String str=orderCheckBoxList.get(m).getText().toString();
									if(orders.contains(str.substring(0, str.length()-2))){
										orderCheckBoxList.get(m).setClickable(true);
									}else{
										orderCheckBoxList.get(m).setClickable(false);
									}	
								
								}else{
									orderCheckBoxList.get(m).setClickable(true);
								}
							}
							choosedChannelsAndOrders.put(channel, orders); // ����ͨ��
							
						} else {
							List<String> orders = new ArrayList<String>();
							if (isChecked) {
								orders.add(str1);
							}
							choosedChannelsAndOrders.put(channel, orders);
							
						}

						// Log.e("ard", "ͨ��" + Integer.valueOf(channel) + "���״�"
						// + str1 + "��״̬��" + isChecked); // TODO SP����
					}
				});
				/////////////////////// -over
			}
		}

		if (oeder_num == 0) {
			Toast.makeText(context.getApplicationContext(), R.string.SelectOrder, 0).show();
			return;
		}
		/**************************************************************/

		// chanPopupWindow =new PopupWindow(channelDialogLayout,
		// v.getWidth()+100, dip2px(31)*4+dip2px(5));
		/////////////////////////////////////////////////////////////////////////// 16.3.11
		// �� ͨ��Order�㷨��������12468�׸�ѡ�б��Ż� -begin
		int tmp_height;
		if (4 < oeder_num)
			tmp_height = dip2px(32) * 4;
		else
			tmp_height = dip2px(32) * oeder_num;

		//////////////////////////////////////////////////////////////////////////// -goon
		chanPopupWindow = new PopupWindow(channelDialogLayout, v.getWidth() + 100, tmp_height);
		chanPopupWindow.setFocusable(true);
		chanPopupWindow.setOutsideTouchable(true);
		chanPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
		// chanPopupWindow.showAsDropDown(v, 0, -dip2px(55)-3*dip2px(35));
		chanPopupWindow.showAsDropDown(v, 0, 30);////////////////////////////////// -over

		chanPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO ������ѡ
				// Log.e("ard", "������ѡ��" + choosedChannelsAndOrders);
				String json = CVU_JSONUtil.orderMap2json(choosedChannelsAndOrders);
				CVU_SPUtil.saveChannelsAndOrders(context, channel, json);
			}
		});
	}

	public int dip2px(float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onClick(View v) {

		CustomImageButton channelButton = (CustomImageButton) v;
		if (context.getClass().getSimpleName().equals("MainActivity")) {

			if (channelButton.ifChannelActivity()) {
				int index = (Integer) v.getTag();
				if (!v.isSelected()) {
					channelCount.add(index + 1);
					
					if(ThemeLogic.themeType==1){
						v.setBackgroundResource(R.drawable.ico_channel_sel);
					}else{
						v.setBackgroundResource(R.drawable.ico_channel_sel1);
					}
					v.setSelected(true);
//					if (index < activityChannelArray.length) {
//						activityChannelArray[index] = 1;
//						xclSingalTransfer.putValue("activityChannelArray", activityChannelArray);
//					}
				} else {
					removeChannelCount(index + 1);
					
					if(ThemeLogic.themeType==1){
						v.setBackgroundResource(R.drawable.ico_channel);
					}else{
						v.setBackgroundResource(R.drawable.ico_channel1);
					}
					v.setSelected(false);
//					if (index < activityChannelArray.length) {
//						activityChannelArray[index] = 0;
//						xclSingalTransfer.putValue("activityChannelArray", activityChannelArray);
//					}
				}
			}

//			ArrayList<View> views = ((MainActivity) context).getViewList();
//			for (int i = 0; i < views.size(); i++) {
//				((ScaleView) views.get(i)).setActivityChannelArray(activityChannelArray);
//			}

		} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
			int index = (Integer) v.getTag();
			if (!v.isSelected()) {
				drive_channelCount.add(index + 1);
				// channelCount.add(index+1);
			
				if(ThemeLogic.themeType==1){
					v.setBackgroundResource(R.drawable.ico_channel_sel2);
				}else{
					v.setBackgroundResource(R.drawable.ico_channel_sel21);
				}
				v.setSelected(true);
//				if (index < activityChannelArray.length) {
//					activityChannelArray[index] = 1;
//
//				}
			} else {
				removeDriveChannelCount(index + 1);
				// removeChannelCount(index+1);
				v.setBackgroundResource(R.drawable.ico_channel_jiashi);
				
				v.setSelected(false);
//				if (index < activityChannelArray.length) {
//					activityChannelArray[index] = 0;
//				}
			}
		}

		if (context.getClass().getSimpleName().equals("MainActivity")) {
			addLegendView();

		} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {

			addDriveLegendView();
		}

		// Toast.makeText(context,
		// "driveͨ����"+drive_channelCount.size()+","+context.getClass().getSimpleName(),
		// 0).show();
	}

	public void addDriveLegendView() {
		Collections.sort(drive_channelCount);
		drive_legend.removeAllViews();
		Legend drawView = new Legend(context, drive_channelCount);
		RelativeLayout.LayoutParams params = new LayoutParams(0, 0);
		if (channelCount.size() <= 16) {
			params.width = 160;
			params.height = drive_channelCount.size() * 25;
		} else if (drive_channelCount.size() > 16 && drive_channelCount.size() <= 32) {
			params.width = 330;
			params.height = 16 * 25;
		} else if (drive_channelCount.size() > 32 && drive_channelCount.size() <= 48) {
			params.width = 490;
			params.height = 16 * 25;
		} else {
			params.width = 650;
			params.height = 16 * 25;
		}

		drawView.setLayoutParams(params);
		drive_legend.addView(drawView);

//		if (scaleViews != null && scaleViews.size() != 0)
//			setActivityChannelArray();
	}

//	private void setActivityChannelArray() {
//		for (int i = 0; i < scaleViews.size(); i++) {
//			scaleViews.get(i).setActivityChannelArray(activityChannelArray);
//		}
//	}

	/** �ڻ���С���ڻ���ͨ�� */
	public void addLegendView() {
		Collections.sort(channelCount);
		for (int i = 0; i < mainFragment.getLegendList().size(); i++) {
			mainFragment.getLegendList().get(i).removeAllViews();
			Legend drawView = new Legend(context, channelCount);
			drawView.setTag(i);
			RelativeLayout.LayoutParams params = new LayoutParams(0, 0);
			if (channelCount.size() <= 16) {
				params.width = 160;
				params.height = channelCount.size() * 25;
			} else if (channelCount.size() > 16 && channelCount.size() <= 32) {
				params.width = 330;
				params.height = 16 * 25;
			} else if (channelCount.size() > 32 && channelCount.size() <= 48) {
				params.width = 490;
				params.height = 16 * 25;
			} else {
				params.width = 650;
				params.height = 16 * 25;
			}

			drawView.setLayoutParams(params);
			mainFragment.getLegendList().get(i).addView(drawView);
			mainFragment.setChannelCount(channelCount);
		}
	}

	private void removeChannelCount(int num) {
		for (int i = 0; i < channelCount.size(); i++) {
			if (channelCount.get(i) == num) {
				channelCount.remove(i);
			}
		}
	}

	private void removeDriveChannelCount(int num) {
		for (int i = 0; i < drive_channelCount.size(); i++) {
			if (drive_channelCount.get(i) == num) {
				drive_channelCount.remove(i);
			}
		}
	}
	public void skinChanged(){
		if(chanButtonList.size()==0)return;
		for(int i=0;i<chanButtonList.size();i++){
			if(ThemeLogic.themeType==1){
				chanButtonList.get(i).setBackgroundResource(R.drawable.ico_channel_noactive);
			}else{
				chanButtonList.get(i).setBackgroundResource(R.drawable.ico_channel_noactive1);
			}
		}
		if(isActivated_ChanNum.size()==0)return;
		for (int j = 0; j < isActivated_ChanNum.size(); j++) {
			int ChannelNum = isActivated_ChanNum.get(j);
			if(ThemeLogic.themeType==1){
				chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel);
			}else{
				chanButtonList.get(ChannelNum).setBackgroundResource(R.drawable.ico_channel1);
			}
		}
		if(channelCount.size()==0)return;
		for(int i=0;i<channelCount.size();i++){
			if(ThemeLogic.themeType==1){
				chanButtonList.get(channelCount.get(i)-1).setBackgroundResource(R.drawable.ico_channel_sel);
			}else{
				chanButtonList.get(channelCount.get(i)-1).setBackgroundResource(R.drawable.ico_channel_sel1);
			}
		}
	}

	public ArrayList<Integer> getIsActivated_ChanNum() {
		return isActivated_ChanNum;
	}

	public void setIsActivated_ChanNum(ArrayList<Integer> isActivated_ChanNum) {
		this.isActivated_ChanNum = isActivated_ChanNum;
	}

	public ArrayList<Integer> getActivated_ChanNumList() {
		return isActivated_ChanNum;
	}

	public void setStopCollection(ImageButton startCollection, DataCollection dataCollection) {
		this.startCollection = startCollection;
		this.dataCollection = dataCollection;
	}

//	public int[] getActivityChannelArray() {
//		return activityChannelArray;
//	}

	public void setScaleViews(ArrayList<ScaleView> scaleViews) {
		this.scaleViews = scaleViews;
	}

	public void setContext(FragmentActivity context) {
		this.context = context;
	}
}
