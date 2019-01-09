package com.example.zhangbo81.mentionedittext.bean;

/**
 * author : zhangbo
 * e-mail : zwill2014@163.com
 * date   : 2019/1/8 11:13
 * desc   : 根据区间对象转换成需要的String或LabelJson对象
 * version: 1.0
 */
public class FormatRange extends Range{

    public FormatRange(int from, int to) {
        super(from, to);
    }

    private Convert convert;

    public Convert getConvert() {
        return convert;
    }

    public void setConvert(Convert convert) {
        this.convert = convert;
    }

    //转换接口
    public interface Convert {

        //转换成String
        CharSequence formatCharSequence(Range range);

        //转换成labeljson对象
        LableJson formatCharSequenceToTarget(Range range);
    }
}
