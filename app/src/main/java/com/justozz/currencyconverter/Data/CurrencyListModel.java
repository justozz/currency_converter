package com.justozz.currencyconverter.Data;

import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import com.justozz.currencyconverter.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by justs on 17.04.2017.
 */

public class CurrencyListModel {
    private boolean hasUpdated = false;
    private OnGotDataListener onGotDataListener = null;

    public void setData(Context context, CurrencyList data) {
        CurrencyReaderDbHelper dbHelper = new CurrencyReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        for (CurrencyEntry entry : data.currencyList) {
            contentValues.put(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_CURRENCY_ID, entry.id);
            contentValues.put(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NUM_CODE, entry.numCode);
            contentValues.put(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_CHAR_CODE, entry.charCode);
            contentValues.put(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NOMINAL, entry.nominal);
            contentValues.put(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NAME, entry.name);
            contentValues.put(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_VALUE, entry.value);

            long id = db.insertWithOnConflict(CurrencyDbReaderContract.CurrencyDbEntry.TABLE_NAME,
                    null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_IGNORE);
            if (id == -1) {
                db.update(CurrencyDbReaderContract.CurrencyDbEntry.TABLE_NAME,
                        contentValues,
                        String.format(Locale.ENGLISH, "%s = ?", CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_CURRENCY_ID),
                        new String[] { entry.id });
            }
        }

        db.close();
    }

    public void getData(Context context) {
        LoadFromDbTask task = new LoadFromDbTask(context);
        task.execute();

        if (!hasUpdated) {
            Intent apiServiceIntent = new Intent(context, ApiService.class);
            apiServiceIntent.setData(Uri.parse("http://www.cbr.ru/scripts/XML_daily.asp"));
            context.startService(apiServiceIntent);
        }
    }

    public void notifyUpdated() {
        hasUpdated = true;
    }

    public void setOnGotDataListener(OnGotDataListener onGotDataListener) {
        this.onGotDataListener = onGotDataListener;
    }

    private class LoadFromDbTask extends AsyncTask<Void, Void, List<CurrencyEntry>> {
        private Context context;

        public LoadFromDbTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(List<CurrencyEntry> currencyEntryList) {
            if (onGotDataListener != null) {
                onGotDataListener.onGotData(currencyEntryList);
            }
        }

        @Override
        protected List<CurrencyEntry> doInBackground(Void... params) {
            CurrencyReaderDbHelper dbHelper = new CurrencyReaderDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    CurrencyDbReaderContract.CurrencyDbEntry._ID,
                    CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_CURRENCY_ID,
                    CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NUM_CODE,
                    CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_CHAR_CODE,
                    CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NOMINAL,
                    CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NAME,
                    CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_VALUE
            };

            Cursor cursor = db.query(
                    CurrencyDbReaderContract.CurrencyDbEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null);

            List<CurrencyEntry> currencyList = new ArrayList<>();

            while (cursor.moveToNext()) {
                CurrencyEntry currencyEntry = new CurrencyEntry();
                currencyEntry.id = cursor.getString(cursor.getColumnIndexOrThrow(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_CURRENCY_ID));
                currencyEntry.numCode = cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NUM_CODE));
                currencyEntry.charCode = cursor.getString(cursor.getColumnIndexOrThrow(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_CHAR_CODE));
                currencyEntry.nominal = cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NOMINAL));
                currencyEntry.name = cursor.getString(cursor.getColumnIndexOrThrow(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_NAME));
                currencyEntry.value = cursor.getDouble(cursor.getColumnIndexOrThrow(CurrencyDbReaderContract.CurrencyDbEntry.COLUMN_NAME_VALUE));

                currencyList.add(currencyEntry);
            }

            db.close();
            return currencyList;
        }
    }
}
