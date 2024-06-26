package com.example.drivefest.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.model.Event;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.FirebaseStorageRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.example.drivefest.data.repository.callback.StorageUrlCallback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private FirebaseFirestoreRepository mDb;
    private FirebaseStorageRepository storage;
    private MutableLiveData<List<EventShort>> eventShortListLiveData;
    private List<EventShort> eventShortFiltered;
    public HomeViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        storage = FirebaseStorageRepository.getStorageInstance();
        eventShortListLiveData = new MutableLiveData<>();
        eventShortFiltered = new ArrayList<>();

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

    public void setFilteredList(String filter, String startDate, String endDate, HashMap<String, List<String>> voivTags){
        List<EventShort> filteredList = new ArrayList<>();
        List<EventShort> currentList = eventShortListLiveData.getValue();
        if(startDate == null && endDate == null && voivTags == null) {
            for (EventShort elem : currentList) {
                if (elem.getName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredList.add(elem);
                }
                Log.d("debugging list", String.valueOf(filteredList.size()));
            }
        }
        else{
            LocalDate fromDate;
            LocalDate toDate;

            if(startDate.isEmpty())
                fromDate = LocalDate.parse("1900-01-01");
            else
                fromDate = LocalDate.parse(startDate);
            if(endDate.isEmpty())
                toDate = LocalDate.parse("3000-12-30");
            else
                toDate = LocalDate.parse(endDate);

            for(EventShort elem: currentList){
                if(elem.getDate().isAfter(fromDate.minusDays(1)) && elem.getDate().isBefore(toDate.plusDays(1))
                    && (voivTags.get("Województwo").contains(elem.getVoivodeship()) || voivTags.get("Województwo").isEmpty())
                ){
                    for(String tag: elem.getTags())
                        if(voivTags.get("Tagi").contains(tag) || voivTags.get("Tagi").isEmpty()) {
                            filteredList.add(elem);
                            break;
                        }
                }
            }
        }
        eventShortFiltered = filteredList;
    }

    public List<EventShort> getFilteredList(){
        return eventShortFiltered;
    }


}
