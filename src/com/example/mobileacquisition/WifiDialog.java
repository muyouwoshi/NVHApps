package com.example.mobileacquisition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.params.HttpConnectionParams;

import left.drawer.ChannelSettingFragment;
import mode.drive.DriveModeActivity;
import preference.welcome.MyAPP;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import bottom.drawer.AddChannelViewPager;
import bottom.drawer.BottomOperate;

import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;
import com.example.mobileacquisition.WelcomeActivity.MyHandler;
import common.CustomTab;
import common.DataCollection;
import common.XclSingalTransfer;

public class WifiDialog implements OnClickListener, OnItemLongClickListener, ThemeChangeListener {
	private final int Equipment0 = 100;
	private final int Equipment1 = 101;
	private final int Equipment2 = 102;
	private MyAPP mAPP = null;
	private MyHandler handler = null;
	private FragmentActivity context = null;
	private List<ScanResult> mwifiResult = new ArrayList<ScanResult>();
	private List<ScanResult> scanResults = new ArrayList<ScanResult>();
	private RelativeLayout wifiLayout, passwordLayout, preferenceLayout;
	private AlertDialog wifiDialog, passwordDialog, preferenceDialog;
	private LayoutInflater inflater;
	private WifiConfiguretion wifiConfiguretion;
	private ScanResult Wifi;// 鑾峰彇鐐瑰嚮鐨剋ifi
	private String wifiSSID;// wifi鍚�
	private Button notUsingHardware, usingMobilePhone;
	private ProgressDialog progressDialog = null;
	private boolean notShown = false;// 涓嶅啀璇㈤棶鏍囪
	private TextView equipmentValue;// ,signalStrength,signalValue;
	private ListView preWifiList;
	private BottomOperate bottomOperate;
	private int languageChangedId;
	private ImageView equipmentImageView;
	private ImageButton dialog_close;
	private ChannelSettingFragment channelSettingFragment;
	private List<String> scanSSID = new ArrayList<String>();
	// private SharedPreferences.Editor editor;
	private SharedPreferences.Editor Equipment_editor;
	private SharedPreferences NotShown_preference;
	private SharedPreferences.Editor NotShown_editor;
	private int pagecount;
	private WifiAdapter wifiAdapter;
	private boolean isWLAN = false;
	private FragmentActivity activity;

	public WifiDialog(FragmentActivity activity) {
		// TODO Auto-generated constructor stub
		this.context = activity;
	}

	/**
	 * 娆㈣繋鐣岄潰渚ф媺鑿滃崟Wifi寮规
	 */
	public void ShowPreWifiDialog(int languageChangedId) {// 鏄剧ずwifi寮瑰嚭妗�
		equipmentImageView = (ImageView) this.context.findViewById(R.id.equipmentImageView);
		// signalStrength=(TextView)this.context.findViewById(R.id.signal_strength);
		equipmentValue = (TextView) this.context.findViewById(R.id.equipment_value);
		// signalValue=(TextView)this.context.findViewById(R.id.signal_value);
		this.languageChangedId = languageChangedId;
		mAPP = (MyAPP) context.getApplication();
		handler = mAPP.getHandler();
		ShowWifiDialog();
	}

	/**
	 * 宸︿晶鎷夎彍鍗昗ifi寮规
	 */
	public void ShowLeftWifiDialog(BottomOperate bottomOperate, FragmentManager manager) {// 鏄剧ずwifi寮瑰嚭妗�
		this.bottomOperate = bottomOperate;
		channelSettingFragment = (ChannelSettingFragment) manager.findFragmentByTag("channelSetting");
		pagecount = ((MainActivity) context).mainCustomTab.getPageCount();
		ShowWifiDialog();
	}

