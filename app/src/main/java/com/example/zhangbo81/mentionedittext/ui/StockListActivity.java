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

import com.example.zhangbo81.mentionedittext.bean.StockSearchResult;
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
public class StockListActivity extends AppCompatActivity {
    RecyclerView recycler;

    public static final String RESULT_TAG = "RESULT_TAG";
    private StockAdapter mTagAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler);
        initView();
    }

    private void initView() {
        recycler = findViewById(R.id.recyclerview);
        List<StockSearchResult> stockSearchResults = provideData();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        mTagAdapter = new StockAdapter(this);
        recycler.setAdapter(mTagAdapter);

        mTagAdapter.setmList(stockSearchResults);
    }

    private List<StockSearchResult> provideData() {
        List<StockSearchResult> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            StockSearchResult tag = new StockSearchResult(i+"","股票"+i,i+"");
            result.add(tag);
        }
        return result;
    }

    private class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {
        private List<StockSearchResult> mList;
        private Context mContext;

        public StockAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public List<StockSearchResult> getmList() {
            return mList;
        }

        public void setmList(List<StockSearchResult> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }

        @Override
        public StockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = View.inflate(mContext,R.layout.item_stock_result,null);
            return new StockViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StockViewHolder stockViewHolder, int i) {
            final StockSearchResult stockSearchResult = mList.get(i);
            stockViewHolder.tvName.setText(stockSearchResult.na);
            stockViewHolder.tvCode.setText(stockSearchResult.code);
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

    private class StockViewHolder extends RecyclerView.ViewHolder {

        private  TextView tvName;
        private  TextView tvCode;

        public StockViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_stock_name);
            tvCode = itemView.findViewById(R.id.tv_stock_code);
        }
    }

    private void setResult(StockSearchResult tag) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_TAG, tag);
        setResult(RESULT_OK, intent);
        finish();
    }
}
