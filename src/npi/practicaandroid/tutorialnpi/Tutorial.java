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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * @author Ruben Bermudez
 * @author Santiago Tapia
 * @author Isaac Morely
 * 
 * @brief Main Activity
 *  This class (extended from ListActivity) show the list of features that our application have.
 *  Choosing one of them launch an activity about it 
 *  
 * @see android.app.ListActivity
 */
public class Tutorial extends ListActivity {
	String tutoriales[] = {"Accelerometer","Multitouch","DataBase"}; //< List of features
	
    @Override
    /**
	 * @name onCreate
	 * @brief Method called when creating the view
	 * @note
	 * 	This method initialises list
	 * 
	 * @see android.app.ListActivity#onCreate(android.os.Bundle)
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tutoriales));
    }

    @Override
    /**
	 * @name onCreate
	 * @brief Method called when creating the view
	 * @note
	 * 	This method initialises launch the activity that match the option choosen on the screen
	 * 
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    protected void onListItemClick(ListView list,View view, int position, long id){
    	super.onListItemClick(list, view, position, id);
    	String nameTutorial = tutoriales[position];
    	try {
    		Class<?> clazz = Class.forName("npi.practicaandroid.tutorialnpi." + nameTutorial);
    		Intent intent = new Intent(this, clazz);
    		startActivity(intent);
    	}
    	catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    }
}
