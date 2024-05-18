package com.example.drivefest.data.repository.callback;

import androidx.lifecycle.MutableLiveData;

import com.example.drivefest.data.model.EventShort;

import java.util.List;

public interface DatabaseDataCallback {

    void OnSuccess(List<?> response);
    void OnFail(String response);
}
