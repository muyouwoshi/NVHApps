package draw.map;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;

import com.calculate.Arith_FFT;

public class ColorMapHelper implements Runnable {
	private Canvas mCanvas;
	private Bitmap map;
	private Handler handler;
	private List<float[]> result;
	private ExecutorService threadPool;
	private int[] buffer;
	private Arith_FFT fft;
	private int yPointCount;
	private int[] colors;
	private float maxData = 100;
	private int[] Palette;
	private int WIDTH;
	

	public ColorMapHelper(Handler handler, Arith_FFT fft) {
		this.handler = handler;
		result = new ArrayList<float[]>();
		threadPool = Executors.newSingleThreadExecutor();
		this.fft = fft;

		Palette = this.fft.GetPalette(1);

	}
	
	public void setFFT(Arith_FFT fft){
		this.fft = fft;
	}

	public void setYPointCount(int count, int w, int h) {
		this.yPointCount = count;
		WIDTH = w;
		colors = null;
		colors = new int[(int) ((w - 100) * (int) (yPointCount))];
		if (map != null)
			map.recycle();
		map = Bitmap.createBitmap(w-100, yPointCount, Config.RGB_565);
		mCanvas = new Canvas(map);
	}
	public void shutdown(){
		threadPool.shutdownNow();
	}
	public void caculate(int[] buffer) {
		
		this.buffer = buffer;
		threadPool.execute(this);
	}

	@Override
	public void run() {
		if(buffer == null) return;
		synchronized (buffer) {
			getBuffer(buffer);
			buffer = null;
		}
	}

	private int preSize;
	private int startX;
	private int mapSize;

	private synchronized void getBuffer(int[] buffer) {
//		Log.v("bug11", "  "+buffer[0]+"  "+buffer[4]+"  "+buffer[20]+"  "+buffer[200]+"  "+buffer[buffer.length*6/16]+"  "+buffer[buffer.length*7/16]+"  "+buffer[buffer.length*8/16]+"  "+buffer[buffer.length*10/16]+"  "+buffer[buffer.length*14/16]+"  "+buffer[buffer.length*15/16]+"  "+buffer[buffer.length-1]);

		if (buffer == null )
			return;
		List<float[]> tempList = new ArrayList<float[]>(); 
		fft.Int_calculate(buffer, buffer.length);
		int size = fft.GetResultInfo(0);
		for (int i = preSize; i < size; i++) {
			float[] FFTResult = fft.GetResult(i);
			
			if (FFTResult != null && FFTResult.length > 0) {
				result.add(FFTResult);
				tempList.add(FFTResult);
				if (result.size() >= WIDTH - 50) {
					result.remove(0);
				}
			}
		}

		if (preSize == size)
			return;
		
		int xcount = tempList.size();
		colors = new int[xcount*yPointCount];
		for (int j = yPointCount ; j > 0; j--) { // ͼ�����µߵ����� ��
			for (int i = 0; i < xcount; i++) {
				float FFTData = tempList.get(i)[j];
				if (FFTData < 0) {
					colors[i + (yPointCount - j) * xcount] = Palette[0];
				} else if (FFTData > maxData) {
					colors[i + (yPointCount - j) * xcount] = Palette[255];
				} else {
					colors[i + (yPointCount - j) * xcount] = Palette[(int) (FFTData * 255 / maxData)];
				}
			}
		}

//		Log.v("bug11", "  "+colors[0]+"  "+colors[4]+"  "+colors[20]+"  "+colors[200]+"  "+colors[colors.length*6/16]+"  "+colors[colors.length*7/16]+"  "+colors[colors.length*8/16]+"  "+colors[colors.length*10/16]+"  "+colors[colors.length*14/16]+"  "+colors[colors.length*15/16]+"  "+colors[colors.length-1]);

		
		Bitmap nextMap = Bitmap.createBitmap(xcount, yPointCount, Config.RGB_565); 
		Canvas canvas = new Canvas(nextMap);
		canvas.drawBitmap(colors, 0, xcount, 0, 0, xcount, yPointCount, false, null);
		
		
		if(mapSize+xcount > WIDTH -100) {

			Bitmap premap = Bitmap.createBitmap(map,mapSize+xcount-WIDTH+100, 0,WIDTH-100-xcount, yPointCount);
			mCanvas.drawBitmap(premap, 0, 0,null);
			premap.recycle();
			startX = WIDTH-100-xcount;
		}else{
			startX = mapSize;
		}
		
		mCanvas.drawBitmap(nextMap,startX,0, null);
		mapSize = startX+xcount;
		
		nextMap.recycle();
		
		preSize = size;
		Message msg = Message.obtain();
		msg.what = 1;
		msg.obj = result;
		handler.sendMessage(msg);
		
		Message msg1 = Message.obtain();
		msg1.what = 0;
		msg1.obj = map;
		handler.sendMessage(msg1);
//		getBitmap();
	}
	
	@SuppressWarnings("unused")
	private void add(){			//system arrayCopy  ��ԭ���������õĲ��ָ�ֵ��������
//		System.arraycopy(colors[], srcPos, newColor[], dstPos, length)
	}
	
	@SuppressWarnings("unused")
	private void remove(){		//
		
	}

}