	/**
	 * Wifi寮规
	 */
	@SuppressLint("InflateParams")
	public void ShowWifiDialog() {
		NotShown_preference = context.getSharedPreferences("NotShown", 0);
		NotShown_editor = NotShown_preference.edit();
		notShown = NotShown_preference.getBoolean("isShown", false);
		SharedPreferences preference = context.getSharedPreferences("Equipment", 0);
		Equipment_editor = preference.edit();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		wifiConfiguretion = new WifiConfiguretion(context);
		wifiLayout = (RelativeLayout) inflater.inflate(R.layout.wifi_dialog, null);
		dialog_close = (ImageButton) wifiLayout.findViewById(R.id.dialog_close);
		dialog_close.setOnClickListener(this);
		// if(wificonnect.isConnected()) return;
		if (!wifiConfiguretion.isOpen()) {
			mwifiResult.clear();
		} else {
			mwifiResult = wifiConfiguretion.scan();// 鑾峰彇闄勮繎wifi鐑偣
		}
		if (mwifiResult.size() != 0) {
			for (ScanResult result : mwifiResult) {
				if (!scanSSID.contains(result.SSID)) {
					scanSSID.add(result.SSID);
					scanResults.add(result);
				}
			}
		}
		wifiAdapter = new WifiAdapter(context, scanSSID, wifiConfiguretion);
		wifiAdapter.notifyDataSetChanged();
		preWifiList = (ListView) wifiLayout.findViewById(R.id.dialog_view);
		preWifiList.setAdapter(wifiAdapter);
		if (wifiDialog == null) {
			wifiDialog = new AlertDialog.Builder(context).create();
			wifiDialog.show();
		} else {
			wifiDialog.show();
		}
		wifiDialog.getWindow().setContentView(wifiLayout);
		wifiDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		wifiDialog.setView(preWifiList);
		preWifiList.setOnItemClickListener(new PreWifiItemListener());
		preWifiList.setOnItemLongClickListener(this);
		notUsingHardware = (Button) wifiLayout.findViewById(R.id.not_using_hardware);
		usingMobilePhone = (Button) wifiLayout.findViewById(R.id.using_mobile_phone);
		notUsingHardware.setOnClickListener(this);
		usingMobilePhone.setOnClickListener(this);

	}

