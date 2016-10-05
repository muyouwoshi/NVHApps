package com.example.mobileacquisition;

import java.util.ArrayList;
import java.util.HashSet;

import left.drawer.MainFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import common.CustomTab;
import common.MyViewPager;

import draw.map.Legend;

public class OneTabFragment extends Fragment {//implements OnClickListener

	private ArrayList<String> mapList = null;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private float density = 0;
	private MyViewPager viewPager;
	private MainFragment mainFragment;
	private int page;
	private CustomTab customTab;
	public OneTabFragment() {
		// TODO Auto-generated constructor stub
	}
	public static OneTabFragment newInstance(ArrayList<String> mapList, MyViewPager viewPager) {
		OneTabFragment oneTabFragment = new OneTabFragment();
		Bundle args = new Bundle();
        args.putStringArrayList("map", mapList);
        args.putParcelable("viewPager", viewPager);
        oneTabFragment.setArguments(args);
		return oneTabFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		if (getArguments() != null) {
            this.mapList = getArguments().getStringArrayList("map");
            this.viewPager =  getArguments().getParcelable("viewPager");
        }
		customTab = ((MainActivity)getActivity()).mainCustomTab;
		page=customTab.getPage();
		mainFragment = (MainFragment)getFragmentManager().findFragmentByTag("main");
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		density = dm.density;
		RelativeLayout layout = new RelativeLayout(getActivity());
		addMainContextFragment(layout);
		return layout;
	}
	
	private MainContextView mainContextView1,mainContextView2,mainContextView3,mainContextView4;
	private View mainlayout;
	
