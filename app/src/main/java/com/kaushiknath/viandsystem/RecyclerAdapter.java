package com.kaushiknath.viandsystem;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kaushik Nath on 19-Mar-16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    private String[] dataSource;
    private String[] cat;
    private int[] rates;
    private float[] ran;

    public RecyclerAdapter(Context context, String[] dataArgs, int[] rates, float[] ran, String cat[]) {
        dataSource = dataArgs;
        this.rates = rates;
        this.ran = ran;
        this.cat = cat;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(itemView.getContext(),Main2Activity.class);
                //itemView.getContext().startActivity(intent);
                //returnPos(position);
                //tv.setText(((TextView) v).getText().toString());
                Intent intent = new Intent(v.getContext(), DetailedInfo.class);
                TextView textView = (TextView) v.findViewById(R.id.list_item);
                intent.putExtra("Selected", textView.getText().toString());
                v.getContext().startActivity(intent);

            }
        });
        return viewHolder;


    }


    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(dataSource[position]);
        holder.textView1.setText("Approximate Budget :" + Integer.toString(rates[position]));
        holder.textView2.setText("Delivery within :" + Float.toString(ran[position]) + "km");
        String ca = cat[position];
        Log.d("Category", ca);
        if (ca.equals("cafe"))
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.cafe));
        else if (ca.equals("bakery")) {
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.bakery));
            Log.d("Status","I am Here");
        }
        else
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.restaurant));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;
        protected TextView textView1;
        protected TextView textView2;
        protected ImageView imageView;
        //public ClipData.Item currentItem;
        int position;

        public ViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_item);
            textView1 = (TextView) itemView.findViewById(R.id.rate111);
            textView2 = (TextView) itemView.findViewById(R.id.ran);
            imageView = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }

}