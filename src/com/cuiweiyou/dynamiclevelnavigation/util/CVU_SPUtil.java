package com.cuiweiyou.dynamiclevelnavigation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * <b>����</b>: SPUtil��С���ݴ洢 <br/>
 * <b>˵��</b>: SharePreferneces������ <br/>
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

	/** ���õ�ǰload��prj<br/>
	 * ͬʱ���� dataFlag �� currentProject 2��key<br/>
	 * currentProjectd��Ӧ������Ŀbeanת����json */
	public static void saveCurrentProject(Context ctx, String key, String json){
		if(null == sp)
			getSP(ctx);
		
		Editor edit = sp.edit();
		edit.putString(key, json);
		edit.commit();
	}

	/** �����Ѿ�load��prj��json<br/>
	 * key:"dataFlag" data1����data2<br/>
	* key:"currentProject" ��Ŀbeanת��json */
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
	
	/** �����Ѽ��ص�ģ��ȫ·�������{@link CVU_SPUtil.saveTemplateFromWhere}ʹ��
	 * @param value ģ���ȫ·�� */
	public static void saveTemplate4SaveRecord(Context ctx, String value){
		if(null == sp)
			getSP(ctx);
		
		Editor edit = sp.edit();
		edit.putString(template4SaveRecord  , value);
		edit.commit();
	}
	
	/** �����Ѽ��ص�ģ��������Ŀbean��json�����CVU_SPUtil.{@link saveTemplateFromWhere}��{@link saveTemplate4SaveRecord}ʹ��
	 * @param value ģ�����ڵ���Ŀ */
	public static void saveTemplate4SaveRecordParent(Context ctx, String value){
		if(null == sp)
			getSP(ctx);
		
		Editor edit = sp.edit();
		edit.putString(template4SaveRecordParent  , value);
		edit.commit();
	}

	/** ��ǰ���ص�ģ���ǴӺδ����صģ�value��<br/>
	 * abc���Ҳ���abc��<br/>
	 * wel����ӭ����<br/>
	 * prj���Ҳ�����Ŀ��Ŀ¼��<br/>
	 * mes���Ҳ�����Ŀ/����Ŀ¼��<br/>
	 * ��2����� {@link saveTemplate4SaveRecord} �� {@link saveTemplate4SaveRecordParent}ʹ�� */
	public static void saveTemplateFromWhere(Context ctx, String value) {
		if(null == sp)
			getSP(ctx);
		
		Editor edit = sp.edit();
		edit.putString(templateFromWhereKey , value);
		edit.commit();
	}
	
	/** ��ǰ���ص�ģ���ǴӺδ����صģ�value��<br/>
	 * abc���Ҳ���abc��<br/>
	 * wel����ӭ����<br/>
	 * prj���Ҳ�����Ŀ��Ŀ¼��<br/>
	 * mes���Ҳ�����Ŀ/����Ŀ¼��<br/>
	 * ��2�������{@link getTemplate4SaveRecord} �� {@link getTemplate4SaveRecordParent}  */
	public static String getTemplateFromWhere(Context ctx) {
		if(null == sp)
			getSP(ctx);
		
		return sp.getString(templateFromWhereKey , "");
	}
	
	/** ��ȡ�Ѽ��ص�ģ�����·�� <br/>
	 * ���{@link getTemplateFromWhere} �� {@link getTemplate4SaveRecordParent}<br/>
	 * ֻҪģ������prj��mes��������õ���ȷ·��*/
	public static String getTemplate4SaveRecord(Context ctx){
		if(null == sp)
			getSP(ctx);
		
		return sp.getString(template4SaveRecord, "");
	}

	/** ��ȡ�Ѽ��ص�ģ��������Ŀbean��json <br/>
	 * ���{@link getTemplate4SaveRecord} �� {@link getTemplateFromWhere}<br/>
	 * ֻҪģ������prj��mes��������õ���ȷ·�� */
	public static String getTemplate4SaveRecordParent(Context ctx){
		if(null == sp)
			getSP(ctx);
		
		return sp.getString(template4SaveRecordParent, "");
	}
	
	/** Ӧ���˳�ʱɾ����ǰload��prj */
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
		
		Log.e("ard", "SharedPreferences���");
	}
	
	


	/** ��ȡ����������õ���������
	 * @param key <br/>
	 *  1��'fileNameRule':prj����������Ĭ��"project"��<br/>
	 *  2��'measurementfileNameRule':measurement����������Ĭ��"measurement"��<br/>
	 *  3��'modeChange':prj������ģʽ��Ĭ��"0"-�Զ��塣1-Ĭ��<br/>
	 *  4��'measurementModeChange':measurement������ģʽ��Ĭ��"0"-�Զ��塣1-Ĭ��<br/>
	 *  5��'Dir':�洢λ��0-SD����1-�ֻ���<br/>
	 *  6��'project':��������
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
