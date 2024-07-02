package com.example.drivefest;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.drivefest.adapter.PassStringListener;

import java.io.Serializable;

public class SortEventFragment extends Fragment {
    private RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort_event, container, false);

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.xdefault);

        Button acceptButton = view.findViewById(R.id.sortbuttonAccept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                RadioButton button = getView().findViewById(radioGroup.getCheckedRadioButtonId());
                bundle.putString("sort", getResources().getResourceEntryName(button.getId()));

                getParentFragmentManager().setFragmentResult("sortResult", bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("eventsFragment");
                Fragment currFragment = fragmentManager.findFragmentByTag("sortFragment");
                fragment.setArguments(bundle);
                fragmentManager
                        .beginTransaction()
                        .hide(currFragment)
                        .show(fragment)
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedButton", radioGroup.getCheckedRadioButtonId());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            int selectedRadioButtonId = savedInstanceState.getInt("selectedButton", -1);
            if (selectedRadioButtonId != -1) {
                radioGroup.check(selectedRadioButtonId);
            }
        }
    }

}