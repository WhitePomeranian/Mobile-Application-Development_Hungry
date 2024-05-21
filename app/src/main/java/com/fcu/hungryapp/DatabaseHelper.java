package com.fcu.hungryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "hungry"; // 資料庫名稱
    protected static final int DATABASE_VERSION = 1; // 版本號


    // 資料表: Guest
    private static final String TABLE_GUEST = "Guest";
    private static final String COLUMN_GUEST_ID = "guest_id";
    private static final String COLUMN_GUEST_NAME = "guest_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";

    // 創建 Guest 資料表的 SQL 語句
    private static final String CREATE_TABLE_GUEST = "CREATE TABLE " + TABLE_GUEST + "("
            + COLUMN_GUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_GUEST_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_PASSWORD + " TEXT, "
            + COLUMN_PHONE + " TEXT"
            + ")";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 創建 Guest 資料表
        db.execSQL(CREATE_TABLE_GUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 在資料庫版本更新時處理相應的邏輯 (例如: 添加新的資料表、修改現有的資料表結構或者刪除資料表)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUEST);
        onCreate(db);
    }
}
