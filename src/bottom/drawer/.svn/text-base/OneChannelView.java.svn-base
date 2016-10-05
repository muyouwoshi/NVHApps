package bottom.drawer;


import java.util.ArrayList;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class OneChannelView extends RelativeLayout{
private View view;
private CustomImageButton channel1;
	public OneChannelView(Context context,ArrayList<CustomImageButton> chanButtonList) {
		super(context);
		// TODO Auto-generated constructor stub
		view = LayoutInflater.from(context).inflate(
				R.layout.channel_pager1, null);
		addView(view, new RelativeLayout.LayoutParams(-1,-1));
		channel1=(CustomImageButton)view.findViewById(R.id.channel1);
//		if(ThemeLogic.themeType==1){
//			channel1.setBackgroundResource(R.drawable.ico_channel);
//		}else{
//			channel1.setBackgroundResource(R.drawable.ico_channel_noactive_jiashi1);
//		}
		
		chanButtonList.add(channel1);
		for(int i=0;i<chanButtonList.size();i++){
			channel1.setTag(i);
		}
	}

}
