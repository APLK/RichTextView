package example.ricktextview.view.richtext;

import android.app.Activity;
import android.content.Intent;

import example.ricktextview.TopicListActivity;
import example.ricktextview.UserListActivity;


public class JumpUtil {

    public static void goToUserList(Activity activity, int code) {
        Intent intent = new Intent(activity, UserListActivity.class);
        activity.startActivityForResult(intent, code);
    }


    public static void goToTopicList(Activity activity, int code) {
        Intent intent = new Intent(activity, TopicListActivity.class);
        activity.startActivityForResult(intent, code);
    }
}
