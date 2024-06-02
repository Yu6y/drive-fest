package com.example.drivefest.viewmodel;



import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.model.Event;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;

import java.util.List;
import java.util.Map;

public class EventDescViewModel extends ViewModel {

    FirebaseFirestoreRepository mDb;
    MutableLiveData<Event> eventLiveData;
    public EventDescViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        eventLiveData = new MutableLiveData<>();
    }

    public void setEvent(EventShort eventShort){
        Event event = new Event(eventShort);
        eventLiveData.postValue(event);
    }
    public LiveData<Event> getEvent(){
        return eventLiveData;
    }

    private void updateEvent(Map<String, Object> map){
        Event eventUpdated = eventLiveData.getValue();
        eventUpdated.setDescription((String)map.get("description"));
        eventUpdated.setLocationCords((String)map.get("location"));
        eventLiveData.postValue(eventUpdated);
    }
    public void updateEventDesc(){
        Event event = eventLiveData.getValue();
        mDb.getEventDesc(event.getId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                updateEvent((Map<String, Object>)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.d("response", response);
            }
        });
    }
}
