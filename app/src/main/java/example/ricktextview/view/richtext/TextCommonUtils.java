package example.ricktextview.view.richtext;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.ricktextview.view.richtext.listener.ITextViewShow;
import example.ricktextview.view.richtext.listener.SpanAtUserCallBack;
import example.ricktextview.view.richtext.listener.SpanTopicCallBack;
import example.ricktextview.view.richtext.listener.SpanUrlCallBack;
import example.ricktextview.view.richtext.span.ClickAtUserSpan;
import example.ricktextview.view.richtext.span.ClickTopicSpan;
import example.ricktextview.view.richtext.span.LinkSpan;


/**
 * 通用处理问题的Utisl
 */

public class TextCommonUtils {

    /**
     * 单纯emoji表示
     *
     * @param context 上下文
     * @param text    包含emoji的字符串
     * @param tv      显示的textview
     */
    public static void setEmojiText(Context context, String text, ITextViewShow tv) {
        if (TextUtils.isEmpty(text)) {
            tv.setText("");
        }
        Spannable spannable = SmileUtils.unicodeToEmojiName(context, text);
        tv.setText(spannable);
    }

    /**
     * 单纯获取emoji表示
     *
     * @param context 上下文
     * @param text    需要处理的文本
     * @param size    emoji大小
     * @return 返回显示的spananle
     */
    public static Spannable getEmojiText(Context context, String text, int size) {
        return getEmojiText(context, text, size, DynamicDrawableSpan.ALIGN_BOTTOM);

    }


