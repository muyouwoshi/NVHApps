package com.cuiweiyou.dynamiclevelnavigation.util;

import com.cuiweiyou.glideview.view.CVU_ExpandableListView;
import com.example.mobileacquisition.R;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ExpandableListView;


/** 动态创建子ExpandableListView */
public class CVU_ViewUtil {
	
	//---------------------------------------------------------------------------
	
	/** 动态创建子ExpandableListView */
	public static CVU_ExpandableListView getExpandableListView(Context ctx) {
//		ExpandableListView mExpandableListView = new ExpandableListView(ctx);
		CVU_ExpandableListView mExpandableListView = new CVU_ExpandableListView(ctx);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ctx.getResources().getDimension(R.dimen.cvu_expitempaddingleft));
		mExpandableListView.setLayoutParams(lp);
//		mExpandableListView.setDividerHeight(0);// 取消group项的分割线
//		mExpandableListView.setChildDivider(null);// 取消child项的分割线
		mExpandableListView.setGroupIndicator(null);// 取消展开折叠的指示图标
		
		return mExpandableListView;
	}
}
