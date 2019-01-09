package com.example.zhangbo81.mentionedittext.bean;


import com.example.zhangbo81.mentionedittext.listener.InsertData;

import java.io.Serializable;

/**
 * Created by zhangbo at 2018/11/13
 */
public class StockSearchResult implements Serializable,InsertData {
  /**
   * 股票名称
   */
  public String na;
  /**
   * 股票代码
   */
  public String code;
  /**
   * 股票详细类型（A股，B股，指数，基金）跳转必传
   */
  public String ast;
  /**
   * 所属市场（SH,SZ,US,HK）
   */
  public String m;

  public StockSearchResult() {
  }

  public StockSearchResult(String ast, String na, String code) {
    this.na = na;
    this.code = code;
    this.ast = ast;
  }


  public CharSequence getNa() {
    return na;
  }
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    StockSearchResult user = (StockSearchResult) o;

    if (m != null ? !m.equals(user.m) : user.m != null) return false;
    if (na != null ? !na.equals(user.na) : user.na != null) return false;
    return code != null ? code.equals(user.code) : user.code == null;
  }

  @Override
  public int hashCode() {
    int result = m != null ? m.hashCode() : 0;
    result = 31 * result + (na != null ? na.hashCode() : 0);
    result = 31 * result + (code != null ? code.hashCode() : 0);
    return result;
  }

  @Override
  public CharSequence charSequence() {
    return "$"+ na +"("+ code +")$";
  }

  @Override
  public FormatRange.Convert formatData() {
    return new StockSearchResultConvert(this);
  }

  @Override
  public int color() {
    return 0xff4576D6;
  }

  private class StockSearchResultConvert implements FormatRange.Convert {
    private StockSearchResult stockSearchResult;

    public StockSearchResultConvert(StockSearchResult stockSearchResult) {
      this.stockSearchResult = stockSearchResult;
    }

    @Override
    public CharSequence formatCharSequence(Range range) {
      return "{\"from\":\""+range.getFrom()+"\",\"to\":\""+range.getTo()+"\",\"dataType\":\""+stockSearchResult.ast+"\",\"type\":"+"0"+",\"text\":\""+stockSearchResult.na+"("+stockSearchResult.code+")"+"\",\"name\":\""+stockSearchResult.na +"\",\"data\":\""+stockSearchResult.code +"\"}";
    }

     public LableJson formatCharSequenceToTarget(Range range) {
       LableJson target = new LableJson();
        target.setFrom(range.getFrom());
        target.setTo(range.getTo());
        target.text = stockSearchResult.charSequence().toString();
        target.type = 0;
        target.data =stockSearchResult.code;
        target.dataType =stockSearchResult.ast;
        return target;
    }
  }
}
