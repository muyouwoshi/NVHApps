package com.cuiweiyou.dynamiclevelnavigation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * <b>类名</b>: SPUtil，小数据存储 <br/>
 * <b>说明</b>: SharePreferneces工具类 <br/>
 * @version 1 <br/>
 */
public class CVU_SPUtil {
	private static SharedPreferences sp;
	private static SharedPreferences ordersp;
	private static String templateFromWhereKey = "TemplateFromWhere";
	private static String template4SaveRecord = "Template4SaveRecord";
	private static String template4SaveRecordParent = "template4SaveRecordParent";

	private CVU_SPUtil() {
	}

	/** 设置当前load的prj<br/>
	 * 同时保存 dataFlag 和 currentProject 2个key<br/>
	 * currentProjectd对应的是项目bean转换的json */
	public static void saveCurrentProject(Context ctx, String key, String json){
		if(null == sp)
			getSP(ctx);
		
		Editor edit = sp.edit();
		edit.putString(key, json);
		edit.commit();
	}

	/** 加载已经load的prj的json<br/>
	 * key:"dataFlag" data1还是data2<br/>
	* key:"currentProject" 项目bean转的json */
	public static String getCurrentProject(Context ctx, String key){
		if(null == sp)
			getSP(ctx);
		
		return sp.getString(key, "");
	}
	

	
	public static void saveChannelsAndOrders(Context ctx, String channel, String json){
		if(null == ordersp)
			getOrderSP(ctx);
		
		Editor edit = ordersp.edit();
		edit.putString("choosedChannelsAndOrders" + channel, json);
		edit.commit();
	}
	
	
	
	public static String getChannelsAndOrders(Context ctx, String channel){
		if(null == ordersp)
			getOrderSP(ctx);
		
		return ordersp.getString("choosedChannelsAndOrders" + channel, "");
	}
	
	/** 保存已加载的模板全路径，配合{@link CVU_SPUtil.saveTemplateFromWhere}使用
	 * @param value 模板的全路径 */
	public static void saveTemplate4SaveRecord(Context ctx, String value){
		if(null == sp)
			getSP(ctx);
		
		Editor edit = sp.edit();
		edit.putString(template4SaveRecord  , value);
		edit.commit();
	}
	
	/** 保存已加载的模板所在项目bean的json，配合CVU_SPUtil.{@link saveTemplateFromWhere}和{@link saveTemplate4SaveRecord}使用
	 * @param value 模板所在的项目 */
	public static void saveTemplate4SaveRecordParent(Context ctx, String value){
		if(null == sp)
			getSP(ctx);
		
		Editor edit = sp.edit();
		edit.putString(template4SaveRecordParent  , value);
		edit.commit();
	}

	/** 当前加载的模板是从何处加载的，value：<br/>
	 * abc：右侧拉abc下<br/>
	 * wel：欢迎界面<br/>
	 * prj：右侧拉项目根目录下<br/>
	 * mes：右侧拉项目/测量目录下<br/>
	 * 后2个配合 {@link saveTemplate4SaveRecord} 和 {@link saveTemplate4SaveRecordParent}使用 */
	public static void saveTemplateFromWhere(Context ctx, String value) {
		if(null == sp)
			getSP(ctx);
		
		Editor edit = sp.edit();
		edit.putString(templateFromWhereKey , value);
		edit.commit();
	}
	
	/** 当前加载的模板是从何处加载的，value：<br/>
	 * abc：右侧拉abc下<br/>
	 * wel：欢迎界面<br/>
	 * prj：右侧拉项目根目录下<br/>
	 * mes：右侧拉项目/测量目录下<br/>
	 * 后2个，配合{@link getTemplate4SaveRecord} 和 {@link getTemplate4SaveRecordParent}  */
	public static String getTemplateFromWhere(Context ctx) {
		if(null == sp)
			getSP(ctx);
		
		return sp.getString(templateFromWhereKey , "");
	}
	
	/** 获取已加载的模板绝对路径 <br/>
	 * 配合{@link getTemplateFromWhere} 和 {@link getTemplate4SaveRecordParent}<br/>
	 * 只要模板来自prj或mes，这里就拿到正确路径*/
	public static String getTemplate4SaveRecord(Context ctx){
		if(null == sp)
			getSP(ctx);
		
		return sp.getString(template4SaveRecord, "");
	}

	/** 获取已加载的模板所在项目bean的json <br/>
	 * 配合{@link getTemplate4SaveRecord} 和 {@link getTemplateFromWhere}<br/>
	 * 只要模板来自prj或mes，这里就拿到正确路径 */
	public static String getTemplate4SaveRecordParent(Context ctx){
		if(null == sp)
			getSP(ctx);
		
		return sp.getString(template4SaveRecordParent, "");
	}
	
	/** 应用退出时删除当前load的prj */
	public static void deleteCurrentProject(Context ctx){
		if(null == sp)
			getSP(ctx);

		Editor edit = sp.edit();
		edit.clear();
		edit.commit();
		
		if(null == ordersp)
			getOrderSP(ctx);
		
		Editor edit2 = ordersp.edit();
		edit2.clear();
		edit2.commit();
		
		Log.e("ard", "SharedPreferences清空");
	}
	
	


	/** 获取左侧拉中设置的命名规则
	 * @param key <br/>
	 *  1，'fileNameRule':prj下命名规则，默认"project"。<br/>
	 *  2，'measurementfileNameRule':measurement下命名规则，默认"measurement"。<br/>
	 *  3，'modeChange':prj下命名模式，默认"0"-自定义。1-默认<br/>
	 *  4，'measurementModeChange':measurement下命名模式，默认"0"-自定义。1-默认<br/>
	 *  5，'Dir':存储位置0-SD卡，1-手机。<br/>
	 *  6，'project':工程设置
	 *  */
	public static String getLeftNamingRule(Context ctx, int key) {
		SharedPreferences preferences = ctx.getSharedPreferences("displayInfo", 0);
		String tmp = "";
		
		switch(key){
		case 1:
			tmp = preferences.getString("fileNameRule", "project");
			break;
		case 2:
			tmp = preferences.getString("MeasurementFileNameRule", "measurement");
			break;
		case 3:
			tmp = preferences.getInt("modeChange", 0)+"";
			break;
		case 4:
			tmp = preferences.getInt("measurementModeChange", 0)+"";
			break;
		case 5:
			tmp = preferences.getInt("Dir", 0)+"";
			break;
		case 6:
			tmp = preferences.getInt("project", 0)+"";
			break;
		}
		
		return tmp;
	}
	
	private static SharedPreferences getSP(Context ctx){
		if(null == sp)
			sp = ctx.getSharedPreferences("rightslide", Context.MODE_PRIVATE);
		return sp;
	}
	
	private static SharedPreferences getOrderSP(Context ctx){
		if(null == ordersp)
			ordersp = ctx.getSharedPreferences("choosedorders", Context.MODE_PRIVATE);
		return ordersp;
	}
}
