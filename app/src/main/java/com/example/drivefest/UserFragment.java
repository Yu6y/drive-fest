package com.example.drivefest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drivefest.adapter.ClickExpenseListener;
import com.example.drivefest.adapter.ExpenseListAdapter;
import com.example.drivefest.data.model.Expense;
import com.example.drivefest.data.model.TechData;
import com.example.drivefest.viewmodel.CarRegisterViewModel;
import com.example.drivefest.viewmodel.EventDescViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private Button updateBtn, deleteBtn, addExpenseBtn, showChartBtn;
    private EditText insurance, tech, course, engineOil, transmisisonOil;
    private CarRegisterViewModel carVM;
    private RecyclerView recyclerView;
    private ExpenseListAdapter expenseListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user, container, false);
        carVM = new ViewModelProvider(requireActivity()).get(CarRegisterViewModel.class);
        carVM.fetchCarRegister();
        carVM.fetchExpensesList();

        insurance = view.findViewById(R.id.register_insurance);
        tech = view.findViewById(R.id.register_tech);
        course = view.findViewById(R.id.register_course);
        engineOil = view.findViewById(R.id.register_engine_oil);
        transmisisonOil = view.findViewById(R.id.register_transmission_oil);
        updateBtn = view.findViewById(R.id.register_update);
        deleteBtn = view.findViewById(R.id.register_delete);
        addExpenseBtn = view.findViewById(R.id.add_expense_button);
        recyclerView = view.findViewById(R.id.expenses_list);
        showChartBtn = view.findViewById(R.id.show_chart);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        expenseListAdapter = new ExpenseListAdapter(getContext(), new ArrayList<>(), new ClickExpenseListener() {
            @Override
            public void onClick(String id, int action) {
                if(action == 0){
                    carVM.clearToast();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    Fragment fragment = new AddExpenseFragment();
                    fragment.setArguments(bundle);
                    Fragment currFragment = getParentFragmentManager().findFragmentByTag("userFragment");
                    getParentFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, fragment, "addExpenseFragment")
                            .hide(currFragment)
                            .addToBackStack("addExpense")
                            .commit();
                }
                else if(action == 1){
                    carVM.clearToast();
                    carVM.deleteExpense(id);
                }
            }
        });
        recyclerView.setAdapter(expenseListAdapter);


        carVM.getExpensesListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                expenseListAdapter.updateData(expenses);
                //recyclerView.scrollToPosition(0);
            }
        });
        updateBtn.setOnClickListener(v -> {
            carVM.clearToast();
            Fragment currFragment = getParentFragmentManager().findFragmentByTag("userFragment");
            Fragment fragment = new UpdateCarFragment();
            Bundle bundle = new Bundle();
            bundle.putString("insurance", insurance.getText().toString());
            bundle.putString("tech", tech.getText().toString());
            if(course.getText().toString().isEmpty())
                bundle.putString("course", course.getText().toString());
            else
                bundle.putString("course", course.getText().toString().substring(0, course.getText().toString().length() - 3));
            if(engineOil.getText().toString().isEmpty())
                bundle.putString("engineOil", engineOil.getText().toString());
            else
                bundle.putString("engineOil", engineOil.getText().toString().substring(0, engineOil.getText().toString().length() - 3));
            if(transmisisonOil.getText().toString().isEmpty())
                bundle.putString("transmisisonOil", transmisisonOil.getText().toString());
            else
                bundle.putString("transmisisonOil", transmisisonOil.getText().toString().substring(0, transmisisonOil.getText().toString().length() - 3));
            fragment.setArguments(bundle);
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, "updateCarFragment")
                    .hide(currFragment)
                    .addToBackStack("updateCar")
                    .commit();
        });

        addExpenseBtn.setOnClickListener(v -> {
            carVM.clearToast();
            Log.e("debug", "addexprenseclick");
            Fragment currFragment = getParentFragmentManager().findFragmentByTag("userFragment");
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new AddExpenseFragment(), "addExpenseFragment")
                    .hide(currFragment)
                    .addToBackStack("addExpense")
                    .commit();
        });

        deleteBtn.setOnClickListener(v -> showConfirmationDialog());

        carVM.getTechDataLiveData().observe(getViewLifecycleOwner(), new Observer<TechData>() {
            @Override
            public void onChanged(TechData techData) {
                Log.e("updatetech", "success");
                insurance.setText(techData.getInsurance());
                tech.setText(techData.getTech());
                if(techData.getCourse() != null) {
                    if (techData.getCourse().isEmpty())
                        course.setText(techData.getCourse());
                    else
                        course.setText(techData.getCourse() + " km");
                }else
                    course.setText(techData.getCourse());
                if(techData.getEngineOil() != null) {
                    if (techData.getEngineOil().isEmpty())
                        engineOil.setText(techData.getEngineOil());
                    else
                        engineOil.setText(techData.getEngineOil() + " km");
                }else
                    engineOil.setText(techData.getEngineOil());
                if(techData.getTransmissionOil() != null) {
                    if (techData.getTransmissionOil().isEmpty())
                        transmisisonOil.setText(techData.getTransmissionOil());
                    else
                        transmisisonOil.setText(techData.getTransmissionOil() + " km");
                }else
                    transmisisonOil.setText(techData.getTransmissionOil());

            }
        });

        carVM.getToastLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(isVisible()){
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        showChartBtn.setOnClickListener(v ->{
            if(carVM.checkIfExpensesExist()){
                Fragment currFragment = getParentFragmentManager().findFragmentByTag("userFragment");
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new ChartFragment(), "chartFragment")
                        .hide(currFragment)
                        .addToBackStack("chart")
                        .commit();
            }
            else
                Toast.makeText(getContext(), "Brak wydatków!", Toast.LENGTH_SHORT).show();
        });
        //list.setposition(0) na observe list

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e("debug lifeecycle user", "onviewcreatedbeforesuper");
        super.onViewCreated(view, savedInstanceState);

        Log.e("debug lifeecycle user", "onviewcreated");

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
        Log.e("debug lifeecycle user", "ondestroyviewbeforesuper");
        super.onDestroyView();
        Log.e("debug lifeecycle user", "ondestroyview");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ((HomeActivity) activity).setupDrawerToggle(false);
        }
    }
    public void close() {
        carVM.clearToast();
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("userFragment");
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
            fragmentManager.popBackStack();
        }
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Potwierdzenie")
                .setMessage("Czy na pewno chcesz kontynuować?")
                .setPositiveButton("Tak", (dialog, which) -> {
                    carVM.clearRegister();
                    expenseListAdapter.updateData(new ArrayList<>());
                })
                .setNegativeButton("Nie", (dialog, which) -> {
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}