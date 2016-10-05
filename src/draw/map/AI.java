package draw.map;

import java.util.ArrayList;
import java.util.List;

import mode.drive.DriveModeActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;

import com.algorithm.helper.AIHelper;
import com.calculate.Arith_AI;
import com.example.mobileacquisition.MainActivity;
import common.DataCollection;
import common.MyViewPager;
import common.ScaleView;
import common.XclSingalTransfer;
public class AI extends ScaleView{
	private float maxValue;
	private float px = 100;
	private float x = 102;
	private float py = 0;
	private float y = 0;
	private Arith_AI ai;
	private ArrayList<List<Float>> AIBuffer;
	private boolean ifStartToGetData = false;
	private static boolean ifEnterToDriverMode=false;
	private ArrayList<Integer> isActivated_ChanNum;
	private XclSingalTransfer xclSingalTransfer;
	
	
	public AI(Context context) {
		super(context);
		// this.context = (Activity) context;
		ai = new Arith_AI();
		isFrist = true;
		// init();
		setBufferIntListIndex();
	}

	public AI(Context context, MyViewPager viewPager) {
		super(context, viewPager);
		// this.context = (Activity) context;
		ai = new Arith_AI();
		isFrist = true;
		
		AIBuffer = new ArrayList<List<Float>>();
//		channalCount = 8;
//		for (int i = 0; i < channalCount; i++) {
//			AIBuffer.add(new ArrayList<Float>());
//		}
		// init();
		
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
		xlabelState = preference.getString("AI_XAxis", "s");
		ylabelState = preference.getString("AI_YAxis", "%");
		refreshLabelState();
		
	
//		helper.setInfo(checkedChannelIndexArray);
		setBufferIntListIndex();
	}

	public void init() {
		super.init();
		ybaseLine = viewh - 50;

		divider = 2.0f; // 每锟斤拷锟斤拷锟斤拷锟斤拷锟截伙拷一锟斤拷锟斤拷
		offsetX = 0.002f; // 每锟斤拷锟斤拷锟�0.002锟斤拷
		xmultiple = offsetX / divider; // 每px锟斤拷锟�0.001锟斤拷;

		ymultiple = (float) (10000.0 / (viewh - 50));
		float ygrid = 100 / ymultiple;

		int n = 0;
		while (ymultiple / (int) Math.pow(2, n) >= 1) {
			n += 1;
		}

		yGrid = ((int) Math.pow(2, n)) * ygrid;
		ymultiple = ymultiple / 100;
		if (yGrid > xGrid) {
			yGrid = yGrid / 2;
			// ymultiple = ymultiple/2;
		}	
		
		AIHelper.getInstance().addViewQueue(this);
			
		 
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		if (context.getClass().getSimpleName().equals("MainActivity")) 
			isActivated_ChanNum=((MainActivity)context).bottomOperate.addChannelViewPager.isActivated_ChanNum;
		else if(context.getClass().getSimpleName().equals("DriveModeActivity"))
			isActivated_ChanNum=((DriveModeActivity)context).addChannelViewPager.isActivated_ChanNum;
		if(isActivated_ChanNum==null||isActivated_ChanNum.size()==0) return;
		if (AIBuffer == null || AIBuffer.size() ==0 ) {
			return;
		}
		canvas.save();
		canvas.clipRect(left, 0, vieww, viewh - 50);
//		getAutoRate((ArrayList<Float>) AIBuffer.get(0), ybaseLine);	
		for (int i = 1; i <maxDataCount; i++) {
			int index = 0;
			while (index < isActivated_ChanNum.size()) {
				if(DataCollection.dataListMap.containsKey(isActivated_ChanNum.get(index)+1)){
					List<Float> dataList = AIBuffer.get(index);
					if(maxDataCount<dataList.size()-1){
						maxDataCount=dataList.size()-1;
					}
					if(dataList.size()==0||dataList.size()-1<i||i==dataList.size()){
						index++;
						continue;
					}
					py = ybaseLine - dataList.get(i-1) * syrate / ymultiple;
					px = xbaseLine + i * sxrate * divider;
					y = ybaseLine - dataList.get(i) * syrate / ymultiple;
					x = px + sxrate * divider;
					if (paintList != null && paintList.size() > 0) {
						if (index + 1 > paintList.size()) {
							canvas.drawLine(px, py, x, y, paintList.get(0));
						} else {
							canvas.drawLine(px, py, x, y, paintList.get(index));
						}
					}
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
		if (channelNum > AIBuffer.size())
			return;
		if (AIBuffer != null && channelNum != -1 && AIBuffer.get(channelNum).size() > 0) {
			resultDatalist = (ArrayList) AIBuffer.get(channelNum);

		}
	}

	private void getAutoRate(ArrayList<Float> list, float ybase) {
		if (list.size() == 0)
			return;
		maxValue = list.get(0) / ymultiple;
		int i = 0;
		while (i < list.size()) {
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

	private String getUnit() {
		return context.getSharedPreferences("hz_5D", 0).getString("AI_XAxis", "s");
	}

	@Override
	public List<Float> getCursorValue(float moveX) {
		// TODO Auto-generated method stub
		List<Float> list = new ArrayList<Float>();

		float xValue = 0;
		float yValue = 0;
		float yPosition = ybaseLine;

		getDataResult(cursorNum);
		if (divider == 0 || xlabelState == null || ylabelState == null || cursorNum == -1) {
			list.add(xValue);
			list.add(yValue);
			list.add(yPosition);
			return list;
		}

		xValue = ((moveX - xbaseLine + 50) / sxrate) * xmultiple + changeX;

		if ("s".equals(xlabelState)) {
			xlabelState = "s";
		} else if ("ms".equals(xlabelState)) {
			xValue = xValue * 1000;
			xlabelState = "ms";
		} else if ("Hz".equals(xlabelState)) {
			xlabelState = "Hz";
			xValue = moveX * xmultiple / sxrate;
		} else if ("RPM".equals(xlabelState)) {
			xlabelState = "RPM";
			// xValue = moveX;
		}

		int x = (int) ((moveX - xbaseLine + 50) / divider / sxrate);

		if (resultDatalist != null && resultDatalist.size() > 0) {
			if (x < resultDatalist.size() && x > 0) {
				yValue = Float.parseFloat(resultDatalist.get(x).toString());

			} else if (x >= resultDatalist.size()) {
				yValue = Float.parseFloat(resultDatalist.get(resultDatalist.size() - 1).toString());
			} else if (x == 0) {
				yValue = (Float.parseFloat(resultDatalist.get(0).toString()));
			}
		}

		yPosition = ybaseLine - yValue / ymultiple * syrate;

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
	
	//-----------驾驶|普通采集同步专用index----------
	public void setBufferIntListIndex(){
		if(xclSingalTransfer==null){
			xclSingalTransfer=XclSingalTransfer.getInstance();
		}
//		bufferIntListIndex=xclSingalTransfer.containsKey("SPL_DataCollectionIntBufferListIndex")?(Integer) xclSingalTransfer.getValue("SPL_DataCollectionIntBufferListIndex"):0;
	}

	
	
	@Override
	protected void onDetachedFromWindow() {		//view 摧毁时调用
//		util.shutdown();
		super.onDetachedFromWindow();
	}


	public void setResult(ArrayList<List<Float>> list) {
		// TODO Auto-generated method stub
		this.AIBuffer = list;
		this.invalidate();
	}

	public ArrayList<List<Float>> getResultBuffer() {
		// TODO Auto-generated method stub
		return AIBuffer;
	}

}
