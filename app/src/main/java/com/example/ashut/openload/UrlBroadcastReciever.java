package com.example.ashut.openload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UrlBroadcastReciever extends BroadcastReceiver {
    @Override

    //broadcast the ui
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        //perform the action
    }
}
