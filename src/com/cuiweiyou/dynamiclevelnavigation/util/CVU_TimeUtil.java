package com.cuiweiyou.dynamiclevelnavigation.util;

import java.text.SimpleDateFormat;

/** 获取当前系统的时间戳 */
public class CVU_TimeUtil {

	private CVU_TimeUtil() {
	}
	
	/**
	 * <b>功能</b>: getTimestamp，获取当前系统的时间戳<br/>
	 * <b>说明</b>: 形式（年月日_时分秒）：20160217_101999 共15位 <br/>
	 * <b>创建</b>: 2016-2-17_上午10:18:43 <br/>
	 * @return : 时间戳字串
	 * @author : weiyou.cui@ts-tech.com.cn <br/>
	 * @version 1 <br/>
	 */
	public static String getTimestamp(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");//设置日期格式
		return df.format(new java.util.Date());
	}
}
