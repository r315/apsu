package io.i2c.apsu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.util.Log;

public class Iboard{
	private static final String TAG = "Iboard";
	private static final int DEVICE = 0x63;	// 7-bit address
	private static final String BUS = "/dev/i2c-1";	
	// i2c protocol format
	// datah					   datal
	// |F3|F2|F1|RD|S1|S0|D9|D8|   |d7|d6|d5|d4|d3|d2|d1|d0|	
	public static final int getID  = (0 << 5);
	public static final int ADC = (1 << 5);
	public final int ADC0 = ADC;
	public final int ADC1 = ADC | (1 << 2);
	public final int ADC2 = ADC | (2 << 2);		
	public static final int PWM = (2 << 5);
	public final int PWM0 = PWM;
	public final int PWM1 = PWM | (1 << 2);
	public final int GPIO = (3 << 5);	
	public static final int RDBIT = (1 << 4);
	
	public static final int MAX_PWM_VALUE = 1024;
	public static final int MIN_PWM_VALUE = 0;
	public static final int MAX_ADC_VALUE = 255;	
	
	private smallI2c ctrlbd;	
	private boolean hasroot;
	private int data[];	
	
	private boolean lock;	

	public boolean isLocked(){		
		return lock;
	}
	
	public void setValue(int func, int val){
		if(!hasroot) return;
		lock = true;
		data[0] = func | ((val >> 8) & 3) ;
    	data[1] = val;
    	ctrlbd.write(data,2);
    	lock = false;
	}
	
	public int getValue(int func){
		if(!hasroot) return 0;
		lock = true;
		switch(func)
		{
			case ADC0:	// adc function requires 
			case ADC1:  // set up read ch
			case ADC2:  // then read function
				setValue(func,0);				 
				break;
			case PWM0:
			case PWM1:	
			default:				
				break;
		}			
		setValue(func | RDBIT,0);
    	ctrlbd.read(data,2); 
    	lock = false;
    	return (data[0] << 8) | data[1];
	}

	
	public void open(){
		if(!hasroot) return;
		ctrlbd = new smallI2c();
		int fp = ctrlbd.isopen(); 
		if( fp < 0){
			Log.i(TAG,"Opening i2c bus");		
			fp = ctrlbd.open(BUS, DEVICE);
		}
		if(fp < 0){
			Log.e(TAG,"Fail to start HW");
			return;
		}
		else
			Log.i(TAG,String.format("%s opened with handle %d\n",BUS,fp));		
	}
	
	public boolean hasRoot(){
		Log.i(TAG,"Checking for root");
		hasroot = false;
		try { 
			Process p = Runtime.getRuntime().exec(String.format("su -c chmod 777 %s\n",BUS));	
			p.waitFor();
			p = Runtime.getRuntime().exec(String.format("ls -l %s\n",BUS));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			char [] permissions = new char[50];
			StringBuffer cmdres = new StringBuffer();
			int n = bufferedReader.read(permissions);
			cmdres.append(permissions, 0, n);			
			bufferedReader.read(permissions); 
			bufferedReader.close();
			p.waitFor();			
			String [] attr = cmdres.toString().split(" ");
			hasroot = attr[0].equals("crwxrwxrwx") ? true : false;
			if(hasroot)
				Log.i(TAG,"Device is rooted");
			else
				Log.e(TAG,"device not rooted");			
		} catch (IOException e) {
			Log.e(TAG,e.toString());			
		} catch (InterruptedException e) {
			Log.e(TAG,e.toString());			
		}
		return hasroot;
	}	
	
	public Iboard(){		
		data = new int [2];		
		hasRoot();
	}

	public void close() {
		if(hasroot)
			ctrlbd.close();		
	}
}
