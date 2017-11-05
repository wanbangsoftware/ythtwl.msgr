//
// Created by Hsiang Leekwok on 2017/11/05.
//
#include <jni.h>
#include <string>

extern "C"

JNIEXPORT jstring JNICALL Java_com_hlk_ythtwl_msgr_core_Core_accountDebug(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "development";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_com_hlk_ythtwl_msgr_core_Core_passwordDebug(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "759b74ce43947f5f4c91aeddc3e5bad3";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_com_hlk_ythtwl_msgr_core_Core_accountRelease(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "boss_user";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_com_hlk_ythtwl_msgr_core_Core_passwordRelease(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "7edb18ad781a0dfa7ea1bcc1da5ad802";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_com_hlk_ythtwl_msgr_core_Core_accountServer(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "service";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_com_hlk_ythtwl_msgr_core_Core_passwordServer(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "aaabf0d39951f3e6c3e8a7911df524c2";
    return env->NewStringUTF(hello.c_str());
}