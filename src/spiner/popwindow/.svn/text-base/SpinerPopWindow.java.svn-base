package spiner.popwindow;

import java.util.HashSet;
import java.util.List;

import spiner.popwindow.AbstractSpinerAdapter.IOnItemSelectListener;

import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class SpinerPopWindow extends PopupWindow implements OnItemClickListener,ThemeChangeListener{

	private Context mContext;
	public ListView mListView;
	private NormalSpinerAdapter mAdapter;
	private IOnItemSelectListener mItemSelectListener;
	private View view;
	public RelativeLayout pop_window;
	public SpinerPopWindow(Context context){
		super(context);	
		mContext = context;
		init();
	}
	
	
	public void setItemListener(IOnItemSelectListener listener){
		mItemSelectListener = listener;
	}
	
	
	private void init(){
		 view = LayoutInflater.from(mContext).inflate(R.layout.spiner_window_layout, null);
		switch(ThemeLogic.themeType){
		case 1:
			mContext.setTheme(R.style.mode1);
			break;
		case 2:
			mContext.setTheme(R.style.mode2);
		}
		setContentView(view);		
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		
		setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
	
	   pop_window = (RelativeLayout) view.findViewById(R.id.pop_window);
		mListView = (ListView) view.findViewById(R.id.listview);
		  if(ThemeLogic.themeType==1){
			   mListView.setSelector(R.drawable.spinnerDivider1);
			   pop_window.setBackgroundResource(R.drawable.input_box);
		   }else{
			   mListView.setSelector(R.drawable.spinnerDivider);
			   mListView.setDividerHeight(3);
			   pop_window.setBackgroundResource(R.drawable.input_box1);
		   }
		mAdapter = new NormalSpinerAdapter(mContext);	
		mListView.setAdapter(mAdapter);	
		mListView.setOnItemClickListener(this);
		ThemeLogic.getInstance().addListener(this);
	}
	
	
	public void refreshData(List<String> list, int selIndex){
		if (list != null && selIndex  != -1){
			mAdapter.refreshData(list, selIndex);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		dismiss();
		if (mItemSelectListener != null){
			mItemSelectListener.onItemClick(pos);
		}
	}


	@SuppressLint("NewApi")
	@Override
	public void onThemeChanged() {
		switch (ThemeLogic.themeType) {

		/** 1閵嗗倸鍘涢弴瀛樻煀娑撳顣?閿涗緤绱掗敓锟? */
		case 1:
			mContext.setTheme(R.style.mode1);
			
			break;
		case 2:
			mContext.setTheme(R.style.mode2);
			break;
		}
		  if(ThemeLogic.themeType==1){
			   mListView.setSelector(R.drawable.spinnerDivider1);
			   pop_window.setBackgroundResource(R.drawable.input_box);
		   }else{
			   mListView.setSelector(R.drawable.spinnerDivider);
			   mListView.setDividerHeight(3);
			   pop_window.setBackgroundResource(R.drawable.input_box);
		   }
		TypedArray typedArray = mContext.obtainStyledAttributes(R.styleable.myStyle);
//		RelativeLayout pop_window = (RelativeLayout) view.findViewById(R.id.pop_window);
//		pop_window.setBackground(typedArray.getDrawable(R.styleable.myStyle_pop_window1));
		typedArray.recycle();
		
	}



	
}
