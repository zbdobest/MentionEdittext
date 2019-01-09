package com.example.zhangbo81.mentionedittext.utils;

import android.text.TextUtils;

import com.example.zhangbo81.mentionedittext.bean.DynamicPostResult;
import com.example.zhangbo81.mentionedittext.bean.DynamicSaveResult;
import com.example.zhangbo81.mentionedittext.bean.FormatRange;
import com.example.zhangbo81.mentionedittext.bean.LableJson;
import com.example.zhangbo81.mentionedittext.bean.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhangbo at 2018/11/13
 */
public class FormatRangeManager extends RangeManager {

  public DynamicSaveResult getFormatCharSequence(String text) {
    if (TextUtils.isEmpty(text)) {
      return null;
    }
    DynamicSaveResult demo = new DynamicSaveResult();
    ArrayList<? extends Range> ranges = get();
    Collections.sort(ranges);

    CharSequence newChar;
    List<CharSequence> list = new ArrayList<>();
    for (Range range : ranges) {
      if (range instanceof FormatRange) {
        FormatRange formatRange = (FormatRange) range;
        FormatRange.Convert convert = formatRange.getConvert();
        newChar = convert.formatCharSequence(range);
        list.add(newChar);
      }
    }
    demo.list = list;
    demo.text = text;
    return demo;
  }

  public DynamicPostResult getFormatTarget(String text) {
    if (TextUtils.isEmpty(text)) {
      return null;
    }
    DynamicPostResult demo = new DynamicPostResult();
    ArrayList<? extends Range> ranges = get();
    Collections.sort(ranges);

    List<LableJson> list = new ArrayList<>();
    for (Range range : ranges) {
      if (range instanceof FormatRange) {
        FormatRange formatRange = (FormatRange) range;
        FormatRange.Convert convert = formatRange.getConvert();
        LableJson target = convert.formatCharSequenceToTarget(range);
        list.add(target);
      }
    }
    demo.list = list;
    demo.text = text;
    return demo;
  }
}
