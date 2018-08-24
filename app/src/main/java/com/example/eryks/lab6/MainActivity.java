package com.example.eryks.lab6;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int BufferElements2Rec = 1024;
    private static final int BytesPerElement = 2;
    private Item savingItem;
    Queue <byte[]> queue;
    boolean recordingStarted = false;
    boolean recordingPaused = false;
    int bufferSize = 8192;
    ImageView start;
    ImageView menu;
    TextView txtStart;
    TextView txtMenu;
    TextView timer;
    MediaRecorder recorder;
    AudioRecord record;
    private EditText name;
    private EditText surname;
    private EditText title;
    private EditText info;
    private Thread recordingThread = null;
    private Thread savingThread = null;
    int time = 0;
    LinkedBlockingQueue<byte[]> audioBuffersQueue;
    private int samplesRate;
    private short channel;
    private short encoding;
    private int samplesBufferSize;
    private boolean old;
    private Timer myTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recorder = new MediaRecorder();

        start = findViewById(R.id.imageStartPause);
        start.setOnClickListener(this);

        menu = findViewById(R.id.imageMenuStop);
        menu.setOnClickListener(this);

        txtStart = findViewById(R.id.textStartPause);
        txtMenu = findViewById(R.id.textMenuStop);
        timer = findViewById(R.id.textView);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDate = sdf.format(cal.getTime());

        name = (EditText) findViewById(R.id.editText);
        surname = (EditText) findViewById(R.id.editText2);
        title = (EditText) findViewById(R.id.editText3);
        info = (EditText) findViewById(R.id.editText4);
        savingItem = new Item();
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 10);
        timer.setText("0");
        time = 0;
    }
    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            if(recordingStarted && !recordingPaused) {
                time++;
                if(time %100 ==0) {
                    String str = Integer.toString(time/100);
                    timer.setText(str);
                }
            }
            //This method runs in the same thread as the UI.

            //Do something to the UI thread here

        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageStartPause: {
                if (!recordingStarted)
                {
                    if(!recordingPaused)
                    {
                     start.setImageDrawable(getDrawable(R.drawable.pausepng));
                     txtStart.setText(R.string.pause);
                     menu.setImageDrawable(getDrawable(R.drawable.stoppng));
                     txtMenu.setText(R.string.end);
                     recordingPaused = false;
                     recordingStarted = true;
                     startRecording();
                    }
                    else
                    {
                        //pausuj
                        start.setImageDrawable(getDrawable(R.drawable.playpng2));
                        txtStart.setText(R.string.start);
                        recordingPaused = true;
                    }
                }
                else
                    {
                    if(!recordingPaused) {
                        start.setImageDrawable(getDrawable(R.drawable.playpng2));
                        txtStart.setText(R.string.start);
                        recordingPaused = true;
                        //pausuj
                    }
                    else
                    {
                        start.setImageDrawable(getDrawable(R.drawable.pausepng));
                        txtStart.setText(R.string.pause);
                        recordingPaused = false;
                        //odpausuj
                    }

                    }
            }break;


            case R.id.imageMenuStop: {
                if (!recordingStarted) {
                    Intent intent = new Intent(this,ListActivity.class);
                    startActivity(intent);
                }
                else {
                    //zakońćz i zapisz
                    stopRecording();
                    setInfo();
                    save();
                    timer.setText("0");
                    time = 0;
                    menu.setImageDrawable(getDrawable(R.drawable.menupng));
                    txtMenu.setText(R.string.menu);
                    start.setImageDrawable(getDrawable(R.drawable.playpng2));
                    txtStart.setText(R.string.start);
                }
            }break;
        }
    }
    public void setInfo(){

        savingItem.setName((String) name.getText().toString());
        savingItem.setSurname((String) surname.getText().toString());
        savingItem.setTitle((String) title.getText().toString()+String.format(" %d,%d",time/100,time%100));
        savingItem.setInfo((String) info.getText().toString());
    }
    public void startRecording()
    {
        audioBuffersQueue = new LinkedBlockingQueue<byte[]>();
        record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                8192);
        record.startRecording();
        recordingStarted = true;
        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int dstFromLoud = 0;

                try {
                    while (recordingStarted ) {
                        if(!recordingPaused) {
                            int loud = 0;
                            byte[] tab;
                            tab = getNextBlock();
                            assert tab != null : "Coś nie działa";
                            for (byte b : tab) {
                                if ((b > 60) || (b < -60)) {
                                    loud++;
                                    dstFromLoud = 0;
                                } else {
                                    dstFromLoud++;
                                }

                            }
                            Log.i("Bytes over threshold:", Integer.toString(loud));
                            if (loud > 100 || dstFromLoud < 1500) {
                                audioBuffersQueue.put(tab);
                            }
                        }
                    }
                    while (!audioBuffersQueue.isEmpty()) {
                        byte[] curr = audioBuffersQueue.take();
                        Log.i("AUDIO BUFFER", Arrays.toString(curr));
                        savingItem.addContent(curr);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }
    private void save() {

        savingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Converter wav = new Converter();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
                    String time = sdf.format(new Date());
                    wav.rawToWave(savingItem,44100);
                    old=true;
                    // wav.rawToWave(voiceMessage.getContent(), new File(Environment.getExternalStorageDirectory() + "/nagrania/" + time + ".wav"), samplesRate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        );
        savingThread.start();
        try {
            savingThread.join(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    byte[] getNextBlock()
    {
        byte[] buffer = new byte[bufferSize];
        int returnCode = record.read(buffer,0,buffer.length);
        if(returnCode >= 0 )
        {
            return buffer;
        }
        else return null;
    }
    private void stopRecording() {
        if (null != record) {
            recordingStarted = false;
            recordingPaused = false;
            record.stop();
            record.release();
            record = null;
            recordingThread = null;
        }
    }
}