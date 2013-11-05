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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 
 * @author Ruben Bermudez
 * @author Santiago Tapia
 * @author Isaac Morely
 * 
 * @brief
 *  This class (extended from Activity) allow to remove contact from a database
 *  
 * @see android.app.Activity
 */
public class RemoveContact extends Activity {
	private AdaptadorBD dataBase; //< Database
	private Button removeButton; //< Button
	private Spinner names; //< Spinner to select the name to remove
	@Override
	/**
	 * @name onCreate
	 * @brief Method called when creating the view
	 * @note
	 * 	This method initialises the view and set a listener on the button to remove a contact.
	 *  It remove the contact that is selected on the spinner.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
 	 * @see android.widget.EditText
	 * @see android.widget.Toast
	 * @see android.widget.Spinner
	 * @see android.database.Cursor
	 * @see android.widget.ArrayAdapter
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_contact);
		dataBase = new AdaptadorBD(getBaseContext());
		removeButton = (Button) findViewById(R.id.RemoveButton);
		names = (Spinner) findViewById(R.id.RemoveList);
		removeButton.setEnabled(false);
		List<String> nameList = new ArrayList<String>();
		try {
			dataBase.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Cursor namesCursor = dataBase.getAllNames();
		dataBase.close();
		if (namesCursor != null){
			if (namesCursor.getCount() > 0)
				removeButton.setEnabled(true);
			while (!namesCursor.isAfterLast()){
				nameList.add(namesCursor.getString(0));
				namesCursor.moveToNext();
			}
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		names.setAdapter(dataAdapter);
        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String nameToRemove = String.valueOf(names.getSelectedItem());
        		try {
        			dataBase.open();
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		boolean result = dataBase.removeContactByName(nameToRemove);
        		dataBase.close();
            	if (result){
            		Toast.makeText(RemoveContact.this,"Contact has been removed",Toast.LENGTH_SHORT).show();
            		RemoveContact.this.finish();
            	}
            	else {
            		dataBase.close();
            		Toast.makeText(RemoveContact.this,"Error: Contact could not be removed",Toast.LENGTH_SHORT).show();
            	}
            	
            }
        });
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

