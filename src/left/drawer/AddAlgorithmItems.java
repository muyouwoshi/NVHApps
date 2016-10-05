package left.drawer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Spinner;

public class AddAlgorithmItems {
	public ArrayList<String> algorithmItems = new ArrayList<String>();
	public void addItems(Activity context){
		SharedPreferences preference =context.getSharedPreferences("hz_5D", 0);
		String algorithmChecked = preference.getString("Signal", "close");
		if(algorithmChecked.equals("open")) {
			addAlgorithmItems("Signal");
		}else{
			removeAlgorithmItems("Signal");
		}
		algorithmChecked = preference.getString("SPL", "close");
		if(algorithmChecked.equals("open")) {
			addAlgorithmItems("SPL");
		}else{
			removeAlgorithmItems("SPL");
		}
		algorithmChecked = preference.getString("OCT", "close");
		if(algorithmChecked.equals("open")) {
			addAlgorithmItems("OCT");
		}else{
			removeAlgorithmItems("OCT");
		}
		algorithmChecked = preference.getString("FFT", "close");
		if(algorithmChecked.equals("open")) {
			addAlgorithmItems("FFT");
		}else{
			removeAlgorithmItems("FFT");
		}
		algorithmChecked = preference.getString("AI", "close");
		if(algorithmChecked.equals("open")) {
			addAlgorithmItems("AI");
		}else{
			removeAlgorithmItems("AI");
		}
		algorithmChecked = preference.getString("RPM", "close");
		if(algorithmChecked.equals("open")) {
			addAlgorithmItems("RPM");
			
		}else{
			removeAlgorithmItems("RPM");
		}
		algorithmChecked = preference.getString("Waterfall", "close");
		if(algorithmChecked.equals("open")) {
			addAlgorithmItems("Waterfall");
		}else{
			removeAlgorithmItems("Waterfall");
		}
		algorithmChecked = preference.getString("Order", "close");
		if(algorithmChecked.equals("open")) {
			addAlgorithmItems("Order");
		}else{
			removeAlgorithmItems("Order");
		}
	}
	private void addAlgorithmItems(String str){
		if(!algorithmItems.contains(str)){
			algorithmItems.add(str);
		}
	}
	private void removeAlgorithmItems(String str){
		for(int i=0;i<algorithmItems.size();i++){
			if(algorithmItems.get(i).equals(str)){
				algorithmItems.remove(i);
			}
		}
	}
	public ArrayList<String> getAlgorithmItems() {
		return algorithmItems;
	}
	
	public void noPageOne(){
		if(algorithmItems.contains("Waterfall")
				||algorithmItems.contains("OCT")){
			removeAlgorithmItems("Waterfall");
			removeAlgorithmItems("OCT");
		}
	}
}
