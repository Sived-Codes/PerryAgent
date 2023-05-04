package com.perry.jasus.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.perry.jasus.Models.SMS_Model;

import java.util.ArrayList;

public class SMS_Database extends SQLiteOpenHelper {

    private static final String DB_Name = "SMS_Information";

    private static final String DB_version= "1";

    private static final String Table_Name = "SMS";

    private static final String SMS_TEXT = "sms_type";

    private static final String SMS_NUMBER = "sms_number";

    private static final String SMS_TIME = "sms_time";



    public SMS_Database(Context context) {
        super(context, DB_Name, null, 1);

    }
        @Override
        public void onCreate (SQLiteDatabase db){
            String Query = "CREATE TABLE " + Table_Name + " ( "
                    + SMS_TEXT + " TEXT,"
                    + SMS_NUMBER + " TEXT,"
                    + SMS_TIME + " INTEGER)";
            db.execSQL(Query);

        }
    public  void addSMSDetails(String smsType, String smsNum, String smsTime){

        SQLiteDatabase Data = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SMS_TEXT, smsType);
        values.put(SMS_NUMBER,smsNum);
        values.put(SMS_TIME, smsTime);

        Data.insert(Table_Name,null,values);
        Data.close();
    }
    public ArrayList<SMS_Model> SMSDetail(){

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor= db.rawQuery(" SELECT * FROM "+ Table_Name,null);

        ArrayList<SMS_Model>sList=new ArrayList<>();

        if (cursor.moveToNext()){
            do{
                sList.add(new SMS_Model(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return sList;

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db .execSQL(" DROP TABLE "+Table_Name);
        onCreate(db);
    }
}
