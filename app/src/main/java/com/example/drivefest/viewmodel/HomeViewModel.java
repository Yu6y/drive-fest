package com.example.drivefest.viewmodel;

import android.media.metrics.Event;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private FirebaseFirestoreRepository mDb;
    private List<EventShort> eventShortListLiveData;

    public HomeViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        eventShortListLiveData = new ArrayList<>();
    }

    public void fetchEventShortList(){
        //eventShortList.clear();
        mDb.getEventShort(new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> list) {
                List<EventShort> eventShortList = new ArrayList<>();
                for(Object obj : list){
                    if(obj instanceof EventShort)
                        eventShortList.add((EventShort) obj);
                }
                eventShortListLiveData = eventShortList;
            }
            @Override
            public void OnFail(String response){
                Log.d("response", response);
            }
        });
        //Log.d("List show elem", String.valueOf(eventShortList.size()));
    }

    public List<EventShort> getEventShortList() {
        return eventShortListLiveData;
    }

}
