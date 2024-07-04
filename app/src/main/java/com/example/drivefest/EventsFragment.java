package com.example.drivefest;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivefest.adapter.ClickListener;
import com.example.drivefest.adapter.EventListAdapter;
import com.example.drivefest.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventsFragment extends Fragment{
    private HomeViewModel homeVM;
    private EventListAdapter eventListAdapter;
    private RecyclerView list;
    private LinearLayout linearLayout;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        homeVM = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        list = view.findViewById(R.id.event_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(getContext(), 1));

        eventListAdapter = new EventListAdapter(getContext(), new ArrayList<>(), new ClickListener() {
            @Override
            public void onClick(String id) {
                // Navigate to the event description fragment or activity
                Bundle bundle = new Bundle();
                bundle.putParcelable("event_id",(Parcelable) homeVM.getEventShortList().getValue().get(Integer.valueOf(id)));
                EventDescFragment fragment = new EventDescFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,fragment, "descFragment")
                        .addToBackStack("desc")
                        .commit();
            }
        });
        list.setAdapter(eventListAdapter);

        homeVM.getEventShortList().observe(getViewLifecycleOwner(), eventShorts -> {
            Log.e("cos", "nietego");
            //eventListAdapter.updateData(eventShorts);
            homeVM.getFavEventShortLiveData().observe(getViewLifecycleOwner(), favEventShorts -> {
                homeVM.setFollowedEvents();
                eventListAdapter.updateData(eventShorts);
            });
        });

        linearLayout = view.findViewById(R.id.btnLayout);
        searchView = view.findViewById(R.id.searchView);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                linearLayout.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.scrollToPosition(0);
                homeVM.setFilteredList(newText, null, null, null);
                eventListAdapter.updateData(homeVM.getFilteredList());
                return true;
            }
        });

        Button buttonWyszukaj = view.findViewById(R.id.eventsButtonWyszukaj);
        buttonWyszukaj.setOnClickListener(v -> {
            linearLayout.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
        });

        Button buttonSortuj = view.findViewById(R.id.eventsButtonSortuj);
        buttonSortuj.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("sortFragment");

            if(fragment == null) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.container, new SortEventFragment(), "sortFragment")
                        .hide(this)
                        .addToBackStack(null)
                        .commit();
            }else {
                fragmentManager
                        .beginTransaction()
                        .hide(this)
                        .show(fragment)
                        .commit();
            }
        });
        Button buttonFiltruj = view.findViewById(R.id.eventsButtonFiltruj);
        buttonFiltruj.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("filterFragment");

            if(fragment == null) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.container, new FilterEventFragment(), "filterFragment")
                        .hide(this)
                        .addToBackStack(null)
                        .commit();
            }else {
                fragmentManager
                        .beginTransaction()
                        .hide(this)
                        .show(fragment)
                        .commit();
            }
        });

        getParentFragmentManager().setFragmentResultListener("sortResult", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Log.e("Wchodzi", "jak w maslo2");
                String sort = bundle.getString("sort");
                eventListAdapter.updateSortCriteria(sort);
                if(sort.equals("xdefault")) {
                    if (homeVM.getFilteredList().isEmpty()) {
                        Log.d("debug list orig", homeVM.getEventShortList().getValue().get(1).getName());
                        eventListAdapter.updateData(homeVM.getEventShortList().getValue());
                    }
                    else {
                        Log.d("debug list", homeVM.getFilteredList().get(1).getName());
                        eventListAdapter.updateData(homeVM.getFilteredList());
                    }
                }
                list.scrollToPosition(0);
            }
        });

        getParentFragmentManager().setFragmentResultListener("filterResult", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                homeVM.setFilteredList(null, bundle.getString("startDate"),
                        bundle.getString("endDate"),
                        (HashMap<String, List<String>>)bundle.getSerializable("checkedItems"));
                eventListAdapter.updateData(homeVM.getFilteredList());
                list.scrollToPosition(0);
            }
        });

        return view;
    }

    public boolean hideSearchView() {
        if(searchView.getVisibility() == View.VISIBLE) {
            linearLayout.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}
