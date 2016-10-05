package common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;
public abstract class Helper<A,T> implements Runnable{
	protected A algorith;
	protected Handler handler;
	protected ArrayList<T> result;
	protected int length;
	protected Map<Integer,List<int[]>> dataListMap;
	protected int[] checkedChannelIndexArray;
	protected String xlabelState;
	protected String ylabelState;
	protected float changeX = 0;
	
	protected double p0 = 2 * Math.pow(10, -5);
	protected double a0 = 2 * Math.pow(10, -5);
	
	protected ExecutorService threadPool;
	
	public Helper(Handler handler,A algorith,ArrayList<T> result) {
		
		this.handler = handler;
		this.algorith = algorith;
		this.result = result;
		
		threadPool = Executors.newFixedThreadPool(8);

	}
	
	/**
	 * ���ñ�������ݳ��ȣ���scaleView.init()������
	 * @param lenght  ��ͼ��Ҫ����������
	 * ͼ�Ŀ�ȳ��Ե�֮��ļ���õ�   vieww/divider
	 */
	public void setLength(int length){
		this.length = length;
	}
	
	/**
	 * ���ü���������Ҫ�Ĳ���
	 * @param checkedChannelIndexArray ��ѡ�е�ͨ��
	 */
	public void setInfo(int[] checkedChannelIndexArray,String ylabelState,String xlabelState) {
		this.checkedChannelIndexArray = checkedChannelIndexArray;
		this.ylabelState = ylabelState;
		this.xlabelState = xlabelState;
	}
	
	/**
	 * ������
	 */
	public void caculate(Map<Integer,List<int[]>> dataListMap){
		this.dataListMap = dataListMap;
		threadPool.execute(this);
	}
	
	public void shutdown() {
		threadPool.shutdownNow();
	}
	
	public void setChangeX(float value){
		this.changeX = value;
	}
	
	@Override
	public void run() {
		if (dataListMap == null)
			return;
		synchronized (dataListMap) {
			getBuffer(dataListMap);
		}
	}
	
	protected abstract void getBuffer(Map<Integer,List<int[]>> dataListMap);

	public void sendResult(){
		Message msg = Message.obtain();
		msg.what = 0;
		msg.obj = result;
		handler.sendMessage(msg);
	}
}
