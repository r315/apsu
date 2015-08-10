#include <jni.h>


#ifndef _smallI2c_h_
#define _smallI2c_h_

#ifdef __cplusplus
extern "C" {
#endif


#define  LOG_TAG "smallI2c"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

JNIEXPORT jint JNICALL Java_io_i2c_apsu_smallI2c_open(JNIEnv *env, jobject obj, jstring, jint);
JNIEXPORT jint JNICALL Java_io_i2c_apsu_smallI2c_read(JNIEnv *env, jobject obj, jintArray, jint);
JNIEXPORT jint JNICALL Java_io_i2c_apsu_smallI2c_write(JNIEnv *env, jobject obj, jintArray, jint);
JNIEXPORT void JNICALL Java_io_i2c_apsu_smallI2c_close(JNIEnv *env, jobject obj);
JNIEXPORT jint JNICALL Java_io_i2c_apsu_smallI2c_isopen(JNIEnv *env, jobject obj);

#ifdef __cplusplus
};
#endif
#endif
