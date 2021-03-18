package com.muchlish.scan_ai.activity.listdownload;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.muchlish.scan_ai.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ListDownloadActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    CartListAdapter adapter;
    CartListAdapter.ItemClickListener itemClickListener;
    List<File> listfile = new ArrayList<File>();
    RelativeLayout downloadlayout;
    boolean deletefile = true;
    boolean timerison = false;
    File itemswiped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_download);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        downloadlayout = findViewById(R.id.downloadlayout);
        File root = new File(Environment.getExternalStorageDirectory()+"/DataBarcode");
        ListDir(root);
        itemClickListener =((view,position)->{
            Uri path = FileProvider.getUriForFile(this,this.getApplicationContext().getPackageName()+".provider",listfile.get(position));
            Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
            pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfOpenIntent.setDataAndType(path, "application/vnd.ms-excel");
            startActivity(pdfOpenIntent);
        });
        adapter = new CartListAdapter(this,listfile,itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            adapter = new CartListAdapter(this,listfile,itemClickListener);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        });
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }
    void ListDir(File f){
        System.out.println(f);
        System.out.println(f.getName());
        System.out.println(f.listFiles().toString());
        System.out.println(f.listFiles().length);
        System.out.println(f.listFiles()[0].getName());
        File[] files = f.listFiles();
        listfile.clear();
        for(File file:files){
            listfile.add(file);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(timerison){
            if(itemswiped.exists()){
                if (itemswiped.delete()) {
                    System.out.println("file Deleted");
                } else {
                    System.out.println("file not Deleted");
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = listfile.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final File deletedItem = listfile.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            deletefile = true;
            itemswiped = deletedItem;

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());


            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(downloadlayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                    deletefile = false;
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            timerison = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(deletefile){
                        if(deletedItem.exists()){
                            if (deletedItem.delete()) {
                                System.out.println("file Deleted");
                            } else {
                                System.out.println("file not Deleted");
                            }
                        }
                        timerison=false;
                    }
                }
            },5000);
        }
    }
}
