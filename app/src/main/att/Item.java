package com.example.sapij.lab5b;

/**
 * Created by sapij on 08.05.2018.
 */

public class Item {
        String name;
        String status;
        int sensorType;
        public Item(String name, String status ,int type ) {
            this.name=name;
            this.status = status;
            sensorType = type;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }
        public int getSensorType()
        {
            return sensorType;
        }
    }
