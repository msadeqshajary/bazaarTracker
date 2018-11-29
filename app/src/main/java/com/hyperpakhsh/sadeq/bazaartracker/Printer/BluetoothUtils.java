package com.hyperpakhsh.sadeq.bazaartracker.Printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Set;

public class BluetoothUtils {

    private Activity activity;
    private BluetoothAdapter bluetoothAdapter;

    BluetoothUtils(Activity activity) {
        this.activity = activity;
    }

    void checkBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBluetooth,1);
        }
    }

    void findPairedDevices(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.e("MAC ADDRESS IS ",deviceHardwareAddress+ "//"+ deviceName);
            }
        }
    }
}
