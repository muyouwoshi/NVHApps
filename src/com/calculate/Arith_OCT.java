package com.calculate;


public class Arith_OCT extends Arith{
	private int mNativeOCT; 
	public Arith_OCT(){
		mNativeOCT=init();
	}
	static{
		System.loadLibrary("OCT");
	}
	@Override
	protected void finalize(){
		try{
			finalizer(mNativeOCT);
		}finally{
			try{
				super.finalize();
			}catch(Throwable e){
				e.printStackTrace();
			}
		}
	}
	public int calculate(int[] buf,int samples){
		return native_calculate(mNativeOCT,buf,samples);
	}
	public void SetWeightint(int weightintType){
		native_SetWeightint(mNativeOCT,weightintType);
	}
	public int GetResultInfo(){
		return native_GetResultInfo(mNativeOCT);
	}
	public  float[] GetResult(int index){
		return native_GetResult(mNativeOCT,index);
	}
	public  float[] GetMeanResult(int index){
		return native_GetMeanResult(mNativeOCT,index);
	}
	public float[] GetFinalMean(float[] data,int size){
		return native_GetFinalMean(mNativeOCT,data,size);
	}
	public void SetAvarage(int avarage){
		native_setAvarage(mNativeOCT,avarage);
	}
	public void SetTimeconstant(double timeconstant){
		native_setTimeconstant(mNativeOCT,timeconstant);
	}
	public void SetTimeinterval(int timeinterval){
		native_setTimeinterval(mNativeOCT,timeinterval);
	}
	public  void setWeight(int thirdOctaveType,float[] resultData,int weighting){
		if(resultData==null||resultData.length==0){
			return ;
		}
		setWeight(mNativeOCT,thirdOctaveType,resultData,resultData.length,weighting);
	}
	@Override
	public void resetResult() {
		// TODO Auto-generated method stub
		
	}
	private native int init();
	private native int native_calculate(int nOCT,int[] buf,int samples);
	private native void native_SetWeightint(int nOCT,int weightintType);
	private native int native_GetResultInfo(int nOCT);
	private native float[] native_GetResult(int nOCT,int index);
	private native float[] native_GetMeanResult(int nOCT,int index);
	private native float[] native_GetFinalMean(int nOCT,float[] data,int size);
	private native void finalizer(int nOCT);
	private native void native_setAvarage(int nOCT,int avarage);
	private native void native_setTimeconstant(int nOCT,double timeconstant);
	private native void native_setTimeinterval(int nOCT,int timeinterval);
	private native float[] setWeight(int nOCT,int thirdOctaveType,float[] resultData,int dataCount,int weighting);

}
