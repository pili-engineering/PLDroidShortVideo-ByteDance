# PLDroidShortVideo-ByteDance Release Notes for 1.0.0

### 简介
PLDroidShortVideo-ByteDance 是七牛推出的一款适用于 Android 平台的具有高级特效功能的短视频 SDK，提供了包括高级美颜、高级滤镜、动态贴纸、水印、断点录制、分段回删、视频编辑、混音特效、本地/云端存储在内的多种功能，支持高度定制以及二次开发。

### 版本
- 发布 pldroid-shortvideo-3.1.2.jar
- 发布 ByteDancePlugin-1.0.0.jar
- 发布 libpldroid_shortvideo_core.so
- 发布 libpldroid_encoder.so
- 发布 libpldroid_amix.so
- 发布 libpldroid_beauty.so
- 发布 libpldroid_crash.so
- 发布 libeffect.so
- 发布 libeffect_proxy.so

### 功能
- 高级美颜、美型
- 高级滤镜
- 动态贴纸
- 摄像头采集
- 麦克风采集
- 视频采集参数定义
- 音频采集参数定义
- 视频编码参数定义
- 音频编码参数定义
- 拍摄时长设置
- 前后台切换
- 摄像头切换
- 闪光灯设置
- 画面镜像
- 画面对焦
- 焦距调节
- 曝光调节
- 横屏拍摄
- 分段拍摄
- 静音拍摄
- 音频录制
- 屏幕录制
- 变速拍摄
- 分屏拍摄
- 实时截图
- 实时预览
- 实时美颜
- 实时滤镜
- 实时水印
- 背景音乐
- 视频导入
- 编辑预览
- 时长裁剪
- 本地转码
- 视频旋转
- 画面裁剪
- 视频旋转特效
- 单音频混音
- 滤镜特效
- 涂鸦特效
- 字幕特效
- 水印特效
- 贴纸特效
- 时间特效
- 时光倒流
- 音乐唱片
- 多音频混音
- MV 特效
- 视频拼接
- GIF 动画
- 图片拼接
- 基础转场
- 过场字幕
- 视频合成
- 图片 & GIF 图 & 视频混拼
- 草稿箱
- 接口扩展
- 外部裸数据导入
- View 录制
- 视频上传
- 断点续传
- 上传加速
- 支持 arm64-v8a, armeabi-v7a 体系架构

### 注意事项

* 从 PLDroidShortVideo v3.1.0 版本开始，需要在 Application 中初始化 SDK：

```java
PLShortVideoEnv.init(getApplicationContext());
```
