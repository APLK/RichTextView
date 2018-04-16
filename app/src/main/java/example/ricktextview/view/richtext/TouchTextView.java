package example.ricktextview.view.richtext;


import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TouchTextView implements View.OnTouchListener {
    Spannable spannable;

    public TouchTextView(Spannable spannable) {
        this.spannable = spannable;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (!(v instanceof TextView)) {
            return false;
        }
        TextView textView = (TextView) v;
        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);
            int showCount =textView.getText().length()- textView.getLayout().getEllipsisCount(textView.getLineCount() - 1);
            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP && spannable.getSpanEnd(link[0]) <= showCount) {
                    link[0].onClick(textView);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(spannable,
                            spannable.getSpanStart(link[0]),
                            spannable.getSpanEnd(link[0]));
                }

                return true;
            } else {
                Selection.removeSelection(spannable);
            }
        }

        return false;
    }
}
