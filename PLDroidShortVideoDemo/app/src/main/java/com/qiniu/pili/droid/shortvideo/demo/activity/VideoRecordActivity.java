package com.qiniu.pili.droid.shortvideo.demo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.qiniu.bytedanceplugin.ByteDancePlugin;
import com.qiniu.bytedanceplugin.effectsdk.BytedEffectConstants;
import com.qiniu.pili.droid.shortvideo.*;
import com.qiniu.pili.droid.shortvideo.demo.R;
import com.qiniu.pili.droid.shortvideo.demo.utils.Config;
import com.qiniu.pili.droid.shortvideo.demo.utils.GetPathFromUri;
import com.qiniu.pili.droid.shortvideo.demo.utils.NotchScreenUtil;
import com.qiniu.pili.droid.shortvideo.demo.utils.RecordSettings;
import com.qiniu.pili.droid.shortvideo.demo.utils.ToastUtils;
import com.qiniu.pili.droid.shortvideo.demo.view.CustomProgressDialog;
import com.qiniu.pili.droid.shortvideo.demo.view.RecordView;
import com.qiniu.pili.droid.shortvideo.demo.view.RecordViewListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.qiniu.pili.droid.shortvideo.demo.utils.RecordSettings.chooseCameraFacingId;

public class VideoRecordActivity extends AppCompatActivity {

    public static final String PREVIEW_SIZE_RATIO = "PreviewSizeRatio";
    public static final String PREVIEW_SIZE_LEVEL = "PreviewSizeLevel";
    public static final String ENCODING_MODE = "EncodingMode";
    public static final String ENCODING_SIZE_LEVEL = "EncodingSizeLevel";
    public static final String ENCODING_BITRATE_LEVEL = "EncodingBitrateLevel";
    public static final String AUDIO_CHANNEL_NUM = "AudioChannelNum";
    public static final String DRAFT = "draft";

    private static final int REQUEST_CODE_ADD_MIX_MUSIC = 10001;

    private PLShortVideoRecorder mShortVideoRecorder;
    private ByteDancePlugin mByteDancePlugin;

    private RecordView mRecordView;
    private CustomProgressDialog mProcessingDialog;

