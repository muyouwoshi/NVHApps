package draw.map;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import bottom.drawer.AddChannelViewPager;

import com.example.mobileacquisition.MainActivity;
import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;
import common.XclSingalTransfer;
import mode.drive.DriveModeActivity;

public class Signal extends ScaleView {
	// private Ruler ruler;
	private float px = 100;
	private float x = 102;
	protected float maxValue;
	private ArrayList<List<Float>> SignalBuffer;
	private float py = 0;
	private float y = 0;
	// private Activity context;
	private boolean startToFindZero = true;
	// private int reduceData;
	private int j;
	private byte endlessData = -1;// δ��ʼ��ֵ״̬
	private DecimalFormat df = new DecimalFormat();
	private int count = 0;
	private int channalCount;
	private boolean ifStartToGetData = false;
	private static boolean ifEnterToDriverMode = false;
	private int bufferIntListIndex;
	private XclSingalTransfer xclSingalTransfer;

	public Signal(Context context) {
		super(context);
		// this.viewPager = viewPager;
		// --------驾驶|普通采集模式数据同步专用功能---------
		if (context.getClass().getSimpleName().equals("DriveModeActivity"))
			setBufferIntListIndex();
		// --------完毕--------------------------------------------
	}

	public Signal(Context context, MyViewPager viewPager) {
		super(context, viewPager);
		// this.viewPager = viewPager;
		SignalBuffer = new ArrayList<List<Float>>();
		helper = new SignalHelper(mHandler, null, SignalBuffer);
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);

