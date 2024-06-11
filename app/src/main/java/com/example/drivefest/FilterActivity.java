package com.example.drivefest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.drivefest.adapter.ExpandableListAdapter;
import com.example.drivefest.data.ExpandableListDataItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FilterActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    HashMap<String, List<String>> expandableDetailList;

    HashMap<String, List<String>> filterListElems; // to do adaptera

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
        expandableDetailList = ExpandableListDataItems.getData();
        expandableTitleList = new ArrayList<>(expandableDetailList.keySet());
        expandableListAdapter = new ExpandableListAdapter(this, expandableTitleList, expandableDetailList);
        filterListElems = new HashMap<>();
        filterListElems.put(expandableTitleList.get(0), new ArrayList<>());
        filterListElems.put(expandableTitleList.get(1), new ArrayList<>());
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(groupPosition ->
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show());

        expandableListView.setOnGroupCollapseListener(groupPosition ->
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show());

       /* expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition)
                    + " -> "
                    + expandableDetailList.get(
                    expandableTitleList.get(groupPosition)).get(
                    childPosition), Toast.LENGTH_SHORT
            ).show();

            return false;
        });*/

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

    public void onAcceptClick(View view){
        //sprawddzic zaznaczenie checkoboxw, sciagnac daty i przekazac do home
    }
}
