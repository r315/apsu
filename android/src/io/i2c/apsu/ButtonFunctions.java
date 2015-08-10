package io.i2c.apsu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;


public class ButtonFunctions {
	Context context;
	Activity main;	
	Button test;
	Button set;
	
	public void setButtonListners(){		
		set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {            	
            	Input ip = new Input(context);
            	ip.getInput();
            }
        });		
	}	
	
	public ButtonFunctions(Context ctx){
		context = ctx;
		main = (Activity) ctx;		
		set = (Button) main.findViewById(R.id.input_set);
		
	}
}
