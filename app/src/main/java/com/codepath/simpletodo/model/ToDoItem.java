package com.codepath.simpletodo.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vidhya on 8/11/17.
 */

public class ToDoItem {
    private int itemId;
    private String itemName;
    private String notes;
    private Long dueDate;
    private String priority;
    private String aboutDate;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dueDate);
        StringBuilder sb = new StringBuilder();
        sb.append(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " ");
        sb.append(cal.get(Calendar.DAY_OF_MONTH) + ", ");
        sb.append(cal.get(Calendar.YEAR));
        setAboutDate(sb.toString());
    }

    public String getAboutDate() {
        return aboutDate;
    }

    public void setAboutDate(String aboutDate) {
        this.aboutDate = aboutDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return itemName;
    }
}
