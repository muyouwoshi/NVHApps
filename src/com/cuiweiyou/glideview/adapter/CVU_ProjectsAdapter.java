package com.cuiweiyou.glideview.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cuiweiyou.dynamiclevelnavigation.back.CVU_IData2ClickBack;
import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_FileUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_JSONUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.cuiweiyou.fragment.CVU_RightDrawerFragment;
import com.cuiweiyou.glideview.back.Back4ProjectDelete;
import com.cuiweiyou.glideview.back.Back4ProjectLoad;
import com.cuiweiyou.glideview.view.CVU_GlideItemView;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

/** 右侧拉项目列表 */
public class CVU_ProjectsAdapter extends BaseAdapter implements Back4ProjectDelete, Back4ProjectLoad{

	private Context ctx;
	private CVU_IData2ClickBack back;
	private boolean isEditable = false;
	private List<CVU_BeanNameAndPath> res;
	public List<String> selectedViews = new ArrayList<String>();
	private String dataFlag;
	private CVU_RightDrawerFragment parent;

	public CVU_ProjectsAdapter(CVU_RightDrawerFragment parent, CVU_IData2ClickBack back, Context ctx, int dataFlag) {
		this.parent = parent;
		this.back = back;
		this.ctx = ctx;
		this.dataFlag = dataFlag + "";
		this.res = getRes();
	}

	private List<CVU_BeanNameAndPath> getRes() {

		List<CVU_BeanNameAndPath> list = CVU_FileUtil.getFoldersInPro();
		
		return list;
	}

