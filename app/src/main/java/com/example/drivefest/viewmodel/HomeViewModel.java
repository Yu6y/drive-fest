package com.example.drivefest.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.model.RatedWorkshop;
import com.example.drivefest.data.model.Workshop;
import com.example.drivefest.data.model.WorkshopDesc;
import com.example.drivefest.data.repository.FirebaseAuthRepository;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.FirebaseStorageRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.example.drivefest.data.repository.callback.StorageUrlCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private FirebaseFirestoreRepository mDb;
    private FirebaseStorageRepository storage;
    private FirebaseAuthRepository mAuth;
    private MutableLiveData<List<EventShort>> eventShortListLiveData;
    private List<EventShort> eventShortFiltered;
    private MutableLiveData<List<EventShort>> favEventShortLiveData;
    private MutableLiveData<List<Workshop>> workshopsLiveData;
    private List<Workshop> workshopsFiltered;
    private MutableLiveData<WorkshopDesc> workshopDesc;
    private int sortEvent;
    private String filterStartDate, filterEndDate;
    private HashMap<String, List<String>> filterCheckedItems;
    private int sortWorkshop;
    private int rateWorkshop;
    private HashMap<String, List<String>> filterCheckedItemsWorkshop;
    private MutableLiveData<List<RatedWorkshop>> ratedWorkshop;
    private String userId;
    private MutableLiveData<String> addEventPhotoName;
    private MutableLiveData<String> addEventId;
    private MutableLiveData<String> addEventResponse;
    private MutableLiveData<String> addWorkshopPhotoName;
    private MutableLiveData<String> addWorkshopId;
    private MutableLiveData<String> addWorkshopResponse;
    public HomeViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        storage = FirebaseStorageRepository.getStorageInstance();
        mAuth = FirebaseAuthRepository.getRepoInstance();
        eventShortListLiveData = new MutableLiveData<>();
        eventShortFiltered = new ArrayList<>();
        favEventShortLiveData = new MutableLiveData<>();
        userId = mAuth.getCurrentUserId();
        workshopsLiveData = new MutableLiveData<>();
        workshopsFiltered = new ArrayList<>();
        workshopDesc = new MutableLiveData<>();
        sortEvent = -1;
        filterStartDate = null;
        filterEndDate = null;
        filterCheckedItems = null;
        sortWorkshop = -1;
        filterCheckedItemsWorkshop = null;
        ratedWorkshop = new MutableLiveData<>();
        addEventPhotoName = new MutableLiveData<>();
        addEventId = new MutableLiveData<>();
        addEventResponse = new MutableLiveData<>();
        addWorkshopPhotoName = new MutableLiveData<>();
        addWorkshopId = new MutableLiveData<>();
        addWorkshopResponse = new MutableLiveData<>();
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
        //poprawic
        if(startDate == null && endDate == null && voivTags == null) {
            if(!eventShortFiltered.isEmpty()) {
                Log.e("fioilter", String.valueOf(currentList.size()));

                currentList = eventShortFiltered;
                Log.e("fioilter2", String.valueOf(currentList.size()));
            }
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
                        eventShort.setFollowed();
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
        if(favEventShortLiveData.getValue() != null) {
            for (EventShort event : favEventShortLiveData.getValue()) {
                for (EventShort eventShort : eventShortListLiveData.getValue()) {
                    Log.e("porownanie", event.getId() + " _ " + eventShort.getId());
                    if (event.getId().equals(eventShort.getId())) {
                        eventShort.setFollowed();
                        break;
                    }
                }
            }
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
        if(!workshopsFiltered.isEmpty())
            currentList = workshopsFiltered;
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
                case "from1":
                    rate = 1;
                    break;
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
                    rate = 0;
                    break;
                default:
                    rate = 0;
            }
            for(Workshop elem: currentList){
                if(elem.getRate() >= rate
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

    public void updateWorkshopDesc(Workshop workshop) {
        workshopDesc.postValue(new WorkshopDesc(workshop));
        mDb.getWorkshopDesc(workshop.getId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                updateWorkshopDescription((Map<String, Object>)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.d("response workshopdesc", response);
            }
        });
    }
    private void updateWorkshopDescription(Map<String, Object> map) {
        WorkshopDesc temp = workshopDesc.getValue();
        temp.setDescription((String) map.get("description"));
        temp.setAddress((String) map.get("address"));
        workshopDesc.postValue(temp);
    }
    public MutableLiveData<WorkshopDesc> getWorkshopDesc(){
        return workshopDesc;
    }

    public void setEventSort(int sort){
        sortEvent = sort;
    }
    public int getSortEvent(){
        return sortEvent;
    }
    public void setFilterItems(String start, String end, HashMap<String, List<String>> items){
        filterStartDate = start;
        filterEndDate = end;
        filterCheckedItems = items;
    }
    public String getFilterStartDate(){
        return filterStartDate;
    }
    public String getFilterEndDate(){
        return filterEndDate;
    }
    public HashMap<String, List<String>> getFilterCheckedItems(){
        return filterCheckedItems;
    }

    public void setWorkshopSort(int sort){
        sortWorkshop = sort;
    }
    public int getSortWorkshop(){
        return sortWorkshop;
    }
    public void setFilterWorkshopItems(int rate,  HashMap<String, List<String>> items){
        rateWorkshop = rate;
        filterCheckedItemsWorkshop = items;
    }
    public int getRateWorkshop(){
        return rateWorkshop;
    }
    public HashMap<String, List<String>> getFilterCheckedItemsWorkshop(){
        return filterCheckedItemsWorkshop;
    }

    public MutableLiveData<List<RatedWorkshop>> getRatedWorkshop(){
       return ratedWorkshop;
    }

    public void fetchRatedWorkshops(){

        mDb.getWorkshopRate(userId, new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> list) {
                List<RatedWorkshop> listTemp = new ArrayList<>();
                int[] fetchCount = {0};
                int totalCount = list.size();

                for(Object obj : list) {
                    if (obj instanceof RatedWorkshop) {
                        RatedWorkshop ratedWorkshop = (RatedWorkshop) obj;
                        listTemp.add(ratedWorkshop);

                        fetchCount[0]++;
                        checkAndUpdateRatedWorkshops(fetchCount[0], totalCount, listTemp);
                    }
                }
            }

            @Override
            public void OnFail(String response){
                Log.d("response", response);
            }
        });
    }

    private void checkAndUpdateRatedWorkshops(int currentCount, int totalCount, List<RatedWorkshop> listTemp) {
        if (currentCount == totalCount) {
            ratedWorkshop.postValue(listTemp);
        }
    }

    public void setRatedWorkshops(){
        Log.e("rated", "workshops");
        Log.e("Tu sie", "wypierdala");
        if(ratedWorkshop.getValue() != null) {
            for (RatedWorkshop rated : ratedWorkshop.getValue()) {
                for (Workshop workshop : workshopsLiveData.getValue()) {
                    Log.e("porownanie", workshop.getId() + " _ " + rated.getWorkshopId());
                    if (rated.getWorkshopId().equals(workshop.getId())) {
                        workshop.setRated();
                        workshop.setRatedFromDb(true);
                        Log.e("rated", String.valueOf(rated.getRate()));
                        workshop.setRateByUser(rated.getRate());
                        break;
                    }
                }
            }
        }
    }

    public void clearWorkshopDesc(){
        workshopDesc = new MutableLiveData<>();
    }

    public String getUserPic(){
            return mAuth.getUserPhoto().toString();
    }

    public String getUserDisplayName(){
        return mAuth.getUserName();
    }

    public String getUserId(){
        return userId;
    }

    public void followEvent(EventShort event){
        Map<String, Object> map = new HashMap<>();
        map.put("date", event.getDateString());
        map.put("eventId", event.getId());
        map.put("followersCount", event.getFollowersCount() + 1);
        map.put("image", event.getImage());
        map.put("location", event.getLocation());
        map.put("name", event.getName());
        map.put("tags", Arrays.asList(event.getTags()));
        map.put("voivodeship", event.getVoivodeship());
        map.put("userId", mAuth.getCurrentUserId());

        event.setFollowersCount(event.getFollowersCount() + 1);
        event.setFollowed();
        List<EventShort> newFav = favEventShortLiveData.getValue();
        if(newFav == null)
            newFav = new ArrayList<>();
        newFav.add(0, event);
        favEventShortLiveData.postValue(newFav);
       // updateEventFav(event);
        mDb.postFavEvent(map, new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });
        mDb.updateFollowedEvent(event.getId(), event.getFollowersCount(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });
    }

    public void unFollowEvent(EventShort event){
        event.setFollowersCount(event.getFollowersCount() - 1);
        event.setUnFollowed();
       // updateEventFav(event);
        mDb.deleteFollowedEvent(event.getId(), mAuth.getCurrentUserId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });

        mDb.updateFollowedEvent(event.getId(), event.getFollowersCount(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });
    }
    public void unFollowFavEvent(EventShort event){
        List<EventShort> currList;
        List<EventShort> newFav;
        newFav = favEventShortLiveData.getValue();
        if(newFav == null)
            newFav = new ArrayList<>();
        newFav.remove(event);
        favEventShortLiveData.postValue(newFav);
        currList = eventShortListLiveData.getValue();
        for(EventShort eventShort: currList){
            if(eventShort.getId().equals(event.getId())) {
                eventShort.setUnFollowed();
                eventShort.setFollowersCount(event.getFollowersCount() - 1);
                break;
            }
        }

        eventShortListLiveData.postValue(currList);
        event.setFollowersCount(event.getFollowersCount() - 1);
        // updateEventFav(event);
        mDb.deleteFollowedEvent(event.getId(), mAuth.getCurrentUserId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });

        mDb.updateFollowedEvent(event.getId(), event.getFollowersCount(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });
    }
   /* private void updateEventFav(EventShort event){
        List<EventShort> currentList = favEventShortLiveData.getValue();
        if (currentList != null) {
            int index = currentList.indexOf(event);
            if (index != -1) {
                currentList.remove(event);
                favEventShortLiveData.postValue(currentList);
            }
            else{
                currentList.add(event);
                favEventShortLiveData.postValue(currentList);
            }
        }else {
            currentList = new ArrayList<>();
            currentList.add(event);
            favEventShortLiveData.postValue(currentList);
        }
    }*/

    public void followDescEvent(EventShort event){
        Map<String, Object> map = new HashMap<>();
        map.put("date", event.getDateString());
        map.put("eventId", event.getId());
        map.put("followersCount", event.getFollowersCount() + 1);
        map.put("image", event.getImage());
        map.put("location", event.getLocation());
        map.put("name", event.getName());
        map.put("tags", Arrays.asList(event.getTags()));
        map.put("voivodeship", event.getVoivodeship());
        map.put("userId", mAuth.getCurrentUserId());

        List<EventShort> currList = eventShortListLiveData.getValue();
        EventShort newES = null;
        //event ustawic follow i count
        //dodac do ulubionych
        for(EventShort eventShort: currList){
            if(eventShort.getId().equals(event.getId())){
                eventShort.setFollowersCount(eventShort.getFollowersCount() + 1);
                eventShort.setFollowed();
                newES = eventShort;
                break;
            }
        }
        eventShortListLiveData.postValue(currList);
        List<EventShort> newFav = favEventShortLiveData.getValue();
        if(newFav == null)
            newFav = new ArrayList<>();
        newFav.add(0, newES);
        favEventShortLiveData.postValue(newFav);
        event.setFollowersCount( event.getFollowersCount() + 1);
        // updateEventFav(event);
        mDb.postFavEvent(map, new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });
        mDb.updateFollowedEvent(event.getId(), event.getFollowersCount(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });
    }

    public void unFollowDescEvent(EventShort event){
        event.setFollowersCount(event.getFollowersCount() - 1);
        List<EventShort> currList = eventShortListLiveData.getValue();

        for(EventShort eventShort: currList){
            if(eventShort.getId().equals(event.getId())){
                eventShort.setFollowersCount(eventShort.getFollowersCount() - 1);
                eventShort.setUnFollowed();
                break;
            }
        }
        eventShortListLiveData.postValue(currList);

        List<EventShort> newFav;
        newFav = favEventShortLiveData.getValue();
        if(newFav == null)
            newFav = new ArrayList<>();
        for(EventShort delete: newFav){
            if(delete.getId().equals(event.getId())) {
                Log.e("deletign", "event from favorites");
                Log.e("deletign", event.getName() + "  " + delete.getName());
                boolean check = newFav.remove(delete);
                Log.e("deletign", String.valueOf(check));
                break;
            }
        }
        favEventShortLiveData.postValue(newFav);

        mDb.deleteFollowedEvent(event.getId(), mAuth.getCurrentUserId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });

        mDb.updateFollowedEvent(event.getId(), event.getFollowersCount(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });
    }

   public void workshopRate(Workshop workshop, float rate){
        RatedWorkshop rated = new RatedWorkshop(mAuth.getCurrentUserId(),
                workshop.getId(), (int)rate);
        Map<String, Object> map = new HashMap<>();
        map.put("rate", rated.getRate());
        map.put("userId", rated.getUserId());
        map.put("workshopId", rated.getWorkshopId());

        List<RatedWorkshop> currList = ratedWorkshop.getValue();
        if(currList == null)
            currList = new ArrayList<>();
        currList.add(0, rated);
        ratedWorkshop.postValue(currList);
        workshop.updateRating((int)rate,1);
       WorkshopDesc workshopDesc1 = getWorkshopDesc().getValue();
       workshopDesc1.setRateCount(workshopDesc1.getRateCount() + 1);

       float value = 0;
       float newRate, newRateCount = workshopDesc1.getRateCount();
       for(Integer i = 1; i < 6; i++)
           value += i * workshopDesc1.getRatings().get(i);
       newRate = (float) (Math.round((value /newRateCount) * 10) / 10.0);
        Log.e("debug rtate", String.valueOf(value + "  " + newRateCount) + " " + newRate);
       workshopDesc1.setRate(newRate);
       workshopDesc.postValue(workshopDesc1);

       List<Workshop> updateList = workshopsLiveData.getValue();

        for(Workshop workshop1: updateList){
            if(workshop1.getId().equals(workshop.getId())){
               workshop1.setRated();
               workshop1.setRate(newRate);
               workshop1.setRatedFromDb(true);
               break;
            }
        }
        workshopsLiveData.postValue(updateList);

        mDb.addWorkshopRate(map, new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });

        mDb.updateRatedWorkshop(workshop.getId(), workshop.getRatingsForDb(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", (String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
            }
        });
    }

    public EventShort getEventById(String id){
        EventShort event = new EventShort();
        for(EventShort eventShort: eventShortListLiveData.getValue()){
            if(eventShort.getId().equals(id)){
                event = eventShort;
                break;
            }
        }

        return event;
    }

   /* public int makeEvent(String name, String date, String location, String address, String pic,
                             String desc, HashMap<String, List<String>> list){
        boolean[] flag = {false};
      /*  storage.addEventPhoto(Uri.parse(pic), new StorageUrlCallback() {
            @Override
            public void onUrlReceived(String url) {
                Log.e("zdjecie v1", url);
                shortEvent.put("image", url);
                //dodac livedata dodaje obrazek, observe -> metoda dodaj do bazy
            }
        });



        mDb.postEventDesc(eventMap, eventShort.getId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", "addeed event");
                flag[0] = true;
            }

            @Override
            public void OnFail(String response) {
                flag[0] = false;
            }
        });

        if(!flag[0])
            return 3;



        return 0;
    }*/

    public void pushEventPhoto(String photo){
        storage.addPhoto(Uri.parse(photo), "event", new StorageUrlCallback() {
            @Override
            public void onUrlReceived(String url) {
                Log.e("zdjecie v1", url);
                addEventPhotoName.postValue(url);
            }
        });
    }

    public MutableLiveData<String> getPhotoName(){
        return addEventPhotoName;
    }

    public void pushEventShort(String name, String date, String location,
                                HashMap<String, List<String>> list){

        Map<String, Object> shortEvent = new HashMap<>();
        shortEvent.put("name", name);
        shortEvent.put("date", date);
        shortEvent.put("location", location);
        shortEvent.put("followersCount", 0);
        shortEvent.put("voivodeship", list.get("Województwo").get(0));
        shortEvent.put("tags", list.get("Tagi"));
        shortEvent.put("image", addEventPhotoName.getValue());

        Log.e("eventshot", (String)shortEvent.get("image"));
        mDb.postToDbEventWorkshop(shortEvent, "event", new DatabaseDataCallback() {

            @Override
            public void OnSuccess(List<?> response) {
                addEventId.postValue((String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
                addEventId.postValue("fail");
            }
        });
    }

    public MutableLiveData<String> getEventId(){
        return addEventId;
    }

    public void pushEventDesc(String name, String date, String location, String address,
                              String desc,HashMap<String, List<String>> checked){

        EventShort eventShort = new EventShort();
        Map<String, Object> list = new HashMap<>();
        list.put("name", name);
        list.put("date", date);
        list.put("location", location);
        list.put("followersCount", 0);
        list.put("voivodeship", checked.get("Województwo").get(0));
        list.put("tags", checked.get("Tagi"));
        Log.e("tags checLsd", String.valueOf(checked.get("Tagi").size()));
        list.put("image", addEventPhotoName.getValue());
        eventShort.setData(list, addEventId.getValue());


        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("description", desc);
        eventMap.put("location", "");
        eventMap.put("address", address);
        mDb.postToDbEventWorkshopDesc(eventMap, addEventId.getValue(), "event", new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", "addeed event");

                List<EventShort> currList = eventShortListLiveData.getValue();
                currList.add(0, eventShort);
                eventShortListLiveData.postValue(currList);

                addEventResponse.postValue("success");
            }

            @Override
            public void OnFail(String response) {
                addEventResponse.postValue("fail");
            }
        });
    }

    public MutableLiveData<String> getAddEventResponse(){
        return addEventResponse;
    }

    public Bitmap resizeImage(Uri imageUri, Activity activity) {
        Bitmap resizedBitmap = null;
        try {
            InputStream input = activity.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();

            int originalHeight = bitmap.getHeight();
            int targetHeight = originalHeight;

            int newHeight = targetHeight;
            int newWidth = 2 * newHeight;

            resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resizedBitmap;
    }


    public Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public void pushWorkshopPhoto(String photo){
        storage.addPhoto(Uri.parse(photo), "workshop", new StorageUrlCallback() {
            @Override
            public void onUrlReceived(String url) {
                Log.e("zdjecie v1", url);
                addWorkshopPhotoName.postValue(url);
            }
        });
    }

    public MutableLiveData<String> getWorkshopPhotoName(){
        return addWorkshopPhotoName;
    }

    public void pushWorkshopShort(String name, String location, HashMap<String, List<String>> list){
        Map<String, Object> workshop = new HashMap<>();
        workshop.put("name", name);
        workshop.put("location", location);
        workshop.put("voivodeship", list.get("Województwo").get(0));
        workshop.put("tags", list.get("Tagi"));
        workshop.put("image", addWorkshopPhotoName.getValue());
        Map<String, Long> map = new HashMap<>();
        map.put("1", (long)0);
        map.put("2", (long)0);
        map.put("3", (long)0);
        map.put("4", (long)0);
        map.put("5", (long)0);
        workshop.put("ratings", map);

        Log.e("eventshot", (String)workshop.get("image"));
        mDb.postToDbEventWorkshop(workshop, "workshop", new DatabaseDataCallback() {

            @Override
            public void OnSuccess(List<?> response) {
                addWorkshopId.postValue((String)response.get(0));
            }

            @Override
            public void OnFail(String response) {
                Log.e("Fail", response);
                addWorkshopId.postValue("fail");
            }
        });
    }

    public MutableLiveData<String> getWorkshopId(){
        return addWorkshopId;
    }

    public void pushWorkshopDesc(String name, String location, String address,
                              String desc,HashMap<String, List<String>> checked){

        Workshop workshop = new Workshop();
        Map<String, Object> list = new HashMap<>();
        list.put("name", name);
        list.put("location", location);
        list.put("voivodeship", checked.get("Województwo").get(0));
        list.put("tags", checked.get("Tagi"));
        list.put("image", addWorkshopPhotoName.getValue());
        Map<String, Long> map = new HashMap<>();
        map.put("1", (long)0);
        map.put("2", (long)0);
        map.put("3", (long)0);
        map.put("4", (long)0);
        map.put("5", (long)0);
        list.put("ratings", map);
        workshop.setData(list, addWorkshopId.getValue());


        Map<String, Object> workshopMap = new HashMap<>();
        workshopMap.put("description", desc);
        workshopMap.put("location", "");
        workshopMap.put("address", address);
        mDb.postToDbEventWorkshopDesc(workshopMap, addWorkshopId.getValue(), "workshop", new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.d("Success", "addeed event");

                List<Workshop> currList = workshopsLiveData.getValue();
                if(currList != null) {
                    currList.add(0, workshop);
                    workshopsLiveData.postValue(currList);
                }
                addWorkshopResponse.postValue("success");
            }

            @Override
            public void OnFail(String response) {
                addWorkshopResponse.postValue("fail");
            }
        });
    }

    public MutableLiveData<String> getAddWorkshopResponse(){
        return addWorkshopResponse;
    }
}