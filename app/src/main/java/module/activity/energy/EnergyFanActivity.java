package module.activity.energy;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import org.kymjs.aframe.database.KJDB;

import java.util.ArrayList;
import java.util.HashMap;

import constant.EnergyConstant;
import module.core.SwipeBackActivity;
import module.database.EnergyFanEntity;
import utils.TimeUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-09
 * Time: 14:25
 * 显示电风扇的能耗
 */
public class EnergyFanActivity extends SwipeBackActivity implements OnChartValueSelectedListener {

    /** BarChart视图 */
    private BarChart barChart;
    /** piChart视图*/
    private PieChart pieChart;
    /** LineChart */
    private LineChart lineChart;

    /** X轴数据 */
    private ArrayList<String> xValues;
    /** Y轴数据 */
    private ArrayList<BarEntry> yValues;
    private ArrayList<Entry> lineYValues;

    @Override
    protected void initWidget() {
        super.initWidget();
        barChart = (BarChart)findViewById(R.id.energy_activity_fan_barChart);
        pieChart = (PieChart)findViewById(R.id.energy_activity_fan_piChart);
        lineChart = (LineChart)findViewById(R.id.energy_activity_fan_lineChart);
        initBarChart();
    }

    @Override
    protected void initData() {
        super.initData();
        EnergyFanEntity.kjdb = KJDB.create(this);
    }

    /**
     * 初始化表格设置
     */
    private void initBarChart() {
        if (pieChart != null && pieChart.getVisibility() != View.GONE) {
            pieChart.setVisibility(View.GONE);
        } else if (barChart != null && barChart.getVisibility() != View.GONE) {
            barChart.setVisibility(View.GONE);
        }

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
        lineChart.setBackgroundColor(Color.LTGRAY);

        lineChart.animateX(2500);

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

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

        xValues = TimeUtils.getCurrentMonthDay();//X 轴数据
        lineYValues = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            float val = (float) (Math.random() * 100) + 100 / 3;
            lineYValues.add(new Entry((int) val, i));
        }

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
     * 查询过去三十天
     * 比如 今天为10号，那查询的数据就是上个月10号到这个月10号的数据
     */
    private void getCurrentMonthData() {
        HashMap<String, Long> timeMap = TimeUtils.getCurrentMonthTime();
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.energy_activity_fan);
        setActionBarView(true);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
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

    private void showToday() {
        if (barChart != null && barChart.getVisibility() != View.GONE) {
            barChart.setVisibility(View.GONE);
        } else if (lineChart != null && lineChart.getVisibility() != View.GONE) {
            lineChart.setVisibility(View.GONE);
        }
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

    /**
     * 现实最近十二个月
     */
    private void showLast12Month() {
        if (lineChart.getVisibility() != View.GONE) {
            lineChart.setVisibility(View.GONE);
        }
        if (pieChart != null && pieChart.getVisibility() != View.GONE) {
            pieChart.setVisibility(View.GONE);
        }

        barChart.setVisibility(View.VISIBLE);
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
}
