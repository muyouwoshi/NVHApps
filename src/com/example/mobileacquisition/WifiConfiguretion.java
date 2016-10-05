package com.example.mobileacquisition;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import common.XclSingalTransfer;

public class WifiConfiguretion {
	private List<ScanResult> listResult;
	public WifiManager mWifiManager;
	public WifiInfo mWifiInfo;
	private List<WifiConfiguration> mWifiConfiguration;
	WifiLock mWifiLock;
	private Context context;
	public ConnectivityManager connectivity;
	public enum WifiCipherType {
		WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
	}

	public WifiConfiguretion(Context context) {
		this.context=context;
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
		connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public void flushWifiInfo(){
		if(mWifiManager != null){
			mWifiInfo = mWifiManager.getConnectionInfo();
		}
	}
	public boolean isOpen(){
		 if(mWifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
			 return true;
		 }else{
			 return false;
		 }

	}
	public void openNetCard() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	public void closeNetCard() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	public int checkNetCardState() {
		int state = mWifiManager.getWifiState();
		if (state == WifiManager.WIFI_STATE_DISABLING) {
		
			} else if (state == WifiManager.WIFI_STATE_DISABLED) {
			
				} else if (state == WifiManager.WIFI_STATE_ENABLING) {
		
					} else if (state == WifiManager.WIFI_STATE_ENABLED) {
						} else {		
							}
		return state;
	}

	public List<ScanResult> scan() {
		mWifiManager.startScan();
		listResult = mWifiManager.getScanResults();
		return listResult;
	}

	public WifiInfo connect() {
		return mWifiInfo = mWifiManager.getConnectionInfo();

	}
	public void disconnectWifi() {
		int netId = getNetworkId();
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
		mWifiInfo = null;
	}

	public boolean checkNetWorkState() {
		if (mWifiInfo != null) {
	
			return true;
		} else {
			return false;
		}
	}

	public boolean isWifiConnected(){
	        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        if(wifiNetworkInfo.isConnected())
	        {
	            return true ;
	        }
	     
	        return false ;
	    }
	public  boolean isNetworkAvailable() { 
		 
		if (connectivity == null) { 
			Log.i("NetWorkState", "Unavailabel"); 
			return false; 
		} else { 
			NetworkInfo[] info = connectivity.getAllNetworkInfo(); 
			if (info != null) { 
				for (int i = 0; i < info.length; i++) { 
					if (info[i].getState() == NetworkInfo.State.CONNECTED) { 
						Log.i("NetWorkState", "Availabel"); 
						return true; 
		} 
		} 
		} 
		} 
		return false; 
		} 
	public boolean isConnecting(){
        State wifi_State=connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(wifi_State==State.CONNECTING){
        	return true;
        }else if(wifi_State==State.CONNECTED){
        	return false; 
        }else{
        	return false; 
        }
	}
	public String isConnectSSID(){
		//int wifiState = mWifiManager.getWifiState();
		WifiInfo info = mWifiManager.getConnectionInfo();
		String wifiId = info != null ? info.getSSID() : null;
		String wifiIdConnect=wifiId.substring(1,wifiId.length()-1);
		return wifiIdConnect;
	}

	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	public int getIPAddress() {
		   
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}
	//将获取的int转为真正的ip地址,参考的网上的，修改了下  
	public String intToIp(int ipAddress) {
		if(ipAddress==0)return null;  
        return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."+(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
	} 
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	public void releaseWifiLock() {

		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	public List<WifiConfiguration> getConfiguration() {
		// 得到配置好的网络连接   
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
		return mWifiConfiguration;
	}
	
	//修改为返回boolean类型来判断是否连接成功，并加上连接硬件的部分
	//修改人：刘亦茜
	@SuppressWarnings("resource")
	public boolean connectConfiguration(int index) {
		
		if (index >= mWifiConfiguration.size()) {
			return false;
		}
		if(!mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,true)){
			return false;
		}
		ConnectDisk connectDisk=new ConnectDisk();
		connectDisk.start();
		while(!connectDisk.ifExitThread){			
		}
		return connectDisk.ifConnectionSuccessfully;
	}


	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
	}

	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}
	
	public int getRssi(){
		return  (mWifiInfo == null) ? -100 : mWifiInfo.getRssi();
	}
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}
	public boolean addNetWork(WifiConfiguration cfg){
		
		if(mWifiInfo != null){
			
			mWifiManager.disableNetwork(mWifiInfo.getNetworkId());
//			wm.disconnect();
		}
		
		boolean flag = false;
		
		
		if(cfg.networkId > 0){
			
			flag = mWifiManager.enableNetwork(cfg.networkId,true);
			
			mWifiManager.updateNetwork(cfg);
		}else{
			
			int netId = mWifiManager.addNetwork(cfg);
			
			if(netId > 0){
				mWifiManager.saveConfiguration();
				flag = mWifiManager.enableNetwork(netId, true);
			}
			else{
				
			}
		}
		
		return flag;
	}
	public WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		if(existingConfigs!=null){
			for (WifiConfiguration existingConfig : existingConfigs) {
				if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
					return existingConfig;
				}
			}
		}else{
			Toast.makeText(context,
					R.string.wifi_switch_closed,
					Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	public String IsExsitsSSID(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig.SSID;
			}
		}
		return null;
	}
	public WifiConfiguration CreateWifiInfo(String SSID, String Password,
			WifiCipherType Type) {
		WifiConfiguration wc= new WifiConfiguration();
		wc.allowedAuthAlgorithms.clear();
		wc.allowedGroupCiphers.clear();
		wc.allowedKeyManagement.clear();
		wc.allowedPairwiseCiphers.clear();
		wc.allowedProtocols.clear();
		wc.SSID = "\"" + SSID + "\"";
		if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
			//wc.wepKeys[0] = "";
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wc.wepTxKeyIndex = 0;
		} else if (Type == WifiCipherType.WIFICIPHER_WEP) {
			wc.wepKeys[0] = "\"" + Password + "\"";
			wc.hiddenSSID = true;

			wc.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wc.wepTxKeyIndex = 0;
			
		} else if (Type == WifiCipherType.WIFICIPHER_WPA) {
			wc.preSharedKey = "\"" + Password + "\"";
			wc.hiddenSSID = true;
			
			wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			
			wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA); // for WPA
			wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // for WPA2
	
		} else {
			return null;
		}
		return wc;
	}
	public WifiCipherType getWifiCipher(String capability){
		
		String cipher = getEncryptString(capability);
		
		if(cipher.contains("WEP")){
			
			return WifiCipherType.WIFICIPHER_WEP;
		}else if(cipher.contains("WPA") || cipher.contains("WPA2") || cipher.contains("WPS")){
			
			return WifiCipherType.WIFICIPHER_WPA;
		}else if(cipher.contains("unknow")){
			
			return WifiCipherType.WIFICIPHER_INVALID;
		}else{
			return WifiCipherType.WIFICIPHER_NOPASS;
		}
	}
	public String getEncryptString(String capability){
		
		
		StringBuilder sb = new StringBuilder();
		
		if(TextUtils.isEmpty(capability))
			return "unknow";
		
		if(capability.contains("WEP")){
			
			sb.append("WEP");
			
			return sb.toString();
		}
		
		if(capability.contains("WPA")){
			
			sb.append("WPA");
			
		}
		if(capability.contains("WPA2")){
			
			sb.append("/");
			
			sb.append("WPA2");
			
		}
		
		if(capability.contains("WPS")){
			
			sb.append("/");
			
			sb.append("WPS");
			
		}
		
		if(TextUtils.isEmpty(sb))
			return "OPEN";
		
		return sb.toString();
	}	
	private boolean OpenWifi() {
		boolean bRet = true;
		if (!mWifiManager.isWifiEnabled()) {
			bRet = mWifiManager.setWifiEnabled(true);
		}
		return bRet;
	}

	public boolean Connect(String SSID, String Password, WifiCipherType Type) {
		if (!this.OpenWifi()) {
			return false;
		}
			while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
			try {
				Thread.currentThread();
				Thread.sleep(100);
			} catch (InterruptedException ie) {
			}
		}

		WifiConfiguration wifiConfig = this
				.CreateWifiInfo(SSID, Password, Type);
		//
		if (wifiConfig == null) {
			return false;
		}
		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}
		int netID = mWifiManager.addNetwork(wifiConfig);
		
		mWifiManager.startScan();

		for (WifiConfiguration c0 : mWifiManager.getConfiguredNetworks()) {
			if (c0.networkId == netID) {
				return mWifiManager.enableNetwork(c0.networkId, true);
			} else {
				mWifiManager.enableNetwork(c0.networkId, false);
			}
		}
		boolean bRet = mWifiManager.enableNetwork(netID, true);
		mWifiManager.saveConfiguration();
		return bRet;
	}
