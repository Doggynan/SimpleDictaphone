package com.example.eryks.lab6;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    ArrayList<Item> items;
    final Context con = this;
    ItemAdapter adapter;
    ListView lv;
    private List<File> files;
    private boolean old;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        lv = (ListView) findViewById (R.id.listView1);
        items = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().toString()+"/nagrania/";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files1 = directory.listFiles();
        files = new ArrayList<>();
        if(directory.listFiles()==null){
            files1 = new File[0];
        }
        Log.d("Files", "Size: "+ files1.length);
        for (int i = 0; i < files1.length; i++)
        {
            files.add(files1[i]);
            Log.d("Files", "FileName:" + files1[i].getName());
        }

       lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View arg1,
                                           int position, long id)
                {
                    File f = files.get(position);
                            File file = new File(Environment.getExternalStorageDirectory().toString() + "/nagrania/" + f.getName());
                            file.delete();
                    files.remove(position);
                    adapter= new ItemAdapter(con,R.layout.row_item,files);
                    lv.setAdapter(adapter);
                    return true;
                }
        });



        adapter= new ItemAdapter(this,R.layout.row_item,files);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (File f : files) {
                    if (f.equals(parent.getItemAtPosition(position))) {
                        playFromList(f.getName());
                    }
                }
            }
        });
    }



    public void playFromList(String your_file_name) {

        MediaPlayer player;
        try {
            player = new MediaPlayer();
            player.setDataSource(Environment.getExternalStorageDirectory().toString()+"/nagrania/"+your_file_name);
            player.prepare();
            player.start();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