		if (xlabelState == null)
			xlabelState = preference.getString("Signal_XAxis", "s");
		if (ylabelState == null)
			ylabelState = preference.getString("Signal_YAxis", "Pa");
		refreshLabelState();

		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		// channalCount = 8;
		// for (int i = 0; i < channalCount; i++) {
		// SignalBuffer.add(new ArrayList<Float>());
		// }
		// --------驾驶|普通采集模式数据同步专用功能---------
		if (context.getClass().getSimpleName().equals("DriveModeActivity"))
			setBufferIntListIndex();
		// --------完毕--------------------------------------------
	}

	public void init() {
		super.init();
		xGrid = 160;
		divider = 2;
		offsetX = 1 / acquiFreq;
		xmultiple = offsetX / divider;
		ymultiple = 0.01f;
		if (helper != null)
			helper.setLength((int) (vieww / divider + 0.5f));
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		if (context.getClass().getSimpleName().equals("MainActivity"))
			isActivated_ChanNum = ((MainActivity) context).bottomOperate.addChannelViewPager.isActivated_ChanNum;
		else if (context.getClass().getSimpleName().equals("DriveModeActivity"))
			isActivated_ChanNum = ((DriveModeActivity) context).addChannelViewPager.isActivated_ChanNum;
		if (isActivated_ChanNum == null || isActivated_ChanNum.size() == 0)
			return;
		if (SignalBuffer == null || SignalBuffer.size() <= 0) {
			return;
		}
		canvas.save();
		canvas.clipRect(left, 0, vieww, viewh - 50);

		getAutoRate((ArrayList<Float>) SignalBuffer.get(0), ybaseLine);
		for (int i = 1; i <maxDataCount; i++) {
			int index = 0;
			while (index < isActivated_ChanNum.size()) {
				if(DataCollection.dataListMap.containsKey(isActivated_ChanNum.get(index)+1)){
					List<Float> dataList = SignalBuffer.get(index);
					if(maxDataCount<dataList.size()){
						maxDataCount=dataList.size();
					}
					if(dataList.size()==0||dataList.size()-1<i||i>=dataList.size()){
						index++;
						continue;
					}
					System.out.println("i:"+i);
					System.out.println("dataList.size():"+dataList.size());
					py = ybaseLine - dataList.get(i-1)/5000 * syrate / ymultiple;
					px = xbaseLine + i * sxrate * divider;
					y = ybaseLine - dataList.get(i)/5000 * syrate / ymultiple;
					x = px + sxrate * divider;
					if (paintList != null && paintList.size() > 0) {
						if (index + 1 > paintList.size()) {
							canvas.drawLine(px, py, x, y, paintList.get(0));
						} else {
							canvas.drawLine(px, py, x, y, paintList.get(index));
						}
					}
					System.out.println(maxDataCount);

				}
				index++;
			}
			// ------------------锟斤拷锟斤拷-------------
		}
		canvas.restore();
	}

	public void getDataResult(int channelNum) {
		if (channelNum > SignalBuffer.size())
			return;
		if (SignalBuffer != null && channelNum != -1 && SignalBuffer.size() > 01) {
			resultDatalist = (ArrayList) SignalBuffer.get(channelNum);
		}
	}

	protected void getAutoRate(ArrayList<Float> list, float ybase) {
		if (list.size() == 0)
			return;
		maxValue = list.get(0) / ymultiple;
		int i = 0;
		int length = list.size();
		while (i < length - 1) {
			float value = list.get(i) / ymultiple;
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

	// private String getUnit() {
	// return context.getSharedPreferences("hz_5D", 0).getString(
	// "Signal_XAxis", "s");
	// }
	// �Ա�ʹ��
	private float byte2float(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}

	protected void refreshXmultiple() {
		// TODO Auto-generated method stub
		if (xlabelState.equals("s")) {
			offsetX = 1 / acquiFreq;
			xmultiple = offsetX / divider;
		} else if (xlabelState.equals("ms")) {
			offsetX = 1 / acquiFreq;
			xmultiple = offsetX / divider * 1000;
		}
	}

	@Override
	protected void yDataChange() {
		// TODO Auto-generated method stub
		if (YUNIT_SWITCH_MODE == 0)
			return;
		else if (YUNIT_SWITCH_MODE == 2) { // db �л��� pa
			int listCount = SignalBuffer.size();
			ArrayList<List<Float>> lists = new ArrayList<List<Float>>();
			for (int i = 0; i < listCount; i++) {
				List<Float> list = new ArrayList<Float>();

				Iterator<Float> it = SignalBuffer.get(i).iterator();
				while (it.hasNext()) {
					float data = it.next();

					data = (float) (p0 * (Math.pow(10, (data / 20))));

					list.add(data);
				}
				lists.add(list);
			}

			SignalBuffer.clear();
			SignalBuffer = lists;
		} else { // pa �л��� db

			int listCount = SignalBuffer.size();
			ArrayList<List<Float>> lists = new ArrayList<List<Float>>();
			for (int i = 0; i < listCount; i++) {
				List<Float> list = new ArrayList<Float>();

				Iterator<Float> it = SignalBuffer.get(i).iterator();
				while (it.hasNext()) {
					float data = it.next();

					data = (float) (20 * (Math.log10(data / p0)));

					list.add(data);
				}
				lists.add(list);
			}

			SignalBuffer.clear();
			SignalBuffer = lists;
		}

	}

	// -----------驾驶|普通采集同步专用index----------
	public void setBufferIntListIndex() {
		if (xclSingalTransfer == null) {
			xclSingalTransfer = XclSingalTransfer.getInstance();
		}
		bufferIntListIndex = xclSingalTransfer.containsKey("Signal_DataCollectionIntBufferListIndex")
				? (Integer) xclSingalTransfer.getValue("Signal_DataCollectionIntBufferListIndex") : 0;
	}

	/**
	 * 优化绘图使用
	 * 
	 * @author zhoujie
	 *
	 */
	private SignalHelper helper;
	private final int BUFFER_SUSECSS = 0;
	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// Log.v("bug11", msg.what+"");
			switch (msg.what) {

			case BUFFER_SUSECSS:
				if (DataCollection.collectionState != 2) {
					if (SignalBuffer != null && msg.obj != null) {
						SignalBuffer = (ArrayList<List<Float>>) msg.obj;
					}
					invalidate();
				}
				break;
			}
		}

	};

	@Override
	protected void onDetachedFromWindow() { // view 摧毁时调用
		// util.shutdown();
		super.onDetachedFromWindow();
	}

	public void startCaculate() {
		switch (DataCollection.collectionState) {
		case 0:
			// --------------初次计算前清空曲线------
			if (!ifStartToGetData) {
				if (SignalBuffer.size() > 0) {
					for (int i = 0; i < SignalBuffer.size(); i++) {
						SignalBuffer.get(i).clear();
					}
					if (!ifEnterToDriverMode)
						changeX = 0;
				}
				ifStartToGetData = true;
				ifEnterToDriverMode = false;
			}
			// //------------计算--------------------
			helper.caculate(DataCollection.dataListMap);
			break;
		case 1:
			if (ifStartToGetData == true) {
				DataCollection.collectionOverCalculateCount += 1;
				// bufferIntListIndex = 0;
				ifStartToGetData = false;
				helper.lineDataSizeList.clear();
				helper.lineDataSizeList.add(-1);
				helper.dataIndex = 0;
				helper.lineDataSizeListIndex = 0;
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
