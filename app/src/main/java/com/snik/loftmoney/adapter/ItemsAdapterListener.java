package com.snik.loftmoney.adapter;

import com.snik.loftmoney.model.Item;

public interface ItemsAdapterListener {
    void onItemClick(Item item, int position);

    void onItemLongClick(Item item, int position);
}
