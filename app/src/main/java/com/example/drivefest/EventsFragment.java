package com.example.drivefest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivefest.adapter.ClickListener;
import com.example.drivefest.adapter.EventListAdapter;
import com.example.drivefest.adapter.FollowClickListener;
import com.example.drivefest.data.model.EventShort;
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
        Log.e("lifecycle", "oncreateview");
        homeVM = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        list = view.findViewById(R.id.event_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(getContext(), 1));

        eventListAdapter = new EventListAdapter(getContext(), new ArrayList<>(), new ClickListener() {
            @Override
            public void onClick(String id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("event_id", (Parcelable) homeVM.getEventById(id));

                EventDescFragment fragment = new EventDescFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment currFragment = fragmentManager.findFragmentByTag("eventsFragment");
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragment, "descFragment")
                        .hide(currFragment)
                        .addToBackStack("desc")
                        .commit();
            }
        }, new FollowClickListener() {
            @Override
            public void onBtnClick(EventShort event, Button button, TextView text) {
                if(button.getText().equals("Obserwuj")) {
                    homeVM.followEvent(event);
                    button.setText("Obserwujesz");
                    Drawable newIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_white);
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                    button.setBackgroundResource(R.drawable.button_pressed);
                    button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    /*int count = event.getFollowersCount() + 1;
                    text.setText(String.valueOf(count));*/
                    eventListAdapter.notifyDataSetChanged();
                }
                else{
                    homeVM.unFollowEvent(event);
                    button.setText("Obserwuj");
                    Drawable newIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite);
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                    button.setBackgroundResource(R.drawable.custom_button);
                    button.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    /*int count = event.getFollowersCount() - 1;
                    text.setText(String.valueOf(count));*/
                    eventListAdapter.notifyDataSetChanged();
                }

            }
        });
        list.setAdapter(eventListAdapter);

        homeVM.getEventShortList().observe(getViewLifecycleOwner(), eventShorts -> {
            Log.e("cos", "nietego");
                eventListAdapter.updateData(eventShorts);
                homeVM.getFavEventShortLiveData().observe(getViewLifecycleOwner(), favEventShorts -> {
                homeVM.setFollowedEvents();
                eventListAdapter.notifyDataSetChanged();
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
            Fragment fragment = new SortEventFragment();
            if(homeVM.getSortEvent() != -1) {
                Bundle bundle = new Bundle();
                bundle.putInt("sort", homeVM.getSortEvent());
                fragment.setArguments(bundle);
            }
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment, "sortFragment")
                    .hide(this)
                    .addToBackStack("sort")
                    .commit();
        });

        Button buttonFiltruj = view.findViewById(R.id.eventsButtonFiltruj);
        buttonFiltruj.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            Fragment fragment = new FilterEventFragment();
            if(homeVM.getFilterStartDate() != null && homeVM.getFilterEndDate() != null
                    && homeVM.getFilterCheckedItems() != null){
                Bundle bundle = new Bundle();
                bundle.putString("startDate", homeVM.getFilterStartDate());
                bundle.putString("endDate", homeVM.getFilterEndDate());
                bundle.putSerializable("checkedItems", homeVM.getFilterCheckedItems());
                fragment.setArguments(bundle);
            }
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment, "filterFragment")
                    .hide(this)
                    .addToBackStack("filter")
                    .commit();

        });

        getParentFragmentManager().setFragmentResultListener("sortResult", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Log.e("Wchodzi", "jak w maslo2");
                homeVM.setEventSort(bundle.getInt("sort"));
                String sort = getResources().getResourceEntryName(homeVM.getSortEvent());
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
                homeVM.setFilterItems(bundle.getString("startDate"),
                        bundle.getString("endDate"),
                        (HashMap<String, List<String>>)bundle.getSerializable("checkedItems"));
                homeVM.setFilteredList(null, homeVM.getFilterStartDate(),
                        homeVM.getFilterEndDate(),
                        homeVM.getFilterCheckedItems());
                eventListAdapter.updateData(homeVM.getFilteredList());
                list.scrollToPosition(0);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                hideSearchView();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }



    public void hideSearchView() {
        if(searchView.getVisibility() == View.VISIBLE) {
            linearLayout.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
        }
    }

    public void reload(){
        eventListAdapter.notifyDataSetChanged();
    }
}
