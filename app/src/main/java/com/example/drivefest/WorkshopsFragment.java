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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivefest.adapter.ClickListener;
import com.example.drivefest.adapter.WorkshopListAdapter;
import com.example.drivefest.data.model.Workshop;
import com.example.drivefest.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkshopsFragment extends Fragment {
    private HomeViewModel homeVM;
    private WorkshopListAdapter workshopListAdapter;
    private RecyclerView list;
    private LinearLayout linearLayout;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workshops, container, false);

        homeVM = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeVM.fetchWorkshopsList();

        list = view.findViewById(R.id.workshop_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(getContext(), 1));


        workshopListAdapter = new WorkshopListAdapter(getContext(), new ArrayList<>(), new ClickListener() {
            @Override
            public void onClick(String id) {

                Bundle bundle = new Bundle();
                Workshop foundWorkshop = null;
                Log.e("tu dochodzi","II");
                for (Workshop workshop : homeVM.getWorkshopsLiveData().getValue()) {
                    Log.e("dibug workshp[", workshop.getId() + " = " + id);
                    if (workshop.getId().equals(id)) {
                        foundWorkshop = workshop;
                        break;
                    }
                }
                if (foundWorkshop != null) {

                    bundle.putParcelable("workshop_id", foundWorkshop);
                    System.out.println("Znaleziono i umieszczono obiekt w Bundle: " + foundWorkshop.getName());
                } else {
                    System.out.println("Nie znaleziono obiektu o id: " + id);
                }

                WorkshopDescFragment fragment = new WorkshopDescFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,fragment)
                        .commit();
            }
        });
        list.setAdapter(workshopListAdapter);

        homeVM.getWorkshopsLiveData().observe(getViewLifecycleOwner(), workshops -> {
            Log.e("cos", "nietego");
            workshopListAdapter.updateData(workshops);
        });

        linearLayout = view.findViewById(R.id.workshopbtnLayout);
        searchView = view.findViewById(R.id.workshopsearchView);

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
                homeVM.setWorkshopsFilteredList(newText,null, null);
                workshopListAdapter.updateData(homeVM.getWorkshopsFiltered());
                return true;
            }
        });

            Button buttonWyszukaj = view.findViewById(R.id.workshopButtonWyszukaj);
            buttonWyszukaj.setOnClickListener(v -> {
                linearLayout.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
            });

            Button buttonSortuj = view.findViewById(R.id.workshopButtonSortuj);
        buttonSortuj.setOnClickListener(v -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("sortWorkshopFragment");

                if(fragment == null) {
                    fragmentManager
                            .beginTransaction()
                            .add(R.id.container, new SortWorkshopFragment(), "sortWorkshopFragment")
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
            Button buttonFiltruj = view.findViewById(R.id.workshopButtonFiltruj);
            buttonFiltruj.setOnClickListener(v -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("filterWorkshopFragment");

                if(fragment == null) {
                    fragmentManager
                            .beginTransaction()
                            .add(R.id.container, new FilterWorkshopFragment(), "filterWorkshopFragment")
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

            getParentFragmentManager().setFragmentResultListener("sortWorkshopResult", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                    Log.e("Wchodzi", "jak w maslo2");
                    String sort = bundle.getString("sort");
                    workshopListAdapter.updateSortCriteria(sort);
                    if(sort.equals("wxdefault")) {
                        if (homeVM.getWorkshopsFiltered().isEmpty()) {
                            Log.d("debug wksplist orig", homeVM.getWorkshopsLiveData().getValue().get(1).getName());
                            workshopListAdapter.updateData(homeVM.getWorkshopsLiveData().getValue());
                        }
                        else {
                            Log.d("debug wkshplist", homeVM.getWorkshopsFiltered().get(1).getName());
                            workshopListAdapter.updateData(homeVM.getWorkshopsFiltered());
                        }
                    }
                    list.scrollToPosition(0);
                }
            });

            getParentFragmentManager().setFragmentResultListener("filterWorkshopResult", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                    Log.e("Wokshop rate", bundle.getString("rate"));
                    homeVM.setWorkshopsFilteredList(null, bundle.getString("rate"),
                            (HashMap<String, List<String>>)bundle.getSerializable("checkedItems"));
                    workshopListAdapter.updateData(homeVM.getWorkshopsFiltered());
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