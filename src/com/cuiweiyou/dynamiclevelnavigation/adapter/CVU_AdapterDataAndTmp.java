package com.cuiweiyou.dynamiclevelnavigation.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import left.drawer.AcquisitionFragment;
import left.drawer.AiFragment;
import left.drawer.AnalogFragment;
import left.drawer.ChannelSettingFragment;
import left.drawer.ColormapFragment;
import left.drawer.DigitalFragment;
import left.drawer.DisplayFragment;
import left.drawer.FftFragment;
import left.drawer.OctFragment;
import left.drawer.OrderFragment;
import left.drawer.PreTriggerFragment;
import left.drawer.RpmFragment;
import left.drawer.SetPreviewFragment;
import left.drawer.SignalFragment;
import left.drawer.SplFragment;
import left.drawer.TriggerFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cuiweiyou.dynamiclevelnavigation.back.CVU_ISecondDataBack;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanSecondStep;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanThreadStep;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_FileUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_ViewUtil;
import com.cuiweiyou.glideview.back.Back4TemplateDelete;
import com.cuiweiyou.glideview.back.Back4TemplateLoad;
import com.cuiweiyou.glideview.view.CVU_ExpandableListView;
import com.cuiweiyou.glideview.view.CVU_GlideItemView;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import common.PullBookParser;

/** Data��Template�б?Template����2����item */
@SuppressLint("ResourceAsColor")
public class CVU_AdapterDataAndTmp extends BaseExpandableListAdapter implements Back4TemplateDelete{

	private Context ctx;
	private List<CVU_BeanSecondStep> list;
	private CVU_ISecondDataBack back;
	private boolean isAbcExpand = true;

	/** Data��Template�б?Template����2����item */
	public CVU_AdapterDataAndTmp(Context ctx, CVU_ISecondDataBack back) {
		this.ctx = ctx;
		this.back = back;
		list = getRes();
	}

	private List<CVU_BeanSecondStep> getRes() {
//		Log.e("ard", "��д����ģ���ļ�" );
		List<CVU_BeanNameAndPath> threads = CVU_FileUtil.getFoldersInTemplate();
		
		CVU_BeanThreadStep bts1 = new CVU_BeanThreadStep("Data2", null, null, null);
		CVU_BeanThreadStep bts2 = new CVU_BeanThreadStep("ABC", null, null, threads);

		List<CVU_BeanThreadStep> seconds = new ArrayList<CVU_BeanThreadStep>();
		seconds.add(bts1);
		seconds.add(bts2);

		CVU_BeanSecondStep bean1 = new CVU_BeanSecondStep("Data1", null, null, null);
		CVU_BeanSecondStep bean2 = new CVU_BeanSecondStep("Template", null, null, seconds);

		List<CVU_BeanSecondStep> list = new ArrayList<CVU_BeanSecondStep>();
		list.add(bean1);
		list.add(bean2);

		return list;
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(R.layout.cvu_item_rootlist, null);
		}
		LinearLayout cvu_selector_item_list  = (LinearLayout) convertView.findViewById(R.id.cvu_selector_item_list);
		TextView name = (TextView) convertView.findViewById(R.id.item_root_name);
		if(name != null)
			name.setText(list.get(groupPosition).getName());
		if(ThemeLogic.themeType==1){
			cvu_selector_item_list.setBackgroundResource(R.drawable.cvu_selector_item_list);
			name.setTextColor(Color.rgb(255, 255, 255));
		}else{
			cvu_selector_item_list.setBackgroundResource(R.drawable.cvu_selector_item_list1);
			name.setTextColor(Color.rgb(0, 0, 0));
		}
		return convertView;
	}

	// ----------------------------------------------------------------------

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = 0;
		
		CVU_BeanSecondStep bss = list.get(groupPosition);
		if(null == bss)
			return count;
		
		List<CVU_BeanThreadStep> seconds = bss.getSeconds();
		if(null == seconds)
			return count;
		
		return seconds.size();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		CVU_BeanThreadStep child = null;
		
		CVU_BeanSecondStep bean = list.get(groupPosition);
		if(null == bean)
			return child;
		
		List<CVU_BeanThreadStep> seconds = bean.getSeconds();
		if(null == seconds)
			return child;
		
		child = seconds.get(childPosition);
		
		return child;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//		Log.e("ard", "groupPosition " + groupPosition + ",childPosition " + childPosition);
		
		CVU_BeanSecondStep cb = list.get(groupPosition);
		List<CVU_BeanThreadStep> lb = cb.getSeconds();
		final CVU_BeanThreadStep b = lb.get(childPosition);
		
		final CVU_ExpandableListView childview = CVU_ViewUtil.getExpandableListView(ctx);
		final List<CVU_BeanThreadStep> seconds = new ArrayList<CVU_BeanThreadStep>();
		seconds.add(b);
		childview.setAdapter(new CVU_AdapterDataAndTmpChild(this, ctx, seconds, this));
		
		childview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition2, long id) {
				if(0==childPosition){
					back.clickSecondDataBack();
					Log.e("ard", "adapterdataandtmp ��� data2");
				}
				
				else if(1 == childPosition){
					if(isAbcExpand){
						isAbcExpand = false;
						list = getRes();
						notifyDataSetChanged();
						childview.collapseGroup(0);
					}else{
						isAbcExpand = true;
						list = getRes();
						notifyDataSetChanged();
						childview.expandGroup(0);
					}
//					Log.e("ard", "չ������" + isAbcExpand);
				}
				
				return false;
			}
		});
		
