package draw.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import bottom.drawer.AddChannelViewPager;

import com.calculate.Arith_OCT;
import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;
import common.XclSingalTransfer;

public class OCT extends ScaleView {
	private double maxValue;
	private float px, x;
	private float py = 0;
	private float y = 0;
	private ArrayList<List<Float>> OCTBuffer;
	private ArrayList<int[]> OCTBufferArray;
	private List<float[]> OCTResultList = new ArrayList<float[]>();
	private int[] voiceData;
	private Arith_OCT oct;
	// private Activity context;
	private String[] xAxisText;
	private float rectWidth;
	private boolean startToFindZero = true;
	private int j;
	private int lastIndex = -1;
	private int channalCount;
	private int pre_size = 0;
	private int bufferIntListIndex = 0;
	private XclSingalTransfer xclSingalTransfer;
	private boolean ifStartToGetData=false;
	private boolean ifEnterToDriverMode=false;
	public OCT(Context context) {
		super(context);
		// this.context = (Activity) context;
		oct = new Arith_OCT();
		CoordinatePaint = new Paint();
		CoordinatePaint.setColor(Color.rgb(234, 234, 234));
		LablePaint = new Paint();
		LablePaint.setColor(Color.rgb(64, 64, 64));
		xAxisText = new String[] { "20", "25", "31.5", "40", "50", "63", "80", "100", "125", "160", "200", "250", "315",
				"400", "500", "630", "800", "1k", "1.25k", "1.6k", "2k", "2.5k", "3.15k", "4k", "5k", "6.3k", "8k",
				"10k", "12.5k", "16k" };
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);

