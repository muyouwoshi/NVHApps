package common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileacquisition.AlgorithmFragment;
import com.example.mobileacquisition.CursorFragment;
import com.example.mobileacquisition.CustomDialog;
import com.example.mobileacquisition.LegendFragment;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.MainContextView;
import com.example.mobileacquisition.R;
import com.example.mobileacquisition.RulerView;
import com.example.mobileacquisition.XaxisFragment;
import com.example.mobileacquisition.YaxisFragment;

import draw.map.Legend;
import mode.drive.DriveModeActivity;
import mode.calibration.CalibrationActivity;
public abstract class ScaleView extends View {
	private boolean enlagerAlways = false;
	private int clickTag;
	private int colorMapXLabelIndex = -1;
	protected float  colorMaptempChangex=-1;	
	protected int cursorNum = -1;
	protected RulerView ruler;
	protected float divider;
	protected ArrayList resultDatalist;  
	protected MyViewPager viewPager;
	protected int midCount = 1;
	protected float xGrid = 150;
	protected float yGrid = 100;
	protected float yaxisLeft = 10;

	protected DecimalFormat xdf = new DecimalFormat("###0.000");
	protected DecimalFormat ydf = new DecimalFormat("###0.00");
	protected float xaxisbottom = 20;
	protected Paint CoordinatePaint;
	protected Paint LablePaint;
	
	protected int YUNIT_SWITCH_MODE = 0;    //  DB  
	protected float x1, x2, y1, y2, ox, oy, oldox, oldoy, newox, newoy;
	protected float xdis = 0;
	protected float ydis, oldxdis, oldydis;
	protected float dx = 0;
	protected float dy = 0;
	protected int axisTextSize = 18;
	protected float xrate = 1;
	protected float yrate = 1;
	protected float sxrate = 1;
	protected float syrate = 1;
	protected float viewh, vieww;
	protected float xbaseLine;
	protected float ybaseLine;

	protected float rightx;			 //闁告瑥鐤囩粩鐔烘崉閿燂拷
	protected float left = 50;		 //鐎归潻绠掔粩鐔烘崉閿燂拷
	protected float xmultiple = 1;   //婵絽绻嬮柌娓泋 濞寸媴缍�妴鍐╁緞濮橆剛姣屾慨锝庡亞椤濡存担渚健闁靛棔寮搝闁靛棔闃淧M
	protected float ymultiple = 1;   //婵絽绻嬮柌娓泋 濞寸媴缍�妴鍐╁緞濮橆剛姣孭a闁靛棔榫欱闁靛棔寮搝闁靛棔闃淧M
	protected float offsetX;         //婵絽绻嬮柌婊堟倷閸︻厽绾柟鎭掑劤濞村鎹勫┑鍡╂▼閻忓繑鍨归～锟�  闁挎稑婢僫gnal濞戞搫鎷�   1/闂佹彃娲﹂悧閬嶆偝閸ラ绀�        姣忎竴鐐逛箣闂寸殑闂撮殧鏃堕棿涓�.002绉掞紝single涓�/acquiFreq
	protected float acquiFreq = 12800;;          //闂佹彃娲﹂悧閬嶆偝閿燂拷

	protected float maxRate = 100;

	protected boolean isFrist;
	protected boolean isAuto;

	protected  float changeX = 0;
	protected String xlabelState;
	protected String ylabelState;

	protected double p0 = 2 * Math.pow(10, -5);
	protected double a0 = 2 * Math.pow(10, -5);
	protected FragmentActivity context;
	private GestureDetector gesture;
	protected MainContextView mainContextView;
	private Handler logarithmSettingHandler;
//	protected int[] checkedChannelIndexArray;
//	protected int[] activityChannelArray;
	protected List<Paint> paintList;
	protected int hardType;
	protected float freqRes = 0.05f;
	
	protected Map<Integer,List<int[]>> dataListMap;
	protected ArrayList<Integer> isActivated_ChanNum;
	protected int maxDataCount=2;//各通道数据量的最大值
	// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ㄩ悤鍌涘
	public ScaleView(Context context) {
		super(context);
		this.context = (FragmentActivity) context;
		gesture = new GestureDetector(context, new GestureListener());
		CoordinatePaint = new Paint();
		CoordinatePaint.setColor(Color.rgb(234, 234, 234));
		LablePaint = new Paint();
		LablePaint.setColor(Color.rgb(64, 64, 64));
		// LablePaint.setTextSize(axisTextSize);
		init();

	}

