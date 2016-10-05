package preference.welcome;

import java.util.Locale;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.WelcomeActivity;
import com.example.mobileacquisition.WelcomeActivity.MyHandler;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class LanguageFragment extends Fragment implements OnClickListener{
	private  int Chinese = -11;
	private  int English = 11;
	private MyAPP mAPP = null;
	private MyHandler mHandler = null;
	private View view=null;
	LinearLayout chineseLayout,englishLayout,imageButton3Layout;
	TableLayout languageLayout;
	Fragment manager;
	SetDialog setDialog;
	private ImageView equipmentImageView;
	private TextView equipmentValue;
	private EquipmentFragment equipmentFragment;
	public EquipmentFragment getEquipmentFragment() {
		return equipmentFragment;
	}

	public void setEquipmentFragment(EquipmentFragment equipmentFragment) {
		this.equipmentFragment = equipmentFragment;
	}
	private SkinFragment skinFragment;
	public SkinFragment getSkinFragment() {
		return skinFragment;
	}

	public void setSkinFragment(SkinFragment skinFragment) {
		this.skinFragment = skinFragment;
	}

	//Spinner templateSelection;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.language, container,false);
		mAPP = (MyAPP) getActivity().getApplication();
		mHandler = (MyHandler) mAPP.getHandler();
		 manager =getParentFragment();
		setDialog=(SetDialog) manager.getFragmentManager().findFragmentByTag("setDialog");
//		setDialog.show(manager, "setDialog");
//		 setDialog = new SetDialog(getActivity(),fragmentList);
		chineseLayout = (LinearLayout) view.findViewById(R.id.chineseLayout);
		englishLayout = (LinearLayout) view.findViewById(R.id.englishLayout);
		imageButton3Layout = (LinearLayout) view.findViewById(R.id.imageButton3Layout);
		SharedPreferences language_preference = getActivity().getSharedPreferences("Language", 0);
		String language=language_preference.getString("language", "chinese");
		if(language.equals("english")){
			chineseLayout.setBackgroundResource(R.drawable.skin_selector);
			englishLayout.setBackgroundResource(R.drawable.skin_checked_selector);
		}else if(language.equals("chinese")){
			chineseLayout.setBackgroundResource(R.drawable.skin_checked_selector);
			englishLayout.setBackgroundResource(R.drawable.skin_selector);
		}
		chineseLayout.setOnClickListener(this);
		englishLayout.setOnClickListener(this);
		equipmentImageView=(ImageView)getActivity().findViewById(R.id.equipmentImageView);
		equipmentValue=(TextView)getActivity().findViewById(R.id.equipment_value);
		return view;
	}

	/*
	 * �����л�
	 */
	public void updateLange(Locale locale){    	
		 Resources res = getResources();
		 Configuration config = res.getConfiguration();
		 config.locale = locale;
		 DisplayMetrics dm = res.getDisplayMetrics();
		 res.updateConfiguration(config, dm);           
		 languageChanged();  // ˢ�®�ǰ���
		 
	}
	
	 private void languageChanged() {
		 ((TextView)view.findViewById(R.id.English_tx)).setText(R.string.English);
		 ((TextView)view.findViewById(R.id.German_tx)).setText(R.string.German);
		 ((TextView)view.findViewById(R.id.French_tx)).setText(R.string.French);
		 ((TextView)view.findViewById(R.id.Chinese_tx)).setText(R.string.Chinese);
		 ((TextView)view.findViewById(R.id.Russian_tx)).setText(R.string.Russian);
		 ((TextView)view.findViewById(R.id.Japanese_tx)).setText(R.string.Japanese);
		 ((TextView)view.findViewById(R.id.Korean_tx)).setText(R.string.Korean);
		 ((WelcomeActivity)getActivity()).languageChanged();
		 equipmentFragment.connect_message.setText(R.string.Connected_to);
		 equipmentFragment.default_device.setText(R.string.Default);
		 equipmentFragment.front_end.setText(R.string.Front_end);
		 equipmentFragment.main_Configuration.setText(R.string.Main_Configuration);
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
				if (i == 0) { 
					textview.setBackgroundResource(R.drawable.tab_l_n);
					setDialog.tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
				} else if (i == 2) {
					textview.setBackgroundResource(R.drawable.tab_r_n);
					setDialog.tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
				} else {
					textview.setBackgroundResource(R.drawable.tab_m_f);
					setDialog.tabList.get(i).setTextColor(Color.rgb(44, 82, 134));
				}
		}
		setDialog.dialogPager.setAdapter(setDialog.adapter); 
		setDialog.dialogPager.setCurrentItem(1);
		skinFragment.languageChange();
		}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences skin_preference = getActivity().getSharedPreferences("Language", 0);
		SharedPreferences.Editor editor = skin_preference.edit();
		SharedPreferences preference = getActivity().getSharedPreferences("Equipment", 0);
		int Equipment_Num=preference.getInt("Equipment", 0);
		switch(v.getId()){
		case R.id.chineseLayout:
			 chineseLayout.setBackgroundResource(R.drawable.skin_checked_selector);
			 englishLayout.setBackgroundResource(R.drawable.skin_selector);
			 editor.putString("language", "chinese");
	    	 editor.commit();
	    	 mHandler.sendEmptyMessage(Chinese);
	   		 updateLange(Locale.SIMPLIFIED_CHINESE);
	   	     chineseLayout.setBackgroundResource(R.drawable.skin_checked_selector);
	   	     englishLayout.setBackgroundResource(R.drawable.skin_selector); 
			 switch(Equipment_Num){
				case 0:
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_2);
					equipmentValue.setText(R.string.NO);
					break;
				case 1:
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_3);
					equipmentValue.setText(R.string.This_machine);//����
					break;
				case 2:
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_1);
					break;
			 }
			break;
		case R.id.englishLayout:
			 englishLayout.setBackgroundResource(R.drawable.skin_checked_selector);
			 chineseLayout.setBackgroundResource(R.drawable.skin_selector);
			 editor.putString("language", "english");
	    	 editor.commit();
	    	 mHandler.sendEmptyMessage(English);
	   		 updateLange(Locale.ENGLISH);
	   		 englishLayout.setBackgroundResource(R.drawable.skin_checked_selector);
	   		 chineseLayout.setBackgroundResource(R.drawable.skin_selector);
	   		 switch(Equipment_Num){
				case 0:
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_2_en);
					equipmentValue.setText(R.string.NO);
					break;
				case 1:
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_3_en);
					equipmentValue.setText(R.string.This_machine);//����
					break;
				case 2:
					equipmentImageView.setBackgroundResource(R.drawable.ico_01_1);
					break;
	   		 }
			break;
		}
	}

}
