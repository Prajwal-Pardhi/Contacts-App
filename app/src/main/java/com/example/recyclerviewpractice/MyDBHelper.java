package com.example.recyclerviewpractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactsDB";  //database name
    private static final int DATABASE_VERSION = 1;             //database version
    private static final String TABLE_CONTACT = "contacts";     //table name

    private static final String KEY_ID = "id";              //column 1
    private static final String KEY_NAME = "name";          //column 2
    private static final String KEY_PHONE_NO = "phone_no";  //column 3

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL Query: CREATE TABLE contacts (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone_no TEXT)
        db.execSQL("CREATE TABLE "+TABLE_CONTACT+"("+KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_NAME + " TEXT," + KEY_PHONE_NO + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACT);
        onCreate(db);
    }

    //Add Data function
    public void addContact(String name, String phone_no){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME,name);
        values.put(KEY_PHONE_NO, phone_no);

        db.insert(TABLE_CONTACT,null,values);

        //HOW TO ADD DATA
        // MyDBHelper dbHelper = new MyDBHelper(this);
        //dbHelper.addContact(name_string,phone_number_string);
    }

    //Fetching data in ArrayList form
    public ArrayList<ContactModel> fetchContact(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ContactModel> arrContacts = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_CONTACT,null);

        while (cursor.moveToNext()){

            ContactModel model = new ContactModel();
            model.id = cursor.getInt(0);
            model.name = cursor.getString(1);
            model.number = cursor.getString(2);

            arrContacts.add(model);

        }

        return arrContacts;
    }

    //Update Data function
    public void updateContact(ContactModel contactModel, String oldNumber){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME,contactModel.name);
        cv.put(KEY_PHONE_NO,contactModel.number);

        db.update(TABLE_CONTACT,cv ,KEY_PHONE_NO+" = "+oldNumber,null);


    }

    //Delete Data function
    public void deleteContact(String number){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CONTACT,KEY_PHONE_NO+" = ?",new String[]{number});

    }

}
