package com.example.drivefest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.example.drivefest.adapter.ExpandableListAdapter;
import com.example.drivefest.data.ExpandableListDataItems;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterEventFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableTitleList;
    private HashMap<String, List<String>> expandableDetailList;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private HashMap <String, List<String>> checkedBoxes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_event, container, false);

        expandableListView = view.findViewById(R.id.expandableListView);
        expandableListAdapter = new ExpandableListAdapter(getContext(), ExpandableListDataItems.getEventData());//usunac stad listy, przekzywac context
        expandableListView.setAdapter(expandableListAdapter);

        startDateEditText = view.findViewById(R.id.editTextDateOd);
        endDateEditText = view.findViewById(R.id.editTextDateDo);
        startDateEditText.setOnClickListener(this::pickStartDate);
        endDateEditText.setOnClickListener(this::pickEndDate);

        Button acceptBtn = view.findViewById(R.id.filterButtonAccept);
        acceptBtn.setOnClickListener(v -> {
               accept();
        });
        Button clearBtn = view.findViewById(R.id.filterButtonClear);
        clearBtn.setOnClickListener(v -> {
               clear();
        });

        synchronizeCheckboxStates();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("startDate", String.valueOf(startDateEditText.getText()));
        outState.putString("endDate", String.valueOf(endDateEditText.getText()));
        outState.putSerializable("checkedItems", (Serializable) expandableListAdapter.getCheckedBoxes());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            String startDate = savedInstanceState.getString("startDate");
            String endDate = savedInstanceState.getString("endDate");
            //HashMap<String, List<String>> checkedItems = (HashMap<String, List<String>>)
              //      savedInstanceState.getSerializable("checkedItems");

            if(!startDate.isEmpty())
                startDateEditText.setText(startDate);
            if(!endDate.isEmpty())
                endDateEditText.setText(endDate);
            Set<String> checkBoxesItems = new HashSet<>();
            for(String key: checkedBoxes.keySet()){
                for(String elem: checkedBoxes.get(key)){
                    checkBoxesItems.add(elem);
                }
            }

            expandableListAdapter.updateCheckedSet(checkBoxesItems);
            expandableListAdapter.notifyDataSetChanged();
        }

        synchronizeCheckboxStates();
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

    public boolean dateCheck(String dateStart, String dateEnd){
        if(!dateStart.isEmpty() && !dateEnd.isEmpty()) {
            LocalDate start = LocalDate.parse(dateStart);
            LocalDate end = LocalDate.parse(dateEnd);

            if (start.isAfter(end)) {
                startDateEditText.setError("Bad date");
                endDateEditText.setError("Bad date");
                return false;
            }
        }
        return true;
    }

    public void pickDate(EditText editText){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate.getTime());

                        editText.setText(date);
                    }
                }, year, month, day);

        datePicker.show();
    }

    public void pickStartDate(View view){
        pickDate(startDateEditText);
    }
    public void pickEndDate(View view){
        pickDate(endDateEditText);
    }

    public void clear(){
        startDateEditText.getText().clear();
        endDateEditText.getText().clear();
        expandableListAdapter.updateCheckedSet(new HashSet<>());
        expandableListAdapter.notifyDataSetChanged();
    }

    public void accept(){
        Bundle bundle = new Bundle();
        String startDate = String.valueOf(startDateEditText.getText());
        String endDate = String.valueOf(endDateEditText.getText());
        if(dateCheck(startDate, endDate)) {
            bundle.putString("startDate", startDate);
            bundle.putString("endDate", endDate);

            bundle.putSerializable("checkedItems", (Serializable) expandableListAdapter.getCheckedBoxes());

            getParentFragmentManager().setFragmentResult("filterResult", bundle);
            FragmentManager fragmentManager = getParentFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("eventsFragment");

            fragment.setArguments(bundle);
            fragmentManager
                    .beginTransaction()
                    .hide(this)
                    .show(fragment)
                    .commit();
        }
    }
}