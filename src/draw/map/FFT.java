package draw.map;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import left.drawer.AcquisitionFragment;
import left.drawer.AnalogFragment;
import left.drawer.FftFragment;
import mode.calibration.CalibrationDataCollection;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import bottom.drawer.AddChannelViewPager;

import com.calculate.Arith_FFT;
import com.example.mobileacquisition.MainActivity;
import common.DataCollection;
import common.IAudioTrack;
import common.MyViewPager;
import common.ScaleView;
import common.XclSingalTransfer;

public class FFT extends ScaleView {
	public final static int MODE_NORMAL = 0;
	public final static int MODE_DRIVE = 1;
	public final static int MODE_CALIBRATION = 2;
	// private Ruler ruler;
	private boolean calibration = false;
	private float freqRang = 0f;
	private float overlap = 0.5f;
	private int windowType = 0;
	private int mean = 3;
	private int xPointCount;

	private float perUnit = 1;
	private float x = 0;
	private float px = 0;
	private float maxValue;
	private ArrayList<List<Float>> FFTBuffer;
	private ArrayList<int[]> FFTBufferArray;
	private List<float[]> fftresultList = new ArrayList<float[]>();
	private float py = 0;
	private float y = 0;
	private int[] voiceData;
	private Arith_FFT fft;

	private List<Float> peakValueList = new ArrayList();
	private float[] maxArray = new float[2];
	private Float max;
	private int firstCalculation = 0;
	private Handler fftPeakVauleHandler;
	// private Activity context;//原为 MainActivity
	// 锟斤拷锟酵ｏ拷锟睫革拷为Acitivity锟斤拷锟酵ｏ拷锟斤拷为锟疥定锟斤拷锟斤拷也锟斤拷要锟矫达拷锟姐法锟斤拷锟斤拷锟皆诧拷应锟矫固讹拷为Main锟斤拷Activity
	private double freq = 44100;
	private int xlabelSer;
	private int j;
	private boolean startToFindZero = true;
	private int lastIndex = -1;
	private int count = 0;

	private ArrayList<float[]> FFTCalculate = new ArrayList<float[]>();

	private int channalCount;
	private int pre_size = 0;
	private int bufferIntListIndex = 0;
	private XclSingalTransfer xclSingalTransfer;
	private boolean ifStartToGetData=false;
	private boolean ifEnterToDriverMode=false;
	public FFT(Context context) {

		super(context);

		if(fft == null)fft = new Arith_FFT();
		setArithFFT();
		
		channalCount = 8;
		FFTBuffer = new ArrayList<List<Float>>();
		FFTBufferArray = new ArrayList<int[]>();
		for (int i = 0; i < channalCount; i++) {
			FFTBuffer.add(new ArrayList<Float>());
		}
	}
	
	public FFT(Context context,int mode) {
		super(context);
		// this.context = (FragmentActivity) context;
		// setFFTParamHandler((Activity) context);
		
		if(fft == null)fft = new Arith_FFT();
		if(mode == MODE_CALIBRATION) this.calibration = true;
		setArithFFT();
		
		channalCount = 8;
		FFTBuffer = new ArrayList<List<Float>>();
		FFTBufferArray = new ArrayList<int[]>();
		for (int i = 0; i < channalCount; i++) {
			FFTBuffer.add(new ArrayList<Float>());
		}
		setBufferIntListIndex();
	}

	public FFT(Context context, MyViewPager viewpager) {
		super(context, viewpager);
		if(fft == null)fft = new Arith_FFT();
		setArithFFT();
		
		channalCount = 8;
		FFTBuffer = new ArrayList<List<Float>>();
		FFTBufferArray = new ArrayList<int[]>();
		for (int i = 0; i < channalCount; i++) {
			FFTBuffer.add(new ArrayList<Float>());
		}
		setBufferIntListIndex();
	}

	protected void init() {
		super.init();
		
		ybaseLine = viewh - 50;
		ymultiple = 0.01f;
		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);

		if(xlabelState ==null) xlabelState = preference.getString("FFT_XAxis", "Hz");
		if (!calibration&&(ylabelState ==null)) {
			ylabelState = preference.getString("FFT_YAxis", "Pa");
		}
		refreshLabelState();
		
