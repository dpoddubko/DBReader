package com.dpoddubko.dbreader;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class MainFragment extends Fragment {

    private DatabasesList mBasesList;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBasesList = new DatabasesList(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container,
                false);
        RecyclerView recyclerView = (RecyclerView) view
                .findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new BaseAdapter(mBasesList.getBases()));
        return view;
    }

    private class BaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mButton;
        private Database mBase;

        public BaseHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.button, container,
                    false));
            mButton = (Button) itemView.findViewById(R.id.button);
            mButton.setOnClickListener(this);
        }

        public void bindBase(Database base) {
            mBase = base;
            mButton.setText(mBase.getButtonName());
        }

        @Override
        public void onClick(View view) {
            Intent intent = BaseActivity
                    .newIntent(getActivity(), mBase.getAssetPath(), mBase.getBaseName());
            startActivity(intent);
        }
    }

    private class BaseAdapter extends RecyclerView.Adapter<BaseHolder> {
        private List<Database> mBases;

        public BaseAdapter(List<Database> bases) {
            mBases = bases;
        }

        @Override
        public BaseHolder onCreateViewHolder
                (ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new BaseHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(BaseHolder baseHolder, int position) {
            Database base = mBases.get(position);
            baseHolder.bindBase(base);
        }

        @Override
        public int getItemCount() {
            return mBases.size();
        }
    }

}
