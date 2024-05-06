package com.example.drivefest.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private FirebaseFirestoreRepository mDb;
    private List<EventShort> eventShortList;

    public HomeViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        eventShortList = new ArrayList<>();
    }

    public void fetchEventShortList(){
        mDb.getEventShort();
    }

    public List<EventShort> getEventShortList(){
        return eventShortList;
    }

}
