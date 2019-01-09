package com.example.zhangbo81.mentionedittext.bean;


import java.io.Serializable;
import java.util.List;

public class DynamicPostResult implements Serializable {
    public CharSequence text;
    public List<LableJson> list;

    @Override
    public String toString() {
        return "DynamicSaveResult{" +
                "text=" + text +
                ", list=" + list +
                '}';
    }
}
