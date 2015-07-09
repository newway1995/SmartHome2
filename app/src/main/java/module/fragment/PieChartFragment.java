package module.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;

import constant.EnergyConstant;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-28
 * Time: 20:53
 * 大饼图
 */
public class PieChartFragment extends Fragment{

    /** piChart视图*/
    private PieChart pieChart;
    /** X轴数据 */
    private ArrayList<String> xValues;
    /** Y轴数据 */
    private ArrayList<BarEntry> yValues;
    private ArrayList<Entry> lineYValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_chart_pie, container, false);
        initData();
        initView(parentView);
        return parentView;
    }

    /**
     * 初始化视图
     */
    private void initView(View parentView) {
        pieChart = (PieChart)parentView.findViewById(R.id.energy_fragment_piChart);

        pieChart.setVisibility(View.VISIBLE);
        pieChart.setDescription("");
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextSize(20);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setCenterText("今天风扇电量消耗");
        setPieChartData(3, 100);
        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    private void setPieChartData(int count, float range) {
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        ArrayList<Entry> yVals1 = new ArrayList<>();

        //for (int i = 0; i < count + 1; i++) {
            //yVals1.add(new Entry(0.0f, i));
        //}
        yVals1.add(new Entry(1.0f, 0));
        yVals1.add(new Entry(53.6f, 1));
        yVals1.add(new Entry(44.4f, 2));
        yVals1.add(new Entry(1.0f, 3));

        ArrayList<String> xVals = new ArrayList<>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(EnergyConstant.FAN_TODAY_X[i % EnergyConstant.FAN_TODAY_X.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "时间段");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }
}
