package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.model.ToDoItem;
import com.codepath.simpletodo.data.DatabaseHelper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String ITEM_NAME = "com.codepath.simpletodo.ITEM_NAME";
    public static final String ITEM_POSITION = "com.codepath.simpletodo.ITEM_POSITION";
    public static final String ITEM_ID = "com.codepath.simpletodo.ITEM_ID";
    private final int REQUEST_CODE = 20;

    List<ToDoItem> itemsList;
    ArrayAdapter<ToDoItem> toDoItemArrayAdapter;

    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        //readItems();
        itemsList = new ArrayList<ToDoItem>();
        readFromDB();
        toDoItemArrayAdapter = new ArrayAdapter<ToDoItem>(this, android.R.layout.simple_list_item_1, itemsList);
        lvItems.setAdapter(toDoItemArrayAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String text = etNewItem.getText().toString();
        if (!text.isEmpty()) {
            ToDoItem todo = new ToDoItem();
            todo.setItemName(text);
            toDoItemArrayAdapter.add(todo);
            etNewItem.setText("");
            lvItems.setSelection(lvItems.getCount() - 1);
            // Write to database
            writeToDB(todo);
        }
    }

   private void setupListViewListener() {
        // Listener to delete item on long Click
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                ToDoItem item = (ToDoItem) adapterView.getItemAtPosition(pos);
                itemsList.remove(pos);
                toDoItemArrayAdapter.notifyDataSetChanged();
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
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }


    private void deleteTodoItem(ToDoItem item) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.deleteTodoItemFromDatabase(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            String updatedItemName = data.getStringExtra(EditItemActivity.EDIT_ITEM_NAME);
            ToDoItem item = new ToDoItem();
            item.setItemName(updatedItemName);
            item.setItemId(data.getExtras().getInt(EditItemActivity.EDIT_ITEM_ID));
            int position = data.getExtras().getInt(EditItemActivity.POS);
            itemsList.set(position, item);
            toDoItemArrayAdapter.notifyDataSetChanged();
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
    }

    private void updateItem(ToDoItem item) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.updateToDoItem(item);
    }
}
