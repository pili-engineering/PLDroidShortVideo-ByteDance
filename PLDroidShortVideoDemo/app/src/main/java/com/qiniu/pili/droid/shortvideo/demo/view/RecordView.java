package com.qiniu.pili.droid.shortvideo.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qiniu.pili.droid.shortvideo.demo.R;
import com.qiniu.pili.droid.shortvideo.demo.fragment.EffectFragment;
import com.qiniu.pili.droid.shortvideo.demo.fragment.StickerFragment;
import com.qiniu.pili.droid.shortvideo.demo.utils.ToastUtils;

public class RecordView extends RelativeLayout {

    public static final String TAG_EFFECT = "effect";
    public static final String TAG_STICKER = "sticker";

    public static final int ANIMATOR_DURATION = 400;

    private View mRootView;
    private GLSurfaceView mPreviewView;
    private SectionProgressBar mSectionProgressBar;
    private FocusView mFocusView;
    //顶部视图
    private ImageView mIvBack;
    private ImageView mIvSwitchLight;
    private ImageView mIvSwitchCamera;
    private TextView mTvNext;
    //侧边视图
    private LinearLayout mSideLayout;
    private LinearLayout mMusicLayout;
    private ImageView mIvMusic;
    private TextView mTvMusic;
    private VerticalSeekBar mBrightnessBar;
    //底部视图
    private RecordSpeedView mSpeedView;
    private RelativeLayout mBottomLayout;
    private ImageView mIvRecord;
    private ImageView mIvEffect;
    private ImageView mIvSticker;
    private TextView mTvDelete;
    //标志面板
    private EffectFragment mEffectFragment;
    private StickerFragment mStickerFragment;
    private FragmentManager mFragmentManager;

    private Context mContext;
    private RecordViewListener mListener;

    private float mLastScaleFactor;
    private float mScaleFactor;
    private float mMaxScaleFactor = 1.0f;

    private boolean isFrontCamera = true;
    private boolean mLightEnable = false;
    private boolean isRecording = false;

    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mRootView = LayoutInflater.from(context).inflate(R.layout.record_view, this);

