package common;

import java.util.ArrayList;
import java.util.List;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.MainContextView;
import com.example.mobileacquisition.OneTabFragment;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import left.drawer.DisplayFragment;
import left.drawer.MainFragment;

public class CustomTab extends RelativeLayout implements OnClickListener,
		OnPageChangeListener {

	private View view;
	private LinearLayout tab;
	private Activity context;
	public MyViewPager viewPager = null;
	public ArrayList<Fragment> fragmentList = null;
	public ArrayList<TextView> tabList = new ArrayList<TextView>();
	public ImageButton add_bt;
	public ImageButton return_bt;
	public RelativeLayout TabUpLayout;
	private Fragment fragment;
	private MainFragment mainFragment;
	private FragmentActivity mcontext;
	//private boolean isWelcomeActivity = false;
	private FragmentManager fm;
	private Handler pageChangeHandle;
	
	private int pageNo;
	public ArrayList<View> viewList = new ArrayList<View>();
	public ArrayList<RelativeLayout> LegendList = new ArrayList<RelativeLayout>();
	public ArrayList<TextView> algorithm_spinnerList = new ArrayList<TextView>();
	public ArrayList<String> channel_spinnerList=new ArrayList<String>();
	public ArrayList<View> mainContextViewList = new ArrayList<View>();
	private int page=0;
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	public int getPageCount(){
		if(fragmentList!=null){
			return fragmentList.size();
		}else{
			return 0;
		}
	}
	public CustomTab(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		try {
			view = LayoutInflater.from(context).inflate(R.layout.custom_tab,
					this, true);
			add_bt = (ImageButton) view.findViewById(R.id.addPage);
			add_bt.setOnClickListener(this);
			return_bt = (ImageButton) view.findViewById(R.id.return_bt);
			TabUpLayout = (RelativeLayout) view.findViewById(R.id.TabUpLayout);
			tab = (LinearLayout) view.findViewById(R.id.Tab);
			viewPager = (MyViewPager) view.findViewById(R.id.ViewPager);
			viewPager.setOnPageChangeListener(this);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void returnButton(FragmentActivity context, Fragment ft) {//, boolean b
		return_bt.setVisibility(View.VISIBLE);
		this.fragment = ft;
		this.mcontext = context;
		//this.isWelcomeActivity = b;
		mainFragment = (MainFragment)mcontext.getSupportFragmentManager()
				.findFragmentByTag("main");
		return_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FragmentTransaction transaction = mcontext
						.getSupportFragmentManager().beginTransaction();
				//if (isWelcomeActivity) {
				if (mcontext.getClass().getSimpleName().equals("WelcomeActivity")) {
					transaction.hide(fragment).commit();
				} else {
					if (mainFragment.isShowFloatWindow()) {
						//if (mainFragment.getFloatIntent() != null) {
						//	mcontext.startService(new Intent(mcontext, FloatingService.class));
						mainFragment.show();
						//}
					}
					transaction.hide(fragment).show(mainFragment).commit();
					((MainActivity)mcontext).analogFragment.returnButton();
					/*
					 * ����������˵�Handle�Ŀ��
					 */
					((MainActivity)mcontext).leftDrawerOpen();
				}
			}
		});

	}

	public void setTabs(ArrayList<String> tabs) {
		tab.removeAllViews();
		tabList.clear();
		for (int i = 0; i < tabs.size(); i++) {
			newTab(tabs.get(i), i);
		}
		
	}

	public void setAddButtonVisible(ArrayList<Fragment> fragmentList) {
		this.fragmentList = fragmentList;
		if(fragmentList.size()>4){
			add_bt.setVisibility(View.GONE);
		}else{
			add_bt.setVisibility(View.VISIBLE);
		}
		
	}

	public void addTab(String str, Fragment fragment) {
        
		if (fragmentList == null) {
			fragmentList = new ArrayList<Fragment>();
		}
		
		newTab(str, fragmentList.size());
		fragmentList.add(fragment);
		viewPager.getAdapter().notifyDataSetChanged();
		viewPager.setCurrentItem(fragmentList.size());
		if (pageChangeHandle != null) {
			Message pageChangeMsg = pageChangeHandle.obtainMessage();
			pageChangeMsg.what = 0;
			pageChangeHandle.sendMessage(pageChangeMsg);
		}
		if (fragmentList.size() ==5){
			add_bt.setVisibility(View.GONE);
			//return;
		}
		
		SharedPreferences preference = context.getSharedPreferences(
				"hz_5D", 0);
		
		SharedPreferences.Editor editor = preference.edit();
		editor.putString("PageCount", ""+getPageCount());
		editor.commit();
	}

	public int reAddTab(ArrayList<Fragment> fragmentList){
		return fragmentList.size();
	}
	public void setup(FragmentManager fm, ArrayList<Fragment> fragmentList) {
		this.fm = fm;
		this.fragmentList = fragmentList;
		viewPager.setAdapter(new CustomFragmentAdapter(fm));
		viewPager.setOffscreenPageLimit(5);
		if (fragmentList.size() ==5){
			add_bt.setVisibility(View.GONE);
			//return;
		}
	}

	public void removeAllTabs() {
		tab.removeAllViews();
	}

	public void delete(int position) {
		Fragment oldFragment = fragmentList.get(position);
		fragmentList.remove(position);
		fm.beginTransaction().remove(oldFragment);
	
		((MainActivity)context).getViewList().clear();
		
		viewPager.setAdapter(new CustomFragmentAdapter(fm));
//		viewPager.getAdapter().notifyDataSetChanged();
		//tabList.remove(position);
		


		   tabList.remove(tabList.size() - 1);
		   if(ThemeLogic.themeType==1){
			   if(tabList.size()==1){
				    tabList.get(0).setBackgroundResource(R.drawable.tab_bg_f);
				    tabList.get(0).setTextColor(Color.rgb(44, 82, 134));
			   }  
		   }else{
			   if(tabList.size()==1){
				    tabList.get(0).setBackgroundResource(R.drawable.title_bg1);
				    tabList.get(0).setTextColor(Color.rgb(255, 255, 255));
			   }
		   }
		 
//		if (tabList.size() == 1) {
//			tabList.get(0).setBackgroundResource(R.drawable.tab_bg_f);
//			tabList.get(0).setTextColor(Color.rgb(44, 82, 134));
//		}
		tab.removeViewAt(tabList.size());
		viewPager.setCurrentItem(position == fragmentList.size() ? position - 1
				: position, false);

		if (pageChangeHandle != null) {
			Message pageChangeMsg = pageChangeHandle.obtainMessage();
			pageChangeMsg.what = 1;
			pageChangeMsg.arg1 = position;
			pageChangeHandle.sendMessage(pageChangeMsg);
		}
		if (fragmentList.size()<5){
			add_bt.setVisibility(View.VISIBLE);
			//return;
		}
		
		SharedPreferences preference = context.getSharedPreferences(
				"hz_5D", 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putString("PageCount", ""+getPageCount());
		editor.commit();
		
		pageNo = (position==fragmentList.size() ? position - 1: position);
	}
	
	public void clearForReset(){
		
		fragmentList.clear();
		viewPager.setAdapter(new CustomFragmentAdapter(fm));
		//tabList.remove(position);
	
		tabList.clear();
		add_bt.setVisibility(View.VISIBLE);
		((MainActivity)context).getViewList().clear();
	}
	

	public void replace(int position, Fragment fragment) {
		Fragment oldFragment = fragmentList.get(position);

		if(oldFragment.getClass().getSimpleName().equals("OneTabFragment")){
			((OneTabFragment)oldFragment).clear();
		}
		oldFragment = null;
		fragmentList.set(position, fragment);
		viewPager.setAdapter(new CustomFragmentAdapter(fm));
		viewPager.setCurrentItem(position, false);
	}

	private void newTab(String str, int tag) {
		TextView v = new TextView(context);
		v.setWidth(dip2px(100));
		v.setHeight(dip2px(50));
		v.setGravity(Gravity.CENTER);
		v.setText(str);
		v.setTag(tag);
		v.setOnClickListener(this);
		tab.addView(v);
		tabList.add(v);
		
		if(ThemeLogic.themeType==1){
			if (tabList.size() == 1) {
				tabList.get(0).setBackgroundResource(R.drawable.tab_bg_n);
				tabList.get(0).setTextColor(Color.rgb(44, 82, 134));
			} else {
				for (int i = 0; i < tabList.size(); i++) {
					if (i == 0) {
						tabList.get(i).setBackgroundResource(R.drawable.tab_l_f);
						tabList.get(i).setTextColor(Color.rgb(44, 82, 134));
					} else if (i == tabList.size() - 1) {
						tabList.get(i).setBackgroundResource(R.drawable.tab_r_n);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					} else {
						tabList.get(i).setBackgroundResource(R.drawable.tab_m_n);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					}
				}
			}
		}else{
			if (tabList.size() == 1) {
				tabList.get(0).setBackgroundResource(R.drawable.title_bg1);
				tabList.get(0).setTextColor(Color.rgb(255, 255, 255));
			} else {
				for (int i = 0; i < tabList.size(); i++) {
					if (i == 0) {
						tabList.get(i).setBackgroundResource(R.drawable.tab_l_f1);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					} else if (i == tabList.size() - 1) {
						tabList.get(i).setBackgroundResource(R.drawable.tab_r_n1);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					} else {
						tabList.get(i).setBackgroundResource(R.drawable.tab_m_n1);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					}
				}
			}
			
		}
	}

	public void changeTabs(ArrayList<String> tabs, String str) {
		for (int i = 0; i < tabs.size(); i++) {
			tabList.get(i).setText(tabs.get(i));
		}
	}

	public class CustomFragmentAdapter extends FragmentStatePagerAdapter {

		public CustomFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public Object instantiateItem(ViewGroup arg0, int arg1) {
			return super.instantiateItem(arg0, arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			super.restoreState(arg0, arg1);
		}

		@Override
		public Parcelable saveState() {
			return super.saveState();
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
		
		public int getPosition(){
			return pageNo;
		}

	}

	public int dip2px(float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		/*for(int i=0;i<viewList.size();i++){
			viewList.get(i).setVisibility(View.VISIBLE);
		}*/
		// TODO Auto-generated method stub
		for (int i = 0; i < tabList.size(); i++) {
			if(ThemeLogic.themeType==1){
				tabList.get(i).setTextColor(
						i == arg0 ? Color.rgb(44, 82, 134) : Color.rgb(255, 255, 255));
			}else{
				tabList.get(i).setTextColor(
						i == arg0 ? Color.rgb(255, 255, 255) : Color.rgb(255, 255, 255));
				
			}
		}
			for (int i = 0; i < tabList.size(); i++) {
				if(ThemeLogic.themeType==1){
					if (i == 0) {
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_l_f : R.drawable.tab_l_n); //(R.drawable.tab_l_f);
					} else if (i == tabList.size() - 1) {
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_r_f : R.drawable.tab_r_n);//R.drawable.tab_r_f);
					} else if(i != 0&&i != tabList.size() - 1){
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_m_f : R.drawable.tab_m_n);//R.drawable.tab_m_f);
					}
			}else{
					if (i == 0) {
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_l_f1 : R.drawable.tab_l_n1); //(R.drawable.tab_l_f);
					} else if (i == tabList.size() - 1) {
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_r_f1 : R.drawable.tab_r_n1);//R.drawable.tab_r_f);
					} else if(i != 0&&i != tabList.size() - 1){
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_m_f1 : R.drawable.tab_m_n1);//R.drawable.tab_m_f);
					}
				}
			}
			pageNo=arg0;
			if(((MainActivity)context).analogFragment!=null){
				((MainActivity)context).analogFragment.returnButton();
			}
	}
	@Override
	public void onClick(View v) {
		context.getIntent()
				.getStringExtra("LanguageIntent");
		if (v.getId() == R.id.addPage) {
			page++;
			mainFragment = (MainFragment)((MainActivity) context).getSupportFragmentManager()
					.findFragmentByTag("main");
			ArrayList<String> defList= new ArrayList<String>();
			/*if(AddAlgorithmItems.algorithmItems.size()!=0){
				defList.add(AddAlgorithmItems.algorithmItems.get(0));// "Signal"
			}else{
				defList.add("ChannelWatch");
			}*/
			//defList.add("Signal");
			 if(mainFragment.getSpinnerValues().getAlgorithmItems().size()>1){
					defList.add(mainFragment.getSpinnerValues().getAlgorithmItems().get(0));//
				}else{
					defList.add("ChannelWatch");
				}
			 	addTab(context.getResources().getString(R.string.Main_Window), OneTabFragment.newInstance(defList, viewPager));
				DisplayFragment df = (DisplayFragment) ((MainActivity) context)
						.getSupportFragmentManager().findFragmentByTag(
								"display");
				for (Button btn : df.list) {
					if (btn.getVisibility() == View.GONE) {
						btn.setVisibility(View.VISIBLE);
						return;
					}
				}
				df.pageSkinChanged();
				
		//	}
				
		} else {
			viewPager.setCurrentItem((Integer) v.getTag(), false);
		}

	}

	public int getTabCount() {
		return fragmentList.size();
	}

	public void setHandlerFromPreLeftDisplay(Handler pageChangeHandle) {
		this.pageChangeHandle = pageChangeHandle;
	}
	public ArrayList<Fragment> getFragmentList(){
		return fragmentList;
	}
	public int getPageNo(){
		return pageNo;
	}
	public void skinChanged(TypedArray typedArray){
		int pageNum = getPageNo();
		for (int i = 0; i < tabList.size(); i++) {
			if(ThemeLogic.themeType==1){
				 tabList.get(i).setTextColor(
						i == pageNum ? Color.rgb(44, 82, 134) : Color.rgb(255, 255, 255));
			}else{
				 tabList.get(i).setTextColor(
						i == pageNum ? Color.rgb(255, 255, 255) : Color.rgb(255, 255, 255));
				
			}
		}
		for (int i = 0; i < tabList.size(); i++) {
			if(ThemeLogic.themeType==1){
				if (tabList.size() == 1) {
					tabList.get(0).setBackgroundResource(R.drawable.tab_bg_n);
					tabList.get(0).setTextColor(Color.rgb(255, 255, 255));
				}else{
					if (i == 0) {
						tabList.get(i).setBackgroundResource(
								i == pageNum ? R.drawable.tab_l_f : R.drawable.tab_l_n); //(R.drawable.tab_l_f);
						
					} else if (i == tabList.size() - 1) {
						tabList.get(i).setBackgroundResource(
								i == pageNum ? R.drawable.tab_r_f : R.drawable.tab_r_n);//R.drawable.tab_r_f);
					
					} else if(i != 0&&i != tabList.size() - 1){
						tabList.get(i).setBackgroundResource(
								i == pageNum ? R.drawable.tab_m_f : R.drawable.tab_m_n);//R.drawable.tab_m_f);
					}
				}
		}else{
			if (tabList.size() == 1) {
				tabList.get(0).setBackgroundResource(R.drawable.title_bg1);
				tabList.get(0).setTextColor(Color.rgb(255, 255, 255));
			}else{
				if (i == 0) {
					tabList.get(i).setBackgroundResource(
							i == pageNum ? R.drawable.tab_l_f1 : R.drawable.tab_l_n1); //(R.drawable.tab_l_f);
				} else if (i == tabList.size() - 1) {
					tabList.get(i).setBackgroundResource(
							i == pageNum ? R.drawable.tab_r_f1 : R.drawable.tab_r_n1);//R.drawable.tab_r_f);
				} else if(i != 0&&i != tabList.size() - 1){
					tabList.get(i).setBackgroundResource(
							i == pageNum ? R.drawable.tab_m_f1 : R.drawable.tab_m_n1);//R.drawable.tab_m_f);
				}
			}
			}
		}
		add_bt.setImageDrawable(typedArray.getDrawable(R.styleable.myStyle_addPage));
		RelativeLayout	TabUpLayout = (RelativeLayout) findViewById(R.id.TabUpLayout);
		TabUpLayout.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_TabUpLayout,Color.YELLOW));
		for(int i=0;i<mainContextViewList.size();i++){
			((MainContextView)mainContextViewList.get(i)).algorithm_title.setBackgroundColor(typedArray.getColor(
					R.styleable.myStyle_algorithm_title,Color.YELLOW));
		}
	}
}
