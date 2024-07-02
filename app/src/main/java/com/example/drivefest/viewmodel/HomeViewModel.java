package com.example.drivefest.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.model.Workshop;
import com.example.drivefest.data.repository.FirebaseAuthRepository;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.FirebaseStorageRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.example.drivefest.data.repository.callback.StorageUrlCallback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private FirebaseFirestoreRepository mDb;
    private FirebaseStorageRepository storage;
    private FirebaseAuthRepository mAuth;
    private MutableLiveData<List<EventShort>> eventShortListLiveData;
    private List<EventShort> eventShortFiltered;
    private MutableLiveData<List<EventShort>> favEventShortLiveData;
    private MutableLiveData<List<Workshop>> workshopsLiveData;
    private List<Workshop> workshopsFiltered;
    private String userId;
    public HomeViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        storage = FirebaseStorageRepository.getStorageInstance();
        mAuth = FirebaseAuthRepository.getRepoInstance();
        eventShortListLiveData = new MutableLiveData<>();
        eventShortFiltered = new ArrayList<>();
        favEventShortLiveData = new MutableLiveData<>();
        //userId = mAuth.getCurrentUserId();
        workshopsLiveData = new MutableLiveData<>();
        workshopsFiltered = new ArrayList<>();
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
            Log.e("dibug notfav", "to 1");
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
            Log.e("dibug vm", String.valueOf(filteredList.size()));
        }
        eventShortFiltered = filteredList;
    }

    public List<EventShort> getFilteredList(){
        return eventShortFiltered;
    }

    public void fetchFavEventShortList(){
        mDb.getFollowedEvents(userId, new DatabaseDataCallback() {
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
                                    checkAndUpdateFavLiveData(fetchCount[0], totalCount, listTemp);
                                }
                            }, eventShort.getImage());
                        } else {
                            fetchCount[0]++;
                            checkAndUpdateFavLiveData(fetchCount[0], totalCount, listTemp);
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

    private void checkAndUpdateFavLiveData(int currentCount, int totalCount, List<EventShort> listTemp) {

        if (currentCount == totalCount) {
            Log.e("dibug fav", "to 2");
            favEventShortLiveData.postValue(listTemp);
        }
    }

    public MutableLiveData<List<EventShort>> getFavEventShortLiveData() {
        return favEventShortLiveData;
    }

    public void setFollowedEvents(){
        Log.e("Tu sie", "wypierdala");
        for(EventShort event : eventShortListLiveData.getValue()){
            if(favEventShortLiveData.getValue().contains(event))
                event.setFollowed();
        }
    }
    public void fetchWorkshopsList(){
        mDb.getWorkshops(new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> list) {
                List<Workshop> listTemp = new ArrayList<>();
                int[] fetchCount = {0};
                int totalCount = list.size();

                for(Object obj : list) {
                    if (obj instanceof Workshop) {
                        Workshop workshop = (Workshop) obj;
                        listTemp.add(workshop);

                        if(!workshop.getImage().isEmpty()) {
                            storage.getWorkshopPhotoUrl(new StorageUrlCallback() {
                                @Override
                                public void onUrlReceived(String url) {
                                    if(!url.isEmpty()) {
                                        workshop.setImage(url);
                                        Log.d("Debug", url);
                                    } else {
                                        Log.d("Error", "Can't fetch image url" + workshop.getImage());
                                    }
                                    fetchCount[0]++;
                                    checkAndUpdateWorkshopsLiveData(fetchCount[0], totalCount, listTemp);
                                }
                            }, workshop.getImage());
                        } else {
                            fetchCount[0]++;
                            checkAndUpdateWorkshopsLiveData(fetchCount[0], totalCount, listTemp);
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
    private void checkAndUpdateWorkshopsLiveData(int currentCount, int totalCount, List<Workshop> listTemp) {
        if (currentCount == totalCount) {
            Log.e("dibug fav", "to 2");
            workshopsLiveData.postValue(listTemp);
        }
    }

    public MutableLiveData<List<Workshop>> getWorkshopsLiveData() {
        return workshopsLiveData;
    }

    public void setWorkshopsFilteredList(String filter, String rating, HashMap<String, List<String>> voivTags){
        List<Workshop> filteredList = new ArrayList<>();
        List<Workshop> currentList = workshopsLiveData.getValue();
        if(rating == null && voivTags == null) {
            for (Workshop elem : currentList) {
                if (elem.getName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredList.add(elem);
                }
                Log.d("debugging wrksplist", String.valueOf(filteredList.size()));
            }
        }
        else{
            float rate;
            switch(rating){
                case "from2":
                    rate = 2;
                    break;
                case "from3":
                    rate = 3;
                    break;
                case "from4":
                    rate = 4;
                    break;
                case "maxrate":
                    rate = 5;
                    break;
                case "allrates":
                    rate = 1;
                    break;
                default:
                    rate = 0;
            }
            for(Workshop elem: currentList){
                if(elem.getRating() >= rate
                        && (voivTags.get("Województwo").contains(elem.getVoivodeship())
                        || voivTags.get("Województwo").isEmpty())
                ){
                    for(String tag: elem.getTags())
                        if(voivTags.get("Tagi").contains(tag) || voivTags.get("Tagi").isEmpty()) {
                            filteredList.add(elem);
                            break;
                        }
                }
            }
            Log.e("dibug vm", String.valueOf(filteredList.size()));
        }
        workshopsFiltered = filteredList;
    }

    public List<Workshop> getWorkshopsFiltered(){
        return workshopsFiltered;
    }
}
