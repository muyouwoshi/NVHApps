package com.cuiweiyou.dynamiclevelnavigation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CVU_StringUtil {

	/** �����ִ�Сд�滻 
	 * @param source ԭ
	 * @param oldstring ���滻��
	 * @param newstring �µ�
	 * @return */
	public static String ignoreCaseReplace(String source, String oldstring, String newstring) {
		Pattern p = Pattern.compile(oldstring, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);
		String ret = m.replaceAll(newstring);
		return ret;
	}
}
