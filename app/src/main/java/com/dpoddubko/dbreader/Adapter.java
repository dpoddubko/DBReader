package com.dpoddubko.dbreader;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Adapter extends BaseAdapter {

    private Cursor mCursor;
    private final Context mContext;
    private int countRow;
    private int countCol;

    public Adapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        countRow = mCursor.getCount();
        countCol = mCursor.getColumnCount();
    }

    @Override
    public int getCount() {
        return countRow;
    }

    @Override
    public Object getItem(int position) {
        return mCursor;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mCursor.moveToPosition(position);
        View view = newView(parent);
        return view;
    }

    public View newView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.single_row_item, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.width = 0;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.weight = 1;
        for (int i = 0; i < countCol; i++) {
            TextView tv = new TextView(mContext);
            tv.setPadding(15, 0, 15, 0);
            tv.setLayoutParams(lp);
            tv.setSingleLine(true);
            tv.setBackgroundResource(R.drawable.cell_shape);
            tv.setText(mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(i))));
            view.addView(tv);
        }
        return view;
    }
}