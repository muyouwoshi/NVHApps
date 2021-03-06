package draw.map;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;
import bottom.drawer.AddChannelViewPager;

import com.calculate.Arith_RPM;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;

import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;

public class RPM_Curve extends ScaleView {

	private float maxValue;
	private float px = 100;
	private float x = 102;
	private float py = 0;
	private float y = 0;
	private Paint mPaint;
	// private int[] voiceData;
	// private Arith_AI ai;
	// private Activity context;
	private List<int[]> RPMBufferArray;
	private  List<float[]> RPMResultBufferArray;
	private List<ArrayList<Float>> RPMBuffer;
	private int xlabelSer;
	private int triggerMode = 0;
	private float lower = -1;
	private float upper = 100000.0f;
	private int step = 0;
	private boolean isRunUpMode = false;
	private Arith_RPM rpm;
	private int skipcount;
	private int[] checkedTachoChannel = {1,0};
	private int lastIndex = -1;
	private int channalCount;
	private int oneTimeTips=0;
	public RPM_Curve(Context context) {
		super(context);
		// this.context = (Activity) context;
		// AIBuffer = new ArrayList<Float>();
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		// ai = new Arith_AI();
		isFrist = true;
		rpm = new Arith_RPM();
		// init();
	}

	public RPM_Curve(Context context, MyViewPager viewPager) {
		super(context, viewPager);
		// this.context = (Activity) context;
		// AIBuffer = new ArrayList<Float>();
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		// ai = new Arith_AI();
		isFrist = true;
		rpm = new Arith_RPM();
		// init();
	}

	public void init() {
		super.init();
		ybaseLine = viewh - 50;
		divider = 2;

		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);

		if(xlabelState ==null)xlabelState = preference.getString("RPM_XAxis", "s");
		if(ylabelState ==null)ylabelState = preference.getString("RPM_YAxis", "Pa");
		channalCount=8;
		RPMBuffer = new ArrayList<ArrayList<Float>>();
		RPMBufferArray = new ArrayList<int[]>();
		RPMResultBufferArray = new ArrayList<float[]>();
		for (int i = 0; i < channalCount; i++) {
			RPMBuffer.add(new ArrayList<Float>());
		}
		refreshLabelState();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		/*
		 * if (AIBuffer != null && AIBuffer.size() == 0) { if (changeX != 0) {
		 * xlabelSer++; changeX = 0.002f * xlabelSer; } }
		 */
		super.onDraw(canvas);
		if(DataCollection.hardType!=0||IAudioTrack.hardType!=0){
			if(oneTimeTips==0){
				Toast.makeText(context, context.getResources().getString(R.string.draw_failed), Toast.LENGTH_SHORT).show();
			}
			oneTimeTips=1;
		}
		if(DataCollection.collectionState==1){
			DataCollection.collectionOverCalculateCount+=1;
			oneTimeTips=0;
		}else if(IAudioTrack.readState == 3||IAudioTrack.readState == 1){
			IAudioTrack.audioOverCalculateCount+=1;
			oneTimeTips=0;
		}
		if(oneTimeTips==1||DataCollection.collectionState>0&&IAudioTrack.readState>0){
			return;
		}
//		if (!isRunUpMode) {
//			SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
//			triggerMode = preference.getInt("Trigger_Mode_ID", 0);
//			String lowerStr = preference.getString("Trigger_Lower", "");
//			if (lowerStr.matches("-?[0-9]+.[0-9]*")) {
//				lower = Float.valueOf(lowerStr);
//			}
//			String upperStr = preference.getString("Trigger_Upper", "");
//			if (upperStr.matches("-?[0-9]+.[0-9]*")) {
//				upper = Float.valueOf(upperStr);
//			}
//			String stepLengthStr = preference.getString("Trigger_Step_Length", "");
//			if (stepLengthStr.matches("-?[0-9]+.[0-9]*")) {
//				step = Integer.valueOf(stepLengthStr);
//			}
//			isRunUpMode = true;
//		}
//		if (context.getClass().getSimpleName().equals("MainActivity")) {
//			// if (AddChannelViewPager.channelCount.size() == 0)
//			// return;
//			if(DataCollection.isRecording){
//				getBuffer(
//					((MainActivity) context).getPlayBackState() ? IAudioTrack.mAudioTrackIntBuffer : DataCollection.buffer,
//					canvas);
//			}
//			// getBuffer(DataCollection.buffer_rpm, canvas);
//		} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
//			// if (AddChannelViewPager.drive_channelCount.size() == 0)
//			// return;
//			// getBuffer(DataCollection.buffer_rpm, canvas);
//			if(DataCollection.isRecording){
//				getBuffer(DataCollection.buffer, canvas);
//			}
//		}
	
		// getAutoRate(AIBuffer, ybaseLine);

