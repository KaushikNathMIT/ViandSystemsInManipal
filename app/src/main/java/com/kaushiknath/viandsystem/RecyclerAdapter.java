package com.kaushiknath.viandsystem;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kaushik Nath on 19-Mar-16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] dataSource;
    private int[] rates;

    public RecyclerAdapter(String[] dataArgs, int[] rates) {
        dataSource = dataArgs;
        this.rates = rates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(itemView.getContext(),Main2Activity.class);
                //itemView.getContext().startActivity(intent);
                //returnPos(position);
                //tv.setText(((TextView) v).getText().toString());
                Intent intent = new Intent(v.getContext(), DetailedInfo.class);
                intent.putExtra("Selected", ((TextView) v).getText().toString());
                v.getContext().startActivity(intent);

            }
        });
        return viewHolder;


    }


    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;
        protected TextView textView1;
        //public ClipData.Item currentItem;
        int position;

        public ViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_item);
            textView1 = (TextView) itemView.findViewById(R.id.rate111);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(dataSource[position]);
        holder.textView1.setText(Integer.toString(rates[position])) ;
    }

}