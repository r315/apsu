#include <jni.h>
#include <errno.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <time.h>
#include <linux/i2c.h>
#include <android/log.h>

#include "smallI2c.h"

struct i2c_msg msg;
struct i2c_rdwr_ioctl_data{
	struct i2c_msg *msgs;
	int nmsgs;
}msgs;

int file = -1;

JNIEXPORT jint JNICALL Java_io_i2c_apsu_smallI2c_isopen(JNIEnv *env, jobject obj){
	LOGI("Handle %d\n",file);
	return file;
}
//int i2cWrite(char *data){
JNIEXPORT jint JNICALL Java_io_i2c_apsu_smallI2c_write
(JNIEnv *env, jobject obj, jintArray data, jint len) {
int res,i;

	jint *bufInt;
	char *buf;

	bufInt = (jint *) malloc(len * sizeof(int)); // assumes there's memory
	buf = (char *) malloc(len * sizeof(char));

	(*env)->GetIntArrayRegion(env, data, 0, len, bufInt);

	for(i = 0; i < len ; i++)
			buf[i] = bufInt[i] & 0xFF;

	msg.flags = 0; //write
	msg.len = len;
	msg.buf = buf;

	msgs.nmsgs = 1;

	if ((res = ioctl(file, I2C_RDWR, &msgs)) < 0)
		LOGE("Error: write error:%d\n",res);

	free(bufInt);
	free(buf);
	return len;
}



//int i2cRead(char *data){
JNIEXPORT jint JNICALL Java_io_i2c_apsu_smallI2c_read
(JNIEnv *env, jobject obj, jintArray data, jint len) {
int res,i;

	jint *bufInt;
	char *buf;

	bufInt = (jint *) malloc(len * sizeof(int)); // assumes there's memory
	buf = (char *) malloc(len * sizeof(char));

	(*env)->GetIntArrayRegion(env, data, 0, len, bufInt);

	msg.flags = 1; //read
	msg.len = len;
	msg.buf = buf;
	msgs.nmsgs = 1;

	memset(buf,0,len);

	if ( res = ioctl(file, I2C_RDWR, &msgs) < 0){
		LOGE("Error: Read error:%d\n",res);
		goto exit;
	}

	for(i = 0; i < len ; i ++){
		bufInt[i] = buf[i];
		(*env)->SetIntArrayRegion(env, data, 0, len, bufInt);
	}

exit:
	free(bufInt);
	free(buf);
	return len;
}

JNIEXPORT jint JNICALL Java_io_i2c_apsu_smallI2c_open(JNIEnv *env, jobject obj, jstring fname, jint device){
	char bus[20];
	const jbyte *str;

	str = (*env)->GetStringUTFChars(env, fname, NULL);

	sprintf(bus, "%s", str);
	(*env)->ReleaseStringUTFChars(env, fname, str);

	if ((file = open(bus,O_RDWR)) < 0) {
 	 	LOGE("Error: Could not open file %s %s\n",bus, strerror(errno));
		return file;
	}

	if (ioctl(file,I2C_SLAVE,device) < 0) {
		LOGE("Error: Cannot communicate with slave: %s\n",strerror(errno));
		close(file);
		return -1;
	}

	msg.addr = device;
	msgs.msgs = &msg;
	msgs.nmsgs = 1;
	LOGI("%s opened with handle %u\n",bus,file);
	return file;
}

JNIEXPORT void JNICALL Java_io_i2c_apsu_smallI2c_close(JNIEnv *env, jobject obj){
	LOGI("handle %u closed!\n",file);
	close(file);
	file = -1;
}


