package left.drawer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.OneTabFragment;
import com.example.mobileacquisition.R;
import common.CustomTab;

public class Display_Common implements OnClickListener{

//	private static final String Integer = null;
	private RelativeLayout layout = null;
	private ArrayList<RelativeLayout> layoutList = null;
	private TableLayout child = null;
	private DisplayMetrics dm = new DisplayMetrics();
	private DisplayFragment df = null;
	private int screenHeight = 0;
	private float scale;
	public EditPopWindow popupWindow;
	private AddAlgorithmItems addAlgorithmItems;
	public Display_Common(DisplayFragment df) {
		
		this.df = df;
		df.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeight = dm.heightPixels;
		scale = dm.density;
		layout = (RelativeLayout) df.getActivity().findViewById(R.id.maintab);
		child = (TableLayout) LayoutInflater.from(df.getActivity()).inflate(R.layout.common_layout, layout, false);
		
		layoutList = new ArrayList<RelativeLayout>();
		layoutList.add((RelativeLayout) child.findViewById(R.id.display_common_ul));
		layoutList.add((RelativeLayout) child.findViewById(R.id.display_common_ur));
		layoutList.add((RelativeLayout) child.findViewById(R.id.display_common_dl));
		layoutList.add((RelativeLayout) child.findViewById(R.id.display_common_dr));
		
		for(RelativeLayout layout : layoutList) {
			layout.setOnClickListener(this);
		}
		
		LayoutParams params = new LayoutParams(-1,-1);
		params.width = screenHeight * 3 / 5;
		params.height = screenHeight * 3 / 5;
		params.leftMargin = dip2px(310);
		params.addRule(RelativeLayout.CENTER_VERTICAL, -1);
		layout.addView(child, params);
		child.setVisibility(View.GONE);
		
		readFromTemplate();
	}
	
	public int dip2px(float dpValue) {
		return (int)(dpValue * scale +0.5f);
	}

	public void setVisibility(Boolean visible) {
		child.setVisibility( visible ? View.VISIBLE : View.GONE );
	}
	
	public void addView(ArrayList<String> viewList) {
		
		for(int i = 0; i < layoutList.size(); i ++) {
			if(((TextView)layoutList.get(i).getChildAt(0)).getText().toString().equals("NULL")) {
				layoutList.get(i).removeAllViews();
				CreateChildViewAndAdd(layoutList.get(i),viewList);
				layoutList.get(i).setTag(viewList);
				
				saveTemplate(i,viewList);

				
				Toast.makeText(df.getActivity(), R.string.Saved_Successfully, Toast.LENGTH_SHORT).show();
				return;
								
			}
		}
		Toast.makeText(df.getActivity(), R.string.Saved_Failed, Toast.LENGTH_SHORT).show();
		
	}