	public ScaleView(Context context, AttributeSet attrs) {
		super(context);
		this.context = (FragmentActivity) context;
		CoordinatePaint = new Paint();
		CoordinatePaint.setColor(Color.rgb(234, 234, 234));
		LablePaint = new Paint();
		LablePaint.setColor(Color.rgb(64, 64, 64));
		acquiFreq = 48000;	
		// LablePaint.setTextSize(axisTextSize);
		init();
	}

	public ScaleView(Context context, MyViewPager viewPager) {
		super(context);
		this.context = (FragmentActivity) context;
		gesture = new GestureDetector(context, new GestureListener());
		this.viewPager = viewPager;
		CoordinatePaint = new Paint();
		CoordinatePaint.setColor(Color.rgb(234, 234, 234));
		LablePaint = new Paint();
		LablePaint.setColor(Color.rgb(64, 64, 64));
		acquiFreq = 48000;	
		// LablePaint.setTextSize(axisTextSize);
		init();
	}

	public ScaleView(Context context, MyViewPager viewPager, AttributeSet attrs) {
		super(context, attrs);
		this.context = (FragmentActivity) context;
		CoordinatePaint = new Paint();
		CoordinatePaint.setColor(Color.rgb(234, 234, 234));
		LablePaint = new Paint();
		LablePaint.setColor(Color.rgb(64, 64, 64));
		acquiFreq = 48000;	
		// LablePaint.setTextSize(axisTextSize);
		init();
	}

	protected void init() {

		isFrist = true;
		sxrate = 1;
		syrate = 1;
		this.viewh = this.getHeight();
		this.vieww = this.getWidth();
		ybaseLine = (viewh - 50) / 2;
		xbaseLine = left;
		rightx = 0;
		isAuto = true;

		paintList=Legend.linePaintList;
		// acquiFreq = Float.parseFloat(context.getSharedPreferences("hz_5D",
		// 0).getString("AcquiFreq_spinner_values","48000")); //闂備焦褰冨ú锕傛偋闁秵鍋濋柛銉戝啳顔夐柣鐘辩婢т粙鎮块崱娑樼睄闁割偅娲栧▍姘舵煟椤喗瀚�
								  //闂佺儵鏅╅崰鏍ㄦ櫠閻樼粯鐓傞柛銉ｅ妿婢瑰鏌ｅ鍐╁枠缂侇噮鍨抽幏瀣Χ閸曨喚顦�8000;
		
		final XclSingalTransfer xclSingalTransfer=XclSingalTransfer.getInstance();
		
		if(!xclSingalTransfer.containsKey("logarithmSettingHandler")){
			logarithmSettingHandler = new Handler() {
				public void handleMessage(Message age) {
					List<String> logrithmList = (List<String>) age.obj;
					String acoustic = logrithmList.get(0).trim();
					String acceleration = logrithmList.get(1).trim();
					
					if (acoustic.contains(",")) {
						String[] acousticArray = acoustic.split(",");
						p0 = Double.valueOf(acousticArray[0])
								* Math.pow(Double.valueOf(acousticArray[1]), Double.valueOf(acousticArray[2]));
					} else if (acoustic.indexOf(" ") > 0) {
						String[] acousticArray = acoustic.split(" ");
						p0 = Double.valueOf(acousticArray[0])
								* Math.pow(Double.valueOf(acousticArray[1]), Double.valueOf(acousticArray[2]));
					} else if (!acoustic.equals("")) {
						p0 = Double.valueOf(acoustic);
					}
					// 闂傚倸鍊风粈渚�箟閳ユ剚娼栫紓浣姑崹鏃堟煙缂併垹鏋熼柡鍛倐閺屻劑鎮㈤崜浣虹厯闂佸搫琚崝鎴﹀箖閵堝纾兼繛鎴烇供娴硷拷
					if (acceleration.contains(",")) {
						String[] accelerationArray = acceleration.split(",");
						a0 = Double.valueOf(accelerationArray[0])
								* Math.pow(Double.valueOf(accelerationArray[1]), Double.valueOf(accelerationArray[2]));
					} else if (acceleration.indexOf(" ") > 0) {
						String[] accelerationArray = acceleration.split(" ");
						a0 = Double.valueOf(accelerationArray[0])
								* Math.pow(Double.valueOf(accelerationArray[1]), Double.valueOf(accelerationArray[2]));
					} else if (!acceleration.equals("")) {
						a0 = Double.valueOf(acceleration);
					}
				}
			};
			xclSingalTransfer.putValue("logarithmSettingHandler", logarithmSettingHandler);
		}
		if (context.getClass().getSimpleName().equals("MainActivity")) {
//			activityChannelArray = ((MainActivity) context).bottomOperate.addChannelViewPager.getActivityChannelArray();
//			setActivityChannelArray(activityChannelArray);
		}

	}

	public void setViewPager(MyViewPager viewPager) {
		this.viewPager = viewPager;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);

