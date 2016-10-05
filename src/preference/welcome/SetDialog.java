package preference.welcome;

import java.util.ArrayList;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;
import com.example.mobileacquisition.WelcomeActivity.MyHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetDialog extends DialogFragment implements OnClickListener,OnPageChangeListener,ThemeChangeListener{

	private int h;
	private float scale;
	private Activity context;
	public ArrayList<String> nameList = new ArrayList<String>();
	LanguageFragment languageFragment = new LanguageFragment();
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	LinearLayout tabLayout;
	public ArrayList<TextView> tabList = new ArrayList<TextView>();
	ViewPager dialogPager;
	Adapter adapter ;
	private View view;
	private ImageButton dialog_close;
	public SetDialog(Activity context,ArrayList<Fragment> fragmentList) {
		super();
		// TODO Auto-generated constructor stub
		this.context=context;
		this.fragmentList = fragmentList;
		h = context.getResources().getDisplayMetrics().heightPixels;
		scale = context.getResources().getDisplayMetrics().density;

	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//去掉dialog标题栏
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		 view = inflater.inflate(R.layout.dialog_custom_tab, container, false);
		 switch(ThemeLogic.themeType){
			case 1:
				view.getContext().setTheme(R.style.mode1);
				break;
			case 2:
				view.getContext().setTheme(R.style.mode2);
			}
		dialog_close=(ImageButton)view.findViewById(R.id.dialog_close);
	
		dialog_close.setOnClickListener(this);
		dialogPager = (ViewPager) view.findViewById(R.id.ViewPager);
		dialogPager.setOnPageChangeListener(this);
		tabLayout = (LinearLayout) view.findViewById(R.id.Tab);
		//mHandler.handleMessage(Chinese);
		nameList.add(context.getResources().getString(R.string.Device));
		nameList.add(context.getResources().getString(R.string.Language));
		nameList.add(context.getResources().getString(R.string.Skin));
			for (int i = 0; i < nameList.size(); i ++) {
				TextView textview = newTabs(nameList.get(i),i);
				tabLayout.addView(textview);
				if(ThemeLogic.themeType==1){
					if (i == 0) { 
						textview.setBackgroundResource(R.drawable.tab_l_f);
						tabList.get(i).setTextColor(Color.rgb(44, 82, 134));
					} else if (i == 2) {
						textview.setBackgroundResource(R.drawable.tab_r_n);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					} else {
						textview.setBackgroundResource(R.drawable.tab_m_n);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					}
				}else{
					if (i == 0) { 	
						textview.setBackgroundResource(R.drawable.tab_l_f1);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					} else if (i == 2) {
						textview.setBackgroundResource(R.drawable.tab_r_n1);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					} else {
						textview.setBackgroundResource(R.drawable.tab_m_n1);
						tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
					}
					
				}
			}
	
	/*	tabList.get(0).setTextColor(Color.rgb(0, 0, 0));
		for(int i=1;i<tabList.size();i++){
			tabList.get(i).setTextColor(Color.rgb(255, 255, 255));
		}*/
		adapter = new Adapter(getChildFragmentManager());
		dialogPager.setAdapter(adapter);
		/*if(languageFragment.languegeNumber==1||languageFragment.languegeNumber==-1){
		   dialogPager.setCurrentItem(1);
		}*/
		ThemeLogic.getInstance().addListener(this);
		return view;
	}
	
	class Adapter extends FragmentStatePagerAdapter {

		public Adapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
//			if(languageFragment.languegeNumber==1||languageFragment.languegeNumber==-1){
//				position
//			}
			
			
			
			   return fragmentList.get(position);
//			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragmentList.size();
		}
		
	}
	
	public TextView newTabs(String str,int tag) {
		TextView v = new TextView(context);
		v.setWidth(dip2px(100));
		v.setHeight(dip2px(30));
		v.setGravity(Gravity.CENTER);
		v.setText(str);
		v.setTag(tag);
		v.setOnClickListener(this);
		tabList.add(v);
		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Window window = getDialog().getWindow();
		int width = dip2px(3 * 100 + 100);
		int height = h * 4 / 5;
		window.setBackgroundDrawableResource(R.drawable.dialog_corners_bg);
		window.setLayout(width+80, height);
		super.onResume();
	}
	  public void onReStart(){
	  }
	public int dip2px(float dpValue) {
		return (int)(dpValue * scale + 0.5f);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.dialog_close){
			
			//	getFragmentManager().beginTransaction().remove(this).commit();
//			 getFragmentManager().beginTransaction().addToBackStack(null);
			this.dismiss();
		}else{
			dialogPager.setCurrentItem((Integer) v.getTag(), false);
		}
		
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
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
		// TODO Auto-generated method stub
			for (int i = 0; i < tabList.size(); i++) {
				if(ThemeLogic.themeType==1){
						tabList.get(i).setTextColor(
								i == arg0 ? Color.rgb(44, 82, 134) : Color.rgb(255, 255, 255));
						if (i == 0) {
							tabList.get(i).setBackgroundResource(
									i == arg0 ? R.drawable.tab_l_f : R.drawable.tab_l_n); 
						} else if (i == tabList.size() - 1) {
							tabList.get(i).setBackgroundResource(
									i == arg0 ? R.drawable.tab_r_f : R.drawable.tab_r_n);
						} else if(i != 0&&i != tabList.size() - 1){
							tabList.get(i).setBackgroundResource(
									i == arg0 ? R.drawable.tab_m_f : R.drawable.tab_m_n);
						}
				}else{
					
					tabList.get(i).setTextColor(
							i == arg0 ? Color.rgb(255, 255, 255) : Color.rgb(255, 255, 255));
					if (i == 0) {
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_l_f1 : R.drawable.tab_l_n1); 
					} else if (i == tabList.size() - 1) {
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_r_f1 : R.drawable.tab_r_n1);
					} else if(i != 0&&i != tabList.size() - 1){
						tabList.get(i).setBackgroundResource(
								i == arg0 ? R.drawable.tab_m_f1 : R.drawable.tab_m_n1);
					}
					
				}
			}
	
	}
	@SuppressLint("NewApi")
	@Override
	public void onThemeChanged() {
		switch (ThemeLogic.themeType) {

		/** 1閵嗗倸鍘涢弴瀛樻煀娑撳顣�閿涗緤绱掗敓锟� */
		case 1:
			view.getContext().setTheme(R.style.mode1);
			break;
		case 2:
			view.getContext().setTheme(R.style.mode2);
			break;
		}
		TypedArray typedArray = view.getContext().obtainStyledAttributes(R.styleable.myStyle);
		RelativeLayout tabUpLayout=(RelativeLayout)view.findViewById(R.id.TabUpLayout);
		tabUpLayout.setBackground(typedArray.getDrawable(R.styleable.myStyle_title_windw_top));
		dialog_close.setBackground(typedArray.getDrawable(R.styleable.myStyle_bt_dialog_close));
		typedArray.recycle();
	}
}
