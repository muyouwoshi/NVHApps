package draw.map;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.calculate.Arith_FFT;
import com.calculate.Arith_RPM;
import com.calculate.Arith_SPL;
import com.example.mobileacquisition.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import bottom.drawer.AddChannelViewPager;
import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;
import common.XclSingalTransfer;
import left.drawer.AnalogFragment;
import mode.calibration.CalibrationDataCollection;
import mode.drive.DriveModeActivity;

public class SPL extends ScaleView {
	public final static int MODE_NORMAL = 0;
	public final static int MODE_DRIVE = 1;
	public final static int MODE_CALIBRATION = 2;
	// private Ruler ruler;
	private double maxValue;
	private ArrayList<List<Double>> SPLBuffer;
	private ArrayList<int[]> SPLBufferArray = new ArrayList<int[]>();
	private List<double[]> SPLResultList = new ArrayList<double[]>();
	private float px, x;
	private float py = 0;
	private float y = 0;
	private int[] voiceData;
	private Arith_SPL spl;
	private int firstCalculation;
	private double max;
	private double[] maxArray = new double[2];
	private ArrayList<Double> peakValueList = new ArrayList<Double>();
	private Handler splPeakVauleHandler;
	// private Activity context;// 原为
	// MainActivity锟斤拷锟酵ｏ拷锟睫革拷为Acitivity锟斤拷锟酵ｏ拷锟斤拷为锟疥定锟斤拷锟斤拷也锟斤拷要锟矫达拷锟姐法锟斤拷锟斤拷锟皆诧拷应锟矫固讹拷为Main锟斤拷Activity
	private Handler splParamHandler;// spl锟姐法锟斤拷锟斤拷锟斤拷锟矫猴拷锟斤拷锟斤拷息
	private int xlabelSer;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷每锟轿斤拷锟诫都锟斤拷锟斤拷愿锟较碉拷锟�
	private boolean startToFindZero = true;
	private int j;
	private int count;

	private Arith_RPM rpm;
	private int skipcount = 0;// 锟斤拷锟斤拷实转锟斤拷通锟斤拷锟斤拷锟街猴拷删锟斤拷
	private float[] rpmRange;// 锟叫伙拷为rpm锟斤拷暮锟斤拷锟�
	private float[] rpmData;// 锟叫伙拷为rpm锟斤拷锟斤拷锟斤拷锟�
	private int channalCount;
	private  boolean ifStartToGetData = false;
	private static boolean ifEnterToDriverMode=false;
	private  int bufferIntListIndex;
	private boolean calibration = false;
	private XclSingalTransfer xclSingalTransfer;
	public SPL(Context context) {
		super(context);
		// this.context = (Activity) context;
		spl = new Arith_SPL();
		setSplParamHandler();
		setBufferIntListIndex();
	}
	
	public SPL(Context context,int mode) {
		super(context);
		// this.context = (Activity) context;
		spl = new Arith_SPL();
		if(mode == MODE_CALIBRATION) calibration = true;
		setSplParamHandler();
	}

	public void init() {
		super.init();
		ybaseLine = viewh - 50;
		divider = 2.0f; // 每锟斤拷锟斤拷锟斤拷锟斤拷锟截伙拷一锟斤拷锟斤拷
		offsetX = 0.002f; // 每锟斤拷锟斤拷锟�0.002锟斤拷
		xmultiple = offsetX / divider; // 每px锟斤拷锟�0.001锟斤拷;
		ymultiple = 0.01f;
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);

		if(xlabelState ==null)xlabelState = preference.getString("SPL_XAxis", "s");
		if (!calibration&&(ylabelState ==null)) {
			ylabelState = preference.getString("SPL_YAxis", "Pa");
		}

