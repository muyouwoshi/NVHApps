package left.drawer;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.OneTabFragment;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;

import common.CustomTab;

public class DisplayFragment extends Fragment implements OnClickListener,OnTouchListener{
	private View view=null;
	private ImageView delete = null;
	public ArrayList<Button> list = null;
	public ArrayList<Button> backlist = null;
	private GestureDetector mGestureDetector = null;
	private ViewHolder tag = null;
	public Display_Custom custom = null;
	public Display_Common common = null;
	//public ArrayList<ArrayList<String>> viewList = new ArrayList<ArrayList<String>>();
	private RelativeLayout Display_layout;
	MainFragment mainFragment;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mGestureDetector = new GestureDetector(getActivity(), new DefaultGestureListener());
		view = inflater.inflate(R.layout.display, container, false);
		common = new Display_Common(this);
		custom = new Display_Custom(this);
		list = new ArrayList<Button>();
		view.findViewById(R.id.Back_Display).setOnClickListener(this);
		list.add((Button) view.findViewById(R.id.imageView1));
		list.add((Button) view.findViewById(R.id.imageView2));
		list.add((Button) view.findViewById(R.id.imageView3));
		list.add((Button) view.findViewById(R.id.imageView4));
		list.add((Button) view.findViewById(R.id.imageView5));
		
		backlist = new ArrayList<Button>();
		backlist.add((Button) view.findViewById(R.id.back_imageView1));
		backlist.add((Button) view.findViewById(R.id.back_imageView2));
		backlist.add((Button) view.findViewById(R.id.back_imageView3));
		backlist.add((Button) view.findViewById(R.id.back_imageView4));
		backlist.add((Button) view.findViewById(R.id.back_imageView5));
		view.findViewById(R.id.Common).setOnClickListener(this);
		view.findViewById(R.id.Custom).setOnClickListener(this);
		Display_layout = (RelativeLayout) view.findViewById(R.id.Display_Layout);
		Display_layout.setOnTouchListener(this);
		for(int i = 0; i < list.size(); i ++) {
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Num = i;
			viewHolder.Name = "front";
			list.get(i).setTag(viewHolder);
			list.get(i).setOnTouchListener(this);
			list.get(i).setOnClickListener(this);
		}
		
		for(int i = 0; i < backlist.size(); i ++) {
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Num = i;
			viewHolder.Name = "back";
			backlist.get(i).setTag(viewHolder);
			backlist.get(i).setOnTouchListener(this);
		}
		pageSkinChanged();
		delete = (ImageView) view.findViewById(R.id.Display_Delete);
		initSelectedPages();
		
