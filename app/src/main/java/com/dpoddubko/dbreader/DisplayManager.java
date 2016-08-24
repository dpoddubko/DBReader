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
    private LinearLayout ll;

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

    public void displayTitle(Context context, Cursor cursor, LinearLayout ll)
            throws RuntimeException {
        try {
            if (cursor.moveToFirst()) {
                final String[] names = cursor.getColumnNames();
                ll.setBackgroundResource(R.color.colorCell);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.width = 0;
                lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                lp.weight = 1;

                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    TextView tv = new TextView(context);
                    tv.setPadding(15, 0, 15, 0);
                    tv.setLayoutParams(lp);
                    tv.setSingleLine(true);
                    tv.setBackgroundResource(R.drawable.cell_shape);
                    tv.setText(names[i]);
                    ll.addView(tv);
                }
            }
        } catch (SQLiteReadOnlyDatabaseException e) {
            throw new RuntimeException("Attempt to change database", e);
        }

    }
    public void setLL(LinearLayout ll) {
        this.ll = ll;
    }

    public void clearView() {
        if (ll != null) {
            ll.removeAllViews();
        }
    }
}
