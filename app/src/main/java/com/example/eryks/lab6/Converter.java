package com.example.eryks.lab6;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter  {
        File waveFile;
// public void rawToWave(byte[] data, final File waveFile, int sampleRate) throws IOException {
        public void rawToWave(Item item, int sampleRate) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
        String time = sdf.format(new Date());


        waveFile = new File(Environment.getExternalStorageDirectory() + "/nagrania/");
        if(!waveFile.exists()){
        waveFile.mkdirs();
        }




        waveFile = new File(Environment.getExternalStorageDirectory() + "/nagrania/" + item.getName()+ " " + item.getSurname()+ " '" + item.getTitle()+ "' " + item.getInfo()+ ".wav");

        if(!waveFile.exists())
        {
        waveFile.createNewFile();
        }

        DataOutputStream output = null;
        try {
        byte[] rawData = copyArr(item.getContent());
        output = new DataOutputStream(new FileOutputStream(waveFile));
        // WAVE header
        // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
        writeString(output, "RIFF"); // chunk id
        writeInt(output, 36 + rawData.length); // chunk size
        writeString(output, "WAVE"); // format
        writeString(output, "fmt "); // subchunk 1 id
        writeInt(output, 16); // subchunk 1 size
        writeShort(output, (short) 1); // audio format (1 = PCM)
        writeShort(output, (short) 1); // number of channels
        writeInt(output, sampleRate); // sample rate
        writeInt(output, sampleRate * 2); // byte rate
        writeShort(output, (short) 2); // block align
        writeShort(output, (short) 16); // bits per sample
        writeString(output, "data"); // subchunk 2 id
        writeInt(output, rawData.length); // subchunk 2 size
        // Audio data (conversion big endian -> little endian)
        short[] shorts = new short[rawData.length / 2];
        ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
        for (short s : shorts) {
        bytes.putShort(s);
        }

        output.write(rawData);
        } finally {
        if (output != null) {
        output.close();
        }
        }
        }


public static byte[] copyArr(byte[] arr)
        {
        byte[] res = new byte[arr.length];
        for(int i = 0; i < arr.length; i++)
        {
        res[i] = arr[i];
        }

        return res;
        }
private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
        }

private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        }

private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
        output.write(value.charAt(i));
        }
    }
}