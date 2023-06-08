package com.icodechef.android.tick.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.icodechef.android.tick.R;
import com.icodechef.android.tick.database.TickDBAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, ScheduleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setToolBar();

        TickDBAdapter adapter = new TickDBAdapter(this);

        // 今日数据
        adapter.open();
        HashMap today = adapter.getToday();
        adapter.close();

        TextView todayDurations = (TextView)findViewById(R.id.schedule_today_durations);
        TextView todayTimes = (TextView)findViewById(R.id.schedule_today_times);

        todayDurations.setText(String.valueOf(today.get("duration")));
        todayTimes.setText(String.valueOf(today.get("times")));

        // 累计数据
        adapter.open();
        HashMap amount = adapter.getAmount();
        adapter.close();

        TextView amountDurations = (TextView)findViewById(R.id.schedule_amount_durations);
        TextView amountTimes = (TextView)findViewById(R.id.schedule_amount_times);

        amountDurations.setText(String.valueOf(amount.get("duration")));
        amountTimes.setText(String.valueOf(amount.get("times")));

        //饼图绘制

        adapter.open();
        HashMap pie_data = adapter.pie_result();
        adapter.close();

        PieChart pie_chart =findViewById(R.id.pie_chart);
        pie_chart.setUsePercentValues(true);
        pie_chart.getDescription().setEnabled(false);

        if((int) pie_data.get("finishtime")==0 && (int)pie_data.get("not_finishtime")==0){
            pie_chart.setNoDataText("还没有记录呢，快去试试番茄钟吧！");
            pie_chart.setNoDataTextColor(Color.BLACK);
        }
        else{
            //设置饼状图数据
            ArrayList<PieEntry> entries=new ArrayList<>();
            entries.add(new PieEntry((int) pie_data.get("finishtime"),"完成计时"));
            entries.add(new PieEntry((int)pie_data.get("not_finishtime"),"未完成计时"));


            PieDataSet dataSet = new PieDataSet(entries, "番茄完成度统计");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            PieData pieData = new PieData(dataSet);
            pieData.setDrawValues(true);
            pieData.setValueTextSize(12f);

            pie_chart.setData(pieData);
            pie_chart.setEntryLabelTextSize(12f);
            pie_chart.setEntryLabelColor(Color.BLACK);

            // 刷新图表
            pie_chart.invalidate();
        }

    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
