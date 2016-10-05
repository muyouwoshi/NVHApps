package mode.drive;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.mobileacquisition.R;

public class Drive_Dialog implements OnClickListener{

	private RelativeLayout rpm_layout = null;
	private RelativeLayout rpm_table = null;
	private RelativeLayout child = null;
	private float scale;
	private TextView textView1,textView2;
	private RelativeLayout table_layout;
	
	public Drive_Dialog(Activity context) {
		// TODO Auto-generated constructor stub
		rpm_table=(RelativeLayout) context.findViewById(R.id.rpm_table);
		rpm_layout = (RelativeLayout) context.findViewById(R.id.rpm);
		table_layout=(RelativeLayout) context.findViewById(R.id.table_layout);
		child = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.drive_dialog, rpm_table, false);
		scale = context.getResources().getDisplayMetrics().density;
		LayoutParams params = new LayoutParams(-2,-2);
		params.height = dip2px(180);
		params.width= dip2px(180);
		params.addRule(RelativeLayout.CENTER_VERTICAL, -1);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, -1);
		rpm_table.addView(child, params);
		child.setVisibility(View.GONE);
		textView1=(TextView)child.findViewById(R.id.textView1);
		textView1.setOnClickListener(this);
		textView2=(TextView)child.findViewById(R.id.textView2);
		textView2.setOnClickListener(this);
	}
	
	public int dip2px(float dpValue) {
		return (int)(dpValue * scale +0.5f);
	}
	
	public void setVisibility(Boolean visible) {
		child.setVisibility(View.VISIBLE);
	}
	
	public Boolean getVisibility() {
		return child.getVisibility() == 0 ? true : false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.textView1:
			rpm_layout.setVisibility(View.VISIBLE);
			table_layout.setVisibility(View.GONE);
			child.setVisibility(View.GONE);
			break;
		case R.id.textView2:
			rpm_layout.setVisibility(View.GONE);
			table_layout.setVisibility(View.VISIBLE);
			child.setVisibility(View.GONE);
			break;
		}
	}	
}
