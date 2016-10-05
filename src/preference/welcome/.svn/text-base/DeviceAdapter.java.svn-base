package preference.welcome;
import java.util.List;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.WifiConfiguretion;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter {

	private List<String> list;
	private List<String> list_message;
	// 布局加载器
	private LayoutInflater mInflater;
	// 上下文
	private Activity context;
	// 布局缓存对象
	private ViewHolder holder;
	private WifiConfiguretion wifiConfiguretion;
	private TextView connect_wifi;
	public TextView getConnect_wifi() {
		return connect_wifi;
	}

	public void setConnect_wifi(TextView connect_wifi) {
		this.connect_wifi = connect_wifi;
	}
	public DeviceAdapter(Context context,List<String> list,WifiConfiguretion wifiConfiguretion,List<String> list_message) {//String connectedSSID
		super();
		this.list = list;
		this.list_message=list_message;
		this.context = (Activity)context;
		//this.connectedSSID=connectedSSID;
		this.wifiConfiguretion=wifiConfiguretion;
		mInflater = LayoutInflater.from(this.context);
		
	}

	@Override
	public int getCount() {
		return null == list ? 0 : list.size();
	}

	@Override
	public String getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			notifyDataSetChanged();
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.itemImage = (CheckBox) convertView.findViewById(R.id.ItemImage);
			holder.itemTitle=(TextView) convertView.findViewById(R.id.ItemTitle);
			holder.main_Configuration=(TextView) convertView.findViewById(R.id.Main_Configuration);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.itemTitle.setText(getItem(position));
		holder.main_Configuration.setText(list_message.get(position));
		wifiConfiguretion.flushWifiInfo();
		if(wifiConfiguretion.isNetworkAvailable()&&wifiConfiguretion.isConnectSSID()!=null
				&&wifiConfiguretion.isConnectSSID().equals(holder.itemTitle.getText())){
			//holder.itemImage.setVisibility(View.VISIBLE);
			connect_wifi.setText(wifiConfiguretion.isConnectSSID());
		}else{
		//	holder.itemImage.setVisibility(View.GONE);
		}
		SharedPreferences default_device_preference = context.getSharedPreferences("default_device", 0);
		String default_device_SSID=default_device_preference.getString("WifiSSID", "");
		if(wifiConfiguretion.isOpen()&&default_device_SSID!=null&&default_device_SSID.equals(holder.itemTitle.getText())){
			holder.itemImage.setButtonDrawable(R.drawable.checked_active);
		}else{
			holder.itemImage.setButtonDrawable(R.drawable.checked);
		}
		return convertView;
	}

	class ViewHolder {
		CheckBox itemImage;
		TextView itemTitle;
		TextView main_Configuration;
	}

}