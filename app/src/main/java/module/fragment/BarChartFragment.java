package module.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import utils.TimeUtils;
import vgod.smarthome.R;


/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-28
 * Time: 20:53
 * 柱状图
 */
public class BarChartFragment extends Fragment{

    /** BarChart视图 */
    private BarChart barChart;

    /** X轴数据 */
    private ArrayList<String> xValues;
    /** Y轴数据 */
    private ArrayList<BarEntry> yValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_chart_bar, container, false);
        initData();
        initView(parentView);
        return parentView;
    }

    /**
     * 初始化视图
     */
    private void initView(View parentView) {
        barChart = (BarChart)parentView.findViewById(R.id.energy_fragment_barChart);
        barChart.setDescription("过去12个月风扇电量消耗");

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
     * 初始化数据
     */
    private void initData() {
        xValues = TimeUtils.getLastMonthName();//X 轴数据
        yValues = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            float val = (float) (Math.random() * 100) + 100 / 3;
            yValues.add(new BarEntry((int) val, i));
        }
    }
}
