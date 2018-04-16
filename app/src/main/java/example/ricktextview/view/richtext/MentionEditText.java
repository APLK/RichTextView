/*
 * Copyright 2016 Andy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example.ricktextview.view.richtext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.ricktextview.view.richtext.span.ClickAtUserSpan;
import example.ricktextview.view.richtext.span.ClickTopicSpan;


/**
 * <p>
 * MentionEditText adds some useful features for mention string(@xxxx), such as highlight,
 * intelligent deletion, intelligent selection and '@' input detection, etc.
 */
class MentionEditText extends AppCompatEditText {
    //public static final String DEFAULT_MENTION_PATTERN = "@[\\u4e00-\\u9fa5\\w\\-]+";

    //    public static final String DEFAULT_MENTION_PATTERN = "@[^(?!@)\\s]+?\\u0008";
    //    public static final String TOPIC_MENTION_PATTERN = "#[^^(?!#)\\s]+?#\\u0008";
    //
    //    protected Pattern mPattern;
    //    protected Pattern mTopicPattern;

    private Range mLastSelectedRange;
    protected List<Range> mRangeArrayList;

    private OnDeleteListener mOnDeleteListener;

    private boolean mIsSelected;
    private List<TopicModel> topicList;
    private List<UserModel> nameList;

    public MentionEditText(Context context) {
        super(context);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new HackInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        colorMentionString();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        Log.i("1", "onSelectionChanged=" + selStart + ",selend=" + selEnd);
        //        if (mLastSelectedRange != null) {
        //            LogUtil.i("1", "mLastSelectedRange=" + mLastSelectedRange.from + ",selend=" + mLastSelectedRange.to);
        //        }
        //avoid infinite recursion after calling setSelection()
        if (mLastSelectedRange != null && mLastSelectedRange.isEqual(selStart, selEnd)) {
            return;
        }

        //if user cancel a selection of mention string, reset the state of 'mIsSelected'
        Range closestRange = getRangeOfClosestMentionString(selStart, selEnd);
        if (closestRange != null) {
            Log.i("1", "closestRange=" + closestRange.from + ",selend=" + closestRange.to);
        }
        if (closestRange != null && closestRange.to == selEnd) {
            mIsSelected = false;
        }

        Range nearbyRange = getRangeOfNearbyMentionString(selStart, selEnd);
        //if there is no mention string nearby the cursor, just skip
        if (nearbyRange == null) {
//            Log.i("1", "nearbyRange=");
            return;
        }
//        Log.i("1", "onSelectionChanged1=" + nearbyRange.from + ",selend=" + nearbyRange.to);
        //forbid cursor located in the mention string.
        if (selStart == selEnd) {
            setSelection(nearbyRange.getAnchorPosition(selStart));
        } else {
            if (selEnd < nearbyRange.to) {
                setSelection(selStart, nearbyRange.to);
            }
            if (selStart > nearbyRange.from) {
                setSelection(nearbyRange.from, selEnd);
            }
        }
    }

    private void init() {
        mRangeArrayList = new ArrayList<>();

        //        mPattern = Pattern.compile(DEFAULT_MENTION_PATTERN);
        //        mTopicPattern = Pattern.compile(TOPIC_MENTION_PATTERN);
        //disable suggestion
        //setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        //addTextChangedListener(new MentionTextWatcher());
    }

    private void colorMentionString() {
        if (this instanceof RichEditText) {
            nameList = ((RichEditText) this).getNameList();
            topicList = ((RichEditText) this).getTopicList();
        }
        //reset state
        mIsSelected = false;
        if (mRangeArrayList != null) {
            mRangeArrayList.clear();
        }

        Editable spannableText = getText();
        if (spannableText == null || TextUtils.isEmpty(spannableText.toString())) {
            return;
        }

        CharSequence charSequence = getText();
        int ends = charSequence.length();
        Spannable sp = getText();

        if (sp instanceof SpannableStringBuilder) {
            //find mention string and color it
            String text = spannableText.toString();
            if (nameList != null) {
                for (int i = 0; i < nameList.size(); i++) {
                    int lastMentionIndex = -1;
                    // 设置正则
                    Pattern pattern = Pattern.compile(nameList.get(i).getUser_name());
                    Matcher matcher = pattern.matcher(text);
                    while (matcher.find()) {
                        String mentionText = matcher.group();
                        //                        LogUtil.i("1", "nameList1=" + mentionText);
                        int start;
                        if (lastMentionIndex != -1) {
                            start = text.indexOf(mentionText, lastMentionIndex);
                        } else {
                            start = text.indexOf(mentionText);
                        }
                        int end = start + mentionText.length();
                        lastMentionIndex = end;
                        //record all mention-string's position
                        mRangeArrayList.add(new Range(start, end));
                    }
                }
            }
            if (topicList != null) {
                //                LogUtil.i("1", "topicList=" + topicList.size());
                for (int i = 0; i < topicList.size(); i++) {
                    int lastMentionIndex = -1;
                    // 设置正则
                    Pattern pattern = Pattern.compile(topicList.get(i).getTopicName());
                    Matcher matcher = pattern.matcher(text);
                    while (matcher.find()) {
                        String mentionText = matcher.group();
                        //                    LogUtil.i("1", "topicList1=" + mentionText);
                        int start;
                        if (lastMentionIndex != -1) {
                            start = text.indexOf(mentionText, lastMentionIndex);
                        } else {
                            start = text.indexOf(mentionText);
                        }
                        int end = start + mentionText.length();
                        lastMentionIndex = end;
                        //record all mention-string's position
                        //                        LogUtil.i("1", "mRangeArrayList2=" + mRangeArrayList.size() + ",start=" + start + ",end=" + end);
                        mRangeArrayList.add(new Range(start, end));
                    }
                }
            }
        } else {
            ClickTopicSpan[] topicSpan = sp.getSpans(0, ends, ClickTopicSpan.class);
            for (ClickTopicSpan clickTopicSpan : topicSpan) {
                mRangeArrayList.add(new Range(sp.getSpanStart(clickTopicSpan), sp.getSpanEnd(clickTopicSpan)));
            }
            ClickAtUserSpan[] atSpan = sp.getSpans(0, ends, ClickAtUserSpan.class);
            for (ClickAtUserSpan clickATSpan : atSpan) {
                mRangeArrayList.add(new Range(sp.getSpanStart(clickATSpan), sp.getSpanEnd(clickATSpan)));
            }
        }
        //find mention string and color it
        if (mRangeArrayList != null && mRangeArrayList.size() > 0)
            Collections.sort(mRangeArrayList);
    }

