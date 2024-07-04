package com.example.drivefest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drivefest.adapter.ClickListener;
import com.example.drivefest.adapter.EventListAdapter;
import com.example.drivefest.viewmodel.HomeViewModel;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private EventListAdapter eventListAdapter;
    private RecyclerView favList;
    private HomeViewModel homeVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        homeVM = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        favList = view.findViewById(R.id.fav_event_list);
        favList.setHasFixedSize(true);
        favList.setLayoutManager(new GridLayoutManager(getContext(), 1));

        eventListAdapter = new EventListAdapter(getContext(), new ArrayList<>(), new ClickListener() {
            @Override
            public void onClick(String id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("event_id",(Parcelable) homeVM.getEventShortList().getValue().get(Integer.valueOf(id)));
                EventDescFragment fragment = new EventDescFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,fragment)
                        .commit();
            }
        });
        favList.setAdapter(eventListAdapter);

        homeVM.getFavEventShortLiveData().observe(getViewLifecycleOwner(), favEventShorts -> {
            homeVM.setFollowedEvents();
            eventListAdapter.updateData(favEventShorts);
        });


        return view;
    }
}