package com.example.drivefest.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.model.Expense;
import com.example.drivefest.data.model.TechData;
import com.example.drivefest.data.repository.FirebaseAuthRepository;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarRegisterViewModel extends ViewModel {

    private FirebaseFirestoreRepository mDb;
    private FirebaseAuthRepository mAuth;
    private MutableLiveData<TechData> techDataLiveData;
    private MutableLiveData<String> toastLiveData;
    private MutableLiveData<List<Expense>> expensesListLiveData;

    public CarRegisterViewModel(){
        mDb = FirebaseFirestoreRepository.getDbInstance();
        mAuth = FirebaseAuthRepository.getRepoInstance();
        techDataLiveData = new MutableLiveData<>();
        toastLiveData = new MutableLiveData<>();
        expensesListLiveData = new MutableLiveData<>();
    }

    public void updateTech(String insuranceDate, String techDate, String course, String courseEngineOil,
                           String courseTransmissionOil){
        Map<String, Object> map = new HashMap<>();
        map.put("insurance", insuranceDate);
        map.put("tech", techDate);
        map.put("course", course);
        map.put("engine", courseEngineOil);
        map.put("transmission", courseTransmissionOil);
        TechData techData = new TechData();
        techData.setData(map);
        techDataLiveData.postValue(techData);
        mDb.updateRegisterTechData(map, mAuth.getCurrentUserId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.e("updatetechvm", "success");

                toastLiveData.postValue("Pomyślnie dodano dane!");
            }

            @Override
            public void OnFail(String response) {
                Log.e("updatetechvm ", "fail");
                toastLiveData.postValue("Dodanie danych niepowiodło się!");
            }
        });
    }

    public MutableLiveData<TechData> getTechDataLiveData(){
        return techDataLiveData;
    }

    public MutableLiveData<String> getToastLiveData(){
        return toastLiveData;
    }

    public void fetchCarRegister(){
        mDb.getCarRegister(mAuth.getCurrentUserId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                if(response != null){
                    TechData techData = new TechData();
                    techData.setData((Map<String, Object>)response.get(0));
                    techDataLiveData.postValue(techData);
                }
            }

            @Override
            public void OnFail(String response) {
                toastLiveData.postValue(response);
            }
        });
    }

    public void clearToast(){
        toastLiveData = new MutableLiveData<>();
    }

    public void clearRegister(){
        mDb.deleteRegister(mAuth.getCurrentUserId());
        TechData techData = new TechData();
        techDataLiveData.postValue(techData);

            Log.e("Degbug delete register", "listnoteempty");
            mDb.deleteExpenses(mAuth.getCurrentUserId(), new DatabaseDataCallback() {
                @Override
                public void OnSuccess(List<?> response) {
                    toastLiveData.postValue((String) response.get(0));
                }

                @Override
                public void OnFail(String response) {
                    Log.e("Firestore", "Fail");
                }
            });
            expensesListLiveData = new MutableLiveData<>();

    }

    public void addExpense(String type, String desc, String price, String date){
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("description", desc);
        map.put("price",Math.round((Float.parseFloat(price) * 100.0f)) / 100.0f);
        map.put("date", date);

        mDb.addExpense(map, mAuth.getCurrentUserId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                Log.e("updatetechvm ", "success");
                toastLiveData.postValue("Pomyślnie dodano dane!");
            }

            @Override
            public void OnFail(String response) {
                Log.e("updatetechvm ", "fail");
                toastLiveData.postValue("Dodanie danych niepowiodło się!");
            }
        });


    }

    public void fetchExpensesList(){
        mDb.getExpensesList(mAuth.getCurrentUserId(), new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                expensesListLiveData.postValue((List<Expense>)response);
            }

            @Override
            public void OnFail(String response) {
                Log.e("Firestore", "Fail");
            }
        });
    }

    public MutableLiveData<List<Expense>> getExpensesListLiveData(){
        return expensesListLiveData;
    }

    public Expense getExpense(String id){
        for(Expense expense: expensesListLiveData.getValue()){
            if(expense.getId().equals(id))
                return expense;
        }
        return null;
    }

    public void udpateExpense(String id, String type, String desc, String price, String date){
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("description", desc);
        map.put("price",Math.round((Float.parseFloat(price) * 100.0f)) / 100.0f);
        map.put("date", date);

        mDb.updateExpense(mAuth.getCurrentUserId(), map, id, new DatabaseDataCallback() {
            @Override
            public void OnSuccess(List<?> response) {
                toastLiveData.postValue((String) response.get(0));
               /* List<Expense> currList = expensesListLiveData.getValue();
                for(Expense exp: currList)
                    if(exp.getId().equals(id)) {
                        exp.setType(map.get("type").toString());
                        exp.setDescription(map.get("description").toString());
                        exp.setPrice(Float.parseFloat(map.get("price").toString()));
                        exp.setDateStr(map.get("date").toString());
                    }
                */
            }

            @Override
            public void OnFail(String response) {
                toastLiveData.postValue(response);
            }
        });
    }

    public void deleteExpense(String id){
        mDb.deleteExpense(id, mAuth.getCurrentUserId());
        List<Expense> currList = expensesListLiveData.getValue();
        if(currList.size() == 1)
            expensesListLiveData.postValue(new ArrayList<>());
        else{
            for(Expense exp: currList) {
                if (exp.getId().equals(id)) {
                    currList.remove(exp);
                    break;
                }
            }
            expensesListLiveData.postValue(currList);
        }
    }
}
