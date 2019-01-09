/*
 * Copyright 2016 Andy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.zhangbo81.mentionedittext.widget;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhangbo81.mentionedittext.bean.DynamicPostResult;
import com.example.zhangbo81.mentionedittext.bean.DynamicSaveResult;
import com.example.zhangbo81.mentionedittext.bean.FormatRange;
import com.example.zhangbo81.mentionedittext.bean.LableJson;
import com.example.zhangbo81.mentionedittext.bean.Range;
import com.example.zhangbo81.mentionedittext.bean.StockSearchResult;
import com.example.zhangbo81.mentionedittext.bean.TopicSearchResult;
import com.example.zhangbo81.mentionedittext.listener.InsertData;
import com.example.zhangbo81.mentionedittext.listener.MentionInputConnection;
import com.example.zhangbo81.mentionedittext.listener.MentionTextWatcher;
import com.example.zhangbo81.mentionedittext.utils.FormatRangeManager;
import com.example.zhangbo81.mentionedittext.utils.RangeManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;


/**
 * MentionEditText adds some useful features for mention string(@xxxx), such as highlight,
 * intelligent deletion, intelligent selection and '@' input detection, etc.
 *
 * @author Andy
 */
public class MentionEditText extends EditText {
    private Runnable mAction;

    private boolean mIsSelected;

    public MentionEditText(Context context) {
        super(context);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MentionInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }

    @Override
    public void setText(final CharSequence text, BufferType type) {
        super.setText(text, type);
        //hack, put the cursor at the end of text after calling setText() method
        if (mAction == null) {
            mAction = new Runnable() {
                @Override
                public void run() {
                    setSelection(getText().length());
                }
            };
        }
        post(mAction);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //avoid infinite recursion after calling setSelection()
        if (null != mRangeManager && !mRangeManager.isEqual(selStart, selEnd)) {
            //if user cancel a selection of mention string, reset the state of 'mIsSelected'
            Range closestRange = mRangeManager.getRangeOfClosestMentionString(selStart, selEnd);
            if (closestRange != null && closestRange.getTo() == selEnd) {
                mIsSelected = false;
            }

            Range nearbyRange = mRangeManager.getRangeOfNearbyMentionString(selStart, selEnd);
            //if there is no mention string nearby the cursor, just skip
            if (null != nearbyRange) {
                //forbid cursor located in the mention string.
                if (selStart == selEnd) {
                    setSelection(nearbyRange.getAnchorPosition(selStart));
                } else {
                    if (selEnd < nearbyRange.getTo()) {
                        setSelection(selStart, nearbyRange.getTo());
                    }
                    if (selStart > nearbyRange.getFrom()) {
                        setSelection(nearbyRange.getFrom(), selEnd);
                    }
                }
            }
        }
    }

    public void insert(Context context, InsertData insertData, int type, String data) {
        if (null != insertData) {

            //话题插入3个并且当前插入的也是话题内容
            if(mRangeManager.getTopicList().size() >= 3 && 1== type){
                Toast.makeText(context, "话题最多只能插入三个", Toast.LENGTH_SHORT).show();
                return;
            }

            //是否插入相同话题
            List<Range> list = mRangeManager.getTopicList();
            for (Range range : list) {
                if(range.data != null && range.data.equals(data)){
                    Toast.makeText(context, "不能插入相同话题", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            CharSequence charSequence = insertData.charSequence();
            Editable editable = getText();
            int start = getSelectionStart();
            int end = start + charSequence.length();
            if(end > 500){
                return;
            }
            editable.insert(start, charSequence+" ");
            FormatRange.Convert format = insertData.formatData();
            FormatRange range = new FormatRange(start, end);
            range.type = type;
            range.data = data;
            range.setConvert(format);
            mRangeManager.add(range);
            int color = insertData.color();
            editable.setSpan(new ForegroundColorSpan(color), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public DynamicSaveResult getFormatCharSequence() {
        String text = getText().toString();
        return mRangeManager.getFormatCharSequence(text);
    }

    public DynamicPostResult getFormatTarget() {
        String text = getText().toString();
        return mRangeManager.getFormatTarget(text);
    }

    public void clear() {
        mRangeManager.clear();
        setText("");
    }

    protected FormatRangeManager mRangeManager;

    private void init() {
        mRangeManager = new FormatRangeManager();
        //disable suggestion
        addTextChangedListener(new MentionTextWatcher(this));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    public RangeManager getRangeManager() {
        return mRangeManager;
    }

    @Override
    public boolean isSelected() {
        return mIsSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public void setSaveText(DynamicSaveResult demo) {
        clear();
        Editable editable = getText();
        editable.insert(0,demo.text);
        for (CharSequence charSequence : demo.list) {
            LableJson target = new GsonBuilder().create().fromJson(charSequence.toString(), LableJson.class);
            if (0==target.type) {
                StockSearchResult tag = new StockSearchResult(target.dataType, target.name,target.data);
                CharSequence charSequence2 = tag.charSequence();
                int end = target.getFrom() + charSequence2.length();
                editable.replace(target.getFrom(),end,"");
                editable.insert(target.getFrom(), charSequence2+"");
                FormatRange.Convert format = tag.formatData();
                FormatRange range = new FormatRange(target.getFrom(), end);
                range.type = 0;
                range.data = target.data;
                range.setConvert(format);
                mRangeManager.add(range);

                int color = tag.color();
                editable.setSpan(new ForegroundColorSpan(color), target.getFrom(), end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (1==target.type) {
                TopicSearchResult user = new TopicSearchResult(target.data, target.name);
                CharSequence charSequence2 = user.charSequence();
                int end = target.getFrom() + charSequence2.length();
                editable.replace(target.getFrom(),end,"");
                editable.insert(target.getFrom(), charSequence2+"");
                FormatRange.Convert format = user.formatData();
                FormatRange range = new FormatRange(target.getFrom(), end);
                range.type = 1;
                range.data = target.data;
                range.setConvert(format);
                mRangeManager.add(range);

                int color = user.color();
                editable.setSpan(new ForegroundColorSpan(color), target.getFrom(), end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }
}
