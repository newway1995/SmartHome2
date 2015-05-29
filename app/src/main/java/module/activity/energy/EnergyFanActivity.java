package module.activity.energy;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import org.kymjs.aframe.ui.BindView;

import framework.base.SwipeBackActivity;
import framework.ui.common.SegmentedGroup;
import module.fragment.BarChartFragment;
import module.fragment.LineChartFragment;
import module.fragment.PieChartFragment;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-09
 * Time: 14:25
 * 显示电风扇的能耗
 */
public class EnergyFanActivity extends SwipeBackActivity implements RadioGroup.OnCheckedChangeListener{
    @BindView(id = R.id.nav_energy_segment , click = true)
    private SegmentedGroup segmentedGroup;
    private BarChartFragment barChartFragment;//BarChart
    private LineChartFragment lineChartFragment;//LineChart
    private PieChartFragment pieChartFragment;//PieChart
    private FragmentManager fm;


    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.energy_activity_fan);
        setActionBarView(true);
    }

    @Override
    protected void initData() {
        super.initData();
        barChartFragment = new BarChartFragment();
        lineChartFragment = new LineChartFragment();
        pieChartFragment = new PieChartFragment();
        fm = getFragmentManager();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        segmentedGroup.setOnCheckedChangeListener(this);
        segmentedGroup.check(R.id.nav_energy_segment_bar);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
    }

    /**
     * SegmentedGroup点击事件
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup , int checkedId){
        Debug("onCheckedChanged");
        FragmentTransaction ft = fm.beginTransaction();
        switch (checkedId){
            case R.id.nav_energy_segment_bar:
                Debug("BarChart");
                ft.replace(R.id.nav_energy_fragment_fan_container, barChartFragment);
                break;
            case R.id.nav_energy_segment_line:
                Debug("LineChart");
                ft.replace(R.id.nav_energy_fragment_fan_container, lineChartFragment);
                break;
            case R.id.nav_energy_segment_pie:
                Debug("PieChart");
                ft.replace(R.id.nav_energy_fragment_fan_container, pieChartFragment);
                break;
        }
        ft.commit();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