    /**
     * 单纯获取emoji表示
     *
     * @param context           上下文
     * @param text              需要处理的文本
     * @param size              emoji大小
     * @param verticalAlignment 垂直方式
     * @return 返回显示的spananle
     */
    public static Spannable getEmojiText(Context context, String text, int size, int verticalAlignment) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        return SmileUtils.unicodeToEmojiName(context, text, size, verticalAlignment);

    }


    /**
     * 单纯获取emoji表示
     *
     * @param context 上下文
     * @param text    需要处理的文本
     * @return 返回显示的spananle
     */
    public static Spannable getEmojiText(Context context, String text) {
        return getEmojiText(context, text, -1);
    }


    /**
     * 显示emoji和url高亮
     *
     * @param context            上下文
     * @param text               需要处理的文本
     * @param textView           需要显示的view
     * @param color              需要显示的颜色
     * @param needNum            是否需要显示号码
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     * @return 返回显示的spananle
     */
    public static Spannable getUrlEmojiText(Context context, String text, ITextViewShow textView, int color, boolean needNum, SpanAtUserCallBack spanAtUserCallBack,  SpanUrlCallBack spanUrlCallBack) {
        if (!TextUtils.isEmpty(text)) {
            return getUrlSmileText(context, text, null,  textView, color, 0, needNum, spanAtUserCallBack, spanUrlCallBack);
        } else {
            return new SpannableString(" ");
        }
    }

    /**
     * 设置带高亮可点击的Url、表情、at某人的textview文本
     *
     * @param context            上下文
     * @param string             需要处理的文本
     * @param listUser           需要显示的AT某人
     * @param textView           需要显示的view
     * @param color              需要显示的颜色
     * @param needNum            是否需要显示号码
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     */
    public static void setUrlSmileText(Context context, String string, List<UserModel> listUser, ITextViewShow textView, int color, boolean needNum, SpanAtUserCallBack spanAtUserCallBack,SpanUrlCallBack spanUrlCallBack) {
        Spannable spannable = getUrlSmileText(context, string, listUser,  textView, color, 0, needNum, spanAtUserCallBack,  spanUrlCallBack);
        textView.setText(spannable);
    }

    /**
     * AT某人的跳转
     *
     * @param context            上下文
     * @param listUser           需要显示的AT某人
     * @param content            需要处理的文本
     * @param textView           需要显示的view
     * @param clickable          AT某人是否可以点击
     * @param color              AT需要显示的颜色
     * @param topicColor         topic需要显示的颜色
     * @param spanAtUserCallBack AT某人点击的返回
     * @return 返回显示的spananle
     */
    public static Spannable getAtText(Context context, List<UserModel> listUser,  List<TopicModel> listTopic, String content, ITextViewShow textView, boolean clickable,
                                      int color, int topicColor, SpanAtUserCallBack spanAtUserCallBack, SpanTopicCallBack spanTopicCallBack) {

        Spannable spannable = null;

        if (listTopic != null && listTopic.size() > 0) {
            spannable = getTopicText(context, listTopic, content, textView, clickable, topicColor, spanTopicCallBack);
        }

        Spannable spannableString = new SpannableString((spannable == null) ? content : spannable);
        if (listUser != null && listUser.size() > 0) {
            for (int i = 0; i < listUser.size(); i++) {
                Pattern pattern = Pattern.compile(listUser.get(i).getUser_name());
                Matcher matcher = pattern.matcher(content);
                while (matcher.find()) {
                    //直接用span会导致后面没文字的时候新输入的一起变色
                    ClickAtUserSpan clickAtUserSpan = null;
                    if (textView != null) {
                        clickAtUserSpan = textView.getCustomClickAtUserSpan(context, listUser.get(i), color, spanAtUserCallBack);
                    }

                    if (clickAtUserSpan == null) {
                        clickAtUserSpan = new ClickAtUserSpan(context, listUser.get(i), color, spanAtUserCallBack);
                    }

                    spannableString.setSpan(clickAtUserSpan, matcher.start(), matcher.end(), Spanned.SPAN_MARK_POINT);

                }
            }
        }
        SmileUtils.addSmiles(context, textView.emojiSize(), textView.verticalAlignment(), spannableString);
        return spannableString;
    }

    /**
     * 话题span
     *
     * @param context           上下文
     * @param listTopic         需要的话题列表
     * @param content           需要处理的文本
     * @param textView          需要显示的view
     * @param clickable         是否可以点击
     * @param color             颜色
     * @param spanTopicCallBack 点击回调
     * @return Spannable
     */
    public static Spannable getTopicText(Context context, List<TopicModel> listTopic, String content, ITextViewShow textView, boolean clickable,
                                         int color, SpanTopicCallBack spanTopicCallBack) {

        if (listTopic == null || listTopic.size() <= 0)
            return new SpannableString(content);
        Spannable spannableString = new SpannableString(content);
        for (int i = 0; i < listTopic.size(); i++) {
            Pattern pattern = Pattern.compile(listTopic.get(i).getTopicName());
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                //直接用span会导致后面没文字的时候新输入的一起变色
                ClickTopicSpan clickTopicSpan = null;
                if (textView != null) {
                    clickTopicSpan = textView.getCustomClickTopicSpan(context, listTopic.get(i), color, spanTopicCallBack);
                }

                if (clickTopicSpan == null) {
                    clickTopicSpan = new ClickTopicSpan(context, listTopic.get(i), color, spanTopicCallBack);
                }

                spannableString.setSpan(clickTopicSpan, matcher.start(), matcher.end(), Spanned.SPAN_MARK_POINT);

            }
        }
        return spannableString;
    }


    /**
     * 设置带高亮可点击的Url、表情的textview文本、AT某人
     *
     * @param context            上下文
     * @param string             需要处理的文本
     * @param listUser           需要显示的AT某人
     * @param textView           需要显示的view
     * @param colorAt            需要显示的颜色
     * @param colorLink          需要显示的颜色
     * @param needNum            是否需要显示号码
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     * @return 返回显示的spananle
     */
    public static Spannable getUrlSmileText(Context context, String string, List<UserModel> listUser,  ITextViewShow textView, int colorAt, int colorLink, boolean needNum,
                                            SpanAtUserCallBack spanAtUserCallBack,  SpanUrlCallBack spanUrlCallBack) {
        return getAllSpanText(context, string, listUser,  null, textView, colorAt, colorLink, 0, needNum, true, spanAtUserCallBack,  spanUrlCallBack, null);
    }

    /**
     * 设置带高亮可点击的Url、表情的textview文本、AT某人、话题
     *
     * @param context            上下文
     * @param string             需要处理的文本
     * @param listUser           需要显示的AT某人
     * @param listTopic          需要的话题列表
     * @param textView           需要显示的view
     * @param colorAt            需要显示的颜色
     * @param colorLink          需要显示的颜色
     * @param colorTopic         需要显示的颜色
     * @param needNum            是否需要显示号码
     * @param needUrl            是否需要显示url
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     * @param spanTopicCallBack  话题点击的返回
     * @return 返回显示的spananle
     */
    public static Spannable getAllSpanText(Context context, String string, List<UserModel> listUser, List<TopicModel> listTopic, ITextViewShow textView, int colorAt, int colorLink, int colorTopic, boolean needNum, boolean needUrl,
                                           SpanAtUserCallBack spanAtUserCallBack, SpanUrlCallBack spanUrlCallBack, SpanTopicCallBack spanTopicCallBack) {
        //        if (needUrl || needNum) {
        //            textView.setAutoLinkMask(Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
        //        }
        if (!TextUtils.isEmpty(string)) {
            //超链接
            string = string.replaceAll("\r", "\r\n");
            if (textView.getText() instanceof SpannedString) {
                SpannedString sp = (SpannedString) textView.getText();
                URLSpan[] urls = sp.getSpans(0, sp.length(), URLSpan.class);
                //                LogUtil.i("1", "url0=" + string+",urls="+urls.length);
                //循环把链接发过去
                for (URLSpan url : urls) {
                    String[] arr = url.getURL().split("-");//1-1,类型-id
                    if (Constant.LinkType.TOPIC == Integer.parseInt(arr[0])) {//话题
                        listTopic.add(new TopicModel(sp.subSequence(sp.getSpanStart(url), sp.getSpanEnd(url)).toString(), Integer.parseInt(arr[1])));
                    } else if (Constant.LinkType.ATUSER == Integer.parseInt(arr[0])) {//好友
                        listUser.add(new UserModel(sp.subSequence(sp.getSpanStart(url), sp.getSpanEnd(url)).toString(), Integer.parseInt(arr[1])));
                    }
                }
            }
            Spannable spannable = getAtText(context, listUser,  listTopic, string, textView, true, colorAt, colorTopic, spanAtUserCallBack,  spanTopicCallBack);
            textView.setText(spannable);
            if (needUrl || needNum) {
                return resolveUrlLogic(context, string, textView, spannable, colorLink, needNum, needUrl, spanUrlCallBack);
            } else {
                return spannable;
            }
        } else {
            return new SpannableString(" ");
        }
    }

    static class LinkSpec {
        String url;
        int start;
        int end;
    }

    private static String makeUrl(String url, String[] prefixes,
                                  Matcher m, Linkify.TransformFilter filter) {
        if (filter != null) {
            url = filter.transformUrl(m, url);
        }

        boolean hasPrefix = false;

        for (int i = 0; i < prefixes.length; i++) {
            if (url.regionMatches(true, 0, prefixes[i], 0,
                    prefixes[i].length())) {
                hasPrefix = true;

                // Fix capitalization if necessary
                if (!url.regionMatches(false, 0, prefixes[i], 0,
                        prefixes[i].length())) {
                    url = prefixes[i] + url.substring(prefixes[i].length());
                }

                break;
            }
        }

        if (!hasPrefix) {
            url = prefixes[0] + url;
        }

        return url;
    }


    /**
     * 处理带URL的逻辑
     *
     * @param context         上下文
     * @param content
     * @param textView        需要显示的view
     * @param spannable       显示的spananle
     * @param color           需要显示的颜色
     * @param needNum         是否需要显示号码
     * @param needUrl         是否需要显示url
     * @param spanUrlCallBack 链接点击的返回       @return 返回显示的spananle
     */
    private static Spannable resolveUrlLogic(Context context, String content, ITextViewShow textView, Spannable spannable, int color, boolean needNum, boolean needUrl, SpanUrlCallBack spanUrlCallBack) {
        Spannable spannableString = new SpannableString(content);
        ClickAtUserSpan[] atSpan = spannable.getSpans(0, textView.getText().length(), ClickAtUserSpan.class);
        ClickTopicSpan[] topicSpan = spannable.getSpans(0, textView.getText().length(), ClickTopicSpan.class);
        ArrayList<LinkSpec> links = new ArrayList<LinkSpec>();
        String[] schemes = {"http://", "https://", "rtsp://"};
        Matcher urlMatcher = Patterns.WEB_URL.matcher(content);
        Matcher telMatcher = Patterns.PHONE.matcher(content);
        while (urlMatcher.find()) {
            int start = urlMatcher.start();
            int end = urlMatcher.end();

            if (Linkify.sUrlMatchFilter == null || Linkify.sUrlMatchFilter.acceptMatch(content, start, end)) {
                LinkSpec spec = new LinkSpec();
                String url = makeUrl(urlMatcher.group(0), schemes, urlMatcher, null);

                spec.url = url;
                spec.start = start;
                spec.end = end;

                links.add(spec);
            }
        }
        while (telMatcher.find()) {
            int start = telMatcher.start();
            int end = telMatcher.end();

            if (Linkify.sPhoneNumberMatchFilter == null || Linkify.sPhoneNumberMatchFilter.acceptMatch(content, start, end)) {
                LinkSpec spec = new LinkSpec();

                spec.url = telMatcher.group();
                spec.start = start;
                spec.end = end;

                links.add(spec);
            }
        }
        for (LinkSpec link : links) {
            LinkSpan linkSpan = null;
            if (textView != null) {
                linkSpan = textView.getCustomLinkSpan(context, link.url, color, spanUrlCallBack);
            }
            if (linkSpan == null) {
                linkSpan = new LinkSpan(context, link.url, color, spanUrlCallBack);
            }
            spannableString.setSpan(linkSpan, link.start, link.end, Spanned.SPAN_MARK_POINT);
        }

        for (ClickAtUserSpan atUserSpan : atSpan) {
            //剔除at某人中的link span
            LinkSpan[] removeUrls = spannableString.getSpans(spannable.getSpanStart(atUserSpan), spannable.getSpanEnd(atUserSpan), LinkSpan.class);
            if (removeUrls != null && removeUrls.length > 0) {
                for (LinkSpan linkSpan : removeUrls) {
                    spannableString.removeSpan(linkSpan);
                }
            }
            spannableString.setSpan(atUserSpan, spannable.getSpanStart(atUserSpan), spannable.getSpanEnd(atUserSpan), Spanned.SPAN_MARK_POINT);
        }
        for (ClickTopicSpan clickTopicSpan : topicSpan) {
            //剔除话题中的link span
            LinkSpan[] removeUrls = spannableString.getSpans(spannable.getSpanStart(clickTopicSpan), spannable.getSpanEnd(clickTopicSpan), LinkSpan.class);
            if (removeUrls != null && removeUrls.length > 0) {
                for (LinkSpan linkSpan : removeUrls) {
                    spannableString.removeSpan(linkSpan);
                }
            }
            spannableString.setSpan(clickTopicSpan, spannable.getSpanStart(clickTopicSpan), spannable.getSpanEnd(clickTopicSpan), Spanned.SPAN_MARK_POINT);
        }
        SmileUtils.addSmiles(context, textView.emojiSize(), textView.verticalAlignment(), spannableString);
        return spannableString;


       /* CharSequence charSequence = textView.getText();
        LogUtil.i("1", "link111=" + charSequence + ",type=" + (charSequence instanceof Spannable));
        if (charSequence instanceof Spannable) {
            int end = charSequence.length();
            Spannable sp = (Spannable) textView.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            LogUtil.i("1", "link11=" + urls.length);
            ClickAtUserSpan[] atSpan = sp.getSpans(0, end, ClickAtUserSpan.class);
            if (urls.length > 0) {
                SpannableStringBuilder style = new SpannableStringBuilder(charSequence);
                style.clearSpans();// should clear old spans
                for (URLSpan url : urls) {
                    String urlString = url.getURL();
                    if (isNumeric(urlString.replace("tel:", ""))) {
                        if (!needNum && !isMobileSimple(urlString.replace("tel:", ""))) {
                            style.setSpan(new StyleSpan(Typeface.NORMAL), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        } else {
                            LinkSpan linkSpan = null;
                            if (textView != null) {
                                linkSpan = textView.getCustomLinkSpan(context, url.getURL(), color, spanUrlCallBack);
                            }
                            if (linkSpan == null) {
                                linkSpan = new LinkSpan(context, url.getURL(), color, spanUrlCallBack);
                            }
                            style.setSpan(linkSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        }
                    } else if (needUrl && isTopURL(urlString.toLowerCase())) {
                        LinkSpan linkSpan = null;
                        if (textView != null) {
                            linkSpan = textView.getCustomLinkSpan(context, url.getURL(), color, spanUrlCallBack);
                        }
                        if (linkSpan == null) {
                            linkSpan = new LinkSpan(context, url.getURL(), color, spanUrlCallBack);
                        }
                        style.setSpan(linkSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    } else {
                        style.setSpan(new StyleSpan(Typeface.NORMAL), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    LogUtil.i("1", "link1=" + sp.getSpanStart(url) + ",end=" + sp.getSpanEnd(url) + ",url=" + url);

                }
                for (ClickAtUserSpan atUserSpan : atSpan) {
                    //剔除话题和at某人中的link span
                    LinkSpan[] removeUrls = style.getSpans(sp.getSpanStart(atUserSpan), sp.getSpanEnd(atUserSpan), LinkSpan.class);
                    if (removeUrls != null && removeUrls.length > 0) {
                        for (LinkSpan linkSpan : removeUrls) {
                            style.removeSpan(linkSpan);
                        }
                    }
                    style.setSpan(atUserSpan, sp.getSpanStart(atUserSpan), sp.getSpanEnd(atUserSpan), Spanned.SPAN_MARK_POINT);
                }
                SmileUtils.addSmiles(context, textView.emojiSize(), textView.verticalAlignment(), style);
                //                textView.setAutoLinkMask(0);
                return style;
            } else {
                return spannable;
            }
        } else {
            return spannable;
        }*/
    }


    /**
     * 顶级域名判断；如果要忽略大小写，可以直接在传入参数的时候toLowerCase()再做判断
     * 处理1. 2. 3.识别成链接的问题
     *
     * @param str
     * @return 是否符合url
     */

    private static boolean isTopURL(String str) {
        String ss[] = str.split("\\.");
        if (ss.length < 3)
            return false;

        return true;

    }

    /**
     * 是否数字
     *
     * @param str
     * @return 是否数字
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * @param string 待验证文本
     * @return 是否符合手机号（简单）格式
     */
    private static boolean isMobileSimple(String string) {
        String phone = "^[1]\\d{10}$";
        return !TextUtils.isEmpty(string) && Pattern.matches(phone, string);
    }


}
