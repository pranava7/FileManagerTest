package com.example.filemanagertest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListFileActivity extends ListActivity {

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);

        path = "/";
//        if (getIntent().hasExtra("path")) {
//            path = getIntent().getStringExtra("path");
//        }
        setTitle(path);
        path = Environment.getExternalStorageDirectory().getPath();
        Log.e("path" , path);
        List<String> values = new ArrayList<>();
        File dir = new File(path);
        Log.e("dir" , dir.toString());

        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
       File[] list = dir.listFiles();


        if (list != null) {
            for (File file : list) {
                if (file.isDirectory()) {
                    values.add(file.getName());
                    Toast.makeText(this,  " is not a directory", Toast.LENGTH_LONG).show();


                }
            }
        }
//        Collections.sort(values);
//        values.add(0 , "Heloooo");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String filename = (String) getListAdapter().getItem(position);
        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }
        if (new File(filename).isDirectory()) {
            Intent intent = new Intent(this, ListFileActivity.class);
            intent.putExtra("path", filename);
            startActivity(intent);
        } else {
            Toast.makeText(this, filename + " is not a directory", Toast.LENGTH_LONG).show();
        }
    }
}