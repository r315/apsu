package io.i2c.apsu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestHw extends Activity {
	
	private Button bReadAdc, bWritePwm1, bReadGpi,bWriteGpo, bWritePwm2;	 
	private TextView adc_value, pwm1_value, pwm2_value, gpo_value, gpi_value; 	
	private Iboard ibd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_menu);		
				
		ibd = new Iboard();
		ibd.hasRoot();
		ibd.open(); 
		
		bReadAdc = (Button) findViewById(R.id.test_adc);
		bWritePwm1 = (Button) findViewById(R.id.test_pwm1);
		bWritePwm2 = (Button) findViewById(R.id.test_pwm2);
		bReadGpi = (Button) findViewById(R.id.test_gpi);
		bWriteGpo = (Button) findViewById(R.id.test_gpo);
	
		adc_value   = (TextView)findViewById(R.id.test_adc_value);
		pwm1_value   = (TextView)findViewById(R.id.test_pwm1_value);
		pwm2_value   = (TextView)findViewById(R.id.test_pwm2_value);
		gpo_value   = (TextView)findViewById(R.id.test_gpo_value);		
		gpi_value   = (TextView)findViewById(R.id.test_gpi_value);
	
		adc_value.setText("0");
		pwm1_value.setText("0");
		pwm2_value.setText("0"); 
		gpo_value.setText("0");    		
		gpi_value.setText("0");	
		
		bReadAdc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String t = String.format("0x%X\n",ibd.getValue(ibd.ADC1));
            	adc_value.setText(t); 
            }
        });
	 
        bWritePwm1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {	            	
            	String t = pwm1_value.getText().toString();
            	ibd.setValue(ibd.PWM0,Integer.parseInt(t,10) ); 
            } 
        });		
        bWritePwm2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {	            	
            	String t = pwm2_value.getText().toString();
            	ibd.setValue(ibd.PWM1,Integer.parseInt(t,10) );	 
            } 
        });		
        bReadGpi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {	            	
            	String t = String.format("0x%X\n",ibd.getValue(ibd.GPIO));
            	gpi_value.setText(t);  
            } 
        });		
        bWriteGpo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String t = gpo_value.getText().toString();
            	ibd.setValue(ibd.GPIO,Integer.parseInt(t,10) );	 
            } 
        });	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.main.main, menu);
		return true;
	}

	
	@Override 
    public void onDestroy()
    {
        super.onDestroy();
        //ibd.close();
    }
}
