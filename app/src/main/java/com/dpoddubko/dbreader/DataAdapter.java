package com.dpoddubko.dbreader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DataAdapter extends BaseAdapter {

    private Cursor mCursor;
    private final Context mContext;
    private int countRow;
    private int countCol;

    public DataAdapter(Context context, Cursor cursor) throws RuntimeException {
        mContext = context;
        mCursor = cursor;
        try {
            countRow = mCursor.getCount() + 1;
            countCol = mCursor.getColumnCount();
        } catch (SQLiteReadOnlyDatabaseException e) {
            throw new RuntimeException("Attempt to change database", e);
        }
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

    static class ViewHolder {
        TextView[] myTextViews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.single_row_item, parent, false);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            holder = new ViewHolder();
            holder.myTextViews = new TextView[countCol];
            for (int i = 0; i < countCol; i++) {
                TextView tv = (TextView) inflater.inflate(R.layout.text_view, null);
                tv.setLayoutParams(lp);
                holder.myTextViews[i] = tv;
                view.addView(tv);
            }
            convertView = view;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            for (int i = 0; i < countCol; i++) {
                TextView view = holder.myTextViews[i];
                view.setBackgroundResource(R.color.colorCell);
                view.setText(mCursor.getColumnName(i));
            }
        } else {
            mCursor.moveToPosition(position - 1);
            for (int i = 0; i < countCol; i++) {
                TextView view = holder.myTextViews[i];
                view.setBackgroundColor(Color.WHITE);
                view.setBackgroundResource(R.drawable.cell_shape);
                view.setText(mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(i))));
            }
        }
        return convertView;
    }
}