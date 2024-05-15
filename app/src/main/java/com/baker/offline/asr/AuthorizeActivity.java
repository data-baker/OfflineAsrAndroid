package com.baker.offline.asr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.databaker.offlineasr.BakerASRManager;
import com.databaker.offlineasr.ErrorCode;
import com.databaker.offlineasr.callback.BakerAuthorizeCallBack;

public class AuthorizeActivity extends AppCompatActivity {

    public static void start(Context mContext, boolean isMic) {
        Intent intent = new Intent();
        intent.putExtra("isMic", isMic);
        intent.setClass(mContext, AuthorizeActivity.class);
        mContext.startActivity(intent);
    }

    private boolean isMic;
    private final boolean isInitData = true;


    private AppCompatEditText etClientId;
    private AppCompatEditText etClientSecret;
    private AppCompatButton btnAuthorize;



    private String clientId = "";
    private String clientSecret = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        etClientId = findViewById(R.id.et_client_id);
        etClientSecret = findViewById(R.id.et_client_secret);
        btnAuthorize = findViewById(R.id.btn_authorize);


        isMic = getIntent().getBooleanExtra("isMic", true);


        btnAuthorize.setOnClickListener(view -> {
            if (etClientId.getText() != null) {
                clientId = etClientId.getText().toString().trim();
            }
            if (etClientSecret.getText() != null) {
                clientSecret = etClientSecret.getText().toString().trim();
            }
            if (isInitData) {
                initData();
            } else {
                initCopyData();
            }
        });
    }


    private void initData() {
        BakerASRManager.getInstance()
                .setContext(this)
                .setDebug(true)
                .setNumThreads(2)
                .initSDK(clientId, clientSecret, new BakerAuthorizeCallBack() {
                    @Override
                    public void onAuthorizeSuccess() {
                        if (isMic) {
                            ASRMicActivity.start(AuthorizeActivity.this);
                        } else {
                            ASRFileActivity.start(AuthorizeActivity.this);
                        }
                    }

                    @Override
                    public void onError(@NonNull String errorCode, @NonNull String errorMsg) {
                        if (!ErrorCode.INIT_ERROR.getErrorCode().equals(errorCode)) {
                            Toast.makeText(AuthorizeActivity.this, "errorMsg：" + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initCopyData() {
        String encoderParamPath = Utils.AssetsFileToString(this, "model-cat/encoder_jit_trace-pnnx.ncnn.param");
        String encoderBinPath = Utils.AssetsFileToString(this, "model-cat/encoder_jit_trace-pnnx.ncnn.bin");
        String decoderParamPath = Utils.AssetsFileToString(this, "model-cat/decoder_jit_trace-pnnx.ncnn.param");
        String decoderBinPath = Utils.AssetsFileToString(this, "model-cat/decoder_jit_trace-pnnx.ncnn.bin");
        String joinerParamPath = Utils.AssetsFileToString(this, "model-cat/joiner_jit_trace-pnnx.ncnn.param");
        String joinerBinPath = Utils.AssetsFileToString(this, "model-cat/joiner_jit_trace-pnnx.ncnn.bin");
        String tokensPath = Utils.AssetsFileToString(this, "model-cat/tokens.txt");

        BakerASRManager.getInstance()
                .setContext(this)
                .setDebug(true)
                .setNumThreads(2)
                .setAssetsManager(false)
                .setParamPaths(encoderParamPath, encoderBinPath, decoderParamPath, decoderBinPath, joinerParamPath, joinerBinPath, tokensPath)
                .initSDK(clientId, clientSecret, new BakerAuthorizeCallBack() {
                    @Override
                    public void onAuthorizeSuccess() {
                        if (isMic) {
                            ASRMicActivity.start(AuthorizeActivity.this);
                        } else {
                            ASRFileActivity.start(AuthorizeActivity.this);
                        }
                    }

                    @Override
                    public void onError(@NonNull String errorCode, @NonNull String errorMsg) {
                        if (!ErrorCode.INIT_ERROR.getErrorCode().equals(errorCode)) {

                            Toast.makeText(AuthorizeActivity.this, "errorMsg：" + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}