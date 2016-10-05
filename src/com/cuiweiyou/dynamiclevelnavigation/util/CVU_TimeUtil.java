package com.cuiweiyou.dynamiclevelnavigation.util;

import java.text.SimpleDateFormat;

/** ��ȡ��ǰϵͳ��ʱ��� */
public class CVU_TimeUtil {

	private CVU_TimeUtil() {
	}
	
	/**
	 * <b>����</b>: getTimestamp����ȡ��ǰϵͳ��ʱ���<br/>
	 * <b>˵��</b>: ��ʽ��������_ʱ���룩��20160217_101999 ��15λ <br/>
	 * <b>����</b>: 2016-2-17_����10:18:43 <br/>
	 * @return : ʱ����ִ�
	 * @author : weiyou.cui@ts-tech.com.cn <br/>
	 * @version 1 <br/>
	 */
	public static String getTimestamp(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");//�������ڸ�ʽ
		return df.format(new java.util.Date());
	}
}
