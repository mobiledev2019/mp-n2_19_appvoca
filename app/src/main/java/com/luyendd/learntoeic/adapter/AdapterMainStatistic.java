package com.luyendd.learntoeic.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luyendd.learntoeic.ConnectDataBase;
import com.luyendd.learntoeic.R;
import com.luyendd.learntoeic.activity.VocaDetailsActivity;
import com.luyendd.learntoeic.obj.Topic;
import com.luyendd.learntoeic.utils.Const;

import java.util.List;

public class AdapterMainStatistic extends BaseAdapter {

    Context context;
    List<Topic> topicList;
    ViewHolderTopic vht;
    private ConnectDataBase cdb;
    public AdapterMainStatistic(Context context, List<Topic> topicList){
        this.context = context;
        this.topicList = topicList;
        cdb = new ConnectDataBase(context);
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Topic getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.adapter_main_statistic, null);
            vht = new ViewHolderTopic();
            vht.btnFavorite = convertView.findViewById(R.id.btnFavorite);
            vht.iv = convertView.findViewById(R.id.imageViewTopic);
            vht.tv = convertView.findViewById(R.id.textViewTopic);
            vht.tvPass = convertView.findViewById(R.id.tv_pass);
            vht.tvNotPass = convertView.findViewById(R.id.tv_not_pass);
            vht.mProgress = convertView.findViewById(R.id.pb_media_progress);
            convertView.setTag(vht);
        }else{
            vht = (ViewHolderTopic) convertView.getTag();
        }

        final Topic topic = topicList.get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();
        Glide.with(context)
                .load("file:///android_asset/image/"+
                        topic.getName().toLowerCase()
                                .replace(" ", "_") + ".jpg")
                .apply(options).into(vht.iv);

        vht.tv.setText(topic.getTranslate());


        if (topic.getFavourite() == 1 ){
            vht.btnFavorite.setBackground(context.getDrawable(R.drawable.ic_favorite));
        } else {
            vht.btnFavorite.setBackground(context.getDrawable(R.drawable.ic_not_favorite));
        }

        vht.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topic.getFavourite() == 1) {
                    topic.setFavourite(0);
                    cdb.UpdateTopicFavourite(topic);
                }else {
                    topic.setFavourite(1);
                    cdb.UpdateTopicFavourite(topic);
                }
                //Cap nhat ui
                notifyDataSetChanged();

            }
        });
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("ABC", "convertView_ONCLICK");
//                Intent i = new Intent(context, VocaDetailsActivity.class);
//                i.putExtra(Const.TOPIC_ID, topic.getId());
//                i.putExtra(Const.TOPIC_NAME, topic.getTranslate());
//                context.startActivity(i);
//            }
//        });

        vht.tvPass.setText("Pass: " + topic.getPass());
        vht.tvNotPass.setText("Not pass: " + topic.getNotPass());
        Log.d("ABC", "___" + topic.getPass() +"___" + topic.getNotPass());
        int total = topic.getPass() + topic.getNotPass();
        float process = Math.round(topic.getPass() * 1.0 * 100 / total);
        Log.d("ABC_process", "___" + process);
        vht.mProgress.setProgress((int)Math.round(process));
        return convertView;
    }

    public class ViewHolderTopic{
        ImageView iv;
        TextView tv, tvPass, tvNotPass;
        Button btnFavorite;
        ProgressBar mProgress;
    }
}