		this.init();

		// if (ruler != null) {
		//
		// ruler.setHeight(((RelativeLayout)getParent()).getHeight());
		// }

	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (context.getClass().getSimpleName().equals("MainActivity")) {
			hardType = ((MainActivity) context).getPlayBackState() ? IAudioTrack.hardType : DataCollection.hardType;
//			activityChannelArray = ((MainActivity) context).bottomOperate.addChannelViewPager.getActivityChannelArray();
//			checkedChannelIndexArray = ((MainActivity) context).bottomOperate.addChannelViewPager.checkedChannelIndexArray;
		} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
//				activityChannelArray = ((DriveModeActivity) context).getAddChannelViewPager().getActivityChannelArray();
//				checkedChannelIndexArray = ((DriveModeActivity) context)
//						.getAddChannelViewPager().checkedChannelIndexArray;
				hardType = DataCollection.hardType;
		}else if(context.getClass().getSimpleName().equals("CalibrationActivity")){
			hardType=((CalibrationActivity)context).hardType;
//			checkedChannelIndexArray=((CalibrationActivity) context).getCheckedChannelIndexArray();
//			activityChannelArray=checkedChannelIndexArray;
		}
		Coordinate(canvas);
	}

	protected void Coordinate(Canvas canvas) {
		drawYLable(canvas);
		drawXLable(canvas);
		drawYLine(canvas);
		drawXLine(canvas);
	}

	protected void drawYLable(Canvas canvas) {
		if (LablePaint == null) {
			LablePaint = new Paint();
			LablePaint.setTextSize(axisTextSize);
		}
		float h = canvas.getHeight();
		int n = 0;
		while (syrate / (int) Math.pow(2, n) >= 2) {
			n += 1;
		}
		midCount = (int) Math.pow(2, n);
		float dis = yGrid * syrate / midCount;
		int upCount = (int) ((ybaseLine) / yGrid);
		int downCount = (int) ((h - 50 - ybaseLine) / yGrid);
		canvas.save();
		// canvas.clipRect(0, 40, 100, canvas.getHeight());
		for (float i = 0; i <= upCount; i++) {
			float ypos = ybaseLine - i * dis;
			if (ypos > 55 && ypos < h - 50) {
				String text = null;
				if (ylabelState == null) {
					return;
				}

				if ("Pa".equals(ylabelState)) {
					text = ydf.format((i * yGrid / midCount) / 100);
				} else if ("dB".equals(ylabelState)) {
					text = ydf.format(i * yGrid / midCount);
				} else if ("%".equals(ylabelState)||"Hz".equals(ylabelState)) {
					text = ydf.format(i * yGrid / midCount * ymultiple);
				} else {
					text = ydf.format(i * yGrid / midCount);
				}

				canvas.drawText(text, yaxisLeft, ypos, LablePaint);
			}
		}
		canvas.drawText(ylabelState, yaxisLeft, 25, LablePaint);
		for (float i = 1; i <= downCount; i++) {

			float ypos = ybaseLine + i * dis;
			if (ypos > 55 && ypos <= h - axisTextSize) {
				String text = null;
				if (ylabelState == null) {
					return;
				}

				if ("Pa".equals(ylabelState)) {
					text = "-" + ydf.format(i * yGrid / midCount / 100);
				} else {
					text = "-" + ydf.format(i * yGrid / midCount);
				}

				canvas.drawText(text, yaxisLeft, ypos, LablePaint);
			}
		}
		canvas.restore();

	}

	protected void drawXLable(Canvas canvas) {
		if (LablePaint == null) {
			LablePaint = new Paint();
			LablePaint.setTextSize(24);
		}
		if (xlabelState == null) {
			return;
		}
		float w = canvas.getWidth();
		int n = 0;
		while (sxrate / (int) Math.pow(2, n) >= 2) {
			n += 1;
		}
		midCount = (int) Math.pow(2, n);

		float dis = xGrid * sxrate / midCount;
		int rightCount = (int) ((w - 50 - xbaseLine) / xGrid);

		canvas.save();
		// canvas.clipRect(0,canvas.getHeight()-50 , canvas.getWidth()-60,
		// canvas.getHeight());
		for (float i = 0; i <= rightCount; i++) {
			float xpos = xbaseLine + i * dis;
			if (xpos >= 100 && xpos < w - 60) {
				String text = null;

				text = xdf.format(((i / midCount / 2) + changeX) * 1000);
			

				if ("RPM".equals(xlabelState)) {
					SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
					String lowerStr = preference.getString("Trigger_Lower", "");
					String upperStr = preference.getString("Trigger_Upper", "");
					float lower = 0;
					float upper = 0;
					if (lowerStr.equals("")) {
						lower = 0;
					} else {
						lower = Float.parseFloat(lowerStr);
					}
					if (upperStr.equals("")) {
						upper = 50;
					} else {
						upper = Float.parseFloat(upperStr);
					}
					String triggerMode = preference.getString("Trigger_Mode", "Time");
					if (triggerMode.equals("runup")) {
						text = xdf.format(lower + (upper - lower) * i / rightCount);
					} else if (triggerMode.equals("rundown")) {
						text = xdf.format(upper - (upper - lower) * i / rightCount);
					} else {
						text = xdf.format(lower + (upper - lower) * i / rightCount);
					}
				} else if ("Hz".equals(xlabelState)) {
					text = xdf.format(xGrid/midCount*i*xmultiple);
				}

				else if("ms".equals(xlabelState)){
					text = xdf.format(xGrid/midCount*i*xmultiple+changeX*1000);
				}
				else {
					text = xdf.format(xGrid/midCount*i*xmultiple+changeX);
				} 
				
				canvas.drawText(text, xpos - 25, canvas.getHeight()
						- xaxisbottom, LablePaint);
			}
		}
		if(changeX>0){		
			colorMapXLabelIndex++;
		}
		canvas.drawText(xlabelState, canvas.getWidth() - 25, canvas.getHeight() - xaxisbottom, LablePaint);
		canvas.restore();
	}

	protected void drawYLine(Canvas canvas) {
		if (CoordinatePaint == null) {
			CoordinatePaint = new Paint();
		}
		canvas.drawLine(left, 0, left, canvas.getHeight() - 50, CoordinatePaint);

		float h = canvas.getHeight();
		float w = canvas.getWidth();

		int n = 0;
		while (syrate / (int) Math.pow(2, n) >= 2) {
			n += 1;
		}
		midCount = (int) Math.pow(2, n);

		float dis = yGrid * syrate / midCount;
		int upCount = (int) ((ybaseLine) / yGrid);
		int downCount = (int) ((h - 50 - ybaseLine) / yGrid);

		for (float i = 0; i <= upCount; i++) {
			float ypos = ybaseLine - i * dis;
			if (ypos > 0 && ypos < h - 50) {
				canvas.drawLine(left, ypos, w, ypos, CoordinatePaint);
			}
		}
		for (float i = 1; i <= downCount; i++) {
			float ypos = ybaseLine + i * dis;
			if (ypos > 0 && ypos <= h - 50) {
				canvas.drawLine(left, ypos, w, ypos, CoordinatePaint);
			}
		}

	}

	protected void drawXLine(Canvas canvas) {
		if (CoordinatePaint == null) {
			CoordinatePaint = new Paint();
		}
		canvas.drawLine(left, canvas.getHeight() - 50, canvas.getWidth(), canvas.getHeight() - 50, CoordinatePaint);

		float w = canvas.getWidth();
		float h = canvas.getHeight();

		int n = 0;
		while (sxrate / (int) Math.pow(2, n) >= 2) {
			n += 1;
		}
		midCount = (int) Math.pow(2, n);

		float dis = xGrid * sxrate / midCount;

		int rightCount = (int) ((w - left - xbaseLine) / xGrid);

		for (float i = 0; i <= rightCount + 1; i++) {

			float xpos = xbaseLine + i * dis;
			if (xpos >= left && xpos < w) {
				canvas.drawLine(xpos, 0, xpos, h - 50, CoordinatePaint);
			}
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		this.invalidate();
		if (!context.getClass().getSimpleName().equals("CalibrationActivity")) {
			if (gesture.onTouchEvent(event)) {
				return true;
			} else {
				if (event.getPointerCount() == 1) {
					if (viewPager != null)
						viewPager.setCanScroll(true);
					return true;
				} else if (event.getPointerCount() == 2) {
					if (viewPager != null)
						viewPager.setCanScroll(false);
					setRate(event);
					if (this.ruler != null) {
						ruler.postInvalidate();
					}
					return true;
				} else {
					return true;
				}
			}
		} else {
			if (event.getPointerCount() == 1) {
				if (viewPager != null)
					viewPager.setCanScroll(true);
				return true;
			} else if (event.getPointerCount() == 2) {
				if (viewPager != null)
					viewPager.setCanScroll(false);
				setRate(event);
				return true;
			} else {
				return true;
			}
		}
	}

	public void setAlwaysEnlager(boolean b) {
		enlagerAlways = b;
	}

	class GestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {

			// clickTag++;
			// if (clickTag % 2 != 0) {
			// mainContextView.enlarge();
			//
			// } else {
			// mainContextView.reduce();
			// }
			if (enlagerAlways || context.getClass().getSimpleName().equals("DriveModeActivity"))
				return false;
			if (!mainContextView.enlarge) {
				mainContextView.enlarge();
			} else {
				mainContextView.reduce();
			}
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			/*
			 * ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
			 * AlgorithmFragment algorithmFragment = new AlgorithmFragment();
			 * XaxisFragment xaxisFragment = new XaxisFragment(); YaxisFragment
			 * yaxisFragment = new YaxisFragment();
			 * xaxisFragment.setXLableState(xlabelState);
			 * yaxisFragment.setYLableState(ylabelState);
			 * xaxisFragment.setScaleView(ScaleView.this);
			 * yaxisFragment.setScaleView(ScaleView.this);
			 * fragmentList.add(algorithmFragment);
			 * fragmentList.add(xaxisFragment); fragmentList.add(yaxisFragment);
			 * CustomDialog customDialog = new CustomDialog(context,
			 * fragmentList); FragmentManager manager =
			 * context.getSupportFragmentManager(); Fragment isShown =
			 * manager.findFragmentByTag("customDialog"); if (isShown != null)
			 * return false; customDialog.show(manager, "customDialog");
			 */
			return false;
		}

		@Override
		public void onLongPress(MotionEvent ev) {
			ImageButton autoRange = (ImageButton) context.findViewById(R.id.autoRange);

			if (autoRange != null && autoRange.isSelected())
				return;

			if (DataCollection.isRecording) {

				Toast.makeText(context.getApplicationContext(), R.string.Collecting_data, Toast.LENGTH_SHORT).show();

				return;
			}

			if (context.getClass().getSimpleName().equals("MainActivity")) {
				if (mainContextView.getAlgorithm_spinner().getText().equals("ChannelWatch"))
					return;
			} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
				TextView algorithm_spinner = (TextView) context.findViewById(R.id.algorithm_title);
				if (algorithm_spinner.getText().equals("ChannelWatch"))
					return;
			}
			ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
			AlgorithmFragment algorithmFragment = new AlgorithmFragment();
			XaxisFragment xaxisFragment = new XaxisFragment();
			YaxisFragment yaxisFragment = new YaxisFragment();
			xaxisFragment.setXLableState(xlabelState);
			yaxisFragment.setYLableState(ylabelState);
			xaxisFragment.setScaleView(ScaleView.this);
			yaxisFragment.setScaleView(ScaleView.this);
			LegendFragment legendFragment = new LegendFragment();

			if (mainContextView != null) {
				legendFragment.setLegend(mainContextView.getLegend());
				legendFragment.setAlgorithm_spinner(mainContextView.getAlgorithm_spinner());
				xaxisFragment.setAlgorithm_spinner(mainContextView.getAlgorithm_spinner());
				yaxisFragment.setAlgorithm_spinner(mainContextView.getAlgorithm_spinner());
				algorithmFragment.setMainContextView(mainContextView);
			}
			algorithmFragment.setXaxisFragment(xaxisFragment);
			algorithmFragment.setYaxisFragment(yaxisFragment);
			fragmentList.add(algorithmFragment);
			fragmentList.add(xaxisFragment);
			fragmentList.add(yaxisFragment);
			fragmentList.add(legendFragment);
			if (ruler != null) {
				CursorFragment cursorFragment = new CursorFragment();
				cursorFragment.setView(ScaleView.this);
				fragmentList.add(cursorFragment);

			}

			CustomDialog customDialog = new CustomDialog(context, fragmentList);
			FragmentManager manager = context.getSupportFragmentManager();
			Fragment isShown = manager.findFragmentByTag("customDialog");
			if (isShown != null)
				return;
			customDialog.show(manager, "customDialog");
		}
	}

	protected void setRate(MotionEvent event) {
		x1 = (int) event.getX(0);
		y1 = (int) event.getY(0);
		x2 = (int) event.getX(1);
		y2 = (int) event.getY(1);
		if (isFrist) {

			oldxdis = getValue(x1, x2, y1, y2)[0];
			oldydis = getValue(x1, x2, y1, y2)[1];
			oldox = getValue(x1, x2, y1, y2)[2];
			oldoy = getValue(x1, x2, y1, y2)[3];
			ox = oldox;
			oy = oldoy;
			isFrist = false;

		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

			xdis = getValue(x1, x2, y1, y2)[0];
			ydis = getValue(x1, x2, y1, y2)[1];
			newox = getValue(x1, x2, y1, y2)[2];
			newoy = getValue(x1, x2, y1, y2)[3];

			ox = oldox;
			oy = oldoy;

			xrate = xdis / oldxdis;
			yrate = ydis / oldydis;

			if (ydis < 100 || syrate * yrate >= maxRate) {
				yrate = 1;
			} else if (syrate * yrate < 1) {
				yrate = 1 / syrate;
				syrate = 1;
			} else {
				syrate = yrate * syrate;
			}

			dy = newoy - oldoy;
			dx = newox - oldox;

			if (xdis < 100 || sxrate * xrate >= maxRate) {
				xrate = 1;
				if (xbaseLine + dx > left) {
					dx = left - xbaseLine;
				} else if ((vieww - left) * sxrate + xbaseLine + dx < vieww) {
					dx = vieww - (vieww - left) * sxrate - xbaseLine;
				}
			} else if (sxrate * xrate < 1) {

				xrate = 1 / sxrate;
				sxrate = 1;
				dx = left - xbaseLine;
			} else {

				sxrate = xrate * sxrate;
				if (ox + (xbaseLine - ox) * xrate + dx > left) {
					ox = xbaseLine;
					dx = left - xbaseLine;
				} else if ((vieww + rightx - ox) * xrate - vieww + ox + dx < 0) {
					ox = vieww + rightx;
					dx = 0 - rightx;
				}
			}

			ybaseLine = ybaseLine + dy;
			xbaseLine = xbaseLine + dx;
			rightx = rightx + dx;

			xbaseLine = ox + (xbaseLine - ox) * xrate;
			rightx = (vieww + rightx - ox) * xrate - vieww + ox;

			ybaseLine = oy + (ybaseLine - oy) * yrate;

			oldxdis = xdis;
			oldydis = ydis;

			oldox = newox;
			oldoy = newoy;

		} else {
			if (sxrate * syrate <= 1.05)
				isAuto = true;
			else
				isAuto = false;
			isFrist = true;
			xrate = 1;
			yrate = 1;
			dx = 0;
			dy = 0;

		}
	}

	protected float[] getValue(float x1, float x2, float y1, float y2) {
		float[] xy = new float[4];
		float dx = x1 - x2;
		if (dx < 0)
			dx = 0 - dx;
		float dy = y1 - y2;
		if (dy < 0)
			dy = 0 - dy;
		xy[0] = dx;
		xy[1] = dy;
		xy[2] = (x1 + x2) / 2;
		xy[3] = (y1 + y2) / 2;
		return xy;
	}

	protected void getDataResult(int channelNum) {

	}

	public List<Float> getCursorValue(float moveX) {

		List<Float> list = new ArrayList<Float>();

		float xValue = 0;
		float yValue = 0;
		float yPosition = ybaseLine;

		getDataResult(cursorNum);

		if (divider*ymultiple == 0 || xlabelState == null || ylabelState==null||cursorNum == -1) { 

			list.add(xValue);
			list.add(yValue);
			list.add(yPosition);
			return list;
		}


		xValue = ((moveX - xbaseLine + 50) / sxrate)*xmultiple+changeX;


		if ("s".equals(xlabelState)) {
			xlabelState = "s";
		} else if ("ms".equals(xlabelState)) {
			xValue = xValue * 1000;
			xlabelState = "ms";
		} else if ("Hz".equals(xlabelState)) {
			xlabelState = "Hz";
			xValue = ((int) ((moveX - xbaseLine + 50) / divider / sxrate)) * freqRes;
		} else if ("RPM".equals(xlabelState)) {
			xlabelState = "RPM";
			// xValue = moveX;
		}

		int x = (int) ((moveX - xbaseLine + 50) / divider / sxrate);
		// float yValue = 0;
		
		if (resultDatalist != null && resultDatalist.size() > 0) {
			if (x < resultDatalist.size() && x > 0) {

				yValue = Float.parseFloat(resultDatalist.get(x).toString())
						/ ymultiple;


			} else if (x >= resultDatalist.size()) {

				yValue = Float.parseFloat(resultDatalist.get(
						resultDatalist.size() - 1).toString())
						/ ymultiple;

			} else if (x == 0) {

				yValue = (Float
						.parseFloat(resultDatalist.get(0).toString()) / ymultiple);

			}
		}

		yPosition = ybaseLine - yValue * syrate;
		if(yPosition < 0)yPosition =50;
		else if(yPosition> viewh-50) yPosition = viewh-50;
		if ("Pa".equals(ylabelState)) {
			yValue = yValue / 100;
		} else {
			yValue = yValue;
		}

		// if(cursorNum == 0){
		// xValue = 1;
		// yValue = 1;
		//
		// }
		//
		// else if(cursorNum ==1){
		// xValue = 2;
		// yValue = 2;
		// }
		// else if(cursorNum ==2){
		// xValue = 3;
		// yValue = 3;
		// }
		// else if(cursorNum ==3){
		// xValue = 4;
		// yValue = 4;
		// }

		list.add(xValue);
		list.add(yValue);
		list.add(yPosition);
		return list;
	}

	public void setXLableState(String xlabelState) {
		this.xlabelState = xlabelState;
		refreshLabelState();
		if (ruler != null)
			ruler.postInvalidate();
		
	}

	public void setYLableState(String ylabelState) {
		if(this.ylabelState.equals("Pa") && ylabelState.equals("dB")) this.YUNIT_SWITCH_MODE = 1;
		else if(this.ylabelState.equals("dB") && ylabelState.equals("Pa"))this.YUNIT_SWITCH_MODE = 2;
		else YUNIT_SWITCH_MODE = 0;
		
		yDataChange();
		this.ylabelState = ylabelState;
		
		refreshLabelState();
		if (ruler != null)
			ruler.postInvalidate();
		this.invalidate();
	}

	public void setXExent(float minValue, float maxValue) {

		if ("s".equals(xlabelState)) {
			sxrate = ((vieww - 50) / xGrid / 2 / (maxValue - minValue));
			if (sxrate > 100)
				sxrate = 100;
			xbaseLine = 50 - (minValue * xGrid * 2) * sxrate;
			rightx = (vieww - 50 - minValue * xGrid * 2) * sxrate - vieww + 50;
			// sxrate=((vieww-50)/xGrid/2/(xMaxNum-changeX));
		} else if ("ms".equals(xlabelState)) {
			sxrate = ((vieww - 50) / xGrid / 2 / ((maxValue - minValue) / 1000));
			if (sxrate > 100)
				sxrate = 100;
			xbaseLine = 50 - (minValue / 1000 * xGrid * 2) * sxrate;
			rightx = (vieww - 50 - minValue * xGrid * 2) * sxrate - vieww + 50;
			// sxrate=(vieww/xGrid/2/(xMaxNum/1000-changeX));
		} else if ("Hz".equals(xlabelState)) {
			sxrate = ((vieww - 50) * xmultiple / (maxValue - minValue));
			if (sxrate > 100)
				sxrate = 100;
			xbaseLine = 50 - (minValue / xmultiple) * sxrate;
			rightx = (vieww - 50 - minValue / xmultiple) * sxrate - vieww + 50;
		} else {
			sxrate = ((vieww - 50) / xGrid / 2 / ((maxValue - minValue) - changeX));
			if (sxrate > 100)
				sxrate = 100;
			xbaseLine = 50 - (minValue * xGrid * 2) * sxrate;
			rightx = (vieww - 50 - minValue * xGrid * 2) * sxrate - vieww + 50;
		}

		this.invalidate();
	}

	public void setYExent(float minValue, float maxValue) {

		if ("Pa".equals(ylabelState)) {
			syrate = (viewh - 50) / 100 / (maxValue - minValue);
			if (syrate > 100)
				syrate = 100;

			ybaseLine = (viewh - 50) / 2 + (maxValue * 100) * syrate - (viewh - 50) / 2;
		} else if ("dB".equals(ylabelState) || "dBA".equals(ylabelState) || "dBC".equals(ylabelState)) {
			syrate = (viewh - 50) / (maxValue - minValue);
			if (syrate > 100)
				syrate = 100;
			ybaseLine = (viewh - 50) / 2 + (maxValue) * syrate - (viewh - 50) / 2;
		}

		/*
		 * switch (ylabelState) { case "Pa":// pa syrate = (viewh - 50) / 100 /
		 * (maxValue - minValue); if (syrate > 100) syrate = 100;
		 * 
		 * ybaseLine = (viewh - 50) / 2 + (maxValue * 100) * syrate - (viewh -
		 * 50) / 2;
		 * 
		 * break; case "dB":// db case "dBA":// dbA case "dBC":// dbC syrate =
		 * (viewh - 50) / (maxValue - minValue); if (syrate > 100) syrate = 100;
		 * ybaseLine = (viewh - 50) / 2 + (maxValue) * syrate - (viewh - 50) /
		 * 2; break; // default://hz // syrate = pa/yMaxNum; // break; }
		 */

		this.invalidate();
	}

	public void setMainContextView(MainContextView mainContextView) {
		this.mainContextView = mainContextView;
	}

	public float getXGrid() {
		return this.xGrid;
	}

	public float getXMultiple() {
		return this.xmultiple;
	}

	public float getYGrid() {
		return this.yGrid;
	}

	public void setCursorNum(int num) {
		this.cursorNum = num;
	}

	public int getCursorNum() {
		return this.cursorNum;
	}

	public void setRuler(RulerView ruler) {
		this.ruler = ruler;
	}

