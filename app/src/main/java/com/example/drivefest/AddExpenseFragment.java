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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.drivefest.data.model.Expense;
import com.example.drivefest.viewmodel.CarRegisterViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddExpenseFragment extends Fragment {

    private CarRegisterViewModel carVM;
    private RadioGroup radioGroup;
    private EditText desc, price, date;
    private Button acceptBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        carVM = new ViewModelProvider(requireActivity()).get(CarRegisterViewModel.class);
        radioGroup = view.findViewById(R.id.expense_radio_group);
        radioGroup.check(R.id.fuel);

        desc = view.findViewById(R.id.expense_info);
        price = view.findViewById(R.id.expense_price);
        date = view.findViewById(R.id.expense_date);
        acceptBtn = view.findViewById(R.id.add_expense_accept);

        if(getArguments() != null){
            Expense expense = carVM.getExpense(getArguments().getString("id"));
            desc.setText(expense.getDescription());
            price.setText(String.valueOf(expense.getPrice()));
            date.setText(expense.getDateStr());
            switch(expense.getType()){
                case "fuel":
                    radioGroup.check(R.id.fuel);
                    break;
                case "service":
                    radioGroup.check(R.id.service);
                    break;
                case "parking":
                    radioGroup.check(R.id.parking);
                    break;
                case "other":
                    radioGroup.check(R.id.other);
                    break;
                }
        }
        acceptBtn.setOnClickListener(v -> {
            if(checkData()){
                RadioButton button = view.findViewById(radioGroup.getCheckedRadioButtonId());
                if(getArguments() != null)
                    carVM.udpateExpense(getArguments().getString("id"), getResources().getResourceEntryName(button.getId()), desc.getText().toString(),
                            price.getText().toString(), date.getText().toString());
                else
                    carVM.addExpense(getResources().getResourceEntryName(button.getId()), desc.getText().toString(),
                        price.getText().toString(), date.getText().toString());
            }
        });

        carVM.getToastLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                carVM.fetchExpensesList();
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                close();
            }
        });
        date.setOnClickListener(v -> pickDate(date));
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
            ((HomeActivity) activity).setupDrawerToggle(true);
        }
    }
    public void close() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("addExpenseFragment");
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
            fragmentManager.popBackStack();
        }
    }

    private boolean checkData(){
        if(desc.getText().toString().trim().isEmpty()){
            desc.setError("Podaj dane!");
            return false;
        }
        if(price.getText().toString().trim().isEmpty()){
            price.setText("Podaj dane!");
            return false;
        }
        else{
            try {
                float number = Float.parseFloat(price.getText().toString().trim());
            } catch (NumberFormatException e) {
                price.setError("Błędne dane!");
                return false;
            }
        }
        if(date.getText().toString().trim().isEmpty()){
            date.setError("Podaj dane!");
            return false;
        }

        return true;
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
                        String date = new SimpleDateFormat("dd-MM-yyyy").format(selectedDate.getTime());

                        editText.setText(date);
                    }
                }, year, month, day);

        datePicker.show();
    }
}