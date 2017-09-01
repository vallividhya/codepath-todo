package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.model.ItemPriority;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {

    private final int RESPONSE_CODE = 200;
    public static final String EDIT_ITEM_NAME = "com.codepath.simpletodo.EDIT_ITEM_NAME";
    public static final String POS = "com.codepath.simpletodo.POSITION";
    public static final String EDIT_ITEM_ID = "com.codepath.simpletodo.EDIT_ITEM_ID";
    public static final String DUE_DATE = "com.codepath.simpletodo.DUE_DATE";
    public static final String PRIORITY = "com.codepath.simpletodo.PRIORITY";

    private int position = 0;
    int itemId = 0;
    long itemDueDate = System.currentTimeMillis() - 1000;
    private DatePicker datePicker;
    Spinner spinner;
    CharSequence priority = ItemPriority.Medium.name();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        spinner = (Spinner) findViewById(R.id.spPriority);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
            // Item at position 1 is "Medium".
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priority = (CharSequence) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                priority = (CharSequence) adapterView.getItemAtPosition(1);
                // Item at position 1 is "Medium".
            }
        });

        datePicker = (DatePicker) findViewById(R.id.dpDueDate);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        Intent intent = getIntent();
        String itemName = intent.getStringExtra(MainActivity.ITEM_NAME);
        position = intent.getIntExtra(MainActivity.ITEM_POSITION, 0);
        itemId = intent.getExtras().getInt(MainActivity.ITEM_ID);
        itemDueDate = intent.getExtras().getLong(MainActivity.ITEM_DUEDATE);
        priority = intent.getStringExtra(MainActivity.ITEM_PRIORITY);
        spinner.setSelection(ItemPriority.valueOf((String) priority).ordinal());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(itemDueDate);
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
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
        String selectedPriority = (String) spinner.getSelectedItem();

        if (!text.isEmpty()) {
            Intent in = new Intent();
            in.putExtra(EDIT_ITEM_ID, itemId);
            in.putExtra(DUE_DATE, dateInt);
            in.putExtra(EDIT_ITEM_NAME, text);
            in.putExtra(POS, position);
            in.putExtra(PRIORITY, selectedPriority);
            setResult(RESPONSE_CODE, in);
            finish();
        }

    }
}