	/**
	 * WifiItem鐐瑰嚮浜嬩欢
	 */
	class PreWifiItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			wifiConfiguretion.flushWifiInfo();
			Wifi = scanResults.get(arg2);
			wifiSSID = Wifi.SSID;
			if (wifiConfiguretion.IsExsits(wifiSSID) == null) {// 鏈繛鎺ヨ繃鐨剋ifi
				if (wifiConfiguretion.getEncryptString(Wifi.capabilities).equals("OPEN")) { // 寮�斁鐨剋ifi鐩存帴杩炴帴
					wifiConfiguretion.addNetWork(wifiConfiguretion.CreateWifiInfo(wifiSSID, "",
							wifiConfiguretion.getWifiCipher(Wifi.capabilities)));
					progressDialog = ProgressDialog.show(context, "",
							context.getResources().getString(R.string.In_connecting));
					// wifiConfiguretion.mWifiManager.enableNetwork(wifiConfiguretion.mWifiManager.addNetwork(wifiConfiguretion.wc),
					// true);
					wifiConfiguretion.mWifiManager.enableNetwork(wifiConfiguretion.IsExsits(wifiSSID).networkId, true);
					new EquipmentThread().start();
					wifiDialog.dismiss();
				} else {
					ShowPasswordDialog();
				}
			} else {
				progressDialog = ProgressDialog.show(context, "",
						context.getResources().getString(R.string.In_connecting));
				wifiConfiguretion.mWifiManager.enableNetwork(wifiConfiguretion.IsExsits(wifiSSID).networkId, true);
				new EquipmentThread().start();
				wifiDialog.dismiss();
			}
		}

	}

	/**
	 * 瀵嗙爜寮规
	 */
	@SuppressLint("InflateParams")
	public void ShowPasswordDialog() {// 鏄剧ず瀵嗙爜寮瑰嚭妗�
		// 1. 甯冨眬鏂囦欢杞崲涓篤iew瀵硅薄
		passwordLayout = (RelativeLayout) inflater.inflate(R.layout.wifipasd_dialog, null);
		// 2. 鏂板缓瀵硅瘽妗嗗璞�
		passwordDialog = new AlertDialog.Builder(context).create();
		// passwordDialog.setCancelable(false);
		passwordDialog.show();
		passwordDialog.getWindow().setContentView(passwordLayout);
		passwordDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		// 3. 娑堟伅鍐呭
		final EditText password = (EditText) passwordLayout.findViewById(R.id.dialog_edit);
		// 4. 纭畾鎸夐挳
		Button btnOK = (Button) passwordLayout.findViewById(R.id.dialog_ok);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if(wifiConfiguretion.isWifiConnected(WelcomeActivity.this)){//鍒ゆ柇鏄惁杩炴帴wifi
				String str_password = password.getText().toString();
				if (wifiConfiguretion.Connect(wifiSSID, str_password,
						wifiConfiguretion.getWifiCipher(Wifi.capabilities))) {
					wifiConfiguretion.addNetWork(wifiConfiguretion.CreateWifiInfo(wifiSSID, str_password,
							wifiConfiguretion.getWifiCipher(Wifi.capabilities)));
					wifiDialog.dismiss();
					passwordDialog.dismiss();
					progressDialog = ProgressDialog.show(context, "",
							context.getResources().getString(R.string.In_connecting));
					new EquipmentThread().start();
				} else {
					Toast.makeText(context.getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT)// 璇疯緭鍏ュ瘑鐮�
							.show();
				}
			}
		});
		// 5. 鍙栨秷鎸夐挳
		Button btnCancel = (Button) passwordLayout.findViewById(R.id.dialog_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				passwordDialog.dismiss();
			}
		});
	}

	/**
	 * 鏄惁璁句负榛樿璁惧寮规
	 */
	@SuppressLint("InflateParams")
	public void ShowPreferenceDialog() {//// 鏄剧ず璇㈤棶鏄惁璁句负榛樿璁惧鐨勫脊鍑烘
		// 1. 甯冨眬鏂囦欢杞崲涓篤iew瀵硅薄
		preferenceLayout = (RelativeLayout) inflater.inflate(R.layout.preference_dialog, null);
		// 2. 鏂板缓瀵硅瘽妗嗗璞�
		preferenceDialog = new AlertDialog.Builder(context).create();
		// preferenceDialog.setCancelable(false);
		preferenceDialog.show();
		preferenceDialog.getWindow().setContentView(preferenceLayout);
		preferenceDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		RelativeLayout rl_dialog_content = (RelativeLayout) preferenceLayout.findViewById(R.id.rl_dialog_content);
		TextView dialog_title = (TextView) preferenceLayout.findViewById(R.id.dialog_title);
		// 4. 纭畾鎸夐挳
		Button btnOK = (Button) preferenceLayout.findViewById(R.id.dialog_ok);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Toast.makeText(context.getApplicationContext(), wifiSSID,
				 * Toast.LENGTH_SHORT) .show();
				 */
				SharedPreferences preference = context.getSharedPreferences("default_device", 0);
				SharedPreferences.Editor wifiSSID_editor = preference.edit();
				wifiSSID_editor.putString("WifiSSID", wifiSSID);
				wifiSSID_editor.commit();
				preferenceDialog.dismiss();
			}
		});
		// 5. 鍙栨秷鎸夐挳
		Button btnCancel = (Button) preferenceLayout.findViewById(R.id.dialog_cancel);

		if (ThemeLogic.themeType == 1) {
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg);
			btnOK.setBackgroundResource(R.drawable.btn_gray);
			btnCancel.setBackgroundResource(R.drawable.corners_bg3);
			dialog_title.setTextColor(Color.argb(255, 25, 25, 112));
		} else {
			rl_dialog_content.setBackgroundResource(R.drawable.welcome_title_corners_bg1);
			btnOK.setBackgroundResource(R.drawable.bt_gray_selector2);
			btnCancel.setBackgroundResource(R.drawable.bt_blue_selector1);
			dialog_title.setTextColor(Color.argb(255, 255, 255, 255));
		}

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				preferenceDialog.dismiss();
			}
		});
		// 涓嶅啀璇㈤棶
		CheckBox checkNotShown = (CheckBox) preferenceLayout.findViewById(R.id.not_shown);
		checkNotShown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {
					NotShown_editor.putBoolean("isShown", true);
					NotShown_editor.commit();
					// notShown=true;
				} else {
					NotShown_editor.putBoolean("isShown", false);
					NotShown_editor.commit();
					// notShown=false;
				}

			}
		});
	}

	/**
	 * 鏄剧ず璁惧淇℃伅
	 */
	class EquipmentThread extends Thread {
		@Override
		public void run() {
			wifiConfiguretion.flushWifiInfo();
			Message msg = new Message();
			try {
				Thread.sleep(5000);
				/*
				 * if(wifiConfiguretion.isConnecting()){
				 * 
				 * Log.e("姝ｅ湪杩炴帴", "姝ｅ湪杩炴帴..............."); Log.e("aaaaa",
				 * wifiConfiguretion.isNetworkAvailable()+"");
				 * Thread.sleep(3000);
				 * 
				 * // wifiConfiguretion.flushWifiInfo(); }
				 */
				// ------------寤虹珛杩炴帴-------
				// SocketAddress address = new InetSocketAddress("192.168.2.8",
				// 7);
				SocketAddress client_connectaddress = new InetSocketAddress("192.168.120.1", 8010);//客户端
				Socket client_socket = null;//客户端socket
				ServerSocket server_serverSocket=null;//服务器端serversocket
				String result = "";
				// wifiConfiguretion.isWifiConnected()&&
				if (wifiConfiguretion.isNetworkAvailable() && wifiConfiguretion.isConnectSSID() != null) {//
					// --------杩炴帴纭欢---------
					try {
//						long begin = System.currentTimeMillis();// 璁＄畻寮�杩炴帴鐨勬椂闂�
						client_socket = new Socket();
						client_socket.setSoTimeout(5000);
						// socket.connect(address,1000);
						client_socket.connect(client_connectaddress, 1000);
//						long end = System.currentTimeMillis();// 璁＄畻鏈鸿繛鎺ョ粨鏉熺殑鏃堕棿
//						result = (end - begin) + "ms";
//						Log.e("bug1110", result);
						if (client_socket.isConnected() && !client_socket.isClosed()) {
							msg.what = 1;// what锛宨nt绫诲瀷锛屾湭瀹氫箟鐨勬秷鎭紝浠ヤ究鎺ユ敹娑堟伅鑰呭彲浠ラ壌瀹氭秷鎭槸鍏充簬浠�箞鐨勩�姣忎釜鍙ユ焺閮芥湁鑷繁鐨勬秷鎭懡鍚嶇┖闂达紝涓嶅繀鎷呭績鍐茬獊
							XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
							xclSingalTransfer.putValue("client_socket", client_socket);
							try {
								server_serverSocket= new ServerSocket(8011);
								xclSingalTransfer.putValue("server_serverSocket",server_serverSocket);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								server_serverSocket.close();
							}
						} else {
							wifiConfiguretion.disconnectWifi();
							msg.what = 0;
							client_socket.close();
						}
						// socket.close();
					} catch (BindException e) {
						// result = "IP鍦板潃鎴栫鍙ｇ粦瀹氬紓甯革紒";
						Log.e("bug111", result);
						wifiConfiguretion.disconnectWifi();
						msg.what = 0;
						e.printStackTrace();
					} catch (UnknownHostException e) {
						// result = "鏈瘑鍒富鏈哄湴鍧�紒";
						Log.e("bug1111", result);
						wifiConfiguretion.disconnectWifi();
						msg.what = 0;
						e.printStackTrace();
					} catch (SocketTimeoutException e) {
						wifiConfiguretion.disconnectWifi();
						msg.what = 0;
						e.printStackTrace();
					} catch (ConnectException e) {
						// result = "鎷掔粷杩炴帴锛�;
						Log.e("bug11111", result);
						wifiConfiguretion.disconnectWifi();
						msg.what = 0;
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						wifiConfiguretion.disconnectWifi();
						msg.what = 0;// 鏈繛鎺�
						// msg.arg1=0;//杩炴帴澶辫触
						e.printStackTrace();
					} catch (Exception e) {
						// result = "澶辫触鍟︼紒";
						Log.e("bug111111", result);
						wifiConfiguretion.disconnectWifi();
						msg.what = 0;
						e.printStackTrace();
						// }finally{
						// if (socket!=null) {
						// try {
						// socket.close();
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						// }
						// }
					}
				} else {
					msg.what = 0;// 鏈繛鎺�
				}
			} catch (InterruptedException e) {
				msg.what = 0;// 鏈繛鎺�
				e.printStackTrace();
			}
			mHandler.sendMessage(msg);
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			wifiConfiguretion.flushWifiInfo();
			String str_SSID = wifiConfiguretion.getSSID();
			String equipment_Name = str_SSID.substring(1, str_SSID.length() - 1);
			String signal_Strength = String.valueOf(wifiConfiguretion.getRssi() + 100);
			// ((Integer)wifiConfiguretion.getRssi()).toString();
			isWLAN = false;
			switch (msg.what) {
			case 0:
				progressDialog.dismiss();
				if (context.getClass().getSimpleName().equals("WelcomeActivity")) {
					if (WelcomeActivity.languageChangedId == -11) {
						equipmentImageView.setBackgroundResource(R.drawable.ico_01_2);
					} else if (WelcomeActivity.languageChangedId == 11) {
						equipmentImageView.setBackgroundResource(R.drawable.ico_01_2_en);
					}
					// equipmentImageView.setBackgroundResource(R.drawable.ico_01_2);
					equipmentValue.setText(R.string.NO);
					// signalValue.setText("");
					// signalStrength.setVisibility(View.GONE);
					handler.sendEmptyMessage(Equipment0);
				} else {
					if (pagecount != 0) {
						removeAllView();
					}
					if (channelSettingFragment.getAnalog().lvt != null) {
						channelSettingFragment.getAnalog().clearOldChannel();
						channelSettingFragment.getAnalog().lvt.removeAllViews();
						Equipment_editor.remove("Equipment");
						Equipment_editor.putInt("Equipment", 0);
						Equipment_editor.commit();
						channelSettingFragment.getAnalog().InitChannelList(64);
					}
					bottomOperate.addChannelViewPager();
				}
				Toast.makeText(context.getApplicationContext(), R.string.Not_Connected, Toast.LENGTH_SHORT)// 鏈繛鎺ュ埌wifi
						.show();
				break;
			case 1:
				progressDialog.dismiss();
				Log.e("bug11",
						"杩炴帴wifi鐨刬p===============" + wifiConfiguretion.intToIp(wifiConfiguretion.getIPAddress()));
				if (context.getClass().getSimpleName().equals("WelcomeActivity")) {
					// equipmentImageView.setBackgroundResource(R.drawable.ico_01_1);
					equipmentValue.setText(equipment_Name);
					// signalStrength.setVisibility(View.VISIBLE);
					// signalStrength.setText(R.string.signal_strength);
					// signalValue.setText(signal_Strength);
					((WelcomeActivity) context).obtainWifiInfo();
					handler.sendEmptyMessage(Equipment2);

				} else {
					if (pagecount != 0) {
						removeAllView();
					}
					Toast.makeText(context.getApplicationContext(), equipment_Name, Toast.LENGTH_SHORT)// 宸茶繛鎺�/+R.string.Connected
							.show();
					if (channelSettingFragment.getAnalog().lvt != null) {
						channelSettingFragment.getAnalog().clearOldChannel();
						channelSettingFragment.getAnalog().lvt.removeAllViews();
						Equipment_editor.remove("Equipment");
						Equipment_editor.putInt("Equipment", 2);
						Equipment_editor.commit();
						channelSettingFragment.getAnalog().InitChannelList(8);
					} else {
						Equipment_editor.remove("Equipment");
						Equipment_editor.putInt("Equipment", 2);
						Equipment_editor.commit();
					}
					bottomOperate.addChannelViewPager();
				}
				// SharedPreferences preference =
				// context.getSharedPreferences("NotShown", 0);

				if (!notShown) {// 娌℃湁閫変腑涓嶅啀璇㈤棶CheckBox鏃跺脊鍑烘槸鍚﹁涓洪粯璁よ澶囩殑寮瑰嚭妗�
					ShowPreferenceDialog();
				}
				break;
			default:
				progressDialog.dismiss();
				break;
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.not_using_hardware:
			isWLAN = true;
			if (context.getClass().getSimpleName().equals("WelcomeActivity")) {
				if (WelcomeActivity.languageChangedId == -11) {
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_2);
				} else if (WelcomeActivity.languageChangedId == 11) {
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_2_en);
				}
				equipmentValue.setText(R.string.NO);// 鏃犺澶�
				// signalStrength.setVisibility(View.GONE);
				// signalStrength.setText("");
				// signalValue.setText("");
				wifiDialog.dismiss();
				wifiConfiguretion.disconnectWifi();
				handler.sendEmptyMessage(Equipment0);
			} else {
				if (pagecount != 0) {
					removeAllView();
				}
				wifiConfiguretion.disconnectWifi();
				wifiDialog.dismiss();
				if (channelSettingFragment.getAnalog().lvt != null) {
					channelSettingFragment.getAnalog().clearOldChannel();
					channelSettingFragment.getAnalog().lvt.removeAllViews();
					Equipment_editor.remove("Equipment");
					Equipment_editor.putInt("Equipment", 0);
					Equipment_editor.commit();
					channelSettingFragment.getAnalog().InitChannelList(64);

				} else {
					Equipment_editor.remove("Equipment");
					Equipment_editor.putInt("Equipment", 0);
					Equipment_editor.commit();
				}
				bottomOperate.addChannelViewPager();
			}
			break;
		case R.id.using_mobile_phone:
			isWLAN = true;
			if (context.getClass().getSimpleName().equals("WelcomeActivity")) {
				if (WelcomeActivity.languageChangedId == -11) {
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_3);
				} else if (WelcomeActivity.languageChangedId == 11) {
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_3_en);
				}
				equipmentValue.setText(R.string.This_machine);// 鏈満
				// signalStrength.setVisibility(View.GONE);
				// signalStrength.setText("");
				// signalValue.setText("");
				wifiDialog.dismiss();
				wifiConfiguretion.disconnectWifi();
				handler.sendEmptyMessage(Equipment1);
			} else {
				if (pagecount != 0) {
					removeAllView();
				}
				wifiConfiguretion.disconnectWifi();
				wifiDialog.dismiss();
				if (channelSettingFragment.getAnalog().lvt != null) {
					channelSettingFragment.getAnalog().clearOldChannel();
					channelSettingFragment.getAnalog().lvt.removeAllViews();
					Equipment_editor.remove("Equipment");
					Equipment_editor.putInt("Equipment", 1);
					Equipment_editor.commit();
					channelSettingFragment.getAnalog().InitChannelList(1);
				} else {
					Equipment_editor.remove("Equipment");
					Equipment_editor.putInt("Equipment", 1);
					Equipment_editor.commit();
				}
				bottomOperate.addChannelViewPager();
				// DataCollection.hardType=1;
			}
			break;
		case R.id.dialog_close:
			wifiDialog.dismiss();
			break;
		}
	}

	private void removeAllView() {
		CustomTab ct = ((MainActivity) context).mainCustomTab;
		ArrayList<Fragment> pageList = ct.getFragmentList();
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
						AddChannelViewPager.channelCount.clear();
					}
					break;
				}
				i++;
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		/*
		 * Toast.makeText(context.getApplicationContext(), "闀挎寜浜嬩欢",
		 * Toast.LENGTH_SHORT)//宸茶繛鎺�/+R.string.Connected .show();
		 */
		return true;
	}

	public BroadcastReceiver WifiBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (!context.getClass().getSimpleName().equals("WelcomeActivity")) {
				SharedPreferences preference = context.getSharedPreferences("Equipment", 0);
				int Equipment_Num = preference.getInt("Equipment", 0);
				switch (Equipment_Num) {
				case 0:
					isWLAN = true;
					break;
				case 1:
					isWLAN = true;
					break;
				case 2:
					isWLAN = false;
					break;
				}

			}
			if (isWLAN)
				return;
			if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				// if(info!=null){
				if (progressDialog != null && progressDialog.isShowing())
					return;
				if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
					System.out.println("wifi缃戠粶杩炴帴鏂紑");
					if (context.getClass().getSimpleName().equals("WelcomeActivity")) {
						refresh();

					} /*
						 * else if(context.getClass().getSimpleName().equals(
						 * "MainActivity")){
						 * 
						 * ShowWifiDialog();
						 * Toast.makeText(context.getApplicationContext(),
						 * "wifi缃戠粶杩炴帴鏂紑", Toast.LENGTH_SHORT) .show(); }
						 */else {
						Toast.makeText(context.getApplicationContext(), R.string.wifi_disconnected, Toast.LENGTH_SHORT)
								.show();
					}
				} else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
					System.out.println("杩炴帴鍒扮綉缁�");
					if (context.getClass().getSimpleName().equals("WelcomeActivity")) {
						refresh();
					}
				}
				/*
				 * }else{ refresh(); }
				 */
			}
		}

	};

	private void refresh() {
		((WelcomeActivity) context).refreshLayout();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	@Override
	public void onThemeChanged() {
		// TODO Auto-generated method stub
		switch (ThemeLogic.themeType) {

		/** 1闁靛棗鍊搁崢娑㈠即鐎涙ɑ鐓�☉鎾愁煼椤ｏ拷闁挎稐绶ょ槐鎺楁晸閿燂拷 */
		case 1:
			wifiLayout.getContext().setTheme(R.style.mode1);
			break;
		case 2:
			wifiLayout.getContext().setTheme(R.style.mode2);
			break;
		}
		TypedArray typedArray = wifiLayout.getContext().obtainStyledAttributes(R.styleable.myStyle);
		RelativeLayout tabUpLayout = (RelativeLayout) wifiLayout.findViewById(R.id.dialog_title);
		tabUpLayout.setBackground(typedArray.getDrawable(R.styleable.myStyle_title_windw_top));
		dialog_close.setBackground(typedArray.getDrawable(R.styleable.myStyle_bt_dialog_close));
		Button using_mobile_phone = (Button) wifiLayout.findViewById(R.id.using_mobile_phone);
		using_mobile_phone.setTextColor(typedArray.getColor(R.styleable.myStyle_wifi_dialog_bt, Color.YELLOW));
		Button not_using_hardware = (Button) wifiLayout.findViewById(R.id.not_using_hardware);
		not_using_hardware.setTextColor(typedArray.getColor(R.styleable.myStyle_wifi_dialog_bt, Color.YELLOW));
		typedArray.recycle();
	}
}
