package com.example.drivefest;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drivefest.viewmodel.CarRegisterViewModel;
import com.example.drivefest.viewmodel.HomeViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class UpdateCarFragment extends Fragment {
    private CarRegisterViewModel carVM;
    private EditText insurance, tech, course, engineOil, transmissionOil;
    private Button acceptBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_update_car, container, false);

        carVM = new ViewModelProvider(requireActivity()).get(CarRegisterViewModel.class);

        insurance = view.findViewById(R.id.update_insurance);
        tech = view.findViewById(R.id.update_tech);
        course = view.findViewById(R.id.udpate_course);
        engineOil = view.findViewById(R.id.update_engine_oil);
        transmissionOil = view.findViewById(R.id.update_transmission_oil);
        acceptBtn = view.findViewById(R.id.update_data_button);

        acceptBtn.setOnClickListener(v -> {
            if(checkData()){
                carVM.updateTech(insurance.getText().toString(),
                    tech.getText().toString(), course.getText().toString().trim(), engineOil.getText().toString().trim(),
                    transmissionOil.getText().toString().trim());
            }
        });
        if (getArguments() != null) {
            String insuranceStr = getArguments().getString("insurance");
            String techStr = getArguments().getString("tech");
            String courseStr = getArguments().getString("course");
            String engineOilStr = getArguments().getString("engineOil");
            String transmissionOilStr = getArguments().getString("transmisisonOil");

            if(!insuranceStr.isEmpty())
                insurance.setText(insuranceStr);
            if(!techStr.isEmpty())
                tech.setText(techStr);
            if(!courseStr.isEmpty())
                course.setText(courseStr);
            if(!engineOilStr.isEmpty())
                engineOil.setText(engineOilStr);
            if(!transmissionOilStr.isEmpty())
                transmissionOil.setText(transmissionOilStr);
        }

        carVM.getToastLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                close();
            }
        });

        insurance.setOnClickListener(v -> pickDate(insurance));
        tech.setOnClickListener(v -> pickDate(tech));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e("debug lifeecycle update", "onviewcreatedbeforesuper");
        super.onViewCreated(view, savedInstanceState);
        Log.e("debug lifeecycle update", "onviewcreated");
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
        Log.e("debug lifeecycle update", "ondestroyviewbeforesuepr");
        super.onDestroyView();
        Log.e("debug lifeecycle update", "ondestroyview");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ((HomeActivity) activity).setupDrawerToggle(true);
        }

    }
    private void close() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("updateCarFragment");
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
            fragmentManager.popBackStack();
        }
    }

    private void pickDate(EditText editText){
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

    private boolean checkData(){
        if(!course.getText().toString().trim().isEmpty()){
            try {
                int number = Integer.parseInt(course.getText().toString().trim());
            } catch (NumberFormatException e) {
                course.setError("Błędne dane!");
                return false;
            }
        }
        if(!engineOil.getText().toString().trim().isEmpty()){
            try {
                int number = Integer.parseInt(engineOil.getText().toString().trim());
            } catch (NumberFormatException e) {
                engineOil.setError("Błędne dane!");
                return false;
            }
        }
        if(!transmissionOil.getText().toString().trim().isEmpty()){
            try {
                int number = Integer.parseInt(transmissionOil.getText().toString().trim());
            } catch (NumberFormatException e) {
                transmissionOil.setError("Błędne dane!");
                return false;
            }
        }

        return true;

    }

}