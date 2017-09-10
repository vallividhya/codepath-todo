package com.codepath.simpletodo.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.adapters.TodoItemsAdapter;
import com.codepath.simpletodo.model.ToDoItem;
import com.codepath.simpletodo.helpers.DatabaseHelper;
import com.codepath.simpletodo.receivers.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String ITEM_NAME = "com.codepath.simpletodo.ITEM_NAME";
    public static final String ITEM_POSITION = "com.codepath.simpletodo.ITEM_POSITION";
    public static final String ITEM_ID = "com.codepath.simpletodo.ITEM_ID";
    public static final String ITEM_DUEDATE = "com.codepath.simpletodo.ITEM_DUEDATE";
    public static final String ITEM_PRIORITY = "com.codepath.simpletodo.ITEM_PRIORITY";
    private final int REQUEST_CODE = 20;
    private final int REQUEST_CODE1 = 10;

    List<ToDoItem> itemsList;
    ArrayAdapter<ToDoItem> toDoItemArrayAdapter;
    TodoItemsAdapter customAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsList = new ArrayList<ToDoItem>();
        customAdapter = new TodoItemsAdapter(this, itemsList);
        lvItems.setAdapter(customAdapter);
        readFromDB();
        setupListViewListener();
        scheduleAlarm();
    }


    public void onAddItem(View v) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        startActivityForResult(intent, REQUEST_CODE1);
    }

   private void setupListViewListener() {
        // Listener to delete item on long Click
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                ToDoItem item = (ToDoItem) adapterView.getItemAtPosition(pos);
                itemsList.remove(pos);
                customAdapter.updateList(itemsList);
                deleteTodoItem(item);
                return true;
            }
        });

        // Listener to edit item on click
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                ToDoItem item = (ToDoItem) adapterView.getItemAtPosition(pos);
                intent.putExtra(ITEM_POSITION, pos);
                intent.putExtra(ITEM_NAME, item.getItemName());
                intent.putExtra(ITEM_ID, item.getItemId());
                intent.putExtra(ITEM_DUEDATE, item.getDueDate());
                intent.putExtra(ITEM_PRIORITY, item.getPriority());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public void scheduleAlarm() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 7);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long timeInMillis = c.getTimeInMillis();

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void deleteTodoItem(ToDoItem item) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.deleteTodoItemFromDatabase(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE1 && data != null) {
            String updatedItemName = data.getStringExtra(EditItemActivity.EDIT_ITEM_NAME);
            ToDoItem item = new ToDoItem();
            item.setItemName(updatedItemName);
            item.setDueDate(data.getExtras().getLong(EditItemActivity.DUE_DATE));
            item.setPriority(data.getExtras().getString(EditItemActivity.PRIORITY));
            itemsList.add(item);
            customAdapter.updateList(itemsList);
            lvItems.setSelection(lvItems.getCount() - 1);
            // Write to database
            writeToDB(item);
        }

        if (requestCode == REQUEST_CODE && data != null) {
            String updatedItemName = data.getStringExtra(EditItemActivity.EDIT_ITEM_NAME);
            ToDoItem item = new ToDoItem();
            item.setItemName(updatedItemName);
            item.setItemId(data.getExtras().getInt(EditItemActivity.EDIT_ITEM_ID));
            int position = data.getExtras().getInt(EditItemActivity.POS);
            item.setDueDate(data.getExtras().getLong(EditItemActivity.DUE_DATE));
            item.setPriority(data.getExtras().getString(EditItemActivity.PRIORITY));
            itemsList.set(position, item);
            customAdapter.updateList(itemsList);
            updateItem(item);
        }
    }

    private void writeToDB(ToDoItem item) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.addToDoItem(item);
    }

    private  void readFromDB() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        itemsList = dbHelper.getToDoItems();
        Collections.sort(itemsList);
        customAdapter.addAll(itemsList);
    }

    private void updateItem(ToDoItem item) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.updateToDoItem(item);
    }

}
