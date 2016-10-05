package left.drawer;

import left.drawer.LanguagePopWindow.MyArrayAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import bottom.drawer.BottomOperate;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.WifiDialog;

public class PopWindow extends PopupWindow implements OnItemClickListener{
//	public View view = null;
	public Activity context = null;
	public ListView listView = null;
	private  final String[] connection_list= {"WIFI"};//,"USB","3G/4G"
	private  WifiDialog wifiDialog;
	private BottomOperate bottomOperate;
	private FragmentManager manager;
	public void setValues(WifiDialog wifiDialog, BottomOperate bottomOperate, FragmentManager manager) {
		this.wifiDialog = wifiDialog;
		this.bottomOperate=bottomOperate;
		this.manager=manager;
	}

	//private String[] list = null;
	public PopWindow(Activity context) {
		this.context = context;
		ViewGroup root = (ViewGroup) context.findViewById(android.R.id.content);
		View view = LayoutInflater.from(context).inflate(R.layout.left_popwindow, root,false);
		listView = (ListView) view.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		this.setContentView(view);	
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		//this.setBackgroundDrawable(new ColorDrawable(0x00000000));
		this.setBackgroundDrawable(new ColorDrawable(Color.CYAN));
		TypedArray typedArray = view.getContext().obtainStyledAttributes(R.styleable.myStyle);
		this.setBackgroundDrawable(typedArray.getDrawable(R.styleable.myStyle_input_box2));
		typedArray.recycle();
		this.update();
		InitListView(connection_list);
	}
	
	public void InitListView(String[] list){
		MyArrayAdapter adapter = new MyArrayAdapter(context, R.layout.con_pop_list_item);
		for(int i = 0; i < list.length; i ++){
			adapter.add(list[i]);
		}
        //浣峀istView璁剧疆Adapter
		listView.setAdapter(adapter);
		this.setHeight(dip2px(35)*list.length+dip2px(5));
	}
	public class MyArrayAdapter extends ArrayAdapter<String>{

		private int mResource;
		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			this.mResource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView)convertView.findViewById(R.id.textView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			TypedArray typedArray = convertView.getContext().obtainStyledAttributes(R.styleable.myStyle);//
			holder.text.setBackground(typedArray.getDrawable(R.styleable.myStyle_popuWindowItem));
			typedArray.recycle();
			holder.text.setText(connection_list[position]);
			holder.text.setTextColor(Color.WHITE);
			return convertView;
		}
	}
	
	public final class ViewHolder{
		public TextView text;
	}
	
	public void showPopupWindow(View v) {
		this.setWidth(v.getWidth());
		if (!this.isShowing()) {
			this.showAsDropDown(v, 0, -dip2px(55)-connection_list.length*dip2px(35));
		} else {
			this.dismiss();
		}
	}
	public int dip2px(float dpValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//((TextView)view).setText(list[position]);
		switch(position){
		case 0://wifi
			wifiDialog.ShowLeftWifiDialog(bottomOperate,manager);						
			break;
		case 1://USB
			
			break;
		case 2://3G/4G
			
			break;
		}
		this.dismiss();
	}
}