//	public int[] getActivityChannelArray() {
//		return this.activityChannelArray;
//	}

//	public void setActivityChannelArray(int[] channelArray) {
//		this.activityChannelArray = channelArray;
//		if (cursorNum == -1 || (cursorNum != -1 && activityChannelArray[cursorNum] == 0)) {
//
//			for (int i = 0; i < activityChannelArray.length; i++) {
//
//				if (activityChannelArray[i] == 1) {
//					cursorNum = i;
//					break;
//				}
//
//				if (activityChannelArray[i] == 0)
//					cursorNum = -1;
//			}
//
//			if (ruler != null)
//				ruler.invalidate();
//
//		}
//	}

	public RulerView getRuler() {
		return this.ruler;
	}

	public void refreshLabelState() { // 闂佽崵濮崇粈浣规櫠娴犲鍋柛鈩冪懁閻掑﹪鐓崶銊︹拹婵炲牐娉涢湁闁挎繂鎳愭牎闂佸搫妫撮幏锟�		// TODO Auto-generated method stub

		SharedPreferences preferences = context.getSharedPreferences("displayInfo", Context.MODE_PRIVATE);

		int xdigits = 0, ydigits = 0;
		if (xlabelState.equals("RPM")) {
			xdigits = preferences.getInt("rpm_digits", 1);
		} else if (xlabelState.equals("Hz")) {
			xdigits = preferences.getInt("hz_digits_set", 1);
		} else if (xlabelState.equals("s")) {
			xdigits = preferences.getInt("s_digits", 3);

			refreshXmultiple();         	//闂佹悶鍎虫慨宕囨嫻閻掝櫙gnal濠殿噯绲界换瀣煂濠婂牊鍊烽柛锔诲幗閻ｉ亶鏌￠崘銊у煟婵☆偄娼″濠氬箻椤旂瓔鍚橀梺鍛婄矊閼活垶宕ｉ悙顒傤浄闁哄诞鍕洯婵炴垶鎸哥粔宕囩博閹绢喖鍐�梺鍨儏椤ｆ彃霉閻橆喖鍔︽繛鎾冲閹茬増绗熼敓鑺ヮ仧闂佸憡鍔栭悷锕傤敆濠靛绀勯柤鎭掑劜濞堬拷
		} else if (xlabelState.equals("ms")) {

			xdigits = preferences.getInt("ms_digits", 1);
			refreshXmultiple();
		}

		if (ylabelState.equals("RPM")) {
			ydigits = preferences.getInt("rpm_digits", 1);
		} else if (ylabelState.equals("Hz")) {

			ydigits = preferences.getInt("hz_digits", 1);	
			
		}
		else if(ylabelState.equals("Pa")){

			ydigits = preferences.getInt("pa_digits", 1);
			ymultiple = 0.01f;
		}
		else if(ylabelState.equals("dB")||ylabelState.equals("dBA")||ylabelState.equals("dBC")){

			ydigits = preferences.getInt("db_digits", 1);

			ymultiple = 1f;
		}
		else if(ylabelState.equals("%")){

			ydigits = preferences.getInt("percent_digits", 1);
		}
		StringBuilder xbuilder = new StringBuilder();
		xbuilder.append("###0");
		if (xdigits != 0) {
			xbuilder.append(".");
			for (int i = 0; i < xdigits; i++) {
				xbuilder.append("0");
			}
		}
		StringBuilder ybuilder = new StringBuilder();
		ybuilder.append("###0");
		if (ydigits != 0) {
			ybuilder.append(".");
			for (int i = 0; i < ydigits; i++) {
				ybuilder.append("0");
			}
		}
		xdf.applyPattern(xbuilder.toString());
		ydf.applyPattern(ybuilder.toString());

		this.invalidate();
	}


	protected void refreshXmultiple() {
		// TODO Auto-generated method stub
		if(xlabelState.equals("s")){
			offsetX = 0.002f;
			xmultiple = offsetX/divider;
		}
		else if(xlabelState.equals("ms")){
			offsetX = 0.002f;
			xmultiple = offsetX/divider*1000;
		}
	}

	public void resetXChange() {
		// TODO Auto-generated method stub
		changeX = 0;
		this.invalidate();
	}
	
	/**
	 * <b>鍔熻兘<b> 鍗曚綅鍒囨崲閲嶆柊璁＄畻db鍜宲a鍊�br>
	 * <b>璇存槑<b> 涓瓙绫婚渶閲嶅啓璇ユ柟娉�br>
	 * @param list  鐢诲浘鎵�敤鐨勬暟鍊奸泦鍚�
	 */
	protected void yDataChange(){
		
	}

	public  ArrayList<List<Float>> getResultBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getDataLength() {
		return (int)((vieww-50)/divider+0.5);

	}
}