		refreshLabelState();
	}

	public SPL(Context context, MyViewPager viewPager) {
		super(context, viewPager);
		// this.context = (Activity) context;
		channalCount = 8;
		SPLBuffer = new ArrayList<List<Double>>();
		for (int i = 0; i < channalCount; i++) {
			SPLBuffer.add(new ArrayList<Double>());
		}
		spl = new Arith_SPL();
		setSplParamHandler();
		setBufferIntListIndex();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		if (context.getClass().getSimpleName().equals("MainActivity")) 
			isActivated_ChanNum=((MainActivity)context).bottomOperate.addChannelViewPager.isActivated_ChanNum;
		else if(context.getClass().getSimpleName().equals("DriveModeActivity"))
			isActivated_ChanNum=((DriveModeActivity)context).addChannelViewPager.isActivated_ChanNum;
		if(isActivated_ChanNum==null||isActivated_ChanNum.size()==0) return;
		if (SPLBuffer == null || SPLBuffer.size() <= 0) {
			return;
		}
		canvas.save();
		canvas.clipRect(left, 0, vieww, viewh - 50);

		getAutoRate((ArrayList<Double>) SPLBuffer.get(0), ybaseLine);
		for (int i = 0; i <maxDataCount; i++) {
			int index = 0;
			while (index < isActivated_ChanNum.size()) {
				if(DataCollection.dataListMap.containsKey(isActivated_ChanNum.get(index))){
					List<Double> dataList = SPLBuffer.get(index);
					if(maxDataCount<dataList.size()){
						maxDataCount=dataList.size();
					}
					if(i==dataList.size()){
						continue;
					}
					py = (float) (ybaseLine - dataList.get(i) * syrate / ymultiple);
					px = xbaseLine + i * sxrate * divider;
					y = (float) (ybaseLine - dataList.get(i + 1) * syrate / ymultiple);
					x = px + sxrate * divider;
					// ------------------标定取峰值----------------
					if (splPeakVauleHandler != null) {
						if (firstCalculation < 2) {
							if (i <= SPLBuffer.size() - 2) {
								if (i > 0 && i < SPLBuffer.size() - 1) {
									if (dataList.get(i) > dataList.get(i - 1) && dataList.get(i) < dataList.get(i + 1)) {
										peakValueList.add(dataList.get(i));
									}
								}
							} else if (i == SPLBuffer.size() - 1) {
								max = Collections.max(peakValueList);
								maxArray[firstCalculation] = max;
								peakValueList.clear();
								firstCalculation++;
							}
						}
					}
					// ------------------完毕--------------------------
					if (paintList != null && paintList.size() > 0) {
						if (index + 1 > paintList.size()) {
							canvas.drawLine(px, py, x, y, paintList.get(0));
						} else {
							canvas.drawLine(px, py, x, y, paintList.get(index));
						}
					}
					//------------------标定获取灵敏度-------------------
					if (splPeakVauleHandler != null) {
						if (firstCalculation == 2) {
							double sum = 0;
							for (int j = 0; j < SPLBuffer.size(); j++) {
								sum = sum + (maxArray[0] - maxArray[1]) * (maxArray[0] - maxArray[1]);
							}
							if (sum < 1.0) {// ���뼴Ϊ�ȶ�״̬
								double realPeakValue = maxArray[1];
								Message realPeakValueMessage = splPeakVauleHandler.obtainMessage();
								realPeakValueMessage.obj = realPeakValue;
								splPeakVauleHandler.sendMessage(realPeakValueMessage);
							} else {
								firstCalculation = 0;
							}
						}
					}
					//----------------完毕---------------------------
				}
				index++;
			}
			// ------------------锟斤拷锟斤拷--------------
		}
		canvas.restore();
	}

	@Override
	protected void getDataResult(int channelNum) {
		// TODO Auto-generated method stub
		if (channelNum > SPLBuffer.size())
			return;
		if (SPLBuffer != null && channelNum != -1 && SPLBuffer.get(channelNum).size() > 0) {
			resultDatalist = (ArrayList<Double>) SPLBuffer.get(channelNum);
		}
	}

	private void getAutoRate(ArrayList<Double> list, float ybase) {

		if (list.size() == 0)
			return;
		maxValue = list.get(0) / ymultiple;
		int i = 0;
		while (i < list.size()) {
			double value = list.get(i) / ymultiple;
			if (value < 0)
				value = 0 - value;
			if (value > maxValue)
				maxValue = value;
			i++;
		}
		if (isAuto) {

			if ((ybase * 0.8 / maxValue) >= 1 && (ybase * 0.8 / maxValue) <= 100) {
				syrate = (float) (ybase * 0.8 / maxValue);
			} else if ((ybase * 0.8 / maxValue) > 100) {
				syrate = 100;
			} else
				syrate = 1;
		}
	}

	// 锟疥定之spl锟斤拷值锟斤拷锟斤拷
	public void setSPLPeakVauleHandler(Handler splPeakVauleHandler) {
		this.splPeakVauleHandler = splPeakVauleHandler;
	}

	public Handler getSPLPeakVauleHandler() {
		return this.splPeakVauleHandler;
	}

	private void setSplParamHandler() {
		splParamHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (voiceData != null && voiceData.length > 0) {
					return;
				}
				switch (msg.what) {
				case 0:
					spl.setAvarage(msg.arg1);
					break;
				case 1:
					spl.setWeighting(msg.arg1);
					break;
				}
			}
		};
		XclSingalTransfer xclSingalTransfer = XclSingalTransfer.getInstance();
		xclSingalTransfer.putValue("splParamHandler", splParamHandler);
	}

	// protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	// if (viewPager != null) {
	// if (ruler == null) {
	// ruler = new Ruler(context,
	// ((RelativeLayout) getParent()).getHeight());
	// ((RelativeLayout) getParent()).addView(ruler);
	// } else {
	// ruler.setHeight(((RelativeLayout) getParent()).getHeight());
	// }
	// }
	// super.onSizeChanged(w, h, oldw, oldh);
	// }
	//
	// private String getUnit() {
	// return context.getSharedPreferences("hz_5D", 0).getString("SPL_XAxis",
	// "s");
	// }

	public void setCalibration(boolean value) {
		this.calibration = value;
	}
	@Override
	protected void yDataChange() {
		// TODO Auto-generated method stub
		if(YUNIT_SWITCH_MODE == 0) return;	
		else if(YUNIT_SWITCH_MODE == 2){   	  // pa 锟叫伙拷锟斤拷   db
			int listCount = SPLBuffer.size();
			ArrayList<List<Double>> lists = new ArrayList<List<Double>>();
			for(int i =0 ; i< listCount ;i++){
				List<Double> list= new ArrayList<Double>();
				
				Iterator<Double> it = SPLBuffer.get(i).iterator();
				while(it.hasNext()){
					double data = it.next();
					
					data=p0*(Math.pow(10, (data/ 20)));
					
					list.add(data);
				}
				lists.add(list);
			}
			
			SPLBuffer.clear();
			SPLBuffer = lists;
		}else {					// db 锟叫伙拷锟斤拷     pa

			int listCount = SPLBuffer.size();
			ArrayList<List<Double>> lists = new ArrayList<List<Double>>();
			for(int i =0 ; i< listCount ;i++){
				List<Double> list= new ArrayList<Double>();
				
				Iterator<Double> it = SPLBuffer.get(i).iterator();
				while(it.hasNext()){
					double data = it.next();
									  
					data=20*(Math.log10(data/p0)); 
					
					list.add(data);
				}
				lists.add(list);
			}
			
			SPLBuffer.clear();
			SPLBuffer = lists;
		}
		
	}
	//-----------驾驶|普通采集同步专用index----------
	public void setBufferIntListIndex(){
		if(xclSingalTransfer==null){
			xclSingalTransfer=XclSingalTransfer.getInstance();
		}
		bufferIntListIndex=xclSingalTransfer.containsKey("SPL_DataCollectionIntBufferListIndex")?(Integer) xclSingalTransfer.getValue("SPL_DataCollectionIntBufferListIndex"):0;
	}
	
	
	
	/**
	 * 优化绘图使用
	 * @author zhoujie
	 *
	 */
	private SPLHelper helper;
	private final int BUFFER_SUSECSS = 1;
	private Handler mHandler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
