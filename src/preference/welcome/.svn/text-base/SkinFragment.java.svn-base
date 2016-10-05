package preference.welcome;
import com.example.mobileacquisition.WelcomeActivity.MyHandler;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SkinFragment extends Fragment implements OnClickListener{
	private final int Skin0 = -1;
	private final int Skin1 = 1;
	private MyAPP mAPP = null;
	private MyHandler mHandler = null;
	private View view=null;
	Fragment manager;
	SetDialog setDialog;
	private LinearLayout Layout_skin1,Layout_skin2;
//	private CustomTab customTab;
/*	public SkinFragment(CustomTab customTab) {
		// TODO Auto-generated constructor stub
		this.customTab=customTab;
		
	}*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		view = inflater.inflate(R.layout.skin, container,false);
		
		mAPP = (MyAPP) getActivity().getApplication();
		mHandler = (MyHandler) mAPP.getHandler();
		Layout_skin1=(LinearLayout)view.findViewById(R.id.Layout_skin1);
		Layout_skin2=(LinearLayout)view.findViewById(R.id.Layout_skin2);
		view.findViewById(R.id.Layout_skin1).setOnClickListener(this);
		view.findViewById(R.id.Layout_skin2).setOnClickListener(this);
		SharedPreferences skin_preference = getActivity().getSharedPreferences("Skin", 0);
		String skin=skin_preference.getString("skin", "skin1");
		if(skin.equals("skin1")){
			Layout_skin1.setBackgroundResource(R.drawable.skin_checked_selector);
			Layout_skin2.setBackgroundResource(R.drawable.skin_selector);
		}else if(skin.equals("skin2")){
			Layout_skin2.setBackgroundResource(R.drawable.skin_checked_selector);
			Layout_skin1.setBackgroundResource(R.drawable.skin_selector);
		}
		 manager =getParentFragment();
		setDialog=(SetDialog) manager.getFragmentManager().findFragmentByTag("setDialog");
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences skin_preference = getActivity().getSharedPreferences("Skin", 0);
		SharedPreferences.Editor editor = skin_preference.edit();
		switch(v.getId()){
		case R.id.Layout_skin1:
			ThemeLogic.themeType = 1; 
			ThemeLogic.getInstance().notifiyChange();
			Layout_skin1.setBackgroundResource(R.drawable.skin_checked_selector);
			Layout_skin2.setBackgroundResource(R.drawable.skin_selector);
			editor.putString("skin", "skin1");
	    	editor.commit();
			mHandler.sendEmptyMessage(Skin0);
			skinChange();
		//	customTab.setBackgroundResource(R.drawable.main_bg);
			break;
		case R.id.Layout_skin2:
			ThemeLogic.themeType = 2;
			ThemeLogic.getInstance().notifiyChange();
			Layout_skin2.setBackgroundResource(R.drawable.skin_checked_selector);
			Layout_skin1.setBackgroundResource(R.drawable.skin_selector);
			editor.putString("skin", "skin2");
	    	editor.commit();
			mHandler.sendEmptyMessage(Skin1);
			skinChange();
			//customTab.setBackgroundColor(Color.CYAN);
			break;
		}
	}
	public void languageChange(){
		TextView skin1_text=(TextView)view.findViewById(R.id.skin1_text);
		skin1_text.setText(R.string.skin1_tx);
		TextView skin2_text=(TextView)view.findViewById(R.id.skin2_text);
		skin2_text.setText(R.string.skin2_tx);
	}
	private void skinChange() {
		 setDialog.nameList.clear();
		 setDialog.tabLayout.removeAllViews();
	 	 if( setDialog.tabList.size()!=0){
	 		setDialog.tabList.clear();
	 	 }
	 	setDialog.nameList.add(getActivity().getResources().getString(R.string.Device));
	 	setDialog.nameList.add(getActivity().getResources().getString(R.string.Language));
	 	setDialog.nameList.add(getActivity().getResources().getString(R.string.Skin));
		for (int i = 0; i < setDialog.nameList.size(); i ++) {
			TextView textview = setDialog.newTabs(setDialog.nameList.get(i),i);
			setDialog.tabLayout.addView(textview);
			if(ThemeLogic.themeType==1){
				if (i == 0) { 
					textview.setBackgroundResource(R.drawable.tab_l_n);
					setDialog.tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
				} else if (i == 2) {
					textview.setBackgroundResource(R.drawable.tab_r_f);
					setDialog.tabList.get(i).setTextColor(Color.rgb(44, 82, 134));
				} else {
					textview.setBackgroundResource(R.drawable.tab_m_n);
					setDialog.tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
				}
			}else{
				if (i == 0) { 	
					textview.setBackgroundResource(R.drawable.tab_l_n1);
					setDialog.tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
				} else if (i == 2) {
					textview.setBackgroundResource(R.drawable.tab_r_f1);
					setDialog.tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
				} else {
					textview.setBackgroundResource(R.drawable.tab_m_n1);
					setDialog.tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
				}
				
			}
		}
		setDialog.dialogPager.setAdapter(setDialog.adapter); 
		setDialog.dialogPager.setCurrentItem(2);
		
	}
}
