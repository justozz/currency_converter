package com.justozz.currencyconverter;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.justozz.currencyconverter.Data.CurrencyEntry;
import com.justozz.currencyconverter.Data.CurrencyList;
import com.justozz.currencyconverter.Data.CurrencyListModel;
import com.justozz.currencyconverter.Data.OnGotDataListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataChangedListener, OnGotDataListener {
    CurrencyListModel model;
    CurrencyListView view;

    List<CurrencyEntry> currencyList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        subscribeToUpdateEvents();
        initModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    //OnDataChangedListener
    @Override
    public void updateCompleted() {
        model.notifyUpdated();
        model.getData(this);
    }

    @Override
    public void updateFailed() {
        view.dismissProgressDialog();
    }

    //OnGotDataListener
    @Override
    public void onGotData(List<CurrencyEntry> currencyList) {
        this.currencyList = new ArrayList<>();
        this.currencyList.addAll(currencyList);
        view.updateSpinners(this.currencyList);
        view.dismissProgressDialog();
    }

    private void initView() {
        view = new CurrencyListView(this, getWindow().getDecorView());
    }

    private void subscribeToUpdateEvents() {
        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        ApiBroadcastReceiver broadcastReceiver = new ApiBroadcastReceiver();
        broadcastReceiver.setOnDataChangedListener(this);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void initModel() {
        model = new CurrencyListModel();
        model.setOnGotDataListener(this);
    }

    private void getData() {
        view.showProgressDialog();
        model.getData(getApplicationContext());
    }

    public void convert(int srcPos, double srcValue, int dstPos) {
        CurrencyEntry srcCurrency, dstCurrency;
        srcCurrency = currencyList.get(srcPos);
        dstCurrency = currencyList.get(dstPos);

        double srcNormalized = srcCurrency.getValue() / srcCurrency.getNominal();
        double dstNormalized = dstCurrency.getValue() / dstCurrency.getNominal();

        double result = srcValue * srcNormalized / dstNormalized;
        view.updateConversationResult(result);
    }
}
