package left.drawer;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import common.CustomTab;
public class PreLeftFragment extends Fragment implements OnClickListener{
	
	private View view=null;
	public ArrayList<String> tabs=null;
	private ArrayList<Fragment> fragmentList= null;
	public PreLeftFileStorageFragment fileStorageFragment=null;
	public PreLeftLogarithmicFragment logarithmicFragment=null;
	public PreLeftAnalogDisplayFragment analogDisplayFragment=null;
	public PreLeftDisplayFragment layoutDisplayFragment=null;
	public PreLeftDecimalFragment decimalFragment = null;
	private FragmentManager managerChild,managerFather;
	//public MainFragment mainFragment = new MainFragment();
	public CustomTab customTab;
	//private int languageNumber = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.customtab_preference, container,false);
		//view.findViewById(R.id.Back_Ai).setOnClickListener(this);
		fileStorageFragment=new PreLeftFileStorageFragment();
		logarithmicFragment=new PreLeftLogarithmicFragment();
		analogDisplayFragment=new PreLeftAnalogDisplayFragment();
		layoutDisplayFragment=new PreLeftDisplayFragment();
		decimalFragment = new PreLeftDecimalFragment();
		customTab=(CustomTab)view.findViewById(R.id.customtab);
		managerChild = getChildFragmentManager();
		managerFather=getFragmentManager();
		tabs = new ArrayList<String>();

		tabs.add(getActivity().getResources().getString(R.string.File));
		tabs.add(getActivity().getResources().getString(R.string.Logarithmic));
		tabs.add(getActivity().getResources().getString(R.string.Channel));
		tabs.add(getActivity().getResources().getString(R.string.Layout));
		tabs.add(getActivity().getResources().getString(R.string.Decimal));
		customTab.setTabs(tabs);
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fileStorageFragment);
		fragmentList.add(logarithmicFragment);
		fragmentList.add(analogDisplayFragment);
		fragmentList.add(layoutDisplayFragment);
		fragmentList.add(decimalFragment);
		customTab.setup(managerChild,fragmentList);
	
		
		PreLeftFragment preLeftFragment= (PreLeftFragment)managerFather.findFragmentByTag("preleft");
		customTab.returnButton(this.getActivity(),preLeftFragment);//,false
		return view;
	}
	public PreLeftFileStorageFragment getFileStorageFragment() {
		return fileStorageFragment;
	}

	public PreLeftLogarithmicFragment getLogarithmicFragment() {
		return logarithmicFragment;
	}

	public PreLeftAnalogDisplayFragment getAnalogDisplayFragment() {
		return analogDisplayFragment;
	}

	public PreLeftDisplayFragment getLayoutDisplayFragment() {
		return layoutDisplayFragment;
	}
	public PreLeftDecimalFragment getDecimalFragment() {
		return decimalFragment;
	}
	
	
	public ArrayList<String> getTabs() {
		return tabs;
	}
	/*public int getLanguageNumber() {
		return languageNumber;
	}
	public void setLanguageNumber(int languageNumber) {
		this.languageNumber = languageNumber;
	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		}
	}
	public void skinChanged(TypedArray typedArray){
		int pageNum =customTab.getPageNo();
		for (int i = 0; i < customTab.tabList.size(); i++) {
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
		fileStorageFragment.Restore_default.setBackground(typedArray.getDrawable(R.styleable.myStyle_paste));
		fileStorageFragment.Save.setBackground(typedArray.getDrawable(R.styleable.myStyle_copy));	
		
		logarithmicFragment.Restore_default.setBackground(typedArray.getDrawable(R.styleable.myStyle_paste));
		logarithmicFragment.Save.setBackground(typedArray.getDrawable(R.styleable.myStyle_copy));
		
		analogDisplayFragment.restore_default.setBackground(typedArray.getDrawable(R.styleable.myStyle_paste));
		analogDisplayFragment.save.setBackground(typedArray.getDrawable(R.styleable.myStyle_copy));
		
		layoutDisplayFragment.restore.setBackground(typedArray.getDrawable(R.styleable.myStyle_paste));
		layoutDisplayFragment.save.setBackground(typedArray.getDrawable(R.styleable.myStyle_copy));
		
		decimalFragment.recover.setBackground(typedArray.getDrawable(R.styleable.myStyle_paste));
		decimalFragment.save.setBackground(typedArray.getDrawable(R.styleable.myStyle_copy));
	}
	public void languageChanged(){
		tabs.clear();
		tabs.add(getActivity().getResources().getString(R.string.File));
		tabs.add(getActivity().getResources().getString(R.string.Logarithmic));
		tabs.add(getActivity().getResources().getString(R.string.Channel));
		tabs.add(getActivity().getResources().getString(R.string.Layout));
		tabs.add(getActivity().getResources().getString(R.string.Decimal));
		customTab.setTabs(tabs);
		fileStorageFragment.languageChanged();
		logarithmicFragment.languageChanged();
		analogDisplayFragment.languageChanged();
		layoutDisplayFragment.languageChanged();
		decimalFragment.languageChanged();
	}
}