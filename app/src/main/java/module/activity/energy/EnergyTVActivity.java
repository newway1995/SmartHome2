package module.activity.energy;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import org.kymjs.aframe.database.KJDB;

import java.util.ArrayList;

import constant.EnergyConstant;
import framework.base.SwipeBackActivity;
import module.database.EnergyFanEntity;
import utils.TimeUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-10
 * Time: 14:53
 * 电视能耗统计
 */
public class EnergyTVActivity extends SwipeBackActivity implements OnChartValueSelectedListener {

    /** BarChart视图 */
    private BarChart barChart;
    /** piChart视图*/
    private PieChart pieChart;

    /** X轴数据 */
    private ArrayList<String> xValues;
    /** Y轴数据 */
    private ArrayList<BarEntry> yValues;

    @Override
    protected void initData() {
        super.initData();
        EnergyFanEntity.kjdb = KJDB.create(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        barChart = (BarChart)findViewById(R.id.energy_activity_tv_barChart);
        initBarChart();
    }

    /**
     * 初始化表格设置
     */
    private void initBarChart() {
        if (pieChart != null && pieChart.getVisibility() != View.GONE) {
            pieChart.setVisibility(View.GONE);
        }

        barChart.setDescription("过去一个月风扇电量消耗");
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(true);
        barChart.setHighlightEnabled(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.animateY(2500);
        barChart.animateX(1500);
        barChart.getLegend().setEnabled(false);

        xValues = TimeUtils.getCurrentMonthDay();//X 轴数据
        yValues = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            float val = (float) (Math.random() * 100) + 100 / 3;
            yValues.add(new BarEntry((int) val, i));
        }

        /** Current 30 days data */
        BarDataSet currentMonthData = new BarDataSet(yValues, "Current 30 days");
        currentMonthData.setColors(ColorTemplate.VORDIPLOM_COLORS);
        currentMonthData.setDrawValues(true);

        /** All DataSet */
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(currentMonthData);

        /** Need Show Data */
        BarData data = new BarData(xValues, dataSets);
        barChart.setData(data);
        barChart.invalidate();
    }

    private void showToday() {
        if (barChart != null && barChart.getVisibility() != View.GONE) {
            barChart.setVisibility(View.GONE);
        }
        pieChart = (PieChart)findViewById(R.id.energy_activity_tv_piChart);
        pieChart.setVisibility(View.VISIBLE);
        pieChart.setDescription("");
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextSize(20);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setOnChartValueSelectedListener(this);
        pieChart.setCenterText("今天风扇电量消耗");
        setPieChartData(3, 100);
        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }

    private void setPieChartData(int count, float range) {
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        ArrayList<Entry> yVals1 = new ArrayList<>();

        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * range) + range / 5, i));
        }

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

    private void showLast12Month() {
        if (barChart.getVisibility() != View.VISIBLE) {
            barChart.setVisibility(View.VISIBLE);
        }
        if (pieChart != null && pieChart.getVisibility() != View.GONE) {
            pieChart.setVisibility(View.GONE);
        }

        barChart.setDescription("过去12个月风扇电量消耗");
        xValues = TimeUtils.getLastMonthName();//X 轴数据
        yValues = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            float val = (float) (Math.random() * 100) + 100 / 3;
            yValues.add(new BarEntry((int) val, i));
        }

        /** Current 30 days data */
        BarDataSet currentMonthData = new BarDataSet(yValues, "Last 12 month");
        currentMonthData.setColors(ColorTemplate.VORDIPLOM_COLORS);
        currentMonthData.setDrawValues(true);

        /** All DataSet */
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(currentMonthData);

        /** Need Show Data */
        BarData data = new BarData(xValues, dataSets);
        barChart.setData(data);
        barChart.invalidate();
    }

    /**
     * 当数据被选择的时候
     * @param e 数据
     * @param dataSetIndex index
     * @param h HighLight
     */
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_energy_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.energy_item_currentMonth:
                initBarChart();
                break;
            case R.id.energy_item_today:
                showToday();
                break;
            case R.id.energy_item_last_12_month:
                showLast12Month();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.energy_activity_tv);
        setActionBarView(true);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
    }
}