        mFocusView = mRootView.findViewById(R.id.focus_view);
        mSectionProgressBar = mRootView.findViewById(R.id.section_bar);
        initPreviewView();
        initTitleView();
        initSideView();
        initBottomView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPreviewView() {
        mPreviewView = mRootView.findViewById(R.id.preview);
        final ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), mOnScaleGestureListener);
        mPreviewView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeFragment();
                if (event.getPointerCount() >= 2) {
                    scaleGestureDetector.onTouchEvent(event);
                } else if (event.getPointerCount() == 1) {
                    if (isFrontCamera) {
                        return true;
                    }
                    mFocusView.showView();
                    mFocusView.setLocation(event.getX(), event.getY());
                    mListener.onManualFocus((int) event.getX(), (int) event.getY());
                }
                return true;
            }
        });
    }

    /**
     * 初始化顶部视图
     */
    private void initTitleView() {
        //返回按钮
        mIvBack = mRootView.findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFocusView.release();
                mListener.onClickBack();
            }
        });
        //闪光灯
        mIvSwitchLight = mRootView.findViewById(R.id.iv_switch_light);
        updateLightSwitchView();
        mIvSwitchLight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLightEnable = !mLightEnable;
                updateLightSwitchView();
                mListener.onClickLight(mLightEnable);
            }
        });
        //截帧
        mRootView.findViewById(R.id.iv_capture_frame).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onCaptureFrameClick();
            }
        });
        //切换摄像头
        mIvSwitchCamera = mRootView.findViewById(R.id.iv_switch_camera);
        mIvSwitchCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isFrontCamera = !isFrontCamera;
                mScaleFactor = 0f;
                updateLightSwitchView();
                mListener.onClickSwitchCamera();
            }
        });
        //下一步
        mTvNext = mRootView.findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTvNext.isEnabled()) {
                    mListener.onClickNext();
                } else {
                    ToastUtils.showShortMessage("录制时长不足");
                }
            }
        });
    }

    /**
     * 初始化侧边视图
     */
    private void initSideView() {
        //侧边视图
        mSideLayout = mRootView.findViewById(R.id.ll_record_side);
        //选择音乐
        mMusicLayout = mRootView.findViewById(R.id.ll_record_music);
        mIvMusic = mRootView.findViewById(R.id.iv_record_music);
        mTvMusic = mRootView.findViewById(R.id.tv_record_music);
        mMusicLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onClickMusic();
            }
        });
        //调整曝光
        mBrightnessBar = mRootView.findViewById(R.id.brightness_bar);
        mBrightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mListener.onBrightnessChange(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化底部视图
     */
    private void initBottomView() {
        //录制速度选择
        mSpeedView = mRootView.findViewById(R.id.record_speed_bar);
        mSpeedView.setListener(mRecordSpeedViewListener);
        //底部视图
        mBottomLayout = mRootView.findViewById(R.id.rl_record_bottom);
        //录制按钮
        mIvRecord = mRootView.findViewById(R.id.iv_record_record);
        mIvRecord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onRecordStateChange(false, false, isRecording);
            }
        });
        //美颜特效
        mIvEffect = mRootView.findViewById(R.id.iv_record_effect);
        mIvEffect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPanel(TAG_EFFECT);
            }
        });
        //动态贴纸
        mIvSticker = mRootView.findViewById(R.id.iv_record_sticker);
        mIvSticker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPanel(TAG_STICKER);
            }
        });
        //回删按钮
        mTvDelete = mRootView.findViewById(R.id.tv_record_delete);
        mTvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onClickDelete();
                mSectionProgressBar.removeLastBreakPoint();
                updateDeleteView();
                updateMusicView();
                updateNextView();
            }
        });
    }

    private void showPanel(String tag) {
        if (showingFragment() != null) {
            mFragmentManager.beginTransaction().hide(showingFragment()).commit();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.push_down_in, R.anim.push_down_out);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = generateFragment(tag);
            ft.add(R.id.fl_record_panel_container, fragment, tag).commit();
        } else {
            ft.show(fragment).commit();
        }
        showOrHideBoard(false);
    }

    private void showOrHideBoard(boolean show) {
        if (show) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSpeedView.setVisibility(View.VISIBLE);
                    mBottomLayout.setVisibility(View.VISIBLE);
                    updateDeleteView();
                }
            }, ANIMATOR_DURATION);
        } else {
            mSpeedView.setVisibility(View.INVISIBLE);
            mBottomLayout.setVisibility(View.INVISIBLE);
            mTvDelete.setVisibility(View.INVISIBLE);
        }
    }

    private Fragment generateFragment(String tag) {
        switch (tag) {
            case TAG_EFFECT:
                if (mEffectFragment != null) {
                    return mEffectFragment;
                }

                final EffectFragment effectFragment = new EffectFragment();
                effectFragment.setCallback(new EffectFragment.IEffectCallback() {

                    @Override
                    public void updateComposeNodes(String[] nodes) {
                        mListener.onUpdateComposeNodes(nodes);
                    }

                    @Override
                    public void updateComposeNodeIntensity(String path, String key, float value) {
                        mListener.onUpdateComposeNodeIntensity(path, key, value);
                    }

                    @Override
                    public void onFilterSelected(String fileName) {
                        mListener.onFilterSelected(fileName);
                    }

                    @Override
                    public void onFilterValueChanged(float value) {
                        mListener.onFilterValueChanged(value);
                    }

                    @Override
                    public void setEffectOn(boolean isOn) {
                        mListener.onEffectStateChanged(isOn);
                    }

                    @Override
                    public void onDefaultClick() {
                        mListener.onRecoverDefaultConfig();
                    }
                });
                mEffectFragment = effectFragment;
                return effectFragment;
            case TAG_STICKER:
                if (mStickerFragment != null) {
                    return mStickerFragment;
                }
                StickerFragment stickerFragment = new StickerFragment();
                stickerFragment.setCallback(new StickerFragment.IStickerCallback() {
                    @Override
                    public void onStickerSelected(String fileName) {
                        mListener.onStickerSelected(fileName);
                    }
                });
                mStickerFragment = stickerFragment;
                return stickerFragment;
            default:
                return null;
        }
    }

    private Fragment showingFragment() {
        if (mEffectFragment != null && !mEffectFragment.isHidden()) {
            return mEffectFragment;
        } else if (mStickerFragment != null && !mStickerFragment.isHidden()) {
            return mStickerFragment;
        }
        return null;
    }

    private boolean closeFragment() {
        Fragment showingFragment = showingFragment();
        if (showingFragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.push_down_in, R.anim.push_down_out);
            ft.hide(showingFragment).commit();
        }

        showOrHideBoard(true);
        return showingFragment != null;
    }

    /**
     * 当录制状态改变的时候
     *
     * @param isRecording 当前的录制状态
     */
    public void onRecordStateChange(boolean isComplete, boolean isError, boolean isRecording) {
        if (isComplete || isError || (isRecording && mListener.onStopRecordClick())) {
            mSectionProgressBar.stop();
            if (isError) {
                mSectionProgressBar.removeLastBreakPoint();
            }
            updateMusicView();
            updateNextView();
            mIvRecord.setImageResource(R.mipmap.bg_record_start);
            this.isRecording = false;
        } else {
            if (mListener.onStartRecordClick()) {
                mSectionProgressBar.start();
                mIvRecord.setImageResource(R.mipmap.bg_record_stop);
                this.isRecording = true;
            } else {
                ToastUtils.showShortMessage("开始录制失败！");
            }
        }
        updateOtherView();
    }

    public void deleteLastSection() {
        mSectionProgressBar.removeLastBreakPoint();
    }

    /**
     * 更新回删按钮状态
     */
    private void updateDeleteView() {
        if (mSectionProgressBar.isRecorded() && !isRecording) {
            mTvDelete.setVisibility(VISIBLE);
        } else {
            mTvDelete.setVisibility(INVISIBLE);
        }
    }

    /**
     * 更新音乐按钮状态
     */
    private void updateMusicView() {
        if (mSectionProgressBar.isRecorded()) {
            //已经开始录制不允许更改音乐
            mMusicLayout.setClickable(false);
            mIvMusic.setColorFilter(ContextCompat.getColor(getContext(), R.color.disable_filter), PorterDuff.Mode.MULTIPLY);
            mTvMusic.setTextColor(ContextCompat.getColor(getContext(), R.color.disable_filter));
        } else {
            mMusicLayout.setClickable(true);
            mIvMusic.clearColorFilter();
            mTvMusic.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    private void updateNextView() {
        boolean canNext = mSectionProgressBar.getCurrentRecordTimeMs() > mSectionProgressBar.getFirstPointTime();
        mTvNext.setEnabled(canNext);
    }

    /**
     * 更新闪光灯按钮
     */
    private void updateLightSwitchView() {
        if (isFrontCamera) {
            mIvSwitchLight.setClickable(false);
            // 前置摄像头状态, 闪光灯图标变灰
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.icon_record_light_off);
            mIvSwitchLight.setImageDrawable(drawable);
            mIvSwitchLight.setColorFilter(ContextCompat.getColor(getContext(), R.color.disable_filter), PorterDuff.Mode.MULTIPLY);
        } else {
            mIvSwitchLight.setClickable(true);
            // 后置摄像头状态, 清除过滤器
            mIvSwitchLight.clearColorFilter();
            if (mLightEnable) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.icon_record_light_on);
                mIvSwitchLight.setImageDrawable(drawable);
            } else {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.icon_record_light_off);
                mIvSwitchLight.setImageDrawable(drawable);
            }
        }
    }

    private void updateOtherView() {
        if (isRecording) {
            mIvBack.setVisibility(INVISIBLE);
            mIvSwitchLight.setVisibility(INVISIBLE);
            mIvSwitchCamera.setVisibility(INVISIBLE);
            mTvNext.setVisibility(INVISIBLE);
            mSideLayout.setVisibility(INVISIBLE);
            mSpeedView.setVisibility(INVISIBLE);
            mIvEffect.setVisibility(INVISIBLE);
            mIvSticker.setVisibility(INVISIBLE);
        } else {
            mIvBack.setVisibility(VISIBLE);
            mIvSwitchLight.setVisibility(VISIBLE);
            mIvSwitchCamera.setVisibility(VISIBLE);
            mTvNext.setVisibility(VISIBLE);
            mSideLayout.setVisibility(VISIBLE);
            mSpeedView.setVisibility(VISIBLE);
            mIvEffect.setVisibility(VISIBLE);
            mIvSticker.setVisibility(VISIBLE);
        }
        updateDeleteView();
    }

    public GLSurfaceView getPreviewView() {
        return mPreviewView;
    }

    public void adjustExposureCompensationRange(int range) {
        mBrightnessBar.setMax(range);
        mBrightnessBar.setProgress(range / 2);
    }

    public void setMaxPreviewScale(float scaleFactor) {
        mMaxScaleFactor = scaleFactor;
    }

    /**
     * 设置最短录制时间
     *
     * @param timeMs 录制时间（毫秒）
     */
    public void setMinRecordTime(long timeMs) {
        mSectionProgressBar.setFirstPointTime(timeMs);
    }

    /**
     * 设置最长录制时间
     *
     * @param timeMs 录制时间（毫秒）
     */
    public void setMaxRecordTime(long timeMs) {
        mSectionProgressBar.setTotalTime(mContext, timeMs);
    }

    /**
     * 设置整体视图监听
     *
     * @param listener 视图监听
     */
    public void setRecordViewListener(RecordViewListener listener) {
        mListener = listener;
    }

    /**
     * 设置 FragmentManager
     *
     * @param fragmentManager
     */
    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public EffectFragment getEffectFragment() {
        return mEffectFragment;
    }

    public void release() {
        mEffectFragment = null;
        mStickerFragment = null;
        mPreviewView = null;
        mFocusView.release();
    }

    /**
     * 录制速度改变监听
     */
    private RecordSpeedView.RecordSpeedViewListener mRecordSpeedViewListener = new RecordSpeedView.RecordSpeedViewListener() {
        @Override
        public void onSelectRecordSpeed(double speed) {
            mListener.onSelectRecordSpeed(speed);
            mSectionProgressBar.setProceedingSpeed(speed);
        }
    };

    /**
     * 预览视图的缩放手势监听
     */
    private ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float factorOffset = detector.getScaleFactor() - mLastScaleFactor;
            mScaleFactor += factorOffset;
            mLastScaleFactor = detector.getScaleFactor();
            if (mScaleFactor < 1.0f) {
                mScaleFactor = 1.0f;
            }
            if (mScaleFactor > mMaxScaleFactor) {
                mScaleFactor = mMaxScaleFactor;
            }
            mListener.onPreviewViewScale(mScaleFactor);
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mLastScaleFactor = detector.getScaleFactor();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    };

    /**
     * fragment 的关闭接口，每个 fragment 都要实现
     */
    public interface OnCloseListener {
        void onClose();
    }
}
