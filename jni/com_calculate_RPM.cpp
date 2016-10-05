#include "com_calculate_Arith_RPM.h"
#include "rpm.h"
#include <functional>
#include <numeric>
#include <vector>
#include <algorithm>
#include <assert.h>
#include <MeanMax.h>
using namespace std;
JNIEXPORT jint JNICALL Java_com_calculate_Arith_1RPM_init(JNIEnv * env,
		jobject obj) {
	double out_fs = 50;
	CRPM *calc = new CRPM(out_fs);
	CRPMCalc *rpmCalc = new CRPMCalc();
	return (int) calc;
}
JNIEXPORT jint JNICALL Java_com_calculate_Arith_1RPM_intiCalc(JNIEnv * env,
		jobject obj) {
	CRPMCalc *rpmCalc = new CRPMCalc();
	return int(rpmCalc);
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1RPM_finalizer
(JNIEnv * env, jobject obj, jint classz) {
	CRPM *calc=(CRPM*)classz;
	delete calc;
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1RPM_native_1DecodeTacho
(JNIEnv * env, jobject obj, jint classz, jintArray buffer, jint samples) {
	CRPM *calc=(CRPM*)classz;
	uint32_t samp=(uint32_t)samples;
	if(samp!=0) {
//		const int* data=env->GetIntArrayElements(buffer,false);
//		calc->DecodeTacho(data,samp);
//		jint* cData=(jint*)data;
//		env->ReleaseFloatArrayElements(buffer,cData,0);
	}
}
JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1RPM_native_1getRPM(
		JNIEnv * env, jobject obj, jint classz) {
	CRPM *calc = (CRPM*) classz;
	uint32_t count = 0;
	const float* data = calc->GetRPM(&count);
	if (data == NULL)
		return;
	if (count > 0) {
		jfloatArray result = env->NewFloatArray(count);
		if (result == NULL) {
			return NULL;
		}
		env->SetFloatArrayRegion(result, 0, count, (jfloat*) data);
		return result;
	} else {
		return NULL;
	}
}
JNIEXPORT void JNICALL Java_com_calculate_Arith_1RPM_native_1InitTacho
(JNIEnv * env, jobject obj, jint classz, jdouble fs) {
	CRPM *calc=(CRPM*)classz;
	int pulse=1;
	float level=0.0f;
	calc-> InitTacho( fs,pulse,level);
}
/*
 * Class:     com_calculate_Arith_RPM
 * Method:    native_InitCalcTacho
 * Signature: (ID[FI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_calculate_Arith_1RPM_native_1InitCalcTacho(
		JNIEnv * env, jobject obj, jint classz, jdouble fs,
		jfloatArray rpm_data, jint count) {
	CRPMCalc *rpmCalc = (CRPMCalc*) classz;
	uint32_t data_count = 0;
	const float* data = env->GetFloatArrayElements(rpm_data, false);
	rpmCalc->Init(fs, data, (uint32_t) count, data_count);
	env->ReleaseFloatArrayElements(rpm_data, (jfloat*) data, 0);
}

/*
 * Class:     com_calculate_Arith_RPM
 * Method:    native_HaveRPMData
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_calculate_Arith_1RPM_native_1HaveRPMData(
		JNIEnv * env, jobject obj, jint classz) {
	CRPMCalc *rpmCalc = (CRPMCalc*) classz;
	return rpmCalc->HaveRPMData();
}

/*
 * Class:     com_calculate_Arith_RPM
 * Method:    native_getRPMRange
 * Signature: (I)[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1RPM_native_1getRPMRange(
		JNIEnv * env, jobject obj, jint classz) {
	CRPMCalc *rpmCalc = (CRPMCalc*) classz;
	uint32_t startRPM ;
	uint32_t endRPM ;
	uint32_t deltaRPM;
	rpmCalc->GetRPMRange(startRPM, endRPM, deltaRPM);
	int dataCount = (endRPM - startRPM + 1) / deltaRPM;
	float* data = new float[dataCount];
	int i = 1;
	data[0] = startRPM;
	while (i < dataCount) {
		data[i] = data[i - 1] + deltaRPM;
	}
	jfloatArray result = env->NewFloatArray(dataCount);
	env->SetFloatArrayRegion(result, 0, dataCount, (jfloat*) data);
	return result;
}

/*
 * Class:     com_calculate_Arith_RPM
 * Method:    native_getRPMFFTData
 * Signature: (I[FD)[F
 */
JNIEXPORT jstring JNICALL Java_com_calculate_Arith_1RPM_native_1getRPMFFTData(
		JNIEnv * env, jobject obj, jint classz,jobject fftresult,
		jint fftresultsize, jdouble time_interval) {
	CRPMCalc *rpmCalc = (CRPMCalc*) classz;
	jclass cls=env->GetObjectClass(fftresult);
	jfieldID nameFieldId=env->GetFieldID(cls,"fftresult","Ljava/lang/ArrayList;");
	if(nameFieldId==NULL){
		return NULL;
	}
	jstring data=(jstring)env->GetObjectField(fftresult,nameFieldId);
//	vector<float> fftdata;
//
//	const float* data = env->GetFloatArrayElements(fftresult, false);
//	for (int i = 0; i < fftresultsize; i++) {
//		fftdata.push_back(data[i]);
//	}
//	double dddd = (double) time_interval;
//	uint32_t aaaa = (uint32_t) fftresultsize;
//
//	std::vector<std::unique_ptr<float>>  fftdata_test = rpmCalc->GetRPMData(fftdata.begin(),fftdata.end(),aaaa, dddd);
//	env->ReleaseFloatArrayElements(fftresult, (jfloat*) data, 0);
	return data;
}

/*
 * Class:     com_calculate_Arith_RPM
 * Method:    native_getRPM_AI_SPL_Data
 * Signature: (I[FD)[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_calculate_Arith_1RPM_native_1getRPMAIData(
		JNIEnv * env, jobject obj, jint classz, jfloatArray ai_result,jint airesultsize,
		jint time_interval) {
	CRPMCalc *rpmCalc = (CRPMCalc*) classz;
	vector<float> aidata;

	const float* data = env->GetFloatArrayElements(ai_result, false);
	for (int i = 0; i < airesultsize; i++) {
		aidata.push_back(data[i]);
	}
	vector<float> rpmresult= rpmCalc->GetRPMData(aidata.begin(),aidata.end(),time_interval);
	jfloatArray result = env->NewFloatArray(rpmresult.size());
	jfloat tempRPMResult[rpmresult.size()];
	for(int i=0;i<rpmresult.size();i++){
		tempRPMResult[i]=rpmresult[i];
	}
	env->SetFloatArrayRegion(result,0,rpmresult.size(),tempRPMResult);
	env->ReleaseFloatArrayElements(ai_result, (jfloat*) data, 0);
	return result;
}

JNIEXPORT jdoubleArray JNICALL Java_com_calculate_Arith_1RPM_native_1getRPMSPLData
  (JNIEnv * env, jobject obj, jint classz, jdoubleArray splresult,jint splresultsize,jint time_interval){
	CRPMCalc *rpmCalc = (CRPMCalc*) classz;
	vector<double> spldata;

	 jdouble* data = env->GetDoubleArrayElements (splresult, false);
	for (int i = 0; i < splresultsize; i++) {
		spldata.push_back(data[i]);
	}
	vector<double> rpmresult= rpmCalc->GetRPMData(spldata.begin(),spldata.end(),time_interval);
	jdoubleArray result = env->NewDoubleArray(rpmresult.size());
	jdouble tempRPMResult[rpmresult.size()];
	for(int i=0;i<rpmresult.size();i++){
		tempRPMResult[i]=rpmresult[i];
	}
	env->SetDoubleArrayRegion(result,0,rpmresult.size(),tempRPMResult);
	env->ReleaseDoubleArrayElements(splresult,data, 0);
	return result;
}
