package left.drawer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import common.CustomTab;

public class PreLeftDisplayFragment extends Fragment implements OnClickListener,OnCheckedChangeListener {
	private View view = null;
	private LayoutInflater inflater = null;
	private AlgorithmItemsPopWindow algorithmPopupWindow;
	public AddAlgorithmItems addAlgorithmItems;
	private CheckBox page1, page2, page3, page4, page5;
	public Button save,restore;
	private LinearLayout page_context;
	private ArrayList<ViewHolder> viewHolders;
	private ArrayList<ViewHolder> checkedPage;
	private int pageCount;
	private Activity context;
	private SharedPreferences.Editor editor;
	private boolean isAutoSelect = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.layout_display_settings, container,
				false);
		context=this.getActivity();
		viewHolders = new ArrayList<ViewHolder>();
		addAlgorithmItems=new AddAlgorithmItems();
		addAlgorithmItems.addItems(context);
		page_context = (LinearLayout) view.findViewById(R.id.page_context);
		page1 = (CheckBox) view.findViewById(R.id.page1);
		page2 = (CheckBox) view.findViewById(R.id.page2);
		page3 = (CheckBox) view.findViewById(R.id.page3);
		page4 = (CheckBox) view.findViewById(R.id.page4);
		page5 = (CheckBox) view.findViewById(R.id.page5);
		save=(Button)view.findViewById(R.id.Save);
		restore=(Button)view.findViewById(R.id.Restore_default);
		page1.setOnCheckedChangeListener(this);
		page2.setOnCheckedChangeListener(this);
		page3.setOnCheckedChangeListener(this);
		page4.setOnCheckedChangeListener(this);
		page5.setOnCheckedChangeListener(this);
		save.setOnClickListener(this);
		restore.setOnClickListener(this);
		//initDisplayFragment();
		SharedPreferences prefenences =context.getSharedPreferences("displayInfo", 0);
		int pagecount = prefenences.getInt("pageCount", 0);
		if (pagecount == 0) {
			pagecount=1;
			for(int i=pageCount;i<=4;i++){
				addViewList();
				checkPageCheckBox(1, true);
			}
		}else{
			initDisplayFragment();
		}
	//	addAlgorithmItemsChanged();
		return view;
	}

	public void addViewList() {
			ViewHolder viewHolder = new ViewHolder();
			View pageView = inflater.inflate(R.layout.layout_display, page_context,
					false);
			page_context.addView(pageView);
			viewHolder.page_layout = (LinearLayout) pageView
					.findViewById(R.id.page_layout);
			viewHolder.figure_textview = (TextView) pageView
					.findViewById(R.id.figure_textview);
			viewHolder.algorithm_textview = (TextView) pageView
					.findViewById(R.id.algorithm_textview);
			viewHolder.algorithm_textView1 = (TextView) pageView
					.findViewById(R.id.algorithm_textView1);
			viewHolder.algorithm_textView2 = (TextView) pageView
					.findViewById(R.id.algorithm_textView2);
			viewHolder.algorithm_textView3 = (TextView) pageView
					.findViewById(R.id.algorithm_textView3);
			viewHolder.algorithm_textView4 = (TextView) pageView
					.findViewById(R.id.algorithm_textView4);
			viewHolder.algorithm_textView1.setText(addAlgorithmItems.getAlgorithmItems().get(0));
			viewHolder.algorithm_textView2.setText(addAlgorithmItems.getAlgorithmItems().get(0));
			viewHolder.algorithm_textView3.setText(addAlgorithmItems.getAlgorithmItems().get(0));
			viewHolder.algorithm_textView4.setText(addAlgorithmItems.getAlgorithmItems().get(0));
			viewHolder.algorithm_textView1.setOnClickListener(this);
			viewHolder.algorithm_textView2.setOnClickListener(this);
			viewHolder.algorithm_textView3.setOnClickListener(this);
			viewHolder.algorithm_textView4.setOnClickListener(this);
			viewHolder.rightLayout = (LinearLayout) pageView
					.findViewById(R.id.right_layout);
			viewHolder.figure_spinner = (Spinner) pageView
					.findViewById(R.id.figure_spinner);
			SpinnerListener spinnerListener = new SpinnerListener();
			spinnerListener.setLinearLayout(viewHolder.rightLayout,
					viewHolder.algorithm_textView1, viewHolder.algorithm_textView2,
					viewHolder.algorithm_textView3, viewHolder.algorithm_textView4,viewHolder.textviewList);
			viewHolder.figure_spinner.setOnItemSelectedListener(spinnerListener);
			viewHolder.figure_spinner.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					isAutoSelect = true;
					return false;
				}
	
	        });
			viewHolders.add(viewHolder);
			viewHolder.algorithm_textView1.setClickable(false);
			viewHolder.algorithm_textView2.setClickable(false);
			viewHolder.algorithm_textView3.setClickable(false);
			viewHolder.algorithm_textView4.setClickable(false);
			viewHolder.figure_spinner.setEnabled(false);
	}

	public class ViewHolder {
		public LinearLayout page_layout;
		public TextView figure_textview;
		public Spinner figure_spinner;
		public LinearLayout rightLayout;
		public TextView algorithm_textview;
		public TextView algorithm_textView1;
		public TextView algorithm_textView2;
		public TextView algorithm_textView3;
		public TextView algorithm_textView4;
		public ArrayList<TextView> textviewList=new ArrayList<TextView>();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	switch(v.getId()){
		case R.id.Save:
			//����
			if(checkedPage.size()==0||checkedPage==null){
				Toast.makeText(getActivity(),
						R.string.Save_settings_failed, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			saveLayout();
			Toast.makeText(getActivity(),
					R.string.Save_settings, Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.Restore_default://��ԭ
			if(checkedPage==null){
				return;
			}
			while(checkedPage.size()>1){
				int pageTag=viewHolders.indexOf(checkedPage.get(1));
				for(int i=0;i<checkedPage.size();i++){
					checkedPage.get(i).figure_spinner.setSelection(0);
				}
				switch(pageTag){
				/*case 0:
					//page1.setButtonDrawable(R.drawable.checked);
					//page1.setChecked(false);
					//isPageChecked(false, 0);
					break;*/
				case 1:
					page2.setButtonDrawable(R.drawable.checked);
					page2.setChecked(false);
					isPageChecked(false, 1);
					break;
				case 2:
					page3.setButtonDrawable(R.drawable.checked);
					page3.setChecked(false);
					isPageChecked(false, 2);
					break;
				case 3:
					page4.setButtonDrawable(R.drawable.checked);
					page4.setChecked(false);
					isPageChecked(false, 3);
					break;
				case 4:
					page5.setButtonDrawable(R.drawable.checked);
					page5.setChecked(false);
					isPageChecked(false, 4);
					break;
				}	
			}
			page1.setButtonDrawable(R.drawable.checked_active);
			page1.setChecked(true);
			//isPageChecked(true, 0);
			viewHolders.get(0).figure_spinner.setSelection(0);
			viewHolders.get(0).algorithm_textView1.setText(addAlgorithmItems.getAlgorithmItems().get(0));
			viewHolders.get(0).textviewList.clear();
			viewHolders.get(0).textviewList.add(viewHolders.get(0).algorithm_textView1);
			saveLayout();
			Toast.makeText(
					getActivity(),R.string.Recover_settings,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.algorithm_textView1:
			if(editor==null){
				editor=context.getSharedPreferences("displayInfo", Context.MODE_PRIVATE).edit();
			}
			TextView text1=(TextView)v;
			System.out.println(text1.getText().toString());
		case R.id.algorithm_textView2:
			if(editor==null){
				editor=context.getSharedPreferences("displayInfo", Context.MODE_PRIVATE).edit();
			}
		case R.id.algorithm_textView3:
			if(editor==null){
				editor=context.getSharedPreferences("displayInfo", Context.MODE_PRIVATE).edit();
			}
		case R.id.algorithm_textView4:
			if(editor==null){
				editor=context.getSharedPreferences("displayInfo", Context.MODE_PRIVATE).edit();
			}
			
			
			algorithmPopupWindow = new AlgorithmItemsPopWindow(getActivity(), v);
			//AddAlgorithmItems addAlgorithmItems=new AddAlgorithmItems();
			//addAlgorithmItems.addItems(context);
			if(addAlgorithmItems.getAlgorithmItems().size()!=0){//isChecked&&
				algorithmPopupWindow.InitListView(addAlgorithmItems.getAlgorithmItems());
				algorithmPopupWindow.showPopupWindow2();
			}
			break;
		}
	}
	private void saveLayout(){
		if(editor==null){
			editor=context.getSharedPreferences("displayInfo", Context.MODE_PRIVATE).edit();
		}
		int pageNum = 0;
		
		for(ViewHolder value: checkedPage){
			ArrayList<String> str = new ArrayList<String>();
			Iterator<TextView> it=value.textviewList.iterator();
			while(it.hasNext()){
				str.add(((TextView)it.next()).getText().toString());
			}
//			Set<String> aaa=new LinkedHashSet(value.textviewList);
//			((MainActivity)getActivity()).mainCustomTab.replace(checkedPage.indexOf(value),
//					OneTabFragment.newInstance( str, ((MainActivity)getActivity()).mainCustomTab.viewPager));
		
//			editor.putStringSet("page"+String.valueOf(pageNum),aaa);	
			ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
			try {
				ObjectOutputStream objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(str);
				String displaylist=new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
				objectOutputStream.close();
				editor.putString("page"+String.valueOf(pageNum), displaylist);
				SharedPreferences.Editor tempEditor = context.getSharedPreferences("hz_5D", 0).edit();
				tempEditor.putString("page"+String.valueOf(pageNum), displaylist);
				tempEditor.commit();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pageNum++;
		}			
		editor.putInt("pageCount", pageNum);
		editor.commit();
		SharedPreferences.Editor tempEditor = context.getSharedPreferences("hz_5D", 0).edit();
		tempEditor.putString("PageCount", ""+pageNum);
		tempEditor.commit();
		
		((MainActivity)context).mainCustomTab.clearForReset();
		((MainActivity)context).addMainCustomTab();
		((MainActivity)context).mainFragment.getLeftExplandableListView().getDisplayFragment().initSelectedPages();
		
	}

	public void isPageChecked(boolean isChecked, int pageTag) {
		if(checkedPage==null){
			checkedPage=new ArrayList<ViewHolder>();
		}
		viewHolders.get(pageTag).algorithm_textView1.setClickable(isChecked);
		viewHolders.get(pageTag).algorithm_textView2.setClickable(isChecked);
		viewHolders.get(pageTag).algorithm_textView3.setClickable(isChecked);
		viewHolders.get(pageTag).algorithm_textView4.setClickable(isChecked);
		viewHolders.get(pageTag).figure_spinner.setEnabled(isChecked);
		if (isChecked) {
			viewHolders.get(pageTag).page_layout
					.setBackgroundResource(R.drawable.page_01);
			viewHolders.get(pageTag).figure_textview.setTextColor(Color.BLACK);
			viewHolders.get(pageTag).algorithm_textview
					.setTextColor(Color.BLACK);
			viewHolders.get(pageTag).algorithm_textView1
					.setTextColor(Color.BLACK);
			viewHolders.get(pageTag).algorithm_textView2
					.setTextColor(Color.BLACK);
			viewHolders.get(pageTag).algorithm_textView3
					.setTextColor(Color.BLACK);
			viewHolders.get(pageTag).algorithm_textView4
					.setTextColor(Color.BLACK);
			viewHolders.get(pageTag).algorithm_textView1
					.setBackgroundResource(R.drawable.bg_suanfa);
			viewHolders.get(pageTag).algorithm_textView2
					.setBackgroundResource(R.drawable.bg_suanfa);
			viewHolders.get(pageTag).algorithm_textView3
					.setBackgroundResource(R.drawable.bg_suanfa);
			viewHolders.get(pageTag).algorithm_textView4
					.setBackgroundResource(R.drawable.bg_suanfa);
			checkedPage.add(viewHolders.get(pageTag));
			
		} else {
			viewHolders.get(pageTag).page_layout
					.setBackgroundResource(R.drawable.page_02);
			viewHolders.get(pageTag).figure_textview.setTextColor(Color
					.parseColor("#DBDBDB"));
			viewHolders.get(pageTag).algorithm_textview.setTextColor(Color
					.parseColor("#DBDBDB"));
			viewHolders.get(pageTag).algorithm_textView1.setTextColor(Color
					.parseColor("#DBDBDB"));			
			viewHolders.get(pageTag).algorithm_textView2.setTextColor(Color
					.parseColor("#DBDBDB"));			
			viewHolders.get(pageTag).algorithm_textView3.setTextColor(Color
					.parseColor("#DBDBDB"));
			viewHolders.get(pageTag).algorithm_textView4.setTextColor(Color
					.parseColor("#DBDBDB"));
			
			viewHolders.get(pageTag).algorithm_textView1
					.setBackgroundResource(R.drawable.bg_suanfa2);
			viewHolders.get(pageTag).algorithm_textView2
					.setBackgroundResource(R.drawable.bg_suanfa2);
			viewHolders.get(pageTag).algorithm_textView3
					.setBackgroundResource(R.drawable.bg_suanfa2);
			viewHolders.get(pageTag).algorithm_textView4
					.setBackgroundResource(R.drawable.bg_suanfa2);
			checkedPage.remove(viewHolders.get(pageTag));
		}
		
	}
	private class SpinnerListener implements OnItemSelectedListener {
		private LinearLayout rightLayout;
		private TextView algorithmView1, algorithmView2, algorithmView3,
				algorithmView4;
		private ArrayList<TextView> textviewList;
		public void setLinearLayout(LinearLayout linearLayout,
				TextView textView1, TextView textView2, TextView textView3,
				TextView textView4,ArrayList<TextView> textviewList) {
			this.rightLayout = linearLayout;
			this.algorithmView1 = textView1;
			this.algorithmView2 = textView2;
			this.algorithmView3 = textView3;
			this.algorithmView4 = textView4;
			this.textviewList=textviewList;
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Spinner spinner = (Spinner) arg0;
			String data = (String) spinner.getItemAtPosition(arg2);
			DisplayMetrics metrics =context.getResources().getDisplayMetrics();
	        float xdpi = metrics.xdpi;
	        int width = metrics.widthPixels;
	        int height = metrics.heightPixels;
	        double z = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
	        double eqequipment_size = Math.round(z /xdpi);
			switch (arg2) {
			case 0:
				rightLayout.setVisibility(View.GONE);
				algorithmView1.setVisibility(View.VISIBLE);
				algorithmView2.setVisibility(View.GONE);
				algorithmView3.setVisibility(View.GONE);
				algorithmView4.setVisibility(View.GONE);
				textviewList.clear();
				textviewList.add(algorithmView1);
				if(isAutoSelect){
					algorithmView1.setText(addAlgorithmItems.getAlgorithmItems().get(0));
				}
		        if(eqequipment_size >7) return;
				addAlgorithmItems.addItems(context);
				break;
			case 1:
				rightLayout.setVisibility(View.VISIBLE);
				algorithmView1.setVisibility(View.VISIBLE);
				algorithmView2.setVisibility(View.GONE);
				algorithmView3.setVisibility(View.VISIBLE);
				algorithmView4.setVisibility(View.GONE);
				textviewList.clear();
				textviewList.add(algorithmView1);
				textviewList.add(algorithmView3);
				if(isAutoSelect){
					algorithmView1.setText(addAlgorithmItems.getAlgorithmItems().get(0));
					algorithmView3.setText(addAlgorithmItems.getAlgorithmItems().get(0));
				}
				if(eqequipment_size >7) return;
				addAlgorithmItems.noPageOne();
				break;
			case 2:
				rightLayout.setVisibility(View.VISIBLE);
				algorithmView1.setVisibility(View.VISIBLE);
				algorithmView2.setVisibility(View.GONE);
				algorithmView3.setVisibility(View.VISIBLE);
				algorithmView4.setVisibility(View.VISIBLE);
				textviewList.clear();
				textviewList.add(algorithmView1);
				textviewList.add(algorithmView3);
				textviewList.add(algorithmView4);
				if(isAutoSelect){
					algorithmView1.setText(addAlgorithmItems.getAlgorithmItems().get(0));
					algorithmView3.setText(addAlgorithmItems.getAlgorithmItems().get(0));
					algorithmView4.setText(addAlgorithmItems.getAlgorithmItems().get(0));
				}
				if(eqequipment_size >7) return;
				addAlgorithmItems.noPageOne();
				break;
			case 3:
				rightLayout.setVisibility(View.VISIBLE);
				algorithmView1.setVisibility(View.VISIBLE);
				algorithmView2.setVisibility(View.VISIBLE);
				algorithmView3.setVisibility(View.VISIBLE);
				algorithmView4.setVisibility(View.VISIBLE);
				textviewList.clear();
				textviewList.add(algorithmView1);
				textviewList.add(algorithmView3);
				textviewList.add(algorithmView2);
				textviewList.add(algorithmView4);
				if(isAutoSelect){
					algorithmView1.setText(addAlgorithmItems.getAlgorithmItems().get(0));
					algorithmView3.setText(addAlgorithmItems.getAlgorithmItems().get(0));
					algorithmView4.setText(addAlgorithmItems.getAlgorithmItems().get(0));
					algorithmView2.setText(addAlgorithmItems.getAlgorithmItems().get(0));
				}
				if(eqequipment_size >7) return;
				addAlgorithmItems.noPageOne();
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	private void checkPageCheckBox(int pageIndex, boolean visible) {
		switch (pageIndex) {
		case 1:
			page1.setChecked(visible);
			break;
		case 2:
			page2.setChecked(visible);
			break;
		case 3:
			page3.setChecked(visible);
			break;
		case 4:
			page4.setChecked(visible);
			break;
		case 5:
			page5.setChecked(visible);
			break;
		default:
			page1.setChecked(visible);
			break;
		}
	}

	private Handler setPageChangeHandler() {
		Handler pageChangeHandler = new Handler() {
			public void handleMessage(Message age) {
				switch (age.what) {
				case 0:
					pageCount += 1;
					checkPageCheckBox(pageCount, true);
					break;
				case 1:
					checkPageCheckBox(pageCount,false);
					pageCount -= 1;
					break;
				}
			}
		};
		return pageChangeHandler;
	}

	private void initDisplayFragment() {
		CustomTab cus = ((MainActivity) getActivity()).mainCustomTab;
		pageCount = cus.getTabCount();
		for (int i = 0; i < pageCount; i++) {//���ģ�年ѡҳ��
			addViewList();
			checkPageCheckBox(i + 1, true);
		}
		
		cus.setHandlerFromPreLeftDisplay(setPageChangeHandler());
		SharedPreferences prefenences = context.getSharedPreferences("displayInfo", 0);
		int i=0;
		List<String> str = null;
		for(ViewHolder viewholder:viewHolders){
			str = getPreDisplayStringList(prefenences.getString(
					"page" + String.valueOf(i), null));
			if(str==null){//||str.size()==1&&str.get(0).equals("Signal")
				break;
			}
			if(page1.isChecked()||page2.isChecked()||page3.isChecked()||page4.isChecked()||page5.isChecked()){
			int nn=viewholder.figure_spinner.getFirstVisiblePosition();
			viewholder.figure_spinner.setSelection(str.size()-1);
			int num=viewholder.figure_spinner.getSelectedItemPosition();
			if(str.size()>0){
				viewholder.algorithm_textView1.setText(str.get(0));
			}
			if(str.size()>1){
				viewholder.algorithm_textView3.setText(str.get(1));
			}
			if(str.size()>2){
				viewholder.algorithm_textView4.setText(str.get(2));
			}
			if(str.size()>3){
				viewholder.algorithm_textView2.setText(str.get(3));
			}
			i++;
		}
	  }
		for(int j=pageCount;j<=4;j++){//����ģ��δ��ѡ��ҳ��
			addViewList();
			checkPageCheckBox(j + 1, false);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean check) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.page1:
			if (check==true) {
				page1.setButtonDrawable(R.drawable.checked_active);
				isPageChecked(true, 0);
			} else {
				page1.setButtonDrawable(R.drawable.checked);
				isPageChecked(false, 0);
				page2.setChecked(false);
				page3.setChecked(false);
				page4.setChecked(false);
				page5.setChecked(false);
			}
			break;
		case R.id.page2:
			if (check==true) {
				page2.setButtonDrawable(R.drawable.checked_active);
				isPageChecked(true, 1);
				page1.setChecked(true);
			} else {
				page2.setButtonDrawable(R.drawable.checked);
				isPageChecked(false, 1);
				page3.setChecked(false);
				page4.setChecked(false);
				page5.setChecked(false);
			}
			break;
		case R.id.page3:
			if (check==true) {
				page3.setButtonDrawable(R.drawable.checked_active);
				isPageChecked(true, 2);
				page1.setChecked(true);
				page2.setChecked(true);
			} else {
				page3.setButtonDrawable(R.drawable.checked);
				isPageChecked(false, 2);
				page4.setChecked(false);
				page5.setChecked(false);
			}
			break;
		case R.id.page4:
			if (check==true) {
				page4.setButtonDrawable(R.drawable.checked_active);
				isPageChecked(true, 3);
				page1.setChecked(true);
				page2.setChecked(true);
				page3.setChecked(true);

			} else {
				page4.setButtonDrawable(R.drawable.checked);
				isPageChecked(false, 3);
				page5.setChecked(false);
			}
			break;
		case R.id.page5:
			if (check==true) {
				page5.setButtonDrawable(R.drawable.checked_active);
				isPageChecked(true, 4);
				page1.setChecked(true);
				page2.setChecked(true);
				page3.setChecked(true);
				page4.setChecked(true);

			} else {
				page5.setButtonDrawable(R.drawable.checked);
				isPageChecked(false, 4);
			}
		}
	}
	private List<String> getPreDisplayStringList(String oldString) {
		if (oldString == null || oldString.equals("")) {
			return null;
		}
		byte[] bytes = Base64.decode(oldString, Base64.DEFAULT);
		List displaylist = null;
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				bytes);
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
		} catch (StreamCorruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			displaylist = (ArrayList) objectInputStream.readObject();
			objectInputStream.close();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return displaylist;
	}
	public void languageChanged(){
		for(int i=0;i<viewHolders.size();i++){
			viewHolders.get(i).figure_textview.setText(R.string.Graph);
			viewHolders.get(i).algorithm_textview.setText(R.string.Algorithm);
		}
		save.setText(R.string.Save);
		restore.setText(R.string.Recover);
	}
}