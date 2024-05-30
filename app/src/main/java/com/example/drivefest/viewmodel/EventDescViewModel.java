package com.example.drivefest.viewmodel;

import android.media.metrics.Event;

import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.repository.FirebaseFirestoreRepository;

public class EventDescViewModel extends ViewModel {

    FirebaseFirestoreRepository mDb;
    Event event;
}
