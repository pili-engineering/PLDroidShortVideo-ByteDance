package com.qiniu.pili.droid.shortvideo.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.pili.droid.shortvideo.demo.R;

import static com.qiniu.pili.droid.shortvideo.demo.utils.RecordSettings.RECORD_SPEED_ARRAY;

public class RecordSpeedView extends LinearLayout {

    private TextView mSuperSlowSpeedTextView;
    private TextView mSlowSpeedTextView;
    private TextView mNormalSpeedTextView;
    private TextView mFastSpeedTextView;
    private TextView mSuperFastSpeedTextView;
    private TextView mCurrentSelectTextView;

    private RecordSpeedViewListener mListener;

    private double mCurrentRecordSpeed = 1.0;

    public RecordSpeedView(Context context) {
        this(context,null);
    }

    public RecordSpeedView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecordSpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        addListener();
    }

    private void addListener() {
        mSuperSlowSpeedTextView.setOnClickListener(mOnClickListener);
        mSlowSpeedTextView.setOnClickListener(mOnClickListener);
        mNormalSpeedTextView.setOnClickListener(mOnClickListener);
        mFastSpeedTextView.setOnClickListener(mOnClickListener);
        mSuperFastSpeedTextView.setOnClickListener(mOnClickListener);
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.record_speed_view, this, true);
        mSuperSlowSpeedTextView = view.findViewById(R.id.super_slow_speed_text);
        mSlowSpeedTextView = view.findViewById(R.id.slow_speed_text);
        mNormalSpeedTextView = view.findViewById(R.id.normal_speed_text);
        mFastSpeedTextView = view.findViewById(R.id.fast_speed_text);
        mSuperFastSpeedTextView = view.findViewById(R.id.super_fast_speed_text);

        mNormalSpeedTextView.setSelected(true);
        mCurrentSelectTextView = mNormalSpeedTextView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            double recordSpeed;
            switch (view.getId()) {
                case R.id.super_slow_speed_text:
                    recordSpeed = RECORD_SPEED_ARRAY[0];
                    break;
                case R.id.slow_speed_text:
                    recordSpeed = RECORD_SPEED_ARRAY[1];
                    break;
                case R.id.normal_speed_text:
                    recordSpeed = RECORD_SPEED_ARRAY[2];
                    break;
                case R.id.fast_speed_text:
                    recordSpeed = RECORD_SPEED_ARRAY[3];
                    break;
                case R.id.super_fast_speed_text:
                    recordSpeed = RECORD_SPEED_ARRAY[4];
                    break;
                default:
                    recordSpeed = 1.0f;
                    break;
            }
            if (mCurrentRecordSpeed == recordSpeed) {
                //与之前设置的录制速度相同 不再重复设置
                return;
            }
            mListener.onSelectRecordSpeed(recordSpeed);
            mCurrentRecordSpeed = recordSpeed;
            mCurrentSelectTextView.setSelected(false);
            mCurrentSelectTextView=(TextView)view;
            mCurrentSelectTextView.setSelected(true);
        }
    };

    public void setListener(RecordSpeedViewListener listener) {
        mListener = listener;
    }

    public double getCurrentSpeed() {
        return mCurrentRecordSpeed;
    }

    public interface RecordSpeedViewListener{
        void onSelectRecordSpeed(double speed);
    }
}
