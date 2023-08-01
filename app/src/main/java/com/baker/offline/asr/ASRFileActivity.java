package com.baker.offline.asr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.databaker.offlineasr.BakerASRManager;
import com.databaker.offlineasr.callback.BakerRecognizerCallback;

import java.io.IOException;
import java.io.InputStream;

public class ASRFileActivity extends AppCompatActivity {

    public static void start(Context mContext) {
        Intent intent = new Intent();
        intent.setClass(mContext, ASRFileActivity.class);
        mContext.startActivity(intent);
    }

    private TextView tvContent;
    private Button btnRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asrfile);

        tvContent = findViewById(R.id.tv_content);
        btnRecord = findViewById(R.id.btn_record);

        BakerASRManager.getInstance().setASRListener(new BakerRecognizerCallback() {
            @Override
            public void onVolumeChanged(int volume) {
                Log.e("TAG--->", "onVolumeChanged:" + volume);
            }

            @Override
            public void onResult(boolean endFlag, @NonNull String text) {
                Log.e("TAG--->", "onResult:" + text + "  endFlg:" + endFlag);
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
                runOnUiThread(() -> Toast.makeText(ASRFileActivity.this, "code:" + code + "  msg:" + msg, Toast.LENGTH_SHORT).show());
            }
        });
        btnRecord.setOnClickListener(view -> readPcmFromAssets("audio.wav"));

    }

    private void readPcmFromAssets(String fileName) {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open(fileName);
            byte[] buffer = new byte[5120];
            while (inputStream.read(buffer) != -1) {
                // 处理每一个5120字节的PCM数据
                BakerASRManager.getInstance().senData(buffer);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}