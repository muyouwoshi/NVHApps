package common;

import java.util.HashMap;
import java.util.Iterator;

public class XclSingalTransfer {
	//单例设计模式
	private static XclSingalTransfer xclSingalTransfer=null;
	private HashMap<String,Object> dataMap;
	private XclSingalTransfer(){
		dataMap=new HashMap<String,Object>();
	}
	public static XclSingalTransfer getInstance(){
		if(xclSingalTransfer==null){
			synchronized(XclSingalTransfer.class){
				if(xclSingalTransfer==null){
					xclSingalTransfer=new XclSingalTransfer();
				}
			}
		}
		return xclSingalTransfer;
	}
	public void putValue(String key,Object value){
		dataMap.put(key, value);
	}
	public Object getValue(String key){
		return dataMap.get(key);
	}
	public boolean containsKey(String key){
		return dataMap.containsKey(key)?true:false;
	}
	public int getSize(){
		return dataMap.size();
	}
	public void clearMap(){
		dataMap.clear();
	}
	public HashMap<String,Object> getMap(){
		return dataMap;
	}
	public String getFirstKey(){
		String index = null;
		Iterator it=dataMap.keySet().iterator();
		index=(String)it.next();
		return index;
	}
	public String getFirstIndex(){
		String index = null;
		Iterator it=dataMap.keySet().iterator();
		while(it.hasNext()){
			if(((String)it.next()).matches("[0-9]+")){
				index=(String)it.next();
				break;
			}
		}
		return index;
	}
}
