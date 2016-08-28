package com.dpoddubko.dbreader;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class BaseFragment extends Fragment {
    private static final String ARG_ASSETS_PATH = "assets_path";
    private static final String ARG_BASE_NAME = "base_path";
    private static final String BASE_DIR = "/data/data/com.dpoddubko.dbreader/databases";
    static final int MESSAGE_COPY = 0;
    private String inPath;
    private String outPath;
    private String name;
    private FileManager mFileManager;
    private SQLiteDatabase mDB;
    private EditText mQueryField;
    private Button mRunButton;
    private TextView mTextView2;
    private HandlerThread handlerThread;
    private Handler htHandler;
    private Handler uiHandler;
    private Cursor cursor;
    private ListView listView;
    private DataAdapter mDataAdapter;

    public static BaseFragment newInstance(String path, String name) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ASSETS_PATH, path);
        args.putSerializable(ARG_BASE_NAME, name);
        BaseFragment fragment = new BaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        inPath = (String) getArguments().getSerializable(ARG_ASSETS_PATH);
        name = (String) getArguments().getSerializable(ARG_BASE_NAME);
        outPath = BASE_DIR + "/" + name;
        mFileManager = new FileManager();
        if (!mFileManager.isDirExists(BASE_DIR)) new File(BASE_DIR).mkdirs();

        uiHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_COPY) {
                    mRunButton.setEnabled(true);
                    mDB = SQLiteDatabase.openDatabase(outPath, null, SQLiteDatabase.OPEN_READONLY);
                    String query = "SELECT name FROM sqlite_master WHERE " +
                            "type='table' AND name NOT LIKE 'sqlite_%' " +
                            "AND name NOT LIKE 'android_%' ORDER BY name";
                    String request = getTables(performQuery(query));
                    mTextView2.setText(request);
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.base_fragment, container, false);
        mQueryField = (EditText) v.findViewById(R.id.input_select);
        mTextView2 = (TextView) v.findViewById(R.id.textView2);
        listView = (ListView) v.findViewById(R.id.list_data);

        mRunButton = (Button) v.findViewById(R.id.run_button);
        mRunButton.setEnabled(false);
        mRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String query = mQueryField.getText().toString();
                if (StringUtils.isBlank(query))
                    Toast.makeText(getActivity(), "Query string is empty", Toast.LENGTH_LONG).show();
                else {
                    try {
                        cursor = performQuery(query);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mDataAdapter = new DataAdapter(getActivity(), cursor);
                                    listView.setAdapter(mDataAdapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if (position != 0) {
                                                cursor.moveToPosition(position - 1);
                                                StringBuilder sb = new StringBuilder();
                                                for (int i = 0; i < cursor.getColumnCount(); i++) {
                                                    String name = cursor.getColumnName(i);
                                                    sb.append(name).append(" = ").append(cursor.getString(cursor.getColumnIndex(name))).append("\n");
                                                }
                                                Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } catch (SQLiteReadOnlyDatabaseException e) {
                                    Toast.makeText(getActivity(), "You can only read the database", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (RuntimeException e) {
                        Toast.makeText(getActivity(), "Query is not correct!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        htHandler = new Handler(handlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                uiHandler.sendEmptyMessage(msg.what);
            }
        };

        htHandler.post(new Runnable() {
            public void run() {
                if (!mFileManager.isDirExists(outPath))
                    mFileManager.copyDB(getActivity(), inPath, outPath);
                htHandler.sendEmptyMessage(BaseFragment.MESSAGE_COPY);
            }
        });
        return v;
    }

    public Cursor performQuery(String query) {
        try {
            return mDB.rawQuery(query, null);
        } catch (SQLiteException e) {
            throw new RuntimeException("Query is not correct!", e);
        }
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (mDB != null) mDB.close();
        mFileManager.delete(outPath);
        handlerThread.quit();
    }
}