//		childview.setOnItemLongClickListener(new OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//				return false;
//			}
//			
//		});
//		childview.setOnChildClickListener(new OnChildClickListener() {
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition2, int childPosition2, long id) {
//				
//				Integer groupPosition2 = (Integer) view.getTag(R.id.abc_long_group);
//				Integer childPosition2 = (Integer) view.getTag(R.id.abc_long_child);
//				Log.e("ard", "g:" + groupPosition2 + ",c:" + childPosition2);
//				if(null == groupPosition2 || null == childPosition2)
//					return false;
//				
//				String path = seconds.get(groupPosition2).getThreads().get(childPosition2).getPath();
//				String name = seconds.get(groupPosition2).getThreads().get(childPosition2).getName();
//				
//				Log.e("ard", "adapterdataandtmp ���� �����abc��ģ�壺" + path);
//				////////////////////////////////////// 16.3.25 �ܽ� �Ҳ���ģ����� -begin
//				getTempelate(path, name, "abc"); notifyDataSetInvalidated();
//				////////////////////////////////////// -end
//				
//				return true;
//			}
//		});


		// ��ExpandableListViewչ��ʱ����Ϊgroupֻ��һ�������ExpandableListView���ܸ߶�= ����ExpandableListView��child���� + 1 ��* ÿһ��ĸ߶� 
		childview.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				if(null != b && null != b.getThreads()){
					android.widget.AbsListView.LayoutParams lp = new android.widget.AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (b.getThreads().size()+2) * (int) ctx.getResources().getDimension(R.dimen.cvu_expitemheight));
					childview.setLayoutParams(lp);
				}
			}
		});
		
		// ��ExpandableListView�ر�ʱ����ʱֻʣ��group��һ� ������ExpandableListView���ܸ߶ȼ�Ϊһ��ĸ߶� 
		childview.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {

				android.widget.AbsListView.LayoutParams lp = new android.widget.AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ctx.getResources().getDimension(R.dimen.cvu_expitemheight));
				childview.setLayoutParams(lp);
			}
		});

