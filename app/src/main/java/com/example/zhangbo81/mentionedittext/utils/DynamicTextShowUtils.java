package com.example.zhangbo81.mentionedittext.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangbo81.mentionedittext.bean.LableJson;
import com.example.zhangbo81.mentionedittext.R;

import java.util.ArrayList;
import java.util.List;

public class DynamicTextShowUtils {


    public interface DetailsClickListener {
        void onDetailsClick();
    }

    private static String result = "";

    /**
     * 设置帖子的富文本显示
     *  @param context          上下文
     * @param tvDynamicContent textview
     * @param content          文本内容
     * @param lableJson        高亮json
     * @param isTop            是否为置顶文本
     */
    public static void setDynamicText(final Context context, final TextView tvDynamicContent, final TextView goDynamicDetails, final String content,
                                      List<LableJson> lableJson, final boolean isTop, final DetailsClickListener listener, String id,final boolean showAll) {
        if (null == content) {
            return;
        }
        if(null == tvDynamicContent.getTag()){
            tvDynamicContent.setTag("id_"+id+"-"+"content_"+content);
        }else {
            String str = (String) tvDynamicContent.getTag();
            if(("id_"+id+"-"+"content_"+content).equals(str)){
                return;
            }else {
                tvDynamicContent.setTag("id_"+id+"-"+"content_"+content);
            }
        }

        if (isTop) {
            tvDynamicContent.setText("   " + content);
        } else {
            tvDynamicContent.setText(content);
        }
        final List<LableJson> lableJsons;
        if (lableJson == null) {
            lableJsons = new ArrayList<>();
        } else {
            lableJsons = lableJson;
        }
        tvDynamicContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if (showAll) {
                        tvDynamicContent.setMaxLines(100000);
                        goDynamicDetails.setVisibility(View.GONE);
                        result = content;
                    }else {
                        tvDynamicContent.setMaxLines(4);
                        int lines = tvDynamicContent.getLineCount();
                        if (lines > 4) {
                            result = "";
                            int start = 0;
                            int end;
                            Layout layout = tvDynamicContent.getLayout();

                            for (int i = 0; i < 4; i++) {
                                end = layout.getLineEnd(i);
                                if (content.length() > start && content.length() > end) {
                                    result += content.substring(start, end);
                                    start = end;
                                }
                            }
                            goDynamicDetails.setVisibility(View.VISIBLE);
                        }else {
                            goDynamicDetails.setVisibility(View.GONE);
                            result = content;
                        }
                    }

                    tvDynamicContent.setText("");

                    int length = result.length();
                    for (LableJson lableJson : lableJsons) {
                        if (length > lableJson.getFrom() && length < lableJson.getTo()) {
                            result = result.substring(0, lableJson.getFrom()) + "...";
                        }
                    }
                    if (isTop) {
                        SpannableString spannableString = new SpannableString("3  ");
                        Drawable d = context.getResources().getDrawable(R.mipmap.icon_dynamic_top);
                        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                        spannableString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvDynamicContent.append(spannableString);
                    }
                    String contentText = result;
                    int x = contentText.length();

                    SpannableString whoSpannable = new SpannableString(contentText);
                    for (LableJson lableJson : lableJsons) {
                        if (x > lableJson.getFrom() && x >= lableJson.getTo()) {
                            final LableJson finalLableJson = lableJson;
                            final int color = context.getResources().getColor(R.color.color_span_text);
                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View view) {
                                    if (0==finalLableJson.type) {
                                        //股票
                                        Toast.makeText(context,"股票代码="+finalLableJson.data+"股票类型="+finalLableJson.dataType,Toast.LENGTH_SHORT).show();

                                    } else if (1==finalLableJson.type) {
                                        //话题
                                        Toast.makeText(context,"话题id="+finalLableJson.data,Toast.LENGTH_SHORT).show();

                                    } else if (2==finalLableJson.type) {
                                        //计划

                                    } else if (3==finalLableJson.type) {
                                        //组合

                                    }
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setColor(color);       //设置文件颜色
                                    ds.setUnderlineText(false);      //设置下划线
                                }

                            };
                            whoSpannable.setSpan(clickableSpan, lableJson.getFrom(), lableJson.getTo(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }
                    tvDynamicContent.append(whoSpannable);
                    tvDynamicContent.setMovementMethod(CustomMovementMethod.getInstance());
                }catch (Exception e){
                    tvDynamicContent.setText(content);

                }
            }
        }, 50);


        goDynamicDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDetailsClick();
                }
            }
        });
    }

    /**
     * 查看详情
     *
     * @param context
     * @return
     */
    private static SpannableString showDetailSpan(Context context) {
        SpannableString spannableString = new SpannableString("查看详情");
        final int color = context.getResources().getColor(R.color.color_span_text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(color);       //设置文件颜色
                ds.setUnderlineText(false);      //设置下划线
            }
        };
        spannableString.setSpan(clickableSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
