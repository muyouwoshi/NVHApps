package com.cuiweiyou.dynamiclevelnavigation.util;

import com.cuiweiyou.glideview.view.CVU_ExpandableListView;
import com.example.mobileacquisition.R;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ExpandableListView;


/** ��̬������ExpandableListView */
public class CVU_ViewUtil {
	
	//---------------------------------------------------------------------------
	
	/** ��̬������ExpandableListView */
	public static CVU_ExpandableListView getExpandableListView(Context ctx) {
//		ExpandableListView mExpandableListView = new ExpandableListView(ctx);
		CVU_ExpandableListView mExpandableListView = new CVU_ExpandableListView(ctx);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ctx.getResources().getDimension(R.dimen.cvu_expitempaddingleft));
		mExpandableListView.setLayoutParams(lp);
//		mExpandableListView.setDividerHeight(0);// ȡ��group��ķָ���
//		mExpandableListView.setChildDivider(null);// ȡ��child��ķָ���
		mExpandableListView.setGroupIndicator(null);// ȡ��չ���۵���ָʾͼ��
		
		return mExpandableListView;
	}
}
