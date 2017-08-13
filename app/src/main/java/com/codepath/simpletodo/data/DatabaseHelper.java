package com.codepath.simpletodo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.simpletodo.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

import static android.app.SearchManager.QUERY;
import static android.content.ContentValues.TAG;

/**
 * Created by vidhya on 8/10/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_TODO_ITEMS = "todo_items";

    // Column names

    private static final String KEY_ITEM_ID = "itemId";
    private static final String KEY_ITEM_NAME = "itemName";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_DUE_DATE = "dueDate";
    private static final String KEY_PRIORITY = "priority";

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        super.onConfigure(sqLiteDatabase);
    } */

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_TODO_ITEMS +
            "(" + KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ITEM_NAME + " TEXT,"
                + KEY_NOTES + " TEXT,"
                + KEY_PRIORITY + " TEXT, "
                + KEY_DUE_DATE + " INTEGER"
            + ")";
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_ITEMS);
            onCreate(sqLiteDatabase);
        }
    }

    public void addToDoItem(ToDoItem toDoItem) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, toDoItem.getItemName());
            db.insertOrThrow(TABLE_TODO_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error adding todo item");
        } finally {
            db.endTransaction();
        }
        Log.d(TAG, "Added todo item!!!");
    }

    public List<ToDoItem> getToDoItems() {
        List<ToDoItem> list = new ArrayList<ToDoItem>();
        String QUERY = "SELECT itemName FROM todo_items;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDoItem item = new ToDoItem();
                    item.setItemName(cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)));
                    list.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

}
