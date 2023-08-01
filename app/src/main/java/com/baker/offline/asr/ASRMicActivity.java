package com.baker.offline.asr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.databaker.offlineasr.BakerASRManager;
import com.databaker.offlineasr.callback.BakerRecognizerCallback;

public class ASRMicActivity extends AppCompatActivity {

    public static void start(Context mContext) {
        Intent intent = new Intent();
        intent.setClass(mContext, ASRMicActivity.class);
        mContext.startActivity(intent);
    }

    private TextView tvContent;
    private Button btnRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asrmic);

        tvContent = findViewById(R.id.tv_content);
        btnRecord = findViewById(R.id.btn_record);

        BakerASRManager.getInstance().setASRListener(new BakerRecognizerCallback() {
            @Override
            public void onVolumeChanged(int volume) {
                Log.e("TAG--->", "onVolumeChanged:" + volume);
            }
            @Override
            public void onResult(boolean endFlag, @NonNull String text) {
                Log.e("TAG--->", "onResult:" + text);
                runOnUiThread(() -> {
                    if (!TextUtils.isEmpty(text)) {
                        tvContent.setText(text);
                    }
                });
            }
            @Override
            public void onBeginOfSpeech() {
                Log.e("TAG--->", "onBeginOfSpeech");
            }

            @Override
            public void onEndOfSpeech() {
                Log.e("TAG--->", "onEndOfSpeech");
            }

            @Override
            public void onError(@NonNull String code, @NonNull String msg) {
                Log.e("TAG--->", "onError:code--->" + code + "   msg--->" + msg);
                runOnUiThread(() -> Toast.makeText(ASRMicActivity.this, "code:" + code + "  msg:" + msg, Toast.LENGTH_SHORT).show());
            }
        });

        btnRecord.setOnClickListener(v -> {
            if (!BakerASRManager.getInstance().isRecording()) {
                BakerASRManager.getInstance().startRecord();
                btnRecord.setText("停止");
            } else {
                BakerASRManager.getInstance().stopRecord();
                btnRecord.setText("开始");
            }
        });


    }
}