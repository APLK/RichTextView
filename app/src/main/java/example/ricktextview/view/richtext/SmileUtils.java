package example.ricktextview.view.richtext;

/**
 * 表情工具类
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.ricktextview.R;
import example.ricktextview.view.richtext.span.CenteredImageSpan;

import static android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM;
public class SmileUtils {

    private static final Spannable.Factory spannableFactory = Spannable.Factory
            .getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

    private static final List<String> textList = new ArrayList<>();

    public Map<Pattern, Integer> getEmotions() {
        return emoticons;
    }

    public static final String f1 = "[汗]";
    public static final String f2 = "[发呆]";
    public static final String f3 = "[眨眼]";
    public static final String f4 = "[痛哭]";
    public static final String f5 = "[傻萌]";
    public static final String f6 = "[惊恐]";
    public static final String f7 = "[流口水]";
    public static final String f8 = "[冷漠]";
    public static final String f9 = "[乖巧]";
    public static final String f10 = "[可爱]";
    public static final String f11 = "[抓狂]";
    public static final String f12 = "[邪恶]";
    public static final String f13 = "[笑哭]";
    public static final String f14 = "[得意]";
    public static final String f15 = "[抽烟]";
    public static final String f16 = "[示爱]";
    public static final String f17 = "[输入]";
    public static final String f18 = "[无语]";
    public static final String f19 = "[大眼崽]";
    public static final String f20 = "[微笑]";
    public static final String f21 = "[财迷]";
    public static final String f22 = "[开心]";
    public static final String f23 = "[糗大了]";
    public static final String f24 = "[感冒]";
    public static final String f25 = "[调皮]";
    public static final String f26 = "[尴尬]";
    public static final String f27 = "[色迷迷]";
    public static final String f28 = "[大笑]";
    public static final String f29 = "[笑眯眯]";
    public static final String f30 = "[爱你哟]";
    public static final String f31 = "[纠结]";
    public static final String f32 = "[惊呆]";
    public static final String f33 = "[思考]";
    public static final String f34 = "[拳头]";
    public static final String f35 = "[炸弹]";
    public static final String f36 = "[公平]";
    public static final String f37 = "[手]";
    public static final String f38 = "[朋友]";
    public static final String f39 = "[手牵手]";
    public static final String f40 = "[美眉]";
    public static final String f41 = "[合照]";
    public static final String f42 = "[全家福]";
    public static final String f43 = "[财迷]";
    public static final String f44 = "[臭气熏天]";
    public static final String f45 = "[胜利]";
    public static final String f46 = "[保密]";
    public static final String f47 = "[语音]";
    public static final String f48 = "[信封]";
    public static final String f49 = "[手机]";
    public static final String f50 = "[答卷]";
    public static final String f51 = "[合影]";
    public static final String f52 = "[电脑]";
    public static final String f53 = "[日历]";
    public static final String f54 = "[电话]";
    public static final String f55 = "[情书]";
    public static final String f56 = "[可怜]";
    public static final String f57 = "[睡觉]";
    public static final String f58 = "[大骂]";
    public static final String f59 = "[害羞]";
    public static final String f60 = "[撇嘴]";
    public static final String f61 = "[闭嘴]";

    static {
        addPattern(emoticons, addResString(f1), R.drawable.f1);
        addPattern(emoticons, addResString(f2), R.drawable.f2);
        addPattern(emoticons, addResString(f3), R.drawable.f3);
        addPattern(emoticons, addResString(f4), R.drawable.f4);
        addPattern(emoticons, addResString(f5), R.drawable.f5);
        addPattern(emoticons, addResString(f6), R.drawable.f6);
        addPattern(emoticons, addResString(f7), R.drawable.f7);
        addPattern(emoticons, addResString(f8), R.drawable.f8);
        addPattern(emoticons, addResString(f9), R.drawable.f9);
        addPattern(emoticons, addResString(f10), R.drawable.f10);
        addPattern(emoticons, addResString(f11), R.drawable.f11);
        addPattern(emoticons, addResString(f12), R.drawable.f12);
        addPattern(emoticons, addResString(f13), R.drawable.f13);
        addPattern(emoticons, addResString(f14), R.drawable.f14);
        addPattern(emoticons, addResString(f15), R.drawable.f15);
        addPattern(emoticons, addResString(f16), R.drawable.f16);
        addPattern(emoticons, addResString(f17), R.drawable.f17);
        addPattern(emoticons, addResString(f18), R.drawable.f18);
        addPattern(emoticons, addResString(f19), R.drawable.f19);
        addPattern(emoticons, addResString(f20), R.drawable.f20);
        addPattern(emoticons, addResString(f21), R.drawable.f21);
        addPattern(emoticons, addResString(f22), R.drawable.f22);
        addPattern(emoticons, addResString(f23), R.drawable.f23);
        addPattern(emoticons, addResString(f24), R.drawable.f24);
        addPattern(emoticons, addResString(f25), R.drawable.f25);
        addPattern(emoticons, addResString(f26), R.drawable.f26);
        addPattern(emoticons, addResString(f27), R.drawable.f27);
        addPattern(emoticons, addResString(f28), R.drawable.f28);
        addPattern(emoticons, addResString(f29), R.drawable.f29);
        addPattern(emoticons, addResString(f30), R.drawable.f30);
        addPattern(emoticons, addResString(f31), R.drawable.f31);
        addPattern(emoticons, addResString(f32), R.drawable.f32);
        addPattern(emoticons, addResString(f33), R.drawable.f33);
        addPattern(emoticons, addResString(f34), R.drawable.f34);
        addPattern(emoticons, addResString(f35), R.drawable.f35);
        addPattern(emoticons, addResString(f36), R.drawable.f36);
        addPattern(emoticons, addResString(f37), R.drawable.f37);
        addPattern(emoticons, addResString(f38), R.drawable.f38);
        addPattern(emoticons, addResString(f39), R.drawable.f39);
        addPattern(emoticons, addResString(f40), R.drawable.f40);
        addPattern(emoticons, addResString(f41), R.drawable.f41);
        addPattern(emoticons, addResString(f42), R.drawable.f42);
        addPattern(emoticons, addResString(f43), R.drawable.f43);
        addPattern(emoticons, addResString(f44), R.drawable.f44);
        addPattern(emoticons, addResString(f45), R.drawable.f45);
        addPattern(emoticons, addResString(f46), R.drawable.f46);
        addPattern(emoticons, addResString(f47), R.drawable.f47);
        addPattern(emoticons, addResString(f48), R.drawable.f48);
        addPattern(emoticons, addResString(f49), R.drawable.f49);
        addPattern(emoticons, addResString(f50), R.drawable.f50);
        addPattern(emoticons, addResString(f51), R.drawable.f51);
        addPattern(emoticons, addResString(f52), R.drawable.f52);
        addPattern(emoticons, addResString(f53), R.drawable.f53);
        addPattern(emoticons, addResString(f54), R.drawable.f54);
        addPattern(emoticons, addResString(f55), R.drawable.f55);
        addPattern(emoticons, addResString(f56), R.drawable.f56);
        addPattern(emoticons, addResString(f57), R.drawable.f57);
        addPattern(emoticons, addResString(f58), R.drawable.f58);
        addPattern(emoticons, addResString(f59), R.drawable.f59);
        addPattern(emoticons, addResString(f60), R.drawable.f60);
        addPattern(emoticons, addResString(f61), R.drawable.f61);
    }

    /**
     * 添加到map
     *
     * @param map      map
     * @param smile    文本
     * @param resource 显示图片表情列表
     */
    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * 使用你自己的map
     *
     * @param map      map
     * @param smile    文本列表
     * @param resource 显示图片表情列表
     */
    public static void addPatternAll(Map<Pattern, Integer> map, List<String> smile,
                                     List<Integer> resource) {

        map.clear();
        textList.clear();
        if (smile.size() != resource.size()) {
            try {
                throw new Exception("**********文本与图片list不相等");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        textList.addAll(smile);
        for (int i = 0; i < smile.size(); i++) {
            map.put(Pattern.compile(Pattern.quote(smile.get(i))), resource.get(i));
        }
    }

    public static String addResString(String resString) {
         textList.add(resString);
        return resString;
    }

    /***
     * 文本对应的资源
     *
     * @param string 需要转化文本
     * @return
     */
    public static int getRedId(String string) {
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(string);
            while (matcher.find()) {
                return entry.getValue();
            }
        }
        return -1;
    }

    /**
     * 文本转化表情处理
     *
     * @param editText  要显示的EditText
     * @param maxLength 最长高度
     * @param size      显示大小
     * @param name      需要转化的文本
     */
    public static void insertIcon(EditText editText, int maxLength, int size, String name) {

        String curString = editText.toString();
        if ((curString.length() + name.length()) > maxLength) {
            return;
        }

        int resId = SmileUtils.getRedId(name);

        Drawable drawable = editText.getResources().getDrawable(resId);
        if (drawable == null)
            return;

        drawable.setBounds(0, 0, size, size);//这里设置图片的大小
        CenteredImageSpan CenteredImageSpan = new CenteredImageSpan(drawable);
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(CenteredImageSpan, 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);


        int index = Math.max(editText.getSelectionStart(), 0);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getText());
        spannableStringBuilder.insert(index, spannableString);

        editText.setText(spannableStringBuilder);
        editText.setSelection(index + spannableString.length());
    }


    /**
     * replace existing spannable with smiles
     *
     * @param context   上下文
     * @param spannable 显示的span
     * @return 是否添加
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        return addSmiles(context, -1, spannable);
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context   上下文
     * @param size      大小
     * @param spannable 显示的span
     * @return 是否添加
     */
    public static boolean addSmiles(Context context, int size, Spannable spannable) {
        return addSmiles(context, size, ALIGN_BOTTOM, spannable);
    }


    /**
     * replace existing spannable with smiles
     *
     * @param context           上下文
     * @param size              大小
     * @param spannable         显示的span
     * @param verticalAlignment 垂直方向
     * @return 是否添加
     */
    public static boolean addSmiles(Context context, int size, int verticalAlignment, Spannable spannable) {
        boolean hasChanges = false;
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (CenteredImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), CenteredImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    if (size <= 0) {
                        spannable.setSpan(new CenteredImageSpan(context, entry.getValue()),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        Drawable drawable = context.getResources().getDrawable(entry.getValue());
                        if (drawable != null) {
                            drawable.setBounds(0, 0, size, size);//这里设置图片的大小
                            CenteredImageSpan CenteredImageSpan = new CenteredImageSpan(drawable);
                            spannable.setSpan(CenteredImageSpan,
                                    matcher.start(), matcher.end(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }
        return hasChanges;
    }


    public static Spannable getSmiledText(Context context, CharSequence text) {
        return getSmiledText(context, text, -1);
    }

    public static Spannable getSmiledText(Context context, CharSequence text, int size) {
        return getSmiledText(context, text, size, ALIGN_BOTTOM);
    }

    public static Spannable getSmiledText(Context context, CharSequence text, int size, int verticalAlignment) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, size, verticalAlignment, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static Map<Pattern, Integer> getEmoticons() {
        return emoticons;
    }

    public static String stringToUnicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + String.format("%04", Integer.toHexString(c)));
        }

        return "[" + unicode.toString() + "]";
    }

    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    public static String[] specials = {"\\", "\\/", "*", ".", "?", "+", "$",
            "^", "[", "]", "(", ")", "{", "}", "|"};

    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        for (int i = 0; i < specials.length; i++) {
            if (target.contains(specials[i])) {
                target = target.replace(specials[i], "\\" + specials[i]);
            }
        }
        Pattern p = Pattern.compile(target.toLowerCase());
        Matcher m = p.matcher(text.toLowerCase());
        while (m.find()) {
            span = new ForegroundColorSpan(Color.rgb(253, 113, 34));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static SpannableStringBuilder highlight(Spannable text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.rgb(253, 113, 34));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static SpannableStringBuilder highlight(String text) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        span = new ForegroundColorSpan(Color.rgb(253, 113, 34));// 需要重复！
        spannable.setSpan(span, 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable unicodeToEmojiName(Context context, String content, int size, int verticalAlignment) {
        Spannable spannable = getSmiledText(context, content, size, verticalAlignment);
        return spannable;
    }

    public static Spannable unicodeToEmojiName(Context context, String content, int size) {
        Spannable spannable = getSmiledText(context, content, size);
        return spannable;
    }

    public static Spannable unicodeToEmojiName(Context context, String content) {
        Spannable spannable = getSmiledText(context, content, -1);
        return spannable;
    }

    public static List<String> getTextList() {
        return textList;
    }
}
