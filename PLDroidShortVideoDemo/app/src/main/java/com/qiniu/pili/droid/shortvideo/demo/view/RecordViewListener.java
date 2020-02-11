package com.qiniu.pili.droid.shortvideo.demo.view;

import com.qiniu.pili.droid.shortvideo.demo.model.ComposerNode;

import java.io.File;

public interface RecordViewListener extends RecordSpeedView.RecordSpeedViewListener {

    /**
     * 摄像头对焦
     *
     * @param x x 坐标
     * @param y y 坐标
     */
    void onManualFocus(int x, int y);

    /**
     * 预览界面缩放
     *
     * @param scale 缩放倍数
     */
    void onPreviewViewScale(float scale);

    /**
     * 点击返回
     */
    void onClickBack();

    /**
     * 闪光灯开关
     */
    void onClickLight(boolean enable);

    /**
     * 截帧
     */
    void onCaptureFrameClick();

    /**
     * 摄像头转换事件
     */
    void onClickSwitchCamera();

    /**
     * 下一步
     */
    void onClickNext();

    /**
     * 选择音乐
     */
    void onClickMusic();

    /**
     * 曝光度发生改变
     *
     * @param brightness 曝光补偿
     */
    void onBrightnessChange(int brightness);

    /**
     * 开始录制
     */
    boolean onStartRecordClick();

    /**
     * 停止录制
     */
    boolean onStopRecordClick();

    /**
     * 回删点击事件
     */
    void onClickDelete();

    /**
     * 更新特效节点效果
     *
     * @param nodes 节点名字
     */
    void onUpdateComposeNodes(String[] nodes);

    /**
     * 更新特效节点强度
     *
     * @param key 节点 key 值
     * @param value 节点强度
     */
    void onUpdateComposeNodeIntensity(String key,float value);

    /**
     * 滤镜选择事件
     *
     * @param fileName 选择的滤镜文件名称
     */
    void onFilterSelected(String fileName);

    /**
     * 滤镜强度改变
     *
     * @param value 强度系数
     */
    void onFilterValueChanged(float value);

    /**
     * 特效状态改变
     *
     * @param isOn 是否开启
     */
    void onEffectStateChanged(boolean isOn);

    /**
     * 特效恢复默认设置
     */
    void onRecoverDefaultConfig();

    /**
     * 贴纸选择事件
     *
     * @param fileName 选择的贴纸文件名称
     */
    void onStickerSelected(String fileName);

}
