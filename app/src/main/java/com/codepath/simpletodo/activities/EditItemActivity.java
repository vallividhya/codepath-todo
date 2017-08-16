package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codepath.simpletodo.R;

import static android.R.attr.id;

public class EditItemActivity extends AppCompatActivity {

    private final int RESPONSE_CODE = 200;
    public static final String EDIT_ITEM_NAME = "com.codepath.simpletodo.EDIT_ITEM_NAME";
    public static final String POS = "com.codepath.simpletodo.POSITION";
    public static final String EDIT_ITEM_ID = "com.codepath.simpletodo.EDIT_ITEM_ID";
    private int position = 0;
    int itemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

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
        Intent in = new Intent();
        in.putExtra(EDIT_ITEM_ID, itemId);
        in.putExtra(EDIT_ITEM_NAME, text);
        in.putExtra(POS, position);
        setResult(RESPONSE_CODE, in);
        finish();
    }
}
