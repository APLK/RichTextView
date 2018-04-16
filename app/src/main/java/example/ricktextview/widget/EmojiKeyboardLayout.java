package example.ricktextview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import example.ricktextview.ExpressionPagerAdapter;
import example.ricktextview.R;
import example.ricktextview.view.richtext.LockGridView;
import example.ricktextview.view.richtext.RichEditText;
import example.ricktextview.view.richtext.SmileImageExpressionAdapter;
import example.ricktextview.view.richtext.SmileUtils;


public class EmojiKeyboardLayout extends LinearLayout {

    public KeyboardHelper keyboardHelper;
    private LinearLayout mWeiboPublishSelectItem;
    private ViewPager emojiKeyboard;

    private LinearLayout edittextBarViewGroupFace;

    private RichEditText editTextEmoji;
    private List<String> reslist;
    private ImageView[] imageFaceViews;


    private Drawable focusIndicator;
    private Drawable unFocusIndicator;
    private String deleteIconName = "delete_expression";


    private int richMarginBottom;
    private int richMarginTop;

    private int numColumns = 7;

    private int numRows = 5;

    private int pageCount = (numColumns * numRows) - 1;
    private LinearLayout edittextBarLlFaceContainer;


    public EmojiKeyboardLayout(Context context) {
        super(context);
        this.init(context,null);
    }

    public EmojiKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context,attrs);
    }

    public EmojiKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmojiKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context,attrs);
    }



    public LinearLayout getWeiboPublishSelectItem() {
        return mWeiboPublishSelectItem;
    }


    private void init(Context context,AttributeSet attrs) {
        this.setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.publish_button_view, this, true);
        mWeiboPublishSelectItem = (LinearLayout) findViewById(R.id.weibo_publish_select_item);
        emojiKeyboard = (ViewPager) this.findViewById(R.id.emojicons);

        edittextBarViewGroupFace = (LinearLayout) findViewById(R.id.edittext_bar_viewGroup_face);
        edittextBarLlFaceContainer = (LinearLayout) findViewById(R.id.edittext_bar_ll_face_container);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EmojiLayout);
            String deleteIconName = array.getString(R.styleable.EmojiLayout_richDeleteIconName);
            if (!TextUtils.isEmpty(deleteIconName)) {
                this.deleteIconName = deleteIconName;
            }
            focusIndicator = array.getDrawable(R.styleable.EmojiLayout_richIndicatorFocus);
            unFocusIndicator = array.getDrawable(R.styleable.EmojiLayout_richIndicatorUnFocus);
            richMarginBottom = (int) array.getDimension(R.styleable.EmojiLayout_richMarginBottom, dip2px(getContext(), 5));
            richMarginTop = (int) array.getDimension(R.styleable.EmojiLayout_richMarginTop, dip2px(getContext(), 5));
            numColumns = array.getInteger(R.styleable.EmojiLayout_richLayoutNumColumns, 7);
            numRows = array.getInteger(R.styleable.EmojiLayout_richLayoutNumRows, 5);
            pageCount = numColumns * numRows - 1;
            array.recycle();
        }

        if (focusIndicator == null) {
            focusIndicator = getContext().getResources().getDrawable(R.drawable.rich_page_indicator_focused);
        }

        if (unFocusIndicator == null) {
            unFocusIndicator = getContext().getResources().getDrawable(R.drawable.rich_page_indicator_unfocused);
        }

        initViews();
    }


    public void setEditText(AppCompatActivity activity, RichEditText mEditText, ImageView expressionPublishIv) {
        editTextEmoji=mEditText;
        keyboardHelper = KeyboardHelper.setup(activity, editTextEmoji, edittextBarLlFaceContainer, expressionPublishIv);
    }

    /**
     * 初始化View
     */
    private void initViews() {

        int size = dip2px(getContext(), 5);

        int marginSize = dip2px(getContext(), 5);

        // 表情list
        reslist = SmileUtils.getTextList();

        int viewSize = (int) Math.ceil(reslist.size() * 1.0f / pageCount);

        // 初始化表情viewpager
        List<View> views = new ArrayList<>();
        for (int i = 0; i < viewSize; i++) {
            View gv = getGridChildView(i + 1);
            views.add(gv);
        }

        ImageView imageViewFace;
        imageFaceViews = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            LayoutParams margin = new LayoutParams(size, size);
            margin.setMargins(marginSize, 0, 0, 0);
            imageViewFace = new ImageView(getContext());
            imageViewFace.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            imageFaceViews[i] = imageViewFace;
            if (i == 0) {
                imageFaceViews[i].setBackground(focusIndicator);
            } else {
                imageFaceViews[i].setBackground(unFocusIndicator);
            }
            edittextBarViewGroupFace.addView(imageFaceViews[i], margin);
        }

        emojiKeyboard.setAdapter(new ExpressionPagerAdapter(views));
        emojiKeyboard.addOnPageChangeListener(new GuidePageChangeListener());

    }


    /**
     * 获取表情的gridview的子view
     */
    private View getGridChildView(int i) {
        View view = View.inflate(getContext(), R.layout.layout_expression_gridview, null);
        LockGridView gv = (LockGridView) view.findViewById(R.id.gridview);
        gv.setNumColumns(numColumns);
        LayoutParams layoutParams = (LayoutParams) gv.getLayoutParams();
        layoutParams.setMargins(0, richMarginTop, 0, richMarginBottom);

        List<String> list = new ArrayList<String>();

        int startInd = (i - 1) * pageCount;
        if ((startInd + pageCount) >= reslist.size()) {
            list.addAll(reslist.subList(startInd, startInd + (reslist.size() - startInd)));
        } else {
            list.addAll(reslist.subList(startInd, startInd + pageCount));
        }
        list.add(deleteIconName);
        final SmileImageExpressionAdapter smileImageExpressionAdapter = new SmileImageExpressionAdapter(getContext(), 1, list);
        gv.setAdapter(smileImageExpressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = smileImageExpressionAdapter.getItem(position);
                try {
                    if (!deleteIconName.equals(filename)) { // 不是删除键，显示表情
                        (editTextEmoji).insertIconString(filename);
                        //                        (editTextEmoji).insertIcon(filename);
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(editTextEmoji.getText())) {
                            int selectionStart = editTextEmoji.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = editTextEmoji.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                int end = tempStr.lastIndexOf("]");// 获取最后一个表情的位置
                                if (i != -1 && end == (selectionStart - 1)) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        editTextEmoji.getEditableText().delete(i, selectionStart);
                                    //                                    else
                                    //                                        editTextEmoji.getEditableText().delete(selectionStart - 1,
                                    //                                                selectionStart);
                                } /*else {
                                    editTextEmoji.getEditableText().delete(selectionStart - 1, selectionStart);
                                }*/
                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    /**
     * dip转为PX
     */
    private int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageFaceViews.length; i++) {
                imageFaceViews[arg0].setBackground(focusIndicator);
                if (arg0 != i) {
                    imageFaceViews[i].setBackground(unFocusIndicator);
                }
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (keyboardHelper == null) {
            throw new IllegalArgumentException("Please invoke setup method!");
        }
    }

}
