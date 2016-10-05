package mode.autorange;

import java.util.ArrayList;
import java.util.List;

import left.drawer.AnalogFragment;
import left.drawer.CustomSpinner;

import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.ThemeLogic;
import com.example.mobileacquisition.ThemeLogic.ThemeChangeListener;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

@SuppressLint("NewApi")
public class RangeLayout extends Fragment implements OnClickListener ,ThemeChangeListener{
	private Activity context;
	private View rangecontrol_view;
	private View autorangeFrame_view;
	private boolean ifInitAdapter = false;
	private RangePageViewAdapter rangePageViewAdapter;
	private List<View> rangePageViewList;
	private List<RangeSurfaceView> rangeSurfaceViewList;
	private RangeSurfaceView rangeSurfaceView;
	private int pageCount;
	private int sendpagecount = 0;
	private int restChannelCount;
	private int height;
	private int channelNum = 0;
	private Handler viewPageHandler;
	private int channelMostInOnePage = 0;
	private RelativeLayout textview_autorange;
	private LinearLayout auto_range;
	private AutoRangeDataCollection autoRangeDataCollection;
	private int pageID;
	public TextView title_autorange;
	public Button recall_autorange,maxdata_autorange,addgear_autorange,auto_range_return_bt;
	private  ArrayList<List<Integer>> channelRangeLevelList;
	public ArrayList<List<Integer>> getChannelRangeLevelList() {
		return channelRangeLevelList;
	}

	public void setChannelRangeLevelList(
			ArrayList<List<Integer>> channelRangeLevelList) {
		this.channelRangeLevelList = channelRangeLevelList;
	}

