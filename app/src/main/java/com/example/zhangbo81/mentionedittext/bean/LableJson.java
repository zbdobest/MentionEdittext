package com.example.zhangbo81.mentionedittext.bean;

/**
 * author : zhangbo
 * e-mail : zwill2014@163.com
 * date   : 2019/1/8 15:15
 * desc   :
 * version: 1.0
 */
public class LableJson extends Range {
    public String name;
    public String text;
    public String dataType;

    public LableJson(int from, int to) {
        super(from, to);
    }

    public LableJson() {
    }

}