		canvas.save();
		canvas.clipRect(left, 0, vieww, viewh - 50);
		if (lastIndex < 0) {
			return;
		}
		for (int i = 0; i < RPMBuffer.get(lastIndex).size() - 1; i++) {
			int index = 0;
			while (index < RPMBuffer.size() - 1) {
				if (checkedTachoChannel[index] == 0) {
					++index;
					continue;
				}
				List<Float> linelist = RPMBuffer.get(index);
				float data = linelist.get(i);
				float datalater = linelist.get(i + 1);
				switch (triggerMode) {
				case 1:// runup
					if (data >= datalater) {
						continue;
					}
					break;
				case 2:// rundown
					if (data <= datalater) {
						continue;
					}
					break;
				case 3:// up_and_down
					break;
				}
				if (data >= lower && datalater <= upper) {
					py = ybaseLine - data  * syrate;
					px = xbaseLine + i * sxrate * divider;
					y = ybaseLine - datalater  * syrate;
					x = px + sxrate * divider;
					mPaint.setColor(Legend.linePaint.getColor());
					canvas.drawLine(px, py, x, y, mPaint);
				}
				++index;
			}
		}
		canvas.restore();

	}

	public void getDataResult() {
		/*
		 * if (AIBuffer != null && AIBuffer.size() > 0) { resultDatalist =
		 * AIBuffer; }
		 */
	}

	private void getAutoRate(ArrayList<Float> list, float ybase) {
		if (list.size() == 0)
			return;
		maxValue = list.get(0) ;
		int i = 0;
		while (i < list.size()) {
			float value = list.get(i);
			if (value < 0)
				value = 0 - value;
			if (value  > maxValue)
				maxValue = value / 
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

	private void getBuffer(int[] buffer, Canvas canvas) {
		if (buffer == null)
			return;
		// float[] rpmdata = new float[buffer.length / 2];
		// ByteBuffer rpmbuffer = ByteBuffer.wrap(buffer);
		// for (int i = 0; rpmbuffer.position() < rpmbuffer.capacity(); i ++) {
		// rpmdata[i] = rpmbuffer.getFloat();
		// }
		rpm.InitTacho(32768);
	
		int xsize = 0;
		int count = 0;
		for (int i = 0; i < buffer.length-1; i+=2) {
			count = 0;
			while (count < checkedTachoChannel.length) {
				if (checkedTachoChannel[count] == 0) {
					++count;
					continue;
				}
				while(RPMBufferArray.size()<2){
					RPMBufferArray.add(new int[buffer.length/2]);
				}
				RPMBufferArray.get(count)[xsize] =buffer[i+count] ;
				++count;
			}
			++xsize;
		}
		count = 0;
		while (count < checkedTachoChannel.length) {
			if (checkedTachoChannel[count] == 0) {
				++count;
				continue;
			} else {
				lastIndex = count;
			}
			int[] bufferarray = RPMBufferArray.get(count);
			rpm.DecodeTacho(bufferarray, bufferarray.length);
			float[] result = rpm.getRPM();
			if (result == null) {
				result = new float[1];
			}
			if (RPMResultBufferArray.size() < count + 1) {
				RPMResultBufferArray.add(result);
			} else {
				RPMResultBufferArray.set(count, result);
			}
			count++;
		}
		count = 0;
		for (int i = 0; i < RPMResultBufferArray.get(lastIndex).length - 1; i++) {
			count=0;
			while (count < checkedTachoChannel.length - 1) {
				if (checkedTachoChannel[count] == 0) {
					++count;
					continue;
				}
				List<Float> lineList = RPMBuffer.get(count);
				float[] resultArray = RPMResultBufferArray.get(count);
				lineList.add(resultArray[i]);
				if (lineList.size() > (canvas.getWidth() - 50) / divider) {
					lineList.remove(0);
				}
				count++;
			}
		}
	}
	
	@Override
	protected void yDataChange() {
		// TODO Auto-generated method stub
		if(YUNIT_SWITCH_MODE == 0) return;	
		else if(YUNIT_SWITCH_MODE == 2){   	  // pa �л���   db
			int listCount = RPMBuffer.size();
			List<ArrayList<Float>> lists = new ArrayList<ArrayList<Float>>();
			for(int i =0 ; i< listCount ;i++){
				ArrayList<Float> list= new ArrayList<Float>();
				
				Iterator<Float> it = RPMBuffer.get(i).iterator();
				while(it.hasNext()){
					float data = it.next();
					
					data=(float) (p0*(Math.pow(10, (data/ 20))));
					
					list.add(data);
				}
				lists.add(list);
			}
			
			RPMBuffer.clear();
			RPMBuffer = lists;
		}else {					// db �л���     pa

			int listCount = RPMBuffer.size();
			List<ArrayList<Float>> lists = new ArrayList<ArrayList<Float>>();
			for(int i =0 ; i< listCount ;i++){
				ArrayList<Float> list= new ArrayList<Float>();
				
				Iterator<Float> it = RPMBuffer.get(i).iterator();
				while(it.hasNext()){
					float data = it.next();
									  
					data=(float) (20*(Math.log10(data/p0))); 
					
					list.add(data);
				}
				lists.add(list);
			}
			
			RPMBuffer.clear();
			RPMBuffer = lists;
		}
		
	}



	@Override
	public ArrayList<List<Float>> getResultBuffer() {
		// TODO Auto-generated method stub
		return null;
	}
}
