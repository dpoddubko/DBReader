package com.dpoddubko.dbreader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DisplayManager {
    private TableLayout tl;
    private ScrollView sv;

    public String getTables(Cursor c) {
        if (c.moveToFirst()) {
            int numCol = c.getColumnCount();
            StringBuilder sb = new StringBuilder();
            do {
                for (int i = 0; i < numCol; i++) {
                    sb.append(c.getString(i));
                }
                sb.append("\n");
            } while (c.moveToNext());
            c.close();
            return sb.toString();
        } else
            return "Query is not correct!";
    }

    public void displayTable(Context context, Cursor cursor, LinearLayout ll)
            throws RuntimeException {
        tl = new TableLayout(context);
        tl.setStretchAllColumns(true);
        tl.setShrinkAllColumns(true);
        tl.setPadding(25, 25, 25, 25);
        sv = new ScrollView(context);
        sv.addView(tl);
        TableRow tableRow;
        TextView textView;
        try {
            if (cursor.moveToFirst()) {
                int numCol = cursor.getColumnCount();
                String[] names = cursor.getColumnNames();
                tableRow = new TableRow(context);

                for (int j = 0; j < numCol; j++) {
                    textView = new TextView(context);
                    textView.setBackgroundColor(R.color.colorAccent);
                    textView.setText(names[j]);
                    tableRow.addView(textView);
                }
                tl.addView(tableRow);
                do {
                    tableRow = new TableRow(context);
                    for (int j = 0; j < numCol; j++) {
                        textView = new TextView(context);
                        textView.setText(cursor.getString(j));
                        tableRow.addView(textView);
                    }
                    tl.addView(tableRow);
                } while (cursor.moveToNext());
                cursor.close();
                ll.addView(sv);
            }
        } catch (SQLiteReadOnlyDatabaseException e) {
            throw new RuntimeException("Attempt to change database", e);
        }

    }
    public void clearView(){
        if (tl != null) {
            sv.removeAllViews();
            tl.removeAllViews();
        }
    }
}
