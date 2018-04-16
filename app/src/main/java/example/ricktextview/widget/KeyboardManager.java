package example.ricktextview.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

import static example.ricktextview.util.DeviceUtils.getScreenDensity;


public class KeyboardManager {

	private static final String TAG = "KeyboardManager";
	private static final boolean debug = true;
	private static final String PREFERENCES_NAME = "keyboard";
	private static final String KEY_KEYBOARD_HEIGHT = "keyboard_height";
	private static final int DEFAULT_KEYBOARD_HEIGHT = 300;
	private final Activity activity;
	private final InputMethodManager inputMethodManager;
	private final OnKeyboardVisibilityListener internalListener; // internal
	private final SharedPreferences sharedPreferences;
	private OnKeyboardVisibilityListener listener;
	private static int screenHeight = 0;

	private static final int TITLE_HEIGHT = 0;

	public KeyboardManager(Activity activity) {
		this.activity = activity;
		this.inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		this.sharedPreferences = activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		this.internalListener = new MyOnKeyboardVisibilityListener();
		this.monitorKeyboard();
	}

	/**
	 * 设置键盘监听
	 *
	 * @param onKeyboardVisibilityListener
	 */
	public void setOnKeyboardVisibilityListener(OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
		this.listener = onKeyboardVisibilityListener;
	}

	public InputMethodManager getInputMethodManager() {
		return inputMethodManager;
	}

	public void showSoftInput(View view) {
		inputMethodManager.showSoftInput(view, 0);
	}

	public void hideSoftInput(View view) {
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 获取最后一次键盘高度
	 *
	 * @param activity
	 * @return
	 */
	public static int getLastKnowKeyboardHeight(Activity activity) {
		final SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
//		float scale = activity.getResources().getDisplayMetrics().density;
//		int defaultHeight = (int) (DEFAULT_KEYBOARD_HEIGHT * scale + 0.5f);
		int height = sharedPreferences.getInt(KEY_KEYBOARD_HEIGHT, 787);
		return height;
	}

	/**
	 * 获取键盘高度
	 *
	 * @return
	 */
	public int getCurrentKeyboardHeight() {
		Rect r = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
		int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
		int softInputHeight = screenHeight - r.bottom;
		if (Build.VERSION.SDK_INT >= 18) {
			// When SDK Level >= 18, the softInputHeight will contain the softKeyboardHeight of softButtonsBar (if has)
			softInputHeight = softInputHeight - getSoftButtonsBarHeight(activity);
		}
		return softInputHeight;
	}

	/**
	 * 获取软件盘的高度
	 *
	 * @return
	 */
	public int getSupportSoftInputHeight() {
		Rect r = new Rect();
		/**
		 * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
		 * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
		 */
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
		//获取屏幕的高度
		int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
		//计算软件盘的高度
		int softInputHeight = screenHeight - r.bottom;
		/**
		 * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
		 * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
		 * 我们需要减去底部虚拟按键栏的高度（如果有的话）
		 */
		if (Build.VERSION.SDK_INT >= 20) {
			// When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
			softInputHeight = softInputHeight - getSoftButtonsBarHeight();
		}
		//存一份到本地
		final SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
//		float scale = activity.getResources().getDisplayMetrics().density;
//		int defaultHeight = (int) (DEFAULT_KEYBOARD_HEIGHT * scale + 0.5f);
//		int height = sharedPreferences.getInt(KEY_KEYBOARD_HEIGHT, 1011);
		if (softInputHeight > 0) {
			sharedPreferences.edit().putInt(KEY_KEYBOARD_HEIGHT, softInputHeight).apply();
		}
		return softInputHeight;
	}

	/**
	 * 底部虚拟按键栏的高度
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private int getSoftButtonsBarHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		//这个方法获取可能不是真实屏幕的高度
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int usableHeight = metrics.heightPixels;
		//获取当前屏幕的真实高度
		activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		int realHeight = metrics.heightPixels;
		if (realHeight > usableHeight) {
			return realHeight - usableHeight;
		} else {
			return 0;
		}
	}

	public boolean isShowingKeyboard() {
		return getSupportSoftInputHeight() > 0;
//		return getCurrentKeyboardHeight() > 0;
	}


	/**
	 * 监控键盘
	 */
	private void monitorKeyboard() {
		View activityRootView = activity.getWindow().getDecorView().getRootView();
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			private boolean wasOpened;
			private final Rect r = new Rect();

			@Override
			public void onGlobalLayout() {
				if (inputMethodManager.isFullscreenMode()) {
					if (debug) {
						Log.i(TAG, "monitorKeyboard, fullscreen mode");
					}
					return;
				}
				int keyboardHeight = getSupportSoftInputHeight();
//				int keyboardHeight = getCurrentKeyboardHeight();
				if (debug) {
					Log.i(TAG, "monitorKeyboard, keyboard height:" + keyboardHeight);
				}
				boolean isOpen;
				if (keyboardHeight > 0) {
					isOpen = true;
				} else {
					isOpen = false;
				}
				if (isOpen == wasOpened) {
					return;
				}
				wasOpened = isOpen;
				keyboardHeight = isOpen ? keyboardHeight : 0;
				internalListener.onKeyboardVisibilityChanged(isOpen, keyboardHeight);
				if (listener != null) {
					listener.onKeyboardVisibilityChanged(isOpen, keyboardHeight);
				}
			}
		});
	}

	//获取屏幕原始尺寸高度，包括虚拟功能键高度
	public static int getDpi(Context context){
		int dpi = 0;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi=displayMetrics.heightPixels;
		}catch(Exception e){
			e.printStackTrace();
		}
		return dpi;
	}

	public static int getScreenHeight(Context context) {
		int top = 0;
		if (context instanceof Activity) {
			top = ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
			if (top == 0) {
				top = (int) (TITLE_HEIGHT * getScreenDensity(context));
			}
		}
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		screenHeight = dm.heightPixels - top;
		return screenHeight;
	}

	/**
	 * 获取 虚拟按键的高度
	 * @param context
	 * @return
	 */
	public static  int getBottomStatusHeight(Context context){
		int totalHeight = getDpi(context);

		int contentHeight = getScreenHeight(context);

		return totalHeight  - contentHeight;
	}

	/**
	 * 获取虚拟键高度（返回，HOME，MENU）
	 *
	 * @param context
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static int getSoftButtonsBarHeight(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		int usableHeight = metrics.heightPixels;
		windowManager.getDefaultDisplay().getRealMetrics(metrics);
		int realHeight = metrics.heightPixels;
		if (realHeight > usableHeight) {
			return realHeight - usableHeight;
		} else {
			return 0;
		}
	}

	private class MyOnKeyboardVisibilityListener implements OnKeyboardVisibilityListener {
		@Override
		public void onKeyboardVisibilityChanged(boolean visible, int height) {
			if (visible) {
				sharedPreferences.edit().putInt(KEY_KEYBOARD_HEIGHT, height).apply();
			}
		}
	}

	/**
	 * 键盘高度监听
	 */
	public interface OnKeyboardVisibilityListener {
		void onKeyboardVisibilityChanged(boolean visible, int height);
	}
}
