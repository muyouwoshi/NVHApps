#include "com_calculate_Arith_OCT.h"
#include "ThirdOctave.h"
#include <functional>
#include <numeric>
#include <vector>
#include <algorithm>
#include <assert.h>
#include <MeanMax.h>
#include "AddWeighting.h"
using namespace std;

JNIEXPORT jint JNICALL Java_com_calculate_Arith_1OCT_init(JNIEnv * env,
		jobject obj) {
	ThirdOctaveParam param;
	InitThirdOctaveParam(param);
	CThirdOctave *octave = new CThirdOctave(param);
	return (int) octave;
}
JNIEXPORT jint JNICALL Java_com_calculate_Arith_1OCT_native_1calculate(
		JNIEnv * env, jobject obj, jint classz, jintArray buf, jint samples) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	uint32_t samp = (uint32_t) samples;
	if (samp != 0) {
		const int* data = env->GetIntArrayElements(buf, false);
		int calResult = octave->Calculate(data, samp);
		env->ReleaseIntArrayElements(buf, (jint*) data, 0);
		return calResult;
	} else {
		return 0;
	}
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1OCT_native_1SetWeightint(
		JNIEnv * env, jobject obj, jint classz, jint weightType) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	FreqWeighting type = (FreqWeighting) weightType;
	octave->SetWeighting(type);
}
JNIEXPORT jint JNICALL Java_com_calculate_Arith_1OCT_native_1GetResultInfo(
		JNIEnv * env, jobject obj, jint classz) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	uint32_t channelsize;
	return octave->GetResultInfo(&channelsize);
}
JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1OCT_native_1GetResult(
		JNIEnv * env, jobject obj, jint classz, jint index) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	uint32_t in = (uint32_t) index;
	uint32_t ResultChannels;
	octave->GetResultInfo(&ResultChannels);
	const float* meanResult = octave->GetResult(in);
	if (ResultChannels > 0) {
		jfloatArray result = env->NewFloatArray(ResultChannels);
		env->SetFloatArrayRegion(result, 0, ResultChannels,
				(jfloat*) meanResult);
		return result;
	} else {
		return NULL;
	}
}
JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1OCT_native_1GetMeanResult(
		JNIEnv * env, jobject obj, jint classz, jint index) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	uint32_t in = (uint32_t) index;
	uint32_t ResultChannels;
	octave->GetResultInfo(&ResultChannels);
	const float* meanResult = octave->GetMeanResult(in);
	if (ResultChannels > 0) {
		jfloatArray result = env->NewFloatArray(ResultChannels);
		env->SetFloatArrayRegion(result, 0, ResultChannels,
				(jfloat*) meanResult);
		return result;
	} else {
		return NULL;
	}
}
JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1OCT_native_1GetFinalMean(
		JNIEnv * env, jobject obj, jint classz, jfloatArray data, jint size) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	uint32_t count = (uint32_t) size;
	const float* dataFloat = env->GetFloatArrayElements(data, false);
	jfloatArray outPutFloat = env->NewFloatArray(count);
	float* outFloat = env->GetFloatArrayElements(outPutFloat, false);
	octave->GetFinalMean(dataFloat, outFloat, count);
	env->SetFloatArrayRegion(outPutFloat, 0, count, (jfloat*) outFloat);
	env->ReleaseFloatArrayElements(outPutFloat, outFloat, 0);
	env->ReleaseFloatArrayElements(data, (jfloat*) dataFloat, 0);
	return outPutFloat;
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1OCT_finalizer(JNIEnv * env,
		jobject obj, jint classz) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	delete octave;
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1OCT_native_1setAvarage(
		JNIEnv * env, jobject obj, jint classz, jint avg) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	ThirdOctaveParam param = octave->GetM_param();
	switch (avg) {
	case 0:
		param.avarage = SlowAverage;
		break;
	case 1:
		param.avarage = FastAverage;
		break;
	case 2:
		param.avarage = UserAverage;
		break;
	}
	octave->SetM_param(param);
}

JNIEXPORT void JNICALL Java_com_calculate_Arith_1OCT_native_1setTimeconstant(
		JNIEnv * env, jobject obj, jint classz, jdouble timeconstant) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	ThirdOctaveParam param = octave->GetM_param();
	param.time_constant=timeconstant;
	octave->SetM_param(param);
}

JNIEXPORT void JNICALL Java_com_calculate_Arith_1OCT_native_1setTimeinterval(
		JNIEnv * env, jobject obj, jint classz, jint timeinterval) {
	CThirdOctave *octave = (CThirdOctave*) classz;
	ThirdOctaveParam param = octave->GetM_param();
	param.time_interval=timeinterval;
	octave->SetM_param(param);
}

JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1OCT_setWeight
  (JNIEnv * env, jobject obj, jint classz, jint thirdOctaveType, jfloatArray data, jint dataCount,jint weighting){
	if (data != NULL) {
		const float* result = env->GetFloatArrayElements(data, false);
		 GetOctaveWeightingResult(thirdOctaveType,result,weighting);
		env->SetFloatArrayRegion(data, 0, dataCount, result);
		env->ReleaseFloatArrayElements(data, (jfloat*) result, 0);
		return data;
	} else {
		return NULL;
	}
}
