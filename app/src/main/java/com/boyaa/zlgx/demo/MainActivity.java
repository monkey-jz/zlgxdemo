package com.boyaa.zlgx.demo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        Button btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });

    }

    private void checkPermission() {
        boolean hasInstallPermission ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            hasInstallPermission = getPackageManager().canRequestPackageInstalls();
            if(!hasInstallPermission){
                Uri packageUri = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                startActivityForResult(intent,100);
            }
        }else{
            PatchUtils.checkUpdate(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(PatchUtils.TAG,"onResume ===");
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*File newApkFileDir = new File(getFilesDir(),"new_apk");
        PatchUtils.installApk(MainActivity.this,new File(newApkFileDir, "new.apk"));*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            PatchUtils.checkUpdate(this);
        }
    }
}