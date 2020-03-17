package com.snik.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.snik.loftmoney.model.Item;

import java.util.Objects;

public class AddActivity extends AppCompatActivity {
    public static final String KEY_TYPE = "type";
    public static final String KEY_ITEM = "item";

    private EditText nameInput;
    private EditText priceInput;
    private Button addBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nameInput = findViewById(R.id.name);
        priceInput = findViewById(R.id.price);
        addBtn = findViewById(R.id.addBtn);
        final String type = Objects.requireNonNull(getIntent().getExtras()).getString(KEY_TYPE);
        TextListener listener = new TextListener();
        nameInput.addTextChangedListener(listener);
        priceInput.addTextChangedListener(listener);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String price = priceInput.getText().toString();
                Item item = new Item(name, Integer.parseInt(price), type);
                Intent intent = new Intent();
                intent.putExtra(KEY_ITEM, item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    private class TextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            addBtn.setEnabled(!TextUtils.isEmpty(nameInput.getText()) && !TextUtils.isEmpty(priceInput.getText()));
        }
    }
}
