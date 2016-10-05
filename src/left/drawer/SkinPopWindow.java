package left.drawer;

import java.util.ArrayList;

import left.drawer.LanguagePopWindow.MyArrayAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

public class SkinPopWindow extends PopupWindow implements OnItemClickListener{
	//public View view = null;
	public  Activity context = null;
	public ListView listView = null;
	private String[] list = null;
	//private ArrayList<ViewHolder> viewHolders = new ArrayList<ViewHolder>();
	private String[] skin = {"Black","Red"};//,"Blue","Green"
	private final  int[]skin_ico={R.drawable.skin1_corners_bg,R.drawable.skin2_corners_bg}; 
	public  ArrayList<RelativeLayout> Layout_list = new ArrayList<RelativeLayout>();
	private int index=0;
	public SkinPopWindow(Activity context) {
		//this.view = v;
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
		InitListView(skin);
		SharedPreferences skin_preference = context.getSharedPreferences("Skin", 0);
		String skin=skin_preference.getString("skin", "skin1");
		if(skin.equals("skin1")){
			index=0;
		}else if(skin.equals("skin2")){
			index=1;
		}
	}
	
	public void InitListView(String[] list){
		this.list = list;
		MyArrayAdapter adapter = new MyArrayAdapter(context, R.layout.skin_pop_list_item);
		for(int i = 0; i < list.length; i ++){
			adapter.add(list[i]);
		}
        //浣峀istView璁剧疆Adapter
		listView.setAdapter(adapter);
		//this.setHeight(dip2px(48)*list.length);//+dip2px(20)
		this.setHeight(dip2px(45)*list.length+dip2px(5));//+dip2px(20)
	}
	
	public class MyArrayAdapter extends ArrayAdapter<String>{

		private int mResource;
		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			this.mResource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView)convertView.findViewById(R.id.textView);
				holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
				holder.popuwindow_layout = (RelativeLayout)convertView.findViewById(R.id.popuwindow_layout);
				//viewHolders.add(holder);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			TypedArray typedArray = convertView.getContext().obtainStyledAttributes(R.styleable.myStyle);
			holder.popuwindow_layout.setBackground(typedArray.getDrawable(R.styleable.myStyle_popuWindowItem));
			typedArray.recycle();
			holder.text.setText("");//list[position]
			holder.text.setBackgroundResource(skin_ico[position]);
			holder.text.setGravity(Gravity.CENTER);
			holder.text.setTextColor(Color.WHITE);
			//viewHolders.get(index).layout.setVisibility(View.VISIBLE);
			Layout_list.add(holder.layout);
			if(Layout_list.size()>index){
				if(position==index){
					Layout_list.get(index).setVisibility(View.VISIBLE);
				}else{
					Layout_list.get(position).setVisibility(View.GONE);
				}
			}
			return convertView;
		}
	}
	public final class ViewHolder{
		public TextView text;
		public RelativeLayout layout;
		public RelativeLayout popuwindow_layout;
	}
	
	public void showPopupWindow(View v) {
		this.setWidth(v.getWidth());
		if (!this.isShowing()) {
			this.showAsDropDown(v, 0, -dip2px(55)-skin_ico.length*dip2px(45));
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
		SharedPreferences skin_preference = context.getSharedPreferences("Skin", 0);
		SharedPreferences.Editor editor = skin_preference.edit();
		switch(position){
		case 0:
			ThemeLogic.themeType = 1; 
			ThemeLogic.getInstance().notifiyChange();
			editor.putString("skin", "skin1");
	    	editor.commit();
			break;
		case 1:
			ThemeLogic.themeType = 2;
			ThemeLogic.getInstance().notifiyChange();
			editor.putString("skin", "skin2");
	    	editor.commit();
			break;
		case 2:
			
			break;
		}
		for(int i=0;i<Layout_list.size();i++){
			if(Layout_list.get(i).equals(position)){
				Layout_list.get(i).setVisibility(View.VISIBLE);
			}else{
				Layout_list.get(i).setVisibility(View.GONE);
			}
		}
		 index=position;
		this.dismiss();
	}
}
