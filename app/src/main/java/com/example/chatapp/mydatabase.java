package com.example.chatapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class mydatabase extends SQLiteOpenHelper {
    private long result;
    private static final String DBname = "data.db";
    mydatabase(Context context) {
        super(context, DBname, null, 9);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        profile(db);
        user(db);
        chat(db);
        partners(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        profile(db);
        user(db);
        chat(db);
        partners(db);
    }
    private void user(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT, user_email TEXT)");
    }
    private void profile(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS profile");
        db.execSQL("CREATE TABLE profile (id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, userAge TEXT, userGender TEXT)");
    }
    private void chat(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS chat");
        db.execSQL("CREATE TABLE chat (id INTEGER PRIMARY KEY AUTOINCREMENT,msgID TEXT UNIQUE,userName TEXT,userID TEXT,msg TEXT,userGender TEXT,groupNumber TEXT)");
    }
    private void partners(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS partners");
        db.execSQL("create table partners (roomId TEXT PRIMARY KEY, partner TEXT)");
    }
    void insertPartner(String qry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(qry);
    }
    Boolean insertProfile(String userName, String userAge, String userGender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("userName", userName);
        cv.put("userAge", userAge);
        cv.put("userGender", userGender);
        result = db.insert("profile", null, cv);
        return result != -1;
    }
    boolean insertMsg(String msgId, String userName, String userId, String msg, String userGender, String groupNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("msgId", msgId);
        cv.put("userName", userName);
        cv.put("userId", userId);
        cv.put("msg", msg);
        cv.put("userGender", userGender);
        cv.put("groupNumber", groupNumber);
        result = db.insert("chat", null, cv);
        return result != -1;
    }
    ArrayList<String> getProfile(String qry) {
        ArrayList<String> profile = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(qry, null);
        if (res.getCount() > 0) {
            res.moveToFirst();
            while (!res.isAfterLast()) {
                profile.add(res.getString(1));
                profile.add(res.getString(2));
                profile.add(res.getString(3));
                res.moveToNext();
            }
            res.close();
            return profile;
        } else
            profile.add("");
        res.close();
        return profile;
    }
    ArrayList<String> getProfileChat(String qry) {
        ArrayList<String> profile = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(qry, null);
        if (res.getCount() > 0) {
            res.moveToFirst();
            while (!res.isAfterLast()) {
                profile.add(res.getString(0));
                profile.add(res.getString(1));
                /*missed line*/
                res.moveToNext(); /* << add this line */
            }
            res.close();
            return profile;
        } else
            profile.add("");
        res.close();
        return profile;
    }
    ArrayList<msg_class> getMsg(String qry) {
        ArrayList<msg_class> msg = new ArrayList<>();
        msg_class msgClass;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(qry, null);
        if (res.getCount() > 0) {
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String s1 = res.getString(1);
                String s2 = res.getString(2);
                String s3 = res.getString(3);
                String s4 = res.getString(4);
                String s5 = res.getString(5);
                msgClass = new msg_class(s1, s2, s3, s4, s5, "0");
                msg.add(msgClass);
                res.moveToNext();
            }
            res.close();
            return msg;
        } else
            msg.add(new msg_class("1"));
        res.close();
        return msg;
    }
    int getInteger(String qry) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(qry, null);
        res.moveToFirst();
        if (res.getCount() > 0) {
            int I1 = res.getInt(0);
            res.close();
            return I1;
        } else {
            res.close();
            return -1;
        }
    }
    String getString(String qry) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(qry, null);
        // res.moveToNext();
        res.moveToFirst();
        int n = res.getCount();
        if (n > 0) {
            String s1 = res.getString(0);
            res.close();
            return s1;
        } else {
            res.close();
            return "";
        }
    }
    ArrayList<String> getOneToOneChats(String qry) {
        ArrayList<String> groupNumber = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(qry, null);
        if (res.getCount() > 0) {
            res.moveToFirst();
            while (!res.isAfterLast()) {
                groupNumber.add(res.getString(0));
                res.moveToNext();
            }
            res.close();
            return groupNumber;
        } else
            groupNumber.add("");
        res.close();
        return groupNumber;
    }
    msg_class getLastMsg(String qry) {
        msg_class msgClass = new msg_class();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(qry, null);
        if (res.getCount() > 0) {
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String s1 = res.getString(1);
                String s2 = res.getString(2);
                String s3 = res.getString(3);
                String s4 = res.getString(4);
                String s5 = res.getString(5);
                msgClass = new msg_class(s1, s2, s3, s4, s5, "0");
                res.moveToNext();
            }
            res.close();
            return msgClass;
        } else
            msgClass = new msg_class("1");
        res.close();
        return msgClass;
    }
    Boolean insertId(String user_id, String user_email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_id", user_id);
        cv.put("user_email", user_email);
        result = db.insert("user", null, cv);
        return result != -1;
    }
    void dropTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        profile(db);
        chat(db);
        user(db);
        partners(db);
    }
}