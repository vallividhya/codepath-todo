package com.codepath.simpletodo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.model.ItemPriority;
import com.codepath.simpletodo.model.ToDoItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by vidhya on 8/29/17.
 */

public class TodoItemsAdapter extends ArrayAdapter<ToDoItem> {
    private List<ToDoItem> toDoList;

    public TodoItemsAdapter(@NonNull Context context, @NonNull List<ToDoItem> toDoList) {
        super(context, 0, toDoList);
        this.toDoList = toDoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item_view, parent, false);
        }
        TextView tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        tvItemName.setText(item.getItemName());
        tvDueDate.setText(item.getAboutDate());
        tvPriority.setText(item.getPriority());
        return convertView;
    }

    public void updateList(List<ToDoItem> newlist) {

        Collections.sort(newlist);
        toDoList.clear();
        toDoList.addAll(newlist);
        this.notifyDataSetChanged();
    }
}
