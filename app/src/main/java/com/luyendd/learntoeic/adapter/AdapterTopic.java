package com.luyendd.learntoeic.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luyendd.learntoeic.ConnectDataBase;
import com.luyendd.learntoeic.R;
import com.luyendd.learntoeic.activity.VocaDetailsActivity;
import com.luyendd.learntoeic.obj.Topic;
import com.luyendd.learntoeic.utils.Const;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AdapterTopic extends BaseAdapter {
    Context context;
    List<Topic> topicList;
    ViewHolderTopic vht;
    private ConnectDataBase cdb;
    public AdapterTopic(Context context, List<Topic> topicList){
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
        //anh xa ra view
        if (convertView == null) {
            LayoutInflater vi =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.adapter_topic, null);
            vht = new ViewHolderTopic();
            vht.btnFavorite = convertView.findViewById(R.id.btnFavorite);
            vht.iv = convertView.findViewById(R.id.imageViewTopic);
            vht.tv = convertView.findViewById(R.id.textViewTopic);
            convertView.setTag(vht);
        }else{
            vht = (ViewHolderTopic) convertView.getTag();
        }

        //load hinh anh dung Tv glide
        final Topic topic = topicList.get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();
        Glide.with(context)
                .load("file:///android_asset/image/"+
                        topic.getName().toLowerCase()
                                .replace(" ", "_") + ".jpg")
                .apply(options).into(vht.iv);

        vht.tv.setText(topic.getTranslate());//set text tieu de


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
                    cdb.UpdateTopic(topic);
                }else {
                    topic.setFavourite(1);
                    cdb.UpdateTopic(topic);
                }
                //Cap nhat ui
                notifyDataSetChanged();

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ABC", "convertView_ONCLICK");
                if (topic.getIsActive() != 1) {
                    Log.d("ABC", "[Update Topic Active]");
                    topic.setIsActive(1);
                    cdb.UpdateTopic(topic);
                }

                Intent i = new Intent(context, VocaDetailsActivity.class);
                i.putExtra(Const.TOPIC_ID, topic.getId());
                i.putExtra(Const.TOPIC_NAME, topic.getTranslate());
                context.startActivity(i);
            }
        });
        Log.d("ABC", "[TAG]" + topic.getTranslate() + "isActive: " +
                topic.getIsActive() + "...." + topic.getLevel1() + "///"
                + topic.getLevel2() + "///" + topic.getLevel3());
        return convertView;
    }

    public class ViewHolderTopic{
        ImageView iv;
        TextView tv;
        Button btnFavorite;
    }

    public BitmapDrawable getBitmapFromAssets(String fileName) {
        AssetManager assetManager = context.getAssets();

        InputStream istr = null;
        try {
            istr = assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
