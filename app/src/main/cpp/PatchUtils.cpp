
//
// Created by JerryZhu on 2022/6/15.
//

#include <jni.h>
#include <android/log.h>

extern "C"{
  int native_patch(int argc,char * argv[]);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_boyaa_zlgx_demo_PatchUtils_nativePatch(JNIEnv *env, jclass clazz,
                       jstring old_apk_path,
                       jstring patch_path,
                       jstring new_apk_path) {

    //将java字符串转换成char指针
     const char *oldApk = env->GetStringUTFChars(old_apk_path, 0);
     const char *patch = env->GetStringUTFChars(patch_path, 0);
     const char *output = env->GetStringUTFChars(new_apk_path, 0);
     __android_log_print(ANDROID_LOG_INFO, "INFO", "%s", oldApk);
     __android_log_print(ANDROID_LOG_INFO, "INFO", "%s", patch);
     __android_log_print(ANDROID_LOG_INFO, "INFO", "%s", output);
     //bspatch ,oldfile ,newfile ,patchfile
     char *argv[] = {"bspatch", const_cast<char *>(oldApk), const_cast<char *>(output),const_cast<char *>(patch)};
     native_patch(4, argv);
     //释放相应的指针gc
     env->ReleaseStringUTFChars(old_apk_path, oldApk);
     env->ReleaseStringUTFChars(patch_path, patch);
     env->ReleaseStringUTFChars(new_apk_path, output);
}