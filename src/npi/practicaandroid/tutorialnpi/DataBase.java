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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @author Ruben Bermudez
 * @author Santiago Tapia
 * @author Isaac Morely
 * 
 * @brief
 *  This class (extended from Activity) allows to choose an action to perform in the database
 *  
 * @see android.app.Activity
 */
public class DataBase extends Activity {

	@Override
	/**
	 * @name onCreate
	 * @brief Method called when creating the view
	 * @note
	 * 	This method initialises view and add a listener to each button in the view.
	 *  When a button is clicked, the corresponding activity is started.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @see android.widget.Button
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_base);
		
		final Button addContactButton = (Button) findViewById(R.id.AddContact);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		Class<?> clazz = Class.forName("npi.practicaandroid.tutorialnpi.AddContact");
            		Intent intent = new Intent(getBaseContext(), clazz);
            		startActivity(intent);
            	}
            	catch (ClassNotFoundException e) {
            		e.printStackTrace();
            	}
            }
        });
        
        final Button removeContactButton = (Button) findViewById(R.id.RemoveContact);
        removeContactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		Class<?> clazz = Class.forName("npi.practicaandroid.tutorialnpi.RemoveContact");
            		Intent intent = new Intent(getBaseContext(), clazz);
            		startActivity(intent);
            	}
            	catch (ClassNotFoundException e) {
            		e.printStackTrace();
            	}
            }
        });
        
        final Button readContactButton = (Button) findViewById(R.id.ReadContact);
        readContactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		Class<?> clazz = Class.forName("npi.practicaandroid.tutorialnpi.ReadContact");
            		Intent intent = new Intent(getBaseContext(), clazz);
            		startActivity(intent);
            	}
            	catch (ClassNotFoundException e) {
            		e.printStackTrace();
            	}
            }
        });
	}
}
