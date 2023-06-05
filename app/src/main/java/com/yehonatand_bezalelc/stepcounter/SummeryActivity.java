package com.yehonatand_bezalelc.stepcounter;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class SummeryActivity extends MainActivity {
    private BarChart chart;
    private Button buttonSteps, buttonDistance, buttonTime, buttonCalories;
    private LinearLayout linearLayoutChart;
    private final UserData userData = UserData.getInstance();
    private List<Integer> dataPoints = new ArrayList<>();
    private int limit;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_summery;
    }

    @Override
    protected int getCurrentNavBarIdSelect() {
        return R.id.summery;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linearLayoutChart = findViewById(R.id.summery_linear_layout_chart);
        chart = findViewById(R.id.summery_chart);
        buttonSteps = findViewById(R.id.summery_button_steps);
        buttonDistance = findViewById(R.id.summery_button_distance);
        buttonTime = findViewById(R.id.summery_button_time);
        buttonCalories = findViewById(R.id.summery_button_calories);
        setSteps();
        updateChart();
        buttonSteps.setOnClickListener(view -> {
            setSteps();
            updateChart();
        });
        buttonDistance.setOnClickListener(view -> {
            setDistance();
            updateChart();
        });
//        buttonTime.setOnClickListener(view -> {
//            updateChart();
//        });
        buttonCalories.setOnClickListener(view -> {
            setCalories();
            updateChart();
        });
    }

    private void updateChart() {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>(userData.getHistory().keySet());

        for (int i = 0; i < dataPoints.size(); i++) {
            entries.add(new BarEntry(i, dataPoints.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Histogram");
        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.RED);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxis = chart.getAxisLeft(); // Get the Y-axis
        yAxis.removeAllLimitLines();
        yAxis.setTextColor(Color.BLUE); // Set the desired color for the Y-axis labels
        chart.getAxisLeft().setAxisLineColor(Color.GREEN);

        LimitLine limitLine = new LimitLine(limit, "Goal");
        limitLine.setLineColor(Color.BLUE); // Set the color of the line
        limitLine.setLineWidth(2f); // Set the width of the line
        limitLine.enableDashedLine(10f, 5f, 0f); // Enable a dashed line pattern
//        limitLine.setLabel("Goal");
        yAxis.addLimitLine(limitLine);

        chart.invalidate();
    }


    private void setSteps() {
        dataPoints.clear();
        for (String date : userData.getHistory().keySet()) {
            dataPoints.add(userData.getHistory().get(date));
        }
        limit = userData.getGoal();
    }

    private void setCalories() {
        dataPoints.clear();
        for (String date : userData.getHistory().keySet()) {
            dataPoints.add(userData.calculateCaloriesBurned(userData.getHistory().get(date)));
        }
        limit = userData.calculateCaloriesBurned(userData.getGoal());
    }

    private void setDistance() {
        dataPoints.clear();
        for (String date : userData.getHistory().keySet()) {
            dataPoints.add(userData.stepsToDistance(userData.getHistory().get(date)));
        }
        limit = userData.stepsToDistance(userData.getGoal());

    }
}