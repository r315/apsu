package io.i2c.apsu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class Input {
	Context context;
	private int result[];
	
	public int[] getInput(){		
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.prompts, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);		
		alertDialogBuilder.setView(promptsView);
		final EditText userInput0 = (EditText) promptsView.findViewById(R.id.input_min_voltage);
		final EditText userInput1 = (EditText) promptsView.findViewById(R.id.input_dis_current);
		userInput0.setText("0");
		userInput1.setText("0");
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {				
				result[0] = Integer.parseInt(userInput0.getText().toString());
				result[1] = Integer.parseInt(userInput1.getText().toString());
			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  });		
		AlertDialog alertDialog = alertDialogBuilder.create();		
		alertDialog.show();
	return result;
	}
	
	public Input(Context main){
		context = main;
		result = new int[2];
	}
}