		return view;
	}
	@Override
	public void onClick(View v) {
		 mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("main");
		DisplayFragment displayFragment = (DisplayFragment) getFragmentManager().findFragmentByTag("display");
		switch(v.getId()) {
		case R.id.Back_Display:
			getFragmentManager().beginTransaction().hide(displayFragment).show(mainFragment).commit();
			RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(-1, -1);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			((MainActivity)getActivity()).leftDrawer.setLayoutParams(params);
			params=new RelativeLayout.LayoutParams(-1, -1);
			params.width=((MainActivity)getActivity()).screen_width-dip2px(255);
			params.height=-1;
			((MainActivity)getActivity()).leftHandle.setLayoutParams(params);
			common.setVisibility(false );
			custom.setVisibility(false);
			break;
		case R.id.Common:
			//如果custom布局显示则common布局不显示
			if(custom.getVisibility())return;
			//common显示与隐藏切换
			AddAlgorithmItems addAlgorithmItems1=new AddAlgorithmItems();
			addAlgorithmItems1.addItems(getActivity());
			common.setAddAlgorithmItems(addAlgorithmItems1);
			common.setVisibility( common.getVisibility() ? false : true );
			if(common.getVisibility()) {
				setLeftHandleWidth_0();
				return;
			}else{
				setLeftHandleWidth_1();
			}
			break;
		case R.id.Custom:
			//如果common布局显示则custom布局不显示
			if(common.getVisibility()) return;
			//custom显示与隐藏切换
			AddAlgorithmItems addAlgorithmItems=new AddAlgorithmItems();
			addAlgorithmItems.addItems(getActivity());
			custom.setAddAlgorithmItems(addAlgorithmItems);
			custom.spinnerItemPositionChanged();
			custom.setVisibility( custom.getVisibility() ? false : true );
			if(custom.getVisibility()) {
				setLeftHandleWidth_0();
				return;
			}else{
				setLeftHandleWidth_1();
			}
			break;
		case R.id.imageView1:
		case R.id.imageView2:
		case R.id.imageView3:
		case R.id.imageView4:
		case R.id.imageView5:
			((MainActivity)getActivity()).mainCustomTab.viewPager.setCurrentItem(((ViewHolder)v.getTag()).Num);
			break;
		}
	}
	private void setLeftHandleWidth_0(){
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(-1, -1);
		params.width=dip2px(255);
		params.height=-1;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		((MainActivity)getActivity()).leftDrawer.setLayoutParams(params);
		params=new RelativeLayout.LayoutParams(-1, -1);
		params.width=0;
		params.height=-1;
		((MainActivity)getActivity()).leftHandle.setLayoutParams(params);	
	}
	private void setLeftHandleWidth_1(){
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(-1, -1);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		((MainActivity)getActivity()).leftDrawer.setLayoutParams(params);
		params=new RelativeLayout.LayoutParams(-1, -1);
		params.width=((MainActivity)getActivity()).screen_width-dip2px(255);
		params.height=-1;
		((MainActivity)getActivity()).leftHandle.setLayoutParams(params);	
	}
	public int dip2px(float dpValue){
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private int left = 0,top = 0,right = 0,bottom = 0,startX = 0,startY = 0;
	private boolean amget = false;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//消除警告，对功能并无影响
		v.performClick();


		if(v.getId() == R.id.Display_Layout) return true;

		tag = (ViewHolder) v.getTag();
		if(tag.Name == "back") {
			mGestureDetector.onTouchEvent(event);
			return true;
		} else {
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				left   = v.getLeft();
			    top    = v.getTop();
			    right  = left + v.getWidth();
			    bottom = top  + v.getHeight();
			    startX = (int) event.getRawX();
			    startY = (int) event.getRawY();
			    			    
				break;
			case MotionEvent.ACTION_MOVE:
				int moveX = (int) event.getRawX() - startX;
				int moveY = (int) event.getRawY() - startY;
				v.layout(left + moveX, top + moveY, right + moveX, bottom + moveY);
				
				if(event.getRawX() > delete.getLeft() && event.getRawX() < delete.getRight() && event.getRawY() > delete.getTop() && event.getRawY() < delete.getBottom())
				{	
					if(!amget) {
						willDeleteAnimator(v);
				     	amget = true;
					}
				}else if(amget){
						cancleDeleteAnimator(v);
					amget = false;
				}
				
				break;
			case MotionEvent.ACTION_CANCEL:
				v.layout(left, top, right, bottom);
				
				if(amget){
					cancleDeleteAnimator(v);
					amget = false;
				}
				break;
			case MotionEvent.ACTION_UP:
				v.layout(left, top, right, bottom);
				if(event.getRawX() > delete.getLeft() && event.getRawX() < delete.getRight() && event.getRawY() > delete.getTop() && event.getRawY() < delete.getBottom())
				{
					int position = 0;
					for (View view : list) {
						if (view.getVisibility() == View.VISIBLE) {
							
							position ++;
						}
					}
					if (position == 0) return true;
					list.get(position - 1).setVisibility(View.GONE);
					
					((MainActivity)getActivity()).mainCustomTab.delete(((ViewHolder) v.getTag()).Num);
				}
				
				if(amget){
					cancleDeleteAnimator(v);
					amget = false;
				}
				
				break;
			}
			return true;
		}
	}
	
	public class DefaultGestureListener extends SimpleOnGestureListener {
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			for(int i = 0; i <= tag.Num; i++) {
				
				if (list.get(i).getVisibility() == View.GONE) {
					list.get(i).setVisibility(View.VISIBLE);
					ArrayList<String> defList = new ArrayList<String>();
					 mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("main");
					 if(mainFragment.getSpinnerValues().getAlgorithmItems().size()>1){
						defList.add(mainFragment.getSpinnerValues().getAlgorithmItems().get(0));//
					}else{
						defList.add("ChannelWatch");
					}
					//defList.add("Signal");
					 //if(mainFragment.languageNumber==1){
				     	((MainActivity)getActivity()).mainCustomTab
				     	.addTab(getActivity().getResources().getString(R.string.Main_Window),OneTabFragment.newInstance( defList, ((MainActivity)getActivity()).mainCustomTab.viewPager));
					 /*}else{
						/((MainActivity)getActivity()).mainCustomTab
					     	.addTab("窗口",OneTabFragment.newInstance( defList, ((MainActivity)getActivity()).mainCustomTab.viewPager));
					 }*/
				     	
				     showAnimator(list.get(i));
				}
			}
			pageSkinChanged();
			super.onLongPress(e);
		}
	}
	
	public class ViewHolder {
		int Num;
		String Name;
	}
	public void initSelectedPages(){
		for(int i=0;i<list.size();i++){
			Button b=list.get(i);
			b.setVisibility(View.GONE);
		}
		int pageCount=((MainActivity)getActivity()).mainCustomTab.getPageCount();
		for(int i=0;i<pageCount;i++){
			Button b=list.get(i);
			if(b.getVisibility()==View.GONE){
				b.setVisibility(View.VISIBLE);
			}
		}
		pageSkinChanged();
	}
	public void pageSkinChanged(){
		for (Button view : list) {
			if (view.getVisibility() == View.VISIBLE) {
				if(ThemeLogic.themeType==1){
					view.setBackgroundResource(R.drawable.imageView);
					view.setTextColor(Color.rgb(255, 255, 255));
			   }else{
				   view.setBackgroundResource(R.drawable.imageView1);
				   view.setTextColor(Color.rgb(225, 225, 225));
			   }
			}
			
		}
		for (Button view : backlist) {
			if (view.getVisibility() == View.VISIBLE) {
				if(ThemeLogic.themeType==1){
					view.setBackgroundResource(R.drawable.backimageView);
					view.setTextColor(Color.rgb(255, 255, 255));
			   }else{
				
				   view.setBackgroundResource(R.drawable.backimageView1);
				   view.setTextColor(Color.rgb(225, 225, 225));
			   }
			}
		}
	}
	public void skinChanged(TypedArray typedArray){
		view.findViewById(R.id.Display_layout).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_display,Color.YELLOW));
		view.findViewById(R.id.Display_Delete).setBackground(typedArray.getDrawable(R.styleable.myStyle_display_delete));
		view.findViewById(R.id.Display_title).setBackgroundColor(typedArray.getColor(R.styleable.myStyle_all_title,Color.YELLOW));
		custom.custom_layout_save.setBackground(typedArray.getDrawable(R.styleable.myStyle_copy));
		ImageButton Back_Display = (ImageButton) view.findViewById(R.id.Back_Display);
		Back_Display.setBackground(typedArray.getDrawable(R.styleable.myStyle_back_button));
		TextView Display_title = (TextView) view.findViewById(R.id.Display_title);
		Display_title.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Common = (TextView) view.findViewById(R.id.Common);
		Common.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		TextView Custom = (TextView) view.findViewById(R.id.Custom);
		Custom.setTextColor(typedArray.getColor(R.styleable.myStyle_all_title_text,Color.YELLOW));
		pageSkinChanged();
	}
	public void languageChanged(){
		((TextView)view.findViewById(R.id.Display_title)).setText(R.string.Display);
		((TextView)view.findViewById(R.id.Common)).setText(R.string.Common_Layout);
		((TextView)view.findViewById(R.id.Custom)).setText(R.string.Custom_Layout);
		if(custom!=null){
			custom.map_num_text.setText(R.string.Graph);
			custom.custom_layout_save.setText(R.string.Save);
		}
		if(common.popupWindow!=null){
			common.popupWindow.okButton.setText(R.string.Apply);
			common.popupWindow.deleteButton.setText(R.string.Delete);
		}
	}
	
	private void showAnimator(View v){
		
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 0.2f,1);
     	scaleX.setDuration(500);
     	scaleX.start();
     	
     	ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 0.2f,1);
     	scaleY.setDuration(500);
     	scaleY.start();
     	
     	ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 0.2f,1);
     	alpha.setDuration(500);
     	alpha.start();
	}
	
	private void willDeleteAnimator(View v){
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(delete, "scaleX", 1.5f);
     	scaleX.setDuration(300);
     	scaleX.start();
     	
     	ObjectAnimator scaleY = ObjectAnimator.ofFloat(delete, "scaleY", 1.5f);
     	scaleY.setDuration(300);
     	scaleY.start();
     	
     	ObjectAnimator alpha = ObjectAnimator.ofFloat(delete, "alpha", 1,0.2f,1);
     	alpha.setDuration(300);
     	alpha.start();
     	
     	ObjectAnimator vscaleX = ObjectAnimator.ofFloat(v, "scaleX", 0.5f);
     	vscaleX.setDuration(300);
     	vscaleX.start();
     	
     	ObjectAnimator vscaleY = ObjectAnimator.ofFloat(v, "scaleY", 0.5f);
     	vscaleY.setDuration(300);
     	vscaleY.start();
	}
	
	private void cancleDeleteAnimator(View v){	
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(delete, "scaleX", 1);
	 	scaleX.setDuration(500);
	 	scaleX.start();
	 	
	 	ObjectAnimator scaleY = ObjectAnimator.ofFloat(delete, "scaleY", 1);
	 	scaleY.setDuration(500);
	 	scaleY.start();
	 	
	 	ObjectAnimator alpha = ObjectAnimator.ofFloat(delete, "alpha", 1f);
	 	alpha.setDuration(1000);
	 	alpha.start();
	 	
	 	ObjectAnimator vscaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f);
     	vscaleX.setDuration(500);
     	vscaleX.start();
     	
     	ObjectAnimator vscaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f);
     	vscaleY.setDuration(500);
     	vscaleY.start();
	}	
}
