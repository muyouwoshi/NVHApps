package com.cuiweiyou.dynamiclevelnavigation.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanThreadStep;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

/** measurementX/下的目录和文件列表适配器 */
public class CVU_AdapterThreadStep extends BaseExpandableListAdapter {

	private Context ctx;
	private List<CVU_BeanThreadStep> res;

	public CVU_AdapterThreadStep(Context ctx, List<CVU_BeanThreadStep> res) {
		this.ctx = ctx;
		this.res = res;
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
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(R.layout.cvu_item_projectlist_step2, null);
		}
		LinearLayout item_front = (LinearLayout) convertView.findViewById(R.id.item_projectlist_stemp2);
		CVU_BeanThreadStep bean = res.get(groupPosition);
		String name = bean.getName();

		TextView tv = (TextView) convertView.findViewById(R.id.item_project_child_name);
		tv.setText(name);
		String t4sr = CVU_SPUtil.getTemplate4SaveRecord(ctx);
		if (t4sr.equals(bean.getPath())) {
			tv.setTextColor(Color.parseColor("#ff9900"));
		} else {
			if (ThemeLogic.themeType == 1) {
				tv.setTextColor(Color.rgb(255, 255, 255));
			} else {
				tv.setTextColor(Color.rgb(0, 0, 0));
			}
		}

		ImageView img = (ImageView) convertView.findViewById(R.id.item_project_child_img);

		if (name.endsWith(".xml") || name.endsWith(".pcm")) {
			if (ThemeLogic.themeType == 1) {
				img.setBackgroundResource(R.drawable.cvu_file);
			} else {
				img.setBackgroundResource(R.drawable.ico_file_blue1);
			}

		} else {
			if (ThemeLogic.themeType == 1) {
				img.setBackgroundResource(R.drawable.cvu_folder);
			} else {
				img.setBackgroundResource(R.drawable.ico_folder_blue1);
			}

		}
		if (ThemeLogic.themeType == 1) {
			item_front.setBackgroundResource(R.drawable.cvu_selector_item_list);
		} else {
			item_front.setBackgroundResource(R.drawable.cvu_selector_item_list1);
		}
		
		convertView.setTag(R.id.tmp_long_group, Integer.valueOf(groupPosition));
		
		return convertView;
	}

	// ----------------------------------------------------------------------

	@Override
	public int getChildrenCount(int groupPosition) {
		return res.get(groupPosition).getThreads().size();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return res.get(groupPosition).getThreads().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(R.layout.cvu_item_projectlist_step3, null);
		}
		LinearLayout item_front = (LinearLayout) convertView.findViewById(R.id.item_projectlist_stemp3);
		CVU_BeanNameAndPath bean = res.get(groupPosition).getThreads().get(childPosition);
		String name = bean.getName();

		TextView tv = (TextView) convertView.findViewById(R.id.item_project_child_name);
		tv.setText(name);

		ImageView img = (ImageView) convertView.findViewById(R.id.item_project_child_img);
		if (name.endsWith(".xml") || name.endsWith(".pcm") || name.endsWith(".txt")) {

			if (ThemeLogic.themeType == 1) {
				img.setBackgroundResource(R.drawable.cvu_file);
			} else {
				img.setBackgroundResource(R.drawable.ico_file_blue1);
			}
		} else {

			if (ThemeLogic.themeType == 1) {
				img.setBackgroundResource(R.drawable.cvu_folder);
			} else {
				img.setBackgroundResource(R.drawable.ico_folder_blue1);
			}
		}
		if (1 == ThemeLogic.themeType) {
			item_front.setBackgroundResource(R.drawable.cvu_selector_item_list);
			tv.setTextColor(Color.rgb(255, 255, 255));
		} else {
			item_front.setBackgroundResource(R.drawable.cvu_selector_item_list1);
			tv.setTextColor(Color.rgb(0, 0, 0));
		}
		return convertView;
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
}
