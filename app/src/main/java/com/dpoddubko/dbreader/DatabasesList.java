package com.dpoddubko.dbreader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabasesList {
    private static final String TAG = "DatabasesList";
    private static final String BASES_FOLDER = "all_databases";
    private AssetManager mAssets;
    private List<Database> mBases = new ArrayList<>();

    public DatabasesList(Context context) {
        mAssets = context.getAssets();
        loadDatabases();
    }

    private void loadDatabases() {
        String[] basesNames;
        try {
            basesNames = mAssets.list(BASES_FOLDER);
            Log.i(TAG, "Found " + basesNames.length + " bases");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }
        for (String filename : basesNames) {
            Database base = new Database(BASES_FOLDER + "/" + filename);
            mBases.add(base);
        }
    }

    public List<Database> getBases() {
        return mBases;
    }
}
