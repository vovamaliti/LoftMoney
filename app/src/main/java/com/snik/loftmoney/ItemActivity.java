package com.snik.loftmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        List<Item> items = new ArrayList<>();
        items.add(new Item("Баклажан", "69р"));
        items.add(new Item("Огурцы", "51р"));
        items.add(new Item("Помидоры", "111р"));
        items.add(new Item("Сыр", "270р"));
        items.add(new Item("Колбаса", "350р"));
        items.add(new Item("Хлеб", "40р"));


        ItemsAdapter adapter = new ItemsAdapter();
        adapter.setItems(items);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
