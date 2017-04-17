package com.justozz.currencyconverter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by justs on 17.04.2017.
 */

public class ApiBroadcastReceiver extends BroadcastReceiver {
    private OnDataChangedListener onDataChangedListener = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String dataStatusStr = intent.getStringExtra(Constants.EXTENDED_DATA_STATUS);
        if (dataStatusStr != null) {
            if (dataStatusStr.equals(Constants.UPDATED_COMPLETED)) {
                if (onDataChangedListener != null)
                    onDataChangedListener.updateCompleted();
            } else {
                if (onDataChangedListener != null)
                    onDataChangedListener.updateFailed();
            }
        }
    }

    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }
}
