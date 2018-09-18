package com.snik.loftmoney.Adapter;

import com.snik.loftmoney.Model.Item;

public interface ItemsAdapterListener {
    void onItemClick(Item item, int position);

    void onItemLongClick(Item item, int position);
}
