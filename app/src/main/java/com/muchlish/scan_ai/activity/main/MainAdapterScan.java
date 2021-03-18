package com.muchlish.scan_ai.activity.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muchlish.scan_ai.R;

import java.util.ArrayList;

public class MainAdapterScan extends RecyclerView.Adapter<MainAdapterScan.RecyclerViewAdapter> {

    private Context context;
    private ItemClickListener itemClickListener;
    private ArrayList<String> date;
    private ArrayList<String> barcode;
    private ArrayList<String> typecode;

    public MainAdapterScan(Context context, ItemClickListener itemClickListener, ArrayList<String> date, ArrayList<String> barcode, ArrayList<String> typecode) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.date = date;
        this.barcode = barcode;
        this.typecode = typecode;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_scanner, parent, false);
        return new RecyclerViewAdapter(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        holder.datetime.setText(date.get(position));
        holder.barcode.setText(barcode.get(position));
        holder.type_code.setText(typecode.get(position));

    }

    @Override
    public int getItemCount() {
        return barcode.size();
    }

    public class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView datetime,barcode,type_code;
        ItemClickListener itemClickListener;

        public RecyclerViewAdapter(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            this.itemClickListener=itemClickListener;

            datetime = itemView.findViewById(R.id.datetime);
            barcode = itemView.findViewById(R.id.value_code);
            type_code = itemView.findViewById(R.id.type_code);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
