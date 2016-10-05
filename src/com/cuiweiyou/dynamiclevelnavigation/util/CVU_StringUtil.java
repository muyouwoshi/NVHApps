package com.cuiweiyou.dynamiclevelnavigation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CVU_StringUtil {

	/** 不区分大小写替换 
	 * @param source 原
	 * @param oldstring 被替换的
	 * @param newstring 新的
	 * @return */
	public static String ignoreCaseReplace(String source, String oldstring, String newstring) {
		Pattern p = Pattern.compile(oldstring, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);
		String ret = m.replaceAll(newstring);
		return ret;
	}
}
