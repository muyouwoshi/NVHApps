package draw.map;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import bottom.drawer.AddChannelViewPager;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;

import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;

public class RPM_Tachometer extends RelativeLayout {
	private View view;
	public RPM_Tachometer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		view = LayoutInflater.from(context).inflate(R.layout.rpm_tachometer,
				this, true);
	}
}
