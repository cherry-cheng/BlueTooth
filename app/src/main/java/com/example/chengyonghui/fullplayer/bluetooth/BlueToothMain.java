package com.example.chengyonghui.fullplayer.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chengyonghui.fullplayer.R;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by chengyonghui on 2017/9/26.
 */
public class BlueToothMain extends Activity {

    private Button button = null;
    private Button button1 = null;
    private Button button2 = null;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothReceiver bluetoothReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);

        button = (Button) findViewById(R.id.scanButtonId);
        button.setOnClickListener(new ButtonListener());

        button1 = (Button) findViewById(R.id.visibleButtonId);
        button1.setOnClickListener(new visibleListener());

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        button2 = (Button) findViewById(R.id.discoverButtonId);
        button2.setOnClickListener(new discoverListner());
    }

    private class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //可以从收到的Intent对象当中，将代表远程蓝牙适配器的对象取出
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println(device.getAddress());
            }
        }
    }

    /*
    该监听器用于修改蓝牙设备可见性
     */
    private class visibleListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //常见一个Intent对象，并将其action的值设置为BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE
            Intent visibleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            //将一个键值对存放到Intent对象中，主要用于指定可见状态的时间
            visibleIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
            startActivity(visibleIntent);
        }
    }

    private class discoverListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bluetoothReceiver);
        super.onDestroy();
    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //得到BluetoothAdapter对象
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            //判断BluetoothAdapter对象是否为空，如果为空，则表明本机没有蓝牙设备
            if (adapter != null) {
                System.out.println("本机有蓝牙设备");
                //调用isEnabled()方法，判断当前蓝牙设备是否可用
                if (!adapter.isEnabled()) {
                    //创建一个Intent对象，该对象用于启动一个Activity，提示用户开启蓝牙设备
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                    System.out.println("----------cyh");
                }
                //得到所有已经配对的蓝牙适配器对象
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                System.out.println("----------devices.size " + devices.size());
                if (devices.size() > 0) {
                    for (Iterator iterator = devices.iterator(); iterator.hasNext();) {
                        BluetoothDevice bluetoothDevice = (BluetoothDevice) iterator.next();
                        //得到远程蓝牙设备的地址
                        System.out.println(bluetoothDevice.getAddress());
                    }
                }
            } else {
                System.out.println("没有蓝牙设备");
            }
        }
    }
}
