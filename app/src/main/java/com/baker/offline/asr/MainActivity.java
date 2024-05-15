package com.baker.offline.asr;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnFile;
    private Button btnMic;


    private boolean isMic = true;
    private final String[] permissions = {RECORD_AUDIO};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Utils.hasPermission(this, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO)) {
            AuthorizeActivity.start(this, isMic);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btnFile = findViewById(R.id.btn_file);
        btnMic = findViewById(R.id.btn_mic);


        btnFile.setOnClickListener(v -> {
            isMic = false;
            if (Utils.hasPermission(this, RECORD_AUDIO)) {
                AuthorizeActivity.start(this, isMic);
            } else {
                requestPermissions(permissions, 1001);
            }
        });

        btnMic.setOnClickListener(v -> {
            isMic = true;
            if (Utils.hasPermission(this, RECORD_AUDIO)) {
                AuthorizeActivity.start(this, isMic);
            } else {
                requestPermissions(permissions, 1002);
            }
        });
    }


}