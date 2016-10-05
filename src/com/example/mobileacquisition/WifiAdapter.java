package com.example.mobileacquisition;
import java.util.List;
import com.example.mobileacquisition.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiAdapter extends BaseAdapter {

	private List<String> list;
	// 布局加载器
	private LayoutInflater mInflater;
	// 上下文
	private Activity context;
	// 布局缓存对象
	private ViewHolder holder;
	private String connectedSSID;
	private WifiConfiguretion wifiConfiguretion;
	private boolean isconnect;
	public boolean isIsconnect() {
		return isconnect;
	}

	public void setIsconnect(boolean isconnect) {
		this.isconnect = isconnect;
	}

	public WifiAdapter(Context context,List<String> list,WifiConfiguretion wifiConfiguretion) {//String connectedSSID
		super();
		this.list = list;
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
			convertView = mInflater.inflate(R.layout.wifi_list_item, null);
			holder = new ViewHolder();
			holder.itemImage = (ImageView) convertView.findViewById(R.id.ItemImage);
			holder.itemTitle=(TextView) convertView.findViewById(R.id.ItemTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.itemTitle.setText(getItem(position));
		wifiConfiguretion.flushWifiInfo();
		if(wifiConfiguretion.isNetworkAvailable()&&wifiConfiguretion.isConnectSSID()!=null
				&&wifiConfiguretion.isConnectSSID().equals(holder.itemTitle.getText())){
			holder.itemImage.setVisibility(View.VISIBLE);
			isconnect=true;
		}else{
			holder.itemImage.setVisibility(View.GONE);
			isconnect=false;
		}
		return convertView;
	}

	class ViewHolder {
		ImageView itemImage;
		TextView itemTitle;
	}

}