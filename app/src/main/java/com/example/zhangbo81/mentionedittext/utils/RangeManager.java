package com.example.zhangbo81.mentionedittext.utils;


import com.example.zhangbo81.mentionedittext.bean.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Created by zhangbo at 2018/11/13
 */
public class RangeManager {

  private ArrayList<Range> mRanges;

  public RangeManager() {
    mRanges = new ArrayList<>();
  }

  public ArrayList<? extends Range> get() {
    ensureListNonNull();
    return mRanges;
  }

  public List<Range> getTopicList(){
    if(mRanges == null || mRanges.size() == 0){
      return new ArrayList<>();
    }
    List<Range> list = new ArrayList<>();
    for (Range mRange : mRanges) {
      if(1 == mRange.type){
        list.add(mRange);
      }
    }
    return list;
  }

  public <T extends Range>void add(T range) {
    ensureListNonNull();
    mRanges.add(range);
    Collections.sort(mRanges);
  }

  public void clear() {
    ensureListNonNull();
    mRanges.clear();
  }

  public boolean isEmpty() {
    ensureListNonNull();
    return mRanges.isEmpty();
  }

  public Iterator<? extends Range> iterator(){
    ensureListNonNull();
    return mRanges.iterator();
  }

  private void ensureListNonNull() {
    if (null == mRanges) {
      mRanges = new ArrayList<>();
    }
  }
  public Range getRangeOfClosestMentionString(int selStart, int selEnd) {
    if (mRanges == null) {
      return null;
    }
    for (Range range : mRanges) {
      if (range.contains(selStart, selEnd)) {
        return range;
      }
    }
    return null;
  }

  public Range getRangeOfNearbyMentionString(int selStart, int selEnd) {
    if (mRanges == null) {
      return null;
    }
    for (Range range : mRanges) {
      if (range.isWrappedBy(selStart, selEnd)) {
        return range;
      }
    }
    return null;
  }
  ////////////
  private Range mLastSelectedRange;

  public boolean isEqual(int selStart, int selEnd) {
    return null != mLastSelectedRange && mLastSelectedRange.isEqual(selStart, selEnd);
  }

  public void setLastSelectedRange(Range lastSelectedRange) {
    mLastSelectedRange = lastSelectedRange;
  }

}
