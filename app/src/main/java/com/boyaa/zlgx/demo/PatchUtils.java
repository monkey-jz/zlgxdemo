package com.boyaa.zlgx.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.io.File;
import androidx.core.content.FileProvider;

/**
 * @author: JerryZhu
 * @datetime: 2022/6/15
 */
public class PatchUtils {
    public static final String TAG = "PatchUtils";

    static {
        System.loadLibrary("native_lib");
    }

    static native void nativePatch(String oldApkPath,String patchPath,String newApkPath);

    public static void patch(File oldApkFile, File patchFile, File newApkFile) {
        nativePatch(oldApkFile.getAbsolutePath(),patchFile.getAbsolutePath(),newApkFile.getAbsolutePath());
    }

    public static void checkUpdate(Activity activity) {

        String packageResourcePath = activity.getPackageResourcePath();
        File oldApkFile = new File(packageResourcePath);
        boolean canRead = oldApkFile.canRead();
        Log.d(TAG,"oldApkFile : " + packageResourcePath);
        Log.d(TAG,"oldApkFile canRead : " + canRead);
        File filesDir = activity.getFilesDir();
        File zlgx = new File(filesDir, "zlgx");
        if(!zlgx.exists()){
            zlgx.mkdir();
        }
        File patchFile = new File(zlgx, "patch.diff");
        if (patchFile.exists()){
            showDialog(activity,oldApkFile,patchFile);
        }
    }

    private static void showDialog(Activity activity, File oldApkFile, File patchFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("有新版本的应用是否需要现在安装?")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                }).setNegativeButton("现在安装", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        generateNewApk(activity,oldApkFile,patchFile);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private static void generateNewApk(Activity activity, File oldApkFile, File patchFile) {
        File newApkFileDir = new File(activity.getFilesDir(),"new_apk");
        if(!newApkFileDir.exists()){
            newApkFileDir.mkdir();
        }
        File newApkFile = new File(newApkFileDir, "new.apk");
        if(newApkFile.exists()){
            newApkFile.delete();
        }
        PatchUtils.patch(oldApkFile,patchFile,newApkFile);
        patchFile.delete();
        Log.d(TAG,"generateNewApk : " + newApkFile.exists());
        Log.d(TAG,"generateNewApk : " + newApkFile.canRead());
        if(newApkFile.exists()){
            installApk(activity,newApkFile);
        }
    }

    public static void installApk(Activity activity, File apkFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String type = "application/vnd.android.package-archive";
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", apkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(apkFile);
            }
            intent.setDataAndType(uri, type);
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG,"installApk Exception : " + e.toString());
        }
    }
}

