package io.i2c.apsu;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ApsuMain extends Activity {
	final Context context = this;
	private Psu psu;	 
	public Timer dro_timer;
	private TextView volt_value, amp_value, power_value;
	private TextView info_time, info_cset, info_vtarget;	
	private Button test, hold_mesure, en_load, emergency ;	
	private Button increase, decrease, en_volt, en_cur;
	private Button preset;
	private Watch counter; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apsu_main);		
		
		volt_value = (TextView)findViewById(R.id.dro_volt_value);
		amp_value = (TextView)findViewById(R.id.dro_amp_value);
		power_value = (TextView)findViewById(R.id.dro_pw_value);
		info_time = (TextView)findViewById(R.id.info_time);		
		info_cset = (TextView)findViewById(R.id.target_current);
		info_vtarget = (TextView)findViewById(R.id.cut_voltage);	
		
		hold_mesure = (Button) findViewById(R.id.menu_hold);
		en_load = (Button) findViewById(R.id.menu_load);
		increase = (Button) findViewById(R.id.ui_increase);
		decrease = (Button) findViewById(R.id.ui_decrease);
		en_volt = (Button) findViewById(R.id.control_volt_active);
		en_cur = (Button) findViewById(R.id.control_cur_active);
		emergency = (Button) findViewById(R.id.menu_emergency);
		test = (Button) findViewById(R.id.menu_test_hw);
		preset = (Button)findViewById(R.id.input_set);
		((ToggleButton)en_volt).setChecked(true);
		
		setButtonFunctions();
		psu = new Psu();
		initBus();
		dro_timer = new Timer();
		dro_timer.schedule(new LoopTask(),10,40);
		counter = new Watch();		
		counter.start();		
	}  
	
	@Override 
    public void onDestroy()
    {
        super.onDestroy();
        psu.ibd.close();
    }
	
	public void initBus(){		
		if(!psu.ibd.hasRoot())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setTitle("Error");
	        builder.setMessage("Device not rooted!");
	        builder.setCancelable(false);
	        builder.setNegativeButton("ok",new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();	               
	            }
	        });
	        AlertDialog alert = builder.create();
	        alert.show();					
		}
		psu.ibd.open();
	}
	
	private void setButtonFunctions(){	
		
		test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {            	
            	Intent testmenu = new Intent(getBaseContext(), TestHw.class);            	
            	if( dro_timer != null){
            		dro_timer.cancel();
            		dro_timer = null;            		
            	}
            	((ToggleButton)hold_mesure).setChecked(true);
            	startActivity(testmenu);            	
            }
        }); 		
			
		hold_mesure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(!((ToggleButton)v).isChecked()) {
            		if(dro_timer != null)return;            	
                	dro_timer = new Timer();               	
                	dro_timer.schedule(new LoopTask(), 10,10);	
            	}
            	else{
            		if(dro_timer == null)return;
            		dro_timer.cancel();
            		dro_timer = null;
            	}
            }
        });		
		
		en_load.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {	
            	if(!((ToggleButton)v).isChecked()) {
            		psu.loadEnable(false);
            	}else{
            		psu.loadEnable(true);
            		en_cur.callOnClick();            		
            	}            		
            }
        });			
		
		increase.setOnTouchListener(new View.OnTouchListener() {			
			Timer holding;		
			public boolean onTouch(View v, MotionEvent event) {				   
				switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					((Button)v).setPressed(true);
					holding = new Timer();
					HoldTask holdtask = new HoldTask();
					holding.schedule(holdtask, 450, 50);
					holdtask.run();
					return true;
				case MotionEvent.ACTION_UP:
					((Button)v).setPressed(false);
					holding.cancel();
					holding = null;
					return true;
				default: return false;
				}
			}
			class HoldTask extends TimerTask{
				@Override
				public void run() {					
					if(((ToggleButton)en_cur).isChecked()){
						psu.changeCurrent(1);
					}else{
						psu.changeVoltage(1);
					}							
				}				
			}

		});
		
		decrease.setOnTouchListener(new View.OnTouchListener() {			
			Timer holding;		
			public boolean onTouch(View v, MotionEvent event) {				   
				switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					((Button)v).setPressed(true);
					holding = new Timer();
					HoldTask holdtask = new HoldTask();
					holding.schedule(holdtask, 450, 50);
					holdtask.run();
					return true;
				case MotionEvent.ACTION_UP:
					((Button)v).setPressed(false);
					holding.cancel();
					holding = null;
					return true;
				default: return false;
				}
			}
			class HoldTask extends TimerTask{
				@Override
				public void run() {
					if(((ToggleButton)en_cur).isChecked()){
						psu.changeCurrent(-1);
					}else{
						psu.changeVoltage(-1);
					}		
				}				
			}

		});			
		
		en_volt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(psu.isLoadEnable()){
            		((ToggleButton)v).setChecked(false);
            		return;
            	}            	
            	((ToggleButton)v).setChecked(true);
            	((ToggleButton)en_cur).setChecked(false);            	
            }
        }); 
		
		en_cur.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  
            	((ToggleButton)v).setChecked(true);
            	((ToggleButton)en_volt).setChecked(false);               	       	
            }
        });		
		
		emergency.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  
            	psu.stop();
            }
        });	
		
		preset.setOnClickListener(new View.OnClickListener() {
			Input inp;
            public void onClick(View v) {  
            	      inp = new Input(context);
            	      int vals[] = inp.getInput();
            	      psu.setVoltage(vals[0]);
            	      psu.setCurrent(vals[1]);
            }
        });		
	} 
	
	
	class LoopTask extends TimerTask{		
		@Override
		public void run() {
			runOnUiThread(new Runnable(){
				@Override
				public void run() {					
					volt_value.setText(String.format("%.1fv",psu.getVoltage()));
					amp_value.setText(String.format("%.0fmA",psu.getCurrent()));					   
					power_value.setText(String.format("%.1fw",psu.getPower()));					
					info_cset.setText(String.format("%.0fmA",psu.getIlimit()));
					info_vtarget.setText(String.format("%.1fv",psu.getVlimit()));
					info_time.setText(counter.toString());
				}});		
		}
	}
}
