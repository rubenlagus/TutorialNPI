/**
 *   @license Copyright 2013 Ruben Bermudez, Santiago Tapia e Isaac Moreli
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * 
 * @author Ruben Bermudez
 * @author Santiago Tapia
 * @author Isaac Morely
 * 
 * @brief
 *  This class (extended from Activity) allow to read contact from a database
 *  
 * @see android.app.Activity
 */
public class ReadContact extends Activity {
	private AdaptadorBD dataBase; //< Database
	private Spinner names; //< spinner of the view to select the name of the contact
	private EditText email; //< Textview to show the email
	@Override
	/**
	 * @name onCreate
	 * @brief Method called when creating the view
	 * @note
	 * 	This method initialises the view and set a listener on the spinner.
	 *  It also show the contact when it is selected on the spinner.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
 	 * @see android.widget.EditText
	 * @see android.widget.Spinner
	 * @see android.database.Cursor
	 * @see android.widget.ArrayAdapter
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_contact);
		dataBase = new AdaptadorBD(getBaseContext());
		names = (Spinner) findViewById(R.id.ReadList);
		email = (EditText) findViewById(R.id.ConsultEmail);
		List<String> nameList = new ArrayList<String>();
		try {
			dataBase.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Cursor namesCursor = dataBase.getAllNames();
		dataBase.close();
		if (namesCursor != null){
			while (!namesCursor.isAfterLast()){
				nameList.add(namesCursor.getString(0));
				namesCursor.moveToNext();
			}
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		names.setAdapter(dataAdapter);
		names.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					dataBase.open();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Cursor emails = dataBase.getEmailByName(String.valueOf(names.getSelectedItem()));
				dataBase.close();
				email.setText(emails.getString(0));	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
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
