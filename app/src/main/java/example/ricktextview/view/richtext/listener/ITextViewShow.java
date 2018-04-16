package example.ricktextview.view.richtext.listener;

import android.content.Context;
import android.text.method.MovementMethod;

import example.ricktextview.view.richtext.TopicModel;
import example.ricktextview.view.richtext.UserModel;
import example.ricktextview.view.richtext.span.ClickAtUserSpan;
import example.ricktextview.view.richtext.span.ClickTopicSpan;
import example.ricktextview.view.richtext.span.LinkSpan;


/**
 * textview 显示接口
 */

public interface ITextViewShow {
    void setText(CharSequence charSequence);

    CharSequence getText();

    void setMovementMethod(MovementMethod movementMethod);

    void setAutoLinkMask(int flag);

    ClickAtUserSpan getCustomClickAtUserSpan(Context context, UserModel userModel, int color, SpanAtUserCallBack spanClickCallBack);

    ClickTopicSpan getCustomClickTopicSpan(Context context, TopicModel topicModel, int color, SpanTopicCallBack spanTopicCallBack);

    LinkSpan getCustomLinkSpan(Context context, String url, int color, SpanUrlCallBack spanUrlCallBack);

    int emojiSize();

    int verticalAlignment();
}
