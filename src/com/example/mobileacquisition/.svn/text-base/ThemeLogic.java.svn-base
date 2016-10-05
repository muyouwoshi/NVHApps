package com.example.mobileacquisition;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * 主题管理工具类<br/>
 * 单例
 */
public class ThemeLogic {
	/** 工具类 */
	private static ThemeLogic instance;
	/** 当前模式。1：白天，2：夜晚 */
	public static int themeType = 1;
	/** 要切换模式的监听器-Aty */
	private List<ThemeChangeListener> themeList = new ArrayList<ThemeLogic.ThemeChangeListener>();

	// 私有构造函数，避免实例化
	private ThemeLogic() { }

	/**
	 * 获取工具类实例
	 * @return 工具类实例
	 */
	public static ThemeLogic getInstance() {
		// 懒汉模式
		if (instance == null) {
			instance = new ThemeLogic();
		}
		
		return instance;
	}
	
	/**
	 * 添加主题切换监听器
	 * @param listener 实现监听器的Aty
	 */
	public void addListener(ThemeChangeListener listener) {
		themeList.add(listener);
	}
	
	/**
	 * 移除主题切换监听器
	 * @param listener 实现监听器的Aty
	 */
	public void removeListener(ThemeChangeListener listener) {
		themeList.remove(listener);
	}

	/**
	 * 执行切换<br/>
	 * 切换全部实现里接口的aty的主题
	 */
	public void notifiyChange() {
		for (ThemeChangeListener lsn : themeList) {
			lsn.onThemeChanged();
		}
	}

	/**
	 * 主题切换监听器<br/>
	 * 任何需要切换主题的Activity都有实现此接口，以回调的形式实现切换
	 */
	public interface ThemeChangeListener {
		/**
		 * 切换主题功能
		 */
		public void onThemeChanged();
	}
}
