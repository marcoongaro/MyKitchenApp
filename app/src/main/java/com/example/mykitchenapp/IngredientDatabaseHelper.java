package com.example.mykitchenapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class IngredientDatabaseHelper {

    private static final String TAG = IngredientDatabaseHelper.class.getSimpleName();

    // database configuration
    // if you want the onUpgrade to run then change the database_version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydatabase.db";

    // table configuration
    private static final String TABLE_NAME = "ingredient_table";         // Table name
    private static final String INGREDIENT_TABLE_COLUMN_ID = "_id";     // a column named "_id" is required for cursor
    private static final String INGREDIENT_TABLE_COLUMN_NAME = "ingredient_name";   //nome ingrediente
    private static final String INGREDIENT_TABLE_COLUMN_QUANTITY = "ingredient_quantity";    //quantità
    private static final String INGREDIENT_TABLE_COLUMN_DATE = "ingredient_date";     //data di scadenza
    private static final String INGREDIENT_TABLE_COLUMN_DATESORT = "ingredient_datesort";    //data di scadenza per ordinamento (YEAR,MONTH,DAY)
    private static final String INGREDIENT_TABLE_COLUMN_UNITY = "ingredient_unity";   //unità di misura
    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;

    // this is a wrapper class. that means, from outside world, anyone will communicate with IngredientDatabaseHelper,
    // but under the hood actually DatabaseOpenHelper class will perform database CRUD operations

    public IngredientDatabaseHelper(Context aContext) {
        openHelper = new DatabaseOpenHelper(aContext);
        database = openHelper.getWritableDatabase();
    }

    public void insertData (String aIngredientName, String aIngredientQuantity, String aIngredientDate, String aIngredientDatesort, String aIngredientUnity) {

        // we are using ContentValues to avoid sql format errors
        database = openHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(INGREDIENT_TABLE_COLUMN_NAME, aIngredientName);
        contentValues.put(INGREDIENT_TABLE_COLUMN_QUANTITY, aIngredientQuantity);
        contentValues.put(INGREDIENT_TABLE_COLUMN_DATE, aIngredientDate);
        contentValues.put(INGREDIENT_TABLE_COLUMN_DATESORT, aIngredientDatesort);
        contentValues.put(INGREDIENT_TABLE_COLUMN_UNITY, aIngredientUnity);

        database.insert(TABLE_NAME, null, contentValues);

        //database.close();
    }

    public Cursor getAllData () {

        String buildSQL = "SELECT * FROM " + TABLE_NAME;

        Log.d(TAG, "getAllData SQL: " + buildSQL);

        return database.rawQuery(buildSQL, null);
    }

    //function to search in database

    public Cursor getSearchData (String name) {

        return database.query(TABLE_NAME, new String[]{INGREDIENT_TABLE_COLUMN_ID, INGREDIENT_TABLE_COLUMN_NAME, INGREDIENT_TABLE_COLUMN_QUANTITY, INGREDIENT_TABLE_COLUMN_DATE, INGREDIENT_TABLE_COLUMN_DATESORT, INGREDIENT_TABLE_COLUMN_UNITY}, INGREDIENT_TABLE_COLUMN_NAME + "=? COLLATE NOCASE", new String[]{name}, null, null, null, null);

    }

    //function to order data by date

    public Cursor getorderedData () {

        return database.query(TABLE_NAME, new String[] { INGREDIENT_TABLE_COLUMN_ID, INGREDIENT_TABLE_COLUMN_NAME, INGREDIENT_TABLE_COLUMN_QUANTITY, INGREDIENT_TABLE_COLUMN_DATE, INGREDIENT_TABLE_COLUMN_DATESORT, INGREDIENT_TABLE_COLUMN_UNITY }, null, null , null, null, INGREDIENT_TABLE_COLUMN_DATESORT);

    }

    //function to delete data from database

    public boolean deleteData(long rowId) {
        database = openHelper.getWritableDatabase();
        try {
            String selection =  INGREDIENT_TABLE_COLUMN_ID + "=" + String.valueOf(rowId);
            Log.d(TAG, "rowId: " + rowId);
            Log.d(TAG, "selection: " + selection);
            if((database.delete(TABLE_NAME, selection, null)>0)){
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLiteException sqle)
        {
            //database.close();
            return false;
        }
    }

    //function to update data from database

    public int updateData(long rowId, String aIngredientQuantity){
        database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INGREDIENT_TABLE_COLUMN_QUANTITY, aIngredientQuantity);
        // Which row to update, based on the ID
        String selection = INGREDIENT_TABLE_COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowId) };
        return database.update(TABLE_NAME, values, selection, selectionArgs);
    }

    // this DatabaseOpenHelper class will actually be used to perform database related operation

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context aContext) {
            super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            // Create your tables here

            String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " + INGREDIENT_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    INGREDIENT_TABLE_COLUMN_NAME + " TEXT, " + INGREDIENT_TABLE_COLUMN_QUANTITY + " TEXT, " + INGREDIENT_TABLE_COLUMN_DATE + " TEXT, " + INGREDIENT_TABLE_COLUMN_DATESORT + " TEXT, " + INGREDIENT_TABLE_COLUMN_UNITY + " TEXT )";

            Log.d(TAG, "onCreate SQL: " + buildSQL);

            sqLiteDatabase.execSQL(buildSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            // Database schema upgrade code goes here

            String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

            Log.d(TAG, "onUpgrade SQL: " + buildSQL);

            sqLiteDatabase.execSQL(buildSQL);       // drop previous table

            onCreate(sqLiteDatabase);               // create the table from the beginning
        }
    }
}