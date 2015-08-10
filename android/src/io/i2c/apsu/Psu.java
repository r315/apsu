package io.i2c.apsu;

public class Psu {
	private static final double MAX_VOLTAGE = 15.0; //volts
	private static final double MAX_CURRENT = 1500; //miliamps		
	private double voltage, vlimit;
	private double current, ilimit;
	private double V_CONST = (MAX_VOLTAGE/Iboard.MAX_ADC_VALUE);
	private double I_CONST = (MAX_CURRENT/Iboard.MAX_ADC_VALUE);	
	private int pwm_c,pwm_v;
	public Iboard ibd;
	private boolean loadenable;
	private Kfilter vfilter,afilter;
	
	public void stop(){
		pwm_c=0;
    	pwm_v=0;
    	ibd.setValue(ibd.PWM0, pwm_v);
    	ibd.setValue(ibd.PWM1, pwm_c);
	}
	
	public void changeVoltage(int r){
		if( (pwm_v+r)<0 || (pwm_v+r) > Iboard.MAX_PWM_VALUE || ibd.isLocked())
			return;		
		pwm_v += r;
		ibd.setValue(ibd.PWM0, pwm_v);		
	}
	
	public void changeCurrent(int r){
		if( (pwm_c+r)<0 || (pwm_c+r) > Iboard.MAX_PWM_VALUE || ibd.isLocked())
			return;		
		pwm_c += r;
		ibd.setValue(ibd.PWM1, pwm_c);		
	}
	
	public double getVoltage(){
		if(!ibd.isLocked())
			voltage = vfilter.getValue(ibd.getValue(ibd.ADC0)*V_CONST);
		return voltage;
	}
	
	public double getCurrent(){
		if(!ibd.isLocked())
			current = afilter.getValue(ibd.getValue(ibd.ADC1)*I_CONST);
		return current;
	}
	
	public double getPower(){
		return voltage * (current/1000);
	}
	
	public double getVlimit(){
		return vlimit;
	}
	
	public double getIlimit(){
		return ilimit;
	}
	
	public void setVoltage(double v){
		vlimit = v;	
	}
	
	public void setCurrent(double i){
		ilimit = i;
	}
	
	public void loadEnable(boolean en){
		if(en){
			loadenable = true;
			ibd.setValue(ibd.GPIO, 0);
		}
		else{
			loadenable = false;
			ibd.setValue(ibd.GPIO, 1);
		}		
	}
	
	public boolean isLoadEnable(){
		return loadenable;
	}
	
	public Psu(){
		ibd = new Iboard();
		loadenable = false;
		vfilter = new Kfilter(0.125,32.0,1.0,2.5);
		afilter = new Kfilter(0.125,32.0,1.0,0.0);
		pwm_c = 0;
		pwm_v = 0;		
	}
}
