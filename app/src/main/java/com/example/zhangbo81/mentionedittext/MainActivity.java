package com.example.zhangbo81.mentionedittext;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhangbo81.mentionedittext.bean.DynamicPostResult;
import com.example.zhangbo81.mentionedittext.bean.DynamicSaveResult;
import com.example.zhangbo81.mentionedittext.bean.StockSearchResult;
import com.example.zhangbo81.mentionedittext.bean.TopicSearchResult;
import com.example.zhangbo81.mentionedittext.ui.StockListActivity;
import com.example.zhangbo81.mentionedittext.ui.TopicListActivity;
import com.example.zhangbo81.mentionedittext.utils.DynamicTextShowUtils;
import com.example.zhangbo81.mentionedittext.widget.MentionEditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MentionEditText mentionEditText;
    private LinearLayout insertStockLayout;
    private LinearLayout insertTopicLayout;
    private Button btnSave;
    private Button btnShow;
    private Button btnGetContent;
    private TextView tvContent;
    private TextView tvContentDetails;

    public static final int REQUEST_USER_APPEND = 1 << 2;
    public static final int REQUEST_TAG_APPEND = 1 << 3;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        initData();
    }

    private void initData() {

    }

    private void initListener() {
        insertStockLayout.setOnClickListener(this);
        insertTopicLayout.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        btnGetContent.setOnClickListener(this);
    }

    private void initView() {
        mentionEditText = findViewById(R.id.mentionedittext);
        insertStockLayout = findViewById(R.id.ll_insert_stock);
        insertTopicLayout = findViewById(R.id.ll_insert_topic);
        btnSave = findViewById(R.id.btn_save);
        btnShow = findViewById(R.id.btn_show_save);
        btnGetContent = findViewById(R.id.btn_get_content);
        tvContent = findViewById(R.id.tv_show_text);
        tvContentDetails = findViewById(R.id.tv_look_details);

        sp = getSharedPreferences("topic_sp",MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_insert_stock:
                Intent intent = new Intent(this, StockListActivity.class);
                startActivityForResult(intent,REQUEST_USER_APPEND);
                break;
            case R.id.ll_insert_topic:
                Intent intent2 = new Intent(this, TopicListActivity.class);
                startActivityForResult(intent2,REQUEST_TAG_APPEND);
                break;
            case R.id.btn_save:
                DynamicSaveResult dynamicSaveResult = mentionEditText.getFormatCharSequence();
                try {
                    saveObject("dynamic_save_text",dynamicSaveResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_show_save:
                DynamicSaveResult dynamicSaveResult1 = (DynamicSaveResult) getObject("dynamic_save_text");
                mentionEditText.setSaveText(dynamicSaveResult1);
                break;
            case R.id.btn_get_content:
                if (TextUtils.isEmpty(mentionEditText.getText())) {
                    return;
                }
                tvContent.setMaxLines(4);
               final DynamicPostResult dynamicPostResult = mentionEditText.getFormatTarget();
                DynamicTextShowUtils.setDynamicText(this, tvContent, tvContentDetails, dynamicPostResult.text.toString(), dynamicPostResult.list, false, new DynamicTextShowUtils.DetailsClickListener() {
                    @Override
                    public void onDetailsClick() {
                        tvContent.setMaxLines(100000);
                        DynamicTextShowUtils.setDynamicText(MainActivity.this, tvContent, tvContentDetails, dynamicPostResult.text.toString(), dynamicPostResult.list, false, new DynamicTextShowUtils.DetailsClickListener() {
                            @Override
                            public void onDetailsClick() {

                            }
                        },new Random().nextInt(10)+"",true);
                    }
                },new Random().nextInt(10)+"",false);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data) {
            switch (requestCode) {
                case REQUEST_USER_APPEND:
                    StockSearchResult stockSearchResult = (StockSearchResult) data.getSerializableExtra(StockListActivity.RESULT_TAG);
                    mentionEditText.insert(this,stockSearchResult,0,stockSearchResult.code);
                    break;
                case REQUEST_TAG_APPEND:
                    TopicSearchResult topicSearchResult = (TopicSearchResult) data.getSerializableExtra(TopicListActivity.RESULT_TAG);
                    mentionEditText.insert(this,topicSearchResult,1,topicSearchResult.id);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /** 保存对象 需要实现Serializable
     * @param o
     */
    public void saveObject(String key,Object o) throws Exception {
        if(o instanceof Serializable) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(o);//把对象写到流里
                String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                sp.edit().putString(key, temp).commit();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new Exception("User must implements Serializable");
        }
    }

    /**
     * 获取保存的对象
     * @param key
     * @return
     */
    public Object getObject(String key) {
        String temp = sp.getString(key, "");
        ByteArrayInputStream bais =  new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        Object o = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            o  =  ois.readObject();
        } catch (IOException e) {
        }catch(ClassNotFoundException e1) {

        }
        return o;
    }
}
