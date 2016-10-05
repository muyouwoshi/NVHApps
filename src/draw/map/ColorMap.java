package draw.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.calculate.Arith_FFT;
import com.calculate.Arith_RPM;
import common.MyViewPager;
import common.ScaleView;
import common.XclSingalTransfer;

public class ColorMap extends ScaleView {

	private float freqRang = 312.5f;
	private float overlap = 0.5f;
	private int windowType = 0;
	private int mean = 3;
	private int yPointCount;
	private int acquiFreq;
	private ArrayList<float[]> ColorMapBuf;
	private Arith_FFT fft;
	private LinearGradient lg;
	private Paint p;
	private int[] colors;
	private int[] voiceData;
	private int[] Palette;
	private String zlabelState;

	// private Arith_RPM rpm;
	// private int skipcount=0;//����ʵת��ͨ�����ֺ�ɾ��
	// private float[] rpmRange;//�л�Ϊrpm��ĺ���
	// private float[] rpmData;//�л�Ϊrpm�������
	// private Activity context;
	private int xlabelSer;
	private int maxData;
	private int index = -1;
	private boolean startToFindZero = true;
	private int j;
	private Arith_RPM rpm;
	private int preSize = 0;
	private float windowShiftTime;
	private  boolean ifStartToGetData = false;
	private static boolean ifEnterToDriverMode=false;
	private  int bufferIntListIndex;
	private SharedPreferences preference;
	private XclSingalTransfer xclSingalTransfer;
	public ColorMap(Context context) {
		super(context);
	}

	public ColorMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		if(fft== null)fft = new Arith_FFT();
		setArithFFT();
		ColorMapBuf = new ArrayList<float[]>();

		preference = context.getSharedPreferences("hz_5D", 0);
		xlabelState = preference.getString("ColorMap_XAxis", "s");
		ylabelState = preference.getString("ColorMap_YAxis", "Pa");
		zlabelState=preference.getString("ColorMap_FreqWeight","dB");
		
