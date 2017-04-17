package com.justozz.currencyconverter;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.justozz.currencyconverter.Data.CurrencyList;
import com.justozz.currencyconverter.Data.CurrencyListModel;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by justs on 17.04.2017.
 */

public class ApiService extends IntentService {
    public ApiService() {
        super("com.justozz.currencyconverter.ApiService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String urlStr = intent.getDataString();
            requestData(urlStr);
        }
    }

    private void requestData(String requestUrlStr) {
        HttpURLConnection urlConnection = null;
        boolean gotData = false;
        try {
            URL url = new URL(requestUrlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());

            Serializer xmlSerializer = new Persister();
            CurrencyList currencyList = xmlSerializer.read(CurrencyList.class, is);
            CurrencyListModel currencyListModel = new CurrencyListModel();
            currencyListModel.setData(this, currencyList);
            gotData = true;
        } catch (Exception ex) {
            ;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        Intent broadcastIntent = new Intent(Constants.BROADCAST_ACTION)
                .putExtra(Constants.EXTENDED_DATA_STATUS, gotData ? Constants.UPDATED_COMPLETED : Constants.UPDATE_FAILED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
