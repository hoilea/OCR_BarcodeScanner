package com.muchlish.scan_ai.activity.listdownload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muchlish.scan_ai.R;

import java.io.File;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private Context mContext;
    private List<File> listfile;
    private ItemClickListener itemClickListener;

    public CartListAdapter(Context mContext, List<File> listfile, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.listfile = listfile;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_listdownloadv2,parent,false);
        return new MyViewHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.file.setText(listfile.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listfile.size();
    }

    public void removeItem(int position) {
        listfile.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(File item, int position) {
        listfile.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemClickListener itemClickListener;
        TextView file;
        ImageView image;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            file = itemView.findViewById(R.id.file);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            this.itemClickListener = itemClickListener;
            viewForeground.setOnClickListener(this);
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
