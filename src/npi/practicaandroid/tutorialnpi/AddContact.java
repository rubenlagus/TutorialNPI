/**
 * 	 @license Copyright 2013 Ruben Bermudez, Santiago Tapia e Isaac Morely
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

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author Ruben Bermudez
 * @author Santiago Tapia
 * @author Isaac Morely
 * 
 * @brief
 *  This class (extended from Activity) allow to add a contact to a database
 *  
 * @see android.app.Activity
 */
@SuppressLint("DefaultLocale")
public class AddContact extends Activity {
	private AdaptadorBD dataBase; //< Database for the contact
	@Override
	/**
	 * @name onCreate
	 * @brief Method called when creating the view
	 * @note
	 * 	This method initialises the view and set a click listener on the button.
	 *  It also add the contact if everything is correct and the button 'save' is pushed.
	 *  It also inform by toasts when the operation have not been complete or when it is successful.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @see android.widget.Button;
 	 * @see android.widget.EditText
	 * @see android.widget.Toast
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		dataBase = new AdaptadorBD(getBaseContext());
		final EditText name = (EditText) findViewById(R.id.AddName);
		final EditText email = (EditText) findViewById(R.id.AddEmail);
		final Button saveButton = (Button) findViewById(R.id.AddSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String nameToSave = name.getText().toString();
            	String emailToSave = email.getText().toString();
            	switch(checkConstraints(nameToSave,emailToSave)){
            	case 0:
            		try {
            			dataBase.open();
            		} catch (SQLException e) {
            			e.printStackTrace();
            		}
            		if(dataBase.addContact(nameToSave, emailToSave) != -1){
            			dataBase.close();
            			Toast.makeText(AddContact.this,"Contact has been added",Toast.LENGTH_SHORT).show();
            			AddContact.this.finish();
            		}
            		else {
            			Toast.makeText(AddContact.this,"Contact could not be added",Toast.LENGTH_SHORT).show();
            			AddContact.this.finish();
            		}
            		break;
            	case 1:
            		Toast.makeText(AddContact.this,"Contact already exists",Toast.LENGTH_SHORT).show();
            		break;
            	case 2:
            		Toast.makeText(AddContact.this,"Contact name is empty",Toast.LENGTH_SHORT).show();
            		break;
            	case 3:
            		Toast.makeText(AddContact.this,"Contact email is empty",Toast.LENGTH_SHORT).show();
            		break;
            	default:
            		break;
            	}
            
            }
        });
	}
	
	/**
	 * @brief Check the constraints to add a new contact
	 * @param name Name of the contact
	 * @param email Email of the contact
	 * @return 0 if everything pass
	 *  	   1 if the contact already exists
	 *         2 if name is empty 
	 *         3 if email is empty
	 */
	@SuppressLint("DefaultLocale")
	private int checkConstraints(String name, String email){
		try {
			dataBase.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Cursor data = dataBase.getAllNames();
		dataBase.close();
		if (data != null && data.getCount()>0){
			do {
				if (data.getString(0).toLowerCase().equals(name.toLowerCase())){
					data.close();
					return 1;
				}
				} while (data.moveToNext());				
		}
		if (name.equals(""))
			return 2;
		if (email.equals(""))
			return 3;
		return 0;
	}
	
	/**
	 * @name  onPause
	 * @brief This method is called just when the view is not visible for the user (it doesn't mean that
	 * 		the activity is going to be destroyed.
	 * @note The database is closed to allow other activities to use it
	 * 
	 * @see android.app.Activity#onPause()
	 */
	protected void onPause(Bundle savedInstanceState) {
		super.onPause();
		dataBase.close();
	}

}

