package com.qiniu.pili.droid.shortvideo.demo.fragment.contract;

import com.qiniu.pili.droid.shortvideo.demo.base.BasePresenter;
import com.qiniu.pili.droid.shortvideo.demo.base.IView;
import com.qiniu.pili.droid.shortvideo.demo.model.ButtonItem;

import java.util.List;

/**
 * 将一个 int 分为两段，前 16 位存储一级菜单，分别是 美颜、美型、滤镜选项
 * 第 17～24 位存储二级菜单，如美颜的 大眼、瘦脸等，美型选项的 瘦脸、大眼等
 * 最后 8 位存储三级菜单，目前只有美妆选项使用到，如存储美瞳的几种种类，这个还没有集成呢
 */
public interface ItemGetContract {
    int OFFSET = 16;
    int MASK = ~0xFFFF;
    int SUB_OFFSET = 8;

    // 一级菜单

    int TYPE_CLOSE = -1;
    // 美颜
    int TYPE_BEAUTY_FACE = 1 << OFFSET;
    // 美型
    int TYPE_BEAUTY_RESHAPE = 2 << OFFSET;
    // 滤镜
    int TYPE_FILTER = 5 << OFFSET;

    // 二级菜单

    // 美颜
    int TYPE_BEAUTY_FACE_SMOOTH = TYPE_BEAUTY_FACE + (1 << SUB_OFFSET);
    int TYPE_BEAUTY_FACE_WHITEN = TYPE_BEAUTY_FACE + (2 << SUB_OFFSET);
    int TYPE_BEAUTY_FACE_SHARPEN = TYPE_BEAUTY_FACE + (3 << SUB_OFFSET);

    // 美型
    int TYPE_BEAUTY_RESHAPE_FACE_OVERALL = TYPE_BEAUTY_RESHAPE + (1 << SUB_OFFSET);
    int TYPE_BEAUTY_RESHAPE_EYE = TYPE_BEAUTY_RESHAPE + (2 << SUB_OFFSET);

    interface View extends IView {
    }

    abstract class Presenter extends BasePresenter<View> {
        /**
         * 根据类型返回一个 {@link ButtonItem} 列表
         */
        public abstract List<ButtonItem> getItems(int type);
    }
}
