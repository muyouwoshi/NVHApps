#include "ArticulationIndex.h"
#include "com_calculate_Arith_AI.h"
#include "ThirdOctave.h"
#include <functional>
#include <numeric>
#include <vector>
#include <algorithm>
#include <assert.h>
#include <MeanMax.h>
#include<iostream>
using namespace std;
JNIEXPORT jint JNICALL Java_com_calculate_Arith_1AI_init
  (JNIEnv * env, jobject obj){
	ThirdOctaveParam param;
	InitThirdOctaveParam(param);
	AIType type;
	type=AI_NVH;
	CAICalc *calc=new CAICalc(param,type);
	return (int)calc;
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1AI_finalizer
  (JNIEnv * env, jobject obj, jint classz){
	CAICalc *calc=(CAICalc*)classz;
	delete calc;
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1AI_native_1calculate
  (JNIEnv * env, jobject obj, jint classz,jintArray buffer, jint samples){
	CAICalc *calc=(CAICalc*)classz;
	uint32_t samp=(uint32_t)samples;
	jboolean* bo=false;
	if(samp!=0){
		 int *data=env->GetIntArrayElements(buffer,bo);
		calc->ResetResult();
		calc->Calculate(data,samp);
		env->ReleaseIntArrayElements(buffer,(jint*)data,0);
	}
}
JNIEXPORT jdouble JNICALL Java_com_calculate_Arith_1AI_native_1getAI
  (JNIEnv * env, jobject obj, jint classz){
	CAICalc *calc=(CAICalc*)classz;
	return calc->GetAI();
}
//JNIEXPORT jobject JNICALL Java_com_calculate_Arith_1AI_native_1getFrame
//  (JNIEnv * env, jobject obj, jint classz, jint count){
//	CAICalc *calc=(CAICalc*)classz;
//	uint32_t* size=(uint32_t*)&count;
//	return calc->GetFrames(size);
//}
JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1AI_ai_1time
  (JNIEnv * env, jobject obj, jint classz,jint count){
	CAICalc *calc=(CAICalc*)classz;
	uint32_t* size=(uint32_t*)&count;
	CMeanMax mean(NB_BAND_AI, true);
	uint32_t frame_count;
	const AIFrame* frames = calc->GetFrames(&frame_count);
	for (uint32_t i = 0; i != frame_count; ++i)
	{
		mean.AddData(frames[i].ai);
	}
	const float * time_data = mean.GetTimeData(&frame_count);
	jfloatArray aiTimeArray=env->NewFloatArray(frame_count);
	env->SetFloatArrayRegion(aiTimeArray,0,frame_count,time_data);
	return aiTimeArray;
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1AI_native_1resetResult
  (JNIEnv * env, jobject obj ,jint classz){
	CAICalc *calc=(CAICalc*)classz;
	calc->ResetResult();
}
