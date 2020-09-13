package com.example.filemanagertest;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.example.filemanagertest.FilesAdapter.len;
import static com.example.filemanagertest.MainActivity.REQUEST_CODE;

public class ListFileActivity extends AppCompatActivity {

   public static String path;
    public static List<String> filelist = new ArrayList<>();
    HashMap<String, List<String>> childfiles;
    static File[] files;
    RecyclerView recyclerView;
    File root;
    ImageView menu_more;
    static FilesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);
        childfiles = new HashMap<String, List<String>>();
        recyclerView = findViewById(R.id.list);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        permission();
//        path = "/";
////        if (getIntent().hasExtra("path")) {
////            path = getIntent().getStringExtra("path");
////        }

//        path = Environment.getExternalStorageDirectory().getPath();
//        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_files , null , false);
//        menu_more = view.findViewById(R.id.menu_more);
//        menu_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                PopupMenu popupMenu = new PopupMenu(getApplicationContext() , view);
//                popupMenu.getMenuInflater().inflate(R.menu.more_menu , popupMenu.getMenu());
//                popupMenu.show();
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.delete:
//                                Toast.makeText(getApplicationContext() , "Deleted" , Toast.LENGTH_SHORT).show();
//                                deleteFile(getSelectedItemPosition() , view);
//                                break;
//                        }
//                        return true;
//                    }
//                });
//            }
//        });



//
//        setTitle(path);
//        Log.e("path" , path);
//        List<String> values = new ArrayList<>();
//        File dir = new File(path);
//        Log.e("dir" , dir.toString());
//
//        if (!dir.canRead()) {
//            setTitle(getTitle() + " (inaccessible)");
//        }
//       File[] list = dir.listFiles();
//
//
//        if (list != null) {
//            for (File file : list) {
//                if (file.isDirectory()) {
//                    values.add(file.getName());
//                    Toast.makeText(this,  " is not a directory", Toast.LENGTH_LONG).show();
//
//
//                }
//            }
//        }
////        Collections.sort(values);
////        values.add(0 , "Heloooo");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_2, android.R.id.text1, values);
//        setListAdapter(adapter);
    }

    static void deleteFile(int selectedItemPosition, View view) {
        File file = files[selectedItemPosition];
        boolean deleated = file.delete();
        Snackbar.make(view , "Deleted " + deleated + file.getName() , BaseTransientBottomBar.LENGTH_SHORT);
        filelist.remove(selectedItemPosition);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            path = path.substring(0 , path.length() - len);
            root = new File(path);
        } catch (Exception ignored) {

        }// hi

    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ListFileActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE} , REQUEST_CODE);

        }
        else {
//            Intent intent = new Intent(getApplicationContext() , ListFileActivity.class);
//            startActivity(intent);
            if (path == null ){
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

            root = new File(path);
            }
            if (root == null) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
                root = new File(path);
            }

        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
            root = new File(path);
        }
            Log.e("root", root.toString());
            Log.e("Files" , root.exists() + "");
            Log.e("Files" , root.isDirectory() + "");
            Log.e("Files" , root.listFiles() + "");
            files = root.listFiles();




            filelist.clear();

                    if (files != null) {
                        if (files.length != 0) {
                            Toast.makeText(getApplicationContext(), " files not empty", Toast.LENGTH_SHORT).show();
                            for (File file : files) {
                                filelist.add(file.getName());
//                                if (adapter != null) {
//                                    adapter.notifyDataSetChanged();
//                                }

//                    List<String> childName = new ArrayList<>();
//                    File child = new File(Environment.getDataDirectory().getAbsolutePath() + "/" + file.getName() + "/");
//                    File[] childs = child.listFiles();
//                    for (File childfile : childs) {
//                        childName.add(file.getName());
//                        childfiles.put(file.getName(), childName);
//                    }
                            }
                        }
                    }

                    adapter = new FilesAdapter(filelist, getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext() , RecyclerView.VERTICAL , false));
                    recyclerView.setAdapter(adapter);

//                    adapter.notifyDataSetChanged();

//                    adapter.notifyDataSetChanged();
                }

            }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent(getApplicationContext() , ListFileActivity.class);
////                startActivity(intent);
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
                File root = new File(path);

                if (getIntent().hasExtra("path")) {
                    path = getIntent().getStringExtra("path");
                    root = new File(path);
                }
                Log.e("root", root.toString());
                Log.e("Files" , root.exists() + "");
                Log.e("Files" , root.isDirectory() + "");
                Log.e("Files" , root.listFiles() + "");
                File[] files = root.listFiles();




                filelist.clear();
                if (files != null) {
                    if (files.length != 0) {
//                        Toast.makeText(this, " files not empty", Toast.LENGTH_SHORT).show();
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

                adapter = new FilesAdapter(filelist, getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext() , RecyclerView.VERTICAL , false));
                recyclerView.setAdapter(adapter);


            } else {
                ActivityCompat.requestPermissions(ListFileActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE} , REQUEST_CODE);

            }
        }
    }

    public static void copyFile(File src, File dst) throws IOException {

        FileInputStream var2 = new FileInputStream(src);
        FileOutputStream var3 = new FileOutputStream(dst);
        byte[] var4 = new byte[1024];

        int var5;
        while((var5 = var2.read(var4)) > 0) {
            var3.write(var4, 0, var5);
        }

        var2.close();
        var3.close();
    }

}