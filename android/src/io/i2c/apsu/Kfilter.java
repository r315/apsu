package io.i2c.apsu;

public class Kfilter {

	private double q,r,p,x,k;
	
	public Kfilter(double q, double r, double p, double x){
		this.q = q; // process error
		this.r = r; // mesured noise
		this.p = p; // estimated error
		this.x = x;	// value	
	}
	
	public double getValue(double measure){		 
		  p = p + q;		  
		  k = p / (p + r);
		  x = x + k * (measure - x);
		  p = (1 - k) * p;	
		return x;
	}	
}
