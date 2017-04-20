package com.justozz.currencyconverter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
    private FloatingActionButton floatingActionButton;
    private Spinner srcCurrency, dstCurrency;
    private TextView txtResultValue;
    private EditText edtValue;

    public CurrencyListView(MainActivity controller, View parent) {
        this.controller = controller;
        this.view = parent;

        onCreate();
    }

    private void onCreate() {
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        srcCurrency = (Spinner)view.findViewById(R.id.spSrcCurrency);
        dstCurrency = (Spinner)view.findViewById(R.id.spDstCurrency);
        txtResultValue = (TextView)view.findViewById(R.id.txtResultValue);
        edtValue = (EditText)view.findViewById(R.id.edtValue);

        edtValue.setEnabled(false);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (srcCurrency.getSelectedItemPosition() >= 0 && dstCurrency.getSelectedItemPosition() >= 0) {
                    int tmpPos = srcCurrency.getSelectedItemPosition();
                    srcCurrency.setSelection(dstCurrency.getSelectedItemPosition(), true);
                    dstCurrency.setSelection(tmpPos, true);
                }
            }
        });

        srcCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dstCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateValues();
            }
        });
    }

    private void updateValues() {
        String valueStr = edtValue.getText().toString();
        if (!valueStr.isEmpty()) {
            if (srcCurrency.getSelectedItemPosition() >= 0 && dstCurrency.getSelectedItemPosition() >= 0) {
                try {
                    Double value = Double.parseDouble(valueStr);
                    controller.convert(srcCurrency.getSelectedItemPosition(), value, dstCurrency.getSelectedItemPosition());
                    return;
                } catch (NumberFormatException ex) {
                    ;
                }
            }
        }
        txtResultValue.setText("-");
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(controller, controller.getString(R.string.please_wait),
                    controller.getString(R.string.loading_currencies), true, false);
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

        if (adapter.getCount() > 0)
            edtValue.setEnabled(true);
    }

    public void updateConversationResult(double value) {
        txtResultValue.setText(String.format(Locale.getDefault(), "%.2f", value));
    }

    public void updateFailed() {
        if (srcCurrency.getAdapter().isEmpty()) {
            new AlertDialog.Builder(controller)
                    .setTitle(controller.getString(R.string.error))
                    .setMessage(controller.getString(R.string.cant_get_currencies))
                    .setPositiveButton(controller.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            edtValue.setEnabled(false);
        }
    }
}
