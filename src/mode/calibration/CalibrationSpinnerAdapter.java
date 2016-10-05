package mode.calibration;

import com.example.mobileacquisition.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CalibrationSpinnerAdapter extends ArrayAdapter<String> {
  private Context mContext;
    private String [] mStringArray;
  public CalibrationSpinnerAdapter(Context context, String[] stringArray) {
    super(context, android.R.layout.simple_spinner_item, stringArray);
    mContext = context;
    mStringArray=stringArray;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    //修改Spinner展开后的字体颜色
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
    //  convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,false);
      convertView = inflater.inflate(R.layout.calibration_spiner_dropdown_layout, parent,false);
    }
    //此处text1是Spinner默认的用来显示文字的TextView
    TextView tv = (TextView) convertView.findViewById(R.id.textView);//android.R.id.text1
    tv.setText(mStringArray[position]);
    tv.setTextSize(16f);
    tv.setTextColor(Color.WHITE);

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
    tv.setTextSize(16f);
    tv.setTextColor(Color.parseColor("#465468"));
    return convertView;
  }
  public int dip2px(float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}