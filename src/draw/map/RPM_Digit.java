package draw.map;

import com.example.mobileacquisition.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class RPM_Digit extends RelativeLayout {
private View view;
	public RPM_Digit(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		view = LayoutInflater.from(context).inflate(R.layout.rpm_digit,
				this, true);
	}
	
}
