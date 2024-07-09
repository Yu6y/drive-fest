package com.example.drivefest.adapter;

import android.widget.Button;
import android.widget.TextView;

import com.example.drivefest.data.model.EventShort;

public interface FollowClickListener {
    void onBtnClick(EventShort event, Button btn, TextView text);
}
