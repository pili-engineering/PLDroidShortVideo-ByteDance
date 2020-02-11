package com.qiniu.pili.droid.shortvideo.demo.fragment.contract.presenter;

import com.qiniu.bytedanceplugin.ByteDancePlugin;
import com.qiniu.bytedanceplugin.model.MakeUpModel;
import com.qiniu.pili.droid.shortvideo.demo.fragment.contract.ItemGetContract;
import com.qiniu.pili.droid.shortvideo.demo.model.ButtonItem;
import com.qiniu.pili.droid.shortvideo.demo.model.ComposerNode;

import java.util.ArrayList;
import java.util.List;

import static com.qiniu.pili.droid.shortvideo.demo.fragment.contract.ItemGetContract.*;


public class ItemGetPresenter extends ItemGetContract.Presenter {

    @Override
    public List<ButtonItem> getItems(int type) {
        List<ButtonItem> items = new ArrayList<>();
        items.add(new ButtonItem(null, "清除", 0d,new ComposerNode(TYPE_CLOSE)));
        List<MakeUpModel> makeUpModels;
        switch (type & MASK) {
            case TYPE_BEAUTY_FACE:
                makeUpModels = ByteDancePlugin.getBeautyList();
                break;
            case TYPE_BEAUTY_RESHAPE:
                makeUpModels = ByteDancePlugin.getShapeList();
                break;
            default:
                makeUpModels = new ArrayList<>();
        }
        for (MakeUpModel makeUpModel : makeUpModels) {
            items.add(new ButtonItem(makeUpModel.getIconPath(), makeUpModel.getEffectName(), makeUpModel.getDefaultIntensity(),
                    new ComposerNode(translateKey2id(makeUpModel.getKey()), makeUpModel.getFileName(), makeUpModel.getKey())));
        }
        return items;
    }

    private int translateKey2id(String key) {
        switch (key) {
            case "smooth":
                return TYPE_BEAUTY_FACE_SMOOTH;
            case "whiten":
                return TYPE_BEAUTY_FACE_WHITEN;
            case "sharp":
                return TYPE_BEAUTY_FACE_SHARPEN;
            case "Internal_Deform_Overall":
                return TYPE_BEAUTY_RESHAPE_FACE_OVERALL;
            case "Internal_Deform_Eye":
                return TYPE_BEAUTY_RESHAPE_EYE;
            default:
                return -1;
        }
    }


}