    protected Range getRangeOfClosestMentionString(int selStart, int selEnd) {
        if (mRangeArrayList == null) {
            return null;
        }
        //        LogUtil.i("1", "getRangeOfClosestMentionString=" + mRangeArrayList.size());
        for (Range range : mRangeArrayList) {
            //            LogUtil.i("1", "getRangeOfClosestMentionString1=" + range.from + "  " + range.to);
            if (range.contains(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }

    private Range getRangeOfNearbyMentionString(int selStart, int selEnd) {
        if (mRangeArrayList == null) {
            return null;
        }
        //        LogUtil.i("1", "getRangeOfNearbyMentionString=" + mRangeArrayList.size());
        for (Range range : mRangeArrayList) {
            //            LogUtil.i("1", "getRangeOfNearbyMentionString=" + range.from + "  " + range.to);
            if (range.isWrappedBy(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }

    //handle the deletion action for mention string, such as '@test'
    private class HackInputConnection extends InputConnectionWrapper {
        private EditText editText;

        public HackInputConnection(InputConnection target, boolean mutable, MentionEditText editText) {
            super(target, mutable);
            this.editText = editText;
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            //            LogUtil.i("1", "sendKeyEvent00=");
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                Range closestRange = getRangeOfClosestMentionString(selectionStart, selectionEnd);
                if (closestRange == null) {
                    mIsSelected = false;
                    //                    LogUtil.i("1", "sendKeyEvent10=" + mIsSelected + ",selectionStart=" + selectionStart + ",selectionEnd=" + selectionEnd);
                    return super.sendKeyEvent(event);
                }
                //if mention string has been selected or the cursor is at the beginning of mention string, just use default action(delete)
                //                LogUtil.i("1", "sendKeyEvent1=" + closestRange.from + ",selectionEnd=" + closestRange.to
                //                        + "selectionStart=" + selectionStart + ",mIsSelected=" + mIsSelected + ",selectionEnd=" + selectionEnd);
                if (mIsSelected || selectionStart == closestRange.from) {
                    mIsSelected = false;
                    return super.sendKeyEvent(event);
                } else {
                    //select the mention string
                    mIsSelected = true;
                    mLastSelectedRange = closestRange;
                    setSelection(closestRange.to, closestRange.from);
                }
                return true;
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            //            LogUtil.i("1", "deleteSurroundingText=" + beforeLength + ",afterLength=" + afterLength);
            if (mOnDeleteListener != null && mIsSelected && mLastSelectedRange != null) {
                mOnDeleteListener.onDelete();
            }
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            } else if (beforeLength < 0 && afterLength == 0) {
                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                if (selectionStart == selectionEnd) {
                    setSelection(selectionStart - beforeLength, selectionStart - beforeLength);
                    super.deleteSurroundingText(-beforeLength, afterLength);
                }
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    //helper class to record the position of mention string in EditText
    private class Range implements Comparable<Range> {
        int from;
        int to;

        public Range(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public boolean isWrappedBy(int start, int end) {
            return (start > from && start < to) || (end > from && end < to);
        }

        public boolean contains(int start, int end) {
            return from <= start && to >= end;
        }

        public boolean isEqual(int start, int end) {
            return (from == start && to == end) || (from == end && to == start);
        }

        public int getAnchorPosition(int value) {
            if ((value - from) - (to - value) >= 0) {
                return to;
            } else {
                return from;
            }
        }

        @Override
        public int compareTo(@NonNull Range another) {
            return this.from - another.from;
        }

    }

    /**
     * Listener for '@' character
     */
    public interface OnDeleteListener {
        /**
         * call when '@' character is inserted into EditText
         */
        void onDelete();
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mOnDeleteListener = listener;
    }

}
