package com.yq.eventdispatch.verticalPagerAndRecyclerView;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yq.eventdispatch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {

    private Button btn;
    private MyRecyclerView recyclerView;
    private ReAdapter adapter;

    private boolean inter = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        btn = (Button) view.findViewById(R.id.btn_change);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter = !inter;
                btn.setText(String.valueOf(inter));

            }
        });


        adapter = new ReAdapter();
        recyclerView = (MyRecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private class ReAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
            return new MHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MHolder h = (MHolder) holder;
            h.textView.setText(position + "");
            h.button.setText("btn" + position);
        }

        @Override
        public int getItemCount() {
            return 300;
        }
    }

    private class MHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button button;
        public MHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_tv);
            button = (Button) itemView.findViewById(R.id.item_btn);
        }
    }
}
