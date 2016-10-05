package left.drawer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import left.drawer.AnalogFragment.ViewHolder;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import common.CustomTab;

public class ChannelSettingFragment extends Fragment implements Serializable{
	private static final long serialVersionUID = 3408152703654375517L;
	public ArrayList<Fragment> fragments = null;
	private FragmentManager managerChild,managerFather;
	private AnalogFragment analog;
	public DigitalFragment digital;
//	private CanFragment can;
	public CustomTab customTab;
	public ArrayList<String> tabs;
	private ChannelSettingFragment channelSettingFragment;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.channel_setting, container, false);
		customTab = (CustomTab) view.findViewById(R.id.ChannelSetting);
		managerFather=getFragmentManager();
		getActivity().getIntent().getStringExtra("LanguageIntent");
	
		add();
		return view;
	}
	public void add(){
	    tabs = new ArrayList<String>();
		tabs.add(getActivity().getResources().getString(R.string.channel_setting));
		tabs.add(getActivity().getResources().getString(R.string.Tacho));
		customTab.setTabs(tabs);
		managerChild = getChildFragmentManager();
		fragments = new ArrayList<Fragment>();
		analog = new AnalogFragment();
		digital = new DigitalFragment();
		digital.readFromXml(getActivity());
		//can = new CanFragment();
		fragments.add(analog);
		fragments.add(digital);
		//fragments.add(can);
		customTab.setup(managerChild, fragments);
		channelSettingFragment=(ChannelSettingFragment)managerFather.findFragmentByTag("channelSetting");
		customTab.returnButton(this.getActivity(),channelSettingFragment);//,false
	}
	
	public CustomTab getCustomTab() {
		return customTab;
	}
	public  Map<Integer,ViewHolder> getViewHolders(){
		return analog.getCheckedViewMap();
	}
	public AnalogFragment getAnalog(){
		return analog;
	}
	public DigitalFragment getDigital() {
		return digital;
	}
	/*public CanFragment getCan() {
		return can;
	}*/
	public void languageChanged(){
		tabs.clear();
		tabs.add(getActivity().getResources().getString(R.string.channel_setting));
		tabs.add(getActivity().getResources().getString(R.string.Tacho));
		customTab.setTabs(tabs);
		digital.languageChanged();
		analog.languageChanged();
	}
	public void skinChanged(TypedArray typedArray){
		int pageNum =customTab.getPageNo();
		for (int i = 0; i <customTab.tabList.size(); i++) {
			if(ThemeLogic.themeType==1){
				customTab.tabList.get(i).setTextColor(
						i == pageNum ? Color.rgb(44, 82, 134) : Color.rgb(255, 255, 255));
			}else{
				customTab.tabList.get(i).setTextColor(
						i == pageNum ? Color.rgb(255, 255, 255) : Color.rgb(255, 255, 255));
				
			}
		}
		for (int i = 0; i < customTab.tabList.size(); i++) {
			if(ThemeLogic.themeType==1){
				if ( customTab.tabList.size() == 1) {
					 customTab.tabList.get(0).setBackgroundResource(R.drawable.tab_bg_n);
					 customTab.tabList.get(0).setTextColor(Color.rgb(255, 255, 255));
				}else{
					if (i == 0) {
						customTab.tabList.get(i).setBackgroundResource(
								i == pageNum ? R.drawable.tab_l_f : R.drawable.tab_l_n); //(R.drawable.tab_l_f);
						
					} else if (i ==customTab.tabList.size() - 1) {
						customTab.tabList.get(i).setBackgroundResource(
								i == pageNum ? R.drawable.tab_r_f : R.drawable.tab_r_n);//R.drawable.tab_r_f);
					
					} else if(i != 0&&i != customTab.tabList.size() - 1){
						customTab.tabList.get(i).setBackgroundResource(
								i == pageNum ? R.drawable.tab_m_f : R.drawable.tab_m_n);//R.drawable.tab_m_f);
					}
				}
		}else{
			if ( customTab.tabList.size() == 1) {
				 customTab.tabList.get(0).setBackgroundResource(R.drawable.title_bg1);
				 customTab.tabList.get(0).setTextColor(Color.rgb(255, 255, 255));
			}else{
				if (i == 0) {
					customTab.tabList.get(i).setBackgroundResource(
							i == pageNum ? R.drawable.tab_l_f1 : R.drawable.tab_l_n1); //(R.drawable.tab_l_f);
				} else if (i ==customTab.tabList.size() - 1) {
					customTab.tabList.get(i).setBackgroundResource(
							i == pageNum ? R.drawable.tab_r_f1 : R.drawable.tab_r_n1);//R.drawable.tab_r_f);
				} else if(i != 0&&i != customTab.tabList.size() - 1){
					customTab.tabList.get(i).setBackgroundResource(
							i == pageNum ? R.drawable.tab_m_f1 : R.drawable.tab_m_n1);//R.drawable.tab_m_f);
				}
			}
			}
		}
		customTab.return_bt.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button1));
		customTab.TabUpLayout.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_TabUpLayout,Color.YELLOW));
		analog.skinChanged(typedArray);
	}
}
