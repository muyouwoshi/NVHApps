package com.cuiweiyou.dynamiclevelnavigation.util;

import android.content.Context;
import android.view.WindowManager;

/** 获取屏幕尺寸 */
public class CVU_ScreenUtil {
	
	private static WindowManager wm;
	
	private CVU_ScreenUtil(){}

	/** 获取屏幕宽度 */
	public static int getScreenWidth(Context ctx) {
		if(null == wm)
			wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

		return wm.getDefaultDisplay().getWidth();
	}

	/** 获取屏幕高度 */
	public static int getScreenHeight(Context ctx) {
		if(null == wm)
			wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

		return wm.getDefaultDisplay().getHeight();
	}
	
	/** dp转px */
	public static float dp2px(Context ctx, float dp) {
		float scale = ctx.getResources().getDisplayMetrics().density;
		return dp * scale;
	}
}
