package com.example.drivefest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.drivefest.adapter.ExpandableListAdapter;
import com.example.drivefest.data.ExpandableListDataItems;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterWorkshopFragment extends Fragment {
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableTitleList;
    private HashMap<String, List<String>> expandableDetailList;
    private HashMap <String, List<String>> checkedBoxes;
    private RadioGroup radioGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter_workshop, container, false);

        radioGroup = view.findViewById(R.id.filterRadioGrp);
        radioGroup.check(R.id.allrates);
        expandableListView = view.findViewById(R.id.expandableListView);
        expandableListAdapter = new ExpandableListAdapter(getContext(), ExpandableListDataItems.getWorkshopData());
        expandableListView.setAdapter(expandableListAdapter);


        Button acceptBtn = view.findViewById(R.id.filterButtonAccept);
        acceptBtn.setOnClickListener(v -> {
            accept();
        });
        Button clearBtn = view.findViewById(R.id.filterButtonClear);
        clearBtn.setOnClickListener(v -> {
            clear();
        });
        if (getArguments() != null) {
                radioGroup.check(getArguments().getInt("rate"));
            HashMap<String, List<String>> checkedItems = (HashMap<String, List<String>>)
                    getArguments().getSerializable("checkedItems");


            Set<String> checkBoxesItems = new HashSet<>();
            for(String key: checkedItems.keySet()){
                for(String elem: checkedItems.get(key)){
                    checkBoxesItems.add(elem);
                }
            }

            expandableListAdapter.updateCheckedSet(checkBoxesItems);
            expandableListAdapter.notifyDataSetChanged();
            synchronizeCheckboxStates();
        }

        return view;
    }
    private void synchronizeCheckboxStates() {
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String item = expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition);
            Set<String> checkedItems = expandableListAdapter.getCheckedItems();
            if (checkedItems.contains(item)) {
                checkedItems.remove(item);
            } else {
                checkedItems.add(item);
            }

            expandableListAdapter.notifyDataSetChanged();
            return true;
        });
    }
    public void clear(){
        radioGroup.clearCheck();
        radioGroup.check(R.id.allrates);
        expandableListAdapter.updateCheckedSet(new HashSet<>());
        expandableListAdapter.notifyDataSetChanged();
    }
    public void accept(){
        Bundle bundle = new Bundle();
        RadioButton button = getView().findViewById(radioGroup.getCheckedRadioButtonId());

        bundle.putInt("rate", button.getId());
        bundle.putSerializable("checkedItems", (Serializable) expandableListAdapter.getCheckedBoxes());

        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.setFragmentResult("filterWorkshopResult", bundle);
        Fragment fragment = fragmentManager.findFragmentByTag("workshopsFragment");

        fragmentManager
                .beginTransaction()
                .hide(fragment)
                .commit();
        fragmentManager.popBackStack();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            ((HomeActivity) activity).setupDrawerToggle(true);

            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setNavigationOnClickListener(v -> close());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ((HomeActivity) activity).setupDrawerToggle(false);
        }
    }

    public void close() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("filterWorkshopFragment");
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
            fragmentManager.popBackStack();
        }
    }

}