    private List<Float> mStandScaleFactors;
    private boolean mIsEditVideo = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        if (!NotchScreenUtil.checkNotchScreen(this)) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_record);
        mRecordView = findViewById(R.id.videoRecordView);

        initRecordSetting();
        initViewSetting();
    }

    /**
     * 初始化视图设置
     */
    private void initViewSetting() {
        mRecordView.setFragmentManager(getSupportFragmentManager());
        mRecordView.setRecordViewListener(mRecordViewListener);
        mRecordView.setMinRecordTime(RecordSettings.DEFAULT_MIN_RECORD_DURATION);
        mProcessingDialog = new CustomProgressDialog(this);
        mProcessingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShortVideoRecorder.cancelConcat();
            }
        });
    }

    /**
     * 初始化录制设置
     */
    private void initRecordSetting() {
        mShortVideoRecorder = new PLShortVideoRecorder();
        mShortVideoRecorder.setRecordStateListener(mRecordStateListener);

        int previewSizeRatioPos = getIntent().getIntExtra(PREVIEW_SIZE_RATIO, 0);
        int previewSizeLevelPos = getIntent().getIntExtra(PREVIEW_SIZE_LEVEL, 0);
        int encodingModePos = getIntent().getIntExtra(ENCODING_MODE, 0);
        int encodingSizeLevelPos = getIntent().getIntExtra(ENCODING_SIZE_LEVEL, 0);
        int encodingBitrateLevelPos = getIntent().getIntExtra(ENCODING_BITRATE_LEVEL, 0);
        int audioChannelNumPos = getIntent().getIntExtra(AUDIO_CHANNEL_NUM, 0);

        PLCameraSetting cameraSetting = new PLCameraSetting();
        PLCameraSetting.CAMERA_FACING_ID facingId = chooseCameraFacingId();
        cameraSetting.setCameraId(facingId);
        cameraSetting.setCameraPreviewSizeRatio(RecordSettings.PREVIEW_SIZE_RATIO_ARRAY[previewSizeRatioPos]);
        cameraSetting.setCameraPreviewSizeLevel(RecordSettings.PREVIEW_SIZE_LEVEL_ARRAY[previewSizeLevelPos]);

        PLMicrophoneSetting microphoneSetting = new PLMicrophoneSetting();
        microphoneSetting.setChannelConfig(RecordSettings.AUDIO_CHANNEL_NUM_ARRAY[audioChannelNumPos] == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO);

        PLVideoEncodeSetting videoEncodeSetting = new PLVideoEncodeSetting(this);
        videoEncodeSetting.setEncodingSizeLevel(RecordSettings.ENCODING_SIZE_LEVEL_ARRAY[encodingSizeLevelPos]);
        videoEncodeSetting.setEncodingBitrate(RecordSettings.ENCODING_BITRATE_LEVEL_ARRAY[encodingBitrateLevelPos]);
        videoEncodeSetting.setHWCodecEnabled(encodingModePos == 0);
        videoEncodeSetting.setConstFrameRateEnabled(true);

        PLAudioEncodeSetting audioEncodeSetting = new PLAudioEncodeSetting();
        audioEncodeSetting.setHWCodecEnabled(encodingModePos == 0);
        audioEncodeSetting.setChannels(RecordSettings.AUDIO_CHANNEL_NUM_ARRAY[audioChannelNumPos]);

        PLRecordSetting recordSetting = new PLRecordSetting();
        recordSetting.setMaxRecordDuration(RecordSettings.DEFAULT_MAX_RECORD_DURATION);
        recordSetting.setRecordSpeedVariable(true);
        recordSetting.setVideoCacheDir(Config.VIDEO_STORAGE_DIR);
        recordSetting.setVideoFilepath(Config.RECORD_FILE_PATH);

        mShortVideoRecorder.prepare(mRecordView.getPreviewView(), cameraSetting, microphoneSetting, videoEncodeSetting, audioEncodeSetting, null, recordSetting);

        mByteDancePlugin = new ByteDancePlugin(this, ByteDancePlugin.PluginType.record, getExternalFilesDir("assets") + File.separator + "resource");
        mByteDancePlugin.setComposerMode(1);
        mShortVideoRecorder.setEffectPlugin(mByteDancePlugin);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_MIX_MUSIC && resultCode == Activity.RESULT_OK) {
            String selectedFilepath = GetPathFromUri.getPath(this, data.getData());
            if (selectedFilepath != null && !"".equals(selectedFilepath)) {
                mShortVideoRecorder.setMusicFile(selectedFilepath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecordView.getPreviewView().onResume();
        mShortVideoRecorder.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecordView.getPreviewView().onPause();
        mShortVideoRecorder.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShortVideoRecorder.destroy();
        mRecordView.release();
    }

    /**
     * 显示选择弹窗
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.if_edit_video));
        builder.setPositiveButton(getString(R.string.dlg_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIsEditVideo = true;
                mShortVideoRecorder.concatSections(mVideoSaveListener);
            }
        });
        builder.setNegativeButton(getString(R.string.dlg_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIsEditVideo = false;
                mShortVideoRecorder.concatSections(mVideoSaveListener);
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 录制状态回调
     */
    private PLRecordStateListener mRecordStateListener = new PLRecordStateListener() {
        @Override
        public void onReady() {
            //当摄像头准备就绪 运行在主线程
            int exposureCompensationRange = mShortVideoRecorder.getMaxExposureCompensation() - mShortVideoRecorder.getMinExposureCompensation();
            mRecordView.adjustExposureCompensationRange(exposureCompensationRange);
            mStandScaleFactors = mShortVideoRecorder.getZooms();
            mRecordView.setMaxPreviewScale(mStandScaleFactors.get(mStandScaleFactors.size() - 1));
        }

        @Override
        public void onError(final int i) {
            //当录制失败时 运行在子线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecordView.onRecordStateChange(false, true, true);
                    ToastUtils.showShortMessage("错误码：" + i);
                }
            });
        }

        @Override
        public void onDurationTooShort() {
            //当录制时长太短 运行在子线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShortMessage("录制时长过短！");
                    mRecordView.deleteLastSection();
                }
            });
        }

        @Override
        public void onRecordStarted() {
            //录制开始时 运行在子线程
        }

        @Override
        public void onSectionRecording(long l, long l1, int i) {
            //正在录制时 运行在子线程
        }

        @Override
        public void onRecordStopped() {
            //录制停止时 运行在子线程
        }

        @Override
        public void onSectionIncreased(final long incDurationMs, long totalDurationMs, int sectionCount) {
            //当分段录制增加时 运行在子线程
        }

        @Override
        public void onSectionDecreased(long incDurationMs, long totalDurationMs, int sectionCount) {
            //当分段录制减少时 运行在主线程
        }

        @Override
        public void onRecordCompleted() {
            //当整体录制完成时 运行在子线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecordView.onRecordStateChange(true, false, true);
                }
            });
        }
    };

    /**
     * 录制界面回调
     */
    private RecordViewListener mRecordViewListener = new RecordViewListener() {

        @Override
        public void onManualFocus(int x, int y) {
            mShortVideoRecorder.manualFocus(250, 250, x, y);
        }

        @Override
        public void onPreviewViewScale(float scale) {
            for (Float scaleFactor : mStandScaleFactors) {
                if (scale <= scaleFactor) {
                    scale = scaleFactor;
                    break;
                }
            }
            mShortVideoRecorder.setZoom(scale);
        }

        @Override
        public void onClickBack() {
            finish();
        }

        @Override
        public void onClickLight(boolean enable) {
            mShortVideoRecorder.setFlashEnabled(enable);
        }

        @Override
        public void onCaptureFrameClick() {
            mShortVideoRecorder.captureFrame(mCaptureFrameListener);
        }

        @Override
        public void onClickSwitchCamera() {
            mShortVideoRecorder.switchCamera();
        }

        @Override
        public void onClickNext() {
            mProcessingDialog.show();
            showChooseDialog();
        }

        @Override
        public void onClickMusic() {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
            } else {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/*");
            }
            startActivityForResult(Intent.createChooser(intent, "请选择混音文件："), REQUEST_CODE_ADD_MIX_MUSIC);
        }

        @Override
        public void onBrightnessChange(int brightness) {
            mShortVideoRecorder.setExposureCompensation(mShortVideoRecorder.getMinExposureCompensation() + brightness);
        }

        @Override
        public void onSelectRecordSpeed(double speed) {
            mShortVideoRecorder.setRecordSpeed(speed);
        }

        @Override
        public boolean onStartRecordClick() {
            return mShortVideoRecorder.beginSection();
        }

        @Override
        public boolean onStopRecordClick() {
            return mShortVideoRecorder.endSection();
        }

        @Override
        public void onClickDelete() {
            mShortVideoRecorder.deleteLastSection();
        }

        @Override
        public void onUpdateComposeNodes(final String[] nodes) {
            mRecordView.getPreviewView().queueEvent(new Runnable() {
                @Override
                public void run() {
                    mByteDancePlugin.setComposeNodes(nodes);
                }
            });
        }

        @Override
        public void onUpdateComposeNodeIntensity(final String key, final float value) {
            mRecordView.getPreviewView().queueEvent(new Runnable() {
                @Override
                public void run() {
                    mByteDancePlugin.updateComposeNode(key, value);
                }
            });
        }

        @Override
        public void onFilterSelected(final String fileName) {
            mRecordView.getPreviewView().queueEvent(new Runnable() {
                @Override
                public void run() {
                    mByteDancePlugin.setFilter(fileName != null ? fileName : "");
                }
            });
        }

        @Override
        public void onFilterValueChanged(final float value) {
            mRecordView.getPreviewView().queueEvent(new Runnable() {
                @Override
                public void run() {
                    mByteDancePlugin.updateIntensity(BytedEffectConstants.IntensityType.Filter, value);
                }
            });
        }

        @Override
        public void onEffectStateChanged(boolean isOn) {
            mByteDancePlugin.setEffectOn(isOn);
        }

        @Override
        public void onRecoverDefaultConfig() {

        }

        @Override
        public void onStickerSelected(final String fileName) {
            mRecordView.getPreviewView().queueEvent(new Runnable() {
                @Override
                public void run() {
                    mByteDancePlugin.setSticker(fileName != null ? fileName : "");
                }
            });
        }
    };

    /**
     * 截帧回调
     */
    private PLCaptureFrameListener mCaptureFrameListener = new PLCaptureFrameListener() {
        @Override
        public void onFrameCaptured(PLVideoFrame capturedFrame) {
            if (capturedFrame == null) {
                ToastUtils.showShortMessage("截帧失败！");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(Config.CAPTURED_FRAME_FILE_PATH);
                capturedFrame.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortMessage("截帧已保存到路径：" + Config.CAPTURED_FRAME_FILE_PATH);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 视频保存回调
     */
    private PLVideoSaveListener mVideoSaveListener = new PLVideoSaveListener() {
        @Override
        public void onSaveVideoSuccess(final String destFile) {
            //回调到子线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProcessingDialog.dismiss();
                    int screenOrientation = (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == getRequestedOrientation()) ? 0 : 1;
                    if (mIsEditVideo) {
                        VideoEditActivity.start(VideoRecordActivity.this, destFile, screenOrientation);
                    } else {
                        PlaybackActivity.start(VideoRecordActivity.this, destFile, screenOrientation);
                    }
                }
            });
        }

        @Override
        public void onSaveVideoFailed(final int errorCode) {
            //回调到子线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProcessingDialog.dismiss();
                    ToastUtils.showShortMessage("拼接视频段失败: " + errorCode);
                }
            });
        }

        @Override
        public void onSaveVideoCanceled() {
            //回调到主线程
            mProcessingDialog.dismiss();
        }

        @Override
        public void onProgressUpdate(final float percentage) {
            //回调到子线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProcessingDialog.setProgress((int) (100 * percentage));
                }
            });
        }
    };
}
