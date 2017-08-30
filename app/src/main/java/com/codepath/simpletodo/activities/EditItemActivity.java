package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.codepath.simpletodo.R;

import java.util.Calendar;

import static android.R.attr.id;

public class EditItemActivity extends AppCompatActivity {

    private final int RESPONSE_CODE = 200;
    public static final String EDIT_ITEM_NAME = "com.codepath.simpletodo.EDIT_ITEM_NAME";
    public static final String POS = "com.codepath.simpletodo.POSITION";
    public static final String EDIT_ITEM_ID = "com.codepath.simpletodo.EDIT_ITEM_ID";
    public static final String DUE_DATE = "com.codepath.simpletodo.DUE_DATE";
    private int position = 0;
    int itemId = 0;

    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        datePicker = (DatePicker) findViewById(R.id.dpDueDate);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        Intent intent = getIntent();
        String itemName = intent.getStringExtra(MainActivity.ITEM_NAME);
        position = intent.getIntExtra(MainActivity.ITEM_POSITION, 0);
        itemId = intent.getExtras().getInt(MainActivity.ITEM_ID);
        EditText editText = (EditText) findViewById(R.id.etUpdateItem);
        editText.setText(itemName);
        editText.setSelection(editText.getText().length());
    }

    public void onSaveEditItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etUpdateItem);
        String text = etNewItem.getText().toString();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        long dateInt = cal.getTimeInMillis();

        if (!text.isEmpty()) {
            Intent in = new Intent();
            in.putExtra(EDIT_ITEM_ID, itemId);
            in.putExtra(DUE_DATE, dateInt);
            in.putExtra(EDIT_ITEM_NAME, text);
            in.putExtra(POS, position);
            setResult(RESPONSE_CODE, in);
            finish();
        }

    }
}
