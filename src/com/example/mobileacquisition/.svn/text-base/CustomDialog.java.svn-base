package com.example.mobileacquisition;

import java.util.ArrayList;

import left.drawer.MainFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class CustomDialog extends DialogFragment implements OnClickListener,OnPageChangeListener{
 
	private int h,w,type;
	private float scale;
	private Activity context;
	private ArrayList<String> nameList = new ArrayList<String>();
	private ArrayList<TextView> tabList = new ArrayList<TextView>();
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	private RelativeLayout tabUpLayout;
	private HorizontalScrollView horizontalScrollView;
	private LinearLayout tabLayout;
	private TextView textview;
	private ViewPager dialogPager;
	public CustomDialog(Activity context,ArrayList<Fragment> fragmentList) {
		super();
		// TODO Auto-generated constructor stub
		this.context=context;
		this.fragmentList = fragmentList;
		h = context.getResources().getDisplayMetrics().heightPixels;
		w = context.getResources().getDisplayMetrics().widthPixels;
		scale = context.getResources().getDisplayMetrics().density;
		type=context.getResources().getConfiguration().orientation;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//ȥ��dialog������
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.custom_tab, container, false);
		dialogPager = (ViewPager) view.findViewById(R.id.ViewPager);
		dialogPager.setOffscreenPageLimit(5);
		dialogPager.setOnPageChangeListener(this);
		horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
		tabUpLayout = (RelativeLayout) view.findViewById(R.id.TabUpLayout);
		if(ThemeLogic.themeType==1){
			tabUpLayout.setBackgroundResource(R.drawable.bt_corners_bg);
		}else{
			tabUpLayout.setBackgroundResource(R.drawable.bt_corners_bg1);
		}
		tabLayout = (LinearLayout) view.findViewById(R.id.Tab);
		nameList.add(context.getResources().getString(R.string.Algorithm));
		nameList.add(context.getResources().getString(R.string.X_Axis));
		nameList.add(context.getResources().getString(R.string.Y_Axis));
		nameList.add(context.getResources().getString(R.string.Legend));
		if(fragmentList.size()>4){
			nameList.add(context.getResources().getString(R.string.Cursor));
		}
		for (int i = 0; i < nameList.size(); i ++) {
			textview = newTabs(nameList.get(i),i);
			tabLayout.addView(textview);
			if (nameList.size() == 1) {
				if(ThemeLogic.themeType==1){
					textview.setBackgroundResource(R.drawable.tab_bg_n);
					textview.setTextColor(Color.rgb(44, 82, 134));
				}else{
					textview.setBackgroundResource(R.drawable.title_bg1);
					textview.setTextColor(Color.rgb(44, 82, 134));
				}
			} else {
				if(ThemeLogic.themeType==1){
					if (i == 0) { 
						textview.setBackgroundResource(R.drawable.tab_l_f);
						textview.setTextColor(Color.rgb(44, 82, 134));
					} else if (i == nameList.size() - 1) {
						textview.setBackgroundResource(R.drawable.tab_r_n);
						textview.setTextColor(Color.rgb(255, 255, 255));
					} else {
						textview.setBackgroundResource(R.drawable.tab_m_n);
						textview.setTextColor(Color.rgb(255, 255, 255));
					}
				}else{
					if (i == 0) { 
						textview.setBackgroundResource(R.drawable.tab_l_f1);
						textview.setTextColor(Color.rgb(255, 255, 255));
					} else if (i == nameList.size() - 1) {
						textview.setBackgroundResource(R.drawable.tab_r_n1);
						textview.setTextColor(Color.rgb(255, 255, 255));
					} else {
						textview.setBackgroundResource(R.drawable.tab_m_n1);
						textview.setTextColor(Color.rgb(255, 255, 255));
					}
				}
			}
		}
		Adapter adapter = new Adapter(getChildFragmentManager());
		dialogPager.setAdapter(adapter);
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
			return fragmentList.get(position);
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragmentList.size();
		}
		
	}
	
	public TextView newTabs(String str,int tag) {
		TextView v = new TextView(context);
		if(type == Configuration.ORIENTATION_PORTRAIT){
			v.setWidth(dip2px(70));
		}else{
			v.setWidth(dip2px(80));
		}
		v.setHeight(dip2px(50));
		v.setGravity(Gravity.CENTER);
		v.setTextSize(12f);
		v.setText(str);
		tabList.add(v);
		v.setTag(tag);
		v.setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		int width,height;
		Window window = getDialog().getWindow();
		if(type == Configuration.ORIENTATION_PORTRAIT){
			width = w-dip2px(10);
			height = h * 2 / 5;
		}else{
			width = dip2px(nameList.size() * 80 + 35);
			height = h * 3 / 5+dip2px(20);
		}
		window.setBackgroundDrawableResource(R.drawable.dialog_corners_bg);
		window.setLayout(width, height);
		super.onResume();
	}
	public int dip2px(float dpValue) {
		return (int)(dpValue * scale + 0.5f);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dialogPager.setCurrentItem((Integer) v.getTag(), false);
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
							i == arg0 ? R.drawable.tab_l_f : R.drawable.tab_l_n); //(R.drawable.tab_l_f);
					
				} else if (i == tabList.size() - 1) {
					tabList.get(i).setBackgroundResource(
							i == arg0 ? R.drawable.tab_r_f : R.drawable.tab_r_n);//R.drawable.tab_r_f);
				} else if(i != 0&&i != tabList.size() - 1){
					tabList.get(i).setBackgroundResource(
							i == arg0 ? R.drawable.tab_m_f : R.drawable.tab_m_n);//R.drawable.tab_m_f);
				}
			}else{
				tabList.get(i).setTextColor(
						i == arg0 ? Color.rgb(255, 255, 255) : Color.rgb(255, 255, 255));
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
		if(arg0==0){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					horizontalScrollView.scrollTo(0, 0);
				}
			}, 100);
		}else if(arg0==tabList.size()-1){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					horizontalScrollView.scrollTo(tabUpLayout.getRight(), 0);
				}
			}, 100);
		}
		
	}
}