		if (xlabelState == null)
			xlabelState = preference.getString("OCT_XAxis", "Hz");
		if (ylabelState == null)
			ylabelState = preference.getString("OCT_YAxis", "Pa");
		OCTBuffer = new ArrayList<List<Float>>();
		OCTBufferArray = new ArrayList<int[]>();
		channalCount = 8;
		for (int i = 0; i < channalCount; i++) {
			OCTBuffer.add(new ArrayList<Float>());
		}
		refreshLabelState();
		setBufferIntListIndex();
	}

	public OCT(Context context, MyViewPager viewPager) {
		super(context, viewPager);
		// this.context = (Activity) context;
		oct = new Arith_OCT();
		CoordinatePaint = new Paint();
		CoordinatePaint.setColor(Color.rgb(234, 234, 234));
		LablePaint = new Paint();
		LablePaint.setColor(Color.rgb(64, 64, 64));
		xAxisText = new String[] { "20", "25", "31.5", "40", "50", "63", "80", "100", "125", "160", "200", "250", "315",
				"400", "500", "630", "800", "1k", "1.25k", "1.6k", "2k", "2.5k", "3.15k", "4k", "5k", "6.3k", "8k",
				"10k", "12.5k", "16k" };

		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);

		if (xlabelState == null)
			xlabelState = preference.getString("OCT_XAxis", "Hz");
		if (ylabelState == null)
			ylabelState = preference.getString("OCT_YAxis", "Pa");
		OCTBuffer = new ArrayList<List<Float>>();
		OCTBufferArray = new ArrayList<int[]>();
		channalCount = 8;
		for (int i = 0; i < channalCount; i++) {
			OCTBuffer.add(new ArrayList<Float>());
		}
		refreshLabelState();
		setBufferIntListIndex();
	}

	public void init() {
		super.init();
		ybaseLine = viewh - 50;
	}

	/*
	 * OCT����Ҫ�����Ŵ���С���� ��дScaleView��������
	 * 
	 */
	protected void drawYLine(Canvas canvas) {
		if (CoordinatePaint == null) {
			CoordinatePaint = new Paint();
		}
		canvas.drawLine(left, 0, left, canvas.getHeight() - 50, CoordinatePaint);
	}

	protected void drawXLine(Canvas canvas) {
		if (CoordinatePaint == null) {
			CoordinatePaint = new Paint();
		}
		canvas.drawLine(left, canvas.getHeight() - 50, canvas.getWidth(), canvas.getHeight() - 50, CoordinatePaint);
	}

	@Override
	protected void drawXLable(Canvas canvas) {
		// TODO Auto-generated method stub
		int count = xAxisText.length;
		float interdis = (vieww - 100) / count;
		if (rectWidth != 0)
			interdis = rectWidth;
		canvas.save();
		for (int i = 0; i < count; i++) {
			float pos = xbaseLine + (20 + i * interdis) * sxrate;
			if (pos > left && pos < vieww - 60) {
				canvas.drawText(xAxisText[i], pos, canvas.getHeight() - xaxisbottom, LablePaint);
			}

		}
		canvas.drawText(xlabelState, canvas.getWidth() - 25, canvas.getHeight() - xaxisbottom, LablePaint);
		canvas.restore();
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		if (context.getClass().getSimpleName().equals("MainActivity")) {
//			// δ���ϻ��˵���ѡ��ͨ���Ļ����ửͼ
//			if (AddChannelViewPager.channelCount.size() == 0)
//				return;
//
//			if (DataCollection.isRecording || DataCollection.collectionState == 1) {
//				if (DataCollection.dataCollectionIntBufferList != null
//						&& DataCollection.dataCollectionIntBufferList.size() != 0) {
//					if (DataCollection.collectionState == 0) {
//						if (!ifStartToGetData || ifEnterToDriverMode) {
//							if (!ifEnterToDriverMode)
//								changeX = 0;
//							else if (ifStartToGetData)
//								setBufferIntListIndex(); // --------驾驶|普通采集模式数据同步专用功能---------
//							ifStartToGetData = true;
//							ifEnterToDriverMode = false;
//						}
//
//						if (bufferIntListIndex < DataCollection.dataCollectionIntBufferList.size()) {
//							int[] testdata = ((ArrayList<int[]>) DataCollection.dataCollectionIntBufferList)
//									.get(bufferIntListIndex);
//							getBuffer(testdata, canvas);
//							bufferIntListIndex += 1;
//						}
//						// -----------驾驶|普通采集同步专用index----------
//						if (xclSingalTransfer == null) {
//							xclSingalTransfer = XclSingalTransfer.getInstance();
//						}
//						xclSingalTransfer.putValue("OCT_DataCollectionIntBufferListIndex", bufferIntListIndex);
//						// -----------完毕------------------------------------
//					} else if (DataCollection.collectionState == 1&&ifStartToGetData) {
//						// if (bufferIntListIndex >=
//						// DataCollection.dataCollectionIntBufferList.size()) {
//						DataCollection.collectionOverCalculateCount += 1;
//						bufferIntListIndex = 0;
//						pre_size = 0;
//						ifStartToGetData=false;
//						// } else {
//						// int[] testdata =
//						// ((ArrayList<int[]>)DataCollection.dataCollectionIntBufferList)
//						// .get(bufferIntListIndex);
//						// getBuffer(testdata, canvas);
//						// bufferIntListIndex += 1;
//						// }
//					}
//				}
//			} else {
//				try {
//					if (IAudioTrack.readState == 3) {
//						IAudioTrack.audioOverCalculateCount += 1;
//						bufferIntListIndex = 0;
//					}
//					if (IAudioTrack.readState == 0) {
//						if (bufferIntListIndex < IAudioTrack.mAudioTrackIntBufferList.size()) {
//							int[] testdata = ((ArrayList<int[]>) IAudioTrack.mAudioTrackIntBufferList)
//									.get(bufferIntListIndex);
//							getBuffer(testdata, canvas);
//							bufferIntListIndex += 1;
//						}
//					} else if (IAudioTrack.readState == 1) {
//						if (IAudioTrack.readState == 3) {
//							IAudioTrack.audioOverCalculateCount += 1;
//							bufferIntListIndex = 0;
//						}
//						if (IAudioTrack.readState == 1) {
//							if (bufferIntListIndex >= IAudioTrack.mAudioTrackIntBufferList.size()) {
//								IAudioTrack.audioOverCalculateCount += 1;
//								bufferIntListIndex = 0;
//							} else {
//								int[] testdata = ((ArrayList<int[]>) IAudioTrack.mAudioTrackIntBufferList)
//										.get(bufferIntListIndex);
//								getBuffer(testdata, canvas);
//								bufferIntListIndex += 1;
//							}
//						}
//					}
//				} catch (Exception e) {
//					System.out.println(e);
//				}
//			}
//		} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
//			ifEnterToDriverMode=true;
//			if (AddChannelViewPager.drive_channelCount.size() == 0)
//				return;
//			if (DataCollection.isRecording || DataCollection.collectionState == 1) {
//				if (DataCollection.dataCollectionIntBufferList != null
//						&& DataCollection.dataCollectionIntBufferList.size() != 0) {
//					if (DataCollection.collectionState == 0) {
//						if (bufferIntListIndex < DataCollection.dataCollectionIntBufferList.size()) {
//							int[] testdata = ((ArrayList<int[]>) DataCollection.dataCollectionIntBufferList)
//									.get(bufferIntListIndex);
//							getBuffer(testdata, canvas);
//							bufferIntListIndex += 1;
//						}
//						// -----------驾驶|普通采集同步专用index----------
//						if (xclSingalTransfer == null) {
//							xclSingalTransfer = XclSingalTransfer.getInstance();
//						}
//						xclSingalTransfer.putValue("OCT_DataCollectionIntBufferListIndex", bufferIntListIndex);
//						// -----------完毕------------------------------------
//					} else if (DataCollection.collectionState == 1) {
//						// if (bufferIntListIndex >=
//						// DataCollection.dataCollectionIntBufferList.size()) {
//						DataCollection.collectionOverCalculateCount += 1;
//						bufferIntListIndex = 0;
//						pre_size = 0;
//						// } else {
//						// int[] testdata =
//						// ((ArrayList<int[]>)DataCollection.dataCollectionIntBufferList)
//						// .get(bufferIntListIndex);
//						// getBuffer(testdata, canvas);
//						// bufferIntListIndex += 1;
//						// }
//					}
//				}
//			}
//		}
//
//		if (OCTBuffer == null || OCTBuffer.size() <= 0 || lastIndex < 0) {
//			return;
//
//		}
//		if (OCTBuffer.get(lastIndex).size() == 0)
//			return;
//
//		rectWidth = (float) ((canvas.getWidth() - 50 - OCTBuffer.get(lastIndex).size()) * 1.0
//				/ OCTBuffer.get(lastIndex).size());
//		divider = rectWidth;
//
//		getAutoRate((ArrayList<Float>) OCTBuffer.get(0), ybaseLine);
//		canvas.save();
//		canvas.clipRect(left, 0, vieww, viewh - 50);
//		for (int i = 0; i < OCTBuffer.get(lastIndex).size(); i++) {
//			int index = 0;
//			int rectSize = 0;
//			while (index < activityChannelArray.length) {
//				if (activityChannelArray[index] == 0) {
//					index++;
//					continue;
//				}
//				List<Float> lineList = OCTBuffer.get(index);
//				float rectHeight = (canvas.getHeight() - 50 - lineList.get(i));
//
//				py = (float) (ybaseLine - lineList.get(i) / ymultiple * syrate);
//				px = xbaseLine + 1 + (rectWidth * i + 10 * rectSize) * sxrate;
//				y = viewh - 50;
//				x = xbaseLine + (rectWidth * (i + 1) + 10 * rectSize) * sxrate;
//
//				if (paintList != null && paintList.size() > 0) {
//					if (index + 1 > paintList.size()) {
//						canvas.drawRect(px, py, x, y, paintList.get(0));
//					} else {
//						canvas.drawRect(px, py, x, y, paintList.get(index));
//					}
//				}
//				index++;
//				rectSize++;
//			}
//		}
//		canvas.restore();

	}

	/*
	 * protected void drawYLable(Canvas canvas) { canvas.drawText("111", 10, 10,
	 * LablePaint); }
	 */
	/*
	 * public void drawXLable(Canvas canvas) { if (OCTBuffer.size() == 0)
	 * return; for(int i = 0; i < xAxisText.length; i ++) {
	 * canvas.drawText(xAxisText[i], 51 + rectWidth * (i + 0.25f),
	 * canvas.getHeight() - 20, LablePaint); } }
	 */
	/*
	 * protected void drawYLine(Canvas canvas){ canvas.drawLine( left , 0, left
	 * , canvas.getHeight() - 50, CoordinatePaint);
	 * 
	 * }
	 */
	/*
	 * protected void drawXLine(Canvas canvas){ canvas.drawLine( left ,
	 * canvas.getHeight() - 50, canvas.getWidth() , canvas.getHeight() - 50,
	 * CoordinatePaint);
	 * 
	 * }
	 */
	protected void getDataResult(int channelNum) {
		if (channelNum > OCTBuffer.size())
			return;
		if (OCTBuffer != null && channelNum != -1 && OCTBuffer.get(channelNum).size() > 0) {
			resultDatalist = (ArrayList) OCTBuffer.get(channelNum);
		}
	}

	private void getAutoRate(ArrayList<Float> list, float ybase) {

		if (list.size() == 0)
			return;
		maxValue = list.get(0) / ymultiple;

		int i = 0;
		int length = list.size();
		while (i < length) {
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

	private void getBuffer(int[] Buffer, Canvas canvas) {
//		if (Buffer == null)
//			return;
//		switch (hardType) {
//		case 0:// Ӳ��ģʽ
//
//			int xsize = 0;
//			for (int i = 0; i < Buffer.length / channalCount; i++) {
//				int count = 0;
//				while (count < checkedChannelIndexArray.length) {
//					if (checkedChannelIndexArray[count] == 0) {
//						if (OCTBufferArray.size() < count + 1) {
//							OCTBufferArray.add(new int[1]);
//						}
//						count++;
//						continue;
//					} else {
//						lastIndex = count;
//					}
//					int bufferindex = i * channalCount + count;
//					int bufferData = Buffer[bufferindex];
//					if (OCTBufferArray.size() < count + 1) {
//						OCTBufferArray.add(new int[Buffer.length / channalCount]);
//					}
//					OCTBufferArray.get(count)[xsize] = bufferData;
//					count++;
//				}
//				xsize++;
//			}
//			int index = 0;
//			while (index < checkedChannelIndexArray.length) {
//				if (checkedChannelIndexArray[index] == 0) {
//					if (OCTResultList.size() < index + 1) {
//						OCTResultList.add(new float[1]);
//					}
//					index++;
//					continue;
//				}
//				int[] dataArray = OCTBufferArray.get(index);
//				oct.calculate(dataArray, dataArray.length);
//				int size = oct.GetResultInfo();
//				if (size > 0) {
//					float[] OCTResult = oct.GetResult(size - 1);
//					if (OCTResultList.size() < index + 1) {
//						OCTResultList.add(OCTResult);
//					} else {
//						OCTResultList.set(index, OCTResult);
//					}
//				}
//				index++;
//			}
//			index = 0;
//			for (int j = 0; j < OCTResultList.get(lastIndex).length; j++) {
//				index = 0;
//				while (index < checkedChannelIndexArray.length) {
//					if (checkedChannelIndexArray[index] == 0) {
//						index++;
//						continue;
//					}
//					List<Float> lineList = OCTBuffer.get(index);
//					float[] lineArray = OCTResultList.get(index);
//					if (lineList.size() <= j + 1) {
//						lineList.add(lineArray[j]);
//
//						// switch (ylabelState) {
//						// case "Pa":
//						// double data_Pa = (2 * p0)
//						// * (Math.pow(10, (OCTResult[j] / 20)));
//						// OCTBuffer.add((float) data_Pa);
//						// break;
//						// default:
//						// OCTBuffer.add(OCTResult[j]);
//						// break;
//						// }
//					} else {
//						lineList.set(j, lineArray[j]);
//					}
//					index++;
//				}
//			}
//			break;
//		case 1:// �ֻ�ģʽ
//			lastIndex = 0;
//			List<Float> linelist = OCTBuffer.get(0);
//			oct.calculate(Buffer, Buffer.length);
//			int size = oct.GetResultInfo();
//			if (size > 0) {
//				for (int i = pre_size; i < size; i++) {
//					float[] OCTResult = oct.GetResult(i);
//					if (ylabelState != null) {
//						///////// ���� SDK 1.6 -begin
//						if ("dBA".equals(ylabelState)) {
//							oct.setWeight(0, OCTResult, 1);
//						} else if ("dBC".equals(ylabelState)) {
//							oct.setWeight(0, OCTResult, 3);
//						}
//						/////////// - end
//						// switch (ylabelState) {
//						// case "dBA":
//						// oct.setWeight(0, OCTResult, 1);
//						// break;
//						// case "dBC":
//						// oct.setWeight(0, OCTResult, 3);
//						// break;
//						// }
//					}
//					if (OCTResult.length != 0) {
//						linelist.clear();
//						for (int j = 0; j < OCTResult.length; j++) {
//							if (ylabelState != null) {
//
//								///////// ���� SDK 1.6 -begin
//								if ("Pa".equals(ylabelState)) {
//									double data_Pa = p0 * Math.pow(10, OCTResult[j] / 20);
//									linelist.add((float) data_Pa);
//								} else {
//									linelist.add(OCTResult[j]);
//								}
//							} else {
//								linelist.add(OCTResult[j]);
//							}
//						}
//					}
//				}
//			}
//			pre_size = size;
//			break;
//		}

	}

	public List<Float> getCursorValue(float moveX) {

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

		int count = xAxisText.length;
		float interdis = (vieww - 100) / count;
		if (rectWidth != 0)
			interdis = rectWidth;

		int num = (int) ((moveX - xbaseLine + left) / (interdis * sxrate));

		if (num < xAxisText.length && num > -1) {
			String str = xAxisText[num];
			if (str.contains("k")) {
				xValue = Float.parseFloat(str.replace("k", ""));
				xValue = xValue * 1000;
			} else
				xValue = Float.parseFloat(str);
		}

		if (num < resultDatalist.size() && num >= 0) {
			yValue = (Float) resultDatalist.get(num);
		} else if (num >= resultDatalist.size()) {
			yValue = (Float) resultDatalist.get(resultDatalist.size() - 1);
		} else {
			yValue = (Float) resultDatalist.get(0);

		}
		yPosition = (ybaseLine - yValue * syrate);
		if (yPosition < 0)
			yPosition = 50;
		else if (yPosition > viewh - 50)
			yPosition = viewh - 50;
		list.add(xValue);
		list.add(yValue);
		list.add(yPosition);
		return list;
	}

	@Override
	public void setYLableState(String ylabelState) {
		// TODO Auto-generated method stub
		super.setYLableState(ylabelState);
	}

	@Override
	protected void yDataChange() {
		// TODO Auto-generated method stub
		if (YUNIT_SWITCH_MODE == 0)
			return;
		else if (YUNIT_SWITCH_MODE == 2) { // pa �л��� db
			int listCount = OCTBuffer.size();
			ArrayList<List<Float>> lists = new ArrayList<List<Float>>();
			for (int i = 0; i < listCount; i++) {
				List<Float> list = new ArrayList<Float>();

				Iterator<Float> it = OCTBuffer.get(i).iterator();
				while (it.hasNext()) {
					float data = it.next();

					data = (float) (p0 * (Math.pow(10, (data / 20))));

					list.add(data);
				}
				lists.add(list);
			}

			OCTBuffer.clear();
			OCTBuffer = lists;
		} else { // db �л��� pa

			int listCount = OCTBuffer.size();
			ArrayList<List<Float>> lists = new ArrayList<List<Float>>();
			for (int i = 0; i < listCount; i++) {
				List<Float> list = new ArrayList<Float>();

				Iterator<Float> it = OCTBuffer.get(i).iterator();
				while (it.hasNext()) {
					float data = it.next();

					data = (float) (20 * (Math.log10(data / p0)));

					list.add(data);
				}
				lists.add(list);
			}

			OCTBuffer.clear();
			OCTBuffer = lists;
		}

	}

	// -----------驾驶|普通采集同步专用index----------
	public void setBufferIntListIndex() {
		if (xclSingalTransfer == null) {
			xclSingalTransfer = XclSingalTransfer.getInstance();
		}
		bufferIntListIndex = xclSingalTransfer.containsKey("OCT_DataCollectionIntBufferListIndex")
				? (Integer) xclSingalTransfer.getValue("OCT_DataCollectionIntBufferListIndex") : 0;
	}

	@Override
	public ArrayList<List<Float>> getResultBuffer() {
		// TODO Auto-generated method stub
		return null;
	}
}