		if (freqRang == 0 && !calibration) {
			freqRang = Float.parseFloat(preference.getString("FFT_FreqRange", "0.4")) * 1000;
		}
		this.setFreqRang(freqRang);
	}

	public void setArithFFT() {

		SharedPreferences preference = context.getSharedPreferences("hz_5D", 0);

		acquiFreq = Integer.parseInt(preference.getString("AcquiFreq_spinner_values", "12800"));
		acquiFreq = 48000;
		
		
		if (calibration) {
			
			Paint linePaint = new Paint();
			linePaint.setStrokeWidth((float) 5.0); //锟斤拷锟斤拷锟竭匡拷  
			linePaint.setColor(Color.rgb(255, 255, 0));
			paintList.add(linePaint);
			
			overlap = 0.5f;
			freqRes = 11.71875f;
			windowType = 0;
			mean = 3;

			fft.SetWindowType(windowType);
			BigDecimal winlen_bigDecimal = new BigDecimal(acquiFreq / freqRes).setScale(0, BigDecimal.ROUND_HALF_UP);
			int winlen = winlen_bigDecimal.intValue();
			BigDecimal winshift_bigDecimal = new BigDecimal(winlen * (1 - overlap) + 0.5).setScale(0,
					BigDecimal.ROUND_HALF_UP);
			int winshift = winshift_bigDecimal.intValue();
			fft.SetWinLen(winlen);
			fft.SetWinShift(winshift);
		}else{
			if (freqRang == 0 && !calibration) {
				freqRang = Float.parseFloat(preference.getString("FFT_FreqRange", "0.4")) * 1000;
			}
			this.setFreqRang(freqRang);
			// acquiFreq = acquiFragment.getAcquiFreq();
			overlap = Float.parseFloat(preference.getString("FFT_Overlap", "50")) / 100;
			freqRes = Float.parseFloat(preference.getString("FFT_FreqRes", "0.78"));
			windowType = Integer.parseInt(preference.getString("FFT_Window_Position", "0"));
			mean = Integer.parseInt(preference.getString("FFT_Averaging", "3"));

			fft.SetWindowType(windowType);
			BigDecimal winlen_bigDecimal = new BigDecimal(acquiFreq / freqRes).setScale(0, BigDecimal.ROUND_HALF_UP);
			int winlen = winlen_bigDecimal.intValue();
			BigDecimal winshift_bigDecimal = new BigDecimal(winlen * (1 - overlap) + 0.5).setScale(0,
					BigDecimal.ROUND_HALF_UP);
			int winshift = winshift_bigDecimal.intValue();
			fft.SetWinLen(winlen);
			fft.SetWinShift(winshift);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

//		super.onDraw(canvas);
//		if (context.getClass().getSimpleName().equals("MainActivity")) {
//			if (fftPeakVauleHandler == null && AddChannelViewPager.channelCount.size() == 0)
//				return;
//
//			if (DataCollection.isRecording || DataCollection.collectionState == 1) {
//				if (DataCollection.dataCollectionIntBufferList != null
//						&& DataCollection.dataCollectionIntBufferList.size() != 0) {
//					if (DataCollection.collectionState == 0) {
//						if (!ifStartToGetData||ifEnterToDriverMode) {
//								if(!ifEnterToDriverMode) changeX = 0;
//								else
//								if(ifStartToGetData) 	setBufferIntListIndex();	//--------驾驶|普通采集模式数据同步专用功能---------
//							ifStartToGetData = true;
//							ifEnterToDriverMode=false;
//						}						
//						
//						if (bufferIntListIndex < DataCollection.dataCollectionIntBufferList.size()) {
//							int[] testdata = ((ArrayList<int[]>) DataCollection.dataCollectionIntBufferList)
//									.get(bufferIntListIndex);
//							getBuffer(testdata, canvas);
//							bufferIntListIndex += 1;
//						}
//						//-----------驾驶|普通采集同步专用index----------
//						if(xclSingalTransfer==null){
//							xclSingalTransfer=XclSingalTransfer.getInstance();
//						}
//						xclSingalTransfer.putValue("FFT_DataCollectionIntBufferListIndex", bufferIntListIndex);
//						//-----------完毕------------------------------------
//					} else if (DataCollection.collectionState == 1&&ifStartToGetData) {
////						if (bufferIntListIndex >= DataCollection.dataCollectionIntBufferList.size()) {
//							DataCollection.collectionOverCalculateCount += 1;
//							bufferIntListIndex = 0;
//							ifStartToGetData=false;
//							fft.ResetResult();
//							fft.ResetMeanResult();
//							fft.ResetSignal();
//							pre_size = 0;
//							FFTCalculate.clear();
////						} else {
////							int[] testdata = ((ArrayList<int[]>)DataCollection.dataCollectionIntBufferList)
////									.get(bufferIntListIndex);
////							getBuffer(testdata, canvas);
////							bufferIntListIndex += 1;
////						}
//					}
//				}
//			} else {
//					if (IAudioTrack.readState == 3) {
//						IAudioTrack.audioOverCalculateCount += 1;
//						bufferIntListIndex = 0;
//						fft.ResetResult();
//						fft.ResetMeanResult();
//						fft.ResetSignal();
//						pre_size = 0;
//						FFTCalculate.clear();
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
//							fft.ResetResult();
//							fft.ResetMeanResult();
//							fft.ResetSignal();
//							pre_size = 0;
//							FFTCalculate.clear();
//						}
//						if (IAudioTrack.readState == 1) {
//							if (bufferIntListIndex >= IAudioTrack.mAudioTrackIntBufferList.size()) {
//								IAudioTrack.audioOverCalculateCount += 1;
//								bufferIntListIndex = 0;
//								fft.ResetResult();
//								fft.ResetMeanResult();
//								fft.ResetSignal();
//								pre_size = 0;
//								FFTCalculate.clear();
//							} else {
//								int[] testdata = ((ArrayList<int[]>) IAudioTrack.mAudioTrackIntBufferList)
//										.get(bufferIntListIndex);
//								getBuffer(testdata, canvas);
//								bufferIntListIndex += 1;
//							}
//						}
//					}
//			}
//		} else if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
//			ifEnterToDriverMode=true;
//			if (fftPeakVauleHandler == null && AddChannelViewPager.drive_channelCount.size() == 0)
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
//						//-----------驾驶|普通采集同步专用index----------
//						if(xclSingalTransfer==null){
//							xclSingalTransfer=XclSingalTransfer.getInstance();
//						}
//						xclSingalTransfer.putValue("FFT_DataCollectionIntBufferListIndex", bufferIntListIndex);
//						//-----------完毕------------------------------------
//					} else if (DataCollection.collectionState == 1) {
////						if (bufferIntListIndex >= DataCollection.dataCollectionIntBufferList.size()) {
//							DataCollection.collectionOverCalculateCount += 1;
//							bufferIntListIndex = 0;
//							fft.ResetResult();
//							fft.ResetMeanResult();
//							fft.ResetSignal();
//							pre_size = 0;
//							FFTCalculate.clear();
////						} else {
////							int[] testdata = ((ArrayList<int[]>)DataCollection.dataCollectionIntBufferList)
////									.get(bufferIntListIndex);
////							getBuffer(testdata, canvas);
////							bufferIntListIndex += 1;
////						}
//					}
//				}
//			}
//		} else if (context.getClass().getSimpleName().equals("CalibrationActivity")) {
//				getBuffer(CalibrationDataCollection.buffer, canvas);
//				/*
//				 * 没有激活上滑菜单的通道，没有画图例，所以paintList为空。暂时把通道1的颜色值添加到paintList
//				 */
//				if(paintList.size()==0){
//					Paint linePaint = new Paint();
//					linePaint.setStrokeWidth((float) 5.0);   
//					linePaint.setColor(Color.rgb(238, 118, 0));
//					paintList.add(linePaint);
//				}
//		}
//		if (FFTBuffer == null || FFTBuffer.size() <= 0 || lastIndex < 0) {
//			return;
//		}
//		getAutoRate((ArrayList) FFTBuffer.get(0), ybaseLine);
////		int xPointCount = (int)(freqRang/freqRes);
////		if(xPointCount > FFTBuffer.get(lastIndex).size()) xPointCount = FFTBuffer.get(lastIndex).size();
//		for (int i = 0; i < xPointCount-1; i++) {
//			int index = 0;
//			while (index < activityChannelArray.length) {
//				if (activityChannelArray[index] == 0) {
//					index++;
//					continue;
//				}
//				List<Float> dataList = FFTBuffer.get(index);
//				divider = (float) ((vieww - 50) / (freqRang / freqRes - 1));
//				canvas.save();
//				canvas.clipRect(left, 0, vieww, viewh - 50);
//				if (dataList.size() > 0) {
//					// ------------��ȡ��-------------
//					py = ybaseLine - dataList.get(i) / ymultiple * syrate;
//					px = xbaseLine + i * divider * sxrate;
//					y = ybaseLine - dataList.get(i + 1) / ymultiple * syrate;
//					x = px + sxrate * divider;
//					// -------------�궨��ȡ��ֵ����------
//					if (fftPeakVauleHandler != null) {
//						if (firstCalculation < 2) {
//							if (i <= xPointCount - 2) {
//								if (i > 0 && i < dataList.size() - 1) {
//									if (dataList.get(i) > dataList.get(i - 1)
//											&& dataList.get(i) < dataList.get(i + 1)) {
//										peakValueList.add(dataList.get(i));
//									}
//								}
//								if (i == xPointCount - 2) {
//									if (peakValueList == null || peakValueList.size() == 0) {
//										return;
//									}
//									max = Collections.max(peakValueList);
//									// maxIndex=FFTBuffer.indexOf(max);
//									maxArray[firstCalculation] = max;
//									peakValueList.clear();
//									firstCalculation++;
//								}
//							}
//						}
//					}
//					// -------------��������-----------
//					if (paintList != null && paintList.size() > 0) {
//						if (index + 1 > paintList.size()) {
//							canvas.drawLine(px, py, x, y, paintList.get(0));
//						} else {
//							canvas.drawLine(px, py, x, y, paintList.get(index));
//						}
//					}
//
//				}
//				// ----------------�궨�ж������ȶ�״̬---------------
//				if (fftPeakVauleHandler != null) {
//					if (firstCalculation == 2) {
//						float sum = 0;
//						for (int j = 0; j < FFTBuffer.size(); j++) {
//							sum = sum + (maxArray[0] - maxArray[1]) * (maxArray[0] - maxArray[1]);
//						}
//						if (sum < 1.0) {// ���뼴Ϊ����ƽ��״̬
//							double realPeakValue = maxArray[1];
//							Message realPeakValueMessage = fftPeakVauleHandler.obtainMessage();
//							realPeakValueMessage.obj = realPeakValue;
//							fftPeakVauleHandler.sendMessage(realPeakValueMessage);
//						} else {
//							firstCalculation = 0;
//						}
//					}
//				}
//				// --------------���-----------------------
//				index++;
//				canvas.restore();
//			}
//		}

	}

	public void getDataResult(int channelNum) {
		if (channelNum > FFTBuffer.size())
			return;
		if (FFTBuffer != null && channelNum != -1 && FFTBuffer.get(channelNum).size() > 0) {
			resultDatalist = (ArrayList) FFTBuffer.get(channelNum);
		}
	}

	private void getAutoRate(ArrayList<Float> list, float ybase) {
		if (list.size() == 0)
			return;
		maxValue = list.get(0) / ymultiple;
		if(xPointCount>list.size()) xPointCount =list.size();
		int i = 0;
		while (i < xPointCount) {
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

	private void getBuffer(int[] Buffer, Canvas canvas) {
//		if (Buffer == null)
//			return;
//		switch (hardType) {
//		case 0:// Ӳ��ģʽ
//			int xsize = 0;
//			for (int i = 0; i < Buffer.length / channalCount; i++) {
//				count = 0;
//				int data = 0;
//				int bufferData = 0;
//				while (count < checkedChannelIndexArray.length) {
//					if (checkedChannelIndexArray[count] == 0) {
//						if (FFTBufferArray.size() < count + 1) {
//							FFTBufferArray.add(new int[1]);
//						}
//						count++;
//						continue;
//					} else {
//						lastIndex = count;
//					}
//					int bufferindex = i * channalCount + count;
//					bufferData = Buffer[bufferindex];
//					if (FFTBufferArray.size() < count + 1) {
//						FFTBufferArray.add(new int[Buffer.length / channalCount]);
//					}
//					FFTBufferArray.get(count)[xsize] = bufferData;
//					count++;
//				}
//				xsize++;
//			}
//			int index = 0;
//			while (index < checkedChannelIndexArray.length) {
//				if (checkedChannelIndexArray[index] == 0) {
//					if (fftresultList.size() < index + 1) {
//						fftresultList.add(new float[1]);
//					}
//					index++;
//					continue;
//				}
//				int[] dataArray = FFTBufferArray.get(index);
//				fft.ResetMeanResult();
//				fft.Int_calculate(dataArray, dataArray.length);
//				int size = fft.GetResultInfo(0);
//				if (size > 0) {
//					float[] FFTResult = fft.GetMeanResult();
//					if (fftresultList.size() < index + 1) {
//						fftresultList.add(FFTResult);
//					} else {
//						fftresultList.set(index, FFTResult);
//					}
//				}
//				index++;
//			}
//			for (int i = 0; i < fftresultList.get(lastIndex).length - 1; i++) {
//				index = 0;
//				while (index < checkedChannelIndexArray.length) {
//					if (checkedChannelIndexArray[index] == 0) {
//						index++;
//						continue;
//					}
//					List<Float> lineList = FFTBuffer.get(index);
//					float[] lineArray = fftresultList.get(index);
//					float data = lineArray[i];
//					if (ylabelState != null) {
//
//						///////// ���� SDK 1.6 -begin
//						if ("dBA".equals(ylabelState)) {
//							fft.setWeight(1, lineArray, 48000);
//						} else if ("dBC".equals(ylabelState)) {
//							fft.setWeight(3, lineArray, 48000);
//						} else if ("Pa".equals(ylabelState)) {
//							// //////////// 16.3.8 ��ά�� -begin
//							// TODO ��� ͨ�����ã����������˵�ѡ��0 = p0��1 = a0����Ӧ�İ������2*p0����
//							// ����֤
//							AnalogFragment.ViewHolder holder = AnalogFragment.checkedViewMap.get(index);
//							String physical = holder.Physical.getSelectedItem().toString();
//
//							if ("Noise".equals(physical))
//								data = (float) (p0 * Math.pow(10,data/20));
//							else if ("Vibration".equals(physical))
//								data = (float) ((2 * a0) * (Math.pow(10, (data / 20))));
//						}
//
//					}
//					if (lineList.size() <= i + 1) {
//						lineList.add(data);
//					} else {
//						lineList.set(i, data);
//					}
//					index++;
//				}
//			}
//			break;
//		case 1:// �ֻ�ģʽ
//			lastIndex = 0;
//			List<Float> lineData = FFTBuffer.get(0);
//			// fft.ResetMeanResult();
//			fft.Int_calculate(Buffer, Buffer.length);
//			int size = fft.GetResultInfo(0);
//			if (size > 0) {
//				for (int i = pre_size; i < size; i++) {
//					float[] FFTResult = fft.GetResult(i);
//					// ----------����ƽ�����-------
//					if (mean > 0) {
//						FFTCalculate.add(FFTResult);
//					}
//					if (FFTCalculate.size() >= mean) {
//						// -----ƽ�����---
//						if (FFTCalculate.size() > 0) {
//							for (int j = 0; j < FFTCalculate.get(0).length; j++) {
//								float temp = 0;
//								for (int k = 0; k < mean; k++) {
//									temp += Math.pow(10, FFTCalculate.get(k)[j] / 10);
//								}
//								temp /= mean;
//								FFTResult[j] = (float) (10 * Math.log10(temp));
//							}
//							// --------���---
//							FFTCalculate.clear();
//						}
//					}
//					// ----------���----------
//					if (FFTResult != null && FFTResult.length > 0) {
//						lineData.clear();
//						setWeight(FFTResult);
//
//						for (int j = 0; j < FFTResult.length; j++) {
//							lineData.add(FFTResult[j]);
//
//						}
//					}
//				}
//				pre_size = size;
//			}
//			break;
//		}
	}

	public void setWeight(float[] FFTResult) {
		if (ylabelState.equals("dBA"))
			fft.setWeight(1, FFTResult, 48000);
		if (ylabelState.equals("dBC"))
			fft.setWeight(3, FFTResult, 48000);
		if (ylabelState.equals("Pa")) {
			for (int i = 0; i < FFTResult.length; i++) {
				FFTResult[i] = (float) ((2 * p0) * (Math.pow(10, (FFTResult[i] / 20))));
			}
		}
	}

	public Arith_FFT getArith_FFT() {
		return fft;
	}

	public void setFFTPeakVauleHandler(Handler fftPeakVauleHandler) {
		this.fftPeakVauleHandler = fftPeakVauleHandler;
	}

	public Handler getFFTPeakVauleHandler() {
		return fftPeakVauleHandler;
	}

	public void setWindowType(int windowType) {

		this.windowType = windowType;
		fft.SetWindowType(windowType);
		this.invalidate();
	}

	public void setMean(int mean) {

		this.mean = mean;

		this.invalidate();
	}

	public void setFreqRes(float freqRes) {
		xPointCount = (int) (freqRang/freqRes);
		this.freqRes = freqRes;
		BigDecimal winlen_bigDecimal = new BigDecimal(acquiFreq / freqRes).setScale(0, BigDecimal.ROUND_HALF_UP);
		int winlen = winlen_bigDecimal.intValue();
		BigDecimal winshift_bigDecimal = new BigDecimal(winlen * (1 - overlap) + 0.5).setScale(0,
				BigDecimal.ROUND_HALF_UP);
		int winshift = winshift_bigDecimal.intValue();
		fft.SetWinLen(winlen);
		fft.SetWinShift(winshift);
		this.invalidate();
	}

	public void setOverlap(float overlap) {
		this.overlap = overlap;
		this.invalidate();
	}

	//
	public void setAcquiFreq(double d) {
		// TODO Auto-generated method stub

		// this.acquiFreq = (int) d;
		acquiFreq = 48000;
		BigDecimal winlen_bigDecimal = new BigDecimal(acquiFreq / freqRes).setScale(0, BigDecimal.ROUND_HALF_UP);
		int winlen = winlen_bigDecimal.intValue();
		BigDecimal winshift_bigDecimal = new BigDecimal(winlen * (1 - overlap) + 0.5).setScale(0,
				BigDecimal.ROUND_HALF_UP);
		int winshift = winshift_bigDecimal.intValue();
		fft.SetWinLen(winlen);
		fft.SetWinShift(winshift);
		// fft.SetWinShift((int)(acquiFreq / freqRes * (1 - overlap)));
		this.invalidate();
	}

	public void setFreqRang(float freqRang) {

		// TODO Auto-generated method stub
		this.freqRang = freqRang;
		if(freqRang>24000) freqRang = 24000;
		xPointCount = (int) (freqRang/freqRes);
		if (freqRang > (vieww - 50)) {
			xmultiple = (float) (freqRang / (vieww - 50));
			float xgrid = 100 / xmultiple;
			int n = 0;
			while (xmultiple / (int) Math.pow(2, n) >= 1) {
				n += 1;
			}
			xmultiple = (int) Math.pow(2, n);
			xGrid = xmultiple * xgrid;
			xmultiple = (float) (freqRang / (vieww - 50));
		} else {
			xmultiple = (float) ((vieww - 50) / freqRang);
			float xgrid = 100 * xmultiple;
			int n = 0;
			while (xmultiple / (int) Math.pow(2, n) > 2) {
				n += 1;
			}
			xmultiple = (int) Math.pow(2, n);

			xGrid = xgrid / xmultiple;
			xmultiple = (float) (freqRang / (vieww - 50));
		}
		this.invalidate();
	}

	public void setCalibration(boolean value) {
		this.calibration = value;
	}
	
	@Override
	protected void yDataChange() {
		// TODO Auto-generated method stub
		if(YUNIT_SWITCH_MODE == 0) return;	
		else if(YUNIT_SWITCH_MODE == 2){   	  // db 锟叫伙拷锟斤拷   pa
			int listCount = FFTBuffer.size();
			ArrayList<List<Float>> lists = new ArrayList<List<Float>>();
			for(int i =0 ; i< listCount ;i++){
				List<Float> list= new ArrayList<Float>();
				
				Iterator<Float> it = FFTBuffer.get(i).iterator();
				while(it.hasNext()){
					float data = it.next();
					
					data=(float) (p0*(Math.pow(10, (data/ 20))));
					
					list.add(data);
				}
				lists.add(list);
			}
			
			FFTBuffer.clear();
			FFTBuffer = lists;

		}else {					// db 锟叫伙拷锟斤拷     db
			int listCount = FFTBuffer.size();
			ArrayList<List<Float>> lists = new ArrayList<List<Float>>();
			for(int i =0 ; i< listCount ;i++){
				List<Float> list= new ArrayList<Float>();
				
				Iterator<Float> it = FFTBuffer.get(i).iterator();
				while(it.hasNext()){
					float data = it.next();
									  
					data=(float) (20*(Math.log10(data/p0))); 
					
					list.add(data);
				}
				lists.add(list);
			}
			
			FFTBuffer.clear();
			FFTBuffer = lists;
		}
		
	}
	//-----------驾驶|普通采集同步专用index----------
	public void setBufferIntListIndex(){
		if(xclSingalTransfer==null){
			xclSingalTransfer=XclSingalTransfer.getInstance();
		}
		bufferIntListIndex=xclSingalTransfer.containsKey("FFT_DataCollectionIntBufferListIndex")?(Integer) xclSingalTransfer.getValue("FFT_DataCollectionIntBufferListIndex"):0;
	}


	@Override
	public ArrayList<List<Float>> getResultBuffer() {
		// TODO Auto-generated method stub
		return null;
	}
}
