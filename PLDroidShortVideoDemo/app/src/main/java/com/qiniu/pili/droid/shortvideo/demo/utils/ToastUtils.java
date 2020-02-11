package com.qiniu.pili.droid.shortvideo.demo.utils;

import android.content.Context;
import android.widget.Toast;

import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_LOW_MEMORY;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_MULTI_CODEC_WRONG;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_MUXER_START_FAILED;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_NO_VIDEO_TRACK;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_SETUP_AUDIO_ENCODER_FAILED;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_SETUP_CAMERA_FAILED;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_SETUP_MICROPHONE_FAILED;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_SETUP_VIDEO_DECODER_FAILED;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_SETUP_VIDEO_ENCODER_FAILED;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_SRC_DST_SAME_FILE_PATH;

public class ToastUtils {

    private static Context mAppContext = null;

    public static void init(Context context) {
        mAppContext = context;
    }

    public static void showShortMessage(String msg) {
        if (mAppContext != null) {
            Toast.makeText(mAppContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLongMessage(String msg) {
        if (mAppContext != null) {
            Toast.makeText(mAppContext, msg, Toast.LENGTH_LONG).show();
        }
    }

    public static void toastErrorCode(int errorCode) {
        switch (errorCode) {
            case ERROR_SETUP_CAMERA_FAILED:
                ToastUtils.showShortMessage("摄像头配置错误");
                break;
            case ERROR_SETUP_MICROPHONE_FAILED:
                ToastUtils.showShortMessage("麦克风配置错误");
                break;
            case ERROR_NO_VIDEO_TRACK:
                ToastUtils.showShortMessage("该文件没有视频信息！");
                break;
            case ERROR_SRC_DST_SAME_FILE_PATH:
                ToastUtils.showShortMessage("源文件路径和目标路径不能相同！");
                break;
            case ERROR_MULTI_CODEC_WRONG:
                ToastUtils.showShortMessage("当前机型暂不支持该功能");
                break;
            case ERROR_SETUP_VIDEO_ENCODER_FAILED:
                ToastUtils.showShortMessage("视频编码器启动失败");
                break;
            case ERROR_SETUP_VIDEO_DECODER_FAILED:
                ToastUtils.showShortMessage("视频解码器启动失败");
                break;
            case ERROR_SETUP_AUDIO_ENCODER_FAILED:
                ToastUtils.showShortMessage("音频编码器启动失败");
                break;
            case ERROR_LOW_MEMORY:
                ToastUtils.showShortMessage("手机内存不足，无法对该视频进行时光倒流！");
                break;
            case ERROR_MUXER_START_FAILED:
                ToastUtils.showShortMessage("MUXER 启动失败, 请检查视频格式");
                break;
            default:
                ToastUtils.showShortMessage("错误码： " + errorCode);
        }
    }
}
