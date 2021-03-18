package com.muchlish.scan_ai.activity.listdownload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.muchlish.scan_ai.R;

import java.io.File;
import java.util.List;

public class ListDownloadAdapter extends RecyclerView.Adapter<ListDownloadAdapter.RecyclerViewAdapter> {

    private Context mContext;
    private List<File> listfile;
    private ItemClickListener itemClickListener;

    public ListDownloadAdapter(Context mContext, List<File> listfile, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.listfile = listfile;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_listdownload,parent,false);
        return new RecyclerViewAdapter(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        holder.file.setText(listfile.get(position).getName());
        holder.image.setImageResource(R.drawable.microsoft_excel);
    }

    @Override
    public int getItemCount() {
        return listfile.size();
    }

    public class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemClickListener itemClickListener;
        TextView file;
        ImageView image;
        CardView cardView;
        public RecyclerViewAdapter(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            file = itemView.findViewById(R.id.file);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card_item);
            this.itemClickListener = itemClickListener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v,getAdapterPosition());
        }
    }
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
