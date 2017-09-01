package com.codepath.simpletodo.model;

/**
 * Created by vidhya on 8/31/17.
 */

public enum ItemPriority {
    High (3),
    Medium (2),
    Low (1);

    private Integer weight;

    private ItemPriority(Integer weight) {
        this.weight = weight;
    }

    public int compare(ItemPriority other) {
        return weight.compareTo(other.weight);
    }
}
