package com.luyendd.learntoeic.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.luyendd.learntoeic.ConnectDataBase;
import com.luyendd.learntoeic.R;
import com.luyendd.learntoeic.adapter.AdapterTopic;
import com.luyendd.learntoeic.obj.Topic;
import com.luyendd.learntoeic.utils.Const;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TopicActivity extends AppCompatActivity {

    GridView gridView;
    public ConnectDataBase cdb;
    List<Topic> topicList;
    AdapterTopic adapterTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.topic);

        cdb = new ConnectDataBase(this);
        try {
            cdb.createDataBase();
            topicList = cdb.getListTopic();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapterTopic = new AdapterTopic(this, topicList);
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(adapterTopic);
        adapterTopic.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TopicActivity.this, topicList.get(position).getTranslate(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TopicActivity.this, VocaDetailsActivity.class);
                i.putExtra(Const.TOPIC_ID, topicList.get(position).getId());
                i.putExtra(Const.TOPIC_NAME, topicList.get(position).getTranslate());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
