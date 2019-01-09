package com.example.zhangbo81.mentionedittext.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhangbo81.mentionedittext.bean.TopicSearchResult;
import com.example.zhangbo81.mentionedittext.R;

import java.util.ArrayList;
import java.util.List;


/**
 * author : zhangbo
 * e-mail : zwill2014@163.com
 * date   : 2019/1/9 15:05
 * desc   :
 * version: 1.0
 */
public class TopicListActivity extends AppCompatActivity {
    RecyclerView recycler;

    public static final String RESULT_TAG = "RESULT_TAG";
    private TopicAdapter mTagAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler);
        initView();
    }

    private void initView() {
        recycler = findViewById(R.id.recyclerview);
        List<TopicSearchResult> stockSearchResults = provideData();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        mTagAdapter = new TopicAdapter(this);
        recycler.setAdapter(mTagAdapter);

        mTagAdapter.setmList(stockSearchResults);
    }

    private List<TopicSearchResult> provideData() {
        List<TopicSearchResult> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            TopicSearchResult TopicSearchResult = new TopicSearchResult(i+"","#话题"+i+"#");
            result.add(TopicSearchResult);
        }
        return result;
    }

    private class TopicAdapter extends RecyclerView.Adapter<TopicViewHolder> {
        private List<TopicSearchResult> mList;
        private Context mContext;

        public TopicAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public List<TopicSearchResult> getmList() {
            return mList;
        }

        public void setmList(List<TopicSearchResult> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }

        @Override
        public TopicViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = View.inflate(mContext,R.layout.item_stock_result,null);
            return new TopicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TopicViewHolder stockViewHolder, int i) {
            final TopicSearchResult stockSearchResult = mList.get(i);
            stockViewHolder.tvName.setText(stockSearchResult.topicTag);
            stockViewHolder.tvCode.setText(stockSearchResult.id);
            stockViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(stockSearchResult);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList != null && mList.size()>0 ? mList.size() : 0;
        }


    }

    private class TopicViewHolder extends RecyclerView.ViewHolder {

        private  TextView tvName;
        private  TextView tvCode;

        public TopicViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_stock_name);
            tvCode = itemView.findViewById(R.id.tv_stock_code);
        }
    }

    private void setResult(TopicSearchResult stockSearchResult) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_TAG, stockSearchResult);
        setResult(RESULT_OK, intent);
        finish();
    }
}