	// 千万千万不要复用 convertView
	@Override
	public View getView(final int position, View convertView2, ViewGroup parent) {
		View view = View.inflate(ctx, R.layout.cvu_glide_item_top, null);
		ImageView img = (ImageView) (view.findViewById(R.id.cvu_glide_item_top_selector));
		ImageView cvu_folder = (ImageView) (view.findViewById(R.id.cvu_folder));
		if(ThemeLogic.themeType==1){
			cvu_folder.setBackgroundResource(R.drawable.cvu_folder);
		}else{
			cvu_folder.setBackgroundResource(R.drawable.ico_folder_blue1);
		}
		if (isEditable)
			img.setVisibility(View.VISIBLE);
		else
			img.setVisibility(View.INVISIBLE);

		if (selectedViews.contains(position + "")) { // 集合中是否有此对象
			img.setImageResource(R.drawable.cvu_checked_active);
		} else {
			img.setImageResource(R.drawable.cvu_checked_normal);
		}

		CVU_BeanNameAndPath b = res.get(position);
		TextView text = (TextView) view.findViewById(R.id.cvu_glide_item_top_name);
		text.setText(b.getName());
		if(ThemeLogic.themeType==1){
			text.setTextColor(Color.rgb(255, 255, 255));
		}else{
			text.setTextColor(Color.rgb(0, 0, 0));
		}
		String currentDataFlag = CVU_SPUtil.getCurrentProject(ctx, "dataFlag");
		String currentProjectX = CVU_SPUtil.getCurrentProject(ctx, "currentProject");
		
		if(null != currentProjectX && !"".equals(currentProjectX)){
			CVU_BeanNameAndPath b222 = CVU_JSONUtil.json2Bean(currentProjectX);
			String name = b222.getName();
			
			if(name.equals(b.getName())){
				text.setTextColor(Color.parseColor("#ff9900"));
				img.setVisibility(View.INVISIBLE);
			}
				
				
				
			
		}

		CVU_GlideItemView slideView = new CVU_GlideItemView(ctx, ctx.getResources(), view, position, b.getName(), this, this);
		return slideView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public CVU_BeanNameAndPath getItem(int position) {
		return res.get(position);
	}

	@Override
	public int getCount() {
		return res.size();
	}

	// /////////////////////////////////////////////////

	/** 设置是否可选择 */
	public void setEditable(boolean b) {
		this.isEditable = b;
		Log.e("ard", "项目列表 可编辑");
		notifyDataSetChanged();
	}

	/** 全部选择 */
	public void addSelectedAllViews() {
		String currentPrj = getCurrentProjectName();
		
		selectedViews = new ArrayList<String>();
		for (int i = 0; i < res.size(); i++) {
			if (!selectedViews.contains(i + "") && !currentPrj.equals(res.get(i).getName())) {
				selectedViews.add(i + "");
			}
		}

		notifyDataSetChanged();
	}

	/** 选择单个item */
	public void selectItem(int position) {
		String currentPrj = getCurrentProjectName();
		
		if (isEditable) {
			if (selectedViews.contains(position + "")) {
				selectedViews.remove(position + "");
			} else {
				if(!currentPrj.equals(res.get(position).getName())){
					selectedViews.add(position + "");
				}
			}

			notifyDataSetChanged();
		} else {
			back.clickOne(position);
		}
	}

	/** 取消全部所选 */
	public void removeSelectedAllViews() {
		selectedViews.clear();
		selectedViews = new ArrayList<String>();
		
		notifyDataSetChanged();
	}

	/** 取消单个选择 */
	public void removeSelectedView(String string) {
		selectedViews.remove(string);
		notifyDataSetChanged();
	}

	/** 删除单个 */
	private void removeOne(int position) {
		CVU_BeanNameAndPath b1 = CVU_JSONUtil.json2Bean(CVU_SPUtil.getCurrentProject(ctx, "currentProject"));
		String name1 = "";
		if (null != b1)
			name1 = b1.getName(); // 刚进入应用时，sp里没有存储currentproject，所以，b1可能为null

		for (int i = 0; i < res.size(); i++) {
			if (i == position) {
				CVU_BeanNameAndPath b2 = res.get(position);
				String name2 = b2.getName();
				res.remove(position);

				if (name1.equals(name2))
					CVU_SPUtil.deleteCurrentProject(ctx);

				CVU_FileUtil.removeFilesOrDirs(b2.getPath());

				break;
			}
		}
		
		notifyDataSetChanged();
	}

	/** 删除选中的全部item */
	public void removeSelecteds() {
		CVU_BeanNameAndPath b1 = CVU_JSONUtil.json2Bean(CVU_SPUtil.getCurrentProject(ctx, "currentProject"));
		String name1 = "";
		if (null != b1)
			name1 = b1.getName(); // 刚进入应用时，sp里没有存储currentproject，所以，b1可能为null

		List<CVU_BeanNameAndPath> tmp = new ArrayList<CVU_BeanNameAndPath>();
		for (int i = 0; i < res.size(); i++) {
			if (selectedViews.contains(i + "")) {
				tmp.add(res.get(i));
				selectedViews.remove(i + "");
			}
		}

		Iterator<CVU_BeanNameAndPath> it = res.iterator();

		while (it.hasNext()) {
			CVU_BeanNameAndPath next = it.next();
			for (int i = 0; i < tmp.size(); i++) {
				CVU_BeanNameAndPath b2 = tmp.get(i);
				String name2 = b2.getName();
				if (b2.getName().equals(next.getName())) {
					it.remove();

					if (name1.equals(name2))
						CVU_SPUtil.deleteCurrentProject(ctx);

					CVU_FileUtil.removeFilesOrDirs(b2.getPath());

					break;
				}
			}
		}
		
		parent.exitEditMode();

		notifyDataSetChanged();
	}

	@Override
	public void projectDelete(int position) {
		removeOne(position);
		
		Log.e("ard", "projectsadapter 删除项目");
		Toast.makeText(ctx, R.string.project_delete_message, 0).show();
	}

	@Override
	public void projectLoad(int position) {
		CVU_BeanNameAndPath currentProject = res.get(position);
		
		CVU_SPUtil.saveCurrentProject(ctx, "dataFlag", dataFlag + "");
		CVU_SPUtil.saveCurrentProject(ctx, "currentProject", CVU_JSONUtil.bean2Json(currentProject));
		
		Toast.makeText(ctx, ctx.getResources().getString(R.string.project)
				+ currentProject.getName() + ctx.getResources().getString(R.string.loadproject), 0).show();

		Log.e("ard", "projectsadapter 加载项目");
		
		notifyDataSetChanged();
	}

	/** 获取当前加载的项目名称 */
	private String getCurrentProjectName() {
		String currentDataFlag = CVU_SPUtil.getCurrentProject(ctx, "dataFlag");
		String currentProjectX = CVU_SPUtil.getCurrentProject(ctx, "currentProject");
		String currentPrj = "";
		if(null != currentProjectX && !"".equals(currentProjectX)){
			CVU_BeanNameAndPath b222 = CVU_JSONUtil.json2Bean(currentProjectX);
			currentPrj = b222.getName();
		}
		return currentPrj;
	}
}
