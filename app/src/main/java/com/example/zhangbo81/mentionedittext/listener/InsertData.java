package com.example.zhangbo81.mentionedittext.listener;

import com.example.zhangbo81.mentionedittext.bean.FormatRange;

/**
 * author : zhangbo
 * e-mail : zwill2014@163.com
 * date   : 2019/1/8 15:34
 * desc   : 插入的数据接口
 * version: 1.0
 */
public  interface InsertData {
    //内容
     CharSequence charSequence();

    //格式化工具
      FormatRange.Convert formatData();

    //颜色
     int color();


}
