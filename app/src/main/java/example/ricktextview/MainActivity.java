package example.ricktextview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.ricktextview.view.richtext.JumpUtil;
import example.ricktextview.view.richtext.RichEditBuilder;
import example.ricktextview.view.richtext.RichEditText;
import example.ricktextview.view.richtext.RichTextView;
import example.ricktextview.view.richtext.TopicModel;
import example.ricktextview.view.richtext.UserModel;
import example.ricktextview.view.richtext.listener.OnEditTextUtilJumpListener;
import example.ricktextview.view.richtext.listener.SpanAtUserCallBack;
import example.ricktextview.view.richtext.listener.SpanTopicCallBack;
import example.ricktextview.view.richtext.listener.SpanUrlCallBack;
import example.ricktextview.widget.EmojiKeyboardLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_content)
    RichEditText mEtContent;
    @BindView(R.id.keyboardLayout)
    EmojiKeyboardLayout mKeyboardLayout;
    @BindView(R.id.tv_content)
    RichTextView mTvContent;
    @BindView(R.id.expression_publish_iv)
    ImageView mExpressionPublishIv;
    @BindView(R.id.gambit_publish_imgv)
    ImageView mGambitPublishImgv;
    @BindView(R.id.aite_publish_iv)
    ImageView mAitePublishIv;

    private List<TopicModel> topicList = new ArrayList<>();

    private List<UserModel> friendList = new ArrayList<>();


    public final static int REQUEST_USER_CODE_INPUT = 1111;
    public final static int REQUEST_USER_CODE_CLICK = 2222;
    public final static int REQUEST_TOPIC_CODE_INPUT = 3333;
    public final static int REQUEST_TOPIC_CODE_CLICK = 4444;
    private String content = "";
    private List<UserModel> nameModuleList = new ArrayList<>();
    private List<TopicModel> topicModuleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        initData();
        mKeyboardLayout.setEditText(this, mEtContent,mExpressionPublishIv);

        mGambitPublishImgv.setOnClickListener(this);
        mAitePublishIv.setOnClickListener(this);

        RichEditBuilder richEditBuilder = new RichEditBuilder();
        richEditBuilder.setEditText(mEtContent)
                .setTopicModels(topicList)
                .setUserModels(friendList)
                .setColorAtUser("#FDA129")
                .setColorTopic("#FF4081")
                .setEditTextAtUtilJumpListener(new OnEditTextUtilJumpListener() {
                    @Override
                    public void notifyAt() {
                        JumpUtil.goToUserList(MainActivity.this, MainActivity.REQUEST_USER_CODE_INPUT);
                    }


                    @Override
                    public void notifyTopic() {
                        JumpUtil.goToTopicList(MainActivity.this, MainActivity.REQUEST_TOPIC_CODE_INPUT);
                    }
                })
                .builder();


        mTvContent.setSpanAtUserCallBackListener(new SpanAtUserCallBack() {
            @Override
            public void onClick(View view, UserModel userModel1) {
                Toast.makeText(MainActivity.this, userModel1.getUser_name(), Toast.LENGTH_SHORT).show();
            }
        });
        mTvContent.setSpanTopicCallBackListener(new SpanTopicCallBack() {
            @Override
            public void onClick(View view, TopicModel topicModel) {
                Toast.makeText(MainActivity.this, topicModel.getTopicName(), Toast.LENGTH_SHORT).show();
            }
        });
        mTvContent.setSpanUrlCallBackListener(new SpanUrlCallBack() {
            @Override
            public void phone(View view, String phone) {
                if (phone != null && phone.length() > 0) {
                    Toast.makeText(MainActivity.this, phone, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void url(View view, String url) {
                Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
            }
        });
        content = "这是测试#话题话题# 文本哟 www.baidu.com " +
                "来2个@某个人 @22222  @kkk " + "来2个电话 13245685478,0717225478" +
                "来3个表情[发呆][眨眼][痛哭]，最后随便加点超过3行的数据就行了131657848785满3行了吗?还没有满吗?这下够了吧!";
        mTvContent.setMaxLines(3);
        mTvContent.setEllipsize(TextUtils.TruncateAt.END);
        mTvContent.setRichText(content, nameModuleList, topicModuleList);
    }

    private void initData() {
        nameModuleList.clear();
        topicModuleList.clear();

        UserModel userModel = new UserModel();
        userModel.setUser_name("@22222 ");
        userModel.setUser_id(2222);
        nameModuleList.add(userModel);
        userModel = new UserModel();
        userModel.setUser_name("@kkk ");
        userModel.setUser_id(23333);
        nameModuleList.add(userModel);

        TopicModel topicModel = new TopicModel();
        topicModel.setTopicId(333);
        topicModel.setTopicName("#话题话题# ");
        topicModuleList.add(topicModel);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gambit_publish_imgv://话题
                JumpUtil.goToTopicList(MainActivity.this, MainActivity.REQUEST_TOPIC_CODE_CLICK);
                break;
            case R.id.aite_publish_iv://艾特
                JumpUtil.goToUserList(MainActivity.this, MainActivity.REQUEST_USER_CODE_CLICK);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_USER_CODE_CLICK:
                    mEtContent.resolveAtResult((UserModel) data.getSerializableExtra(UserListActivity.DATA));
                    break;
                case REQUEST_USER_CODE_INPUT:
                    mEtContent.resolveAtResultByEnterAt((UserModel) data.getSerializableExtra(UserListActivity.DATA));
                    break;
                case REQUEST_TOPIC_CODE_INPUT:
                    mEtContent.resolveTopicResultByEnter((TopicModel) data.getSerializableExtra(TopicListActivity.DATA));
                    break;
                case REQUEST_TOPIC_CODE_CLICK:
                    mEtContent.resolveTopicResult((TopicModel) data.getSerializableExtra(TopicListActivity.DATA));
                    break;
            }
        }

    }
}
