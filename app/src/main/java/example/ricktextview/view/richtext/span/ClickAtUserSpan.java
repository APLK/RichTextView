package example.ricktextview.view.richtext.span;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import example.ricktextview.view.richtext.UserModel;
import example.ricktextview.view.richtext.listener.SpanAtUserCallBack;


/**
 * 显示可点击的@某人
 */
public class ClickAtUserSpan extends ClickableSpan {

    private Context context;
    private UserModel userModel;
    private SpanAtUserCallBack spanClickCallBack;
    private int color;

    public ClickAtUserSpan(Context context, UserModel userModel, int color, SpanAtUserCallBack spanClickCallBack) {
        this.context = context;
        this.userModel = userModel;
        this.spanClickCallBack = spanClickCallBack;
        this.color = color;
    }

    @Override
    public void onClick(View view) {
        if (spanClickCallBack != null) {
            spanClickCallBack.onClick(view, userModel);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        /** 给文字染色 **/
        ds.setColor(color);
        /** 去掉下划线 ， 默认自带下划线 **/
        ds.setUnderlineText(false);
    }

}