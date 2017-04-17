package com.justozz.currencyconverter.Data;

import android.provider.BaseColumns;

/**
 * Created by justs on 17.04.2017.
 */

public final class CurrencyDbReaderContract {
    public CurrencyDbReaderContract() {

    }

    public static abstract class CurrencyDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "Currencies";
        public static final String COLUMN_NAME_CURRENCY_ID = "currency_id";
        public static final String COLUMN_NAME_NUM_CODE = "num_code";
        public static final String COLUMN_NAME_CHAR_CODE = "char_code";
        public static final String COLUMN_NAME_NOMINAL = "nominal";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VALUE = "value";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + CurrencyDbEntry.TABLE_NAME + " (" +
                        CurrencyDbEntry._ID + " INTEGER PRIMARY KEY," +
                        CurrencyDbEntry.COLUMN_NAME_CURRENCY_ID + " TEXT UNIQUE," +
                        CurrencyDbEntry.COLUMN_NAME_NUM_CODE + " INTEGER," +
                        CurrencyDbEntry.COLUMN_NAME_CHAR_CODE + " TEXT," +
                        CurrencyDbEntry.COLUMN_NAME_NOMINAL + " INTEGER," +
                        CurrencyDbEntry.COLUMN_NAME_NAME + " TEXT," +
                        CurrencyDbEntry.COLUMN_NAME_VALUE + " REAL)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + CurrencyDbEntry.TABLE_NAME;
    }
}
