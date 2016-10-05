package left.drawer;
import java.util.ArrayList;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.R.color;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerArrayAdapter extends ArrayAdapter {
	private Context mContext;
    private ArrayList<String> list = new ArrayList<String>();
	public CustomSpinnerArrayAdapter(Context context, ArrayList<String> list) {
		super(context,android.R.layout.simple_spinner_item,list);
		mContext = context;
		this.list=list;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		//修改Spinner展开后的字体颜色
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,false);
		}

		//此处text1是Spinner默认的用来显示文字的TextView
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(list.get(position));
		tv.setTextSize(14f);
		return convertView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 修改Spinner选择后结果的字体颜色
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
		}

		//此处text1是Spinner默认的用来显示文字的TextView
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		if(list.size()-1<position){//此处会出现list内空无一物的情况，暂时做此修改 by刘亦茜
			tv.setText(0);		
		}else{
			tv.setText(list.get(position));
		}
		//tv.setTextSize(18f);
		 if(ThemeLogic.themeType==1){
			 tv.setTextColor(mContext.getResources().getColor(R.color.white4));
		}else{
			
			tv.setTextColor(mContext.getResources().getColor(R.color.black));
		}
		//tv.setTextColor(mContext.getResources().getColor(R.color.white4));
		tv.setGravity(Gravity.CENTER);
		//tv.setTextColor(Color.BLUE);
		tv.setTextSize(14f);
		return convertView;
	}

}