		refreshLabelState();
		setBufferIntListIndex();
	}

	public ColorMap(Context context, MyViewPager viewPager) {
		super(context, viewPager);
		if(fft== null)fft = new Arith_FFT();
		setArithFFT();
		ColorMapBuf = new ArrayList<float[]>();
		preference = context.getSharedPreferences("hz_5D", 0);
		xlabelState = preference.getString("ColorMap_XAxis", "s");
		ylabelState = preference.getString("ColorMap_YAxis", "Pa");
		zlabelState=preference.getString("ColorMap_FreqWeight","dB");
		
		refreshLabelState();
		setBufferIntListIndex();
	}

	public ColorMap(Context context, MyViewPager viewPager, AttributeSet attrs) {
		super(context, viewPager, attrs);
		if(fft== null)fft = new Arith_FFT();
		
		setArithFFT();
		ColorMapBuf = new ArrayList<float[]>();

		preference = context.getSharedPreferences("hz_5D", 0);
		xlabelState = preference.getString("ColorMap_XAxis", "s");
		ylabelState = preference.getString("ColorMap_YAxis", "Pa");
		zlabelState=preference.getString("ColorMap_FreqWeight","dB");
		
		refreshLabelState();

	}

	protected void init() {
		super.init();
		

		ybaseLine = viewh - 50;
		preference = context.getSharedPreferences("hz_5D", 0);
		freqRang = Float.parseFloat(preference.getString("colorMapFreqRange", "0.4"))*1000;
		setFreqRang(freqRang);
		if(fft!=null){
			Palette = fft.GetPalette(1);
			p = new Paint();

			lg = new LinearGradient(0, 0, 0, viewh - 100, Palette, null,
					Shader.TileMode.CLAMP);
			p.setShader(lg);
		}
		
//		
//		
//		
//		setArithFFT();
//		refreshLabelState();
	}
	
	public void setArithFFT(){
		
		SharedPreferences preference = context.getSharedPreferences(
				"hz_5D", 0);
		
		
		acquiFreq = Integer.parseInt(preference.getString("AcquiFreq_spinner_values", "12800"));
		acquiFreq = 48000;

		freqRang = Float.parseFloat(preference.getString("colorMapFreqRange", "0.4"))*1000;
		this.setFreqRang(freqRang);
//		acquiFreq = acquiFragment.getAcquiFreq();
		overlap = Float.parseFloat(preference.getString("colorMapOverlap", "50"))/100;
		freqRes = Float.parseFloat(preference.getString("colorMapFreqRes", "0.78"));
		windowType = Integer.parseInt(preference.getString("colorMapWindow", "0"));
		mean = Integer.parseInt(preference.getString("colorMapAveraging", "3"));
		
		fft.SetWindowType(0); // native_GetWindowType
		fft.SetWinLen((int)(acquiFreq / freqRes)); // native_GetWinShift
		fft.SetWinShift((int)(acquiFreq / freqRes * (1 - overlap))); // native_GetWinLen
		
		offsetX = windowShiftTime = fft.getWinShift() / 48000.0f;
		divider = 1.0f;					//ÿ���������ػ�һ����
		xmultiple = offsetX/divider;	// ÿpx���0.001��;
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		Log.v("bug11", "colormapstart");
//		canvas.drawRect(canvas.getWidth() - 50, 0, canvas.getWidth(),
//				canvas.getHeight() - 50, p);
//		/*
//		 * if (context.getClass().getSimpleName().equals("DriveModeActivity")) {
//		 * if (AddChannelViewPager.drive_channelCount.size() == 0) return; }
//		 */
//		if (context.getClass().getSimpleName().equals("MainActivity")) {
//				if (DataCollection.isRecording||DataCollection.collectionState==1) {
//					if (DataCollection.dataCollectionIntBufferList!=null&&DataCollection.dataCollectionIntBufferList.size() != 0) {
//						if(DataCollection.collectionState==0){
//							if(!ifStartToGetData||ifEnterToDriverMode){
//								if(ColorMapBuf.size()>0){
//									ColorMapBuf.clear();
//									if(!ifEnterToDriverMode) changeX = 0;
//									else
//									if(ifStartToGetData) 	setBufferIntListIndex();	//--------驾驶|普通采集模式数据同步专用功能---------
//								}
//								ifStartToGetData=true;	
//							}
//							if(bufferIntListIndex<DataCollection.dataCollectionIntBufferList.size()){
//								int[] testdata = ((ArrayList<int[]>) DataCollection.dataCollectionIntBufferList)
//										.get(bufferIntListIndex);
//								getBuffer(testdata, canvas);
//								bufferIntListIndex += 1;
//							}
//							//-----------驾驶|普通采集同步专用index----------
//							if(xclSingalTransfer==null){
//								xclSingalTransfer=XclSingalTransfer.getInstance();
//							}
//							xclSingalTransfer.putValue("Colormap_DataCollectionIntBufferListIndex", bufferIntListIndex);
//							//-----------完毕------------------------------------
//						}else if (DataCollection.collectionState == 1 && ifStartToGetData == true) {
////							if (bufferIntListIndex >= DataCollection.dataCollectionIntBufferList.size()) {
//								DataCollection.collectionOverCalculateCount += 1;
//								bufferIntListIndex = 0;
//								ifStartToGetData = false;
//								fft.ResetResult();
//								fft.ResetMeanResult();
//								fft.ResetSignal();
//								preSize = 0;
////							} else {
////								int[] testdata = ((ArrayList<int[]>)DataCollection.dataCollectionIntBufferList)
////										.get(bufferIntListIndex);
////								getBuffer(testdata, canvas);
////								bufferIntListIndex += 1;
////							}
//						}
//					}
//				}else {
//				try {
//					if (IAudioTrack.readState == 3) {
//						IAudioTrack.audioOverCalculateCount += 1;
//						bufferIntListIndex = 0;
//						ifStartToGetData = false;
//						fft.ResetResult();
//						fft.ResetMeanResult();
//						fft.ResetSignal();
//						preSize=0;
//					}
//					if (IAudioTrack.readState == 0) {
//						if (!ifStartToGetData) {
//							if (ColorMapBuf.size() > 0) {
//								ColorMapBuf.clear();
//								changeX = 0;
//							}
//							ifStartToGetData = true;
//						}
//						if (bufferIntListIndex < IAudioTrack.mAudioTrackIntBufferList.size()) {
//							int[] testdata = ((ArrayList<int[]>) IAudioTrack.mAudioTrackIntBufferList)
//									.get(bufferIntListIndex);
//							getBuffer(testdata, canvas);
//							bufferIntListIndex += 1;
//						}
//					} else if (IAudioTrack.readState == 1 && ifStartToGetData == true) {
//						if (IAudioTrack.readState == 3) {
//							IAudioTrack.audioOverCalculateCount += 1;
//							bufferIntListIndex = 0;
//							ifStartToGetData = false;
//							fft.ResetResult();
//							fft.ResetMeanResult();
//							fft.ResetSignal();
//							preSize=0;
//						}
//						if (IAudioTrack.readState == 1) {
//							if (bufferIntListIndex >= IAudioTrack.mAudioTrackIntBufferList.size()) {
//								IAudioTrack.audioOverCalculateCount += 1;
//								bufferIntListIndex = 0;
//								ifStartToGetData = false;
//								fft.ResetResult();
//								fft.ResetMeanResult();
//								fft.ResetSignal();
//								preSize=0;
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
//		} else if (context.getClass().getSimpleName()
//				.equals("DriveModeActivity")) {
//			ifEnterToDriverMode=true;
//			if (DataCollection.isRecording||DataCollection.collectionState==1) {
//				if (DataCollection.dataCollectionIntBufferList!=null&&DataCollection.dataCollectionIntBufferList.size() != 0) {
//					if(DataCollection.collectionState==0){
//						if(!ifStartToGetData){
//							if(ColorMapBuf.size()>0){
//								ColorMapBuf.clear();
//								changeX=0;
//							}
//							ifStartToGetData=true;
//						}
//						if(bufferIntListIndex<DataCollection.dataCollectionIntBufferList.size()){
//							int[] testdata = ((ArrayList<int[]>) DataCollection.dataCollectionIntBufferList)
//									.get(bufferIntListIndex);
//							getBuffer(testdata, canvas);
//							bufferIntListIndex += 1;
//						}
//						//-----------驾驶|普通采集同步专用index----------
//						if(xclSingalTransfer==null){
//							xclSingalTransfer=XclSingalTransfer.getInstance();
//						}
//						xclSingalTransfer.putValue("Colormap_DataCollectionIntBufferListIndex", bufferIntListIndex);
//						//-----------完毕------------------------------------
//					}else if (DataCollection.collectionState == 1 && ifStartToGetData == true) {
////						if (bufferIntListIndex >= DataCollection.dataCollectionIntBufferList.size()) {
//							DataCollection.collectionOverCalculateCount += 1;
//							bufferIntListIndex = 0;
//							ifStartToGetData = false;
//							fft.ResetResult();
//							fft.ResetMeanResult();
//							fft.ResetSignal();
//							preSize = 0;
////						} else {
////							int[] testdata = ((ArrayList<int[]>)DataCollection.dataCollectionIntBufferList)
////									.get(bufferIntListIndex);
////							getBuffer(testdata, canvas);
////							bufferIntListIndex += 1;
////						}
//					}
//				}
//			}
//		}
//		if (context.getClass().getSimpleName().equals("MainActivity")) {
//			if (mainContextView != null
//					&& mainContextView.channelItems.size() == 0)
//				return;
//		} else if (context.getClass().getSimpleName()
//				.equals("DriveModeActivity")) {
//			if (((DriveModeActivity) context).isActivated_ChanNum.size() == 0)
//				return;
//		}
//		if (ColorMapBuf.size() == 0)
//			return;
//		setFFTPaint();
//		canvas.save();
//		canvas.translate(50, viewh - yPointCount - 50);
//		canvas.scale(1, (viewh - 50) / yPointCount, 0, yPointCount);
//		canvas.drawBitmap(colors, 0, ColorMapBuf.size(), 0, 0,
//				ColorMapBuf.size(), yPointCount, false, null);
//		canvas.restore();
//		Log.v("bug11", "colormap---end");

	}

	private void setFFTPaint() {
		int xcount = ColorMapBuf.size();
		for (int j = yPointCount - 1; j >= 0; j--) { // ͼ�����µߵ����� �� ����Matrix ��ת����
			for (int i = 0; i < xcount; i++) {
				try{
					float FFTData = ColorMapBuf.get(i)[j];
					if (FFTData < 0) {
						colors[i + (yPointCount - j) * xcount] = Palette[0];
						System.out.println("ffftdata<0:"+i+",FFTData:"+FFTData);
					} else if (FFTData > maxData) {
						colors[i + (yPointCount - j) * xcount] = Palette[255];
						System.out.println("FFTData > maxData");
					} else {
						colors[i + (yPointCount - j) * xcount] = Palette[(int) (FFTData * 255 / maxData)];
					}
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
	}

	private int mapLenght = 0;

	private void getBuffer(int[] buffer, Canvas canvas) {
		if (buffer == null)
			return;
		
		fft.Int_calculate(buffer, buffer.length);
		int size = fft.GetResultInfo(0);
		for (int i = preSize; i < size; i++) {
			float[] FFTResult = fft.GetResult(i);
			if (FFTResult != null && FFTResult.length > 0) {
				setWeight(FFTResult);
				ColorMapBuf.add( FFTResult);// fft.GetResult()�ᵼ�½������ż��ٵ�����
				if (ColorMapBuf.size() >= canvas.getWidth() - 100) {
					ColorMapBuf.remove(0);
					changeX += (float) (fft.getWinShift() / 48000f);
				}
			}

//			if (xlabelState != null && xlabelState.equals("RPM")) {
//				if (rpm == null) {
//					rpm = new Arith_RPM();
//				}
//			}
		}
		preSize = size;
	}
	private void setWeight(float[] FFTResult) {
		if(preference==null){
			return;
		}
		 zlabelState=preference.getString("ColorMap_FreqWeight","dB");
		if(zlabelState!=null){
			maxData = 100;
			if (zlabelState.equals("A"))
				fft.setWeight(1, FFTResult, 48000);
			if (zlabelState.equals("C"))
				fft.setWeight(3, FFTResult, 48000);
			if (zlabelState.equals("Pa")) {
				maxData = 2;
				for (int i = 0; i < FFTResult.length; i++) {
					FFTResult[i] = (float) ((2 * p0) * (Math.pow(10, (FFTResult[i] / 20))));
				}
			}
		}else{
			maxData=100;
		}
	}
	public void setChannelIndex(int index) {
		this.index = index;
	}

	public List<Float> getCursorValue(float moveX, float moveY) {

		// TODO Auto-generated method stub
		List<Float> list = new ArrayList<Float>();
		
		moveY = (viewh-50)-moveY;
		
		float xValue = moveX*xmultiple;
		float yValue = 0;
		float zValue = 0;
		int y = (int)(moveY*yPointCount/(viewh-50));
		if(ColorMapBuf!= null && ColorMapBuf.size() > (int)moveX && ColorMapBuf.get((int)moveX).length>y ){
			yValue = y*freqRes;
			zValue = ColorMapBuf.get((int)moveX)[y];
		}
	
		 list.add(xValue);
		 list.add(yValue);
		 list.add(zValue);

		return list;
	}
	public void setFreqRang(float freqRang) {
		// TODO Auto-generated method stub
		if(freqRang>24000) freqRang = 24000;
		this.freqRang = freqRang;
//		if(freqRang> acquiFreq/2) freqRang = acquiFreq/2;
		yPointCount = (int) (freqRang/freqRes);
		colors = null;
		colors = new int[(int) ((vieww - 50) * (int) (viewh - 50))];

		this.invalidate();
		if (freqRang > (viewh - 50)) {
			ymultiple = (float) (freqRang / (viewh - 50));
			float ygrid = 100 / ymultiple;
			int n = 0;
			while (ymultiple / (int) Math.pow(2, n) >= 1) {
				n += 1;
			}
			ymultiple = (int) Math.pow(2, n);
			yGrid = ymultiple * ygrid;
			ymultiple = (float) (freqRang / (viewh - 50));
		} else {
			ymultiple = (float) ((viewh - 50) / freqRang);
			float ygrid = 100 * ymultiple;
			int n = 0;
			while (ymultiple / (int) Math.pow(2, n) > 2) {
				n += 1;
			}
			ymultiple = (int) Math.pow(2, n);

			yGrid = ygrid / ymultiple;
			ymultiple = (float) (freqRang / (viewh - 50));
		}

	}

	public void setFreqRes(float freqRes) {
		// TODO Auto-generated method stub
		this.freqRes = freqRes;
		int winlen = (int) (acquiFreq / freqRes);
		int winshift = (int) (winlen * (1 - overlap) + 0.5);
		fft.SetWinLen(winlen);
		fft.SetWinShift(winshift);
		yPointCount = (int) (freqRang/freqRes);
		ColorMapBuf = new ArrayList<float[]>();	
		colors = null;
		colors = new int[(int) ((vieww - 50) * (int) (viewh - 50))];
		
		offsetX = windowShiftTime = fft.getWinShift() / 48000.0f;
		refreshLabelState();
		this.invalidate();
	}

	public void setAcquiFreq(double d) {
//		acquiFreq = (int) d;           //������ʹ��
	
		acquiFreq = 48000;
		int winlen = (int) (acquiFreq / freqRes);
		int winshift = (int) (winlen * (1 - overlap) + 0.5);
		fft.SetWinLen(winlen);
		fft.SetWinShift(winshift);
		this.invalidate();
	}
	public void setOverlap(float f) {

		// TODO Auto-generated method stub
		this.overlap  =f;
		setFreqRes(this.freqRes);
		refreshLabelState();		
		this.invalidate();
	}

	public void setWindowType(int value) {
		// TODO Auto-generated method stub
		this.windowType = value;
		fft.SetWindowType(value);
	}
	
	public void setMean(int value) {
		// TODO Auto-generated method stub
		this.mean = value;
	}
	
	
	@Override			//��д����ķŴ���С������ȥ��Ŵ���С
	protected void setRate(MotionEvent event) {
		// TODO Auto-generated method stub
	}
	
	protected void refreshXmultiple() {
		// TODO Auto-generated method stub
		offsetX = windowShiftTime = fft.getWinShift() / 48000.0f;

		if(xlabelState.equals("s")){
			xmultiple = offsetX/divider;
		}
		else if(xlabelState.equals("ms")){
			xmultiple = offsetX/divider*1000;
		}
	}
	//-----------驾驶|普通采集同步专用index----------
	public void setBufferIntListIndex(){
		if(xclSingalTransfer==null){
			xclSingalTransfer=XclSingalTransfer.getInstance();
		}
		bufferIntListIndex=xclSingalTransfer.containsKey("Colormap_DataCollectionIntBufferListIndex")?(Integer) xclSingalTransfer.getValue("Colormap_DataCollectionIntBufferListIndex"):0;
	}
	
	/**
	 * 优化绘图使用
	 * @author zhoujie
	 *
	 */
	private FFTHelper helper;
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
//				if(ColorMapBuf!= null && msg.obj!=null){
//					ColorMapBuf= (ArrayList<List<Double>>) msg.obj;
//				}
					
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
//		if (context.getClass().getSimpleName().equals("DriveModeActivity")) 	ifEnterToDriverMode=true;
//		switch(DataCollection.collectionState){
//		case 0:
//			//--------------初次计算前清空曲线------
//			if (!ifStartToGetData||ifEnterToDriverMode) {
//				if (SPLBuffer.get(0).size() > 0) {
//					for (int i = 0; i <SPLBuffer.size(); i++) {
//						SPLBuffer.get(i).clear();
//					}
//					if(!ifEnterToDriverMode) changeX = 0;
//				}
//				ifStartToGetData = true;
//				if (context.getClass().getSimpleName().equals("MainActivity")) 	ifEnterToDriverMode=false;
//			}
//			//------------计算--------------------
//			helper.caculate(DataCollection.dataListMap);
//			break;
//		case 1:
//			 if (ifStartToGetData == true) {
//					DataCollection.collectionOverCalculateCount += 1;
//					bufferIntListIndex = 0;
//					ifStartToGetData = false;
//					spl.resetResult();
//			}
//			break;
//		}
	}

	public void setHelper() {
		// TODO Auto-generated method stub
		
	}
}
