package com.example.drivefest;

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

public class SortWorkshopFragment extends Fragment {
    private RadioGroup radioGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sort_workshop, container, false);

        radioGroup = view.findViewById(R.id.workshopRadioGroup);
        radioGroup.check(R.id.wxdefault);

        Button acceptButton = view.findViewById(R.id.wrkshpsortbuttonAccept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                RadioButton button = getView().findViewById(radioGroup.getCheckedRadioButtonId());
                bundle.putString("sort", getResources().getResourceEntryName(button.getId()));

                getParentFragmentManager().setFragmentResult("sortWorkshopResult", bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("workshopsFragment");
                Fragment currFragment = fragmentManager.findFragmentByTag("sortWorkshopFragment");
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