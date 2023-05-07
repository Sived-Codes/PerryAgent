package com.perry.jasus.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
//static keyword use for runtime memory allocation
    private static final String DB_Name = "User_Information";

    private static final String DB_version= "1";

    private static final String Table_Name = "User";

    private static final String CALL_TYPE = "call_type";

    private static final String CALL_NUMBER = "call_number";

    private static final String CALL_TIME = "call_time";





    public DatabaseHelper(Context context){
        super(context, DB_Name ,null, Integer.parseInt(DB_version));
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String Query = "CREATE TABLE " + Table_Name + " ( "
                + CALL_TYPE + " TEXT,"
                + CALL_NUMBER + " TEXT,"
                + CALL_TIME + " INTEGER)";
        sqLiteDatabase.execSQL(Query);
    }



   public  void addCallDetails(String callType, String callNum, String callTime){

        SQLiteDatabase Data = this.getWritableDatabase();
       ContentValues values = new ContentValues();

       values.put(CALL_TYPE, callType);
       values.put(CALL_NUMBER,callNum);
       values.put(CALL_TIME, callTime);

       Data.insert(Table_Name,null,values);
       Data.close();
   }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       sqLiteDatabase .execSQL(" DROP TABLE "+Table_Name);
       onCreate(sqLiteDatabase);
    }
}
