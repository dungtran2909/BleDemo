/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example.bleexample.callback;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import com.example.bleexample.connect.MyBleManager;

import no.nordicsemi.android.ble.callback.DataSentCallback;
import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.data.Data;

public abstract class TextInDataCallback implements ProfileDataCallback, DataSentCallback,TextInCallback {
    @Override
    public void onDataReceived(@NonNull BluetoothDevice device, @NonNull Data data) {
        doSend(device, data);
    }

    @Override
    public void onDataSent(@NonNull BluetoothDevice device, @NonNull Data data) {
        doSend(device, data);
    }

    private void doSend(BluetoothDevice device, Data data){
        if (data.size() != 1) {
            onInvalidDataReceived(device, data);
            return;
        }

        final int state = data.getIntValue(Data.FORMAT_UINT8, 0);
        String text = data.getStringValue(0);
        if (text == null){
            onInvalidDataReceived(device, data);
        }else {
            onTextInChanged(device, text);
        }
//        if (state == STATE_ON) {
//            onLedStateChanged(device, true);
//        } else if (state == STATE_OFF) {
//            onLedStateChanged(device, false);
//        } else {
//            onInvalidDataReceived(device, data);
//        }

    }
}
