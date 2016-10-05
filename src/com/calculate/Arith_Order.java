package com.calculate;



public class Arith_Order extends Arith{
	private int mNativeOrder;
	private int startRPM;
	private int stopRPM;
	private int delayRPM;
	static {
	       System.loadLibrary("Order");
	 }
	public Arith_Order() {
		mNativeOrder=init();
	}
	@Override
	protected void finalize(){
		try{
			finalizer(mNativeOrder);
		}finally{
			try{
				super.finalize();
			}catch(Throwable e){
				e.printStackTrace();
			}
		}
	}
	public boolean  integer_calculate(int[] data,int samples){
		try{
			return native_Integer_calculate(mNativeOrder,data,samples);
		}catch(Throwable e){
			e.printStackTrace();
		}
		return false;
	}
	public boolean float_calculate(float[] data,int samples){
		try{
			return native_Float_calculate(mNativeOrder,data,samples);
		}catch(Throwable e){
			e.printStackTrace();
		}
		return false;
	}
	public float[] getOrder(double OrderLevel){
		return native_getOrder(mNativeOrder,OrderLevel);
	}
	public void setRPMstate(){
		startRPM=native_getRpmState(mNativeOrder)[0];
		stopRPM=native_getRpmState(mNativeOrder)[1];
		delayRPM=native_getRpmState(mNativeOrder)[2];
	}
	public int  getRPMStartState(){
		return startRPM;
	}
	public int  getRPMStopState(){
		return stopRPM;
	}
	public int  getRPMDelayState(){
		return delayRPM;
	}
	public int getOrderResultSize(){
		return native_getOrderResultSize( mNativeOrder);
	}
	public void setRPMData(float[] rpm,float samplerate){
		 native_setRPMData(mNativeOrder,rpm,rpm.length,samplerate);
	}
	public int getProgress(){
		return native_getProgress(mNativeOrder);
	}
	private native int init();
	private native void finalizer(int mNativeOrder);
	private native float[] native_getOrder(int mNativeOrder,double OrderLevel);
	private native boolean native_Integer_calculate(int mNativeOrder,int[] data, int samples);
	private native boolean native_Float_calculate(int mNativeOrder,float[] data, int samples);
	private native int[] native_getRpmState(int mNativeOrder);
	private native int native_getOrderResultSize(int mNativeOrder);
	private native void native_setRPMData(int mNativeOrder,float[] rpm,int rpmsize,float samplerate);
	private native int native_getProgress(int mNativeOrder);
	@Override
	public void resetResult() {
		// TODO Auto-generated method stub
		
	}
}
  