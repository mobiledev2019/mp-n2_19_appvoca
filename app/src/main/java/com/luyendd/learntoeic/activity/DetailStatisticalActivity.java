package com.luyendd.learntoeic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import com.luyendd.learntoeic.ConnectDataBase;
import com.luyendd.learntoeic.R;
import com.luyendd.learntoeic.adapter.AdapterDetailStatistic;
import com.luyendd.learntoeic.obj.TopicStatistical;

import java.util.ArrayList;
import java.util.List;

public class DetailStatisticalActivity extends AppCompatActivity {

    ConnectDataBase cdb;
    GridView gridView;
    List<TopicStatistical> topicStatisticalList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_statistical);
        gridView = findViewById(R.id.gridview_detail_statistical);

        getSupportActionBar().setTitle("Thống kê chi tiết");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cdb = new ConnectDataBase(this);
        topicStatisticalList = cdb.getListTopicStatistical();
        AdapterDetailStatistic adapterDetailStatistic = new AdapterDetailStatistic(this,
                topicStatisticalList);
        gridView.setAdapter(adapterDetailStatistic);
        adapterDetailStatistic.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
