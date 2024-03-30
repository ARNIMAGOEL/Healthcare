package com.example.healthcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "healthcare";
    private static final int DATABASE_VERSION = 1;

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create tables if they don't exist
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (username TEXT, email TEXT, password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cart (username TEXT, product TEXT, price REAL, otype TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS orderplace (username TEXT, fullname TEXT, address TEXT, contactno TEXT, pincode INTEGER, date TEXT, time TEXT, amount REAL, otype TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Upgrade the database if needed (not implemented in this example)
    }

    public void register(String username, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users", null, cv);
        db.close();
    }

    public int login(String username, String password) {
        int result = 0;
        String[] str = {username, password};
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", str);
        if (c.moveToFirst()) {
            result = 1;
        }
        c.close();
        db.close();
        return result;
    }

    public void addCart(String username, String product, float price, String otype) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("product", product);
        cv.put("price", price);
        cv.put("otype", otype);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("cart", null, cv);
        db.close();
    }
    public int checkCart(String username, String product) {
        int result = 0;
        String str[] = new String[2];
        str[0] = username;
        str[1] = product;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from cart where username = ? and product = ?", str);
        if (c.moveToFirst()) {
            result = 1;
        }
        db.close();
        return result;
    }

    public void removeCart(String username, String otype) {
        String str[] = new String[2];
        str[0] = username;
        str[1] = otype;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "username=? and otype=?", str);
        db.close();
    }

    public ArrayList getCartData(String username, String otype) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String str[] = new String[2];
        str[0] = username;
        str[1] = otype;
        Cursor c = db.rawQuery("select * from cart where username = ? and otype = ?", str);
        if (c.moveToFirst()) {
            do {
                String product = c.getString(1);
                String price = c.getString(2);
                arr.add(product + "$" + price);


            } while (c.moveToNext());


        }
        db.close();
        return arr;
    }

    public void addOrder(String username, String fullname, String address, String contact, int pincode, String date, String time, float price, String otype) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("contactno", contact);
        cv.put("pincode", pincode);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("amount", price);
        cv.put("otype", otype);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("orderplace", null, cv);
        db.close();
    }

    public ArrayList getOrderData(String username) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String str[] = new String[1];
        str[0] = username;
        Cursor c = db.rawQuery("select * from orderplace where username= ?", str);
        if (c.moveToFirst()) {
            do {
                arr.add(c.getString(1) + "$" + c.getString(2) + "$" + c.getString(3) + "$" + c.getString(4) + "$" + c.getString(5) + "$" + c.getString(6) + "$" + c.getString(7) + "$" + c.getString(8));
            } while (c.moveToNext());
        }
        db.close();
        return arr;
    }

    public int checkAppointmentExists(String username, String fullname, String address, String contact, String date, String time) {
        int result = 0;
        String str[] = new String[6];
        str[0] = username;
        str[1] = fullname;
        str[2] = address;
        str[3] = contact;
        str[4] = date;
        str[5] = time;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from orderplace where username = ? and fullname = ? and address = ? and contactno = ? and date = ? and time = ?", str);
        if (c.moveToFirst()) {
            result = 1;
        }
        db.close();
        return result;
    }
}
