package draw.map;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import mode.drive.DriveModeActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import bottom.drawer.AddChannelViewPager;

import com.calculate.Arith_Order;
import com.calculate.Arith_RPM;
import com.example.mobileacquisition.MainActivity;
import com.example.mobileacquisition.R;

import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;
import left.drawer.AnalogFragment;
import mode.drive.DriveModeActivity;

public class Order extends ScaleView {
	// private Activity context;// ԭΪ MainActivity
	private Arith_Order order;
	private Arith_RPM rpm;

	private ArrayList<int[]> OrderBufferArray = new ArrayList<int[]>();
	private List<float[]> OrderResultList = new ArrayList<float[]>();
	private ArrayList<List<float[]>> OrderBuffer;

	private int count;
	private int lastIndex = -1;
	private int channalCount;
	//private SharedPreferences preference;
	//private Activity context;
	private Map<String, List<String>> choosedChannelsAndOrders;
	private float px, x;
	private float py = 0;
	private float y = 0;
	// ------------�������ɾ��--------------
	private List<Integer> tempList = new ArrayList<Integer>();
	private int[] tempBuffer;
	private float[] rpmdata = new float[459];
	private int oneTimeTips=0;
	// -------------���---------------
	public Order(Context context) {
		super(context);
	//	this.context = (Activity) context;
		order = new Arith_Order();
	//	preference = context.getSharedPreferences("hz_5D", 0);
	//	xlabelState = preference.getString("Order_XAxis", "s");
	//	ylabelState = preference.getString("Order_YAxis", "Pa");
	}

	public Order(Context context, MyViewPager viewPager) {

		super(context, viewPager);
		order = new Arith_Order();
	//	this.context = (Activity) context;
		
	}

