package example.ricktextview.widget;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class KeyboardHelper {

    private Activity activity;
    private View customKeyboardLayout;
    private EditText editText;
    private View emojiToggleView;
    private KeyboardManager keyboardManager;

    private KeyboardHelper(Activity activity, EditText editText, View customKeyboardLayout, View emojiToggleView) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.activity = activity;
        this.keyboardManager = new KeyboardManager(activity);
        this.keyboardManager.setOnKeyboardVisibilityListener(onKeyboardVisibilityListener);
        if (customKeyboardLayout == null) {
            throw new IllegalArgumentException("customKeyboardLayout can not be null!");
        }
        if (emojiToggleView == null) {
            throw new IllegalArgumentException("emojiToggleView can not be null!");
        }
        if (editText == null) {
            throw new IllegalArgumentException("editText can not be null!");
        }
        this.customKeyboardLayout = customKeyboardLayout;
        this.editText = editText;
        this.emojiToggleView = emojiToggleView;
        this.emojiToggleView.setOnClickListener(onClickListener);
        emojiToggleView.setSelected(true);
    }

    public static KeyboardHelper setup(Activity activity, EditText editText, View customKeyboardLayout, View emojiToggleView) {
        return new KeyboardHelper(activity, editText, customKeyboardLayout, emojiToggleView);
    }


    public boolean onBackPressed() {
        if (isCustomKeyboardVisible()) {
            hideCustomKeyboard();
            return true;
        } else if (keyboardManager.isShowingKeyboard()) {
            keyboardManager.hideSoftInput(editText);
            return true;
        } else {
            return false;
        }
    }

    public boolean isShowedKeyboard = false;
    private KeyboardManager.OnKeyboardVisibilityListener onKeyboardVisibilityListener = new KeyboardManager.OnKeyboardVisibilityListener() {
        @Override
        public void onKeyboardVisibilityChanged(boolean visible, int height) {
            if (visible == true) {
                if (emojiToggleView != null) {
                    emojiToggleView.setSelected(true);
                }
                isShowedKeyboard = true;
                // After system keyboard is open, reset SoftInputMode to resize
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                if (isCustomKeyboardVisible()) {
                    // Emoji keyboard is being displayed
                    customKeyboardLayout.getLayoutParams().height = 0;
                    customKeyboardLayout.requestLayout();
                } else {
                    //                    if (emojiToggleView != null) {
                    //                        emojiToggleView.setSelected(true);
                    //                    }
                    // No keyboard is being displayed
                }
            } else {
                isShowedKeyboard = false;
                // After the keyboard is closed
                if (isCustomKeyboardVisible()) {
                    //                    if (emojiToggleView != null) {
                    //                        emojiToggleView.setSelected(false);
                    //                    }
                    // Emoji keyboard is being displayed, reset SoftInputMode to adjust_pan.
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                } else {
                    // No keyboard is being displayed ,reset SoftInputMode to resize.
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    //                    if (emojiToggleView != null) {
                    //                        emojiToggleView.setSelected(true);
                    //                    }
                }
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == emojiToggleView) {
                emojiToggleView.setSelected(!emojiToggleView.isSelected());
                if (v.isSelected()) {
                    showSystemKeyboard();
                } else {
                    showCustomKeyboard();
                }
            }
        }
    };


    public boolean isCustomKeyboardVisible() {
        return customKeyboardLayout.getLayoutParams().height != 0;
    }

    public void showSystemKeyboard() {
        editText.setFocusable(true);
        editText.requestFocus();
        keyboardManager.showSoftInput(editText);
    }

    private void showCustomKeyboard() {
        if (keyboardManager.isShowingKeyboard()) {
            // system keyboard is being displayed
            //            emojiToggleView.setSelected(true);
            keyboardManager.hideSoftInput(editText);
            customKeyboardLayout.getLayoutParams().height = keyboardManager.getLastKnowKeyboardHeight(activity);
            customKeyboardLayout.requestLayout();
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else if (!isCustomKeyboardVisible()) {
            // no keyboard is being displayed
            customKeyboardLayout.getLayoutParams().height = keyboardManager.getLastKnowKeyboardHeight(activity);
            customKeyboardLayout.requestLayout();
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else {
            // do nothing
        }
    }

    private void hideCustomKeyboard() {
        if (isCustomKeyboardVisible()) {
            customKeyboardLayout.getLayoutParams().height = 0;
            customKeyboardLayout.requestLayout();
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }
}
