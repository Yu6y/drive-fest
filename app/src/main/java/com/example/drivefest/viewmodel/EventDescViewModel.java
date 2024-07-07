package com.example.drivefest.viewmodel;


import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.EventDescFragment;
import com.example.drivefest.data.model.Comment;
import com.example.drivefest.data.model.Event;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.model.RatedWorkshop;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDescViewModel extends ViewModel {

    private FirebaseFirestoreRepository mDb;
    private MutableLiveData<Event> eventLiveData;
    private MutableLiveData<String> commentResponse;
    private MutableLiveData<List<Comment>> commentsList;

    public EventDescViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        eventLiveData = new MutableLiveData<>();
        commentResponse = new MutableLiveData<>();
        commentsList = new MutableLiveData<>();
    }

    public void setEvent(EventShort eventShort){
        Event event = new Event(eventShort);
        eventLiveData.postValue(event);
    }
    public MutableLiveData<Event> getEvent(){
        return eventLiveData;
    }

    public void updateEventDesc(EventShort eventShort){
        Event event = new Event(eventShort);
        eventLiveData.postValue(event);
        //Event event = eventLiveData.getValue();
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

    private void updateEvent(Map<String, Object> map){
        Event eventUpdated = eventLiveData.getValue();
        eventUpdated.setDescription((String)map.get("description"));
        eventUpdated.setLocationCords((String)map.get("location"));
        eventUpdated.setAddress((String)map.get("address"));
        eventLiveData.postValue(eventUpdated);
    }

    public void postComment(Comment comment){
        Map<String, Object> map = new HashMap<>();
        map.put("content", comment.getContent());
        map.put("image", comment.getImage());
        map.put("timestamp", comment.getTimestamp());
        map.put("userId", comment.getUserId());
        map.put("userName", comment.getUserName());

        mDb.postComment(eventLiveData.getValue().getId(), map, new DatabaseDataCallback(){
            @Override
            public void OnSuccess(List<?> list) {
               commentResponse.postValue(list.get(0).toString());
            }

            @Override
            public void OnFail(String response){
                commentResponse.postValue(response);

            }
        });
    }

    public MutableLiveData<String> getCommentResponse(){
        return commentResponse;
    }

    public void fetchCommentsList(EventShort eventShort){
        Event event = new Event(eventShort);
        eventLiveData.postValue(event);
        mDb.fetchCommentsList(event.getId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                commentsList.postValue((List<Comment>) response);
            }

            @Override
            public void OnFail(String response) {
                Log.d("response", response);
            }
        });
    }

    public MutableLiveData<List<Comment>> getCommentsList(){
        return commentsList;
    }
}
