package com.codepath.simpletodo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.model.PriorityType;
import com.codepath.simpletodo.model.ToDoItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.R.attr.resource;

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
        tvDueDate.setText("Due: " + item.getAboutDate());
        tvPriority.setText("Priority: " + item.getPriority());
        return convertView;
    }

    public void updateList(List<ToDoItem> newlist) {

        Collections.sort(newlist, new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem t0, ToDoItem t1) {
                Integer p0 = PriorityType.valueOf(t0.getPriority()).ordinal();
                Integer p1 = PriorityType.valueOf(t1.getPriority()).ordinal();
                return p0.compareTo(p1);
            }
        });
        toDoList.clear();
        toDoList.addAll(newlist);
        this.notifyDataSetChanged();
    }
}
