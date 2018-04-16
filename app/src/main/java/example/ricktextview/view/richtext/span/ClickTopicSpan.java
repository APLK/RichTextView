package example.ricktextview.view.richtext.span;

import android.content.Context;
import android.view.View;

import example.ricktextview.view.richtext.TopicModel;
import example.ricktextview.view.richtext.listener.SpanTopicCallBack;


/**
 * 话题#点击回调
 */

public class ClickTopicSpan extends ClickAtUserSpan {


    private TopicModel topicModel;
    private SpanTopicCallBack spanTopicCallBack;

    public ClickTopicSpan(Context context, TopicModel topicModel, int color, SpanTopicCallBack spanTopicCallBack) {
        super(context, null, color, null);
        this.topicModel = topicModel;
        this.spanTopicCallBack = spanTopicCallBack;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (spanTopicCallBack != null) {
            spanTopicCallBack.onClick(view, topicModel);
        }
    }
}