	private void addMainContextFragment(RelativeLayout layout) {
		// TODO Auto-generated method stub
		if(mapList!=null&&mapList.size() == 0) return;
		switch(mapList.size()) {
		case 1:
			View view1 = new MainContextView( getActivity(), viewPager);
			mainContextView1=(MainContextView)view1;
			mainContextView1.setOneTabFragment(this,0);
			mainContextView1.setMapList(mapList);
			layout.addView(view1);

			customTab.viewList.add(mainContextView1.getNewMainView());
			customTab.LegendList.add(mainContextView1.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView1.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view1);
			((MainContextView)view1).setSpinnerPosition(mapList.get(0));
			
			break;
		case 2:	
			View view2 = new MainContextView( getActivity(), viewPager);
			mainContextView1=(MainContextView)view2;
			mainContextView1.setOneTabFragment(this,0);
			mainContextView1.setMapList(mapList);
			RelativeLayout.LayoutParams params2 = new LayoutParams(0,0);
			params2.width = screenWidth / 2;
			params2.height = -1;
			params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			view2.setLayoutParams(params2);
			layout.addView(view2);
//			addViewListValues(mainContextView2.getNewMainView(),mainContextView2.getLegend(),
//					mainContextView2.getChannelCount());
			customTab.viewList.add(mainContextView1.getNewMainView());
			customTab.LegendList.add(mainContextView1.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView1.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view2);
			((MainContextView)view2).setSpinnerPosition(mapList.get(0));

			view2 = new MainContextView( getActivity(), viewPager);
			mainContextView2=(MainContextView)view2;
			mainContextView2.setOneTabFragment(this,1);
			mainContextView2.setMapList(mapList);
			params2 = new LayoutParams(0,0);
			params2.width = screenWidth / 2;
			params2.height = -1;
			params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			view2.setLayoutParams(params2);
			layout.addView(view2);
//			addViewListValues(mainContextView2.getNewMainView(),mainContextView2.getLegend(),
//					mainContextView2.getChannelCount());
			customTab.viewList.add(mainContextView2.getNewMainView());
			customTab.LegendList.add(mainContextView2.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView2.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view2);
			mainFragment.setLegendList(customTab.LegendList);
			((MainContextView)view2).setSpinnerPosition(mapList.get(1));
			
			break;
		case 3:
			View view3 = new MainContextView( getActivity(), viewPager);
			mainContextView1=(MainContextView)view3;
			mainContextView1.setOneTabFragment(this,0);
			mainContextView1.setMapList(mapList);
			RelativeLayout.LayoutParams params3 = new LayoutParams(0,0);
			params3.width = screenWidth / 2;
			params3.height = (screenHeight - dip2px(50)) / 2;
			params3.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
			params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			view3.setLayoutParams(params3);
			layout.addView(view3);
//			addViewListValues(mainContextView3.getNewMainView(),mainContextView3.getLegend(),
//					mainContextView3.getChannelCount());
			customTab.viewList.add(mainContextView1.getNewMainView());
			customTab.LegendList.add(mainContextView1.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView1.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view3);
			((MainContextView)view3).setSpinnerPosition(mapList.get(0));

			view3 = new MainContextView( getActivity(), viewPager);
			mainContextView2=(MainContextView)view3;
			mainContextView2.setOneTabFragment(this,1);
			mainContextView2.setMapList(mapList);
			params3 = new LayoutParams(0,0);
			params3.width = screenWidth / 2;
			params3.height = (screenHeight - dip2px(50)) / 2;
			params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
			params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			view3.setLayoutParams(params3);
			layout.addView(view3);
//			addViewListValues(mainContextView3.getNewMainView(),mainContextView3.getLegend(),
//					mainContextView3.getChannelCount());
			customTab.viewList.add(mainContextView2.getNewMainView());
			customTab.LegendList.add(mainContextView2.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView2.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view3);
			((MainContextView)view3).setSpinnerPosition(mapList.get(2));

			view3 = new MainContextView( getActivity(), viewPager);
			mainContextView3=(MainContextView)view3;
			mainContextView3.setOneTabFragment(this,2);
			mainContextView3.setMapList(mapList);
			params3 = new LayoutParams(0,0);
			params3.width = screenWidth / 2;
			params3.height = -1;
			params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			view3.setLayoutParams(params3);
			layout.addView(view3);
//			addViewListValues(mainContextView3.getNewMainView(),mainContextView3.getLegend(),
//					mainContextView3.getChannelCount());
			customTab.viewList.add(mainContextView3.getNewMainView());
			customTab.LegendList.add(mainContextView3.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView3.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view3);
			((MainContextView)view3).setSpinnerPosition(mapList.get(1));

			break;
		case 4:
			View view4 = new MainContextView( getActivity(), viewPager);
			mainContextView1=(MainContextView)view4;
			mainContextView1.setOneTabFragment(this,0);
			mainContextView1.setMapList(mapList);
			RelativeLayout.LayoutParams params4 = new LayoutParams(0,0);
			params4.width = screenWidth / 2;
			params4.height = (screenHeight - dip2px(50)) / 2;
			params4.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
			params4.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			view4.setLayoutParams(params4);
			layout.addView(view4);
//			addViewListValues(mainContextView4.getNewMainView(),mainContextView4.getLegend(),
//					mainContextView4.getChannelCount());
			customTab.viewList.add(mainContextView1.getNewMainView());
			customTab.LegendList.add(mainContextView1.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView1.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view4);
			((MainContextView)view4).setSpinnerPosition(mapList.get(0));

			view4 = new MainContextView( getActivity(), viewPager);
			mainContextView2=(MainContextView)view4;
			mainContextView2.setOneTabFragment(this,1);
			mainContextView2.setMapList(mapList);
			params4 = new LayoutParams(0,0);
			params4.width = screenWidth / 2;
			params4.height = (screenHeight - dip2px(50)) / 2;
			params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
			params4.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			view4.setLayoutParams(params4);
			layout.addView(view4);
//			addViewListValues(mainContextView4.getNewMainView(),mainContextView4.getLegend(),
//					mainContextView4.getChannelCount());
			customTab.viewList.add(mainContextView2.getNewMainView());
			customTab.LegendList.add(mainContextView2.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView2.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view4);
			((MainContextView)view4).setSpinnerPosition(mapList.get(2));

			view4 = new MainContextView( getActivity(), viewPager);
			mainContextView3=(MainContextView)view4;
			mainContextView3.setOneTabFragment(this,2);
			mainContextView3.setMapList(mapList);
			params4 = new LayoutParams(0,0);
			params4.width = screenWidth / 2;
			params4.height = (screenHeight - dip2px(50)) / 2;
			params4.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
			params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			view4.setLayoutParams(params4);
			layout.addView(view4);
//			addViewListValues(mainContextView4.getNewMainView(),mainContextView4.getLegend(),
//					mainContextView4.getChannelCount());
			customTab.viewList.add(mainContextView3.getNewMainView());
			customTab.LegendList.add(mainContextView3.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView3.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view4);
			((MainContextView)view4).setSpinnerPosition(mapList.get(1));

			view4 = new MainContextView( getActivity(), viewPager);
			mainContextView4=(MainContextView)view4;
			mainContextView4.setOneTabFragment(this,3);
			mainContextView4.setMapList(mapList);
			params4 = new LayoutParams(0,0);
			params4.width = screenWidth / 2;
			params4.height = (screenHeight - dip2px(50)) / 2;
			params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
			params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			view4.setLayoutParams(params4);
			layout.addView(view4);
//			addViewListValues(mainContextView4.getNewMainView(),mainContextView4.getLegend(),
//					mainContextView4.getChannelCount());
			customTab.viewList.add(mainContextView4.getNewMainView());
			customTab.LegendList.add(mainContextView4.getLegend());
			customTab.algorithm_spinnerList.add(mainContextView4.getAlgorithm_spinner());
			customTab.mainContextViewList.add(view4);
			((MainContextView)view4).setSpinnerPosition(mapList.get(3));

			break;
		}
		
		mainFragment.setLegendList(customTab.LegendList);
		if(mainFragment.getChannelCount()!=null){
			for(int i=0;i<mainFragment.getLegendList().size();i++){
				mainFragment.getLegendList().get(i).removeAllViews();
				Legend drawView=new Legend(getActivity(),mainFragment.getChannelCount());//mainFragment.getChannelCount()
				RelativeLayout.LayoutParams params = new LayoutParams(0,0);
				params.width = 160;
				params.height = mainFragment.getChannelCount().size()*25;//mainFragment.getChannelCount()
				drawView.setLayoutParams(params);
				customTab.LegendList.get(i).addView(drawView);
			}
		}
		
		
	}
	
	public int dip2px(float dpValue) {    
		return (int) (dpValue * density + 0.5f);  
	}
	public ArrayList<String> getMapList(){
		return mapList;
	}

	public MainContextView getMainContextView1() {
		return mainContextView1;
	}
	public MainContextView getMainContextView2() {
		return mainContextView2;
	}
	public MainContextView getMainContextView3() {
		return mainContextView3;
	}
	public MainContextView getMainContextView4() {
		return mainContextView4;
	}
	public void setMapList( ArrayList<String> mapList){
		this.mapList=mapList;
	}
	public void clear(){
		if(mainContextView1 != null)mainContextView1.removeContextList();
		if(mainContextView2 != null)mainContextView2.removeContextList();
		if(mainContextView3 != null)mainContextView3.removeContextList();
		if(mainContextView4 != null)mainContextView4.removeContextList();

	}
}
