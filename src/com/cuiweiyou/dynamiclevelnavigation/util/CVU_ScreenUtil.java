package com.cuiweiyou.dynamiclevelnavigation.util;

import android.content.Context;
import android.view.WindowManager;

/** ��ȡ��Ļ�ߴ� */
public class CVU_ScreenUtil {
	
	private static WindowManager wm;
	
	private CVU_ScreenUtil(){}

	/** ��ȡ��Ļ��� */
	public static int getScreenWidth(Context ctx) {
		if(null == wm)
			wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

		return wm.getDefaultDisplay().getWidth();
	}

	/** ��ȡ��Ļ�߶� */
	public static int getScreenHeight(Context ctx) {
		if(null == wm)
			wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

		return wm.getDefaultDisplay().getHeight();
	}
	
	/** dpתpx */
	public static float dp2px(Context ctx, float dp) {
		float scale = ctx.getResources().getDisplayMetrics().density;
		return dp * scale;
	}
}
