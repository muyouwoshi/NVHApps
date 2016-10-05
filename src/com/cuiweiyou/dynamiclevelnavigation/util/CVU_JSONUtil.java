package com.cuiweiyou.dynamiclevelnavigation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cuiweiyou.dynamiclevelnavigation.bean.CVU_BeanNameAndPath;

/**
 * <b>类名</b>: JSONUtil，<br/>
 * <b>说明</b>: json和bean的互转 <br/>
 */
public class CVU_JSONUtil {

	private CVU_JSONUtil() {
	}
	
	public static String bean2Json(CVU_BeanNameAndPath bean){
		JSONObject jo = new JSONObject();
		
		String name = bean.getName();
		String createTime = bean.getCreateTime();
		String path = bean.getPath();
		
		try {
			jo.put("name", name);
			jo.put("createTime", createTime);
			jo.put("path", path);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jo.toString();
	}
	
	public static CVU_BeanNameAndPath json2Bean(String json){
		CVU_BeanNameAndPath bean = null;
		
		JSONObject jo;
		try {
			jo = new JSONObject(json);
			String name = jo.getString("name");
			String createTime = jo.getString("createTime");
			String path = jo.getString("path");
			bean = new CVU_BeanNameAndPath(name, createTime, path);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public static String orderMap2json(Map<String, List<String>> map){
		StringBuilder sb = new StringBuilder();
		
		Iterator<String> its = map.keySet().iterator();
		while(its.hasNext()){
			String key = its.next();
			sb.append("{\"channel\":\"" + key + "\",\"orders\":[{");
			List<String> list = map.get(key);
			for (int i = 0; i < list.size(); i++) {
				String value = list.get(i);
				sb.append("\"" + i + "\"" + ":" + "\"" + value + "\",");
			}
			
			if(",".equals(String.valueOf(sb.charAt(sb.length()-1)))){
				sb.setLength(sb.length() - 1);
			}
			
			sb.append("}]}");
		}
		
		return sb.toString();
	}

	public static Map<String, List<String>> json2OrderMap(String json){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		try {
			JSONObject jo = new JSONObject(json);
			String channel = jo.getString("channel");
			JSONArray ja = jo.getJSONArray("orders");
			int length = ja.length();
			for (int i = 0; i < length; i++) {
				JSONObject job = ja.getJSONObject(0);
				Iterator keys = job.keys();
				List<String> list = new ArrayList<String>();
				while(keys.hasNext()){
					String key = (String) keys.next();
					String value = job.getString(key);
					list.add(value);
				}
				map.put(channel, list);
			}
		} catch (JSONException e) {
			Log.e("ard", "CVU_JSONUtil，Json转Map异常：" + e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
}
