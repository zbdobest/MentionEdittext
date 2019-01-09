package com.example.zhangbo81.mentionedittext.bean;

import android.support.annotation.NonNull;

/**
 * author : zhangbo
 * e-mail : zwill2014@163.com
 * date   : 2019/1/8 11:17
 * desc   : Range 插入对象的区间控制
 * version: 1.0
 */
public class Range implements Comparable<Range> {
    //起始位置
    public int from;
    //末尾位置
    public int to;
    //类型  0:股票  1:话题
    public int type;
    //业务id
    public String data;

    public Range() {
    }

    public Range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /**
     * 区间是否被截断
     * @param start 起始位置
     * @param end 末尾位置
     * @return
     */
    public boolean isWrapped(int start, int end) {
        return from >= start && to <= end;
    }

    /**
     * 位于区间中间
     * @param start
     * @param end
     * @return
     */
    public boolean isWrappedBy(int start, int end) {
        return (start > from && start < to) || (end > from && end < to);
    }

    /**
     * 包含在区间里面
     * @param start
     * @param end
     * @return
     */
    public boolean contains(int start, int end) {
        return from <= start && to >= end;
    }

    /**
     * 是否是同一个区间
     * @param start
     * @param end
     * @return
     */
    public boolean isEqual(int start, int end) {
        return (from == start && to == end) || (from == end && to == start);
    }

    /**
     * 锚点到区间的开始或结束位置
     * @param value
     * @return
     */
    public int getAnchorPosition(int value) {
        if ((value - from) - (to - value) >= 0) {
            return to;
        } else {
            return from;
        }
    }

    /**
     * 区间更新变化
     * @param offset 位置变化的差值
     */
    public void setOffset(int offset) {
        from += offset;
        to += offset;
    }

    @Override
    public int compareTo(@NonNull Range o) {
        return from - o.from;
    }

    public int getFrom() {
        if (from < 0) {
            from = 0;
        }
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
