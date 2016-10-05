package mode.autorange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import left.drawer.AnalogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mobileacquisition.R;
import common.XclSingalTransfer;

public class RangeSurfaceView extends View implements OnTouchListener,
		OnLongClickListener {
	private Activity context;
	 private RangeControlPopWindow rangeControlPopWindow;
	private int rectTop;
	private int rectBottom;
	private int pageID;
	private int channelCountInOnePage;
	private List<Float> rectLeftList = new ArrayList<Float>();
	private List<Float> rectRightList = new ArrayList<Float>();
	private List<Float> rectTopList = new ArrayList<Float>();
	private int rawY;
	private int rawX;
	private float arrawtop;
//	private int dy = 0;
	private int firstTop;
	private int firstBottom;
	private int secondBottom;
	private int level = 0;
	private FrameLayout frameLayout;
	private View popview;
	private View staffview;//���
	private ImageButton arrow;
	private float[] channelsData;
	private int color;
	private float lowData;
	private float highData;
	private Paint paint;
	private Paint channelDataPaint;
	private List<Float> voltageDateList;
	private List<Integer> levelList;
	private List<Integer> channelList;
	private int channelMostInOnePage;
	private RelativeLayout title_autorange;
	private LinearLayout function_autorange;
	private float rect_top;
	private float rect_left;
	private float rect_right;
	private float rect_bottom;
	private float vol;
	private int channelIndex;
	protected int touchedChannelID=-1;
	private Handler rangehandler;
	/*private List<RangeSurfaceView> rangeSurfaceViewList;
	public List<RangeSurfaceView> getRangeSurfaceViewList() {
		return rangeSurfaceViewList;
	}

	public void setRangeSurfaceViewList(List<RangeSurfaceView> rangeSurfaceViewList) {
		this.rangeSurfaceViewList = rangeSurfaceViewList;
	}*/
	private AutoRangeDataCollection autoRangeDataCollection;
	public AutoRangeDataCollection getAutoRangeDataCollection() {
		return autoRangeDataCollection;
	}

	public void setAutoRangeDataCollection(
			AutoRangeDataCollection autoRangeDataCollection) {
		this.autoRangeDataCollection = autoRangeDataCollection;
	}

	public RangeSurfaceView(Activity context, int channelCountInOnePage,
			int pageID, int channelMostInOnePage) {
		super(context);
		this.context = context;
		this.pageID = pageID;
		this.channelCountInOnePage = channelCountInOnePage;
		this.channelMostInOnePage = channelMostInOnePage;
		// TODO Auto-generated constructor stub
		this.setOnTouchListener(this);
		this.setOnLongClickListener(this);
		channelsData = new float[channelCountInOnePage];
		paint = new Paint();
		channelDataPaint = new Paint();
		channelDataPaint.setColor(Color.BLACK);
		channelDataPaint.setTextSize(30);
		title_autorange = (RelativeLayout) context
				.findViewById(R.id.textview_autorange);
		function_autorange = (LinearLayout) context
				.findViewById(R.id.function_autorange);
	}

	@Override
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (voltageDateList == null||levelList==null||channelList==null) {
			return;
		}
		if(voltageDateList.size()==0||channelList.size()==0||levelList.size()==0){
			return;
		}
		int i = 0;
		while(i<channelCountInOnePage){
			channelIndex=channelList.get(i)+1;
			vol=voltageDateList.get(i);
			// --------------------���ֱ��ͼ״̬-----------------------------------------------
			switch (levelList.get(i)) {
			case 0:// ���
				lowData = 0.01f;
				highData = 0.085f;
				break;
			case 1:// ��ͨ
				lowData = 0.1f;
				highData = 0.85f;
				break;
			case 2:// ���
				lowData = 1f;
				highData = 8.5f;
				break;
			}
			color = (vol <= lowData) ? Color.BLACK
					: (vol >= highData) ? Color.RED : Color.GREEN;
			paint.setColor(color);
			//---------------------����ֱ��ͼ----------------------
			rect_left = this.getWidth() / 100 + i * 200;
			rect_right = rect_left + 100;// ֱ��ͼ���100;
			rect_bottom = canvas.getHeight() - 100;
			rect_top = rect_bottom - vol;
			canvas.drawRect(rect_left, rect_top, rect_right, rect_bottom, paint);
			//----------------���Ƹ�ͨ����ѹֵ--------------
			if (vol < 10) {//��ѹ��ֵС��10�����
				canvas.drawText(String.valueOf(vol), rect_left + 18, rect_top
						- title_autorange.getHeight() / 2, channelDataPaint);
			} else {//��ѹ��ֵ����10�����
				canvas.drawText(String.valueOf(vol), rect_left + 13, rect_top
						- title_autorange.getHeight() / 2, channelDataPaint);
			}
			//-----------���Ƹ�ͨ��id------------------
			if (channelIndex< 10) {//ͨ��idС��10�����
				canvas.drawText(
						String.valueOf(channelIndex),
						rect_left+(rect_right-rect_left)/3,
						rect_bottom
								+ (function_autorange.getTop()
										- rect_bottom)/3,
						channelDataPaint);
			} else {//ͨ��id����10�����
				canvas.drawText(
						String.valueOf(channelIndex),
						rect_left+(rect_right-rect_left)/3,
						rect_bottom
								+  (function_autorange.getTop()
										- rect_bottom)/3,
						channelDataPaint);
			}
			if(rectLeftList.indexOf(rect_left)<0){
				rectLeftList.add(rect_left);
			}
			if(rectRightList.indexOf(rect_right)<0){
				rectRightList.add(rect_right);
			}
			if(rectTopList.indexOf(rectTopList)<0){
				rectTopList.add(rect_top);
			}
			i++;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			rawY = (int) event.getRawY();
			rawX = (int) event.getRawX();
		}
		return false;
	}

	private void openPopView(float x, int y,int levelIndex) {
		level=levelList.get(levelIndex);
		touchedChannelID=channelList.get(levelIndex);//indexOf
		if (popview == null) {
			popview = frameLayout.getChildAt(1);
		//	popview.setBackgroundColor(Color.BLUE);
			RelativeLayout staffLayout=(RelativeLayout)popview.findViewById(R.id.staff);
			staffview=staffLayout.getChildAt(0);
			arrow = (ImageButton) popview.findViewById(R.id.rangeControlButton);
			popview.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					float staffTop=	((PopupWindowView)staffview).getPopStaffTop();
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							if (event.getRawY() <=staffTop+staffTop/2) {// ��
								arrawtop = staffTop;
								level = 0;
								Toast.makeText(context, R.string.v_10,0).show();
							} else if (event.getRawY() > staffTop+staffTop/2
									&& event.getRawY() <= 2*staffTop+staffTop/2) {// ����
								arrawtop = 2*staffTop;
								level = 1;
								Toast.makeText(context, R.string.v_1,0).show();
							}else if (event.getRawY() >2*staffTop+staffTop/2&&event.getRawY() <= 3*staffTop+staffTop/2) {// һ��
								arrawtop = 3*staffTop;
								level = 2;
								Toast.makeText(context, R.string.mv_100,0).show();
							}
						levelList.set(touchedChannelID, level);
						/*AnalogFragment.channelRangeLevelList.add(levelList);
						if(AnalogFragment.channelRangeLevelList.size()>2){
							AnalogFragment.channelRangeLevelList.remove(0);
						}*/
						if(autoRangeDataCollection!=null){
							autoRangeDataCollection.setLevelList(levelList);
						}
						setLevel(level,touchedChannelID);
						arrow.setY(arrawtop);
						break;
					}
					return false;
				}
			});
		}
		((PopupWindowView)staffview).channelDataPaint=channelDataPaint;
		((PopupWindowView)staffview).setRangeSurfaceView(this);
		staffview.invalidate();
		popview.setX(x);
		popview.setY(y); 
		popview.setVisibility(View.VISIBLE);
		levelList.set(touchedChannelID, level);
		if(autoRangeDataCollection!=null){
			autoRangeDataCollection.setLevelList(levelList);
		}
		/*AnalogFragment.channelRangeLevelList.add(levelList);
		if(AnalogFragment.channelRangeLevelList.size()>2){
			AnalogFragment.channelRangeLevelList.remove(0);
		}*/
		
		setLevel(level,touchedChannelID);
		
	}
	public void changeStaffView(int level){
		if(staffview!=null){
		float staffTop=	((PopupWindowView)staffview).getPopStaffTop();
			switch(level){
			case 0:
				arrawtop = staffTop;
				break;
			case 1:
				arrawtop = 2*staffTop;
				break;
			case 2:
				arrawtop = 3*staffTop;
				break;
			}
			arrow.setY(arrawtop);
			this.level=level;
		}
		
		
	}
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		 if(rangeControlPopWindow!=null&&rangeControlPopWindow.isShowing()){
			 rangeControlPopWindow.dismiss();
		 }
		if (rawY >= Collections.min(rectTopList)-10) {// ��ֱ��ͼ��������
			if (rawX > Collections.min(rectLeftList)
					|| rawX < Collections.max(rectRightList)) {// ��ֱ��ͼ��������
				int middleRectRight = (int) Math.rint(rectRightList.size() / 2);// ��ȡ��ͼ�����м�λ��
				if (rawX - rectRightList.get(middleRectRight) > 0) {// ����ƫ����Ļ�Ҳ�
					for (int i = middleRectRight + 1; i < rectRightList.size(); i++) {
						
						if (rawX < rectRightList.get(i)) {
							if (rawX >= rectLeftList.get(i)) {
								//touchedChannelID=channelList.indexOf(i);
								float f=rectLeftList.get(i);
								openPopView(f, rectTop,i);
								break;
							} else {
								frameLayout.getChildAt(1).setVisibility(
										View.GONE);
							}
						}
					}
				} else if (rawX - rectRightList.get(middleRectRight) < 0) {// ����ƫ����Ļ���
					for (int i = 0; i < middleRectRight + 1; i++) {
						if (rawX < rectRightList.get(i)) {
							if (rawX >= rectLeftList.get(i)) {
								//touchedChannelID=channelList.indexOf(i);
								float f=rectLeftList.get(i);
								openPopView(f, rectTop,i);
								break;
							} else {
								frameLayout.getChildAt(1).setVisibility(
										View.GONE);
							}
						}
					}
				} else if (rawX - rectRightList.get(middleRectRight) == 0) {
					//touchedChannelID=channelList.indexOf(middleRectRight);
					openPopView(middleRectRight + 1, rectTop,middleRectRight);
				} else {
					frameLayout.getChildAt(1).setVisibility(View.GONE);
				}
			} else if (rawX == rectLeftList.get(0)) {
				//touchedChannelID=channelList.indexOf(0);
				openPopView(rectRightList.get(0) + 10, rectTop,0);
			} else if (rawX == rectRightList.get(rectRightList.size() - 1)) {
				//touchedChannelID=channelList.indexOf(rectRightList.size() - 1);
				openPopView(rectRightList.size() - 1, rectTop,rectRightList.size()-1);
			} else {
				frameLayout.getChildAt(1).setVisibility(View.GONE);
			}
		} else {
			frameLayout.getChildAt(1).setVisibility(View.GONE);
		}
		/*if(popview!=null&&popview.getVisibility()==View.VISIBLE){
			this.setVisibility(View.GONE);
		}else{
			this.setVisibility(View.VISIBLE);
		}*/
		return false;
	}

	 public RangeControlPopWindow getRangeControlPopWindow() {
	 return rangeControlPopWindow;
	 }

	public void setLevel(int level,int channelID) {
		if (channelID==-1)return;
		if(rangehandler==null){
		rangehandler=(Handler)XclSingalTransfer.getInstance().getValue("rangehandler");
		}
		this.level = level;
		//if (arrow == null) {
		//	return;
		//}
		Message msg=rangehandler.obtainMessage();
		msg.arg1=channelID;
		msg.arg2=level;
		rangehandler.sendMessage(msg);
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel(){
		return level;
	}
	public void setFrameLayout(FrameLayout frameLayout) {
		this.frameLayout = frameLayout;
	}

	public void getChannelArray(float[] channelsData) {
		switch (level) {
		case 0:
			for (int i = 0; i < channelsData.length; i++) {
				channelsData[i] = 1f * i;
			}
			break;
		case 1:
			for (int i = 0; i < channelsData.length; i++) {
				channelsData[i] = 0.1f * i;
			}
			break;
		case 2:
			for (int i = 0; i < channelsData.length; i++) {
				channelsData[i] = 0.001f * i;
			}
			break;
		}
	}

	public void setChannelCountInOnePage(int channelCountInOnePage) {
		this.channelCountInOnePage = channelCountInOnePage;
		channelsData = new float[channelCountInOnePage];
	}

	public void setVoltageDateList(List<Float> voltageDateList,
			List<Integer> levelList,List<Integer> channelList) {
		this.voltageDateList=voltageDateList;
		this.levelList=levelList;
		this.channelList=channelList;
	}
}
