package example.ricktextview.view.richtext.listener;

import android.view.View;

/**
 * url被点击的回掉
 */

public interface SpanUrlCallBack {
    void phone(View view, String phone);

    void url(View view, String url);
}