//			Log.v("bug11", msg.what+"");
			switch (msg.what){

			case BUFFER_SUSECSS:
				if(SPLBuffer!= null && msg.obj!=null){
					SPLBuffer= (ArrayList<List<Double>>) msg.obj;
				}
					
				break;
			}
		}
		
	};
	
	
	@Override
	protected void onDetachedFromWindow() {		//view 摧毁时调用
//		util.shutdown();
		super.onDetachedFromWindow();
	}
	
	public void startCaculate() {
		if (context.getClass().getSimpleName().equals("DriveModeActivity")) 	ifEnterToDriverMode=true;
		switch(DataCollection.collectionState){
		case 0:
			//--------------初次计算前清空曲线------
			if (!ifStartToGetData||ifEnterToDriverMode) {
				if (SPLBuffer.get(0).size() > 0) {
					for (int i = 0; i <SPLBuffer.size(); i++) {
						SPLBuffer.get(i).clear();
					}
					if(!ifEnterToDriverMode) changeX = 0;
				}
				ifStartToGetData = true;
				if (context.getClass().getSimpleName().equals("MainActivity")) 	ifEnterToDriverMode=false;
			}
			//------------计算--------------------
			helper.caculate(DataCollection.dataListMap);
			break;
		case 1:
			 if (ifStartToGetData == true) {
					DataCollection.collectionOverCalculateCount += 1;
					bufferIntListIndex = 0;
					ifStartToGetData = false;
					spl.resetResult();
			}
			break;
		}
	}

	public void setHelper() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<List<Float>> getResultBuffer() {
		// TODO Auto-generated method stub
		return null;
	}
}
