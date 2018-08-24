package com.example.sapij.lab5b;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    SensorManager mSensorManager;
    List<Sensor> sensor;
    ArrayList<Item> items;
    private static ItemAdapter adapter;
    final Context con = this;
    ListView lv;
    ArrayList<float[]> values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lv = (ListView) findViewById (R.id.listView1);
        sensor = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        values = new ArrayList<>();
        items = new ArrayList<>();
        for(int i = 0 ;i<sensor.size();i++)
        {
            Sensor sens = sensor.get(i);
            boolean enabled = !mSensorManager.getSensorList(sens.getType()).isEmpty();
            items.add(new Item(sens.getName(),enabled?"On":"Off",sens.getType()));
            float[] arr = {0};
            values.add(arr);
        }
        adapter= new ItemAdapter(items,getApplicationContext());

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item= items.get(position);
                Sensor sensor = mSensorManager.getSensorList(item.getSensorType()).get(0);
                String st = "";
                st= st +item.getName();
                for(int i =0 ; i<values.get(position).length;i++)
                    if(values.get(position)[i]!=0.0 || i==0)
                        st = st+"\n"+Float.toString(values.get(position)[i]);
                st = st+ String.format("\nPobór mocy : %3.3f mA",sensor.getPower());
                st = st+String.format("\nMaksymalna wartość : %8.3f",sensor.getMaximumRange());
                st = st+String.format("\nRozdzielczość sensora : %8.3f ",sensor.getResolution());
                Toast.makeText(con, st, Toast.LENGTH_LONG).show();
            }
        });
    }
    protected void onStart()
    {
        super.onStart();
        for(int i = 0 ; i<sensor.size();i++)
        {
            Sensor sens = sensor.get(i);
            mSensorManager.registerListener(this,sens,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    protected void onStop()
    {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        for(int i = 0 ; i<sensor.size();i++)
        {
            if(sensorEvent.sensor==sensor.get(i))
                values.set(i,sensorEvent.values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}