package com.cuiweiyou.dynamiclevelnavigation.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanSecondStep;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanThreadStep;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_ViewUtil;
import com.cuiweiyou.glideview.back.Back4TemplateDelete;
import com.cuiweiyou.glideview.back.Back4TemplateLoad;
import com.cuiweiyou.glideview.view.CVU_ExpandableListView;
import com.cuiweiyou.glideview.view.CVU_GlideItemView;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

/** Template列表下2个子item */
public class CVU_AdapterDataAndTmpChild extends BaseExpandableListAdapter implements Back4TemplateLoad{

	private Context ctx;
	private List<CVU_BeanThreadStep> res;
	private Back4TemplateDelete back4TemplateDel;
	private CVU_AdapterDataAndTmp parent;

	/** Template列表下2个子item 
	 * @param childview */
	public CVU_AdapterDataAndTmpChild(CVU_AdapterDataAndTmp parent, Context ctx, List<CVU_BeanThreadStep> res, Back4TemplateDelete back4TemplateDel) {
		this.parent = parent;
		this.ctx = ctx;
		this.res = res;
		this.back4TemplateDel = back4TemplateDel;
	}

	@Override
	public int getGroupCount() {
		return res.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return res.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(R.layout.cvu_item_template_root, null);
		}
		LinearLayout cvu_selector_root  = (LinearLayout) convertView.findViewById(R.id.cvu_selector_root);
		
		CVU_BeanThreadStep bean = res.get(groupPosition);
		String name = bean.getName();
		TextView tv = (TextView) convertView.findViewById(R.id.templateroot);
		tv.setText(name);
		if(ThemeLogic.themeType==1){
			cvu_selector_root.setBackgroundResource(R.drawable.cvu_selector_item_list);
			tv.setTextColor(Color.rgb(255, 255, 255));
		}else{
			cvu_selector_root.setBackgroundResource(R.drawable.cvu_selector_item_list1);
			tv.setTextColor(Color.rgb(0, 0, 0));
		}
		return convertView;
	}

	// ----------------------------------------------------------------------

	@Override
	public int getChildrenCount(int groupPosition) {
		CVU_BeanThreadStep bean = res.get(groupPosition);
		if(null == bean)
			return 0;
		
		List<CVU_BeanNameAndPath> list2 = bean.getThreads();
		if(null == list2)
			return 0;
		
		return list2.size();
//		return 1;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		CVU_BeanThreadStep bean = res.get(groupPosition);
		if(null == bean)
			return null;
		
		List<CVU_BeanNameAndPath> list2 = bean.getThreads();
		if(null == list2)
			return null;
		
		return list2.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView2, ViewGroup parent) {
		View view = LayoutInflater.from(ctx).inflate(R.layout.cvu_lcm_item_template_child, null);
		CVU_BeanNameAndPath bb = res.get(groupPosition).getThreads().get(childPosition);
		TextView sn = (TextView) view.findViewById(R.id.templatechildswipe);
		sn.setText(bb.getName());
		
		String t4sr = CVU_SPUtil.getTemplate4SaveRecord(ctx);
		t4sr = t4sr.replace("/sdcard", "/storage/emulated/0");// TODO 不太严谨。上不明确WelcomActivity中存的/storage为何取的时候变成sdcard
		if(t4sr.equals(bb.getPath())){
			sn.setTextColor(Color.parseColor("#ff9900"));
		}else{
			if(ThemeLogic.themeType==1){
				sn.setTextColor(Color.rgb(255, 255, 255));
			}else{
				sn.setTextColor(Color.rgb(0, 0, 0));
			}
		}
		
		CVU_GlideItemView slideView = new CVU_GlideItemView(ctx, ctx.getResources(), view, groupPosition, childPosition, bb.getPath(), back4TemplateDel, this);
		
		slideView.setTag(R.id.abc_long_group, Integer.valueOf(groupPosition));
		slideView.setTag(R.id.abc_long_child, Integer.valueOf(childPosition));
		
		return slideView;
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

	@Override
	public void templateLoad(int groupPosition, int childPosition) {
		CVU_BeanNameAndPath bean = res.get(groupPosition).getThreads().get(childPosition);
		String name = bean.getName();
		String path = bean.getPath();
		parent.getttttt(name, path);
	}
}

