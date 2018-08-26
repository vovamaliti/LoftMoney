package com.snik.loftmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

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

        nameInput.addTextChangedListener(inputTextWatcher);
        priceInput.addTextChangedListener(inputTextWatcher);


    }


    private TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String name = nameInput.getText().toString().trim();
            String price = priceInput.getText().toString().trim();
            addBtn.setEnabled(!name.isEmpty() && !price.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
