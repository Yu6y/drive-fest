package com.example.drivefest;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drivefest.viewmodel.CarRegisterViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {
    private CarRegisterViewModel carVM;
    private LineChart lineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        carVM = new ViewModelProvider(requireActivity()).get(CarRegisterViewModel.class);

        lineChart = view.findViewById(R.id.chart);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(carVM.getChartDates()));
        xAxis.setLabelCount(carVM.getChartDates().size());
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(carVM.getChartAllValues().size());

        List<Entry> entriesTotal = new ArrayList<>();
        List<Entry> entriesFuel = new ArrayList<>();
        for (int i = 0; i < carVM.getChartDates().size(); i++) {
            try {
                entriesTotal.add(new Entry(i, carVM.getChartAllValues().get(i)));
                entriesFuel.add(new Entry(i, carVM.getFuelValues().get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LineDataSet dataSetTotal = new LineDataSet(entriesTotal, "Wszysktie wydatki");
        dataSetTotal.setColor(Color.BLUE);

        LineDataSet dataSetFuel = new LineDataSet(entriesFuel, "Paliwo");
        dataSetFuel.setColor(Color.RED);

        LineData lineData = new LineData(dataSetTotal, dataSetFuel);

        lineChart.setData(lineData);

        lineChart.invalidate();
        return view;
    }

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
        Fragment fragment = fragmentManager.findFragmentByTag("chartFragment");
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
            fragmentManager.popBackStack();
        }
    }
}