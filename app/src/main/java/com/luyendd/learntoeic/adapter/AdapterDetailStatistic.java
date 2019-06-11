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
import com.luyendd.learntoeic.obj.TopicStatistical;
import com.luyendd.learntoeic.utils.Const;

import java.util.List;

public class AdapterDetailStatistic extends BaseAdapter {

    Context context;
    List<TopicStatistical> topicList;
    ViewHolderTopic vht;
    private ConnectDataBase cdb;
    public AdapterDetailStatistic(Context context, List<TopicStatistical> topicList){
        this.context = context;
        this.topicList = topicList;
        cdb = new ConnectDataBase(context);
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public TopicStatistical getItem(int position) {
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
            vht.tvLevel1 = convertView.findViewById(R.id.tv_quiz_level1);
            vht.tvLevel2 = convertView.findViewById(R.id.tv_quiz_level2);
            vht.tvLevel3 = convertView.findViewById(R.id.tv_quiz_level3);
            vht.tvTotalQuiz = convertView.findViewById(R.id.tv_total_quiz);
            convertView.setTag(vht);
        }else{
            vht = (ViewHolderTopic) convertView.getTag();
        }

        final TopicStatistical topic = topicList.get(position);
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



        vht.tvPass.setText("Pass: " + topic.getLevel1() + "----" + topic.getLevel2() + "----" + topic.getLevel3());
        vht.tvLevel1.setText("Mức 1: " + topic.getLevel1() );
        vht.tvLevel2.setText("Mức 2: " + topic.getLevel2() );
        vht.tvLevel3.setText("Mức 3: " + topic.getLevel3() );
        int totalQuiz = topic.getLevel1() + topic.getLevel2() + topic.getLevel3();
        vht.tvTotalQuiz.setText("Tổng số bài quiz: " + totalQuiz );

        vht.tvNotPass.setText("Số từ đã học: " + topic.getLearnPerTotal());
        handleShowProcessPercent(vht.mProgress, topic.getLearnPerTotal());
        return convertView;
    }

    public class ViewHolderTopic{
        ImageView iv;
        TextView tv, tvPass, tvNotPass, tvLevel1, tvLevel2, tvLevel3, tvTotalQuiz;
        Button btnFavorite;
        ProgressBar mProgress;
    }

    private void handleShowProcessPercent(ProgressBar progressBar, String learnPerTotal){
        int lenght = learnPerTotal.length();
        int learn = Integer.parseInt(learnPerTotal.substring(0, learnPerTotal.indexOf("/")));
        int total = Integer.parseInt(learnPerTotal.substring(learnPerTotal.indexOf("/") + 1, lenght));

        float process = Math.round(learn * 1.0 * 100 / total);
        progressBar.setProgress((int)Math.round(process));

    }



}
