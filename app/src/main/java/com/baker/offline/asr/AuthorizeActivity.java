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
import com.databaker.offlineasr.callback.BakerAuthorizeCallBack;

public class AuthorizeActivity extends AppCompatActivity {

    public static void start(Context mContext, int from) {
        Intent intent = new Intent();
        intent.putExtra("from", from);
        intent.setClass(mContext, AuthorizeActivity.class);
        mContext.startActivity(intent);
    }


    private AppCompatEditText etClientId;
    private AppCompatEditText etClientSecret;
    private AppCompatButton btnAuthorize;

    private int from;
    private String clientId = "";
    private String clientSecret = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        etClientId = findViewById(R.id.et_client_id);
        etClientSecret = findViewById(R.id.et_client_secret);
        btnAuthorize = findViewById(R.id.btn_authorize);



        from = getIntent().getIntExtra("from", 1001);


        btnAuthorize.setOnClickListener(view -> {
            if (etClientId.getText() != null) {
                clientId = etClientId.getText().toString().trim();
            }
            if (etClientSecret.getText() != null) {
                clientSecret = etClientSecret.getText().toString().trim();
            }
            BakerASRManager.getInstance()
                    .setContext(this)
                    .initSDK(clientId, clientSecret, new BakerAuthorizeCallBack() {
                        @Override
                        public void onAuthorizeSuccess() {
                            if (from == 1001) {
                                ASRFileActivity.start(AuthorizeActivity.this);
                            } else {
                                ASRMicActivity.start(AuthorizeActivity.this);
                            }
                        }

                        @Override
                        public void onError(@NonNull String code, @NonNull String msg) {
                            Log.e("TAG--->", "onError:code--->" + code + "   msg--->" + msg);
                            runOnUiThread(() -> Toast.makeText(AuthorizeActivity.this, "code:" + code + "  msg:" + msg, Toast.LENGTH_SHORT).show());
                        }
                    });
        });
    }
}