package com.baker.offline.asr;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnFile;
    private Button btnMic;
    private final String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Util.hasPermission(this, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO)) {
            AuthorizeActivity.start(this, requestCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFile = findViewById(R.id.btn_file);
        btnMic = findViewById(R.id.btn_mic);


        btnFile.setOnClickListener(v -> {
            if (Util.hasPermission(this, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO)) {
                AuthorizeActivity.start(this, 1001);
            } else {
                requestPermissions(permissions, 1001);
            }
        });

        btnMic.setOnClickListener(v -> {
            if (Util.hasPermission(this, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO)) {
                AuthorizeActivity.start(this, 1002);
            } else {
                requestPermissions(permissions, 1002);
            }
        });
    }
}