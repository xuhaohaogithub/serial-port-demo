package com.baihe.serialdebughelper;

import androidx.appcompat.app.AppCompatActivity;
import cn.wch.lib.ch34xMultiDriver;
import cn.wch.lib.ch34xMultiManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private UsbManager manager;
    private Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        start.setOnClickListener(view -> {
            byte[] to_send2 = "A".getBytes();
            if (MyApplication.ch34xmag != null) {
                for (ch34xMultiDriver driver : MyApplication.ch34xmag.ch34xDriverList) {
                    if (to_send2 != null) {
                        driver.WriteData(to_send2, to_send2.length);
                    }
                }
            }
        });
        stop.setOnClickListener(view -> {
            byte[] to_send2 = "C".getBytes();
            if (MyApplication.ch34xmag != null) {
                for (ch34xMultiDriver driver : MyApplication.ch34xmag.ch34xDriverList) {
                    if (to_send2 != null) {
                        driver.WriteData(to_send2, to_send2.length);
                    }
                }
            }
        });
    }

    void initUsb() {
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        MyApplication.ch34xmag = new ch34xMultiManager(getApplicationContext(), manager);
        if (!MainActivity.this.getPackageManager().hasSystemFeature("android.hardware.usb.host")) {
            Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                    .setPositiveButton("确认", (arg0, arg1) -> System.exit(0)).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            openAndConfig();
        }
    }

    void openAndConfig() {
        if (MyApplication.ch34xmag.usbDeviceList.size() == 0) {
            Toast.makeText(this, "No device", Toast.LENGTH_SHORT).show();
            return;
        }
        for (UsbDevice device : MyApplication.ch34xmag.usbDeviceList) {
            if (!MyApplication.ch34xmag.OpenUsbDevice(device)) {
                Toast.makeText(this, device.getDeviceName() + ",打开失败", Toast.LENGTH_SHORT).show();
                MyApplication.ch34xmag = null;
                return;
            }
        }
        for (ch34xMultiDriver driver : MyApplication.ch34xmag.ch34xDriverList) {
            driver.SetConfig(115200, (byte) 8, (byte) 1, (byte) 0, (byte) 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUsb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyApplication.ch34xmag != null) {
            MyApplication.ch34xmag.CloseAll();
            MyApplication.ch34xmag = null;
        }
    }
}