package com.baker.offline.asr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.databaker.offlineasr.BakerASRManager;
import com.databaker.offlineasr.callback.BakerRecognizerCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

        tvContent.setMovementMethod(new ScrollingMovementMethod());

        btnRecord.setOnClickListener(view -> readPcmFromAssets("audio.wav"));


        BakerASRManager.getInstance().setASRListener(new BakerRecognizerCallback() {

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onVolumeChanged(int volume) {

            }

            @Override
            public void onResult(boolean endFlag, @NonNull String response) {
                tvContent.setText(response);
            }

            @Override
            public void onError(@NonNull String errorCode, @NonNull String errorMsg) {
                Toast.makeText(ASRFileActivity.this, "errorCode:" + errorCode + "errorMsg:" + errorMsg, Toast.LENGTH_LONG).show();
                Log.e("TAG--->", "errorCode:" + errorCode + " errorMsg:" + errorMsg);
            }
        });
    }


    private Executor workService = Executors.newSingleThreadExecutor();
    private void readPcmFromAssets(String fileName) {
        workService.execute(() -> {
            try {
                AssetManager assetManager = getAssets();
                InputStream inputStream = assetManager.open(fileName);
                byte[] buffer = new byte[5120];
                while (inputStream.read(buffer) != -1) {
                    BakerASRManager.getInstance().sendData(buffer);
                    Thread.sleep(100);
                }
                inputStream.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onBackPressed() {
        BakerASRManager.getInstance().release();
        super.onBackPressed();
    }


}