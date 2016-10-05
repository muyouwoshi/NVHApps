package com.cuiweiyou.dynamiclevelnavigation.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_FileUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_JSONUtil;
import com.cuiweiyou.dynamiclevelnavigation.util.CVU_SPUtil;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

/**
 * <b>类名</b>: AdapterTemplateChild，<br/>
 * 主目录/Template/ABC 下的item为SwipeListView，此为其适配器
 */
public class CVU_AdapterTemplateChild extends BaseAdapter{
	
	private Context ctx;
	private List<CVU_BeanNameAndPath> res;
//	private ITemplateDelBack back;
//	private int groupPosition;
	private ListView slv;

//	public AdapterTemplateChild(Context ctx, List<BeanNameAndPath> res, int flag, SwipeListView slv, ITemplateDelBack back, int groupPosition){
	public CVU_AdapterTemplateChild(Context ctx, List<CVU_BeanNameAndPath> res, int flag, ListView slv, int groupPosition){
		this.ctx = ctx;
		this.res = res;
		this.slv = slv;
//		this.back = back;
//		this.groupPosition = groupPosition;
	}

	@Override
	public int getCount() {
		return res.size();
	}

	@Override
	public CVU_BeanNameAndPath getItem(int position) {
		return res.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = View.inflate(ctx, R.layout.cvu_item_templatelist, null);
		}
		LinearLayout item_front  = (LinearLayout) convertView.findViewById(R.id.item_front);
		final CVU_BeanNameAndPath bean = res.get(position);

		TextView name = (TextView) convertView.findViewById(R.id.item_template_name);
		name.setText(bean.getName());
	
//		final Button btnDele = (Button) convertView.findViewById(R.id.item_remove);
//		
//		btnDele.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeOne(position);
//				Toast.makeText(ctx, "item删除 " + position, 0).show();
//			}
//		});
		if(ThemeLogic.themeType==1){
			item_front.setBackgroundResource(R.drawable.cvu_selector_item_list);
			name.setTextColor(Color.rgb(255, 255, 255));
		}else{
			item_front.setBackgroundResource(R.drawable.cvu_selector_item_list1);
			name.setTextColor(Color.rgb(0, 0, 0));
		}
		return convertView;
	}

	///////////////////////////////////////////////////
	
	/** 删除单个 */
	private void removeOne(int position){
		CVU_BeanNameAndPath b1 = CVU_JSONUtil.json2Bean(CVU_SPUtil.getCurrentProject(ctx, "currentProject"));
		String name1 = "";
		if(null != b1)
			name1 = b1.getName(); // 刚进入应用时，sp里没有存储currentproject，所以，b1可能为null

		CVU_BeanNameAndPath b2 = res.get(position);
		String name2 = b2.getName();
		
		for (int i = 0; i < res.size(); i++) {
			if(i==position){
				res.remove(position);
				
				if(name1.equals(name2))
					CVU_SPUtil.deleteCurrentProject(ctx);
				
				CVU_FileUtil.removeFilesOrDirs(b2.getPath());
				
				break;
			}
		}
		
		notifyDataSetChanged();
//		slv.closeOpenedItems();
//		back.tmplateDeled(groupPosition, b2);
	}
}
