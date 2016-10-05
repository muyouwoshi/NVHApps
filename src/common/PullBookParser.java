package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import android.util.Log;
import android.util.Xml;

public class PullBookParser {
	private Map<String,String> map = new HashMap<String,String>();
	
	public PullBookParser(String filePath){
		File file = new File(filePath);
		InputStream is = null;
		if(file.exists()){
			try {  
				is = new FileInputStream(file);

				XmlPullParser xpp = Xml.newPullParser();
				xpp.setInput(is, "UTF-8");
				int eventType = xpp.getEventType();
				
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
				    	case XmlPullParser.START_DOCUMENT: // �жϵ�ǰ�¼��Ƿ�Ϊ�ĵ���ʼ�¼�
				    		break;
				    	case XmlPullParser.START_TAG: // �жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�ؿ�ʼ�¼�
				    		if (xpp.getName().equals("string")) { // �жϿ�ʼ ��ǩԪ��(�ڵ�����) �Ƿ��ǡ�string��
						    
				    			String attributeName = xpp.getAttributeName(0);
				    			if ("name".equals(attributeName)) { 
				    				String value = xpp.getAttributeValue(null, attributeName);
				    				String text = xpp.nextText();
				    				if(text == null) text = "null";				    					
				    				map.put(value,text);
				    			}
				    		}	
				    		if(xpp.getName().equals("int")){
				    			String attributeName = xpp.getAttributeName(0);
				    			String attributeValue = xpp.getAttributeName(1);
				    			if("name".equals(attributeName) && "value".equals(attributeValue)){
				    				String value = xpp.getAttributeValue(null, attributeName);
				    				String text = xpp.getAttributeValue(null, attributeValue);
				    				if(text == null) text = "null";				    					
				    				map.put(value,text);
				    			}	
				    		}
				    		break;
				    	case XmlPullParser.END_TAG: // �жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�ؽ����¼�
				    		
				    		break;
				    }
					eventType = xpp.next(); // ������һ��Ԫ�ز�������Ӧ�¼�
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					if(is!= null) is.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getString(String name, String value){
		if(map.keySet().contains(name)){
			value = map.get(name);
		}
		return value;
	}
	
	public int getInt(String name,int value){
		if(map.keySet().contains(name)){
			try{
				value = Integer.parseInt(map.get(name));
			}catch(Exception e){
				e.printStackTrace();
				return value;
			}
		}
		return value;
	}
	
	public Map<String,String> getValueMap(){
		return map;
	}
}