	private void saveTemplate(int layoutNum, ArrayList<String> viewList) {
		// TODO Auto-generated method stub
		SharedPreferences preference = df.getActivity().getSharedPreferences("hz_5D",0);
		SharedPreferences.Editor editor = preference.edit();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			ObjectOutputStream os = new ObjectOutputStream(baos);
			os.writeObject(viewList);
			String list = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
			os.close();
			baos.close();
			
			editor.putString("Common_Layout"+layoutNum, list);
			editor.commit();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void CreateChildViewAndAdd(RelativeLayout layout,ArrayList<String> list) {
		int textSize = 10;
		int fatherWidth = screenHeight * 3 / 5-40;
		switch(list.size()) {
		case 1:
			TextView view1 = new TextView(df.getActivity());
			RelativeLayout.LayoutParams params1 = new LayoutParams(-1,-1);
			view1.setText(list.get(0));
			view1.setLayoutParams(params1);
			view1.setBackgroundColor(Color.rgb(255, 100, 200));
			view1.setGravity(Gravity.CENTER);
			layout.addView(view1);
			view1.setTextSize(textSize);
			break;
		case 2:
			TextView view2 = new TextView(df.getActivity());
			RelativeLayout.LayoutParams params2 = new LayoutParams(-1,-1);
			params2.width =fatherWidth/4;
			params2.height = fatherWidth/2;
			params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			view2.setText(list.get(0));
			view2.setLayoutParams(params2);
			view2.setBackgroundColor(Color.rgb(200, 100, 50));
			view2.setGravity(Gravity.CENTER);
			layout.addView(view2);
			view2.setTextSize(textSize);

			view2 = new TextView(df.getActivity());
			params2 = new LayoutParams(-1,-1);
			params2.width = fatherWidth/4;
			params2.height = fatherWidth/2;
			params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			view2.setText(list.get(1));
			view2.setLayoutParams(params2);
			view2.setBackgroundColor(Color.rgb(150, 150, 200));
			view2.setGravity(Gravity.CENTER);
			layout.addView(view2);
			view2.setTextSize(textSize);

			break;
		case 3:
			TextView view3 = new TextView(df.getActivity());
			RelativeLayout.LayoutParams params3 = new LayoutParams(-1,-1);
			params3.width = fatherWidth/4;
			params3.height = fatherWidth/4;
			params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			params3.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
			view3.setText(list.get(0));
			view3.setLayoutParams(params3);
			view3.setBackgroundColor(Color.rgb(250, 155, 50));
			view3.setGravity(Gravity.CENTER);
			layout.addView(view3);
			view3.setTextSize(textSize);

			view3 = new TextView(df.getActivity());
			params3 = new LayoutParams(-1,-1);
			params3.width = fatherWidth/4;
			params3.height = fatherWidth/4;
			params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
			view3.setText(list.get(2));
			view3.setLayoutParams(params3);
			view3.setBackgroundColor(Color.rgb(0, 255, 150));
			view3.setGravity(Gravity.CENTER);
			layout.addView(view3);
			view3.setTextSize(textSize);
			
			view3 = new TextView(df.getActivity());
			params3 = new LayoutParams(-1,-1);
			params3.width = fatherWidth/4;
			params3.height = fatherWidth/2;
			params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			view3.setText(list.get(1));
			view3.setLayoutParams(params3);
			view3.setBackgroundColor(Color.rgb(50, 155, 50));
			view3.setGravity(Gravity.CENTER);
			layout.addView(view3);
			view3.setTextSize(textSize);

			break;
		case 4:
			TextView view4 = new TextView(df.getActivity());
			RelativeLayout.LayoutParams params4 = new LayoutParams(-1,-1);
			params4.width = fatherWidth/4;
			params4.height = fatherWidth/4;
			params4.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			params4.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
			view4.setText(list.get(0));
			view4.setLayoutParams(params4);
			view4.setBackgroundColor(Color.rgb(250, 50, 100));
			view4.setGravity(Gravity.CENTER);
			layout.addView(view4);
			view4.setTextSize(textSize);

			view4 = new TextView(df.getActivity());
			params4 = new LayoutParams(-1,-1);
			params4.width = fatherWidth/4;
			params4.height = fatherWidth/4;
			params4.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
			view4.setText(list.get(2));
			view4.setLayoutParams(params4);
			view4.setBackgroundColor(Color.rgb(250, 250, 0));
			view4.setGravity(Gravity.CENTER);
			layout.addView(view4);
			view4.setTextSize(textSize);

			view4 = new TextView(df.getActivity());
			params4 = new LayoutParams(-1,-1);
			params4.width = fatherWidth/4;
			params4.height = fatherWidth/4;
			params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			params4.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
			view4.setText(list.get(1));
			view4.setLayoutParams(params4);
			view4.setBackgroundColor(Color.rgb(160, 150, 50));
			view4.setGravity(Gravity.CENTER);
			layout.addView(view4);
			view4.setTextSize(textSize);

			view4 = new TextView(df.getActivity());
			params4 = new LayoutParams(-1,-1);
			params4.width = fatherWidth/4;
			params4.height = fatherWidth/4;
			params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
			view4.setText(list.get(3));
			view4.setLayoutParams(params4);
			view4.setBackgroundColor(Color.rgb(100, 180, 100));
			view4.setGravity(Gravity.CENTER);
			layout.addView(view4);
			view4.setTextSize(textSize);

			break;
			
		}
	}
	
	public Boolean getVisibility() {
		return child.getVisibility() == 0 ? true : false;
	}

	@Override
	public void onClick(View v) {
		TextView text = (TextView)((ViewGroup) v).getChildAt(0);
		if(text.getText().equals("NULL")) return;
	    popupWindow = new EditPopWindow(df.getActivity(),v,this);
	    popupWindow.showPopupWindow();
	    
	}
	public void aplay(View v){
		ArrayList<String> str = (ArrayList<String>) v.getTag();
		ArrayList<String> no_contains= new ArrayList<String>();
		for(int i=0;i<str.size();i++){
			if(addAlgorithmItems.getAlgorithmItems().size()==0
					||!addAlgorithmItems.getAlgorithmItems().contains(str.get(i))){
				no_contains.add(str.get(i));
				HashSet h = new  HashSet(no_contains);     
				no_contains.clear();     
				no_contains.addAll(h); 
			}
		}
	     if(no_contains.size()!=0){
	    	 Toast.makeText(df.getActivity().getApplicationContext(),
						no_contains.toString()+df.getActivity().getResources().
						getString(R.string.algorithm_close),
						Toast.LENGTH_SHORT)
						.show();
				return;
	     }
	    CustomTab customTab = ((MainActivity)df.getActivity()).mainCustomTab;
	  	int position = customTab.viewPager.getCurrentItem();
	  	if(customTab.getTabCount()==0){
	  		Toast.makeText(df.getActivity(), R.string.load_page_message,Toast.LENGTH_SHORT).show();  
	  		return;
	  	}else{
	  		customTab.setPage(position);
	  		customTab.replace(position, OneTabFragment.newInstance( str, customTab.viewPager)); 
	  	}
	  	
	  	
  		
	}

	public void delete(View v) {
        
		ViewGroup layout = (ViewGroup)v;
		layout.removeAllViews();
		TextView view1 = new TextView(df.getActivity());
		RelativeLayout.LayoutParams params1 = new LayoutParams(-1,-1);
		view1.setText("NULL");
		view1.setLayoutParams(params1);
		view1.setBackgroundColor(Color.rgb(255, 255, 255));
		view1.setGravity(Gravity.CENTER);
		layout.addView(view1);
		layout.setTag(null);
		
		removeFormTemplate(v);
		
	}
	private void removeFormTemplate(View v) {
		// TODO Auto-generated method stub
		for(int i = 0; i< layoutList.size();i++){
			if(v == layoutList.get(i)){
				SharedPreferences.Editor editor = df.getActivity().getSharedPreferences("hz_5D", 0).edit();
				editor.remove("Common_Layout"+i);
				editor.commit();
			}
		}
	}

	public void setAddAlgorithmItems(AddAlgorithmItems addAlgorithmItems) {
		// TODO Auto-generated method stub
		this.addAlgorithmItems=addAlgorithmItems;
	}
	
	public void readFromTemplate(){
//		List<ArrayList<String>> viewList= new ArrayList<ArrayList<String>>();
		SharedPreferences preferences = df.getActivity().getSharedPreferences("hz_5D", 0);
		
		for(int i = 0;i <layoutList.size();i++){
			String str = preferences.getString("Common_Layout"+i, "NULL");
			if(str.equals("NULL")) delete(layoutList.get(i));
			else{
				ArrayList<String> viewList = getPreDisplayStringList(str);
				
				layoutList.get(i).removeAllViews();
				CreateChildViewAndAdd(layoutList.get(i),viewList);
				layoutList.get(i).setTag(viewList);
			}
		}
		
	}
	
	private ArrayList<String> getPreDisplayStringList(String oldString) {
		if (oldString == null || oldString.equals("")) {
			return null;
		}
		byte[] bytes = Base64.decode(oldString, Base64.DEFAULT);
		ArrayList<String> displaylist = null;
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				bytes);
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
		} catch (StreamCorruptedException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
			
		}
		try {
			displaylist = (ArrayList<String>) objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return displaylist;
	}
}
