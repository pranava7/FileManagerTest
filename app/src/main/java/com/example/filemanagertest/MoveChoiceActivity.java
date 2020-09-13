package com.example.filemanagertest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoveChoiceActivity extends ListActivity {

    String path;
    List<String> filelist = new ArrayList<>();
    File[] files;
    File src;
    int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_choice);
        if (getIntent().hasExtra("copysrc")) {
            src = new File(getIntent().getStringExtra("copysrc"));
        }
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        File root = new File(path);

        files = root.listFiles();


        filelist.clear();
        if (files != null) {
            if (files.length != 0) {
                Toast.makeText(this, " files not empty", Toast.LENGTH_SHORT).show();
                for (File file : files) {
                    filelist.add(file.getName());
//                    List<String> childName = new ArrayList<>();
//                    File child = new File(Environment.getDataDirectory().getAbsolutePath() + "/" + file.getName() + "/");
//                    File[] childs = child.listFiles();
//                    for (File childfile : childs) {
//                        childName.add(file.getName());
//                        childfiles.put(file.getName(), childName);
//                    }
                }
            }
        } else {
            Toast.makeText(this, " empty", Toast.LENGTH_SHORT).show();

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext() , android.R.layout.simple_list_item_1 , android.R.id.text1 , filelist);
        setListAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        try {
            Log.e("psth" , files[position].toPath().toString());
            Log.e("psth" , src.toPath().toString());

            int p = src.toPath().toString().indexOf(".");

            Log.e("pdddd" , p + " ");
            String ext = src.toPath().toString().substring(p);
            Log.e("pppp" , ext);
            Log.e("destination", files[position].toPath().toString() +File.separator +  src.getName() + "_copy" + i  + ext);
            if (getIntent().hasExtra("click")) {
                if (getIntent().getStringExtra("click").equals("move")) {
                    Files.move(src.toPath(), Paths.get(files[position].toPath().toString() + File.separator + src.getName()));
                    Snackbar.make(v , "MOVED" , BaseTransientBottomBar.LENGTH_SHORT);
                    i++;
                } else if (getIntent().getStringExtra("click").equals("copy")) {
                    Files.copy(src.toPath(), Paths.get(files[position].toPath().toString() + File.separator + src.getName() + "_copy" + i + ext));
                    Snackbar.make(v , "COPIED" , BaseTransientBottomBar.LENGTH_SHORT);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(getApplicationContext() , ListFileActivity.class);
        startActivity(intent);
        finish();
    }
}