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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * 
 * @author Ruben Bermudez
 * @author Santiago Tapia
 * @author Isaac Morely
 * 
 * @brief
 *  This class contains all the methods needed by our application to access the database.
 *  
 * @see DatabaseHelper
 * @see android.database.sqlite.SQLiteDatabase
 *
 */
public class AdaptadorBD {
	public static final String KEY_IDFILA = "_id"; //< Name of the first attribute of our table
	public static final String KEY_NOMBRE = "nombre"; //< Name of the second attribute of our table
	public static final String KEY_EMAIL = "email"; //< Name of the third attribute of our table
	private static final String TABLA_BASEDATOS = "contactos"; //< Name of our table
	private final Context context; //< Context of the DB
	private DatabaseHelper DBHelper; //< Manager to allow manipulation of the DB
	private SQLiteDatabase db; //<  Instance of a BD
	
	/**
	 * @brief  Constructor
	 * 		Initialise the context and the Manager
	 */
	public AdaptadorBD(Context ctx){
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	/**
	 * @brief Open the database 
	 * @return Itself to allow other calls to method after open the db
	 * @throws SQLException If any problem oppening the db
	 */
	public AdaptadorBD open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	/**
	 * @brief Close the database
	 */
	public void close(){
		DBHelper.close();
	}
	
	/**
	 * @brief Add a new contact to our database
	 * @param name Name of the contact
	 * @param email Email of the contact
	 * @return ID of the new registry created when adding
	 */
	public long addContact(String name, String email) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOMBRE, name);
		initialValues.put(KEY_EMAIL, email);
		return db.insert(TABLA_BASEDATOS, null, initialValues);
	}
	
	/**
	 * @brief Remove a contact
	 * @param name Name of the contact to remove
	 * @return True if the contact have been removed
	 */
	public boolean removeContactByName(String name){
		return db.delete(TABLA_BASEDATOS, KEY_NOMBRE + " = ?", new String[] { String.valueOf(name)}) > 0;		
	}
	
	/**
	 * @brief Find all name of the contacts
	 * @return Cursor for the list of names retrieved from the database
	 * @see android.database.Cursor
	 */
	public Cursor getAllNames(){
		String query = "SELECT " + KEY_NOMBRE + " FROM " + TABLA_BASEDATOS;
		Log.e("MyDataBase", query);
		Cursor mCursor = db.rawQuery(query, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
	
	/**
	 * @brief Get all the contacts with their emails
	 * @return Cursor for the list of contacts.
	 * @see android.database.Cursor
	 */
	public Cursor obtenerTodosLosContactos(){
		return db.query(TABLA_BASEDATOS, new String[] {KEY_IDFILA, KEY_NOMBRE, KEY_EMAIL}, null, null, null, null, null);
	}
	
	/**
	 * @brief Get a contact by its ID
	 * @param idFila ID of the contact
	 * @return Cursor for this contact
	 * @throws SQLException If the contact could not be found
	 * @see android.database.Cursor
	 */
	public Cursor obtenerContacto(long idFila) throws SQLException {
		Cursor mCursor = db.query(true, TABLA_BASEDATOS, new String[] {KEY_IDFILA, KEY_NOMBRE, KEY_EMAIL}, KEY_IDFILA + "=" + idFila, null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		
		return mCursor;
	}

	/**
	 * @brief Get the email of a contact given its name
	 * @param name Name of the contact
	 * @return Cursor for this email
	 * @see @see android.database.Cursor
	 */
	public Cursor getEmailByName(String name) {
		Cursor mCursor = db.query(TABLA_BASEDATOS, new String[] { KEY_EMAIL }, KEY_NOMBRE + "=?", new String[] { String.valueOf(name) }, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
		
	}	
}