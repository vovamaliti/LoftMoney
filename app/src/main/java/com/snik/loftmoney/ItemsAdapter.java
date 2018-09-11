package com.snik.loftmoney;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items = Collections.emptyList();
    private ItemsAdapterListener listener = null;

    public void setItemsAdapterListener(ItemsAdapterListener itemsAdapterListener) {
        this.listener = itemsAdapterListener;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        this.items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        Item item = items.get(i);
        itemViewHolder.bind(item, listener, i, selections.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    private SparseBooleanArray selections = new SparseBooleanArray();

    public void toogleItem(int position) {
        if (selections.get(position, false)) {
            selections.put(position, false);
        } else {
            selections.put(position, true);
        }
        notifyItemChanged(position);
    }


    public void clearSelections() {
        selections.clear();
        notifyDataSetChanged();
    }

//    public List<Integer> getSelectedItems() {
//        List<Integer> selected = new ArrayList<>();
//        for (int i = 0; i <getItemCount() ; i++) {
//            if (selections.get(i)){
//                selected.add(i);
//            }
//        }
//        return selected;
//    }
//
//    public void removeItem(int position) {
//        items.remove(position);
//        notifyItemRemoved(position);
//    }

    List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selections.size());
        for (int i = 0; i < selections.size(); i++) {
            items.add(selections.keyAt(i));
        }
        return items;
    }

    void removeItem(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
        }

        void bind(final Item item, final ItemsAdapterListener listener, final int position, final boolean selected) {
            name.setText(item.getName());
            price.setText(String.valueOf(item.getPrice()));
            itemView.setSelected(selected);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(item, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onItemLongClick(item, position);
                    }
                    return true;
                }
            });
        }
    }
}
