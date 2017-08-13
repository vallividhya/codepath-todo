package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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
    private final int REQUEST_CODE = 20;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        //readItems();
        readFromDB();
        itemsAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String text = etNewItem.getText().toString();
        itemsAdapter.add(text);
        etNewItem.setText("");
        //writeItems();
        // Write to database
        writeToDB(text);
    }

    private void setupListViewListener() {
        // Listener to delete item on long Click
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        // Listener to edit item on click
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                String itemName = (String) adapterView.getItemAtPosition(pos);
                intent.putExtra(ITEM_POSITION, pos);
                intent.putExtra(ITEM_NAME, itemName);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            String updatedItemName = data.getStringExtra(EditItemActivity.EDIT_ITEM_NAME);
            int position = data.getExtras().getInt(EditItemActivity.POS);
            items.set(position, updatedItemName);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToDB(String itemName) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        ToDoItem item = new ToDoItem();
        item.setItemName(itemName);
        dbHelper.addToDoItem(item);
    }

    private  void readFromDB() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        List<ToDoItem> itemsList = dbHelper.getToDoItems();
        items = new ArrayList<String>();
        for (ToDoItem item : itemsList) {
            items.add(item.getItemName());
        }
    }
}
