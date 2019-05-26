package com.luyendd.learntoeic.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.luyendd.learntoeic.ConnectDataBase;
import com.luyendd.learntoeic.R;
import com.luyendd.learntoeic.activity.MainActivity;
import com.luyendd.learntoeic.obj.Voca;

import java.util.Locale;


@SuppressLint("ValidFragment")
public class VocaDetailsFragment extends Fragment {

    ImageView imageView;
    TextView textViewVoca, textViewMean, textViewVocabulazion;
    ImageButton imageButtonSpeak, imageButtonFavorite;
    int position = 0;
    Voca voca;
    TextToSpeech tts;

    //    ConnectDataBase connectDataBase;
    public VocaDetailsFragment(Voca voca) {
        this.voca = voca;
        Bundle args = new Bundle();
        args.putInt("position", position);
        setArguments(args);
        this.position = position;
//        connectDataBase = new ConnectDataBase(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voca_details, container, false);
         tts =new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
        imageView = view.findViewById(R.id.imageView);
        textViewVoca = view.findViewById(R.id.textViewVoca);
        textViewMean = view.findViewById(R.id.textViewMean);
        textViewVocabulazion = view.findViewById(R.id.textViewVocabulazion);
        imageButtonSpeak = view.findViewById(R.id.imageButtonSpeak);
        imageButtonFavorite = view.findViewById(R.id.imagebuttonFavorite);
        textViewMean.setText(voca.getTranslate());
        textViewVoca.setText(voca.getVocabulary());
        textViewVocabulazion.setText(voca.getVocalization());

        if (voca.getFavorite() == 0) {
            imageButtonFavorite.setBackgroundDrawable(getContext().getDrawable(R.drawable.ic_not_favorite));
        } else {
            imageButtonFavorite.setBackgroundDrawable(getContext().getDrawable(R.drawable.ic_favorite));
        }

        imageButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick", "imageButtonSpeak");
                tts.speak(voca.getVocabulary(),
                        TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        imageButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voca.getFavorite() == 1) {
                    imageButtonFavorite.setBackgroundDrawable(getContext().getDrawable(R.drawable.ic_not_favorite));
                    voca.setFavorite(0);
                    MainActivity.cdb.UpdateVoca(voca);
                } else {
                    imageButtonFavorite.setBackgroundDrawable(getContext().getDrawable(R.drawable.ic_favorite));
                    voca.setFavorite(1);
                    MainActivity.cdb.UpdateVoca(voca);
                }
            }
        });


        return view;
    }

}
