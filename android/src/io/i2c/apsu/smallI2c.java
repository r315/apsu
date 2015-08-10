package io.i2c.apsu;

public class smallI2c {	
	public native int open(String fname, int device);
	public native int read(int data[],int len);
	public native int write(int data[], int len);
	public native void close();
	public native int isopen();
	static {
        System.loadLibrary("smallI2c");
    }
}
