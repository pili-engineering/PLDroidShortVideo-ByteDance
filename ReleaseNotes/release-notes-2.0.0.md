# PLDroidShortVideo-ByteDance Release Notes for 2.0.0

### 简介
PLDroidShortVideo-ByteDance 是七牛推出的一款适用于 Android 平台的具有高级特效功能的短视频 SDK，提供了包括高级美颜、高级滤镜、动态贴纸、水印、断点录制、分段回删、视频编辑、混音特效、本地/云端存储在内的多种功能，支持高度定制以及二次开发。

### 版本
- 发布 pldroid-bytedance-effect-2.0.0.jar
- 发布字节资源 v3.9 处理脚本
- 发布 libc++_shared.so
- 升级 libeffect.so
- 升级 libeffect_proxy.so

### 功能
- 修复了美妆中口红特效出现在嘴唇遮挡物上的问题
- 优化了美体的边缘效果
- 优化了 YUV 处理速度

### 注意事项
* **pldroid-bytedance-effect-2.0.0.jar 较之前版本接口变动较大，升级请仔细阅读[文档](../docs/PLDroidShortVideo-ByteDance.md)与 demo。**
* **此版本仅支持字节跳动资源版本 v3.9.0，如版本不匹配请重新申请资源。**
* 从 PLDroidShortVideo v3.1.0 版本开始，需要在 Application 中初始化 SDK：

```java
PLShortVideoEnv.init(getApplicationContext());
```
