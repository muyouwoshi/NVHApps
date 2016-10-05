package com.calculate;

import java.util.List;


public class Arith_AI extends Arith{
	private int mNativeAI;
	static {
	       System.loadLibrary("AI");
	 }
	public Arith_AI() {
		mNativeAI=init();
	}
	@Override
	protected void finalize(){
		try{
			finalizer(mNativeAI);
		}finally{
			try{
				super.finalize();
			}catch(Throwable e){
				e.printStackTrace();
			}
		}
	}
	public void calculate(int[] data,int samples){
		try{
			native_calculate(mNativeAI,data,samples);
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	public double getAI(){
		return native_getAI(mNativeAI);
	}
	public List<float[]> getFrame(int count){
		List<float[]> here=native_getFrame(mNativeAI,count);
		count=count+0;
		return here;
	}
	public float[] getAi_Time(int count){
		return ai_time(mNativeAI,count);
	}
	public void resetResult(){
		native_resetResult(mNativeAI);
	}
	private native int init();
	private native void finalizer(int nAI);
	private native double native_getAI(int nAI);
	private native void native_calculate(int nAI,int[] data, int samples);
	private native List<float[]> native_getFrame(int nAI,int count);
	private native float[] ai_time(int nAI,int count);
	private native void native_resetResult(int nAI);
}
  