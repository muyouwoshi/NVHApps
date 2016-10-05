package com.algorithm.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.calculate.Arith;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import common.DataCollection;
import common.ScaleView;
import draw.map.AI;


abstract class Helper<A extends Arith,T> implements Runnable {
	protected final static int BUFFER_SUSECSS = 0;
	protected Map<Integer, List<int[]>> src;
	
	protected A arith;
	
	protected List<View> viewList = new ArrayList<View>();
	protected List<ArrayList<List<Float>>> resultList = new ArrayList<ArrayList<List<Float>>>();
	protected List<Integer> resultListMaxlenght = new ArrayList<Integer>();
	
	protected List<Integer> lineDataSizeList;// 存储各通道计算到第几个int[]
	protected int lineDataSizeListIndex;
	protected int dataIndex;
	
	protected ExecutorService threadPool;
	
	protected int[] buffer;
	
	protected Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case BUFFER_SUSECSS:
				invalidateAll();
				break;
				
			default:
				break;
			}
			
		}
		
	};
	
	protected Helper() {
		viewList = new ArrayList<View>();
		resultList = new ArrayList<ArrayList<List<Float>>>();
		resultListMaxlenght = new ArrayList<Integer>();
		threadPool = Executors.newSingleThreadExecutor();
		src = new HashMap<Integer, List<int[]>>();
		lineDataSizeList = new ArrayList<Integer>();
		lineDataSizeList.add(-1); 
		
	}
	public void addViewQueue(ScaleView view){
		if(null == view ) return;
		if(null == view.getResultBuffer()) return;
		if(!viewList.contains(view)){
			viewList.add(view);
			resultList.add(view.getResultBuffer());
			resultListMaxlenght.add(view.getDataLength());
		}
		else{
			int index = viewList.indexOf(view);
			int maxDataLength = view.getDataLength();
			resultListMaxlenght.set(index, maxDataLength);
		}
	}
	
	protected  void invalidateAll() {
		int viewCount = viewList.size();
		for(int i = 0; i < viewCount;i++){
			ScaleView view = (ScaleView) viewList.get(i);
			view.invalidate();
		}
	}
	
	public void startCaculate(int[] buffer){
		arith.resetResult();
		src.clear();
		this.buffer = buffer;
		switch(DataCollection.collectionState){
		case 0:
			src.putAll(DataCollection.dataListMap);
//			if(src == null	) return;
			threadPool.execute(this);
			break;
		case 1:
		}
	}
	
	@Override
	public void run() {
			synchronized (buffer) {
				caculate(buffer);
			}
	}
	
	protected abstract void caculate(int[] buffer);
	protected abstract void caculate(Map<Integer, List<int[]>> dataListMap);

	protected void notifyDataChange(){
		Message msg = Message.obtain();
		msg.what = BUFFER_SUSECSS;
		handler.sendMessage(msg);
	}
}
