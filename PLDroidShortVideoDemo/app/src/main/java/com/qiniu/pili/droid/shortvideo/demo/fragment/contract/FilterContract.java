package com.qiniu.pili.droid.shortvideo.demo.fragment.contract;

import com.qiniu.bytedanceplugin.model.FilterModel;
import com.qiniu.pili.droid.shortvideo.demo.base.BasePresenter;
import com.qiniu.pili.droid.shortvideo.demo.base.IView;

import java.util.List;

public interface FilterContract {
    interface View extends IView {

    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract List<FilterModel> getItems();
    }
}
