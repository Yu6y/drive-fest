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

import androidx.activity.OnBackPressedCallback;
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
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment currFragment = fragmentManager.findFragmentByTag("workshopsFragment");
                WorkshopDescFragment fragment = new WorkshopDescFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.container,fragment, "workshopDesc")
                        .addToBackStack("workshopDesc")
                        .hide(currFragment)
                        .commit();
            }
        });
        list.setAdapter(workshopListAdapter);

        homeVM.getWorkshopsLiveData().observe(getViewLifecycleOwner(), workshops -> {

            workshopListAdapter.updateData(workshops);
            homeVM.getRatedWorkshop().observe(getViewLifecycleOwner(), ratedWorkshops -> {
                Log.e("cos", "kurwawaaaaaaaaaaaa");
                homeVM.setRatedWorkshops();
                workshopListAdapter.notifyDataSetChanged();
            });
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
                Fragment fragment = new SortWorkshopFragment();
                if(homeVM.getSortWorkshop() != -1) {
                    Log.e("workshopsort", "cos");
                    Bundle bundle = new Bundle();
                    bundle.putInt("sort", homeVM.getSortWorkshop());
                    fragment.setArguments(bundle);
                }
                    fragmentManager
                            .beginTransaction()
                            .add(R.id.container, fragment, "sortWorkshopFragment")
                            .hide(this)
                            .addToBackStack("sortWorkshop")
                            .commit();

            });
            Button buttonFiltruj = view.findViewById(R.id.workshopButtonFiltruj);
            buttonFiltruj.setOnClickListener(v -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment fragment = new FilterWorkshopFragment();
                if(homeVM.getRateWorkshop() != -1 && homeVM.getFilterCheckedItemsWorkshop() != null){
                    Bundle bundle = new Bundle();
                    bundle.putInt("rate", homeVM.getRateWorkshop());
                    bundle.putSerializable("checkedItems", homeVM.getFilterCheckedItemsWorkshop());
                    fragment.setArguments(bundle);
                }
                    fragmentManager
                            .beginTransaction()
                            .add(R.id.container, fragment, "filterWorkshopFragment")
                            .hide(this)
                            .addToBackStack("filterWorkshop")
                            .commit();

            });

            getParentFragmentManager().setFragmentResultListener("sortWorkshopResult", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                    Log.e("Wchodziworks", String.valueOf(bundle.getInt("sort")));
                    homeVM.setWorkshopSort(bundle.getInt("sort"));
                    String sort = getResources().getResourceEntryName(homeVM.getSortWorkshop());
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
                    Log.e("Wokshop rate", String.valueOf(bundle.getInt("rate")));
                    homeVM.setFilterWorkshopItems(bundle.getInt("rate"),
                            (HashMap<String, List<String>>)bundle.getSerializable("checkedItems"));

                    homeVM.setWorkshopsFilteredList(null, getResources().getResourceEntryName(homeVM.getRateWorkshop()),
                            homeVM.getFilterCheckedItemsWorkshop());
                    workshopListAdapter.updateData(homeVM.getWorkshopsFiltered());
                    list.scrollToPosition(0);
                }
            });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (searchView.getVisibility() == View.VISIBLE) {
                    hideSearchView();
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
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
}