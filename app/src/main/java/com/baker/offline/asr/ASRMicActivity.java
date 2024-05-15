package com.baker.offline.asr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
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

        tvContent.setMovementMethod(new ScrollingMovementMethod());

        btnRecord.setOnClickListener(v -> {
            if (!BakerASRManager.getInstance().isRecording()) {
                BakerASRManager.getInstance().startRecord();
                tvContent.setText("");
                btnRecord.setText("停止");
            } else {
                BakerASRManager.getInstance().stopRecord();
                btnRecord.setText("开始");
            }
        });


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
                if (TextUtils.isEmpty(response)) return;
                if (tvContent.getText() == null || TextUtils.isEmpty(tvContent.getText())) {
                    tvContent.setText(response);
                    tvContent.append("\n");
                } else {
                    tvContent.append(response);
                    tvContent.append("\n");
                }
            }

            @Override
            public void onError(@NonNull String errorCode, @NonNull String errorMsg) {
                Log.e("TAG--->", "errorCode:" + errorCode + " errorMsg:" + errorMsg);
                Toast.makeText(ASRMicActivity.this, "errorCode:" + errorCode + "errorMsg:" + errorMsg, Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public void onBackPressed() {
        BakerASRManager.getInstance().release();
        super.onBackPressed();
    }
}