	public void init() {
		super.init();
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);
		if(xlabelState ==null)xlabelState = preference.getString("Order_XAxis", "RPM");
		if(ylabelState ==null)ylabelState = preference.getString("Order_YAxis", "Pa");
		xGrid = 125;
		ybaseLine = viewh - 50;
		divider = 2;
		OrderBuffer = new ArrayList<List<float[]>>();
		channalCount = 8;
		for (int i = 0; i < channalCount; i++) {
			OrderBuffer.add(new ArrayList<float[]>());
		}
		getRPMBufferFromFile("sdcard/data/rpm_order.txt");
		refreshLabelState();
	}

	// -------------�����ɾ-------------------
	private void getRPMBufferFromFile(String rpmPath) {
		File rpmfile = new File(rpmPath);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(rpmfile)));
			String dataStr = null;
			int i = 0;
			while ((dataStr = reader.readLine()) != null) {
				rpmdata[i] = Float.valueOf(dataStr);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// -------------���------------------------
	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		if(DataCollection.hardType!=0||IAudioTrack.hardType!=0){
//			if(oneTimeTips==0){
//				Toast.makeText(context, context.getResources().getString(R.string.draw_failed), Toast.LENGTH_SHORT).show();
//			}
//			oneTimeTips=1;
//		}
//		if(DataCollection.collectionState==1){
//			DataCollection.collectionOverCalculateCount+=1;
//			oneTimeTips=0;
//		}else if(IAudioTrack.readState == 3||IAudioTrack.readState == 1){
//			IAudioTrack.audioOverCalculateCount+=1;
//			oneTimeTips=0;
//		}
//		if(oneTimeTips==1||DataCollection.collectionState>0&&IAudioTrack.readState>0){
//			return;
//		}
//		if (context.getClass().getSimpleName().equals("MainActivity")) {
//			if (AddChannelViewPager.channelCount.size() == 0)
//				return;
//			choosedChannelsAndOrders = ((MainActivity) context).bottomOperate.addChannelViewPager.choosedChannelsAndOrders;
//			if (choosedChannelsAndOrders == null) {
//				return;
//			}
//
//			if (DataCollection.isRecording) {
//				getBuffer(((MainActivity) context).getPlayBackState() ? IAudioTrack.mAudioTrackIntBuffer
//						: DataCollection.buffer, canvas);
//			}
//
//			if (!((MainActivity) context).getPlayBackState()) {
//				getBuffer(DataCollection.buffer, canvas);
//				if (IAudioTrack.readState == 2) {
//					Iterator<Integer> it = tempList.iterator();
//					int i = 0;
//					while (it.hasNext()) {
//						tempBuffer[i] = it.next();
//						i++;
//					}
//					getBuffer(tempBuffer, canvas);
//				}
//			} else {
//				try {
//					if (IAudioTrack.mAudioTrackIntBufferList.size() == 0) {
//						return;
//					}
//					int[] testdata = ((LinkedList<int[]>) IAudioTrack.mAudioTrackIntBufferList).getFirst();
//					for (int i = 0; i < testdata.length; i++) {
//						tempList.add(testdata[i]);
//					}
//					((LinkedList<int[]>) IAudioTrack.mAudioTrackIntBufferList).removeFirst();
//					if (IAudioTrack.readState == 2) {
//						Iterator<Integer> it = tempList.iterator();
//						int i = 0;
//						while (it.hasNext()) {
//							tempBuffer[i] = it.next();
//							i++;
//						}
//						getBuffer(tempBuffer, canvas);
//					}
//				} catch (Exception e) {
//					System.out.println(e);
//				}
//			}
//			// if (!((MainActivity) context).getPlayBackState()) {
//			// getBuffer(DataCollection.buffer, canvas);
//			// } else {
//			// }
//			// getBuffer(((MainActivity) context).getPlayBackState() ?
//			// IAudioTrack.mAudioTrackIntBuffer
//			// : DataCollection.buffer, canvas);
//
//	}else if(context.getClass().getSimpleName().equals("DriveModeActivity"))
//
//	{
//		if (AddChannelViewPager.drive_channelCount.size() == 0)
//			return;
//		choosedChannelsAndOrders = ((DriveModeActivity) context).getAddChannelViewPager().choosedChannelsAndOrders;
//		if (choosedChannelsAndOrders == null) {
//			return;
//		}
//		if (DataCollection.isRecording) {
//			getBuffer(DataCollection.buffer, canvas);
//		}
//	} else if(context.getClass().getSimpleName().equals("CalibrationActivity"))
//
//	{
//		// getBuffer(CalibrationDataCollection.buffer, canvas);
//	}
//
//	int arrayIndex = 0;
//	int maxLength = 0;for(
//	int i = 0;i<OrderBuffer.size();i++)
//
//	{
//		int index = 0;
//		float[] orderdataArray = null;
//		while (index < activityChannelArray.length) {
//			if (activityChannelArray[index] == 0) {
//				index++;
//				continue;
//			}
//			List<float[]> orderBufferList = OrderBuffer.get(index);
//			for (int j = 0; j < orderBufferList.size(); j++) {
//				orderdataArray = (float[]) orderBufferList.get(j);
//				if (arrayIndex + 2 == orderdataArray.length) {
//					continue;
//				} else {
//					if (maxLength < orderdataArray.length) {
//						maxLength = orderdataArray.length;
//					}
//				}
//				py = (float) (ybaseLine - orderdataArray[arrayIndex] * syrate);
//				px = xbaseLine + i * sxrate * divider;
//				y = (float) (ybaseLine - orderdataArray[arrayIndex + 1] * syrate);
//				x = px + sxrate * divider;
//			}
//			index++;
//		}
//		if (orderdataArray != null) {
//			if (arrayIndex + 1 <= maxLength - 1) {
//				arrayIndex++;
//			}
//		}
//	}

	}

	private void getBuffer(int[] Buffer, Canvas canvas) {
		if (Buffer == null || Buffer.length < 2)
			return;
		//--------��ȡת��-----------
//		if (rpm == null) {
//			rpm = new Arith_RPM();
//		}
//		rpm.InitTacho(32768);
//		rpm.DecodeTacho(Buffer,Buffer.length);
//		float[] rpmres = rpm.getRPM();
//		if (rpmres == null) {
//			return;
//		}
		//--------���-----------
		// ��ȡ���
//		if (hardType == 0) {
//			int xsize = 0;
//			for (int i = 0; i < Buffer.length / channalCount; i++) {
//				count = 0;
//				int data = 0;
//				int bufferData = 0;
//				// List<Integer> lineBufferList = null;
//				while (count < checkedChannelIndexArray.length) {
//					if (checkedChannelIndexArray[count] == 0) {
//						if (OrderBufferArray.size() < count + 1) {
//							OrderBufferArray.add(new int[1]);
//						}
//						count++;
//						continue;
//					} else {
//						lastIndex = count;
//					}
//					int bufferindex = channalCount * i + count;
//					bufferData = Buffer[bufferindex];
//					if (OrderBufferArray.size() < count + 1) {
//						OrderBufferArray.add(new int[Buffer.length / channalCount]);
//					}
//					OrderBufferArray.get(count)[xsize] = bufferData;
//					count++; 
//				}
//				xsize++;
//			}
//			int index = 0;
//			while (index < checkedChannelIndexArray.length) {
//				if (checkedChannelIndexArray[index] == 0) {
//					if (OrderBuffer.size() < index + 1) {
//						OrderBuffer.add(new ArrayList<float[]>());
//					}
//					index++;
//					continue;
//				}
//				int[] dataArray = OrderBufferArray.get(index);
//				order.setRPMData(rpmdata, 50);
//				order.integer_calculate(dataArray, dataArray.length);
//				List<String> orderValueList = choosedChannelsAndOrders.get(String.valueOf(index + 1));
//				// OrderResultList�洢һ��ͨ�������������
//				Iterator<String> it = orderValueList.iterator();
//				int i = 0;
//				while (it.hasNext()) {
//					float orderValue = Float.valueOf(it.next());
//					float[] OrderResult = order.getOrder(orderValue);
//					if (OrderResultList.size() < orderValueList.size() + 1) {
//						OrderResultList.add(OrderResult);
//					} else {
//						OrderResultList.set(i, OrderResult);
//					}
//					i++;
//				}
//				if (OrderBuffer.size() < index + 1) {
//					OrderBuffer.add(OrderResultList);
//				} else {
//					OrderBuffer.set(index, OrderResultList);
//				}
//				// ���
//				index++;
//			}
//		}
	}


	@Override
	public ArrayList<List<Float>> getResultBuffer() {
		// TODO Auto-generated method stub
		return null;
	}
}
