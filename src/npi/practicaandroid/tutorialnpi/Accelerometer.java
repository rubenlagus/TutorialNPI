/**
 *   Copyright 2013 Ruben Bermudez, Santiago Tapia and Isaac Morely
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
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;

/**
 * 
 * @author Ruben Bermudez
 * @author Santiago Tapia
 * @author Isaac Morely
 * 
 * @brief
 *  This class (extended from Activity) implements a Sensor listener above the accelerometer in order
 *  to determine when the device is moved and also the direction (vertical or horizontal)
 *  
 * @see android.app.Activity
 * @see android.hardware.SensorEventListener
 */
public class Accelerometer extends Activity implements SensorEventListener {
	
	private float oldx,oldy,oldz; //< Previous values of accelerometer feedback
	private boolean modified = false; //< False until the first measure of the accelerometer is perform
	private SensorManager mSensorManager; //< Manager of the sensor of the device
	private Sensor mAccelerometer; //< Manager of the accelerometer sensor
	private boolean initialization = true; //< true if no movement have taken place
	private final float NOISE = (float) 1.0; //< Threshold to ignore noise
	private int oldorientation; //< old orientation of the device
	
	/**
	 * @name onCreate
	 * @brief Method called when creating the view
	 * @note
	 * 	This method initialises the value of oldorientation with vertical orientation, set the view to 
	 * a loyaut and get the accelerometer sensor. If this sensor doesn't exists, a expecial loyaout is
	 * loaded to the view.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		oldorientation = Surface.ROTATION_0;
		setContentView(R.layout.activity_accelerometer_otro);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0)
			setContentView(R.layout.activity_accelerometer_nothing);
    	else {
    		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	}
		
	}
	
	/**
	 * @name onResume
	 * @brief  This method is called always before show the view, so it is the right place to start listening
	 * 		the sensor.
	 * @note Starting here, we can save battery just using onPause to stop the listening. 
	 * 
	 * @see android.app.Activity#onRsume()
	 */
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/**
	 * @name  onPause
	 * @brief This method is called just when the view is not visible for the user (it doesn't mean that
	 * 		the activity is going to be destroyed.
	 * @note The sensor listener is stopped to save battery
	 * 
	 * @see android.app.Activity#onPause()
	 */
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * @name onSensorChanged
	 * @brief This method is called each time the sensor values change (in this case, when the device
	 * 		is moved)
	 * The new values are read and the differences with the previous ones are  computed (if this
	 *    differences are smaller than the threshold, we assume it is noise and no movement have
	 *    performed). Finally we get the current orientation of the device and depending on it,
	 *    we load the correct layout to the view.
	 * @note The first time this method is called, only the values are read, because we don't have
	 *    previous data to find the kind of movement performed.    
	 * 	
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 * @see android.view.Display
	 * @see android.view.Surface
	 */
	public void onSensorChanged(SensorEvent event) {
		if (modified) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			float deltaX = Math.abs(oldx - x);
			float deltaY = Math.abs(oldy - y);
			float deltaZ = Math.abs(oldz - z);
			
			if (deltaX < NOISE) deltaX = (float)0.0;
			if (deltaY < NOISE) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			
			oldx = event.values[0];
			oldy = event.values[1];
			oldz = event.values[2];
			
			Display getOrient = getWindowManager().getDefaultDisplay();
			int orientation = getOrient.getRotation();
			if (orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180 ){
				if (oldorientation == Surface.ROTATION_90 || oldorientation == Surface.ROTATION_270){
					initialization = true;
				}
				oldorientation = orientation;
				if (deltaX > deltaY) {
					setContentView(R.layout.activity_accelerometer_horizontal);
				} else if (deltaY > deltaX) {
					setContentView(R.layout.activity_accelerometer_vertical);
				}
				else {
					if (initialization){
						setContentView(R.layout.activity_accelerometer_otro);
						initialization = false;
					}
				}
			}
			else{
				if (oldorientation == Surface.ROTATION_0 || oldorientation == Surface.ROTATION_180){
					initialization = true;
				}
				oldorientation = orientation;
				if (deltaX > deltaY) {
					setContentView(R.layout.activity_accelerometer_vertical);
				} else if (deltaY > deltaX) {
					setContentView(R.layout.activity_accelerometer_horizontal);
				}
				else {
					if (initialization){
						setContentView(R.layout.activity_accelerometer_otro);
						initialization = false;
					}
				}
			}
		}
		else {
			oldx = event.values[0];
			oldy = event.values[1];
			oldz = event.values[2];
			
			modified = true;
		}
	}
}

