package com.hfad.jsontutorial;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private int spinnerPosition;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle(getString(R.string.mainactivityTitle2));
        Button button = findViewById(R.id.button1);
        editText = findViewById(R.id.editTextNumber);
        TextView textView = findViewById(R.id.convertToRub);
        button.setOnClickListener(this);
        ArrayAdapter<Valutes> valutesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, MainActivity.valutes);
        valutesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner valuteSpinner = findViewById(R.id.valute);
        valuteSpinner.setAdapter(valutesArrayAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    double currencyValue = Double.parseDouble(editText.getText().toString());
                    String s = "";
                    if (currencyValue < Long.MAX_VALUE) {
                        s = editText.getText().toString() + " RUB = " + String.format(Locale.ENGLISH,
                                "%.2f", calculateExchange(currencyValue)) + " " +
                                MainActivity.valutes.get(spinnerPosition).getCharCode();
                        textView.setText(s);
                    } else textView.setText(R.string.currencyEditValueSize);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        valuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerPosition = adapterView.getSelectedItemPosition();
                textView.setText("");
                editText.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public double calculateExchange(double value) {
        double currencyValue = MainActivity.valutes.get(spinnerPosition).getValue();
        int nominal = MainActivity.valutes.get(spinnerPosition).getNominal();
        return value * (nominal / currencyValue);
    }
}
