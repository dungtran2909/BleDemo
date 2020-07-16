package com.example.bleexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bleexample.connect.MyRePo;
import com.example.bleexample.model.DiscoveredBluetoothDevice;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.data.Data;

public class DoBleActivity extends AppCompatActivity implements ProfileDataCallback {
    EditText edtmMes;
    Button btn_send, btnBack;
    TextView txtNameDevice, txtAddressDevice, txtMes;

    DiscoveredBluetoothDevice device;
    MyRePo myRePo = new MyRePo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_ble);

        Intent intent = getIntent();
        device = intent.getParcelableExtra("DEVICE");

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRePo.connect(device);
            }
        });
    }

    private void addControls() {
        edtmMes = findViewById(R.id.edtMes);
        txtMes = findViewById(R.id.txtMes);
        btn_send = findViewById(R.id.btn_send);
        btnBack = findViewById(R.id.btnBack);
        txtNameDevice = findViewById(R.id.txtNameDevice);
        txtAddressDevice = findViewById(R.id.txtAddressDevice);

        if (device.getDevice().getName() == null){
            txtNameDevice.setText("Unhnown Device");
        }else{
            txtNameDevice.setText(device.getDevice().getName());
        }
        txtAddressDevice.setText(device.getDevice().getAddress());
    }

    @Override
    public void onDataReceived(@NonNull BluetoothDevice device, @NonNull Data data) {

    }

    @Override
    public void onInvalidDataReceived(@NonNull BluetoothDevice device, @NonNull Data data) {

    }
}