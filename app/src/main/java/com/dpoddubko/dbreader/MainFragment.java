package com.dpoddubko.dbreader;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;

public class MainFragment extends Fragment {

    private AssetManager mAssets;
    private String[] baseNames;
    private static final String BASES_FOLDER = "all_databases";

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAssets = getActivity().getAssets();
        baseNames = getBaseNames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container,
                false);
        ListView baseList = (ListView) view.findViewById(R.id.baseList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, baseNames);
        baseList.setAdapter(adapter);
        baseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String baseName = baseNames[position];
                String assetsPath = BASES_FOLDER + "/" + baseName;
                Intent intent = BaseActivity
                        .newIntent(getActivity(), assetsPath, baseName);
                startActivity(intent);
            }
        });
        return view;
    }

    public String[] getBaseNames() {
        try {
            return mAssets.list(BASES_FOLDER);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not list assets", ioe);
        }
    }
}
