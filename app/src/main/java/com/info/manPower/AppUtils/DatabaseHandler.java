package com.info.manPower.AppUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static String DB_NAME = "manpower";
    private static int DB_VERSION = 1;

    private SQLiteDatabase db;

    public static final String CART_TABLE = "cart";

    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_CAT_ID = "category_id";
    public static final String COLUMN_SCAT_ID = "subcat_id";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_NAME = "subcat_name";
    public static final String COLUMN_DFROM = "date_from";
    public static final String COLUMN_DTO = "date_to";
    public static final String COLUMN_TFROM = "time_from";
    public static final String COLUMN_TTO = "time_to";
    public static final String COLUMN_WORK = "work_details";
    public static final String COLUMN_QTY = "num_helpers";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_UNIT_RATE = "unit_value";

    public DatabaseHandler(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String ctable = "CREATE TABLE IF NOT EXISTS "+CART_TABLE
                + "(" + COLUMN_UID + "  integer PRIMARY KEY AUTOINCREMENT not null , "
                + COLUMN_SCAT_ID + " TEXT NOT NULL, "
                + COLUMN_CAT_ID + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_QTY + " integer NOT NULL,"
                + COLUMN_IMAGE + " TEXT NOT NULL, "
                + COLUMN_UNIT_RATE + " DOUBLE NOT NULL, "
                + COLUMN_DFROM + " TEXT NOT NULL, "
                + COLUMN_DTO + " TEXT NOT NULL, "
                + COLUMN_TFROM + " TEXT NOT NULL, "
                + COLUMN_TTO + " TEXT NOT NULL, "
                + COLUMN_WORK + " TEXT NOT NULL "
                +")";

        db.execSQL(ctable);
    }

    public boolean setCart(HashMap<String, String> map, int qty) {
        db = getWritableDatabase();
        if (isInCart(map.get("uid"))){
            db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + qty + "'  where " + COLUMN_UID + " = " + map.get("uid") );
//            db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + qty + "' AND " + COLUMN_RATE + "  =  ('" + qty + "' * " +  Integer.parseInt(map.get("unit_value")) + ") where " + COLUMN_UID + " = " + map.get("uid") );
            Log.e("UID is > ","_____________"+map.get("uid")+"  in Update Query");
            Log.e("DB : ",qty+" X  "+map.get("unit_value"));
            return false;
        }
        else{
            ContentValues values = new ContentValues();

            values.put(COLUMN_SCAT_ID, map.get(COLUMN_SCAT_ID));
            values.put(COLUMN_CAT_ID, map.get(COLUMN_CAT_ID));
            values.put(COLUMN_NAME, map.get(COLUMN_NAME));
            values.put(COLUMN_QTY, map.get(COLUMN_QTY));
            values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
            values.put(COLUMN_UNIT_RATE, map.get(COLUMN_UNIT_RATE));
            values.put(COLUMN_DFROM, map.get(COLUMN_DFROM));
            values.put(COLUMN_DTO, map.get(COLUMN_DTO));
            values.put(COLUMN_TFROM, map.get(COLUMN_TFROM));
            values.put(COLUMN_TTO, map.get(COLUMN_TTO));
            values.put(COLUMN_WORK, map.get(COLUMN_WORK));

            Log.e("DB INSERTION", "   " + values);
            db.insert(CART_TABLE, null, values);
            return true;
        }
    }


    public ArrayList<HashMap<String, String>> getCartAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_UID,cursor.getString(cursor.getColumnIndex(COLUMN_UID)));
            Log.e("COLUMN UID ","___"+cursor.getString(cursor.getColumnIndex(COLUMN_UID)));
            map.put(COLUMN_SCAT_ID, cursor.getString(cursor.getColumnIndex(COLUMN_SCAT_ID)));
            map.put(COLUMN_CAT_ID, cursor.getString(cursor.getColumnIndex(COLUMN_CAT_ID)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_UNIT_RATE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_RATE)));
            map.put(COLUMN_DFROM, cursor.getString(cursor.getColumnIndex(COLUMN_DFROM)));
            map.put(COLUMN_DTO, cursor.getString(cursor.getColumnIndex(COLUMN_DTO)));
            map.put(COLUMN_TFROM, cursor.getString(cursor.getColumnIndex(COLUMN_TFROM)));
            map.put(COLUMN_TTO, cursor.getString(cursor.getColumnIndex(COLUMN_TTO)));
            map.put(COLUMN_WORK,cursor.getString(cursor.getColumnIndex(COLUMN_WORK)));

            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public String getTotalAmount() {
        db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_QTY + " * " + COLUMN_UNIT_RATE + ") as total_amount  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndex("total_amount"));
        if (total != null) {

            return total;
        } else {
            return "0";
        }
    }

    public boolean isInCart(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_UID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;

        return false;
    }

    public int getCartCount() {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        return cursor.getCount();
    }

    public void clearCart() {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE);
    }

    public void removeItemFromCart(String id) {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE + " where " + COLUMN_SCAT_ID + " = " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
