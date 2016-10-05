#include "com_calculate_Arith_Order.h"
#include "OrderAnalysis.h"
#include <functional>

using namespace std;

/*
 * Class:     com_calculate_Arith_Order
 * Method:    init
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_calculate_Arith_1Order_init(JNIEnv * env,
		jobject obj) {
	OrderParams params;
	InitOrderParams(params);
	COrderAnalysis *orderave = new COrderAnalysis(params);
	return (int) orderave;
}

/*
 * Class:     com_calculate_Arith_Order
 * Method:    finalizer
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_calculate_Arith_1Order_finalizer(JNIEnv * env,
		jobject obj, jint classz) {
	COrderAnalysis *orderave = (COrderAnalysis*) classz;
	delete orderave;
}

/*
 * Class:     com_calculate_Arith_Order
 * Method:    native_getOrder
 * Signature: (ID)[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1Order_native_1getOrder(
		JNIEnv * env, jobject obj, jint classz, jdouble index) {
	COrderAnalysis *orderave = (COrderAnalysis*) classz;
	jint channelSize = (jint) orderave->GetResultSize();
	if (channelSize > 0) {
		jfloatArray result = env->NewFloatArray(channelSize);
		float* data = env->GetFloatArrayElements(result, false);
		if(orderave->GetResult(index,data)){
			env->SetFloatArrayRegion(result, 0, channelSize,data);
			env->ReleaseFloatArrayElements(result, (jfloat*) data, 0);
			return result;
		}else{
			return NULL;
		}
	} else {
		return NULL;
	}
}

/*
 * Class:     com_calculate_Arith_Order
 * Method:    native_Integer_calculate
 * Signature: (I[II)V
 */
JNIEXPORT jboolean JNICALL Java_com_calculate_Arith_1Order_native_1Integer_1calculate(
		JNIEnv * env, jobject obj, jint classz, jintArray buf, jint samples) {
	COrderAnalysis *orderave = (COrderAnalysis*) classz;
	uint32_t samp = (uint32_t) samples;
	if (samp != 0) {
		const int* data = env->GetIntArrayElements(buf, false);
		bool calResult = orderave->Calculate(data, samples);
		env->ReleaseIntArrayElements(buf, (jint*) data, 0);
		return calResult;
	} else {
		return false;
	}
}

/*
 * Class:     com_calculate_Arith_Order
 * Method:    native_Float_calculate
 * Signature: (I[FI)V
 */
JNIEXPORT jboolean JNICALL Java_com_calculate_Arith_1Order_native_1Float_1calculate(
		JNIEnv * env, jobject obj, jint classz, jfloatArray buf, jint samples) {
	COrderAnalysis *orderave = (COrderAnalysis*) classz;
	uint32_t samp = (uint32_t) samples;
	if (samp != 0) {
		const float* data = env->GetFloatArrayElements(buf, false);
		bool calResult = orderave->Calculate(data, samples);
		env->ReleaseFloatArrayElements(buf, (jfloat*) data, 0);
		return calResult;
	} else {
		return false;
	}
}

/*
 * Class:     com_calculate_Arith_Order
 * Method:    native_getRpmState
 * Signature: (I)[I
 */
JNIEXPORT jintArray JNICALL Java_com_calculate_Arith_1Order_native_1getRpmState(
		JNIEnv * env, jobject obj, jint classz) {
	COrderAnalysis *orderave = (COrderAnalysis*) classz;
	uint32_t start;
	uint32_t stop;
	uint32_t delay;
	orderave->GetResultRPMInfo(start, stop, delay);
	int  data[]={(int)start,(int)stop,(int)delay};
	jintArray result = env->NewIntArray(3);
	env->SetIntArrayRegion(result, 0, 3, (jint*) data);
	return result;
}

/*
 * Class:     com_calculate_Arith_Order
 * Method:    native_getOrderResultSize
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_calculate_Arith_1Order_native_1getOrderResultSize(
	JNIEnv *env, jobject obj, jint classz) {
COrderAnalysis *orderave = (COrderAnalysis*) classz;
return (jint)orderave->GetResultSize();
}

///*
// * Class:     com_calculate_Arith_Order
// * Method:    native_setRPMData
// * Signature: (I[FF)V
// */
JNIEXPORT void JNICALL Java_com_calculate_Arith_1Order_native_1setRPMData(
JNIEnv *env, jobject obj, jint classz, jfloatArray rpmdata,jint count,jfloat samplerate) {
COrderAnalysis *orderave = (COrderAnalysis*) classz;
const float* rpm=env->GetFloatArrayElements(rpmdata, false);
orderave->SetRPMData(rpm,count,(float)samplerate);
env->ReleaseFloatArrayElements(rpmdata, (jfloat*) rpm, 0);
}
/*
 * Class:     com_calculate_Arith_Order
 * Method:    native_getProgress
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_calculate_Arith_1Order_native_1getProgress(
	JNIEnv *env, jobject obj, jint classz) {
COrderAnalysis *orderave = (COrderAnalysis*) classz;
return (jint)orderave->GetProgress();
}
