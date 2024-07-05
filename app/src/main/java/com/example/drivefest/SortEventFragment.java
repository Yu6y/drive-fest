package com.example.drivefest;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
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

        if(getArguments() != null) {
            Log.e("args", String.valueOf(getArguments().size()));
            radioGroup.check(getArguments().getInt("sort"));
        }
        else
            radioGroup.check(R.id.xdefault);


        Button acceptButton = view.findViewById(R.id.sortbuttonAccept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                RadioButton button = getView().findViewById(radioGroup.getCheckedRadioButtonId());
                bundle.putInt("sort", button.getId());
                Log.e("sort id", String.valueOf(button.getId()));
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.setFragmentResult("sortResult", bundle);
                Fragment currFragment = fragmentManager.findFragmentByTag("sortFragment");
                fragmentManager
                        .beginTransaction()
                        .hide(currFragment)
                        .commit();
                fragmentManager.popBackStack();
            }
        });
        return view;
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
        Fragment fragment = fragmentManager.findFragmentByTag("sortFragment");
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
            fragmentManager.popBackStack();
        }
    }


}