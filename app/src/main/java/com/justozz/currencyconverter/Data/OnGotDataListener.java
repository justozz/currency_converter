package com.justozz.currencyconverter.Data;

import java.util.List;

/**
 * Created by justs on 17.04.2017.
 */

public interface OnGotDataListener {
    void onGotData(List<CurrencyEntry> currencyList);
}
