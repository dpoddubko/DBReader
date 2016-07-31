package com.dpoddubko.dbreader;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

public class BaseActivity extends SingleFragmentActivity {

    private static final String EXTRA_ASSETS_PATH = "assets_path";
    private static final String EXTRA_BASE_NAME = "base_path";


    public static Intent newIntent(Context context, String path, String name) {
        Intent intent = new Intent(context, BaseActivity.class);
        intent.putExtra(EXTRA_ASSETS_PATH, path);
        intent.putExtra(EXTRA_BASE_NAME, name);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String path = (String) getIntent().getSerializableExtra(EXTRA_ASSETS_PATH);
        String name = (String) getIntent().getSerializableExtra(EXTRA_BASE_NAME);
        return BaseFragment.newInstance(path, name);
    }
}
