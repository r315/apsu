package io.i2c.apsu;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class Graph extends View{

	@Override
	public void onDraw(Canvas canvas) {
		  super.onDraw(canvas);        
	     /*   for (int x = 0; x < mXTileCount; x += 1) {
	            for (int y = 0; y < mYTileCount; y += 1) {
	                if (mTileGrid[x][y] > 0) {
	                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], 
	                    		mXOffset + x * mTileSize,
	                    		mYOffset + y * mTileSize,
	                    		mPaint1); 
	                }
	            }
	        }*/ 
	}
	@Override 
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
	}
	public Graph(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
	}
	
	 

}
