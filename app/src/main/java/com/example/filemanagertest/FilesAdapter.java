package com.example.filemanagertest;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.filemanagertest.ListFileActivity.deleteFile;
import static com.example.filemanagertest.ListFileActivity.files;
import static com.example.filemanagertest.ListFileActivity.path;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    List<String> filesList;
    Context mContext;
    public static int len;


    public FilesAdapter(List<String> filesList, Context mContext) {
        this.filesList = filesList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public FilesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_files , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesAdapter.MyViewHolder holder, final int position) {

        String name = filesList.get(position);
        holder.t.setText(name);

        holder.i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(mContext , view);
                popupMenu.getMenuInflater().inflate(R.menu.more_menu , popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Toast.makeText(mContext , "Deleted" , Toast.LENGTH_SHORT).show();
                                deleteFile(position , view);
                                break;
                            case R.id.copy:
                                Toast.makeText(mContext , "COpy" , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext , MoveChoiceActivity.class);
                                intent.putExtra("copysrc" , files[position].getAbsolutePath());
                                intent.putExtra("click" , "copy");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);


                                break;
                            case R.id.move:
                                Toast.makeText(mContext , "Move" , Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(mContext , MoveChoiceActivity.class);
                                intent1.putExtra("copysrc" , files[position].getAbsolutePath());
                                intent1.putExtra("click" , "move");
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent1);


                                break;

                        }
                        return true;
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = (String) filesList.get(position);
                len = filename.length();
                if (path.endsWith(File.separator)) {
                    filename = path + filename;
                } else {
                    filename = path + File.separator + filename;
                }
                if (new File(filename).isDirectory()) {
                    Intent intent = new Intent(mContext, ListFileActivity.class);
                    intent.putExtra("path", filename);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, filename + " is not a directory", Toast.LENGTH_LONG).show();
                    openFile(files[position]);

                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    private void openFile(File url) {

        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")){
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.startActivity(intent);
            System.exit(0);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView t;
        ImageView i;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.text1);
            i  = itemView.findViewById(R.id.menu_more);
        }
    }
}