class ConnectDisk extends Thread{
	protected boolean ifConnectionSuccessfully=false;
	protected boolean ifExitThread=false;
	public void run(){
		// ------------建立连接-------
		final SocketAddress address = new InetSocketAddress("192.168.2.8", 7);
		Socket socket = new Socket();
		// --------连接硬件---------
		try {
			socket.connect(address);
//			// ----------发送命令-------
//			OutputStream out = socket.getOutputStream();
//			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
//			StringBuffer sendMsg = new StringBuffer();
//			// sendMsg.append("HX 00000000 StartTF DY");
//			sendMsg.append("HX 00000000 ClNum DY");
//			writer.write(sendMsg.toString().trim());
//			writer.flush();
//			InputStream input=socket.getInputStream();
//			BufferedReader reader=new BufferedReader(new InputStreamReader(input));
//			String result;
//			while((result=reader.readLine())!=null){
//				if(result.contains("FF")){
//					msg.arg1=2;//设备被占用
//					msg.what = 0;
//				}
//			}
//			msg.arg1=1;//连接成功
    		XclSingalTransfer xclSingalTransfer=XclSingalTransfer.getInstance();
    		xclSingalTransfer.putValue("socket", socket);
    		ifConnectionSuccessfully=true;
		}catch(IOException e){
			ifConnectionSuccessfully=false;
		}
		ifExitThread=true;
	}
}
}
