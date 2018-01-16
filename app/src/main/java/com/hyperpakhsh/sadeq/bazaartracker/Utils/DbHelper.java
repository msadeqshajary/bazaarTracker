package com.hyperpakhsh.sadeq.bazaartracker.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static String TABLE_REGIONS = "regions";
    private static String REGIONS_ID = "id";
    private static String REGIONS_NAME = "name";

    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, "bazzarDb", null, 1);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRegionsTable = "CREATE TABLE "+TABLE_REGIONS+"("+REGIONS_ID+" integer,"+REGIONS_NAME+")";
        db.execSQL(createRegionsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertRegions(NameValueItem[] items){

        for(NameValueItem item : items){
            ContentValues values = new ContentValues();
            values.put(REGIONS_ID,item.getId());
            values.put(REGIONS_NAME,item.getName());
            db.insert(TABLE_REGIONS,null,values);
        }

    }

    public boolean isRegionsExists(){
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_REGIONS,null);
        int count = c.getCount();
        c.close();
        return count>0;
    }

    public NameValueItem[] getRegions(){
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_REGIONS,null);
        if(c.getCount()>0) {
            c.moveToFirst();
            NameValueItem[] items = new NameValueItem[c.getCount()];
            int i = 0;
            do {
                NameValueItem item = new NameValueItem();
                item.setId(String.valueOf(c.getInt(c.getColumnIndex(REGIONS_ID))));
                item.setName(c.getString(c.getColumnIndex(REGIONS_NAME)));
                items[i] = item;
                i++;
            } while (c.moveToNext());
            c.close();
            return items;
        }else return null;
    }

}