	public RangeLayout(Activity context) {
		this.context = context;
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rangecontrol_view = inflater.inflate(R.layout.auto_range, container,
				false);
		title_autorange=(TextView)rangecontrol_view.findViewById(R.id.title_autorange);
		View bottomDrawer = context.findViewById(R.id.bottomDrawer);
		View function_autorange = rangecontrol_view.findViewById(R.id.function_autorange);
		auto_range = (LinearLayout) rangecontrol_view.findViewById(R.id.auto_range);
		textview_autorange = (RelativeLayout) rangecontrol_view.findViewById(R.id.textview_autorange);
		auto_range_return_bt= (Button)rangecontrol_view.findViewById(R.id.auto_range_return_bt);
		auto_range_return_bt.setOnClickListener(this);
		switch(ThemeLogic.themeType){
		case 1:
			rangecontrol_view.getContext().setTheme(R.style.mode1);
			break;
		case 2:
			rangecontrol_view.getContext().setTheme(R.style.mode2);
		}

		/*
		 * 换肤
		 */
		String getSkinValues = getActivity().getIntent().getStringExtra(
				"SkinIntent");
		if (getSkinValues != null) {
			if (getSkinValues.equals("Skin0")) {
				// auto_range.setBackgroundResource(R.color.white);
				// textview_autorange.setBackgroundColor(Color.RED);
				// function_autorange.setBackgroundColor(Color.RED);
			} else if (getSkinValues.equals("Skin1")) {
				// auto_range.setBackgroundColor(Color.LTGRAY);
				// textview_autorange.setBackgroundColor(Color.YELLOW);
				// function_autorange.setBackgroundColor(Color.YELLOW);
			}
		}

		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// float scale = context.getResources().getDisplayMetrics().density;
		// int width = dm.widthPixels;
		int height = dm.heightPixels;
		int a = bottomDrawer.getLayoutParams().height;
		int b = function_autorange.getLayoutParams().height;
		int c = textview_autorange.getLayoutParams().height;

		height = height - a - b - c;
		setPageViewAdapter(height);
		setviewHandler();
		recall_autorange = (Button)rangecontrol_view.findViewById(R.id.recall_autorange);
		recall_autorange.setOnClickListener(this);
		maxdata_autorange = (Button)rangecontrol_view.findViewById(R.id.maxdata_autorange);
		maxdata_autorange.setOnClickListener(this);
		addgear_autorange = (Button)rangecontrol_view.findViewById(R.id.addgear_autorange);
		addgear_autorange.setOnClickListener(this);
		autorangeFrame_view = context.getLayoutInflater().inflate(
				R.layout.drawpageviews_autorange, null);
		int displaywidth = ((WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		channelMostInOnePage = (int) Math.ceil(displaywidth / (100 + 100)) + 1;// 100是每个直方图的间距，100是直方图的宽度
		ThemeLogic.getInstance().addListener(this);
		return rangecontrol_view;
	}

	@SuppressWarnings("deprecation")
	private void setSurface(FrameLayout frameLayout, Activity context,
			int height, int channelCountInOnePage, int pageID) {

		if (rangeSurfaceViewList == null) {
			rangeSurfaceViewList = new ArrayList<RangeSurfaceView>();
		}
		rangeSurfaceView = new RangeSurfaceView(context, channelCountInOnePage,
				pageID, channelMostInOnePage);
		frameLayout.addView(rangeSurfaceView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		rangeSurfaceViewList.add(rangeSurfaceView);
		rangeSurfaceView.setFrameLayout(frameLayout);
		if(autoRangeDataCollection!=null){
			rangeSurfaceView.setAutoRangeDataCollection(autoRangeDataCollection);
		}
		ViewGroup root = (ViewGroup) context.findViewById(android.R.id.content);
		// --------------添加popwindow------------
		View rangeControl = getActivity().getLayoutInflater().inflate(// popwindow
				R.layout.rangecontrol_autorange, null);
		frameLayout.addView(rangeControl, 100, 500);
		RelativeLayout staffLayout = (RelativeLayout) rangeControl
				.findViewById(R.id.staff);
		staffLayout.addView(new PopupWindowView(getActivity()),
				new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
		// ------------------完毕--------------
		frameLayout.getChildAt(1).setX(0);
		frameLayout.getChildAt(1).setY(0);
		frameLayout.getChildAt(1).setVisibility(View.GONE);
	}

	private void setPageViewAdapter(int height) {
		ViewGroup.LayoutParams params1 = ((MainActivity)context).bottomHandle
				.getLayoutParams();
		params1.height = 0;
		((MainActivity)context).bottomHandle.setLayoutParams(params1);

		rangePageViewList = new ArrayList<View>();
		rangePageViewAdapter = new RangePageViewAdapter(rangePageViewList);
		ViewPager viewPager_autorange = (ViewPager) rangecontrol_view
				.findViewById(R.id.drawpages_autorange);
		ViewGroup.LayoutParams params = viewPager_autorange.getLayoutParams();
		params.height = height;
		viewPager_autorange.setLayoutParams(params);

		viewPager_autorange.setAdapter(rangePageViewAdapter);
		viewPager_autorange
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub
						FrameLayout frameLayout = (FrameLayout) rangePageViewAdapter
								.getView();
						rangeSurfaceView = (RangeSurfaceView) frameLayout
								.getChildAt(0);

						if (rangeSurfaceView != null) {
							// if (rangeSurfaceView.getRangeControlPopWindow()
							// != null
							// && rangeSurfaceView
							// .getRangeControlPopWindow()
							// .isShowing()) {
							// rangeSurfaceView.getRangeControlPopWindow()
							// .dismiss();
							// }
						}

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		SharedPreferences preference = getActivity().getSharedPreferences("hz_5D", 0);
		switch (v.getId()) {
		case R.id.recall_autorange:
			if(autoRangeDataCollection==null)return;
			List<Integer> channelListRecall = autoRangeDataCollection
					.getChannelList();
			List<Integer> levelList = autoRangeDataCollection.getLevelList();
			if(channelRangeLevelList.size()==0){
				Toast.makeText(context,R.string.last_result_not_exist, 0).show();
				return;
			}
			for(int i=0;i<channelListRecall.size();i++){
				levelList.set(i, channelRangeLevelList.get(0).get(i));
				rangeSurfaceView.setLevel(channelRangeLevelList.get(0).get(i), channelListRecall.get(i));
			}
			autoRangeDataCollection.setLevelList(levelList);
			int indexChannel = rangeSurfaceView.touchedChannelID;
			if (levelList.size()>0&&channelListRecall.size()>0&&indexChannel > -1) {
				rangeSurfaceView.changeStaffView(levelList
						.get(channelListRecall.indexOf(indexChannel)));
			}
			Toast.makeText(context, R.string.Settings_succeeded, 0).show();
			break;
		case R.id.maxdata_autorange:	
			if (rangeSurfaceView != null) {
				
				List<Integer> channelListMaxData = autoRangeDataCollection
						.getChannelList();
				List<Integer> levelListMaxData = autoRangeDataCollection
						.getLevelList();
				int level = rangeSurfaceView.getLevel();
				if((!levelListMaxData.contains(1)&&!levelListMaxData.contains(2))&&level==0){
					Toast.makeText(context,context.getResources().getString(R.string.Channel) 
									+ context.getResources().getString(R.string.maximum_message),0).show();
					return;
				}
				for (int i = 0; i < channelListMaxData.size(); i++) {
					levelListMaxData.set(i, 0);
					rangeSurfaceView.setLevel(0, channelListMaxData.get(i));
				}
				rangeSurfaceView.changeStaffView(0);
				List<Integer> levelListMaxData_2 = new ArrayList<Integer>();
				for(int i=0;i<levelListMaxData.size();i++){
					levelListMaxData_2.add(levelListMaxData.get(i));
				}
				autoRangeDataCollection.setLevelList(levelListMaxData);
				Toast.makeText(context, R.string.Settings_succeeded, 0).show();
			}
			
			break;
		case R.id.addgear_autorange:
			if (rangeSurfaceView != null) {
				List<Integer> channelListAddGear = autoRangeDataCollection
						.getChannelList();
				List<Integer> levelListAddGear = autoRangeDataCollection
						.getLevelList();
				int level = rangeSurfaceView.getLevel();
				/*if(level==0){
					Toast.makeText(context,context.getResources().getString(R.string.Channel) 
									+ (rangeSurfaceView.touchedChannelID+1)
									+ context.getResources().getString(R.string.maximum_message),0).show();
					return;
				}*/
				if((!levelListAddGear.contains(1)&&!levelListAddGear.contains(2))&&level==0){
					Toast.makeText(context,context.getResources().getString(R.string.Channel) 
									+ context.getResources().getString(R.string.maximum_message),0).show();
					return;
				}
				for(int i=0;i<levelListAddGear.size();i++){
				if(levelListAddGear.get(i)==0){
					/*Toast.makeText(context,context.getResources().getString(R.string.Channel) 
									+ (i+1)
									+ context.getResources().getString(R.string.maximum_message),0).show();*/
					rangeSurfaceView.changeStaffView(0);
					levelListAddGear.set(i, 0);
				}else{
					rangeSurfaceView.changeStaffView(levelListAddGear.get(i)-1);
					levelListAddGear.set(i, levelListAddGear.get(i)-1);
				}
					rangeSurfaceView.setLevel(levelListAddGear.get(i), channelListAddGear.get(i));
				}
					List<Integer> levelListAddGear_2 = new ArrayList<Integer>();
					for(int i=0;i<levelListAddGear.size();i++){
						levelListAddGear_2.add(levelListAddGear.get(i));
					}
					autoRangeDataCollection.setLevelList(levelListAddGear);
					Toast.makeText(context, R.string.Settings_succeeded, 0).show();
				}
			break;
		case R.id.auto_range_return_bt:
			((MainActivity)context).bottomOperate.rangeLayoutReturn();
			break;
		}
	}
	public List<RangeSurfaceView> getRangeSurfaceViewList() {
		return rangeSurfaceViewList;
	}

	private void setSurfacePage() {
		autorangeFrame_view = getActivity().getLayoutInflater().inflate(
				R.layout.drawpageviews_autorange, null);
		if (sendpagecount != pageCount - 1) {
			setSurface(
					(FrameLayout) (autorangeFrame_view
							.findViewById(R.id.draw_autorange)),
					getActivity(), height, channelMostInOnePage, sendpagecount);
		} else {
			if (restChannelCount > 0) {
				setSurface(
						(FrameLayout) (autorangeFrame_view
								.findViewById(R.id.draw_autorange)),
						getActivity(), height, restChannelCount, sendpagecount);

			} else {
				setSurface(
						(FrameLayout) (autorangeFrame_view
								.findViewById(R.id.draw_autorange)),
						getActivity(), height, channelMostInOnePage,
						sendpagecount);

			}
		}
		rangePageViewList.add(autorangeFrame_view);
		rangePageViewAdapter.notifyDataSetChanged();
	}

	private void setviewHandler() {
		viewPageHandler = new Handler() {
			public void handleMessage(Message msg) {
				int channelCount = autoRangeDataCollection.getChannelSize();
				if (channelNum != channelCount) {
					channelNum = channelCount;
					pageCount = (int) Math.ceil((float) channelNum
							/ channelMostInOnePage);
					restChannelCount = channelNum % channelMostInOnePage;
				}
				if (sendpagecount < pageCount) {
					setSurfacePage();
					rangeSurfaceView.setVoltageDateList(
							autoRangeDataCollection.getVoltageDateList(),
							autoRangeDataCollection.getLevelList(),
							autoRangeDataCollection.getChannelList());
					sendpagecount++;
				} else {
					rangeSurfaceView.setVoltageDateList(
							autoRangeDataCollection.getVoltageDateList(),
							autoRangeDataCollection.getLevelList(),
							autoRangeDataCollection.getChannelList());
					rangeSurfaceView.invalidate();
				}
			}
		};
		Log.v("bug11", "msg1:"+viewPageHandler.hashCode());

	}
	public RangeSurfaceView getRangeSurfaceView() {
		return rangeSurfaceView;
	}

	public Handler getViewPageHandler() {
		return viewPageHandler;
	}

	public void setAutoRangeDataCollection( AutoRangeDataCollection autoRangeDataCollection) {
		this.autoRangeDataCollection = autoRangeDataCollection;
		
	}
	
	public void languageChanged(){
		title_autorange.setText(R.string.title_autorange);
		addgear_autorange.setText(R.string.addgear_autorange);
		maxdata_autorange.setText(R.string.maxdata_autorange);
		recall_autorange.setText(R.string.recall_autorange);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	@Override
	public void onThemeChanged() {
		switch (ThemeLogic.themeType) {

		/** 1閵嗗倸鍘涢弴瀛樻煀娑撳顣�閿涗緤绱掗敓锟� */
		case 1:
			rangecontrol_view.getContext().setTheme(R.style.mode1);
			break;
		case 2:
			rangecontrol_view.getContext().setTheme(R.style.mode2);
			break;
		}
		
		/** 2.閸愬秵褰侀崣鏍х潣閿燂拷? */
		TypedArray typedArray = rangecontrol_view.getContext().obtainStyledAttributes(R.styleable.myStyle);
		textview_autorange.setBackgroundColor(typedArray.getColor(R.styleable.myStyle_TabUpLayout,Color.YELLOW));
		recall_autorange.setBackground(typedArray.getDrawable(R.styleable.myStyle_copy));
		maxdata_autorange.setBackground(typedArray.getDrawable(R.styleable.myStyle_paste));
		addgear_autorange.setBackground(typedArray.getDrawable(R.styleable.myStyle_check_all));
		addgear_autorange.setTextColor(typedArray.getColor(R.styleable.myStyle_textColor,Color.YELLOW));
		typedArray.recycle();
		
	}
}
