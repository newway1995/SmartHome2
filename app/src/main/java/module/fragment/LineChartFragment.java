package module.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import utils.TimeUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-28
 * Time: 20:53
 * 折线图
 */
public class LineChartFragment extends Fragment{

    /** LineChart */
    private LineChart lineChart;
    /** X轴数据 */
    private ArrayList<String> xValues;
    /** Y轴数据 */
    private ArrayList<Entry> lineYValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_chart_line, container, false);
        initData();
        initView(parentView);
        return parentView;
    }

    /**
     * 初始化视图
     */
    private void initView(View parentView) {
        lineChart = (LineChart)parentView.findViewById(R.id.energy_fragment_lineChart);
        lineChart.setDescription("过去一个月风扇电量消耗");
        //lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        lineChart.setHighlightEnabled(true);
        // enable touch gestures
        lineChart.setTouchEnabled(true);
        lineChart.setDragDecelerationFrictionCoef(0.95f);
        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        // set an alternative background color
        lineChart.setBackgroundColor(Color.WHITE);

        lineChart.animateX(2500);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(200f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTypeface(tf);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaxValue(900);
        rightAxis.setStartAtZero(false);
        rightAxis.setAxisMinValue(-200);
        rightAxis.setDrawGridLines(false);


        /** Current 30 days data */
        LineDataSet currentMonthData = new LineDataSet(lineYValues, "Current 30 days");
        currentMonthData.setAxisDependency(YAxis.AxisDependency.RIGHT);
        currentMonthData.setColor(Color.RED);
        currentMonthData.setCircleColor(Color.WHITE);
        currentMonthData.setLineWidth(2f);
        currentMonthData.setCircleSize(3f);
        currentMonthData.setFillAlpha(65);
        currentMonthData.setFillColor(Color.RED);
        currentMonthData.setDrawCircleHole(false);
        currentMonthData.setHighLightColor(Color.rgb(244, 117, 117));


        /** All DataSet */
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(currentMonthData);

        /** Need Show Data */
        LineData data = new LineData(xValues, dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        xValues = TimeUtils.getCurrentMonthDay();//X 轴数据
        lineYValues = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            float val = (float) (Math.random() * 100) + 100 / 3;
            lineYValues.add(new Entry((int) val, i));
        }
    }

}
