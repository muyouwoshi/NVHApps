package left.drawer;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MySpinnerAdapter extends ArrayAdapter<String> {
  private Context mContext;
    private String [] mStringArray;
  public MySpinnerAdapter(Context context, String[] stringArray) {
    super(context, android.R.layout.simple_spinner_item, stringArray);
    mContext = context;
    mStringArray=stringArray;
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
    tv.setText(mStringArray[position]);
    tv.setTextSize(14f);
   // tv.setTextColor(Color.WHITE);

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
    tv.setText(mStringArray[position]);
    tv.setGravity(Gravity.CENTER);
    tv.setTextSize(14f);
   // tv.setTextColor(Color.WHITE);
    if(ThemeLogic.themeType==1){
		 tv.setTextColor(mContext.getResources().getColor(R.color.white4));
	}else{
		
		tv.setTextColor(mContext.getResources().getColor(R.color.black));
	}
    return convertView;
  }

}