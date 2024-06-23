package com.example.drivefest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.drivefest.adapter.ExpandableListAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    HashMap<String, List<String>> expandableDetailList;
    EditText startDateEditText;
    EditText endDateEditText;
    HashMap <String, List<String>> checkedBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.filter_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        expandableListView = findViewById(R.id.expandableListView);
        expandableListAdapter = new ExpandableListAdapter(this);//usunac stad listy, przekzywac context
        expandableListView.setAdapter(expandableListAdapter);

        startDateEditText = findViewById(R.id.editTextDateOd);
        endDateEditText = findViewById(R.id.editTextDateDo);

        Intent intent = getIntent();
        if(intent.hasExtra("checkedItems") && intent.hasExtra("startDate")
        && intent.hasExtra("endDate")){

            String startDate = String.valueOf(intent.getSerializableExtra("startDate"));
            String endDate = String.valueOf(intent.getSerializableExtra("endDate"));
            Log.e("debug date", startDate);
            if(!startDate.isEmpty())
                startDateEditText.setText(startDate);
            if(!endDate.isEmpty())
                endDateEditText.setText(endDate);
            checkedBoxes = (HashMap<String, List<String>>) intent.getSerializableExtra("checkedItems");
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

    @Override
    public void onBackPressed() {
        /*Intent resultIntent = new Intent();
        resultIntent.putExtra("checkedItems", (Serializable) getCheckedItems());
        setResult(RESULT_OK, resultIntent);*/
        super.onBackPressed();
    }

    public void onAcceptClick(View view) {
        String startDate = String.valueOf(startDateEditText.getText());
        String endDate = String.valueOf(endDateEditText.getText());
        if(dateCheck(startDate, endDate)) {
            Intent resultIntent = new Intent();
                resultIntent.putExtra("startDate", (Serializable) startDate);
                resultIntent.putExtra("endDate", (Serializable) endDate);

            resultIntent.putExtra("checkedItems", (Serializable) expandableListAdapter.getCheckedBoxes());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    public void pickStartDate(View view){
        pickDate(startDateEditText);
    }
    public void pickEndDate(View view){
        pickDate(endDateEditText);
    }

    public void pickDate(EditText editText){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this,
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

    public void clearClick(View view){
        startDateEditText.getText().clear();
        endDateEditText.getText().clear();
        expandableListAdapter.updateCheckedSet(new HashSet<>());
        expandableListAdapter.notifyDataSetChanged();
    }
}
