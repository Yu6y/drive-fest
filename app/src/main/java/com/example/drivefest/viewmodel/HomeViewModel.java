package com.example.drivefest.viewmodel;

import android.media.metrics.Event;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.FirebaseStorageRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.example.drivefest.data.repository.callback.StorageUrlCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private FirebaseFirestoreRepository mDb;
    private FirebaseStorageRepository storage;
    private MutableLiveData<List<EventShort>> eventShortListLiveData;

    public HomeViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        storage = FirebaseStorageRepository.getStorageInstance();
        eventShortListLiveData = new MutableLiveData<>();
    }

    public void fetchEventShortList(){
        mDb.getEventShort(new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> list) {
                List<EventShort> listTemp = new ArrayList<>();
                int[] fetchCount = {0};
                int totalCount = list.size();

                for(Object obj : list) {
                    if (obj instanceof EventShort) {
                        EventShort eventShort = (EventShort) obj;
                        listTemp.add(eventShort);

                        if(!eventShort.getImage().isEmpty()) {
                            storage.getEventPhotoUrl(new StorageUrlCallback() {
                                @Override
                                public void onUrlReceived(String url) {
                                    if(!url.isEmpty()) {
                                        eventShort.setImage(url);
                                        Log.d("Debug", url);
                                    } else {
                                        Log.d("Error", "Can't fetch image url" + eventShort.getImage());
                                    }
                                    fetchCount[0]++;
                                    checkAndUpdateLiveData(fetchCount[0], totalCount, listTemp);
                                }
                            }, eventShort.getImage());
                        } else {
                            fetchCount[0]++;
                            checkAndUpdateLiveData(fetchCount[0], totalCount, listTemp);
                        }
                    }
                }
            }

            @Override
            public void OnFail(String response){
                Log.d("response", response);
            }
        });
    }

    private void checkAndUpdateLiveData(int currentCount, int totalCount, List<EventShort> listTemp) {
        if (currentCount == totalCount) {
            eventShortListLiveData.postValue(listTemp);
        }
    }

    public MutableLiveData<List<EventShort>> getEventShortList() {
        return eventShortListLiveData;
    }

}
