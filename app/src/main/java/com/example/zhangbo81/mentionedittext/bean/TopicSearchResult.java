package com.example.zhangbo81.mentionedittext.bean;


import com.example.zhangbo81.mentionedittext.listener.InsertData;

import java.io.Serializable;

/**
 * Created by zhangbo at 2018/11/14
 */
public class TopicSearchResult implements Serializable,InsertData {

  public String id;
  public String title;
  public String summary;
  public String topicTag;

  public TopicSearchResult() {
  }

  public TopicSearchResult(String topicId, String topicTag) {
    this.id = topicId;
    this.topicTag = topicTag;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }
  public String getTopicTag() {
    return topicTag;
  }
  public String getSummary() {
    return summary;
  }

  public void setSummary(String stockCode) {
    this.summary = stockCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TopicSearchResult user = (TopicSearchResult) o;

    if (id != null ? !id.equals(user.id) : user.id != null) return false;
    if (title != null ? !title.equals(user.title) : user.title != null) return false;
    if (topicTag != null ? !topicTag.equals(user.topicTag) : user.topicTag != null) return false;
    return summary != null ? summary.equals(user.summary) : user.summary == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (topicTag != null ? topicTag.hashCode() : 0);
    result = 31 * result + (summary != null ? summary.hashCode() : 0);
    return result;
  }

  @Override
  public CharSequence charSequence() {
    return topicTag.startsWith("#") && topicTag.endsWith("#") ? topicTag : "#" + topicTag+ "#" ;
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
    private TopicSearchResult topicSearchResult;

    public StockSearchResultConvert(TopicSearchResult stockSearchResult) {
      this.topicSearchResult = stockSearchResult;
    }

    @Override
    public CharSequence formatCharSequence(Range range) {
      return "{\"from\":\""+range.getFrom()+"\",\"to\":\""+range.getTo()+"\",\"data\":\""+ topicSearchResult.id
              +"\",\"type\":\""+"1"+"\",\"text\":\""+topicSearchResult.topicTag+"\",\"name\":\""+ topicSearchResult.topicTag +"\"}";
    }

    public LableJson formatCharSequenceToTarget(Range range) {
      LableJson target = new LableJson();
      target.setFrom(range.getFrom());
      target.setTo(range.getTo());
      target.text = topicSearchResult.charSequence().toString();
      target.type = 1;
      target.data =topicSearchResult.id;
      return target;
    }
  }
}
