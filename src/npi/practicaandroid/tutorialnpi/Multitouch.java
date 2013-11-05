/**
 *   @license Copyright 2013 Ruben Bermudez, Santiago Tapia e Isaac Morely
 *   
 *   This file is part of TutorialNPI.
 *
 *   TutorialNPI is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   TutorialNPI is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with TutorialNPI.  If not, see <http://www.gnu.org/licenses/>.
 */
package npi.practicaandroid.tutorialnpi;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/**
 * 
 * @author Ruben Bermudez
 * @author Santiago Tapia
 * @author Isaac Morely
 * 
 * @brief
 *  This class (extended from Activity) implements a Touch listener above the view in order
 *  to determine when the screen is touched and how many finger are there on it.
 *  
 * @note Even this application support up to 10 fingers, this number depend on the device. Moreover,
 *  one finger that touch on 2 different moment the screen can receive a diferent ID each time. 
 * @see android.app.Activity
 * @see android.view.View.OnTouchListener
 */
public class Multitouch extends Activity implements OnTouchListener {
	StringBuilder builder = new StringBuilder(); //< String builder
	TextView textView; //< Text view to show the number of fingers
	
	float[] x = new float[10]; //< Position on the x axis of each finger
	float[] y = new float[10]; //< Position on the y axis of each finger
	boolean[] tocado = new boolean[10]; //< When a finger  is touching or not
	
	/**
	 * @name updateTextView
	 * @brief Update the number show on the display 
	 * It count how many finger are touching the screen at a moment and update the textview it.
	 */
	private void updateTextView(){
		
		int number = 0;
		builder.setLength(0);
		for (int i=0;i<10;i++){
			if (tocado[i])
				number++;
		}
		builder.append(number);
		textView.setText(builder.toString());
	}
	
	/**
	 * @name onCreate
	 * @brief Method called when creating the view
	 * @note
	 * 	This method initialises the view and get a reference to the textview that should be modified.
	 *  It also set a ontouchlistener on the view itself.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multitouch);
		textView = new TextView(this); 
		textView = (TextView) findViewById(R.id.dedostext);
		View v= (View) findViewById(R.id.Multitouchact);
		v.setOnTouchListener(this);
	}
	
	@Override
	/**
	 * @name onTouch 
	 * @brief Called each time the screen is touched or untouched (event on the screen)
	 * It find the id of the new finger (here pointer) and compute it position. It also determine if the
	 *  event was put a finger on the screen or take it away from it. Finally it calls to updateTextView
	 *  to show any changes
	 * @note This method is also called when moving a finger above the screen  
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		int pointerId = event.getPointerId(pointerIndex);
		
		switch(action){
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			tocado[pointerId] = true;
			x[pointerId] = (int)event.getX(pointerIndex);
			y[pointerId] = (int)event.getY(pointerIndex);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL:
			tocado[pointerId] = false;
			x[pointerId] = (int)event.getX(pointerIndex);
			y[pointerId] = (int)event.getY(pointerIndex);
			break;
		case MotionEvent.ACTION_MOVE:
			int pointerCount = event.getPointerCount();
			for (int i = 0; i<pointerCount;i++){
				pointerIndex = i;
				pointerId = event.getPointerId(pointerIndex);
				x[pointerId] = (int)event.getX(pointerIndex);
				y[pointerId] = (int)event.getY(pointerIndex);
			}
			break;
		}

		updateTextView();
		
		return true;
	}

}
