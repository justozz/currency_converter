package com.justozz.currencyconverter;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.justozz.currencyconverter.Data.CurrencyEntry;

import java.util.List;
import java.util.Locale;

/**
 * Created by justs on 17.04.2017.
 */

public class CurrencyListView {
    private MainActivity controller;
    private View view;

    //
    private ProgressDialog progressDialog = null;
    private Button btnConvert;
    private Spinner srcCurrency, dstCurrency;
    private TextView txtResultValue;
    private EditText edtValue;

    public CurrencyListView(MainActivity controller, View parent) {
        this.controller = controller;
        this.view = parent;

        onCreate();
    }

    private void onCreate() {
        btnConvert = (Button)view.findViewById(R.id.btnConvert);
        srcCurrency = (Spinner)view.findViewById(R.id.spSrcCurrency);
        dstCurrency = (Spinner)view.findViewById(R.id.spDstCurrency);
        txtResultValue = (TextView)view.findViewById(R.id.txtResultValue);
        edtValue = (EditText)view.findViewById(R.id.edtValue);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueStr = edtValue.getText().toString();
                if (!valueStr.isEmpty()) {
                    try {
                        Double value = Double.parseDouble(valueStr);
                        controller.convert(srcCurrency.getSelectedItemPosition(), value, dstCurrency.getSelectedItemPosition());
                    } catch (NumberFormatException ex) {
                        ;
                    }
                }
            }
        });
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(controller, "Пожалуйста подождите", "Загружаем данные о курсе валют", true, false);
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void updateSpinners(List<CurrencyEntry> currencyList) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(controller, android.R.layout.simple_spinner_dropdown_item);
        for (CurrencyEntry entry : currencyList)
            adapter.add(String.format(Locale.getDefault(), "%s (%s)", entry.getCharCode(), entry.getName()));
        srcCurrency.setAdapter(adapter);
        dstCurrency.setAdapter(adapter);
    }

    public void updateConversationResult(double value) {
        txtResultValue.setText(String.format(Locale.getDefault(), "%.2f", value));
    }
}