//		childview.expandGroup(0);

		if(isAbcExpand){
			childview.collapseGroup(0);
//			isAbcExpand = false;
		}else{
			childview.expandGroup(0);
//			isAbcExpand = true;
		}
		
		return childview;
	}

	// ------------------------------------------------------------------

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	//----------------------------------------------------------------

	@Override
	public void templateDelete(int position) {

		CVU_BeanNameAndPath b = list.get(1).getSeconds().get(1).getThreads().get(position);
		CVU_FileUtil.removeFilesOrDirs(b.getPath());
		
		Log.e("ard", "adapterdataandtmp ɾ��ģ��");
		Toast.makeText(ctx, R.string.template_delete_message, 0).show();

		list = getRes();
		notifyDataSetChanged();
	}
	
	//---------------------------------------------------------------------
	
	public void getttttt(String name, String path){
//		Log.e("ard", "g:" + name2 + ", c:" + path2);
//
//		CVU_BeanSecondStep cb = list.get(name2);
//		List<CVU_BeanThreadStep> lb = cb.getSeconds();
//		final CVU_BeanThreadStep b = lb.get(path2);
//		
//		final List<CVU_BeanThreadStep> seconds = new ArrayList<CVU_BeanThreadStep>();
//		seconds.add(b);
//		
//		String path = seconds.get(name2).getThreads().get(path2).getPath();
//		String name = seconds.get(name2).getThreads().get(path2).getName();
//		
		Log.e("ard", "adapterdataandtmpchild ���� �����abc��ģ�壺" + path);
		////////////////////////////////////// 16.3.25 �ܽ� �Ҳ���ģ����� -begin
		getTempelate(path, name, "abc");
	}

	/** ����ABC�µ�ģ�� */ // 16.3.25 �ܽ�
	private void getTempelate(String oldPath, String name, String flag){
//		String TEMPLATE_PATH = "/sdcard/data/Template";
//		
//		String path = ctx.getApplicationContext().getFilesDir().getAbsolutePath();
//		int num = path.lastIndexOf("/");
//		String str = path.substring(0, num + 1);
//		String newPath = str+ "shared_prefs/hz_5D.xml";
//		
		refresh(oldPath, name, flag);
		notifyDataSetInvalidated();
		Toast.makeText(ctx, R.string.loadTemplate, Toast.LENGTH_SHORT).show();
	}
	
	private void refresh(String oldPath, String name, String flag) {
		FragmentActivity activity = (FragmentActivity) ctx;
		android.support.v4.app.FragmentManager manager = activity.getSupportFragmentManager();
		 
		PullBookParser templateParser = new PullBookParser(oldPath);
		SharedPreferences preferences = activity.getSharedPreferences("hz_5D",0);
		
		SharedPreferences preference = activity.getSharedPreferences("Equipment", 0);
		int equipment_Num = preference.getInt("Equipment", 0);
		int Device_ChannelNum = 0;
		
		ChannelSettingFragment channelSettingFragment = (ChannelSettingFragment)manager.findFragmentByTag("channelSetting");
		AnalogFragment analogFragment = channelSettingFragment.getAnalog();
		
		if(analogFragment.getActivity() != null){
			analogFragment.clearOldChannel();
		}
		
		reLoadPreferences(templateParser,preferences);
		
		switch (equipment_Num) {
		case 0:// ��Ӳ��
			Device_ChannelNum = 64;
			break;
		case 1:// �ֻ�
			Device_ChannelNum = 1;
			break;
		case 2:// Ӳ��
			Device_ChannelNum = 8;
			break;
		}
		
		if(analogFragment!= null){
			if(analogFragment.lvt != null){
				analogFragment.lvt.removeAllViews();
				analogFragment.InitChannelList(Device_ChannelNum);
			}
		}
		
		DigitalFragment digital = channelSettingFragment.getDigital();
		if(digital.getView() == null) digital.readFromXml(activity);
		else digital.reset(activity);
		
		SetPreviewFragment spf = (SetPreviewFragment) manager.findFragmentByTag("setPreview");
		spf.addPreviewChannel("hz_5D");
		spf.addPreviewAnalysis("hz_5D");
		spf.addPreviewDigital();
		
		SignalFragment signalFragment = (SignalFragment) manager.findFragmentByTag("signal");     
		signalFragment.readFromXml(activity);
		signalFragment.init();
		
		SplFragment splFragment = (SplFragment) manager.findFragmentByTag("spl");
		splFragment.readFromXml(activity);
		splFragment.init();
		
		OctFragment octFragment = (OctFragment) manager.findFragmentByTag("oct");
		octFragment.readFromXml(activity);
		octFragment.init();
		
		FftFragment fftFragment = (FftFragment) manager.findFragmentByTag("fft");
		fftFragment.readFromXml(activity);
		fftFragment.init();
		
		AiFragment aiFragment = (AiFragment) manager.findFragmentByTag("ai");
		aiFragment.readFromXml(activity);
		aiFragment.init();

		RpmFragment rpmFragment = (RpmFragment) manager.findFragmentByTag("rpm");
		rpmFragment.readFromXml(activity);
		rpmFragment.init();
		
		ColormapFragment colormapFragment = (ColormapFragment) manager.findFragmentByTag("colormap");
		colormapFragment.readFromXml(activity);
		colormapFragment.init();
		
		OrderFragment orderFragment = (OrderFragment) manager.findFragmentByTag("order");
		orderFragment.reset(activity);
		//		OrderFragment orderFragment = (OrderFragment) manager.findFragmentByTag("order");
		
		AcquisitionFragment acquisitionFragment = (AcquisitionFragment) manager.findFragmentByTag("acqui");		
		acquisitionFragment.reset(activity);
		
		PreTriggerFragment preTriggerFragment = (PreTriggerFragment) manager.findFragmentByTag("pretrig");
		preTriggerFragment.reset(activity);
		
		TriggerFragment triggerFragment = (TriggerFragment) manager.findFragmentByTag("trig");
		triggerFragment.reset(activity);
		
		DisplayFragment displayFragment = (DisplayFragment)manager.findFragmentByTag("display");
		displayFragment.common.readFromTemplate();
		
		((MainActivity)ctx).mainCustomTab.clearForReset();
		((MainActivity)ctx).addMainCustomTab();
		((MainActivity)ctx).mainFragment.getLeftExplandableListView().getDisplayFragment().initSelectedPages();
		
		CVU_SPUtil.saveTemplateFromWhere(ctx, "abc");
		CVU_SPUtil.saveTemplate4SaveRecord(ctx, oldPath);
	}
	
	private void reLoadPreferences(PullBookParser templateParser,SharedPreferences preferences) {
		SharedPreferences.Editor editor = preferences.edit();
		Map<String,String> map = templateParser.getValueMap();
		Set<String> keySet = map.keySet();
		
		for(String key: keySet){
			String value = map.get(key);
			editor.putString(key, value);
		}
		editor.commit();
	}

}
