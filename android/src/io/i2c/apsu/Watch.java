package io.i2c.apsu;

import java.util.Timer;
import java.util.TimerTask;

public class Watch {
	private static final int INTERVAL = 1000; //one second
	private Timer tim;
	private int seconds;	
	
	public String toString(){
		int h = seconds/3600;
		int m = (seconds - (h*3600))/60;		
		return String.format("%02d:%02d:%02d",h,m,seconds%60);		
	}
	
	public void start(){
		if(tim == null){            	
			tim = new Timer();
			tim.schedule(new TimerTask(){
				@Override
				  public void run() {	  
					seconds++;	
				}
			}, 10, INTERVAL);
		}
	}
	
	public void stop(){
		if(tim == null)return;
		tim.cancel();
		tim = null;		
	}
	
	public void reset(){
		stop();
		seconds = 0;
	}
	
	public Watch(){
		seconds = 0;
		tim = null;
	}	
}
