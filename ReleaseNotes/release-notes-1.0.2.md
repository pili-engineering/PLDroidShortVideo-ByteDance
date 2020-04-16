# PLDroidShortVideo-ByteDance Release Notes for 1.0.2

### 简介
PLDroidShortVideo-ByteDance 是七牛推出的一款适用于 Android 平台的具有高级特效功能的短视频 SDK，提供了包括高级美颜、高级滤镜、动态贴纸、水印、断点录制、分段回删、视频编辑、混音特效、本地/云端存储在内的多种功能，支持高度定制以及二次开发。

### 版本
- 发布 ByteDancePlugin-1.0.2.jar

### 功能
- 新增获取可支持的美妆类型列表的接口
- 新增获取可支持的具体美妆效果集合的接口
- 新增获取可支持的美体效果列表的接口
- 新增更新 compose 类型特效（指的是美颜、美型、美妆、美体）列表的接口
- 新增更新可支持的滤镜列表的接口
- 新增更新可支持的动态贴纸列表接口
- 新增更新所有特效列表的接口

### 注意事项

* 从 PLDroidShortVideo v3.1.0 版本开始，需要在 Application 中初始化 SDK：

```java
PLShortVideoEnv.init(getApplicationContext());
```

* 从 ByteDancePlugin v1.0.1 开始更改了接入方式，具体使用请参见使用文档和 PLDroidShortVideoDemo。
