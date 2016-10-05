package preference.welcome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.WelcomeActivity;
import com.example.mobileacquisition.WifiConfiguretion;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EquipmentFragment extends Fragment implements OnClickListener,OnItemClickListener{
	private View view=null;
	private ListView equipmentListView;
	private List<ScanResult> mwifiResult = new ArrayList<ScanResult>();
	private List<String> isExsitsSSID = new ArrayList<String>();
	private List<String> message = new ArrayList<String>();
	private WifiConfiguretion wifiConfiguretion;
	private String wifiSSID;//wifi名
	private ProgressDialog progressDialog = null;
	private TextView equipmentValue;//,signalValue,signalStrength;
	private ImageView equipmentImageView;
	private DeviceAdapter listItemAdapter; 
	private SharedPreferences.Editor Equipment_editor;
	public TextView connect_wifi,connect_message,default_device,front_end,main_Configuration;
	private RelativeLayout preferenceLayout;
	private AlertDialog preferenceDialog;
	private LayoutInflater inflater;
	private SharedPreferences NotShown_preference;
	private SharedPreferences.Editor NotShown_editor;
	private boolean notShown=false;//不再询问标记
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.equipment, container,false);
		connect_wifi=(TextView)view.findViewById(R.id.connect_wifi);
		connect_message=(TextView)view.findViewById(R.id.connect_message);
		default_device=(TextView)view.findViewById(R.id.Default);
		front_end=(TextView)view.findViewById(R.id.Front_end);
		main_Configuration=(TextView)view.findViewById(R.id.Main_Configuration);
		equipmentListView=(ListView) view.findViewById(R.id.equipment_view);
		wifiConfiguretion=new WifiConfiguretion(getActivity());
		if(wifiConfiguretion.isOpen()){
			mwifiResult = wifiConfiguretion.scan();//获取附近wifi热点
		}
		String WifiItem[] = new String[mwifiResult.size()];
		for (int i = 0; i < mwifiResult.size(); i++) {
			ScanResult Wifi = mwifiResult.get(i);
			WifiItem[i]=wifiConfiguretion.IsExsitsSSID(Wifi.SSID);//Wifi.SSID
		}
		/*
		 * 除去SSID中的""
		 */
		for(int j=0;j<WifiItem.length;j++){
			if(WifiItem[j]!=null){
				isExsitsSSID.add(WifiItem[j].substring(1,WifiItem[j].length()-1));
			}
		}
		HashSet<String> h = new  HashSet<String>(isExsitsSSID);     
		isExsitsSSID.clear();     
		isExsitsSSID.addAll(h); 
		for(int i=0;i<isExsitsSSID.size();i++){
			message.add("Message"+i);
		}
		listItemAdapter=new DeviceAdapter(getActivity(),isExsitsSSID,wifiConfiguretion,message);
		listItemAdapter.setConnect_wifi(connect_wifi);
		listItemAdapter.notifyDataSetChanged();
		equipmentListView.setAdapter(listItemAdapter);
		equipmentListView.setOnItemClickListener(this);
		//signalStrength=(TextView)getActivity().findViewById(R.id.signal_strength);
		equipmentImageView=(ImageView)getActivity().findViewById(R.id.equipmentImageView);
		equipmentValue=(TextView)getActivity().findViewById(R.id.equipment_value);
		//signalValue=(TextView)getActivity().findViewById(R.id.signal_value);
		SharedPreferences preference = getActivity().getSharedPreferences("Equipment", 0);
		Equipment_editor = preference.edit();
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		/*switch(v.getId()){
		case R.id.Back_Ai:
			
			break;
		}*/
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		wifiSSID = isExsitsSSID.get(arg2);
		/*Toast.makeText(this.getActivity(),
				String.valueOf(isExsitsSSID.get(arg2)), Toast.LENGTH_SHORT)
				.show();*/
			progressDialog=ProgressDialog.show(getActivity(),"", getActivity().getResources().getString(R.string.In_connecting));
			wifiConfiguretion.mWifiManager.enableNetwork(wifiConfiguretion.IsExsits(wifiSSID).networkId, true);
			new EquipmentThread().start();
			
	//	}
	}
	/** 
	 * 显示设备信息	
	 */
		class EquipmentThread extends Thread{  
	        @Override  
	        public void run(){   
	                try{  
	                    Thread.sleep(5000);  
	                    Message msg = new Message();  
	                    	if(wifiConfiguretion.isNetworkAvailable()){
	                    		msg.what = 1;//what，int类型，未定义的消息，以便接收消息者可以鉴定消息是关于什么的。每个句柄都有自己的消息命名空间，不必担心冲突   
	                    	}else{
	                    		msg.what = 0;//未连接
	                    	}
	                    mHandler.sendMessage(msg);  
	                }  
	                catch (InterruptedException e){  
	                    e.printStackTrace();  
	                }     
	        }  
	    } 
		@SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler(){  
	        @SuppressLint("HandlerLeak")
			@Override  
	        public void handleMessage(Message msg) {    
	            super.handleMessage(msg);  
	            wifiConfiguretion.flushWifiInfo();
            	String str_SSID=wifiConfiguretion.getSSID();
            	String equipment_Name=str_SSID.substring(1,str_SSID.length()-1);
            	switch(msg.what){  
	            case 0:
	            	progressDialog.dismiss();
	            	equipmentValue.setText(R.string.NO);
	            	//signalStrength.setVisibility(View.GONE);
	            //	signalStrength.setText("");
	            	//signalValue.setText("");
	            //	equipmentImageView.setBackgroundResource(R.drawable.ico_01_2);
	            	if(WelcomeActivity.languageChangedId==-11){
    					equipmentImageView.setBackgroundResource(R.drawable.ico_01_2);
    				}else if(WelcomeActivity.languageChangedId==11){
    					equipmentImageView.setBackgroundResource(R.drawable.ico_01_2_en);
    				}
	            	Equipment_editor.remove("Equipment");
 					Equipment_editor.putInt("Equipment", 0);
 					Equipment_editor.commit();
	            	Toast.makeText(getActivity().getApplicationContext(),
	            			R.string.Not_Connected, Toast.LENGTH_SHORT)//未连接到wifi
							.show();
	            	break;
	            case 1:
	            	progressDialog.dismiss();
	            	//123
	            	//signalStrength.setVisibility(View.VISIBLE);
	            	//signalStrength.setText(R.string.signal_strength);
	        		equipmentValue.setText(equipment_Name);
            		//signalValue.setText(signal_Strength);
            		//equipmentImageView.setBackgroundResource(R.drawable.ico_01_1);
	        		((WelcomeActivity) getActivity()).obtainWifiInfo();
            		Equipment_editor.remove("Equipment");
           		    Equipment_editor.putInt("Equipment", 2);
           		    Equipment_editor.commit();
	            	Toast.makeText(getActivity().getApplicationContext(),
	            				 equipment_Name, Toast.LENGTH_SHORT)//"已连接"//+R.string.Connected
	            				 .show();
	            	if(!notShown){//没有选中不再询问CheckBox时弹出是否设为默认设备的弹出框
						ShowPreferenceDialog();
					}
	                break;  
	            default:  
	                break;  
	            }  
	            listItemAdapter.notifyDataSetChanged();
	        }  
	    };
	    
	    /** 
		 * 是否设为默认设备弹框
		 */
		@SuppressLint("InflateParams")
		public void ShowPreferenceDialog(){////显示询问是否设为默认设备的弹出框
			inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// 1. 布局文件转换为View对象
			preferenceLayout = (RelativeLayout) inflater.inflate(R.layout.preference_dialog, null);
			// 2. 新建对话框对象
			preferenceDialog = new AlertDialog.Builder(getActivity()).create();
			//preferenceDialog.setCancelable(false);
			preferenceDialog.show();
			preferenceDialog.getWindow().setContentView(preferenceLayout);
			preferenceDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			// 4. 确定按钮
			Button btnOK = (Button) preferenceLayout.findViewById(R.id.dialog_ok);
			btnOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Toast.makeText(context.getApplicationContext(),
							wifiSSID, Toast.LENGTH_SHORT)
							.show();*/
					SharedPreferences preference = getActivity().getSharedPreferences("default_device", 0);
					SharedPreferences.Editor wifiSSID_editor = preference.edit();
					wifiSSID_editor.putString("WifiSSID", wifiSSID);
					wifiSSID_editor.commit();
					preferenceDialog.dismiss();
					listItemAdapter.notifyDataSetChanged();
				}
			});
			// 5. 取消按钮
			Button btnCancel = (Button) preferenceLayout.findViewById(R.id.dialog_cancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					preferenceDialog.dismiss();
				}
			});
			//不再询问
			CheckBox checkNotShown=(CheckBox) preferenceLayout.findViewById(R.id.not_shown);
			checkNotShown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
		            @Override 
		            public void onCheckedChanged(CompoundButton buttonView, 
		                    boolean isChecked) { 
		                // TODO Auto-generated method stub 
		            	NotShown_preference = getActivity().getSharedPreferences("NotShown", 0);
		            	NotShown_editor = NotShown_preference.edit();
		            	notShown=NotShown_preference.getBoolean("isShown", false);
		                if(isChecked){
		                	NotShown_editor.putBoolean("isShown", true);
		                	NotShown_editor.commit();
		                	//notShown=true;
		                }else{
		                	NotShown_editor.putBoolean("isShown", false);
		                	NotShown_editor.commit();
		                	//notShown=false;
		                } 
		               
		            } 
		        }); 
		}
}
